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
                Text("여기에 카드 내용을 넣어주세요.")
                    .padding()
                    // 카드에 들어갈 추가 내용
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
