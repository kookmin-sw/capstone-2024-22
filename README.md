# 목차
1. [프로젝트 소개](#🛠-프로젝트-소개)
2. [포스터 및 소개 영상](#포스터-및-소개-영상)
3. [팀 소개](#👩🏻‍💻-팀-소개)
4. [배포가이드](#배포가이드)
5. [설계](#설계)
6. [문서](#문서)
7. [기타](#기타)

<br><br>

# 🛠 프로젝트 소개
## **한줄 소개** <br>
혼자 하는 여행 중 친구처럼 던진 질문에 답을 하거나 개개인의 다양한 경험을 스마트 워치를 이용해 음성으로 간편하게 기록하고, <br>
AI를 활용하여 자동 텍스트 변환, 감정 분석, 기록 아카이빙까지 제공하는 음성 인식 다이어리 앱입니다.

## **핵심 기능**
1. 웨어러블 디바이스 연동
스마트워치와 같은 웨어러블 디바이스를 연동해 언제나 손쉽게 음성을 기록할 수 있습니다.
2. 음성인식 인공지능
사용자의 음성을 텍스트로 변환해주는 음성인식 인공지능을 도입해 기록한 음성을 텍스트로 변환해 저장해줍니다.
3. 감정분석 인공지능
기록된 음성기록을 인공지능을 통해 분석해 그 당시의 감정을 분석하고 기록해줍니다.
4. 차별화된 여행 기록 제공
녹음을 기록했던 당시의 장소, 날씨, 시간, 기온 등등 그 당시의 데이터를 추가로 제공해 개개인의 여행 상황에 차별화된 여행기록을 제공해줍니다.
5. 고유한 여행 산출물 생성
여행이 끝난 후 각 여행의 산출물인 "여행 티켓"을 만드는 기능을 제공해 여행에 대한 고유한 결과물을 생성할 수 있습니다.

<br><br>

# 포스터 및 소개 영상
## 포스터
<img width="600" height="800" src="./src/momentposter.png">

<br>

## 소개 영상
[![Video Label](http://img.youtube.com/vi/9_Bpa-_iTuk/0.jpg)](https://youtu.be/9_Bpa-_iTuk)

<br><br>

# 설계
## **Android 설계**  
<img width="600" height="400" alt="아키텍쳐 설계" src="./src/momentandroide.png">

<br>

## **iOS 설계**
### Clean arcitecture
<img width="600" height="400" alt="아키텍쳐 설계" src="https://github.com/kookmin-sw/capstone-2024-22/assets/105967852/e8a2e7cd-17c3-41d0-aa89-361439d7dc51">

<br>

## **BackEnd 설계**
### ERD <br>
<img width="600" height="800" alt="아키텍쳐 설계" src="./src/momenterd.png">

### Architecture <br>
<img width="600" height="400" alt="아키텍쳐 설계" src="./src/Momentarc.png">

<br>

## **AI 설계**
### 음성인식 모델
<img width="600" height="400" alt="아키텍쳐 설계" src="./src/whisper.png">

### 감정분석 모델
<img width="600" height="400" alt="아키텍쳐 설계" src="./src/emotion2vec.png">

<br><br>

# 👩🏻‍💻 팀 소개
🖥 **Frontend** <br>
이름|역할|개발스택
-------|-------|-----
양시관|iOS|<img src="https://img.shields.io/badge/ios-%23000000.svg?&style=for-the-badge&logo=ios&logoColor=white" /> <img src="https://img.shields.io/badge/uikit-%232396F3.svg?&style=for-the-badge&logo=uikit&logoColor=white" /> <img src="https://img.shields.io/badge/swift-%23FA7343.svg?&style=for-the-badge&logo=swift&logoColor=white" /> 
김민중|Android|<img src="https://img.shields.io/badge/android-%233DDC84.svg?&style=for-the-badge&logo=android&logoColor=black" /> <img src="https://img.shields.io/badge/kotlin-%230095D5.svg?&style=for-the-badge&logo=kotlin&logoColor=white" />
홍서현|Android|<img src="https://img.shields.io/badge/android-%233DDC84.svg?&style=for-the-badge&logo=android&logoColor=black" /> <img src="https://img.shields.io/badge/kotlin-%230095D5.svg?&style=for-the-badge&logo=kotlin&logoColor=white" />

🖥 **Backend** <br>
이름|역할|개발스택
------|------|---
김재용|Server|<img src="https://img.shields.io/badge/spring-%236DB33F.svg?&style=for-the-badge&logo=spring&logoColor=white" /> <img src="https://img.shields.io/badge/mysql-%234479A1.svg?&style=for-the-badge&logo=mysql&logoColor=white" /> <img src="https://img.shields.io/badge/docker-%232496ED.svg?&style=for-the-badge&logo=docker&logoColor=white" /> <img src="https://img.shields.io/badge/json%20web%20tokens-%23000000.svg?&style=for-the-badge&logo=json%20web%20tokens&logoColor=white" />
박태진|AI| <img src="https://img.shields.io/badge/pytorch-%23EE4C2C.svg?&style=for-the-badge&logo=pytorch&logoColor=white" /> <img src="https://img.shields.io/badge/flask-%23000000.svg?&style=for-the-badge&logo=flask&logoColor=white" />

🖥 **Communication** <br>
역할|종류
------|------
협업관리|<img src="https://img.shields.io/badge/notion-%23000000.svg?&style=for-the-badge&logo=notion&logoColor=white" />
디자인|<img src="https://img.shields.io/badge/figma-%23F24E1E.svg?&style=for-the-badge&logo=figma&logoColor=white" />
API 문서|<img src="https://img.shields.io/badge/swagger-%2385EA2D.svg?&style=for-the-badge&logo=swagger&logoColor=black" />
형상관리|<img src="https://img.shields.io/badge/git-%23F05032.svg?&style=for-the-badge&logo=git&logoColor=white" />

<br><br>

# 배포가이드

**1. 서버 배포 가이드**

  서버는 github action으로 CI/CD파이프라인을 구성해두었기 때문에 deploy/v1브랜피로 머지되면 자동으로 배포가 이뤄집니다. 
1. [https://github.com/kookmin-sw/capstone-2024-22](https://github.com/kookmin-sw/capstone-2024-22%EC%9D%98)의 master 브랜치를 git pull 받는다.	
2. cd backend/moment/moment-server
3. 각 인스턴스의 src/main/resources에 있는 설정파일(application.properties, application.yml)을 자신이 원하는 대로 수정한다.
4. capstone-2024-22/backend/moment/moment-server/docker-compose.yml파일의 DB 유저와 비밀번호를 설정한다.
5. “docker-compose up —build -d”로 실행한다.  

**2. 안드로이드 배포 가이드**
  1. 안드로이드 스튜디오로 실행하는 법  
    1-1. git clone https://github.com/kookmin-sw/capstone-2024-22.git  
    1-2. cd capstone-2024-22  
    1-2. git checkout master  
    1-3. cd Android/moment-android  
    1-4. 해당 경로에서 안드로이드 스튜디오로 폴더를 열어서 실행합니다.  
  2. 테스트 배포링크로 다운로드 받아서 실행하는 법  
    2-1. nex1223@naver.com (Moment-Android 공식 계정)로 본인의 구글  
    플레이스토어 계정을 보냅니다.  
    2-2. 해당 계정이 등록이 되었다고 nex1223@naver.com에서 응답이 옵  
    니다.  
    2-3. 해당 링크로 들어가서 앱을 다운로드 받습니다.  
    https://play.google.com/store/apps/details?id=com.capstone.android.app  
    lication  

**3.인공지능 서버 배포 가이드**

  최소 RAM 12G 이상의 GPU를 보유하고 있는 상태에서 배포하는 것을 권장합니다.
  현재 AWS에 배포되어있고, lambda로 EC2 인스턴스 시작과 중지 함수가 작성되어있어야 합니다.
1. [https://github.com/kookmin-sw/capstone-2024-22의](https://github.com/kookmin-sw/capstone-2024-22%EC%9D%98) ai/develop/v1 브랜치를 git pull받는다.
2. cd backend/ai/moment-ai
3. conda create --name moment --file packagelist.txt로 conda 환경을 구축합니다.
4. python3 ai_server.py로 인공지능 서버를 실행합니다.
- S3에 저장된 데이터를 불러오기 때문에 S3 구축이 필요합니다.
  
 **4.iOS 배포 가이드**(테스트 배포 완료)
  1. yysskk99999@naver.com 이메일로 사용하려는 기기와 연결되어 있는 appleid 를
     보냅니다.

     1-1. 앱스토어에서 TestFlight App 을 다운로드 받습니다.

     1-2. Appie 이메일로 들어가서 초대 링크를 클릭합니다.

     1-3. 앱을 설치합니다.

  2. App을 다운받았는데 앱이 깨지거나 기능이 메뉴얼대로 실행되지않을경우 밑 빌드를 참고합니다.

     2-1. 깃허브에서 clone 후 ios 파일경로로 들어가서 프로젝트를 실행시킵니다.

     2-2. 로드되기를 기다린후 왼쪽 파일 인디케이터에서 프로젝트를 선택하고 Signing & Capabilities 를 들어갑니다.

     2-4. 팀에서 자신의 애플아이디 계정을 선택하여 왼쪽 상단에 빌드를 누릅니다.
 
   
# 문서
- [중간발표](https://drive.google.com/file/d/1ELYTpvr5rwQeEEy5aQ8zkQpWVYD6Tc5h/view?usp=sharing)
- [중간보고서](https://docs.google.com/document/d/15EEr-d8ANUgzv2IuLtTUmPRXQ-5MzV7M/edit?usp=drive_link&ouid=100333300987560322826&rtpof=true&sd=true)
- [최종발표](https://drive.google.com/file/d/1NVUngwnnO_g0cqBpgnbIXetWi-n_3aw2/view?usp=sharing)
- [수행결과보고서](https://drive.google.com/file/d/1Ige8NZsifMVasT25bFnQQ1b8EKNBFPVM/view?usp=sharing)
- [인쇄용 포스터](https://drive.google.com/file/d/1NlpX4JE9lVlnAaejtYVlCgYRj_xY7Sfm/view?usp=drive_link)
- [최종보고서]()

<br><br>

# 기타
## 인공지능
인공지능 모델은 해당 논문 및 깃헙을 참고하여 구현하였습니다.

Whisper
- [[Paper]](https://cdn.openai.com/papers/whisper.pdf)
- [[github]](https://github.com/openai/whisper)

emotion2vec
- [[Paper]](https://arxiv.org/pdf/2312.15185.pdf)
- [[github]](https://github.com/ddlBoJack/emotion2vec)








