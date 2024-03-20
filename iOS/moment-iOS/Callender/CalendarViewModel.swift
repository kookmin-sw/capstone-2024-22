//
//  TodoViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation



import Foundation

class CalendarViewModel : ObservableObject {
    @Published var title : String
    @Published var time : Date
    @Published var isDisplayCalendar : Bool
    @Published var day : Date
    @Published var startTime: Date?
    @Published var endTime: Date?
    @Published var selectedDays: [Date] = []
    // ui의 상태를 최신화해주는 published를 사용해준다.
    
    init(title: String = "",
         time: Date = Date(),
         isDisplayCalendar: Bool = false,
         day : Date = Date(),
         startTime: Date? = nil,
         endTime: Date? = nil
    )
    {
        self.title = title
        self.time = time
        self.isDisplayCalendar = isDisplayCalendar
        self.day = day
        self.startTime = startTime
        self.endTime = endTime
        
    }
    func addSelectedDay(_ day: Date) {
        // 이미 두 날짜가 선택되었다면 초기화
        if selectedDays.count >= 2 {
            selectedDays.removeAll()
        }
        // 새 날짜 추가
        selectedDays.append(day)

        // 두 날짜가 모두 선택되었다면 시작과 종료 날짜 설정
        if selectedDays.count == 2 {
            selectedDays.sort() // 시작 날짜가 종료 날짜보다 먼저 오도록 정렬
            startTime = selectedDays.first
            endTime = selectedDays.last
        }
    }
}



extension CalendarViewModel {
    func setIsDisplayCalendar(
        _ isDispaly : Bool
    ){
        isDisplayCalendar = isDispaly // isDisplay의 값을 받아서 isDisplayCalendar 속성에 할당할거임 이제 이런게 동작을 캡슐화했다고 봐도 맞는거지
    }
}
