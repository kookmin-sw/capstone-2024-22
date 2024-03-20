////
////  SelectDayView.swift
////  moment-iOS
////
////  Created by 양시관 on 3/5/24.
////
//
import Foundation
import UIKit
import SwiftUI


import SwiftUI

struct SelectDayView: View {
    @ObservedObject var calendarViewModel: CalendarViewModel
    @State private var selectedTab: Int = 0
    @State private var tripName: String = "" // 여행 이름을 위한 상태
    @EnvironmentObject var homeViewModel: HomeViewModel

    private var dateFormatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .none
        return formatter
    }

    var body: some View {
        ZStack{
            Color(.homeBack).edgesIgnoringSafeArea(.all)
            VStack {
                
                TextField("여행 이름", text: $tripName)
                    .padding()
                    .overlay(
                        Rectangle().frame(height: 1)
                            .padding(.horizontal, 10)
                            .foregroundColor(Color.gray), alignment: .bottom
                    )
                    .padding(.top, 20)
                
                HStack {
                    CustomTabField(selectedTab: $selectedTab, date: calendarViewModel.startTime, dateFormatter: dateFormatter, title: "출발 날짜", tag: 0)
                    CustomTabField(selectedTab: $selectedTab, date: calendarViewModel.endTime, dateFormatter: dateFormatter, title: "도착 날짜", tag: 1)
                }
                .padding(.horizontal)
                
                SwiftUIFSCalendarWrapper(startDate: $calendarViewModel.startTime, endDate: $calendarViewModel.endTime, tab: selectedTab)
                    .frame(height: 300) // 달력의 크기 조정
                    .onChange(of: calendarViewModel.startTime) { newStartDate in
                        calendarViewModel.addSelectedDay(newStartDate ?? Date())
                    }
                    .onChange(of: calendarViewModel.endTime) { newEndDate in
                        calendarViewModel.addSelectedDay(newEndDate ?? Date())
                    }
            }
            .padding()
            .navigationBarTitle(Text("날짜 선택"), displayMode: .inline)
            .navigationBarItems(trailing: Button("완료") {
                // '완료' 버튼 액션
                print("여행 이름: \(tripName)")
                if let startDate = calendarViewModel.startTime, let endDate = calendarViewModel.endTime {
                    print("출발 날짜: \(dateFormatter.string(from: startDate))")
                    print("도착 날짜: \(dateFormatter.string(from: endDate))")
                }
                //homeViewModel.updateTripInfo(name: tripName, startDate: calendarViewModel.startTime, endDate: calendarViewModel.endTime)
            })
        }
    }
}


struct CustomTabField: View {
    @Binding var selectedTab: Int // 어떤 탭이 선택되었는지 나타냅니다.
    var date: Date? // 선택된 날짜
    var dateFormatter: DateFormatter // 날짜 형식 지정
    let title: String // 필드의 기본 텍스트
    let tag: Int // 이 필드의 태그
    
    var body: some View {
        Text(date != nil ? dateFormatter.string(from: date!) : title)
            .padding(.vertical, 10) // 세로 방향으로 패딩 추가
            .frame(maxWidth: .infinity) // 최대 너비 설정
            .background(self.selectedTab == self.tag ? Color.white : Color.clear) // 탭이 선택되면 배경색을 흰색으로 변경
            .foregroundColor(.black) // 글자색을 검정색으로 설정
            .cornerRadius(5) // 배경색이 있을 때 모서리를 약간 둥글게
            .onTapGesture {
                self.selectedTab = self.tag // 탭을 선택했을 때 해당 탭으로 변경
            }
            .overlay(
                Rectangle() // 하단에 선을 추가
                    .frame(height: 1) // 선의 높이 설정
                    .foregroundColor(.black), alignment: .bottom // 선의 색과 위치 설정
            )
    }
}


























