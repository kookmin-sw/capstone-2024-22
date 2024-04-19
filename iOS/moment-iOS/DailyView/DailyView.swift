//
//  DailyView.swift
//  moment-iOS
//
//  Created by 양시관 on 4/8/24.
//

import Foundation

import SwiftUI
import Foundation

struct DailyView: View {
    @ObservedObject var viewModel = DailyItemViewModel() // 뷰 모델 인스턴스화
    @Environment(\.presentationMode) var presentationMode
    var body: some View {
        ZStack {
            Color(.homeBack).edgesIgnoringSafeArea(.all)
            
            VStack {
                
                Button(action: {
                    // "뒤로" 버튼의 액션: 현재 뷰를 종료
                    self.presentationMode.wrappedValue.dismiss()
                    
                }) {
                    HStack {
                        
                        
                        Text("뒤로")
                            .padding(.horizontal,20)
                            .padding()
                            .font(.yjObangBold15)
                            .tint(Color.black)
                        Spacer()
                    }
                }
                CustomTitleMainDivider()
                    .padding(.bottom, -10)
                HStack {
                    
                    Spacer()
                    Text("일상기록")
                        .padding(.horizontal, 30)
                    
                        .font(.pretendardBold22)
                        .foregroundColor(.black)
                }
                CustomTitleMainDivider()
                    .padding(.top, -10)
                    .padding(.bottom,10)
                
                // 항목 리스트
                ScrollView(.vertical, showsIndicators: false) {
                    LazyVStack(spacing: 10) {
                        ForEach(viewModel.dailyItems) { item in
                                                   NavigationLink(destination: Text("\(item.name) 상세 페이지")) {
                                                       DailyItemViewCell(item: item) // ItemViewCell을 DailyItem에 맞게 수정
                                                   }
                            CustomHomeSubDivider()
                        }
                    }
                    
                }
            }
        }.navigationBarBackButtonHidden()
    }
}

// DailyItem에 맞게 ItemViewCell 수정
struct DailyItemViewCell: View {
    var item: DailyItem
    
    var body: some View {
        HStack {
            
            Text(item.date)
                .font(.pretendardMedium11)
                .foregroundColor(.black)
            Rectangle()
                .fill(Color.homeRed)
                .frame(width: 1, height: 42)
                .padding(.leading, 5)
                .padding(.trailing, 0)
            
            Text(item.name)
                .font(.pretendardMedium11)
                .foregroundColor(.gray600)
            
            
            Spacer()
        }
        .padding(.horizontal,20)
        .padding()
    }
}