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
import Foundation
import SwiftUI
struct Item: Identifiable {
    let id: UUID
    let date1: String
    let date2: String
    let title: String

    init(date1: String, date2: String, title: String) {
        self.id = UUID() // 고유 식별자 생성
        self.date1 = date1
        self.date2 = date2
        self.title = title
    }
}

struct HomeView: View {
    let items: [Item] = [
        Item(date1: "2024-03-13", date2: "2024-03-14", title: "여행 가기"),
        Item(date1: "2024-03-15", date2: "2024-03-16", title: "출장 가기"),
        Item(date1: "2024-03-20", date2: "2024-03-22", title: "캠핑 가기")
    ]

    var body: some View {
        ScrollView {
            VStack {
                ForEach(items) { item in // 여기서 UUID를 사용
                    SwipeItem(
                        content: {
                            CustomListItem(
                                date1: item.date1,
                                date2: item.date2,
                                title: item.title
                            )
                        },
                        left: { Text("Edit") },
                        right: { Text("Delete") },
                        itemHeight: 60
                    )
                }
            }
        }
    }
}


//
//struct HomeView: View {
//    @EnvironmentObject private var pathModel: PathModel
//    @StateObject var calendarViewModel = CalendarViewModel()
//    @State private var selectedSlideIndex = 0
//
//    let items = [
//        ("2024-03-13", "2024-03-14", "여행 가기"),
//        ("2024-03-15", "2024-03-16", "출장 가기"),
//        ("2024-03-20", "2024-03-22", "캠핑 가기")
//    ]
//
//    var body: some View {
//        NavigationView {
//            ZStack {
//                Color.homeBack.edgesIgnoringSafeArea(.all) // 전체 배경색 설정
//                VStack {
//                    // '추가' 버튼
//                    HStack {
//                        Spacer()
//                        NavigationLink(destination: SelectDayView(calendarViewModel: calendarViewModel)) {
//                            Text("추가")
//                                .padding()
//                                .font(.headline)
//                                .foregroundColor(.black)
//                        }
//                    }
//                    .padding()
//
//                    // 슬라이드 가능한 영역
//                    CustomHomeVDivider().padding()
//                    TabView(selection: $selectedSlideIndex) {
//                        Text("어디로 떠나면 좋을까요?")
//                            .tag(0)
//                        NavigationLink(destination: SelectDayView(calendarViewModel: calendarViewModel)) {
//                            Text("여행 계획하기")
//                                .frame(maxWidth: .infinity, maxHeight: .infinity)
//                        }
//                        .tag(1)
//                        .buttonStyle(PlainButtonStyle())
//                    }
//                    .frame(height: 100)
//                    .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
//                    CustomPageIndicator(numberOfPages: 2, currentPage: $selectedSlideIndex)
//                    CustomHomeVDivider().padding()
//
//                    // 항목 리스트
//                    ScrollView {
//                        ForEach(items.indices, id: \.self) { index in
//                            SwipeItem(content: {
//                                CustomListItem(date1: items[index].0, date2: items[index].1, title: items[index].2)
//                                    .frame(height: 500)
//                                    .background(Color.black) // 각 항목의 배경색 설정
//                            },
//                            left: {
//                                Button(action: {
//                                    // 수정 버튼 액션
//                                    print("수정 버튼 탭")
//                                }) {
//                                    HStack {
//                                        Image(systemName: "pencil.circle")
//                                            .foregroundColor(.white)
//                                            .font(.largeTitle)
//                                        Text("수정")
//                                            .foregroundColor(.white)
//                                            .font(.headline)
//                                    }
//                                    .frame(maxWidth: .infinity, maxHeight: .infinity)
//                                    .background(Color.blue)
//                                }
//                            },
//                            right: {
//                                Button(action: {
//                                    // 삭제 버튼 액션
//                                    print("삭제 버튼 탭")
//                                }) {
//                                    HStack {
//                                        Text("삭제")
//                                            .foregroundColor(.white)
//                                            .font(.headline)
//                                        Image(systemName: "trash.circle")
//                                            .foregroundColor(.white)
//                                            .font(.largeTitle)
//                                    }
//                                    .frame(maxWidth: .infinity, maxHeight: .infinity)
//                                    .background(Color.red)
//                                }
//                            }, itemHeight: 60)
//                            .padding(.horizontal)
//                            .padding(.vertical, 4)
//                        }
//                    }
//                    .background(Color.homeBack) // 스크롤 뷰의 배경색 설정
//                }
//            }
//        }
//    }
//}





//
//struct HomeView: View {
//    @EnvironmentObject private var pathModel: PathModel
//    @StateObject var calendarViewModel = CalendarViewModel()
//    @State private var selectedSlideIndex = 0
//
//    let items = [
//        ("2024-03-13", "2024-03-14", "여행 가기"),
//        ("2024-03-15", "2024-03-16", "출장 가기"),
//        ("2024-03-20", "2024-03-22", "캠핑 가기")
//    ]
//
//    var body: some View {
//        NavigationView {
//            ZStack {
//                Color.homeBack.edgesIgnoringSafeArea(.all) // 전체 배경색 설정
//                VStack {
//                    // '추가' 버튼
//                    HStack {
//                        Spacer()
//                        NavigationLink(destination: SelectDayView(calendarViewModel: calendarViewModel)) {
//                            Text("추가")
//                                .padding()
//                                .font(.headline)
//                                .foregroundColor(.black)
//                        }
//                    }
//                    .padding()
//
//                    // 슬라이드 가능한 영역
//                    CustomHomeVDivider().padding()
//                    TabView(selection: $selectedSlideIndex) {
//                        Text("어디로 떠나면 좋을까요?")
//                            .tag(0)
//                        NavigationLink(destination: SelectDayView(calendarViewModel: calendarViewModel)) {
//                            Text("여행 계획하기")
//                                .frame(maxWidth: .infinity, maxHeight: .infinity)
//                        }
//                        .tag(1)
//                        .buttonStyle(PlainButtonStyle())
//                    }
//                    .frame(height: 100)
//                    .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
//                    CustomPageIndicator(numberOfPages: 2, currentPage: $selectedSlideIndex)
//                    CustomHomeVDivider().padding()
//
//                    // 항목 리스트
//
//
//
//                        ForEach(items.indices, id: \.self) { index in
//                            HStack {
////                                CustomCardView(item: (date1: "", date2: "", title: ""))
//
//                            }
//                            .padding()
//                        }
//
//                    .background(Color.homeBack) // 리스트의 배경색 설정
//                }
//            }
//           // .navigationBarTitle("홈", displayMode: .inline)
//        }
//    }
//}
//

struct CustomListItem: View {
    var date1: String
    var date2: String
    var title: String
    
    var body: some View {
        VStack{
            
            HStack {
                VStack(alignment: .leading, spacing: 6) {
                    Text(date1)
                        .font(.caption)
                        .foregroundColor(.black)
                    Text(date2)
                        .font(.caption)
                        .foregroundColor(.black)
                }
                .padding(.trailing, 5)
                
                Rectangle()
                    .fill(Color.homeRed)
                    .frame(width: 1, height: 42)
                    .padding(.leading, 5)
                    .padding(.trailing, 0)
                
                Spacer().frame(width: 130)
                
                Text(title)
                    .font(.headline)
                
                Rectangle()
                    .fill(Color.homeRed)
                    .frame(width: 1, height: 42)
                    .padding(.trailing, 1)
            }
            
            .background(Color.homeBack) // 이렇게 배경색을 설정할 수 있습니다.
            
        }
    }
}


#Preview {
    HomeView()
}



























































































