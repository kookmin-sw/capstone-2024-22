import os, torch
import pandas as pd
import numpy as np
import torchaudio
from torch.utils.data import Dataset, DataLoader
from sklearn.model_selection import train_test_split


def extract_features(csv_file, model):
    DATA_PATH = "../"
    datasets_csv = pd.read_csv(csv_file)
    datasets_list = []
    print(model.model)
    
    for wav_file, label in zip(datasets_csv['path'], datasets_csv['labels']):
        if not os.path.exists(os.path.join(DATA_PATH, wav_file)) or label is None:
            continue
        
        npy_file = os.path.join(DATA_PATH, os.path.splitext(wav_file)[0] + ".npy")
        
        if not os.path.exists(npy_file):
            print(os.path.join(DATA_PATH, wav_file), os.path.exists(os.path.join(DATA_PATH, wav_file)))
            npy_feature = model.generate(os.path.join(DATA_PATH, wav_file), granularity="utterance")
            np.save(npy_file, npy_feature)
        
        datasets_list.append((npy_file, label))

        if os.path.exists(os.path.join(DATA_PATH, wav_file)):
            os.remove(os.path.join(DATA_PATH, wav_file))
        
    return datasets_list


def load_dataloader(datasets_list):
    # 데이터셋 객체 생성
    print("load dataloader", "-"*20)
    dataset = NpyDataset(datasets_list)

    # 데이터셋 분할
    train_dataset, test_dataset = train_test_split(dataset, test_size=0.2, random_state=42)
    train_dataset, val_dataset = train_test_split(test_dataset, test_size=0.1, random_state=42)
    
    train_loader = DataLoader(train_dataset, batch_size=16, shuffle=True)
    val_loader = DataLoader(val_dataset, batch_size=16, shuffle=False)
    test_loader = DataLoader(test_dataset, batch_size=16, shuffle=False)
    
    return train_loader, val_loader, test_loader


class NpyDataset(Dataset):
    def __init__(self, data_list):
        self.datas = data_list

    def __len__(self):
        return len(self.datas)

    def __getitem__(self, idx):
        feat = np.load(self.data_list[idx])
        feat = torch.from_numpy(feat)
        label = self.data_list[idx]
        
        return feat, label
    
    
def train_one_epoch(model, optimizer, criterion, train_loader, device):
    model.train()
    train_loss = 0
    for batch in train_loader:
        feats, labels = batch

        feats = feats.to(device)
        labels = labels.to(device)
        
        optimizer.zero_grad()
        outputs = model(feats)
        
        loss = criterion(outputs, labels.long())

        train_loss += loss.item()
        
        loss.backward()
        optimizer.step()

    return train_loss


@torch.no_grad()
def validate_and_test(model, data_loader, device, num_classes):
    model.eval()
    correct, total = 0, 0

    # unweighted accuracy
    unweightet_correct = [0] * num_classes
    unweightet_total = [0] * num_classes

    # weighted f1
    tp = [0] * num_classes
    fp = [0] * num_classes
    fn = [0] * num_classes

    for batch in data_loader:
        eats, labels = batch

        feats = feats.to(device)
        labels = labels.to(device)
        
        outputs = model(feats)

        _, predicted = torch.max(outputs.data, 1)
        
        total += labels.size(0)
        correct += (predicted == labels.long()).sum().item()
        for i in range(len(labels)):
            unweightet_total[labels[i]] += 1
            if predicted[i] == labels[i]:
                unweightet_correct[labels[i]] += 1
                tp[labels[i]] += 1
            else:
                fp[predicted[i]] += 1
                fn[labels[i]] += 1

    weighted_acc = correct / total * 100
    unweighted_acc = compute_unweighted_accuracy(unweightet_correct, unweightet_total) * 100
    weighted_f1 = compute_weighted_f1(tp, fp, fn, unweightet_total) * 100

    return weighted_acc, unweighted_acc, weighted_f1

def inference(model, ):
    pass


def compute_unweighted_accuracy(list1, list2):
    result = []
    for i in range(len(list1)):
        result.append(list1[i] / list2[i])
    return sum(result)/len(result)


def compute_weighted_f1(tp, fp, fn, unweightet_total):
    f1_scores = []
    num_classes = len(tp)
    
    for i in range(num_classes):
        if tp[i] + fp[i] == 0:
            precision = 0
        else:
            precision = tp[i] / (tp[i] + fp[i])
        if tp[i] + fn[i] == 0:
            recall = 0
        else:
            recall = tp[i] / (tp[i] + fn[i])
        if precision + recall == 0:
            f1_scores.append(0)
        else:
            f1_scores.append(2 * precision * recall / (precision + recall))
            
    wf1 = sum([f1_scores[i] * unweightet_total[i] for i in range(num_classes)]) / sum(unweightet_total)
    return wf1
