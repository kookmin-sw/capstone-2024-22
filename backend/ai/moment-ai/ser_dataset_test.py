# Import libraries 
import librosa
import librosa.display
import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf
from matplotlib.pyplot import specgram
import pandas as pd
import glob 
from sklearn.metrics import confusion_matrix
import IPython.display as ipd  # To play sound in the notebook
import os
import sys
import warnings
import glob
# ignore warnings 
if not sys.warnoptions:
    warnings.simplefilter("ignore")
warnings.filterwarnings("ignore", category=DeprecationWarning) 


#for dirname, _, filenames in os.walk('/kaggle/input'):
#    for filename in filenames:
#        print(os.path.join(dirname, filename))

DATA_PATH = "./emotion2vec/datas"
DATA_PATH = os.path.abspath(DATA_PATH)

TESS =  os.path.join(DATA_PATH, "TESS/TESS Toronto emotional speech set data/")
RAV =  os.path.join(DATA_PATH, "RAVDESS/audio_speech_actors_01-24/")
SAVEE = os.path.join(DATA_PATH, "SAVEE/")
CREMA = os.path.join(DATA_PATH, "CREMA-D/")
AIHUB = os.path.join(DATA_PATH, "AIHUB/")



# 0. set labels ---------------------------------------------------------------------------------------------------------------------------
labels = ['angry', 'disgust', 'happy', 'neutral', 'sad']


# 1. SAVEE data ---------------------------------------------------------------------------------------------------------------------------
# Get the data location for SAVEE
dir_list = glob.glob(os.path.join(SAVEE, "*.wav"))
print(dir_list[0:5])

# parse the filename to get the emotions
emotion = []
path = []
for d in dir_list:
    i = os.path.basename(d)
    if i[-8:-6] == '_a':
        emotion.append('angry')
    elif i[-8:-6] == '_d':
        emotion.append('disgust')
    elif i[-8:-6] == '_h':
        emotion.append('happy')
    elif i[-8:-6] == '_n':
        emotion.append('neutral')
    elif i[-8:-6] == 'sa':
        emotion.append('sad')
    else:
        if os.path.exists(d):
            os.remove(d)
        continue
    
    path.append(d)
    
    
# Now check out the label count distribution 
SAVEE_df = pd.DataFrame(emotion, columns=['labels'])
SAVEE_df['source'] = 'SAVEE'
SAVEE_df = pd.concat([SAVEE_df, pd.DataFrame(path, columns=['path'])], axis=1)
print(SAVEE_df.head())
print(SAVEE_df.labels.value_counts())

SAVEE_df.to_csv(os.path.join(DATA_PATH, "SAVEE.csv"))


# use the well known Librosa library for this task 
fname = SAVEE + 'DC_a11.wav'  
data, sampling_rate = librosa.load(fname)
plt.figure(figsize=(15, 5))
librosa.display.waveplot(data, sr=sampling_rate)

# Lets play the audio 
ipd.Audio(fname)


# Lets play a happy track
fname = SAVEE + 'DC_h11.wav'  
data, sampling_rate = librosa.load(fname)
plt.figure(figsize=(15, 5))
librosa.display.waveplot(data, sr=sampling_rate)

# Lets play the audio 
ipd.Audio(fname)



# 2. RAVDESS data ---------------------------------------------------------------------------------------------------------------------------
dir_list = glob.glob(os.path.join(RAV, "*/*.wav"))
dir_list.sort()
print(dir_list[0:5])

emotion = []
gender = []
path = []
for fname in dir_list:
    f = os.path.basename(fname)
    part = f.split('.')[0].split('-')
    emotion.append(int(part[2]))
    
    if int(part[2]) not in [1, 3, 4, 5, 7]:
        if os.path.exists(fname):
            os.remove(fname)
        continue
    
    path.append(fname)

RAV_df = pd.DataFrame(emotion)
RAV_df = RAV_df.replace({1:'neutral', 2:'calm', 3:'happy', 4:'sad', 5:'angry', 6:'fear', 7:'disgust', 8:'surprise'})
RAV_df.columns = ['emotion']
RAV_df['labels'] = RAV_df.emotion
RAV_df['source'] = 'RAVDESS'
RAV_df = pd.concat([RAV_df, pd.DataFrame(path, columns=['path'])], axis=1)
RAV_df = RAV_df.drop(['emotion'], axis=1)
print(RAV_df.head())
print(RAV_df.labels.value_counts())

RAV_df.to_csv(os.path.join(DATA_PATH, "RAVDESS.csv"))


# Pick a fearful track
fname = RAV + 'Actor_14/03-01-07-02-02-02-14.wav'  
data, sampling_rate = librosa.load(fname)
plt.figure(figsize=(15, 5))
librosa.display.waveplot(data, sr=sampling_rate)

# Lets play the audio 
ipd.Audio(fname)


# Pick a happy track
fname = RAV + 'Actor_14/03-01-03-02-02-02-14.wav'  
data, sampling_rate = librosa.load(fname)
plt.figure(figsize=(15, 5))
librosa.display.waveplot(data, sr=sampling_rate)

# Lets play the audio 
ipd.Audio(fname)




# 3. TESS Ddta ---------------------------------------------------------------------------------------------------------------------------
dir_list = [d for d in os.listdir(TESS) if not d.startswith('.')]
dir_list.sort()
print(dir_list)

path = []
emotion = []

for i in dir_list:
    fname = os.listdir(os.path.join(TESS, i))
    for f in fname:
        p = os.path.join(TESS, i, f)

        if i == 'OAF_angry' or i == 'YAF_angry':
            emotion.append('angry')
        elif i == 'OAF_disgust' or i == 'YAF_disgust':
            emotion.append('disgust')
        elif i == 'OAF_happy' or i == 'YAF_happy':
            emotion.append('happy')
        elif i == 'OAF_neutral' or i == 'YAF_neutral':
            emotion.append('neutral')                                
        elif i == 'OAF_Sad' or i == 'YAF_sad':
            emotion.append('sad')
        else:
            if os.path.exists(p):
                os.remove(p)
            continue
        
        path.append(p)

TESS_df = pd.DataFrame(emotion, columns=['labels'])
TESS_df['source'] = 'TESS'
TESS_df = pd.concat([TESS_df, pd.DataFrame(path, columns=['path'])], axis=1)
print(TESS_df.head())
print(TESS_df.labels.value_counts())

TESS_df.to_csv(os.path.join(DATA_PATH, "TESS.csv"))


# lets play a fearful track 
fname = TESS + 'OAF_angry/OAF_tell_angry.wav' 

data, sampling_rate = librosa.load(fname)
plt.figure(figsize=(15, 5))
librosa.display.waveplot(data, sr=sampling_rate)

# Lets play the audio 
ipd.Audio(fname)


# lets play a happy track 
fname = TESS + 'YAF_happy/YAF_dog_happy.wav' 

data, sampling_rate = librosa.load(fname)
plt.figure(figsize=(15, 5))
librosa.display.waveplot(data, sr=sampling_rate)

# Lets play the audio 
ipd.Audio(fname)



# 4. CREMA-D data ---------------------------------------------------------------------------------------------------------------------------
dir_list = glob.glob(os.path.join(CREMA, "*.wav"))
dir_list.sort()
print(dir_list[0:10])

gender = []
emotion = []
path = []

for i in dir_list: 
    part = os.path.basename(i).split('_')

    if part[2] == 'SAD':
        emotion.append('sad')
    elif part[2] == 'ANG':
        emotion.append('angry')
    elif part[2] == 'DIS':
        emotion.append('disgust')
    elif part[2] == 'HAP':
        emotion.append('happy')
    elif part[2] == 'NEU':
        emotion.append('neutral')
    else:
        if os.path.exists(i):
            os.remove(i)
        continue
    
    path.append(i)
    
CREMA_df = pd.DataFrame(emotion, columns=['labels'])
CREMA_df['source'] = 'CREMA'
CREMA_df = pd.concat([CREMA_df, pd.DataFrame(path, columns=['path'])],axis=1)
print(CREMA_df.head())
print(CREMA_df.labels.value_counts())

CREMA_df.to_csv(os.path.join(DATA_PATH, "CREMA-D.csv"))


# use the well known Librosa library for this task 
fname = CREMA + '1012_IEO_HAP_HI.wav'  
data, sampling_rate = librosa.load(fname)
plt.figure(figsize=(15, 5))
librosa.display.waveplot(data, sr=sampling_rate)

# Lets play the audio 
ipd.Audio(fname)


# A fearful track
fname = CREMA + '1012_IEO_ANG_HI.wav'  
data, sampling_rate = librosa.load(fname)
plt.figure(figsize=(15, 5))
librosa.display.waveplot(data, sr=sampling_rate)

# Lets play the audio 
ipd.Audio(fname)


# 5. AIHUB data ---------------------------------------------------------------------------------------------------------------------------
excel_list = [excel for excel in glob.glob(os.path.join(AIHUB, "*.csv")) if not "AIHUB" in os.path.basename(excel)]
print(excel_list)

AIHUB_df = pd.DataFrame()

for excel in excel_list:
    fname = os.path.splitext(os.path.basename(excel))[0]
    fname_df = pd.read_csv(excel)
    AIHUB_df = pd.concat([AIHUB_df, fname_df])
    

AIHUB_df = AIHUB_df[['상황', 'wav_id']]
AIHUB_df.rename(columns={"wav_id":"path", "상황":"labels"}, inplace=True)
AIHUB_df['source'] = "AIHUB"
AIHUB_df['path'] = os.path.join(AIHUB, "TOTAL/") + AIHUB_df['path'] + ".wav"
AIHUB_df = AIHUB_df[['labels', 'source', 'path']]
AIHUB_df = AIHUB_df.replace({'labels':'anger'}, 'angry')
AIHUB_df = AIHUB_df.replace({'labels':'happiness'}, 'happy')
AIHUB_df = AIHUB_df.replace({'labels':'sadness'}, 'sad')
print(AIHUB_df.labels.value_counts())

# remove fear labels
remove_list = ['fear', 'surprise']
removes = AIHUB_df[AIHUB_df['labels'].str.contains("|".join(remove_list))]
for i in removes['path']:
    if os.path.exists(i):
        os.remove(i)

AIHUB_df = AIHUB_df[~AIHUB_df.isin(remove_list)]

print(AIHUB_df.head(10))
print(AIHUB_df.labels.value_counts())


AIHUB_df.to_csv(os.path.join(DATA_PATH, "AIHUB.csv"))


# 6. CONCAT DATASETS ---------------------------------------------------------------------------------------------------------------------------

TOTAL_df = pd.DataFrame()
TOTAL_df = pd.concat([TOTAL_df, SAVEE_df])
TOTAL_df = pd.concat([TOTAL_df, RAV_df])
TOTAL_df = pd.concat([TOTAL_df, CREMA_df])
TOTAL_df = pd.concat([TOTAL_df, TESS_df])
TOTAL_df = pd.concat([TOTAL_df, AIHUB_df])

print(TOTAL_df.labels.value_counts())

TOTAL_df.to_csv(os.path.join(DATA_PATH, "DATASETS.csv"))


# ---------------------------------------------------------------------------------------------------------------------------
