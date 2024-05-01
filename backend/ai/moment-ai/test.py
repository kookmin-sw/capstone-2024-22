import boto3
import pandas as pd

# todo : GPU, CPU State 상태 확인
import sys, os
# print(os.getcwd())
# print(sys.path)
# print(os.path.abspath(os.path.join(os.path.dirname(__name__), '.')))
# sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__name__), '.')))
# from .whisper import whisper
import whisper.whisper as whisper
import argparse
import json, torch
import glob

from dataclasses import dataclass
import numpy as np

import torch.nn.functional as F
import fairseq

# for SER task
from funasr import AutoModel
import subprocess
from emotion2vec.iemocap_downstream.model import BaseModel



@dataclass
class UserDirModule:
    user_dir: str

parser = argparse.ArgumentParser()

# parser.add_argument('--source-path', default="/Users/jykim/Desktop/moment/files", help="target directory path")
parser.add_argument('--source-path', default="./source", help="target directory path")
parser.add_argument('--file-path', default=None, help="target file path")
parser.add_argument('--whisper-version', default="large-v2", choices=["tiny", "base", "small", "medium", "large", "large-v2", "large-v3"], help="whisper model version")
parser.add_argument('--save-path', default=None, help="save output(json) directory path")
parser.add_argument('--granularity', type=str, default="utterance", help='which granularity to use, frame or utterance')

args = parser.parse_args()


def run_model_on_gpu(models:dict, source_file, output):
  if torch.cuda.is_available():
    try:
      for model_name, model_config in models.items():
        if model_name == "whisper":
          # # for demo
          # output["text"] = "안녕하세요."
          # continue

          # for real test
          model = model_config[0]
          model.to('cuda')
          result = model.transcribe(source_file)
          output["text"] = result["text"]
          
        elif model_name == "emotion2vec":
          labels = ["neutral", "happy", "angry", "sad", "disgust"]
          # # for demos
          # # labels : neutral : 0, happy : 1, angry : 2, sad : 3, disgust : 4
          # result = {"sad": 75.3, "happy": 4.7, "angry": 7.1, "neutral": 6.9, "disgust": 6}
          # output["emotions"] = result
          # continue
          
          # for real test
          model = model_config[0]
          model.model.to('cuda')

          wav_source_file = None
          if not source_file.endswith('.wav'):
            wav_source_file = os.path.splitext(source_file)[0] + '.wav'
            command = f"ffmpeg -i {source_file} -ab 160k -ac 1 -ar 16000 -vn {wav_source_file}"
            subprocess.call(command, shell=True)
            
          else:
            wav_source_file = source_file
            
          if not os.path.exists(wav_source_file):
            output['status'] = '400'
            output['error'] = f'no such file : {wav_source_file}. failed to make .wav from ffmpeg'
            continue
          
          rec_result = model.generate(wav_source_file, output_dir="./outputs", granularity="utterance", extract_embedding=False)[0]
          scores = rec_result['scores']
          output["emotions"] = dict(zip(labels, scores))
          
          if os.path.exists(wav_source_file):
            os.remove(wav_source_file)
          
      output['status'] = '200'
      output['error'] = None
      # return output
    
    except Exception as e:
      print(f"Failed to run model on GPU: {e}")
      output['status'] = '400'
      output["error"] = f"{e}"
  
  else:
    output['status'] = '400'
    output["error"] = "model cannot run on gpu"
  
  
  # return output

def run_model_on_cpu(models:dict, source_file, output):
  try:
    for model_name, model_config in models.items():
      
      if model_name == "whisper":
        # # for demo
        # output["text"] = "안녕하세요."
        # continue

        # for real test
        model = model_config[0]
        model.to('cpu')
        result = model.transcribe(source_file)
        output["text"] = result["text"]
        
      elif model_name == "emotion2vec":
        labels = ["neutral", "happy", "angry", "sad", "disgust"]
        # # for demos
        # # labels = {"neutral" : 0, "happy" : 1, "angry" : 2, "sad" : 3, "disgust" : 4}
        # result = {"sad": 75.3, "happy": 4.7, "angry": 7.1, "neutral": 6.9, "disgust": 6}
        # output["emotions"] = result
        # continue
        
        # for real test
        model = model_config[0]
        model.model.to('cpu')

        wav_source_file = None
        if not source_file.endswith('.wav'):
          wav_source_file = os.path.splitext(source_file)[0] + '.wav'
          command = f"ffmpeg -i {source_file} -ab 160k -ac 1 -ar 16000 -vn {wav_source_file}"
          subprocess.call(command, shell=True)
          
        else:
          wav_source_file = source_file
          
        if not os.path.exists(wav_source_file):
            output['status'] = '400'
            output['error'] = f'no such file : {wav_source_file}. failed to make .wav from ffmpeg'
            continue
        
        rec_result = model.generate(wav_source_file, output_dir="./outputs", granularity="utterance", extract_embedding=False)[0]
        scores = rec_result['scores']
        output["emotions"] = dict(zip(labels, scores))
        
        if os.path.exists(wav_source_file):
          os.remove(wav_source_file)
        
    output['status'] = '200'
    output['error'] = None
    # return output
  
  except Exception as e:
    print(f"Failed to run model on CPU: {e}")
    output['status'] = '400'
    output["error"] = f"{e}"
  
  # return output



def main(file_name, file_path=None):  
  # config model
  print("\n\nconfiguration of datas ------------------------------------------------------------------------------")
  output = dict()
  
  output["text"] = None
  output["emotions"] = None
  output["status"] = "400"
  output["error"] = "not implements error"
  output["file_name"] = file_name
  output["file_path"] = file_path
  
  # connect s3
  print("\n\nconnect s3 and download file ------------------------------------------------------------------------------")
  key_csv = pd.read_csv('s3_key.csv')
  ACCESS_KEY = key_csv['Access key ID'].to_list()[0]
  SECRET_KEY = key_csv['Secret access key'].to_list()[0]
  
  bucket_name = 'kmumoment'
  region = 'ap-northeast-2'
  prefix = 'users'

  s3 = boto3.client('s3', aws_access_key_id=ACCESS_KEY, aws_secret_access_key=SECRET_KEY)
  s3_file = os.path.join(file_path, file_name)
  source_file = os.path.join('./source', file_name)
  
  try:
    s3.download_file(bucket_name, s3_file, source_file)
  
  except:
    output['status'] = '404'
    output['error'] = 's3 download_file error'
    return output
    

  # load model
  print("\n\nload model ------------------------------------------------------------------------------")
  stt_model = whisper.load_model(args.whisper_version)

  # ser_classifer_dir = None
  ser_classifer_dir = "./emotion2vec/emotion2vec_base/emotion2vec_classifier.pt"
  ser_model =  AutoModel(model="iic/emotion2vec_base_finetuned", model_revision="v2.0.4")
  
  if ser_classifer_dir:
    ser_classifer = BaseModel()
    # ser_classifer.load_state_dict(torch.load(ser_classifer_dir))
    ser_model.model.proj = ser_classifer

  
  models_dict = {'whisper': [stt_model], 'emotion2vec': [ser_model]}

  
  # run model
  print("\n\nrun model ------------------------------------------------------------------------------")
  
  # models in GPU
  # output = run_model_on_gpu(models_dict, source_file, output)
  run_model_on_gpu(models_dict, source_file, output)
  
  if output["status"] == "200":
      # print("Model ran successfully on GPU.")
      pass
  
  else:
      # if model failed on GPU, then run on CPU
      # output = run_model_on_cpu(models_dict, source_file, output)
      run_model_on_cpu(models_dict, source_file, output)
      if output["status"] == "200":
          # print("Model ran successfully on CPU.")
          pass

      else:
          # print("Model could not be run on GPU or CPU. Please check your model or system configuration.")
          output["status"] = "400"
          output['error'] = "Model could not be run on GPU or CPU. Please check your model or system configuration."
    
  if os.path.exists(source_file):
    os.remove(source_file)
    

  
  # save outout
  if args.save_path:
    output_name = os.path.join(args.save_path, os.path.splitext(file_name)[0] + ".json")
    print(output_name)
    print("\n\nsave ------------------------------------------------------------------------------")
    with open(output_name, "wt") as fp:
      fp.write(json.dumps(output))
      fp.close()

  return output

if __name__ == "__main__":
  # file_name = "../moment_ai/test.mp4" if args.file_path is None else args.file_path
  file_path = 'users/1/'
  file_name = '2024-04-30T10:27:06.293422421.mp4'
  print(file_path, file_name)
  result = main(file_path=file_path, file_name=file_name)
  print('results :', result)