////
////  TodoListView.swift
////  moment-iOS
////
////  Created by 양시관 on 3/5/24.
////
//


import SwiftUI
import Foundation
import SwiftUI

struct HomeView: View {
    @EnvironmentObject private var pathModel: PathModel
    @StateObject var calendarViewModel = CalendarViewModel()
    @State private var selectedSlideIndex = 0
    @StateObject var homeviewModel = HomeViewModel()
    //@State private var showingCustomAlert = false
    @Binding var showingCustomAlert: Bool
    @State private var itemToDelete: Item? // 삭제할 아이템을 저장하기 위한 상태변수
    @State private var selectedItemName: String?
    @ObservedObject var audioRecorderManager: AudioRecorderManager
 
    @ObservedObject var cardViewModel : CardViewModel
    @State private var showSlideOverCard = false
    
    var body: some View {
       // NavigationView {
            ZStack {
                
                
                VStack {
                    Spacer().frame(height: 50)
                    // '추가' 버튼
                    HStack {
                        Spacer()
                        NavigationLink(destination: SelectDayView(calendarViewModel: calendarViewModel)) {
                            Text("추가")
                                .padding(.horizontal,30)
                                .padding(.bottom,15)
                                .font(.yjObangBold15)
                                .foregroundColor(.black)
                        }
                    }
                    
                    CustomHomeVDivider()
                    TabView(selection: $selectedSlideIndex) {
                        Text("어디로 떠나면 좋을까요?")
                            .font(.pretendardMedium14)
                            .tag(0)
                        NavigationLink(destination: DailyView()) {
                            Text("일상기록")
                                .font(.pretendardMedium14)
                                .frame(maxWidth: .infinity, maxHeight: .infinity)
                        }
                        .tag(1)
                        .buttonStyle(PlainButtonStyle())
                    }
                    .frame(height: 100)
                    .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                    CustomPageIndicator(numberOfPages: 2, currentPage: $selectedSlideIndex)
                    CustomHomeVDivider().padding()
                    CustomHomeMainDividerthick()
                        .padding()
                    // 항목 리스트
                   // CustomHomeSubDivider()
                    ScrollView(.vertical, showsIndicators: false) {
                        LazyVStack(spacing: 5) {
                            ForEach(homeviewModel.items) { item in
                                ItemViewCell(item: $homeviewModel.items[homeviewModel.getIndex(item: item)], deleteAction: {
                                    self.itemToDelete = item // 사용자가 삭제할 항목을 설정합니다.
                                    self.selectedItemName = item.name
                                    self.showingCustomAlert = true // 삭제 확인 다이얼로그를 표시합니다.
                                }, audioRecorderManager: audioRecorderManager, cardViewModel: cardViewModel)
                                CustomHomeSubDivider()
                            }
                        }
                    }
                    Spacer()
                }  .background(Color.homeBack)
                
                if showingCustomAlert {
                    
                    CustomDialog(
                        isActive: $showingCustomAlert,
                        title: "\(selectedItemName ?? "이 여행")\n 정말 삭제하시겠습니까?",
                        message: "해당 파일에 기록되어있는 녹음카드는\n '일상 기록'으로 이동합니다",
                        yesAction: {
                            if let item = itemToDelete {
                                homeviewModel.deleteItem(myItem: item) // 항목 삭제 실행
                                itemToDelete = nil // 삭제할 아이템 초기화
                                selectedItemName = nil // 선택한 아이템 이름 초기화
                            }
                            showingCustomAlert = false
                        },
                        noAction: {
                            showingCustomAlert = false // 다이얼로그 닫기
                            itemToDelete = nil // 삭제할 아이템 초기화
                        }
                    )
                    .transition(.opacity) // 다이얼로그 등장과 사라짐에 투명도 변화 적용
                    .zIndex(1) // 다이얼로그가 다른 요소들 위에 오도록 설정
                }
            }
       // }
    }
}



struct ItemViewCell: View {
    
    @Binding var item: Item
    var deleteAction: () -> Void
    @State private var isLinkActive = false
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    @ObservedObject var cardViewModel : CardViewModel
    var body: some View {
        
        ZStack {

            if item.offset != 0 {
                Rectangle()
                    .fill(Color.gray2.opacity(0.3)) // 여기서 색상과 투명도를 조정합니다.
                    .frame(width: 183, height: 80) // 여기서 사각형의 크기를 조정합니다.
                    .cornerRadius(3) // 필요한 경우 모서리를 둥글게 처리합니다.
                    .offset(x: item.offset + 86) // 슬라이드된 위치에 따라 사각형의 위치를 조정합니다.
                    .zIndex(1)
            }
            
            deleteButton
            
            NavigationLink(destination:  DateRangeView1(item: item, audioRecorderManager: audioRecorderManager, cardViewModel: cardViewModel), isActive: $isLinkActive) {
                EmptyView()
            }
        
            
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
                }.padding(.bottom,10)
                
               
                  
                    VStack{
                        HStack(spacing: 10) {
                            
                            Spacer()
                            
                            
                           
                                Text(item.name)
                                    .font(.pretendardExtrabold14)
                                    .foregroundColor(.black)
                                    .zIndex(2)
                            
                            Rectangle()
                                .fill(Color.homeRed)
                                .frame(width: 1, height: 42)
                                .padding(.leading, 3)
                                .padding(.trailing, 0)
                            
                        }
                    }
                
                
                
                
            }
            .onTapGesture {
                self.isLinkActive = true // 사용자가 셀을 탭하면 네비게이션 링크 활성화
            }
            .padding()
            .background(Color.homeBack)
            //.background(item.offset != 0 ? Color.gray.opacity(0.5) : Color.homeBack)
            .contentShape(Rectangle())
            
            .offset(x: item.offset)
            .gesture(DragGesture().onChanged(onChanged(value:)).onEnded(onEnd(value:)))
        }
    }
    
    
    
    
    func onChanged(value: DragGesture.Value) {
        if value.translation.width < 0 {
            
            if item.isSwiped {
                item.offset = value.translation.width - 90
            } else {
                item.offset = value.translation.width
            }
        }
    }
    
    func onEnd(value: DragGesture.Value) {
        withAnimation(.easeInOut) {
            if value.translation.width < 0 {
                
                
                if -item.offset > 50 {
                    item.isSwiped = true
                    item.offset = -180
                } else {
                    item.isSwiped = false
                    item.offset = 0
                }
            } else {
                item.isSwiped = false
                item.offset = 0
            }
            
        }
    }
}

extension ItemViewCell {
    
    var deleteButton: some View {
        HStack {
            Spacer()
            
            // 새로운 버튼을 추가합니다.
            Button {
                // 여기에 '즐겨찾기' 버튼을 눌렀을 때 수행할 동작을 추가합니다.
                print("즐겨찾기 버튼이 눌렸습니다.")
                withAnimation {
                    self.item.offset = 0
                }
            } label: {
                Text("수정")
                    .font(.yjObangBold12)
                    .foregroundColor(.black)
                
                    .frame(width: 50, height: 50)
                    .background(Color.homeBack) // 즐겨찾기 버튼 색상 설정
                
            }
            Rectangle()
                .fill(Color.gray2)
                .frame(width: 1, height: 42)
                .padding(.leading, 5)
                .padding(.trailing, 0)
            
            Button {
                // 여기에 '삭제' 버튼을 눌렀을 때 수행할 동작을 추가합니다.
                //deleteDidTapClosure(item)
                self.deleteAction()
                withAnimation {
                    self.item.offset = 0
                }
            } label: {
                Text("삭제")
                    .font(.yjObangBold12)
                    .foregroundColor(.homeRed)
                    .frame(width: 50, height: 50)
                    .background(Color.homeBack) // 삭제 버튼 색상 설정
                
            }
            Spacer().frame(width: 50)
        }.background(Color.homeBack)
    }
}
//TODO: - 버튼 두개를 패딩으로 좀 더 밀어주고 스와이프 부분을 좀더 조정해야할듯


struct CustomDialog: View {
    @Binding var isActive: Bool
    
    let title: String
    let message: String
    let yesAction: () -> Void
    let noAction: () -> Void
    @State private var showingCustomAlert = false
    
    @State private var offset: CGFloat = 1000
    
    var body: some View {
        
        ZStack{
            Color(showingCustomAlert ? .black : .black)
                .opacity(showingCustomAlert ? 1.0 : 0.5)
                .edgesIgnoringSafeArea(.all)
                .animation(.easeInOut, value: showingCustomAlert)
            
            VStack {
                Text(title)
                    .font(.pretendardExtrabold16)
                    .multilineTextAlignment(.center)
                    .padding()
                
                Text(message)
                    
                    .font(.pretendardMedium14)
                    .multilineTextAlignment(.center) 
                    .foregroundColor(.gray500)
                    .padding(.bottom)
                
                HStack { // 버튼 사이 간격을 0으로 조정
                    Button {
                        yesAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("네")
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
                            
                            Text("아니요")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 36) // 버튼의 크기 조절
                    }
                }.padding(.bottom,10)
                
            }
            
            .frame(width: 280, height: 196) // 다이얼로그의 크기 조절
            
            .background(.white)
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



struct DateRangeView1: View {
    var item: Item
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    @ObservedObject var cardViewModel : CardViewModel
    @Environment(\.presentationMode) var presentationMode
    var body: some View {
        ZStack{
        VStack {
            Button(action: {
                // "뒤로" 버튼의 액션: 현재 뷰를 종료
                self.presentationMode.wrappedValue.dismiss()
            }) {
                HStack {
                    Spacer().frame(width: 20)
                    Text("뒤로")
                        .padding()
                        .font(.yjObangBold15)
                        .tint(Color.black)
                    Spacer()
                }
            }
            
            CustomHomeMainDividerthick()
                .padding(.bottom, -10)
            HStack {
                Spacer() // 좌측 공간을 만들어줌으로써 Text를 우측으로 밀어냄
                Text(item.name)
                    .font(.pretendardBold22)
                    .padding(.horizontal,10)
                   
                Spacer().frame(width: 20) // 우측에 조금 더 공간을 추가하여 Text를 중앙으로 조금 이동시킴
            }
            
            CustomHomeMainDividerthick()
                .padding(.top, -7)
            
            ScrollView{
                VStack {
                    
                    if let startDate = convertToDate(dateString: item.startdate),
                       let endDate = convertToDate(dateString: item.enddate) {
                        let days = generateDateRange(from: startDate, to: endDate)
                        
                        ForEach(Array(days.enumerated()), id: \.element) { index, day in
                            NavigationLink(destination: CardView(day: day, item: item, audioRecorderManager: audioRecorderManager, cardViewModel: cardViewModel)) {
                                DayView(day: day, dayIndex: index, item: item) // 'dayIndex' 인자를 전달합니다.
                            }
                        }
                        .padding(.vertical, 4)
                        
                        
                        
                    }
                    
                }
            }
        }
    }.background(Color.homeBack)
            .navigationBarBackButtonHidden(true)
    
    
            }
        
    
    
    // 날짜 문자열을 Date로 변환
    func convertToDate(dateString: String) -> Date? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy. MM. dd"
        return dateFormatter.date(from: dateString)
    }
    
    // 시작 날짜부터 종료 날짜까지의 Date 배열 생성
    func generateDateRange(from startDate: Date, to endDate: Date) -> [Date] {
        var dates: [Date] = []
        var date = startDate
        
        while date <= endDate {
            dates.append(date)
            date = Calendar.current.date(byAdding: .day, value: 1, to: date)!
        }
        
        return dates
    }
    
}

// 날짜 형식 지정
let monthDayFormatter: DateFormatter = {
    let formatter = DateFormatter()
    formatter.dateFormat = "yyyy. MM. dd"
    return formatter
}()


struct DayView: View {
    var day: Date
    var dayIndex: Int
    var item: Item
    
    var body: some View {
        
            VStack {
                
                
                HStack{
                    VStack{
                        Text("\(dayIndex + 1) 일차")
                            .font(.pretendardExtrabold14)
                            .foregroundColor(.black)
                            .padding(.bottom, 5)
                        
                        Text("\(day, formatter: monthDayFormatter)")
                            .font(.pretendardMedium11)
                            .foregroundColor(.black)
                            .padding(.bottom, 5)
                    }
                    
                    
                    Rectangle()
                        .fill(Color.homeRed)
                        .frame(width: 1, height: 42)
                        .padding(.leading, 3)
                        .padding(.trailing, 0)
                    
                    
                    Text("몇개의 파일이 있어요.")
                        .font(.pretendardMedium11)
                        .foregroundColor(.gray600)
                        
                    
                    Spacer()
                }
                .padding()
                CustomHomeSubDivider()
                    .padding(.vertical, 4)
            }
        
    }
}
