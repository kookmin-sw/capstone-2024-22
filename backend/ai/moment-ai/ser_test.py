from funasr import AutoModel
from pydub import AudioSegment


# # MP4 파일 로드
# audio = AudioSegment.from_file("source/test.mp4", format="mp4")

# # WAV 파일로 저장
# audio.export("source/test.wav", format="wav")


'''
Using the finetuned emotion recognization model
rec_result contains {'feats', 'labels', 'scores'}
	extract_embedding=False: 9-class emotions with scores`
	extract_embedding=True: 9-class emotions with scores, along with features

9-class emotions:
    0: angry
    1: disgusted
    2: fearful
    3: happy
    4: neutral
    5: other
    6: sad
    7: surprised
    8: unknown
'''
model = AutoModel(model="iic/emotion2vec_base_finetuned", model_revision="v2.0.4")
wav_file = f"/Users/taejinpark/Desktop/moment-ai/source/test3.wav"
rec_result = model.generate(wav_file, output_dir="./outputs", granularity="utterance", extract_embedding=False)
print(rec_result)



# ##################################################################################################################################################################################################################################################################################################
# import argparse
# from dataclasses import dataclass
# import numpy as np

# import torch, os
# import torch.nn.functional as F
# import fairseq
# import librosa


# from funasr import AutoModel
# from pydub import AudioSegment
# from pydub.utils import mediainfo


# def get_parser():
#     parser = argparse.ArgumentParser(
#         description="extract emotion2vec features for downstream tasks"
#     )
#     parser.add_argument('--source_file', help='location of source wav files', required=True)
#     # parser.add_argument('--target_file', help='location of target npy files', required=True)
#     parser.add_argument('--model_dir', type=str, help='pretrained model', required=True)
#     parser.add_argument('--checkpoint_dir', type=str, help='checkpoint for pre-trained model', required=True)
#     parser.add_argument('--granularity', type=str, help='which granularity to use, frame or utterance', required=True)

#     return parser
  
  
# @dataclass
# class UserDirModule:
#     user_dir: str


# def feature_extract():
#     # region for train
#     # parser = get_parser()
#     # args = parser.parse_args()
#     # print(args)

#     # source_file = args.source_file
#     # target_file = args.target_file
#     # model_dir = args.model_dir
#     # checkpoint_dir = args.checkpoint_dir
#     # granularity = args.granularity
#     # endregion
    
    
#     # for testing
#     source_file = "./source/test.wav"
#     target_file = "./output/test.npy"
#     model_dir = "./emotion2vec/upstream"
#     checkpoint_dir = "./emotion2vec/emotion2vec_base/emotion2vec_base.pt"
#     granularity = "utterance"
    
#     # # MP4 파일 로드
#     # audio = AudioSegment.from_file(source_file, format="mp4")

#     # # WAV 파일로 저장
#     # source_file = os.path.splitext(source_file)[0]
#     # audio.export(os.path.join(source_file, "wav"), format="wav")

#     model_path = UserDirModule(model_dir)
#     fairseq.utils.import_user_module(model_path)
#     model, cfg, task = fairseq.checkpoint_utils.load_model_ensemble_and_task([checkpoint_dir])
#     model = model[0]
#     model.eval()
#     # model.cuda()

#     if source_file.endswith('.wav'):
#         wav, sr = sf.read(source_file)
#         channel = sf.info(source_file).channels
#         # assert sr == 16e3, "Sample rate should be 16kHz, but got {}in file {}".format(sr, source_file)
#         # assert channel == 1, "Channel should be 1, but got {} in file {}".format(channel, source_file)
        
#     with torch.no_grad():
#         # source = torch.from_numpy(wav).float().cuda()
#         source = torch.from_numpy(wav).float()
#         if task.cfg.normalize:
#             source = F.layer_norm(source, source.shape)
#         source = source.view(1, -1)
#         try:
#             feats = model.extract_features(source, padding_mask=None)
#             feats = feats['x'].squeeze(0).cpu().numpy()
#             if granularity == 'frame':
#                 feats = feats
#             elif granularity == 'utterance':
#                 feats = np.mean(feats, axis=0)
#             else:
#                 raise ValueError("Unknown granularity: {}".format(args.granularity))
#             # np.save(target_file, feats)
#         except:
#             Exception("Error in extracting features from {}".format(source_file))
            
#     return feats
  
  
# def ser_downstream():
#     return None
  
  
  



# def main():
#   feats = feature_extract()
#   print(feats)
  
# if __name__ == "__main__":
#   # main()
#   # for testing
#     source_file = "./AudioSeg.wav"
#     target_file = "./output/test.npy"
#     model_dir = "./emotion2vec/upstream"
#     checkpoint_dir = "./emotion2vec/emotion2vec_base/emotion2vec_base.pt"
#     granularity = "utterance"
    
#     model_path = UserDirModule(model_dir)
#     fairseq.utils.import_user_module(model_path)
#     model, cfg, task = fairseq.checkpoint_utils.load_model_ensemble_and_task([checkpoint_dir]) # model : [feature extractor]
#     model = model[0]
#     model.eval()
#     print(model)
#     # model.cuda()
#     exit()

#     # if source_file.endswith('.mp4'):
#     if source_file.endswith('.wav'):
#         # MP4 파일 로드
#         audio = AudioSegment.from_file(source_file)

#         # # sampling rates, channel 변경
#         # if audio.frame_rate != 16000:
#         #   print(f"sample rate changed : {audio.frame_rate} -> {16000}")
#         #   audio = audio.set_frame_rate(16000)
          
#         # if audio.channels != 1:
#         #   print(f"channel changed : {audio.channels} -> {1}")
#         #   audio = audio.set_channels(1)
        
#         print('AudioSegment test', '*'*20)
#         source = audio.get_array_of_samples()
#         source = np.array(source, dtype=np.float64)
        
#         print(source[source!=0])
#         print('data type', type(source), source.dtype, source.shape)
#         print('frame_rate', audio.frame_rate)
#         print('channels', audio.channels)
        
#         # audio.export('AudioSeg.wav', format="wav")
        
#         print('soundfile test', '*'*20)
#         import soundfile as sf
#         wav, sr = sf.read('AudioSeg.wav')
#         channels = sf.info('AudioSeg.wav').channels
        
#         print(wav[source!=0])
#         print('data type', type(wav), wav.dtype, wav.shape)
#         print("frame_rate", sr)
#         print("channels", channels)
        
        
#         print('difference test', '*'*20)
#         print((source != wav))
#         print(f"diff {source[source != wav]}\n{wav[source != wav]}")
        
        
#         print('normalize test', '*'*20)
#         source = torch.from_numpy(source).float()
#         wav = torch.from_numpy(wav).float()
#         print(source.dtype, source.mean())
#         print(wav.dtype, wav.mean())
        
#         source_norm = F.layer_norm(source, source.shape)
#         source_norm = source_norm.view(1, -1)
#         wav_norm = F.layer_norm(wav, wav.shape)
#         wav_norm = wav_norm.view(1, -1)
#         i = 0
#         print(source_norm[0][0], wav_norm[0][0])
#         print(source_norm[i*10:(i+1)*10])
#         print(wav_norm[i*10:(i+1)*10])
        
#         print(torch.equal(source_norm[i*10:(i+1)*10], wav_norm[i*10:(i+1)*10]))
#         print(torch.allclose(source_norm[i*10:(i+1)*10], wav_norm[i*10:(i+1)*10]))
#         # print(min(source))
        
        
#         source_feats = model.extract_features(source_norm, padding_mask=None)
#         source_feats = source_feats['x'].squeeze(0).cpu().detach().numpy()
#         wav_feats = model.extract_features(wav_norm, padding_mask=None)
#         wav_feats = wav_feats['x'].squeeze(0).cpu().detach().numpy()
        
#         print(source_feats[:10])
#         print(wav_feats[:10])
#         print(sum(sum(source_feats - wav_feats)))
        
        
        
        
        
        
        
        
#         # source = torch.from_numpy(audio).float()
#         # print(source)
        
#         # res = model(source)
