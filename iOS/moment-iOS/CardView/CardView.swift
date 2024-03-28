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
    
    var body: some View {
        ZStack{
          
            AccordionView()
        }.background(Color.homeBack)
    }
}

struct AccordionView: View {
    @State private var isExpanded = false

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
