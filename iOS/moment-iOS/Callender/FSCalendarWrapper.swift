//
//  FSCalendarWrapper.swift
//  moment-iOS
//
//  Created by 양시관 on 3/16/24.
//
import SwiftUI
import FSCalendar
import SwiftUI
import FSCalendar

// FSCalendar를 SwiftUI에서 사용하기 위한 래퍼 뷰
struct SwiftUIFSCalendarWrapper: UIViewRepresentable {
    @Binding var startDate: Date?
    @Binding var endDate: Date?
    var tab: Int // 현재 선택된 탭을 나타냅니다. 0: 출발 날짜, 1: 도착 날짜
    
    
    func updateUIView(_ uiView: FSCalendar, context: Context) {
        if let startDate = startDate {
            uiView.select(startDate)
        }
        if let endDate = endDate, tab == 1 { // 도착 날짜 탭일 때만 선택 반영
            uiView.select(endDate)
        }
     
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    func makeUIView(context: Context) -> FSCalendar {
        let calendar = FSCalendar()
        calendar.delegate = context.coordinator
        calendar.backgroundColor = .white
        calendar.appearance.todayColor = .white
        calendar.appearance.selectionColor = .homeRed1
        calendar.appearance.borderRadius = 0.3 // 선택된 날짜의 모서리를 약간 둥글게 설정

        return calendar
    }
   
    
    
    
    class Coordinator: NSObject, FSCalendarDelegate {
        var wrapper: SwiftUIFSCalendarWrapper
        
        init(_ wrapper: SwiftUIFSCalendarWrapper) {
            self.wrapper = wrapper
        }
        
        func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
            if wrapper.tab == 0 { // 출발 날짜 선택
                wrapper.startDate = date
                if let endDate = wrapper.endDate, date > endDate {
                    wrapper.endDate = nil // 시작 날짜가 종료 날짜보다 뒤면 종료 날짜 초기화
                }
            } else { // 도착 날짜 선택
                wrapper.endDate = date
                if let startDate = wrapper.startDate, date < startDate {
                    wrapper.startDate = nil // 도착 날짜가 출발 날짜보다 앞이면 출발 날짜 초기화
                }
            }
        }

    }
}
