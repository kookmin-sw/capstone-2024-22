//
//  ReceiptCompleteviewB.swift
//  moment-iOS
//
//  Created by 양시관 on 5/11/24.
//

import Foundation
import SwiftUI


struct ReceiptCompleteviewB: View {
    var receipt: Receipt
    
    @State var currentPage = 0 // 현재 페이지를 나타내는 상태 변수
    var topColor: Color = .homeRed
    var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
    @EnvironmentObject var sharedViewModel: SharedViewModel
    @State private var tripRecord : String = ""
    @State private var tripExplaneStart : String = ""
    @State private var tripnameStart : String = ""
    @State private var tripnameEnd: String = ""
    @State private var tripExplaneEnd : String = ""
    
    
    var body: some View {
        
        VStack(spacing: 0) {
            Spacer().frame(height: 18)
            HStack{
                
                Spacer().frame(width: 40)
                Text("\(receipt.mainDeparture)")
                    .font(.pretendardMedium14)
                    .foregroundColor(.gray500)  // 글씨
                    .multilineTextAlignment(.center)
                
                Spacer()
            }
            
            Spacer().frame(height: 70)
            
            
            VStack(alignment:.center,spacing:0){
                HStack(alignment:.center,spacing:1)
                {
                    
                    Image("Location")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 19, height: 19)
                    
                    Text("\(receipt.subDeparture)")
                        .font(.pretendardMedium14)
                        .foregroundColor(.gray600)
                        .multilineTextAlignment(.center)
                }.frame(maxWidth: .infinity)
                    .padding(.bottom,8)
                
                
                HStack{
                    Text("\(receipt.mainDestination)")
                        .font(.pretendardExtrabold45)
                        .foregroundColor(.black)  // 글씨
                        .multilineTextAlignment(.center)
                }
                .padding(.bottom,20)
            }
            
            
            Image("Subway")
                .resizable()
                .scaledToFit()
                .frame(width: 40,height: 91)
                .padding(.bottom,20)
            
            
            
            
            VStack(spacing:0){
                HStack(alignment: .center,spacing:0)
                {
                    Image("Location")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 19, height: 19)
                    
                    Text("\(receipt.subDestination)")
                        .font(.pretendardMedium14)
                        .foregroundColor(.gray600)
                        .multilineTextAlignment(.center)
                }  .padding(.bottom,8)
                
                
                
                HStack{
                    
                    Text("\(receipt.oneLineMemo)")
                        .font(.pretendardExtrabold45)
                        .foregroundColor(.black)  // 글씨
                        .multilineTextAlignment(.center)
                }
                .padding(.bottom,20)
                Spacer()
            }
            
            SentimentTrackerViewCompleteB(receipt: receipt)
            
            
            
            
        }
        
        .frame(width: 335, height: 653) // 335,653
        .cornerRadius(5) // 모서리를 둥글게 처리합니다.
        .overlay(
            RoundedRectangle(cornerRadius: 3)
                .stroke(Color.gray500, lineWidth: 1)
            
        )
        //왼쪽 두꺼운 부분의 사각형
        .overlay(
            
            
            Rectangle() // 색칠할 부분의 모양
                .fill(Color.homeRed) // 색칠할 색상
                .frame(width: 30, height: 653), // 색칠할 영역의 크기
            alignment: .leading
            
        ) .overlay(
            Image("LogoVertical") // 다른 이미지 오버레이 추가
            
                .frame(width: 30, height: 645.23), // 이미지 프레임 설정
            
            
            alignment: .topLeading // 이미지 정렬 설정
        )
        //오른족 얇은 부분의 사각형
        .overlay(
            Rectangle() // 색칠할 부분의 모양
                .fill(Color.homeRed) // 색칠할 색상
                .frame(width: 10, height: 596.25), // 색칠할 영역의 크기
            alignment: .init(horizontal: .trailing, vertical: .bottom)
        )
        .overlay(
            Rectangle() // 색칠할 부분의 모양
                .offset(x:15,y:-270)
                .fill(Color.homeRed) // 색칠할 색상
                .frame(width: 305, height: 2.22) // 색칠할 영역의 크기
            
            
            
        )
        .overlay(
            Image("Circledivider")
                .resizable()
                .scaledToFit()
                .frame(width: 466, height: 12)
                .offset(y:130)
        )
        
        
    }
    
}



struct SentimentTrackerViewCompleteB: View {
    var receipt : Receipt
    
    var emotions: [Emotion] {
        [
            Emotion(type: "Happy", value: receipt.happy, color: .Basic, image: "fun"),
            Emotion(type: "Sad", value: receipt.sad, color: .black, image: "sad"),
            Emotion(type: "Angry", value: receipt.angry, color: .Basic, image: "angry"),
            Emotion(type: "Neutral", value: receipt.neutral, color: .homeRed, image: "netral"),
            Emotion(type: "Disgust", value: receipt.disgust, color: .green, image: "disgustFace")
        ].sorted { $0.value > $1.value } // 정렬하여 가장 높은 감정을 위로
    }
    
    var body: some View {
        VStack(spacing:0){
            HStack{
                Text("\(receipt.tripName)")
                    .font(.pretendardMedium14)
                    .padding(.bottom,15)
                    .multilineTextAlignment(.center)
            }
            
            HStack{
                Spacer().frame(width: 142)
                Text("여행 감정")
                    .foregroundColor(.gray500)
                    .frame(width: 50)
                    .font(.pretendardMedium11)
                Spacer().frame(width: 50)
                Text("카드갯수")
                    .foregroundColor(.gray500)
                    .font(.pretendardMedium11)
                    .frame(width: 50)
                Spacer().frame(width: 66)
            }
            HStack{
                Spacer()
                VStack{
                    ForEach(emotions, id: \.type) { emotion in
                        emotionProgressView(emotion: emotion)
                    }
                    //                    HStack{
                    //                        Image("netral")
                    //                        ProgressView(value: 0.6).frame(width: 109,height: 15)
                    //                            .cornerRadius(3)
                    //                            .scaleEffect(x: 1, y: 1, anchor: .center)
                    //                            .tint(.homeRed)
                    //                    }
                    //                    HStack{
                    //                        Image("sad")
                    //                        ProgressView(value: 0.6).frame(width: 109,height: 15)
                    //                            .cornerRadius(3)
                    //                            .scaleEffect(x: 1, y: 1, anchor: .center)
                    //                            .tint(.black)
                    //                    }
                    //                    HStack{
                    //                        Image("fun")
                    //                        ProgressView(value: 0.6).frame(width: 109,height: 15)
                    //                            .cornerRadius(3)
                    //                            .scaleEffect(x: 1, y: 1, anchor: .center)
                    //                            .tint(.Basic)
                    //                    }
                    //                    HStack{
                    //                        Image("angry")
                    //                        ProgressView(value: 0.6).frame(width: 109,height: 15)
                    //                            .cornerRadius(3)
                    //                            .scaleEffect(x: 1, y: 1, anchor: .center)
                    //                            .tint(.green)
                    //                    }
                    
                    
                }
                Spacer().frame(width: 48)
                VStack {
                    Spacer()
                    Text("\(receipt.numOfCard)")
                        .foregroundColor(.homeRed)
                        .font(.yjObangBold20)
                        .frame(width: 60,height: 60)
                        .background(RoundedRectangle(cornerRadius: 8).stroke(Color.black, lineWidth: 2))
                    Spacer()
                }
                .frame(width: 60, height: 60)
                .padding(.trailing,50)
                
            }
        }
        Spacer().frame(height: 30)
    }
    
    private func emotionProgressView(emotion: Emotion) -> some View {
        VStack(alignment: .leading) {
            
            
            HStack {
                
                Image(emotion.image)
                
                ProgressView(value: emotion.value, total: totalEmotionValue())
                    .frame(width: 109, height: 15)
                    .cornerRadius(3)
                    .scaleEffect(x: 1, y: 1, anchor: .center)
                
                    .tint(emotion.color)  // 감정별 색상 적용
                
            
                
            }
        }
    }
    
    // 모든 감정 점수의 합을 계산
    private func totalEmotionValue() -> Double {
        receipt.happy + receipt.sad + receipt.angry + receipt.neutral + receipt.disgust
    }
}


struct Emotion {
    var type: String
    var value: Double
    var color: Color
    var image: String
}
