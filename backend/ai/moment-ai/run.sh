#!/bin/bash
# export CUDA_VISIBLE_DEVICES=0

# run speech to text model
python3 test.py \
--source_file='./test.mp4' \
--whisper-version='tiny' \



# # run speech emotion recognition model
# python emotion2vec.scripts.extract_features.py  \
# --source_file='/mnt/lustre/sjtu/home/zym22/code/emotion2vec/scripts/test.wav' \
# --target_file='/mnt/lustre/sjtu/home/zym22/code/emotion2vec/scripts/test.npy' \
# --model_dir='/mnt/lustre/sjtu/home/zym22/code/emotion2vec/upstream' \
# --checkpoint_dir='/mnt/lustre/sjtu/home/zym22/models/released/emotion2vec/emotion2vec_base.pt' \
# --granularity='utterance' \
