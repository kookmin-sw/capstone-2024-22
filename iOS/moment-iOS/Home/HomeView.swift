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
    
    
    var body: some View {
        NavigationView {
            ZStack {
              
                
                VStack {
                    // '추가' 버튼
                    HStack {
                        Spacer()
                        NavigationLink(destination: SelectDayView(calendarViewModel: calendarViewModel)) {
                            Text("추가")
                                .padding()
                                .font(.headline)
                                .foregroundColor(.black)
                        }
                    }
                    
                    CustomHomeVDivider()
                    TabView(selection: $selectedSlideIndex) {
                        Text("어디로 떠나면 좋을까요?")
                            .tag(0)
                        NavigationLink(destination: SelectDayView(calendarViewModel: calendarViewModel)) {
                            Text("여행 계획하기")
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
                    CustomHomeSubDivider()
                    ScrollView(.vertical, showsIndicators: false) {
                        LazyVStack(spacing: 0) {
                            ForEach(homeviewModel.items) { item in
                                ItemViewCell(item: $homeviewModel.items[homeviewModel.getIndex(item: item)], deleteAction: {
                                    self.itemToDelete = item // 사용자가 삭제할 항목을 설정합니다.
                                    self.showingCustomAlert = true // 삭제 확인 다이얼로그를 표시합니다.
                                })
                                CustomHomeSubDivider()
                            }
                        }
                    }
                    Spacer()
                }  .background(Color.homeBack)
                
                if showingCustomAlert {
                  
                        CustomDialog(
                            isActive: $showingCustomAlert,
                            title: "여행을 삭제하시겠습니까?",
                            message: "해당 파일에 기록된 정보는 모두 사라집니다.",
                            yesAction: {
                                if let item = itemToDelete {
                                    homeviewModel.deleteItem(myItem: item) // 항목 삭제 실행
                                    itemToDelete = nil // 삭제할 아이템 초기화
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
        }
    }
}



struct ItemViewCell: View {
    
    @Binding var item: Item
    var deleteAction: () -> Void
    @State private var isLinkActive = false
    
    var body: some View {
        
        ZStack {
            
            
            deleteButton
            
            NavigationLink(destination: DateRangeView(startDate: item.startdate, endDate: item.enddate), isActive: $isLinkActive) {
                           EmptyView()
                       }
            
            HStack(spacing: 15) {
                
                VStack(alignment: .leading, spacing: 10) {
                    HStack{
                        
                        
                        VStack{
                            Text(item.startdate)
                                .font(.caption)
                                .foregroundColor(.black)
                         
                            
                            Text(item.enddate)
                                .font(.caption)
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
                    HStack(spacing: 15) {
                        
                        Spacer()
                        
                        
                        
                        Text(item.name)
                            .fontWeight(.semibold)
                            .foregroundColor(.black)
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
                    .font(.caption)
                    .foregroundColor(.black)
               
                    .frame(width: 50, height: 50)
                    .background(Color.homeBack) // 즐겨찾기 버튼 색상 설정
                
            }
            Rectangle()
                .fill(Color.homeRed)
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
                    .font(.caption)
                    .foregroundColor(.homeRed)
                    .frame(width: 50, height: 50)
                    .background(Color.homeBack) // 삭제 버튼 색상 설정
                
            }
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
                    .font(.title2)
                    .bold()
                    .padding()
                
                Text(message)
                    .font(.body)
                    .padding(.bottom)
                
                HStack {
                    Button {
                        yesAction()
                        close()
                    } label: {
                        ZStack {
                            RoundedRectangle(cornerRadius: 20)
                                .foregroundColor(.green)
                            Text("네")
                                .font(.system(size: 16, weight: .bold))
                                .foregroundColor(.white)
                                .padding()
                        }
                        .frame(width: 120, height: 44) // 버튼의 크기 조절
                    }
                    .padding()
                    
                    Button {
                        noAction()
                        close()
                    } label: {
                        ZStack {
                            RoundedRectangle(cornerRadius: 20)
                                .foregroundColor(.gray)
                            Text("아니요")
                                .font(.system(size: 16, weight: .bold))
                                .foregroundColor(.white)
                                .padding()
                        }
                        .frame(width: 120, height: 44) // 버튼의 크기 조절
                    }
                    .padding()
                }
                
            }
            .fixedSize(horizontal: false, vertical: true)
            .padding()
            .background(.white)
            .clipShape(RoundedRectangle(cornerRadius: 20))
            
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




import SwiftUI

struct DateRangeView: View {
    var startDate: String
    var endDate: String
    
    var body: some View {
        VStack {
            // 시작 날짜와 종료 날짜 출력
            Text("시작 날짜: \(startDate)")
            Text("끝 날짜: \(endDate)")
            
            // 날짜 차이 계산 및 각 날짜 출력
            if let start = convertToDate(dateString: startDate),
               let end = convertToDate(dateString: endDate) {
                let dayCount = Calendar.current.dateComponents([.day], from: start, to: end).day ?? 0
                
                Text("총 일수: \(dayCount) 일")
                
                ForEach(0..<dayCount, id: \.self) { day in
                    if let date = Calendar.current.date(byAdding: .day, value: day, to: start) {
                        Text("Day \(day + 1): \(date, formatter: yearSpecificFormatter)")
                    }
                }
            } else {
                Text("날짜 형식이 잘못되었습니다.")
            }
        }
    }
}

// YYYY:MM:DD 형식의 문자열을 Date로 변환하는 함수
func convertToDate(dateString: String) -> Date? {
    let dateFormatter = DateFormatter()
    dateFormatter.dateFormat = "YYYY:MM:dd"
    return dateFormatter.date(from: dateString)
}

// 년도를 2024년으로 고정하여 출력하기 위한 DateFormatter 설정
let yearSpecificFormatter: DateFormatter = {
    let formatter = DateFormatter()
    formatter.dateFormat = "2024:MM:dd"
    return formatter
}()

// 예시 사용
struct DateRangeView_Previews: PreviewProvider {
    static var previews: some View {
        DateRangeView(startDate: "2024:01:01", endDate: "2024:01:31")
    }
}
