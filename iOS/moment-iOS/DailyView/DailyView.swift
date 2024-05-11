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
    @ObservedObject var DailyViewModel = DailyItemViewModel() // 뷰 모델 인스턴스화
    @Environment(\.presentationMode) var presentationMode
    @State private var selectedTripFileId: Int? = 0 // 선택된 여행 파일 ID
    @ObservedObject var cardViewModel = CardViewModel()
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    
    
    
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
                ScrollView {
                    LazyVStack(spacing: 10) {
                        ForEach(DailyViewModel.tripDailyFiles) { tripDailyItem in
                            NavigationLink(destination: DailyCardView(tripFileId: tripDailyItem.id, audioRecorderManager: audioRecorderManager, cardViewModel: cardViewModel)) {
                                DailyItemViewCell(tripDailyItem: tripDailyItem)
                            }
                            CustomHomeSubDivider()
                        }
                    }
                }
            }
        }.navigationBarBackButtonHidden()
            .onAppear{
                DailyViewModel.fetchTripFiles()
            }
        
    }
}

// DailyItem에 맞게 ItemViewCell 수정
struct DailyItemViewCell: View {
    var tripDailyItem : TripFileDaily
    @ObservedObject var DailyViewModel = DailyItemViewModel() // 뷰
    
    var body: some View {
        HStack {
            
            Text("\(tripDailyItem.yearDate)")//날짜
                .font(.pretendardMedium11)
                .foregroundColor(.black)
            Rectangle()
                .fill(Color.homeRed)
                .frame(width: 1, height: 42)
                .padding(.leading, 5)
                .padding(.trailing, 0)
            
            Text("\(tripDailyItem.totalCount)개의 파일이 있어요")
                .font(.pretendardMedium11)
                .foregroundColor(.gray600)
            
            
            Spacer()
        }
        .padding(.horizontal,20)
        .padding()
        .onAppear{
            DailyViewModel.fetchTripFiles()
        }
    }
    
}
