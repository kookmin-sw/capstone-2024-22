# todo : GPU, CPU State 상태 확인

import whisper.whisper as whisper
import argparse
import json, torch
import glob, os

from dataclasses import dataclass
import numpy as np

import torch.nn.functional as F
import fairseq

from funasr import AutoModel
from pydub import AudioSegment

@dataclass
class UserDirModule:
    user_dir: str

parser = argparse.ArgumentParser()

# parser.add_argument('--source-path', default="/Users/jykim/Desktop/moment/files", help="target directory path")
parser.add_argument('--source-path', default="./source", help="target directory path")
parser.add_argument('--file-path', default=None, help="target file path")
parser.add_argument('--whisper-version', default="tiny", choices=["tiny", "base", "small", "medium", "large", "large-v2", "large-v3"], help="whisper model version")
parser.add_argument('--save-path', default=None, help="save output(json) directory path")
parser.add_argument('--granularity', type=str, default="utterance", help='which granularity to use, frame or utterance')

args = parser.parse_args()


def run_model_on_gpu(models:dict, source_file, output):
  if torch.cuda.is_available():
    try:
      for model_name, model_config in models.items():
        output["log"].append(f"{model_name} running on gpu")
        
        if model_name == "whisper":
          model = model_config[0]
          model.to('cuda')
          result = model.transcribe(source_file)
          output["text"] = result["text"]
          
        elif model_name == "emotion2vec":
          # for demos
          result = {"sad": 75, "happy": 5, "angry": 7, "neutral": 10}
          output["emotions"] = result
          continue
          
          # for real test
          model, ser_config, ser_task = model_config
          model.to('cuda')
          model.eval()
          
          if source_file.endswith('.mp4'):
            audio = AudioSegment.from_file(source_file)
            source = audio.get_array_of_samples()
            source = np.array(source, dtype=np.float64)
            
            
          with torch.no_grad():
              # extract feature
              source = torch.from_numpy(source).float().cuda()

              if ser_task.cfg.normalize:
                  source = F.layer_norm(source, source.shape)
              source = source.view(1, -1)
              
              try:
                  feats = model.extract_features(source, padding_mask=None)
                  feats = feats['x'].squeeze(0).cpu().numpy()
                  if args.granularity == 'frame':
                      feats = feats
                  elif args.granularity == 'utterance':
                      feats = np.mean(feats, axis=0)
                  else:
                      raise ValueError("Unknown granularity")

              except:
                  Exception("Error in extracting features from {}".format(source_file))
                  
              # classifier
              
              output["emotion"]= feats
          
      
      output["status"] = "success"
      # return output
    
    except Exception as e:
      print(f"Failed to run model on GPU: {e}")
      output["error"] = f"{e}"
  
  
  # return output

def run_model_on_cpu(models:dict, source_file, output):
  try:
    for model_name, model_config in models.items():
      output["log"].append(f"{model_name} running on cpu")
      
      if model_name == "whisper":
        model = model_config[0]
        model.to('cpu')
        result = model.transcribe(source_file)
        output["text"] = result["text"]
        
      elif model_name == "emotion2vec":
        # for demos
        result = {"sad": 75, "happy": 5, "angry": 7, "neutral": 10}
        output["emotions"] = result
        continue
        
        # for real test
        model, ser_config, ser_task = model_config
        model.to('cpu')
        model.eval()
        
        if source_file.endswith('.mp4'):
          audio = AudioSegment.from_file(source_file)
          source = audio.get_array_of_samples()
          source = np.array(source, dtype=np.float64)
          
          
        with torch.no_grad():
            # extract feature
            source = torch.from_numpy(source).float()

            if ser_task.cfg.normalize:
                source = F.layer_norm(source, source.shape)
            source = source.view(1, -1)
            
            try:
                feats = model.extract_features(source, padding_mask=None)
                feats = feats['x'].squeeze(0).cpu().numpy()
                if args.granularity == 'frame':
                    feats = feats
                elif args.granularity == 'utterance':
                    feats = np.mean(feats, axis=0)
                else:
                    raise ValueError("Unknown granularity")

            except:
                Exception("Error in extracting features from {}".format(source_file))
                
            # classifier
            
            output["emotion"]= feats
        
    
    output["status"] = "success"
    # return output
  
  except Exception as e:
    print(f"Failed to run model on CPU: {e}")
    output["error"] = f"{e}"
  
  # return output



def main(file_name):  
  output = dict()
  
  output["text"] = None
  output["emotions"] = None
  output["status"] = "wait"
  output["error"] = None
  output["log"] = []
  
  source_file = os.path.join(args.source_path, file_name)

  # load model
  # print("\n\nload model ------------------------------------------------------------------------------")
  output["log"].append("load model")
  stt_model = whisper.load_model(args.whisper_version)
  
  ser_model_dir = "./emotion2vec/upstream"
  ser_checkpoint_dir = "./emotion2vec/emotion2vec_base/emotion2vec_base.pt"
  ser_classifer_dir = "./emotion2vec/emotion2vec_base/emotion2vec_classifier.pt"

  # for demos
  ser_model, ser_cfg, ser_task = None, None, None

  ## for real test
  # ser_model_path = UserDirModule(ser_model_dir)
  # fairseq.utils.import_user_module(ser_model_path)
  # ser_model, ser_cfg, ser_task = fairseq.checkpoint_utils.load_model_ensemble_and_task([ser_checkpoint_dir])
  # ser_model = ser_model[0]
  
  models_dict = {'whisper' : [stt_model], 'emotion2vec' : [ser_model, ser_cfg, ser_task]}

  
  # run model
  # print("\n\nrun model ------------------------------------------------------------------------------")
  output["log"].append("run model")
  
  # models in GPU
  # output = run_model_on_gpu(models_dict, source_file, output)
  run_model_on_gpu(models_dict, source_file, output)
  
  if output["status"] == "success":
      # print("Model ran successfully on GPU.")
      output["log"].append("Model ran successfully on GPU.")
  
  else:
      # if model failed on GPU, then run on CPU
      # output = run_model_on_cpu(models_dict, source_file, output)
      run_model_on_cpu(models_dict, source_file, output)
      if output["status"] == "success":
          # print("Model ran successfully on CPU.")
          output["log"].append("Model ran successfully on CPU.")
      else:
          # print("Model could not be run on GPU or CPU. Please check your model or system configuration.")
          output["status"] = "failure"
          output["log"].append("Model could not be run on GPU or CPU. Please check your model or system configuration.")
    


  
  # save outout
  if args.save_path:
    output_name = os.path.join(args.save_path, os.path.splitext(file_name)[0] + ".json")
    print(output_name)
    print("\n\nsave ------------------------------------------------------------------------------")
    with open(output_name, "wt") as fp:
      fp.write(json.dumps(output))
      fp.close()
  
  
  # print result
  # print("\n\nresult ------------------------------------------------------------------------------")
  # print(output)

  
  # return json.dumps(output)
  return output

if __name__ == "__main__":
  file_name = "test.mp4" if args.file_path is None else args.file_path
  print(file_name)
  result = main(file_name)
  print(result)