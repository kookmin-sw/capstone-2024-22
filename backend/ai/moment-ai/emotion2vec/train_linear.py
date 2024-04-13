import os
from pathlib import Path

from funasr import AutoModel
from pydub import AudioSegment

from iemocap_downstream.model import BaseModel
from iemocap_downstream.utils import train_one_epoch, validate_and_test

import torch
from torch import nn, optim

import logging

logger = logging.getLogger('emotion2vec Downstream')

def load_dataloader(dataset_path_list):
    
    return

def fine_tuning(label_dict, dataset_path_list):
    # set train hyperparameters
    torch.manual_seed(42)
    epochs = 100
    
    # load pretrained model
    model = AutoModel(model="iic/emotion2vec_base_finetuned", model_revision="v2.0.4").model

    # freeze
    for p in model.parameters():
        p.requires_grad = False
    
    # change classifier model to train
    device = 'cuda' if torch.cuda.is_available() else 'cpu'
    model.proj = BaseModel(input_dim=768, output_dim=len(label_dict))
    model = model.to(device)
    
    print([(n, p.requires_grad) for n, p in model.named_parameters()])

    # count_parameters(model)
    optimizer = optim.RMSprop(model.parameters(), lr=5e-4, momentum=0.9)
    scheduler = optim.lr_scheduler.CyclicLR(optimizer, base_lr=5e-4, max_lr=1e-3, step_size_up=10)
    criterion = nn.CrossEntropyLoss()
    
    test_wa_avg, test_ua_avg, test_f1_avg = 0., 0., 0.
    best_val_wa = 0
    best_val_wa_epoch = 0
    
    # data loader
    train_loader, val_loader, test_loader = load_dataloader(dataset_path_list)
    
    # set save directory
    save_dir = os.path.join(str(Path.cwd()), f"emotion2vec_finetune.pth")
    
    # Training loop
    for epoch in range(epochs):
        train_loss = train_one_epoch(model, optimizer, criterion, train_loader, device)
        scheduler.step()
        
        # Validation step
        val_wa, val_ua, val_f1 = validate_and_test(model, val_loader, device, num_classes=len(label_dict))

        if val_wa > best_val_wa:
            best_val_wa = val_wa
            best_val_wa_epoch = epoch
            torch.save(model.state_dict(), save_dir)

        # Print losses for every epoch
        logger.info(f"Epoch {epoch+1}, Training Loss: {train_loss/len(train_loader):.6f}, Validation WA: {val_wa:.2f}%; UA: {val_ua:.2f}%; F1: {val_f1:.2f}%")

    ckpt = torch.load(save_dir)
    model.load_state_dict(ckpt, strict=True)
    test_wa, test_ua, test_f1 = validate_and_test(model, test_loader, device, num_classes=len(label_dict))
    logger.info(f"{best_val_wa_epoch + 1}, test WA {test_wa}%; UA {test_ua}%; F1 {test_f1}%")
    
    test_wa_avg += test_wa
    test_ua_avg += test_ua
    test_f1_avg += test_f1

    logger.info(f"Average WA: {test_wa_avg}%; UA: {test_ua_avg}%; F1: {test_f1_avg}%")
    
    
    
    
label_dict={'ang': 0, 'hap': 1, 'neu': 2, 'sad': 3}
datasets = ['RAVDESS', 'SAVEE', 'TESS', 'CREMA-D', 'AIHUB']
fine_tuning(label_dict, dataset_path_list=datasets)