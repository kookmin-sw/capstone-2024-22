////
////  TodoListView.swift
////  moment-iOS
////
////  Created by 양시관 on 3/5/24.
////
//
////
////  TodoListView.swift
////  moment-iOS
////
////  Created by 양시관 on 3/5/24.
////
//

import SwiftUI
struct HomeView: View {
    @EnvironmentObject private var pathModel: PathModel
    @StateObject var calendarViewModel = CalendarViewModel()
    @State private var selectedSlideIndex = 0
    
    let items = [
        ("2024-03-13", "2024-03-14", "여행 가기"),
        ("2024-03-13", "2024-03-14", "여행 가기"),
        ("2024-03-13", "2024-03-14", "여행 가기")
    ]
    
    var body: some View {
        NavigationView {
            ZStack {
                Color(.homeBack).edgesIgnoringSafeArea(.all)
                VStack {
                    // '추가' 버튼
                    HStack {
                        Spacer()
                        NavigationLink(destination: SelectDayView(calendarViewModel: calendarViewModel)) {
                            Text("추가")
                                .padding(.horizontal,20)
                                .font(.headline)
                                .tint(.black)
                                .foregroundColor(.black)
                            
                        }
                    }
                    
                    
                    // 슬라이드 가능한 영역
                    CustomHomeVDivider().padding()
                    TabView(selection: $selectedSlideIndex) {
                        
                        Text("어디로 떠나면 좋을까요?")
                            .tag(0)
                        NavigationLink(destination: SelectDayView(calendarViewModel: calendarViewModel)) {
                            Text("여행 계획하기")
                                .frame(maxWidth: .infinity, maxHeight: .infinity) // 탭 영역 전체를 채우도록
                        }
                        .tag(1)
                        .buttonStyle(PlainButtonStyle()) // 버튼의 기본 스타일 제거
                    }
                    .frame(height: 100)
                    .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                    CustomPageIndicator(numberOfPages: 2, currentPage: $selectedSlideIndex)
                    CustomHomeVDivider().padding(.vertical, 17)
                    CustomHomeMainDivider()
                    
                    // 항목 리스트
                    
                   // ScrollView {
                       // ForEach(items.indices, id: \.self) { index in
                    VStack {
                        
                        List{
                            ForEach(items.indices, id: \.self) { index in
                                HStack {
                                    CustomListItem(date1: items[index].0, date2: items[index].1, title: items[index].2)
                                        .background(Color.homeBack)
                                        .swipeActions(edge: .trailing) {
                                            Button("삭제") {
                                                // Handle delete action
                                            }
                                            .tint(.red)
                                            
                                            Button("수정") {
                                                // Handle edit action
                                            }
                                            .tint(.blue)
                                        }
                                }
                                .background(Color.homeBack)
                                .padding()
                            }
                            
                            CustomHomeSubDivider()
                        }.listStyle(PlainListStyle()) // 이것은 리스트의 기본 스타일을 제거합니다.
                            .background(Color.homeBack)
                        //  }
                        //  }
                    }
                }
            }
        }
    }
}



struct CustomListItem: View {
    var date1: String
    var date2: String
    var title: String
    
    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 6) {
                Text(date1)
                    .font(.caption)
                    .foregroundColor(.black)
                Text(date2)
                    .font(.caption)
                    .foregroundColor(.black)
            }
            .padding(.trailing, 5) // 날짜와 빨간선의 간격을 조정
            
            Rectangle()
                .fill(Color.homeRed) // 빨간색으로 설정
                .frame(width: 1, height: 42) // 너비와 높이 설정
                .padding(.leading, 5)
                .padding(.trailing, 0) // 빨간 선과 제목 사이의 간격을 최소화
            
            // 제목
            Spacer().frame(width: 130)
            
            
            Group{
                
                Spacer()
                Text(title)
                    .font(.headline)
                //.padding()
                
                
                Spacer()
                Rectangle()
                    .fill(Color.homeRed) // 빨간색으로 설정
                    .frame(width: 1, height: 42) // 너비와 높이 설정
                    .padding(.trailing, 1) // 여기도 간격을 최소화
                    .padding(.trailing, 1) // 오른쪽 패딩 조정
            }
            
        }
        .padding() // 리스트 항목에 대한 전체 패딩
    }
}




#Preview {
    HomeView()
}



























































































