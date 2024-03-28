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
        AccordionView()
    }
}


struct AccordionView: View {
    @State private var isExpanded = false

    var body: some View {
        VStack {
            DisclosureGroup("카드 상단", isExpanded: $isExpanded) {
                VStack {
                    Text("여기에 카드 내용을 넣어주세요.")
                        .padding()
                    // 카드에 들어갈 내용 추가
                }
                .frame(maxWidth: .infinity)
                .background(Color.gray.opacity(0.2)) // 내용의 배경색
                .padding()
            }
            .accentColor(.blue) // 확장/축소 버튼의 색상
            .padding()
            .background(Color.blue.opacity(0.2)) // 카드 배경색
            .cornerRadius(10)
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


