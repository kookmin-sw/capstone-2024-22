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

struct HomeView: View {
    @EnvironmentObject private var pathModel: PathModel
    @StateObject var calendarViewModel = CalendarViewModel()
    @State private var selectedSlideIndex = 0
    @StateObject var homeviewModel = HomeViewModel()
    
    
    
    var body: some View {
        NavigationView {
            ZStack {
                Color.homeBack.edgesIgnoringSafeArea(.all) // 전체 배경색 설정
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
                    ScrollView(.vertical, showsIndicators: false) {
                        LazyVStack(spacing: 0) {
                            //TODO: - devider 넣으면?
                            CustomHomeSubDivider()
                            ForEach(homeviewModel.items) { item in
                                ItemViewCell(item: $homeviewModel.items[homeviewModel.getIndex(item: item)], deleteDidTapClosure: {
                                    homeviewModel.deleteItem(myItem: $0)
                                    
                                })
                                CustomHomeSubDivider()
                            }
                            CustomHomeSubDivider()
                        }
                    }
                    Spacer()
                }
                
                
                
            }
            
        }
    }
}



struct ItemViewCell: View {
    
    @Binding var item: Item
    var deleteDidTapClosure: (Item) -> ()
    
    var body: some View {
        
        ZStack {
            
            
            deleteButton
            
            
            HStack(spacing: 15) {
                
                
                HStack{
                    VStack(alignment: .leading, spacing: 10) {
                        
                        Text("\(item.enddate)")
                            .font(.caption)
                            .foregroundColor(.black)
                        
                        
                        Text(item.startdate)
                            .font(.caption)
                            .foregroundColor(.black)
                    }
                    Rectangle()
                        .fill(Color.homeRed)
                        .frame(width: 1, height: 42)
                        .padding(.leading, 5)
                        .padding(.trailing, 0)
                }
                
                VStack{
                    HStack(spacing: 15) {
                        
                        Spacer()
                        
                        
                        
                        Text(item.name)
                            .fontWeight(.semibold)
                            .foregroundColor(.black)
                        Rectangle()
                            .fill(Color.homeRed)
                            .frame(width: 1, height: 42)
                            .padding(.leading, 5)
                            .padding(.trailing, 0)
                        
                    }
                }
                    
                

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
            } label: {
                Text("수정")
                    .font(.caption)
                    .foregroundColor(.black)
                //.frame(width: geometry.size.width / 2, height: geometry.size.height)
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
                deleteDidTapClosure(item)
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




#Preview {
    HomeView()
}



























































































