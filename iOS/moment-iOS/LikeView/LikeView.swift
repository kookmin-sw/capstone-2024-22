//
//  TimerView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation

import SwiftUI


import SwiftUI
import AVFoundation

struct LikeView: View {
    //var day: Date
//    var item: Item
//    var tripFile : TripFile
//    var tripFileId : Int
    var player : AVPlayer?
    
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    @State private var isDeleteMode = false // 삭제 모드 상태
    @State private var isSelected = false // 체크박스 선택 여부
    @State private var showConfirmationDialog = false // 커스텀 다이얼로그 표시 여부
    @ObservedObject var cardViewModel : CardViewModel
    @State private var isEditing = false // 편집 모드 상태
    @State private var selectedCardIDs = Set<Int>() // 선택된 카드의 ID 저장
    
    
    var body: some View {
        ZStack{
            NavigationView {
                
                ZStack {
                    
                    VStack {
                        Spacer().frame(height: 30)
                        
                        
                        CustomTitleMainDivider()
                            .padding(.bottom, -10)
                        
                        
                        HStack {
                            Spacer()
                            Text("즐겨찾은 녹음 파일")
                                .font(.pretendardBold22)
                                .foregroundColor(.black)
                        }
                        .padding(.horizontal, 20)
                        
                        CustomTitleMainDivider()
                            .padding(.top, -7)
                        
                        ScrollView {
                            // ForEach를 사용하여 cardViewModel의 cardItems 배열을 반복 처리
                            ForEach(cardViewModel.cardItemsLike) { cardItemLike in
                                // 각 cardItem에 대한 AccordionView 인스턴스를 생성
                                AccordionLikeView(audioRecorderManager: audioRecorderManager, isEditing: $isEditing, selectedCardIDs: $selectedCardIDs, cardViewModel: cardViewModel, cardItemLike: cardItemLike)
                                
                            }
                        }
                        //첫번째 주석 넣기
                        
                    }
                    
                    
                    
                }.background(Color.homeBack)
                
            }
            if showConfirmationDialog {
                CustomConfirmationDialog(
                    isActive: $showConfirmationDialog,
                    title: "\(selectedCardIDs.count)개의 녹음 카드를 정말 삭제할까요?",
                    message: "삭제된 녹음 카드는 복구할 수 없어요",
                    yesAction: {
                        // 삭제 로직
                        cardViewModel.cardItems.removeAll { selectedCardIDs.contains($0.id) }
                        selectedCardIDs.removeAll()
                        isEditing = false
                        showConfirmationDialog = false
                    },
                    noAction: {
                        showConfirmationDialog = false
                    }
                )
            }
            
        }
        .navigationBarBackButtonHidden(true)
        .onAppear{
            cardViewModel.fetchLikedCardViews()
        }
        
        
    }
      
}


struct AccordionLikeView: View {
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    @State private var isExpanded = false
    @Binding var isEditing: Bool
    @Binding var selectedCardIDs: Set<Int>
    @ObservedObject var cardViewModel: CardViewModel
    var cardItemLike: LikeCardViewData
    
    var body: some View {
        HStack { // 체크박스와 카드 컨텐츠 사이의 간격을 없애기 위해 spacing을 0으로 설정
            // 편집 모드일 때만 체크박스 표시
          
            
            VStack {
                DisclosureGroup(isExpanded: $isExpanded) {
                    contentVStack
                } label: {
                    HeaderLikeView(isExpanded: $isExpanded, cardItemLike: cardItemLike, cardViewModel: cardViewModel)
                }
                .accentColor(.black)
                .padding()
                
                
                .background(Color.homeBack)
                .cornerRadius(10)
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(Color.toastColor, lineWidth: 2)
                )
            }.padding(.horizontal,20)
                .padding(.vertical,5)
            
            
        }
        
        .animation(.default, value: isEditing) // 편집 모드 변화에 따른 애니메이션
    }
    
    
    
    
    
    // 컨텐츠 VStack을 별도의 ViewBuilder로 추출하여 재사용
    @ViewBuilder
    private var contentVStack: some View {
        VStack {
            Spacer().frame(height: 40)
            locationAndTimeInfo
            DynamicGradientRectangleLikeView(audioRecorderManager: audioRecorderManager, cardViewModel: cardViewModel, longText: "\(cardItemLike.stt)", cardItemsLike: cardItemLike)
            DynamicGradientImagePicker(cardViewModel: cardViewModel)
            Spacer().frame(height: 30)
            EmotionView(cardViewModel: cardViewModel)
        }
    }
    
    // 위치 및 시간 정보를 보여주는 HStack을 별도의 ViewBuilder로 추출
    @ViewBuilder
    private var locationAndTimeInfo: some View {
        HStack {
            Image("Location")
            Text("\(cardItemLike.location)")
                .font(.pretendardMedium11)
            Spacer()
            Text("\(cardItemLike.weather)")
                .font(.pretendardMedium11)
            Image("Weather_Sunny")
        }
        HStack {
            let (date, time) = cardViewModel.formatDateAndTime(dateString: cardItemLike.recordedAt)
            Image("CardTime")
            Text("\(date)").font(.pretendardMedium11)
            //cardItem.recordedAt
            Spacer()
            CustomCarbarDivider()
            Text("\(time)").font(.pretendardMedium11)
            //cardItem.recordedAt
        }
    }
}





struct HeaderLikeView: View {
    @Binding var isExpanded: Bool
    @State private var isHeartFilled = true // 하트가 채워졌는지 여부
    var cardItemLike: LikeCardViewData
    @ObservedObject var cardViewModel: CardViewModel
    var body: some View {
        VStack{
            HStack {
                Button(action: {
                    // 하트 버튼을 눌렀을 때의 액션
                    isHeartFilled.toggle()
                    cardViewModel.updateCardViewLikeStatus(cardViewId: cardItemLike.id)
                }) {
                    Image(isHeartFilled ? "HeartFill" : "HeartEmpty")
                    
                    
                }
                Text("\(cardItemLike.recordedAt)") // 타이틀 예시
                    .foregroundColor(.black)
                    .font(.pretendardExtrabold14)
                Spacer()
                
                Text("\(cardItemLike.id)")
                    .font(.pretendardExtrabold14)
                    .foregroundColor(.black)
            }
            .padding(.horizontal,10)
            .background(Color.homeBack) // 헤더 배경색
            .cornerRadius(10)
            CustomHomeVDividerCard()
            if !isExpanded {
                HStack{
                    Text("꽤나 즐거운 대화였네요")
                        .font(.pretendardMedium11)
                        .foregroundColor(.gray500)
                        .padding(.horizontal,10)
                        .padding(.top,10)
                    Spacer()
                    Text("해가 쨍쨍한날")
                        .font(.pretendardMedium11)
                        .foregroundColor(.gray500)
                        .padding(.top,10)
                    
                    Image("Weather_Sunny")
                    
                        .padding(.top,10)
                }
            }
        }
    }
}

struct DynamicGradientRectangleLikeView: View {
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    @ObservedObject var cardViewModel: CardViewModel
    let longText: String

    var playerItem: AVPlayerItem?
    var cardItemsLike : LikeCardViewData
    
    var body: some View {
        //ScrollView {
        VStack {

            let url = URL(string: cardItemsLike.recordFileUrl)!
            // 여기에서 url이 어떤 url 이 들어가있는지 확인하고싶어
            let audioPlayer = AudioPlayer(url: url)
                   
                   CustomAudioPlayerView(audioPlayer: audioPlayer)
          

      
            
            Text(longText)
                .font(.pretendardMedium13)
                .padding()
        }
        .background(
            LinearGradient(gradient: Gradient(colors: [.homeBack, .toastColor]), startPoint: .top, endPoint: .bottom)
        )
        .cornerRadius(3)
      
        .frame(width: 340)
   
        
    }
}

struct DynamicGradientLikeImagePicker: View {
    @State private var showingImagePicker = false
    @State private var showingAddButton = false // 처음에는 추가 버튼이 보이지 않음
    @State private var selectedImages: [UIImage?] = []
    
    var body: some View {
        VStack {
            
            HStack {
                Spacer() // HStack의 왼쪽에 Spacer를 추가하여 오른쪽으로 요소를 밀어냄
                if !showingAddButton {
                    // '이미지 추가하기' 텍스트 버튼
                    Group{
                        Button(action: {
                            showingAddButton = true // '이미지 추가하기' 버튼을 누르면 추가 버튼 생성
                            selectedImages.append(nil) // 추가 버튼에 해당하는 nil 추가
                        }) {
                            HStack {
                                Text("사진 추가")
                                    .font(.pretendardMedium11)
                                    .foregroundColor(.black)
                                Image("Imageadd")
                                    .foregroundColor(.white)
                            }
                            .padding(.horizontal,10)
                            
                            
                        }
                        
                    }
                }
            }
            
            if showingAddButton {
                // 이미지들과 추가 버튼을 보여주는 스크롤 뷰
                CustomDividerCardView()
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 10) {
                        ForEach(0..<selectedImages.count, id: \.self) { index in
                            if let image = selectedImages[index] {
                                Image(uiImage: image)
                                    .resizable()
                                    .frame(width: 66, height: 77)
                                    .cornerRadius(3)
                            } else {
                                addButton(index: index)
                            }
                        }
                    }
                }
                .background(Color.homeBack)
                .cornerRadius(3)
                .frame(width: 340) // 가로 크기 고정
            }
        }
        .sheet(isPresented: $showingImagePicker) {
            ImagePicker(selectedImage: Binding(
                get: { UIImage() },
                set: { image in
                    if let image = image, let index = selectedImages.lastIndex(where: { $0 == nil }) {
                        selectedImages[index] = image // 이미지 추가
                        selectedImages.append(nil) // 새로운 추가 버튼 생성
                    }
                }
            ))
        }
    }
    
    @ViewBuilder
    private func addButton(index: Int) -> some View {
        Button(action: {
            showingImagePicker = true
        }) {
            RoundedRectangle(cornerRadius: 3)
                .fill(Color.gray)
                .frame(width: 66, height: 77)
                .overlay(
                    Image(systemName: "plus")
                        .foregroundColor(.white)
                )
        }
    }
}
//CustomEmotionViewDivider()

struct LikeEmotionView: View {
    var body: some View {
        VStack {
            HStack{
                Text("꽤나 즐거운 대화였어요")
                    .font(.pretendardMedium11)
                    .padding(.horizontal,20)
                Spacer()
                Text("감정분석")
                    .font(.pretendardMedium11)
                    .padding(.horizontal,10)
                
            }
            CustomEmotionViewDivider()
            
            emotionRow(imageName: "netral", emotionText: "평범해요", progressValue: 0.6, percentage: "60%")
            emotionRow(imageName: "fun", emotionText: "즐거워요", progressValue: 0.2, percentage: "20%")
            emotionRow(imageName: "angry", emotionText: "화나요", progressValue: 0.15, percentage: "15%")
            emotionRow(imageName: "sad", emotionText: "슬퍼요", progressValue: 0.05, percentage: "5%")
        }
        .padding(.horizontal, 1) // HStack에 패딩을 적용하여 내용이 화면 가장자리에 붙지 않도록 합니다.
    }
    
    @ViewBuilder
    private func emotionRow(imageName: String, emotionText: String, progressValue: Double, percentage: String) -> some View {
        HStack {
            
            Image(imageName)
                .resizable()
                .scaledToFit()
                .frame(width: 12, height: 12) // 이미지의 크기를 설정합니다.
            
            Text(emotionText)
                .font(.pretendardMedium11)
                .frame(width: 50, alignment: .leading) // 텍스트의 너비와 정렬을 설정합니다.
            
            
            Spacer().frame(width:35)
            
            ProgressView(value: progressValue, total: 1.0)
            
                .progressViewStyle(LinearProgressViewStyle(tint: getColorForEmotion(emotionText: emotionText))) // 감정에 따른 색상
                .frame(width: 136) // 진행률 표시기의 너비를 설정합니다.
            Spacer().frame(width:35)
            Text(percentage)
                .font(.pretendardMedium11)
                .frame(width: 27, alignment: .trailing) // 퍼센트 텍스트의 너비와 정렬을 설정합니다.
            
            // Spacer() // 오른쪽에 Spacer를 추가하여 모든 요소를 왼쪽으로 정렬합니다.
        }
    }
    private func getColorForEmotion(emotionText: String) -> Color {
        switch emotionText {
        case "평범해요":
            return .homeRed
        case "즐거워요":
            return .black
        case "화나요":
            return .StrangeColor
        case "슬퍼요":
            return .unsafeColor
        default:
            return .gray
        }
    }
}


struct LikeAudioPlayerControls: View {
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    
    var body: some View {
        // 녹음 파일 재생 관련 UI 구성
        // 예시: 재생, 정지 버튼 등
        VStack {
            ProgressView(value: audioRecorderManager.playbackProgress)
                .progressViewStyle(LinearProgressViewStyle())
                .frame(height: 20)
                .padding()
            HStack{
                if let lastRecording = audioRecorderManager.recordedFiles.last {
                    Button("재생") {
                        audioRecorderManager.startPlaying(recordingURL: lastRecording)
                    }
                }
                Button("정지") {
                    audioRecorderManager.stopPlaying()
                }
            }
            Spacer()
        }
        
    }
}

