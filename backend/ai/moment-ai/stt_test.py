import whisper.whisper as whisper


source_file = "/Users/taejinpark/Downloads/test.m4a"
stt_model = whisper.load_model("tiny")
res = stt_model.transcribe(source_file)

print(stt_model)
print(res)
