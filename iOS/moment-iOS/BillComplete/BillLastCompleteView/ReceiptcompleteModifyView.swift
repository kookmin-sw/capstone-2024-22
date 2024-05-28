//
//  ReceiptcompleteModifyView.swift
//  moment-iOS
//
//  Created by 양시관 on 5/27/24.
//

import Foundation
import Swift
import SwiftUI

struct ReceiptcompleteModifyView: View {
    var receipt: Receipt
    @StateObject var sharedViewModel: SharedViewModel
    @Environment(\.presentationMode) var presentationMode
    @State private var isDialogActive = false
    @State private var isDialogActiveBillCom = false
    var topColor: Color = .homeRed
    var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
    // let item : Item
    @State private var text: String = ""
    @State private var starttrip: String = ""
    @State private var StartLocation : String = ""
    @State private var EndLocation : String = ""
    @State private var isEditing: Bool = false
    @State private var saveButtonTitle = "저장"
    @State private var backButtonTitle = "뒤로"
    @State private var inputText: String = ""
    @State private var EndLocationend : String = ""
    @State private var selectedTab = 0
    @State private var forceUpdate = ""
    
    
    var body: some View{
         
            VStack{
            HStack{
                backButton
                    .padding(.leading,10)
                Spacer()
                saveButton
                    .padding(.trailing,10)
              //  editButton
                   // .padding(.trailing,10)
            }
        }
     
            
            VStack(spacing: 0) {
                
                
            
                
                Rectangle()
                    .fill(topColor)
                    .frame(height: 50) // 상단 바의 높이를 설정합니다.
                    .overlay(
                        HStack{
                            Text("\(receipt.tripName)") // 여기에 원하는 텍스트를 입력합니다.
                                .foregroundColor(textColor) // 텍스트 색상 설정
                            
                                .font(.pretendardMedium14)
                                .padding()
                            
                            //id 예시
                            Text("\(receipt.id)")
                            Spacer()
                            Image("Logo")
                                .padding()
                        }
                    )
                
                // }
                // 나머지 카드 부분
                Rectangle()
                    .fill(Color.Secondary50)
                    .frame(height: 603)
                    .border(.gray500)
                    .overlay(
                        VStack(alignment:.center){
                            
                            Text("티켓이 발행된 날짜는 \(receipt.stDate) 입니다 이 티켓이 발행된 날짜는 \(receipt.edDate) 입니다 이 ")
                                .font(.pretendardMedium8)
                                .foregroundColor(.homeRed)
                            
                            
                            
                            
                            Spacer()
                            
                            
                            TextField("여행의 기록을 한줄로 기록하세요", text: $sharedViewModel.text, prompt: Text("여행의 기록을 한줄로 기록하세요")
                                .foregroundColor(.Natural200))
                            
                            .foregroundColor(.gray500)
                            .font(.pretendardMedium14)
                            .padding(.bottom,30)
                            .multilineTextAlignment(.center)
                            .onChange(of: sharedViewModel.text) { newValue in
                                            // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                            if newValue.count > 14 {
                                                sharedViewModel.text = String(newValue.prefix(14))
                                            }
                                        }
                            
                            
                            
                            
                            
                            VStack(alignment:.center,spacing:0){
                                HStack(alignment:.center,spacing:1)
                                {
                                    
                                    Image("Locationred")
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width: 19, height: 19)
                                    
                                    
                                    TextFieldDynamicWidth(title: "여행의 시작은 여기부터", text: $sharedViewModel.inputText, onEditingChanged: { isEditing in
                                        
                                    }, onCommit: {
                                        
                                    })
                                    
                                    .font(.pretendardMedium14)
                                    .foregroundColor(.homeRed)
                                    .onChange(of: sharedViewModel.inputText) { newValue in
                                        // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                        if newValue.count > 14 {
                                            sharedViewModel.inputText = String(newValue.prefix(14))
                                        }
                                    }
                                }
                                                
                                
                                HStack{
                                    
                                    TextField("출발지",text:$sharedViewModel.StartLocatio,prompt: Text("출발지")
                                        .foregroundColor(.Natural200))
                                    
                                    
                                    .foregroundColor(.homeRed)  // 글씨
                                    .font(.pretendardExtrabold45)
                                    
                                    .multilineTextAlignment(.center)
                                    .onChange(of: sharedViewModel.StartLocatio) { newValue in
                                                    // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                                    if newValue.count > 7 {
                                                        sharedViewModel.StartLocatio = String(newValue.prefix(7))
                                                    }
                                                }
                                }
                            }
                            
                            
                            Image("airplane")
                                .padding(.bottom,20)
                            
                            VStack(spacing:0){
                                HStack(alignment: .center,spacing:0)
                                {
                                    Image("Locationred")
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width: 19, height: 19)
                                    
                                    TextFieldDynamicWidth(title: "기억속에 오래 저장할", text: $sharedViewModel.EndLocationend, onEditingChanged: { isEditing in
                                        
                                    }, onCommit: {
                                        
                                    })
                                    .font(.pretendardMedium14)
                                    .foregroundColor(.homeRed)
                                    .onChange(of: sharedViewModel.EndLocationend) { newValue in
                                                    // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                                    if newValue.count > 14 {
                                                        sharedViewModel.EndLocationend = String(newValue.prefix(14))
                                                    }
                                                }
                                }
                                
                                
                                
                                HStack{
                                    
                                    TextField("도착지",text:$sharedViewModel.EndLocation,prompt: Text("도착지")
                                        .foregroundColor(.Natural200))
                                    .font(.pretendardExtrabold45)
                                    .foregroundColor(.homeRed)  // 글씨
                                    .multilineTextAlignment(.center)
                                    .onChange(of: sharedViewModel.EndLocation) { newValue in
                                                    // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                                    if newValue.count > 7 {
                                                        sharedViewModel.EndLocation = String(newValue.prefix(7))
                                                    }
                                                }
                                }
                            }
                            
                            Spacer()
                            Image("cut")
                                .padding(.bottom,10)
                            
                            StatsViewComplete(receipt: receipt)
                            Spacer()
                        }
                    )
            }
            
            
            .frame(width: 335, height: 653)
            
            .cornerRadius(5) // 모서리를 둥글게 처리합니다.
            .overlay(
                RoundedRectangle(cornerRadius: 3)
                    .stroke(Color.Secondary50, lineWidth: 1)
            )
            
            .background(.homeBack)
            .navigationBarBackButtonHidden()
       Spacer()
    }
      
    
    private var backButton: some View {
        Button(action: {
            presentationMode.wrappedValue.dismiss()
        }) {
            HStack {
                
                Text(backButtonTitle)
                    .padding()
                    .font(.yjObangBold15)
                    .tint(Color.black)
            }
        }
    }
    
    private var exportButton: some View {
        Button(action: {
            print("Export functionality here")
            // Implement export functionality here
        }) {
            Text("내보내기")
                .padding()
                .font(.yjObangBold15)
                .tint(Color.black)
        }
    }
    
    private var editButton: some View {
        Button(action: {
            isEditing.toggle()
            print("Editing mode: \(isEditing)")
            // Implement editing related functionality here
        }) {
            Text("수정")
                .padding()
                .font(.yjObangBold15)
                .tint(Color.black)
        }
    }
    
    private var saveButton : some View {
        Button(action : {
 
            let updatedReceipt = Receipt(
                id: receipt.id,
                    tripId: receipt.tripId,
                    mainDeparture: StartLocation,
                    subDeparture: inputText,
                    mainDestination: EndLocationend,
                    subDestination: EndLocation,
                    oneLineMemo: text,
                    receiptThemeType: receipt.receiptThemeType,
                    createdAt: receipt.createdAt,
                    happy: receipt.happy,
                    sad: receipt.sad,
                    angry: receipt.angry,
                    neutral: receipt.neutral,
                    disgust: receipt.disgust,
                    numOfCard: receipt.numOfCard,
                    stDate: receipt.stDate,
                    edDate: receipt.edDate,
                    tripName: receipt.tripName
                   )
            sharedViewModel.updateReceipt(receipt: updatedReceipt)
                   presentationMode.wrappedValue.dismiss()
        }){
            Text("저장")
                .padding()
                .font(.yjObangBold15)
                .tint(Color.black)
        }
    }
    
}

//
//struct StatsViewModifyComplete: View {
//    //  @EnvironmentObject var sharedViewModel: SharedViewModel
//    var receipt : Receipt
//    
//    var emotions: [EmotionData] {
//        let rawEmotions = [
//            EmotionData(name: "Happy", score: receipt.happy, imageName: "fun", color: .gray),
//            EmotionData(name: "Sad", score: receipt.sad, imageName: "sad", color: .gray),
//            EmotionData(name: "Angry", score: receipt.angry, imageName: "angry", color: .gray),
//            EmotionData(name: "Neutral", score: receipt.neutral, imageName: "netral", color: .gray),
//            EmotionData(name: "Disgust", score: receipt.disgust, imageName: "disgustFace", color: .gray)
//        ]
//        let sortedEmotions = rawEmotions.sorted(by: { $0.score > $1.score })
//        var highestScoreColor = Color.homeRed  // 가장 높은 점수를 가진 감정의 색상
//        var emotionsWithColor = sortedEmotions
//        if let highest = emotionsWithColor.first {
//            emotionsWithColor[0].color = highestScoreColor  // 첫 번째 요소(가장 높은 점수)에 특별 색상 적용
//        }
//        return emotionsWithColor
//    }
//    
//    
//    var body: some View {
//        
//        HStack(spacing:30) {
//            VStack(spacing: 10) {
//                Text("여행 카드")
//                    .font(.pretendardMedium11)
//                    .foregroundColor(.gray500)
//                    .multilineTextAlignment(.center)
//                Text("\(receipt.numOfCard)")
//                    .font(.pretendardExtrabold14)
//                    .foregroundColor(.homeRed)
//                    .multilineTextAlignment(.center)
//                
//                Text("여행 날짜")
//                    .font(.pretendardMedium11)
//                    .foregroundColor(.gray500)
//                    .multilineTextAlignment(.center)
//                Text("\(receipt.stDate)")
//                    .font(.pretendardMedium11)
//                    .foregroundColor(.homeRed)
//                    .multilineTextAlignment(.center)
//                Text("\(receipt.edDate)")
//                    .font(.pretendardMedium11)
//                    .foregroundColor(.homeRed)
//                    .multilineTextAlignment(.center)
//            }
//            
//            VStack(alignment: .leading,spacing: 9) {
//                Text("여행 감정")
//                    .font(.pretendardMedium11)
//                    .foregroundColor(.gray500)
//                //.padding(.vertical,10)
//                    .padding(.top,25)
//                    .padding(.bottom,5)
//                
//                HStack{
//                    
//                    VStack(alignment: .leading) {
//                        ForEach(emotions, id: \.name) { emotion in
//                            emotionProgressView(emotion: emotion)
//                        }
//                    }.padding(.bottom,6)
//                }
//                
//                
//            }
//            
//        }
//        
//        
//        
//        
//        
//    }
//    
//    private func emotionProgressView(emotion: EmotionData) -> some View {
//        VStack(alignment: .leading) {
//            
//            
//            HStack {
//                ProgressView(value: emotion.score, total: totalEmotionValue())
//                    .frame(width: 109, height: 15)
//                    .cornerRadius(3)
//                    .scaleEffect(x: 1, y: 2, anchor: .center)
//                    .tint(emotion.color)  // 감정별 색상 적용
//                
//                Image(emotion.imageName)
//                
//                Text("\(Int(emotion.score))%")
//                    .font(.pretendardMedium11)
//                    .foregroundColor(emotion.color)  // 텍스트에도 감정별 색상 적용
//                    .frame(width: 30)
//            }
//        }
//    }
//    
//    // 모든 감정 점수의 합을 계산
//    private func totalEmotionValue() -> Double {
//        receipt.happy + receipt.sad + receipt.angry + receipt.neutral + receipt.disgust
//    }
//}
