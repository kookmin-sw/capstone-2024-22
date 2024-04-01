//
//  TimerView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation

import SwiftUI



struct LikeView: View {
    @ObservedObject var viewModel = LikeViewModel()
    var day: Date
    var item: Item
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    @State private var isDeleteMode = false // 삭제 모드 상태
    @State private var isSelected = false // 체크박스 선택 여부
    @State private var showConfirmationDialog = false // 커스텀 다이얼로그 표시 여부
    @ObservedObject var cardViewModel : CardViewModel

    var body: some View {
        ScrollView {
            VStack {
                
          Text("")
          Text("f")
            }
        }
    }
}

//
//
//struct AccordionView1: View {
//    @State private var isExpanded = false
//    @State private var isSelected = false
//    @ObservedObject var audioRecorderManager: AudioRecorderManager
//    @Binding var isDeleteMode: Bool
//    @Binding var showConfirmationDialog: Bool
//    @ObservedObject var cardViewModel : CardViewModel
//    
//    var body: some View {
//        VStack {
//            // 삭제 모드 활성화 시 체크박스와 함께 컨텐츠를 표시하는 로직
//            if isDeleteMode {
//                HStack {
//                    Button(action: { isSelected.toggle() }) {
//                        Image(systemName: isSelected ? "checkmark.square.fill" : "square")
//                            .foregroundColor(.blue)
//                    }
//                    .transition(.move(edge: .leading))
//                    .padding(.leading, 20)
//
//                    DisclosureGroup(isExpanded: $isExpanded) {
//                        contentVStack
//                    } label: {
//                        HeaderView(cardViewModel: cardViewModel)
//                    }
//                    .accentColor(.black)
//                    .padding()
//                    .background(Color.homeBack)
//                    .cornerRadius(10)
//                    .overlay(
//                        RoundedRectangle(cornerRadius: 10)
//                            .stroke(Color.toastColor, lineWidth: 2)
//                    )
//                    .offset(x: 20) // 삭제 모드일 때 오른쪽으로 이동
//                }
//            } else {
//                // 기본 모드에서는 컨텐츠만 표시
//                DisclosureGroup(isExpanded: $isExpanded) {
//                    contentVStack
//                } label: {
//                    HeaderView()
//                }
//                .accentColor(.black)
//                .padding()
//                .background(Color.homeBack)
//                .cornerRadius(10)
//                .overlay(
//                    RoundedRectangle(cornerRadius: 10)
//                        .stroke(Color.toastColor, lineWidth: 2)
//                )
//            }
//        }
//        .animation(.default, value: isDeleteMode)
//        .onTapGesture {
//            if !isDeleteMode {
//                withAnimation {
//                    isExpanded.toggle()
//                }
//            }
//        }
//    }
//
//    // 컨텐츠 VStack을 별도의 ViewBuilder로 추출하여 재사용
//    @ViewBuilder
//    private var contentVStack: some View {
//        VStack {
//            Spacer().frame(height: 40)
//            locationAndTimeInfo
//            DynamicGradientRectangleView(audioRecorderManager: audioRecorderManager, longText: "긴 텍스트 예시")
//            DynamicGradientImagePicker()
//            Spacer().frame(height: 30)
//            EmotionView()
//        }
//    }
//
//    // 위치 및 시간 정보를 보여주는 HStack을 별도의 ViewBuilder로 추출
//    @ViewBuilder
//       private var locationAndTimeInfo: some View {
//           if let firstCardItem = cardViewModel.cardItems.first {
//               HStack {
//                   Image("Location")
//                   Text("선유도 공영주차장").font(.caption)
//                   Spacer()
//                   Text("해가 쨍쨍한날").font(.caption)
//                   Image("Weather_Sunny")
//               }
//               HStack {
//                   Image("CardTime")
//                   // 첫 번째 더미 데이터의 date 값을 사용하여 Text 뷰에 표시
//                   Text(firstCardItem.date).font(.caption) // 예: "2024.03.05"
//                   Spacer()
//                   Image("bar")
//                   Text(firstCardItem.time).font(.caption) // 예: "15:03"
//               }
//           }
//       }
//   }
//
//
//
//
//
//struct HeaderView1: View {
//   
//    @State private var isHeartFilled = false // 하트가 채워졌는지 여부
//
//    var body: some View {
//        VStack{
//            HStack {
//                Button(action: {
//                    // 하트 버튼을 눌렀을 때의 액션
//                    isHeartFilled.toggle()
//                }) {
//                    Image(isHeartFilled ? "HeartFill" : "HeartEmpty")
//                       
//                }
//                Text("15:03") // 타이틀 예시
//                    .foregroundColor(.black)
//                    .fontWeight(.bold)
//                Spacer()
//                Text("001")
//                    .foregroundColor(.black)
//            }
//            .padding(.horizontal,10)
//            .background(Color.homeBack) // 헤더 배경색
//            .cornerRadius(10)
//            CustomHomeVDividerCard()
//            
//            HStack{
//                Text("꽤나 즐거운 대화였네요")
//                    .font(.caption)
//                    .foregroundColor(.black)
//                    .padding(.horizontal,10)
//                    .padding(.top,10)
//                Spacer()
//                Text("해가 쨍쨍한날")
//                    .font(.caption)
//                    .foregroundColor(.black)
//                    .padding(.top,10)
//              
//                Image("Weather_Sunny")
//                    
//                    .padding(.top,10)
//            }
//        }
//    }
//}
//
//struct DynamicGradientRectangleView1: View {
//    @ObservedObject var audioRecorderManager: AudioRecorderManager
//    let longText: String
//    
//    var body: some View {
//        //ScrollView {
//            VStack {
//                AudioPlayerControls(audioRecorderManager: audioRecorderManager)
//                    .padding()
//                
//                Text(longText)
//                    .padding()
//            }
//            .background(
//                LinearGradient(gradient: Gradient(colors: [.homeBack, .toastColor]), startPoint: .top, endPoint: .bottom)
//            )
//            .cornerRadius(3)
//           // .padding(.horizontal,3)
//            .frame(width: 340)
//        //}
//    }
//}
//
//struct DynamicGradientImagePicker1: View {
//    @State private var showingImagePicker = false
//    @State private var showingAddButton = false // 처음에는 추가 버튼이 보이지 않음
//    @State private var selectedImages: [UIImage?] = []
//
//    var body: some View {
//        VStack {
//          
//            HStack {
//                Spacer() // HStack의 왼쪽에 Spacer를 추가하여 오른쪽으로 요소를 밀어냄
//                if !showingAddButton {
//                    // '이미지 추가하기' 텍스트 버튼
//                    Group{
//                        Button(action: {
//                            showingAddButton = true // '이미지 추가하기' 버튼을 누르면 추가 버튼 생성
//                            selectedImages.append(nil) // 추가 버튼에 해당하는 nil 추가
//                        }) {
//                            HStack {
//                                Text("이미지 추가하기")
//                                    .foregroundColor(.black)
//                                Image("Imageadd")
//                                    .foregroundColor(.white)
//                            }
//                            .padding(.horizontal,10)
//                       
//                          
//                        }
//
//                    }
//                }
//            }
//            
//            if showingAddButton {
//                // 이미지들과 추가 버튼을 보여주는 스크롤 뷰
//                CustomDividerCardView()
//                ScrollView(.horizontal, showsIndicators: false) {
//                    HStack(spacing: 10) {
//                        ForEach(0..<selectedImages.count, id: \.self) { index in
//                            if let image = selectedImages[index] {
//                                Image(uiImage: image)
//                                    .resizable()
//                                    .frame(width: 66, height: 77)
//                                    .cornerRadius(3)
//                            } else {
//                                addButton(index: index)
//                            }
//                        }
//                    }
//                }
//                .background(Color.homeBack)
//                .cornerRadius(3)
//                .frame(width: 340) // 가로 크기 고정
//            }
//        }
//        .sheet(isPresented: $showingImagePicker) {
//            ImagePicker(selectedImage: Binding(
//                get: { UIImage() },
//                set: { image in
//                    if let image = image, let index = selectedImages.lastIndex(where: { $0 == nil }) {
//                        selectedImages[index] = image // 이미지 추가
//                        selectedImages.append(nil) // 새로운 추가 버튼 생성
//                    }
//                }
//            ))
//        }
//    }
//    
//    @ViewBuilder
//    private func addButton(index: Int) -> some View {
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
//}
//
//
//struct EmotionView1 : View {
//    var body: some View {
//        VStack{
//            HStack{
//                Text("꽤나 즐거운 대화였어요")
//                Spacer()
//                Text("감정분석")
//               
//
//            }
//            CustomEmotionViewDivider()
//            
//            HStack{
//                Text("즐거워요")
//                    .font(.caption)
//                //물차는 뷰
//                ProgressView(value: 0.6, total: 1.0)
//                              .progressViewStyle(LinearProgressViewStyle(tint: .homeRed)) // 빨간색으로 진행률 표시
//                              .frame(width: 200, height: 20) // 크기 조절
//                              .padding()
//                Text("60 %")
//                    .font(.caption)
//            }
//            HStack{
//                Text("걱정돼요")
//                    .font(.caption)
//                //물차는 뷰
//                ProgressView(value: 0.2, total: 1.0)
//                              .progressViewStyle(LinearProgressViewStyle(tint: .black)) // 빨간색으로 진행률 표시
//                              .frame(width: 200, height: 20) // 크기 조절
//                              .padding()
//                Text("20 %")
//                    .font(.caption)
//            }
//            HStack{
//                Text("낮설어요")
//                    .font(.caption)
//                //물차는 뷰
//                ProgressView(value: 0.15, total: 1.0)
//                              .progressViewStyle(LinearProgressViewStyle(tint: .StrangeColor)) // 빨간색으로 진행률 표시
//                              .frame(width: 200, height: 20) // 크기 조절
//                              .padding()
//                Text("15 %")
//                    .font(.caption)
//            }
//            HStack{
//                Text("불안해요")
//                    .font(.caption)
//                //물차는 뷰
//                ProgressView(value: 0.05, total: 1.0)
//                              .progressViewStyle(LinearProgressViewStyle(tint: .unsafeColor)) // 빨간색으로 진행률 표시
//                              .frame(width: 200, height: 20) // 크기 조절
//                              .padding()
//                Text("5 %")
//                    .font(.caption)
//            }
//        }
//    }
//}
//
//struct AudioPlayerControls1: View {
//    @ObservedObject var audioRecorderManager: AudioRecorderManager
//   
//    var body: some View {
//        // 녹음 파일 재생 관련 UI 구성
//        // 예시: 재생, 정지 버튼 등
//        VStack {
//            ProgressView(value: audioRecorderManager.playbackProgress)
//           .progressViewStyle(LinearProgressViewStyle())
//           .frame(height: 20)
//           .padding()
//            HStack{
//                if let lastRecording = audioRecorderManager.recordedFiles.last {
//                    Button("재생") {
//                        audioRecorderManager.startPlaying(recordingURL: lastRecording)
//                    }
//                }
//                Button("정지") {
//                    audioRecorderManager.stopPlaying()
//                }
//            }
//         Spacer()
//        }
//    
//    }
//}
//
