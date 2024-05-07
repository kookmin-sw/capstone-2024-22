import os
import pandas as pd
import numpy as np
from pathlib import Path
import wandb

from funasr import AutoModel
from pydub import AudioSegment

from train_utils import extract_features, train_one_epoch, validate_and_test, load_dataloader, NpyDataset
from sklearn.model_selection import train_test_split
from iemocap_downstream.model import BaseModel


import torch
from torch import nn, optim

import logging

logger = logging.getLogger('emotion2vec Downstream')


def fine_tuning(label_dict, datasets_csv):
    wandb.init(project="Capstone2024", name=f"emotion2vec")
    # set train hyperparameters
    torch.manual_seed(42)
    epochs = 1000
    batch_size = 128
    
    # load pretrained model
    device = 'cuda' if torch.cuda.is_available() else 'cpu'
    model = AutoModel(model="iic/emotion2vec_base", model_revision="v2.0.4")
    model.kwargs['device'] = device
    model.model.to(device)
    
    # extract features
    datasets_list = extract_features(datasets_csv, model, label_dict)
    
    # change classifier model to train
    classifier = BaseModel(input_dim=768, output_dim=len(label_dict))
    classifier.to(device)
    wandb.watch(classifier)

    # count_parameters(model)
    optimizer = optim.RMSprop(classifier.parameters(), lr=5e-4, momentum=0.9)
    scheduler = optim.lr_scheduler.CyclicLR(optimizer, base_lr=5e-4, max_lr=1e-3, step_size_up=10)
    criterion = nn.CrossEntropyLoss()
    
    test_wa_avg, test_ua_avg, test_f1_avg = 0., 0., 0.
    best_val_wa = 0
    best_val_wa_epoch = 0
    
    # data loader
    train_loader, val_loader, test_loader = load_dataloader(datasets_list, batch_size=batch_size)
    
    # set save directory
    save_dir = os.path.join(str(Path.cwd()), f"emotion2vec_classifier.pth")
    
    # Training loop
    for epoch in range(epochs):
        # train
        train_loss = train_one_epoch(classifier, optimizer, criterion, train_loader, device)
        scheduler.step()
        
        # Validation step
        val_wa, val_ua, val_f1 = validate_and_test(classifier, val_loader, device, num_classes=len(label_dict))

        if val_wa > best_val_wa:
            best_val_wa = val_wa
            best_val_wa_epoch = epoch
            torch.save(classifier.state_dict(), save_dir)

        # Print losses for every epoch
        logger.info(f"Epoch {epoch+1}, Training Loss: {train_loss/len(train_loader):.6f}, Validation WA: {val_wa:.2f}%; UA: {val_ua:.2f}%; F1: {val_f1:.2f}%")
        wandb.log(
            {
                "epoch": epoch,
                "train_loss": train_loss,
                "Validation WA": val_wa,
                "Validation UA": val_ua,
                "Validation F1": val_f1,
            }
        )

    ckpt = torch.load(save_dir)
    model.load_state_dict(ckpt, strict=True)
    test_wa, test_ua, test_f1 = validate_and_test(model, test_loader, device, num_classes=len(label_dict))
    logger.info(f"{best_val_wa_epoch + 1}, test WA {test_wa}%; UA {test_ua}%; F1 {test_f1}%")
    
    test_wa_avg += test_wa
    test_ua_avg += test_ua
    test_f1_avg += test_f1

    logger.info(f"Average WA: {test_wa_avg}%; UA: {test_ua_avg}%; F1: {test_f1_avg}%")
        
    wandb.finish()
    
    
    
if __name__ == "__main__":
    label_dict = labels = {"neutral" : 0, "happy" : 1, "angry" : 2, "sad" : 3, "disgust" : 4}
    datasets_csv = "./datas/SER_DATASETS.csv"
    
    fine_tuning(label_dict, datasets_csv=datasets_csv)
    