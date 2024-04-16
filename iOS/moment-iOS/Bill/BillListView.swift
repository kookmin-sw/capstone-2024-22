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
    @EnvironmentObject var homeViewModel: HomeViewModel
    
    
    var body: some View {
        
        ZStack{
            Color(.homeBack).edgesIgnoringSafeArea(.all)
            VStack{
                
                AnnouncementView()
                
                
            }.onAppear {
                homeViewModel.fetchTrips()  // 뷰가 나타날 때 데이터를 로드합니다.
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
    @EnvironmentObject var homeViewModel: HomeViewModel
    
    
    
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
                    .font(.pretendardSemiBold18)
                    .padding()
                    .frame(width: 335,height: 52)
                    .background(Color.homeRed)
                    .cornerRadius(3)
                    .padding(.bottom,30)
                    
                }
                
                
                Spacer()
                
                
            }
            .font(.system(size: 16))
            .foregroundColor(.customGray2)
            .padding()
            .navigationBarTitle("", displayMode: .inline)
            .onAppear {
                homeViewModel.fetchTrips()  // 뷰가 나타날 때 데이터를 로드합니다.
            }
        }
    }
}

struct StatsCardView: View {
    @EnvironmentObject var homeViewModel: HomeViewModel
    
    
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
        .onAppear {
            homeViewModel.fetchTrips()  // 뷰가 나타날 때 데이터를 로드합니다.
        }
    }
}

struct ReceiptsView: View {
    @EnvironmentObject var homeViewModel: HomeViewModel // HomeViewModel 인스턴스
    @Environment(\.presentationMode) var presentationMode
    var body: some View {
        ZStack{
            VStack{
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
                ScrollView{
                    LazyVStack(spacing:5){
                        ForEach(homeViewModel.items) { item in
                            NavigationLink(destination: ReceiptDetailView(item: item)) {
                                ReceiptCell(item: item)
                            }
                            .padding(.vertical, 10)
                            CustomHomeSubDivider()
                            
                        }
                    }
                }
                
            }.onAppear {
                homeViewModel.fetchTrips()  // 뷰가 나타날 때 데이터를 로드합니다.
            }
            
            
        }
        .navigationBarBackButtonHidden()
    }
}

struct ReceiptCell: View {
    let item: Item
    @EnvironmentObject var homeViewModel: HomeViewModel
    
    
    var body: some View {
        
        HStack(spacing: 15) {
            
            
            VStack(alignment: .leading, spacing: 10) {
                HStack{
                    
                    
                    VStack{
                        
                        Text(item.startdate)
                            .font(.pretendardMedium11)
                            .foregroundColor(.black)
                        
                        
                        Text(item.enddate)
                            .font(.pretendardMedium11)
                            .foregroundColor(.black)
                    }
                    Rectangle()
                        .fill(Color.homeRed)
                        .frame(width: 1, height: 42)
                        .padding(.leading, 5)
                        .padding(.trailing, 0)
                    
                }
            }
            .padding(.bottom,20)
            .padding(.horizontal,20)
            
            
            
            VStack{
                HStack(spacing: 10) {
                    
                    Spacer()
                    
                    
                    
                    Text(item.tripName)
                        .font(.pretendardExtrabold14)
                        .foregroundColor(.black)
                        .zIndex(2)
                    
                    Rectangle()
                        .fill(Color.homeRed)
                        .frame(width: 1, height: 42)
                        .padding(.leading, 3)
                        .padding(.trailing, 0)
                    
                }.padding(.horizontal,20)
            }
            
            
            
            
        }
        .onAppear {
            homeViewModel.fetchTrips()  // 뷰가 나타날 때 데이터를 로드합니다.
        }
    }
}

// ReceiptDetailView 정의
struct ReceiptDetailView: View {
    @EnvironmentObject var homeViewModel: HomeViewModel
    @Environment(\.presentationMode) var presentationMode
    
    var topColor: Color = .homeRed
    var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
    let item : Item
    var body: some View {
        ZStack{
            
            VStack {
                HStack {
                    Button(action: {
                        presentationMode.wrappedValue.dismiss()
                    }) {
                        HStack {
                            
                            Text("뒤로")
                        }
                        .padding(.horizontal,20)
                        .padding()
                        .font(.yjObangBold15)
                        .tint(Color.black)
                    }
                    Spacer()
                    
                    Button(action: {
                       // presentationMode.wrappedValue.dismiss()
                    }) {
                        HStack {
                            
                            Text("저장")
                        }
                        .padding(.horizontal,20)
                        .padding()
                        .font(.yjObangBold15)
                        .tint(Color.black)
                    }
                   // Spacer()
                }
                Spacer()
            }
          
            
            
            VStack(spacing: 0) {
                
                
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
                    .frame(height: 603)
                    .overlay(
                        VStack{
                            Text("티켓이 발행된 날짜는 2024.04.08 입니다 이 티켓이 발행된 날짜는 2024 04 08 입니다 이 ")
                                .font(.pretendardMedium8)
                                .foregroundColor(.homeRed)
                            
                            Spacer()
                            
                            Text("여행의 기록을 한줄로 기록하세요 :)")
                                .font(.pretendardMedium14)
                                .foregroundColor(.gray500)
                                .padding(.bottom,30)
                            
                            VStack(spacing:0){
                                HStack(alignment: .center)
                                {
                                    Image("Locationred")
                                    
                                    Text("북촌 한옥마을")
                                        .font(.pretendardMedium14)
                                        .foregroundColor(.homeRed)
                                    
                                }
                                HStack{
                                    Text("서울")
                                        .font(.pretendardExtrabold45)
                                        .foregroundColor(.homeRed)
                                }
                            }
                            
                            
                            Image("airplane")
                                .padding(.bottom,20)
                            
                            VStack(spacing:0){
                                HStack(alignment: .center)
                                {
                                    Image("Locationred")
                                    Text("암스테르담 공항")
                                        .font(.pretendardMedium14)
                                        .foregroundColor(.homeRed)
                                    
                                }
                                HStack{
                                    
                                    Text("암스테르담")
                                        .font(.pretendardExtrabold45)
                                        .foregroundColor(.homeRed)
                                }
                            }
                            
                            Spacer()
                            Image("cut")
                                .padding(.bottom,10)
                            
                            StatsView()
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
            .onAppear {
                homeViewModel.fetchTrips()  // 뷰가 나타날 때 데이터를 로드합니다.
            }
            
            
        }
        .navigationBarBackButtonHidden()
    }
}


struct StatsView: View {
    var body: some View {
        
        HStack(spacing:30) {
            VStack(spacing: 10) {
                Text("여행 카드")
                    .font(.pretendardMedium11)
                    .foregroundColor(.gray500)
                    .multilineTextAlignment(.center)
                Text("27")
                    .font(.pretendardExtrabold14)
                    .foregroundColor(.homeRed)
                    .multilineTextAlignment(.center)
                
                Text("여행 날짜")
                    .font(.pretendardMedium11)
                    .foregroundColor(.gray500)
                    .multilineTextAlignment(.center)
                Text("2024. 03. 05")
                    .font(.pretendardMedium11)
                    .foregroundColor(.homeRed)
                    .multilineTextAlignment(.center)
                Text("2024. 03. 13")
                    .font(.pretendardMedium11)
                    .foregroundColor(.homeRed)
                    .multilineTextAlignment(.center)
            }
            
            VStack(alignment: .leading,spacing: 9) {
                Text("여행 감정")
                    .font(.pretendardMedium11)
                    .foregroundColor(.gray500)
                    //.padding(.vertical,10)
                    .padding(.top,25)
                    .padding(.bottom,5)
                
                HStack{
                    ProgressView(value: 0.6).frame(width: 109,height: 15)
                        .cornerRadius(3)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .tint(.homeRed)
                    
                    Image("netral")
                    
                    Text("60%")
                        .font(.pretendardMedium11)
                        .foregroundColor(.homeRed)
                        .frame(width: 30)
                        
                }
                
                HStack{
                    ProgressView(value: 0.3).frame(width: 109,height: 15)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .tint(.Natural300)
                    Image("sad")
                    Text("30%")
                        .font(.pretendardMedium11)
                        .frame(width: 30)
                      
                }
                
                HStack{
                    ProgressView(value: 0.5).frame(width: 109,height: 15)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .cornerRadius(3)
                        .tint(.Natural300)
                    Image("fun")
                    Text("50%")
                        .font(.pretendardMedium11)
                        .frame(width: 30)
                        
                }
                HStack{
                    ProgressView(value: 0.2).frame(width: 109,height: 15)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .cornerRadius(3)
                        .tint(.Natural300)
                    Image("angry")
                    Text("20%")
                        .font(.pretendardMedium11)
                        .frame(width: 30)
                        
                }
                Spacer().frame(height: 16)
            }
            
        }
        
        
        
        
        
    }
}
