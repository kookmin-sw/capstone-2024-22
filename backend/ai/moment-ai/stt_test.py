import whisper.whisper as whisper
import torch, os
from torcheval.metrics import WordErrorRate
from torchmetrics.text import CharErrorRate
import pandas as pd
from tqdm import tqdm
import random

# metric = WordErrorRate() # 0.403
metric = CharErrorRate() # 0.232 : Large-v3 / 0.166 : Large-v2


source_csv = "./emotion2vec/datas/AIHUB_for_Whisper.csv"
data_dir = './emotion2vec/datas/AIHUB/TOTAL'
source_data = pd.read_csv(source_csv)
source_data.drop_duplicates()
stt_model = whisper.load_model("large-v2")

print(stt_model.device)
preds = []
targets = []
for path, text in tqdm(random.sample(list(zip(source_data['path'], source_data['text'])), 100)):
  file_path = os.path.join(data_dir, path)
  if os.path.exists(file_path):
    res = stt_model.transcribe(os.path.join(data_dir, path))
    preds.append(res['text'])
    targets.append(text)
    print(f"\npredict : {res['text']}\nground truth : {text}\ntotal_CER = {metric(preds, targets)}\n")
    # metric.update(res['text'], text)

# print(metric.compute())
print(metric(preds, targets))

