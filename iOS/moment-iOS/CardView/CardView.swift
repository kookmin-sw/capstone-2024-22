//
//  CardView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/28/24.
//

import SwiftUI

struct CardView: View {
    var day: Date
       var item: Item

    @ObservedObject var audioRecorderManager: AudioRecorderManager
    
    var body: some View {
        ZStack{
            ScrollView{
                AccordionView(audioRecorderManager: audioRecorderManager)
            }
        }.background(Color.homeBack)
    }
}

struct AccordionView: View {
    @State private var isExpanded = false
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    

    var body: some View {
        VStack {
            DisclosureGroup(isExpanded: $isExpanded) {
                // 카드 펼쳐졌을 때 보여질 내용
               //TODO: - 여기에다가 이제 텍스트랑 녹음 뷰 만들어서 넣어야함
                VStack{
                    Spacer().frame(height: 40)
                    HStack{
                        Image("Location")
                        Text("선유도 공영주차장")
                            .font(.caption)
                        Spacer()
                        Text("해가 쨍쨍한날")
                            .font(.caption)
                        Image("Weather_Sunny")
                    }
                    
                    HStack{
                        Image("CardTime")
                        Text("2024. 03. 05. 화요일")
                            .font(.caption)
                        Spacer()
                        Image("bar")
                       
                        Text("15:03")
                            .font(.caption)
                    }
                    DynamicGradientRectangleView(audioRecorderManager: audioRecorderManager, longText: "머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어머이ㅏ럼ㄴ어")
                  
                    DynamicGradientImagePicker()
                    Spacer().frame(height: 30)
                    EmotionView()
                }
            } label: {
                // 접혀있을 때 보여질 커스텀 뷰
                HeaderView()
            }
            .accentColor(.black) // 확장/축소 버튼의 색상
            .padding()
            .background(Color.homeBack) // 배경색
            .cornerRadius(10)
            .overlay(
                RoundedRectangle(cornerRadius: 10) // 스트로크를 적용할 사각형
                    .stroke(Color.toastColor, lineWidth: 2) // 스트로크 색상과 두께 설정
            )
            .onTapGesture {
                withAnimation {
                    isExpanded.toggle()
                }
            }
            Spacer()
        }
        .padding()
    }
}


struct HeaderView: View {
   
    @State private var isHeartFilled = false // 하트가 채워졌는지 여부

    var body: some View {
        VStack{
            HStack {
                Button(action: {
                    // 하트 버튼을 눌렀을 때의 액션
                    isHeartFilled.toggle()
                }) {
                    Image(isHeartFilled ? "HeartFill" : "HeartEmpty")
                       
                }
                Text("15:03") // 타이틀 예시
                    .foregroundColor(.black)
                    .fontWeight(.bold)
                Spacer()
                Text("001")
                    .foregroundColor(.black)
            }
            .padding(.horizontal,10)
            .background(Color.homeBack) // 헤더 배경색
            .cornerRadius(10)
            CustomHomeVDividerCard()
            
            HStack{
                Text("꽤나 즐거운 대화였네요")
                    .font(.caption)
                    .foregroundColor(.black)
                    .padding(.horizontal,10)
                    .padding(.top,10)
                Spacer()
                Text("해가 쨍쨍한날")
                    .font(.caption)
                    .foregroundColor(.black)
                    .padding(.top,10)
              
                Image("Weather_Sunny")
                    
                    .padding(.top,10)
            }
        }
    }
}

struct DynamicGradientRectangleView: View {
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    let longText: String
    
    var body: some View {
        //ScrollView {
            VStack {
                AudioPlayerControls(audioRecorderManager: audioRecorderManager)
                    .padding()
                
                Text(longText)
                    .padding()
            }
            .background(
                LinearGradient(gradient: Gradient(colors: [.homeBack, .toastColor]), startPoint: .top, endPoint: .bottom)
            )
            .cornerRadius(3)
            .padding(.horizontal,3)
            .frame(width: 340)
        //}
    }
}

struct DynamicGradientImagePicker: View {
    @State private var showingImagePicker = false
    @State private var selectedImages = [UIImage?](repeating: nil, count: 5)

    var body: some View {
        VStack {
            CustomDividerCardView()
            ScrollView(.horizontal, showsIndicators: false) {
                
                HStack(spacing: 10) {
                   
                       
                    // 선택된 이미지들을 보여주는 뷰
                    ForEach(selectedImages.indices, id: \.self) { index in
                        if let image = selectedImages[index] {
                            Image(uiImage: image)
                                .resizable()
                                .frame(width: 66, height: 77)
                                .cornerRadius(3)
                        } else {
                            addButton()
                        }
                    }
                    
                    // 모든 슬롯이 이미지로 채워진 경우, 추가 버튼을 하나 더 보여줍니다.
                    if !selectedImages.contains(nil) {
                        addButton()
                    }
                }
            }
            .background(Color.homeBack)
            .cornerRadius(3)
            .frame(width: 340) // 가로 크기 고정
           
        
        }
        .sheet(isPresented: $showingImagePicker) {
            ImagePicker(selectedImage: Binding(
                get: { UIImage() },
                set: { image in
                    if let image = image {
                        // 이미지가 선택되었을 때, 첫 번째 빈 위치에 이미지를 추가합니다.
                        if let firstEmptyIndex = selectedImages.firstIndex(where: { $0 == nil }) {
                            selectedImages[firstEmptyIndex] = image
                        } else {
                            // 모든 슬롯이 이미 채워져 있는 경우, 배열에 새 이미지 추가
                            selectedImages.append(image)
                        }
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


struct EmotionView : View {
    var body: some View {
        VStack{
            HStack{
                Text("꽤나 즐거운 대화였어요")
                Spacer()
                Text("감정분석")
               

            }
            CustomEmotionViewDivider()
            
            HStack{
                Text("즐거워요")
                    .font(.caption)
                //물차는 뷰
                ProgressView(value: 0.6, total: 1.0)
                              .progressViewStyle(LinearProgressViewStyle(tint: .homeRed)) // 빨간색으로 진행률 표시
                              .frame(width: 200, height: 20) // 크기 조절
                              .padding()
                Text("60 %")
                    .font(.caption)
            }
            HStack{
                Text("걱정돼요")
                    .font(.caption)
                //물차는 뷰
                ProgressView(value: 0.2, total: 1.0)
                              .progressViewStyle(LinearProgressViewStyle(tint: .black)) // 빨간색으로 진행률 표시
                              .frame(width: 200, height: 20) // 크기 조절
                              .padding()
                Text("20 %")
                    .font(.caption)
            }
            HStack{
                Text("낮설어요")
                    .font(.caption)
                //물차는 뷰
                ProgressView(value: 0.15, total: 1.0)
                              .progressViewStyle(LinearProgressViewStyle(tint: .StrangeColor)) // 빨간색으로 진행률 표시
                              .frame(width: 200, height: 20) // 크기 조절
                              .padding()
                Text("15 %")
                    .font(.caption)
            }
            HStack{
                Text("불안해요")
                    .font(.caption)
                //물차는 뷰
                ProgressView(value: 0.05, total: 1.0)
                              .progressViewStyle(LinearProgressViewStyle(tint: .unsafeColor)) // 빨간색으로 진행률 표시
                              .frame(width: 200, height: 20) // 크기 조절
                              .padding()
                Text("5 %")
                    .font(.caption)
            }
        }
    }
}

struct AudioPlayerControls: View {
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

