//
//  CardView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/28/24.
//

import SwiftUI
import AVFoundation

struct CardView: View {
    //var day: Date
    var item: Item
    var tripFile: TripFile
    var tripFileId: Int
    var player: AVPlayer?
    var index: Int

    
    
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    @State private var isDeleteMode = false // 삭제 모드 상태
    @State private var isSelected = false // 체크박스 선택 여부
    @State private var showConfirmationDialog = false // 커스텀 다이얼로그 표시 여부
    @ObservedObject var cardViewModel : CardViewModel
    @State private var isEditing = false // 편집 모드 상태
    @State private var selectedCardIDs = Set<Int>() // 선택된 카드의 ID 저장
   // @State private var cardViews : cardViews?
  
    
    
    var body: some View {
        ZStack{
            NavigationView {
                
                ZStack {
                    
                    VStack {
                        
                        HStack {
                            Button(action: {
                                // "뒤로" 버튼의 액션: 현재 뷰를 종료
                                self.presentationMode.wrappedValue.dismiss()
                            }) {
                                HStack {
                                    Spacer().frame(width: 10)
                                    Text("뒤로")
                                        .font(.yjObangBold15)
                                        .tint(Color.black)
                                }
                            }
                            Spacer()
                            
                            
                            
                            
                            
                            
                            if isEditing {
                                Button("삭제") {
                                    // 선택된 카드들을 삭제하는 로직
                                    //cardViewModel.cardItems.removeAll { selectedCardIDs.contains($0.id) }
                                    self.showConfirmationDialog = true
                                }.font(.yjObangBold15)
                                    .tint(Color.black)
                                Spacer().frame(width: 10)
                            } else {
                                Button("편집") { // 편집모드인 삭제
                                    isEditing.toggle()
                                }.font(.yjObangBold15)
                                    .tint(Color.black)
                                Spacer().frame(width: 10)
                            }
                            
                            
                            
                            
                            
                            
                            
                        }
                        .padding()
                        
                        CustomTitleMainDivider()
                            .padding(.bottom, -10)
                        
                        
                        HStack {
                            Spacer()
                            Text(item.tripName + "여행" + " \(index + 1)일차")
                                .font(.pretendardBold22)
                                .padding(.horizontal,16)
                                .foregroundColor(.black)
                        }
                        .padding(.horizontal, 20)
                        
                        CustomTitleMainDivider()
                            .padding(.top, -7)
                        
                        ScrollView {
                    
                            
            
                            ForEach(cardViewModel.cardItems) { cardItem in
                                // 각 cardItem에 대한 AccordionView 인스턴스를 생성
                                AccordionView(audioRecorderManager: audioRecorderManager, isEditing: $isEditing, selectedCardIDs: $selectedCardIDs, cardViewModel: cardViewModel, cardItem: cardItem)
                                
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
        .onAppear {
                       // 특정 tripFileId를 사용하여 카드뷰 데이터 로드
            cardViewModel.fetchAllCardViews(tripFileId: tripFile.id)
                   }
        
        
    }
}

struct AccordionView: View {
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    @State private var isExpanded = false
    @Binding var isEditing: Bool
    @Binding var selectedCardIDs: Set<Int>
    @ObservedObject var cardViewModel: CardViewModel
    var cardItem: cardViews
    
    var body: some View {
        HStack { // 체크박스와 카드 컨텐츠 사이의 간격을 없애기 위해 spacing을 0으로 설정
            // 편집 모드일 때만 체크박스 표시
            if isEditing {
                Button(action: {
                    // 체크박스 선택/해제 로직
                    if selectedCardIDs.contains(cardItem.id) {
                        selectedCardIDs.remove(cardItem.id)
                    } else {
                        selectedCardIDs.insert(cardItem.id)
                    }
                }) {
                    Image(systemName: selectedCardIDs.contains(cardItem.id) ? "checkmark.square.fill" : "square")
                        .foregroundColor(.homeRed)
                        .padding(.leading, 100) // 왼쪽 패딩 추가로 체크박스 위치 조정
                }
            }
            
            VStack {
                DisclosureGroup(isExpanded: $isExpanded) {
                    contentVStack
                } label: {
                    HeaderView(isExpanded: $isExpanded, cardItem: cardItem, cardViewModel: cardViewModel)
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
            DynamicGradientRectangleView(audioRecorderManager: audioRecorderManager, cardViewModel: cardViewModel, longText: "\(cardItem.stt)", cardItem: cardItem)
            DynamicGradientImagePicker(cardViewModel: cardViewModel, cardViewId: cardItem.id)
            Spacer().frame(height: 30)
            EmotionView(cardViewModel: cardViewModel, cardItem: cardItem)
        }
        
    }
    
    // 위치 및 시간 정보를 보여주는 HStack을 별도의 ViewBuilder로 추출
    @ViewBuilder
    private var locationAndTimeInfo: some View {
        
        HStack {
            Image("Location").padding(.bottom,3)
            ScrollingTextView(text: "\(cardItem.location)", maxWidth: 130)
                        
            Spacer()
            Text("\(cardItem.weather)")
                .font(.pretendardMedium11)
            Image("Weather_Sunny")
        }
        HStack {
            let (date, time) = cardViewModel.formatDateAndTime(dateString: cardItem.recordedAt)
            
            Image("CardTime")
            Text("\(date)").font(.pretendardMedium11)
            //cardItem.recordedAt
            //날짜부가 들어가야함
            Spacer()
            CustomCarbarDivider()
            Text("\(time)").font(.pretendardMedium11) // 시간부만 들어가야함
            //cardItem.recordedAt

        }
        
    }
}
import SwiftUI

struct ScrollingTextView: View {
    var text: String
    var maxWidth: CGFloat
    @State private var animateText = false
    @State private var textWidth: CGFloat = 0
    
    var body: some View {
        GeometryReader { geometry in
            HStack(spacing: 0) {
                ForEach(0..<2) { _ in  // 텍스트를 두 번 복제
                    Text(text)
                        .font(.pretendardMedium11)
                        .lineLimit(1)
                        .background(GeometryReader {
                            Color.clear.preference(key: TextWidthPreferenceKey.self,
                                                   value: $0.frame(in: .local).size.width)
                        })
                        .fixedSize(horizontal: true, vertical: false)
                }
            }
            .offset(x: animateText ? -2 * textWidth : 0, y: 0)  // 시작 위치에서 두 배의 텍스트 너비만큼 이동
            .onPreferenceChange(TextWidthPreferenceKey.self) { width in
                self.textWidth = width / 2  // 하나의 텍스트 너비 계산
                withAnimation(Animation.linear(duration: Double(textWidth) * 2 / 30).repeatForever(autoreverses: false)) {
                    animateText = true
                }
            }
        }
        .frame(width: maxWidth, height: 20)  // 텍스트 뷰의 너비와 높이 제한
        .clipped()  // 프레임 밖의 내용을 잘라냄
    }
}

// Preference key to pass the text width
struct TextWidthPreferenceKey: PreferenceKey {
    static var defaultValue: CGFloat = 0
    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = nextValue()
    }
}






struct HeaderView: View {
    @Binding var isExpanded: Bool
    @State private var isHeartFilled = false // 하트가 채워졌는지 여부
    var cardItem: cardViews
    @ObservedObject var cardViewModel: CardViewModel
    var body: some View {
        VStack{
            HStack {
                Button(action: {
                    // 하트 버튼을 눌렀을 때의 액션
                    isHeartFilled.toggle()
                    cardViewModel.updateCardViewLikeStatus(cardViewId: cardItem.id)
                }) {
                    Image(isHeartFilled ? "HeartFill" : "HeartEmpty")
                    
                }
                let (date, time) = cardViewModel.formatDateAndTime(dateString: cardItem.recordedAt)
                Text("\(time)") // 타이틀 예시
                    .foregroundColor(.black)
                    .font(.pretendardExtrabold14)
                Spacer()
                
                Text("_00\(cardItem.id)")
                    .font(.pretendardExtrabold14)
                    .foregroundColor(.black)
            }
            .padding(.horizontal,10)
            .padding(.bottom,10)
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
                    Text("\(cardItem.weather)")
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



struct DynamicGradientRectangleView: View {
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    @ObservedObject var cardViewModel: CardViewModel
    let longText: String

    var playerItem: AVPlayerItem?
    var cardItem : cardViews
    
    var body: some View {
        //ScrollView {
        VStack {

            let url = URL(string: cardItem.recordFileUrl)!
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


//
//struct DynamicGradientImagePicker: View {
//    @State private var showingImagePicker = false
//    @State private var showingAddButton = false // 처음에는 추가 버튼이 보이지 않음
//    @State private var selectedImages: [UIImage] = []
//    @ObservedObject var cardViewModel: CardViewModel
//    
//
//    var body: some View {
//        VStack {
//            HStack {
//                Spacer() // HStack의 왼쪽에 Spacer를 추가하여 오른쪽으로 요소를 밀어냄
//                Button(action: {
//                    showingAddButton.toggle() // '이미지 추가하기' 버튼을 토글
//                }) {
//                    HStack {
//                        Text("사진 추가")
//                            .font(.pretendardMedium11)
//                            .foregroundColor(.black)
//                        Image("Imageadd")
//                            .foregroundColor(.white)
//                    }
//                    .padding(.horizontal,10)
//                }
//            }
//            
//            if showingAddButton {
//                // 이미지들과 추가 버튼을 보여주는 스크롤 뷰
//                CustomDividerCardView()
//                ScrollView(.horizontal, showsIndicators: false) {
//                    HStack(spacing: 10) {
//                        ForEach(selectedImages.indices, id: \.self) { index in
//                            Image(uiImage: selectedImages[index])
//                                .resizable()
//                                .frame(width: 66, height: 77)
//                                .cornerRadius(3)
//                                .aspectRatio(contentMode: .fill)
//                        }
//                        addButton()
//                    }
//                }
//                .background(Color.homeBack)
//                .cornerRadius(3)
//                .frame(width: 340) // 가로 크기 고정
//            }
//        }
//        .sheet(isPresented: $showingImagePicker, onDismiss: loadImage) {
//            ImagePicker(selectedImage: $selectedImage)
//        }
//    }
//    
//    private func addButton() -> some View {
//        Button(action: {
//            showingImagePicker = true
//        }) {
//            RoundedRectangle(cornerRadius: 3)
//                .fill(Color.gray)
//                .frame(width: 66, height: 77)
//                .overlay(
//                    Image(systemName: "plus")
//                        .foregroundColor(.white)
//                )
//        }
//    }
//    
//    @State private var selectedImage: UIImage?
//    
//    func loadImage() {
//        guard let selectedImage = selectedImage else { return }
//        selectedImages.append(selectedImage)
//      
//    }
//}

struct DynamicGradientImagePicker: View {
    @State private var showingImagePicker = false
    @State private var showingAddButton = false
    @State private var selectedImages: [UIImage] = [] // 옵셔널이 아닌 UIImage 배열로 변경
    @ObservedObject var cardViewModel: CardViewModel
    var cardViewId: Int // 이 ID는 이 뷰로 전달되어야 합니다

    var body: some View {
        VStack {
            HStack {
                Spacer()
                if !showingAddButton {
                    Button(action: {
                        showingAddButton = true
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

            if showingAddButton {
                CustomDividerCardView()
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 10) {
                        ForEach(selectedImages.indices, id: \.self) { index in
                            Image(uiImage: selectedImages[index])
                                .resizable()
                                .frame(width: 66, height: 77)
                                .cornerRadius(3)
                                .aspectRatio(contentMode: .fill)
                        }
                        addButton() // 맨 끝에 추가 버튼
                    }
                }
                .background(Color.homeBack)
                .cornerRadius(3)
                .frame(width: 340)
            }
        }
        .sheet(isPresented: $showingImagePicker) {
            ImagePicker(selectedImage: Binding(
                get: { UIImage() },
                set: { image in
                    if let image = image {
                        selectedImages.append(image)
                        cardViewModel.uploadImages([image], to: cardViewId)
                    }
                }
            ))
        }
    }
    
    
    @ViewBuilder
    private func addButton() -> some View {
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





struct EmotionView: View {
    @ObservedObject var cardViewModel: CardViewModel
    var cardItem: cardViews
    
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
 
            ForEach(cardViewModel.emotions, id: \.name) { emotion in
                        emotionRow(emotion: emotion)
                    }
        }
        .padding(.horizontal, 1) // HStack에 패딩을 적용하여 내용이 화면 가장자리에 붙지 않도록 합니다.
        .onAppear{
            cardViewModel.updateEmotions(happy: cardItem.happy, sad: cardItem.sad, angry: cardItem.angry, neutral: cardItem.neutral, disgust: cardItem.disgust)
        }
    }
 
    
    
    @ViewBuilder
      private func emotionRow(emotion: EmotionDataCard) -> some View {
          HStack {
              Image(emotion.image)
                  .resizable()
                  .scaledToFit()
                  .frame(width: 12, height: 12)

              Text(emotion.name)
                  .font(.pretendardMedium11)
                  .frame(width: 50, alignment: .leading)
              
              Spacer().frame(width:35)
              
              ProgressView(value: emotion.score, total: 1.0)
                  .progressViewStyle(LinearProgressViewStyle(tint: emotion.color))
                  .frame(width: 136)

              Spacer().frame(width:35)
              
              Text("\(Int(emotion.score * 100))%")
                  .font(.pretendardMedium11)
                  .frame(width: 27, alignment: .trailing)
          }
      }
    
   
}
 






















struct CustomDialogRecordCard: View {
    @Binding var isActive: Bool
    
    let title: String
    let message: String
    let yesAction: () -> Void
    let noAction: () -> Void
    @State private var showingCustomAlert = false
    
    @State private var offset: CGFloat = 1000
    
    var body: some View {
        
        ZStack{
            Color(showingCustomAlert ? .black : .black)
                .opacity(showingCustomAlert ? 1.0 : 0.5)
                .edgesIgnoringSafeArea(.all)
                .animation(.easeInOut, value: showingCustomAlert)
            
            VStack {
                Text(title)
                    .font(.pretendardExtrabold16)
                
                    .padding()
                
                Text(message)
                    .font(.pretendardMedium14)
                    .foregroundColor(Color.gray500)
                    .padding(.bottom)
                
                HStack { // 버튼 사이 간격을 0으로 조정
                    Button {
                        yesAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("네")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 36) // 버튼의 크기 조절
                    }
                    
                    Rectangle() // 빨간색 세로줄 추가
                        .fill(Color.gray500)
                        .frame(width: 2, height: 20)
                    
                    Button {
                        noAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("아니요")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 36) // 버튼의 크기 조절
                    }
                }.padding(.bottom,10)
                
            }
            .frame(width: 280, height: 158) // 다이얼로그의 크기 조절
            
            .background(.homeBack)
            .clipShape(RoundedRectangle(cornerRadius: 0))
            
        }
        
        // .ignoresSafeArea()
    }
    
    func close() {
        withAnimation(.spring()) {
            offset = 1000
            isActive = false
        }
    }
}


struct CustomConfirmationDialog: View {
    @Binding var isActive: Bool
    var title: String
    var message: String
    var yesAction: () -> Void
    var noAction: () -> Void
    @State private var offset: CGFloat = 1000
    
    var body: some View {
        ZStack {
            // 어두운 배경
            Color.black.opacity(0.4).edgesIgnoringSafeArea(.all)
            
            VStack {
                Text(title)
                    .font(.pretendardExtrabold16)
                
                    .padding()
                
                Text(message)
                    .font(.pretendardMedium14)
                    .foregroundColor(Color.gray500)
                    .padding(.bottom)
                
                HStack { // 버튼 사이 간격을 0으로 조정
                    Button {
                        yesAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("네")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 36) // 버튼의 크기 조절
                    }
                    
                    Rectangle() // 빨간색 세로줄 추가
                        .fill(Color.gray500)
                        .frame(width: 2, height: 20)
                    
                    Button {
                        noAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("아니요")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 36) // 버튼의 크기 조절
                    }
                }.padding(.bottom,10)
                
            }
            .frame(width: 280, height: 158) // 다이얼로그의 크기 조절
            
            .background(.homeBack)
            .clipShape(RoundedRectangle(cornerRadius: 0))
        }
        .onTapGesture {
            // 배경 탭시 다이얼로그 닫기 (선택적)
        }
        
    }
    
    func close() {
        withAnimation(.spring()) {
            offset = 1000
            isActive = false
        }
    }
    
}
