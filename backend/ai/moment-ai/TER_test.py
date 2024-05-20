import torch
from kobert_tokenizer import KoBERTTokenizer
from transformers import BertModel


tokenizer = KoBERTTokenizer.from_pretrained('skt/kobert-base-v1', sp_model_kwargs={'nbest_size': -1, 'alpha': 0.6, 'enable_sampling': True})
model = BertModel.from_pretrained('skt/kobert-base-v1')
text = "한국어 모델을 공유합니다."
inputs = tokenizer.batch_encode_plus([text])
out = model(input_ids = torch.tensor(inputs['input_ids']),
              attention_mask = torch.tensor(inputs['attention_mask']))
print(out.pooler_output.shape)
