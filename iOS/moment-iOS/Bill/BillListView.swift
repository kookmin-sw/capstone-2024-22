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
    @State private var isDialogActive = false
    @State private var isDialogActiveBillCom = false
    var topColor: Color = .homeRed
    var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
    let item : Item
    @State private var text: String = ""
    @State private var starttrip: String = ""
    @State private var StartLocation : String = ""
    @State private var EndLocation : String = ""
    @State private var isEditing: Bool = false
    @State private var saveButtonTitle = "저장"
    @State private var backButtonTitle = "뒤로"
    
    var body: some View {
        ZStack{
            
            
            VStack {
                
                HStack {
                    Button(action: {
                        
                        if backButtonTitle == "뒤로" {
                            // 내보내기 기능 실행
                            isDialogActive = true
                            print("두ㅏㅣ로가기")
                        }else {
                            //내보내기 기능 추가
                            print("내보내기")
                        }
                    }) {
                        HStack {
                            Text(backButtonTitle)
                        }
                        .padding(.horizontal, 20)
                        .padding()
                        .font(.yjObangBold15)
                        .tint(Color.black)
                    }
                    Spacer()
                    
                    Button(action: {
                        if saveButtonTitle == "저장" {
                            saveButtonTitle = "완료"
                            backButtonTitle = "내보내기"
                            isDialogActiveBillCom = true
                        } else {
                            // 완료 버튼의 기능
                            print("완료")
                        }
                    }) {
                        HStack {
                            Text(saveButtonTitle)
                        }
                        .padding(.horizontal, 20)
                        .padding()
                        .font(.yjObangBold15)
                        .foregroundColor(saveButtonTitle == "완료" ? .homeRed : .black)
                    }
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
                        VStack(alignment:.center){
                            Text("티켓이 발행된 날짜는 2024.04.08 입니다 이 티켓이 발행된 날짜는 2024 04 08 입니다 이 ")
                                .font(.pretendardMedium8)
                                .foregroundColor(.homeRed)
                            
                            Spacer()
                            
                            TextField("여행의 기록을 한줄로 기록하세요", text: $text, prompt: Text("여행의 기록을 한줄로 기록하세요").foregroundColor(.Natural200))
                                .foregroundColor(.gray500)
                                .font(.pretendardMedium14)
                                .padding(.bottom,30)
                                .multilineTextAlignment(.center)
                            
                            
                            
                            
                            
                            
                            
                            VStack(alignment:.center,spacing:0){
                                HStack(alignment:.center,spacing:1)
                                {
                                    Spacer()
                                    
                                    // Image("Locationred")
                                    
                                    
                                    TextField("여행의 기록을 한줄로 기록하세요", text: $starttrip, prompt: Text("여행의 시작은 여기부터").foregroundColor(.Natural200))
                                        .font(.pretendardMedium14)
                                        .foregroundColor(.homeRed)  // 글씨 색상 변경
                                        .multilineTextAlignment(.center)
                                        .frame(maxWidth:100)
                                    
                                    
                                    Spacer()
                                }
                                
                                
                                
                                
                                
                                
                                
                                
                                HStack{
                                    TextField("출발지",text:$StartLocation,prompt: Text("출발지").foregroundColor(.Natural200))
                                        .font(.pretendardExtrabold45)
                                        .foregroundColor(.homeRed)  // 글씨
                                        .multilineTextAlignment(.center)
                                }
                            }
                            
                            
                            Image("airplane")
                                .padding(.bottom,20)
                            
                            VStack(spacing:0){
                                HStack(alignment: .center,spacing:0)
                                {
                                    Image("Locationred")
                                    Text("암스테르담 공항")
                                        .font(.pretendardMedium14)
                                        .foregroundColor(.homeRed)
                                        .multilineTextAlignment(.leading)
                                    
                                }
                                
                                
                                
                                HStack{
                                    
                                    TextField("출발지",text:$EndLocation,prompt: Text("도착지").foregroundColor(.Natural200))
                                        .font(.pretendardExtrabold45)
                                        .foregroundColor(.homeRed)  // 글씨
                                        .multilineTextAlignment(.center)
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
            if isDialogActive {
                CustomDialogBill(isActive: $isDialogActive, title: "", message: "앗! 지금 화면을 그냥 나가면 열심히 만든 영수증이 저장되지않아요", yesAction: {
                    //TODO: - 영수증의 시작뷰로 돌아가야함
                    print("저장됨")
                    // 여기에 저장 로직 추가
                }, noAction: {
                    //TODO: - dismiss하기
                    
                    print("취소됨")
                })
            }
            
            if isDialogActiveBillCom{
                
                CustomDialogBillComplete(isActive: $isDialogActiveBillCom, title: "축하해요", message: "첫번째 영수증이 만들어졌어요!\n 나도이제 여행자", yesAction: {
                    //TODO: - 영수증의 시작뷰로 돌아가야함
                    print("저장됨")
                    // 여기에 저장 로직 추가
                }, noAction: {
                    //TODO: - dismiss하기
                    
                    print("취소됨")
                })
            }
            
            VStack{
                //TODO: - 여기에다가 인디케이터를 넣어줘야함
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


struct CustomDialogBill: View {
    @Binding var isActive: Bool
    
    let title: String
    let message: String
    let yesAction: () -> Void
    let noAction: () -> Void
    @State private var showingCustomAlert = false
    
    @State private var offset: CGFloat = 1000
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        
        ZStack{
            Color(showingCustomAlert ? .black : .black)
                .opacity(showingCustomAlert ? 1.0 : 0.5)
                .edgesIgnoringSafeArea(.all)
                .animation(.easeInOut, value: showingCustomAlert)
            
            VStack {
                
                
                Text(message)
                
                    .font(.pretendardMedium14)
                    .multilineTextAlignment(.center)
                    .foregroundColor(.gray500)
                    .padding(.bottom)
                
                HStack { // 버튼 사이 간격을 0으로 조정
                    Button {
                        yesAction()
                        close()
                        presentationMode.wrappedValue.dismiss()
                    } label: {
                        ZStack {
                            
                            Text("나갈게요")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 36) // 버튼의 크기 조절
                    }
                    
                    Rectangle() // 빨간색 세로줄 추가
                        .fill(Color.gray500)
                        .frame(width: 2, height: 20)
                    
                    Button {
                        noAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("돌아갈게요")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 36) // 버튼의 크기 조절
                    }
                }.padding(.bottom,10)
                
            }
            
            .frame(width: 280, height: 144) // 다이얼로그의 크기 조절
            
            .background(.homeBack)
            .clipShape(RoundedRectangle(cornerRadius: 0))
        }
        
        // .ignoresSafeArea()
    }
    
    func close() {
        withAnimation(.spring()) {
            offset = 1000
            isActive = false
        }
    }
}



struct CustomDialogBillComplete: View {
    @Binding var isActive: Bool
    
    let title: String
    let message: String
    let yesAction: () -> Void
    let noAction: () -> Void
    @State private var showingCustomAlert = false
    
    @State private var offset: CGFloat = 0
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        
        ZStack{
            
            Color(showingCustomAlert ? .black : .black)
                .opacity(showingCustomAlert ? 1.0 : 0.5)
            
            
                .onTapGesture {
                    close() // 배경 탭 시 다이얼로그 닫기
                }
                .animation(.easeInOut, value: showingCustomAlert)
                .edgesIgnoringSafeArea(.all)
            
            VStack {
                Text(title)
                    .font(.pretendardExtrabold16)
                    .multilineTextAlignment(.center)
                    .foregroundColor(.homeRed)
                    .padding(.bottom)
                
                Text(message)
                
                    .font(.pretendardMedium14)
                    .multilineTextAlignment(.center)
                    .foregroundColor(.gray500)
                    .padding(.bottom)
                
                
                
            }
            
            .frame(width: 280, height: 144) // 다이얼로그의 크기 조절
            
            .background(.homeBack)
            .clipShape(RoundedRectangle(cornerRadius: 0))
            //            .onAppear {
            //                            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            //                                self.close()  // 1초 후에 다이얼로그 자동 닫기
            //                            }
            //                        }
        }
        
        // .ignoresSafeArea()
    }
    
    
    
    func close() {
        withAnimation(.spring()) {
            offset = 1000  // 다이얼로그가 아래로 이동하며 사라짐
            isActive = false  // 다이얼로그 비활성화
        }
    }
}
