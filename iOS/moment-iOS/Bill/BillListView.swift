//
//  MemoListView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI

struct BillListView: View {
    
    @EnvironmentObject private var pathModel: PathModel
    @EnvironmentObject private var billListViewModel : BillListViewModel
    @EnvironmentObject private var homeBaseViewModel : HomeBaseViewModel
    
    
    var body: some View {
        
        ZStack{
            Color(.homeBack).edgesIgnoringSafeArea(.all)
            VStack{
                
                AnnouncementView()
                
                
            }
            
        }
        
    }
}

struct ReceiptGroupView: View {
    var body: some View {
        ScrollView{
            VStack{
                HStack
                {
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                }
                HStack
                {
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                }
                HStack
                {
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                }
            }
        }
        .navigationBarTitle("새 예시", displayMode: .inline)
    }
}



private struct AnnouncementView: View {
    @EnvironmentObject private var calendarViewModel: CalendarViewModel
    @EnvironmentObject private var billListViewModel: BillListViewModel
    
    // NavigationLink 활성화를 위한 State 변수들
    @State private var isShowingCreateView = false
    @State private var isShowingReceiptsView = false
    
    var body: some View {
        NavigationView {
            VStack(spacing: 15) {
                Spacer()
                HStack{
                    Spacer()
                    NavigationLink(destination: ReceiptGroupView(), isActive: $isShowingCreateView) {
                        Button("영수증 모아보기") {
                            isShowingCreateView = true
                        }
                        .foregroundColor(.black)
                        .padding()
                    }
                }
                StatsCardView()
                
                Spacer().frame(height: 10)
                
                // "만들기" 버튼과 해당하는 NavigationLink
                NavigationLink(destination: ReceiptsView(), isActive: $isShowingReceiptsView) {
                    Button("만들기") {
                        isShowingReceiptsView = true
                    }
                    .foregroundColor(.white)
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color.blue)
                    .cornerRadius(10)
                }
                
                
                Spacer()
                
                
            }
            .font(.system(size: 16))
            .foregroundColor(.customGray2)
            .padding()
            .navigationBarTitle("", displayMode: .inline)
        }
    }
}

struct StatsCardView: View {
    var topColor: Color = .homeRed
    var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
    
    var body: some View {
        VStack(spacing: 0) {
            //ZStack{
            // 상단 색상 바
            Rectangle()
                .fill(topColor)
                .frame(height: 50) // 상단 바의 높이를 설정합니다.
                .overlay(
                    HStack{
                        Text("암스테르담 성당 여행") // 여기에 원하는 텍스트를 입력합니다.
                            .foregroundColor(textColor) // 텍스트 색상 설정
                        
                            .font(.pretendardMedium14)
                            .padding()
                        Spacer()
                        Image("Logo")
                            .padding()
                    }
                )
            
            // }
            // 나머지 카드 부분
            Rectangle()
                .fill(Color.Secondary50)
                .frame(height: 450)
                .overlay(
                    VStack{
                        Text("티켓이 발행된 날짜는 2024.04.08 입니다 이 티켓이 발행된 날짜는 2024 04 08 입니다 이 ")
                            .font(.pretendardMedium8)
                            .foregroundColor(.red)
                            Spacer()
                        Text("여행의 기록을 한줄로 기록하세요 :)")
                            .font(.pretendardMedium14)
                            .foregroundColor(.gray500)
                            .padding(.top,30)
                        
                        HStack(alignment: .center)
                        {
                            Image("Locationred")
                            
                            Text("북촌 한옥마을")
                                .font(.pretendardMedium14)
                                .foregroundColor(.homeRed)
                            
                        }
                        
                     
                            Text("서울")
                                .font(.pretendardExtrabold45)
                                .foregroundColor(.homeRed)
                        Image("airplane")
                            
                        
                        HStack(alignment: .center)
                        {
                            Image("Locationred")
                            Text("암스테르담 공항")
                                .font(.pretendardMedium14)
                                .foregroundColor(.homeRed)
                            
                        }
                        
                        
                               Text("암스테르담")
                                   .font(.pretendardExtrabold45)
                                   .foregroundColor(.homeRed)
                        
                        Image("cut")
                        
                    }
                )
        }
        .frame(width: 340, height: 500)
        
        .cornerRadius(5) // 모서리를 둥글게 처리합니다.
        .overlay(
            RoundedRectangle(cornerRadius: 3)
                .stroke(Color.Secondary50, lineWidth: 1)
        )
    }
}


struct ReceiptsView: View {
    var body: some View {
        NavigationView {
            VStack {
                // 테두리만 있는 사각형 안의 텍스트
                NavigationLink(destination: ReceiptDetailView()) {
                    Text("여행1")
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(RoundedRectangle(cornerRadius: 10).stroke(Color.blue, lineWidth: 2))
                        .foregroundColor(.black)
                }
                .padding() // 네비게이션 링크에 패딩을 적용
            }
            .navigationBarTitle("영수증", displayMode: .inline)
        }
    }
}

struct ReceiptDetailView: View {
    var body: some View {
        Rectangle()
            .fill(Color.gray)
            .frame(height: 500)
            .overlay(
                Image(systemName: "pencil.tip")
                    .font(.largeTitle)
                    .foregroundColor(.customGray2)
            )
            .cornerRadius(10)
            .navigationBarTitle("영수증 상세", displayMode: .inline)
    }
}




#Preview {
    BillListView()
        .environmentObject(PathModel())
        .environmentObject(BillListViewModel())
}
