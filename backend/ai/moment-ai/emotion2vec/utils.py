import os
import pandas as pd
import torchaudio
from torch.utils.data import Dataset, DataLoader
from sklearn.model_selection import train_test_split



class CustomDataset(Dataset):
    def __init__(self, csv_file):
        self.data = pd.read_csv(csv_file)

    def __len__(self):
        return len(self.data)

    def __getitem__(self, idx):
        file_name = self.data.iloc[idx]['path']
        label = self.data.iloc[idx]['labels']
        
        if os.path.splitext(file_name)[-1] == '.wav':
            feat = None
            
        elif os.path.splitext(file_name)[-1] == '.npy':
            feat = None
        
        return feat, label


if __name__ == '__main__':
  # CSV 파일 경로 및 오디오 파일이 있는 루트 디렉토리 경로
  csv_file = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'datas/TESS.csv')

  # 데이터셋 객체 생성
  dataset = CustomDataset(csv_file)

  # 데이터셋 분할
  train_dataset, test_dataset = train_test_split(dataset, test_size=0.2, random_state=42)
  train_dataset, val_dataset = train_test_split(train_dataset, test_size=0.1, random_state=42)

  # 데이터로더 생성
  batch_size = 2
  train_dataloader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
  val_dataloader = DataLoader(val_dataset, batch_size=batch_size, shuffle=False)
  test_dataloader = DataLoader(test_dataset, batch_size=batch_size, shuffle=False)
  
  for batch in train_dataloader:
    print(batch)
    break
