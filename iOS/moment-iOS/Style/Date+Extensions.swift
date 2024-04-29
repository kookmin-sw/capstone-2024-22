//
//  Date+Extensions.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


extension Date {
  var formattedTime: String { //시간을 스트링형식으로 포맷한다는 말을 의미한다 그게
    let formatter = DateFormatter()//여기서 진행하는거고 들어가서보면 날짜를 정의한다라는 함수가있네
    formatter.locale = Locale(identifier: "ko_KR")// 이거는 우리나라 기준으로 시간을 정의한다고하는거임 즉 오전 오후를 한국어르 바꿔주겠다 이거임
    formatter.dateFormat = "a hh:mm" // 이거는 시간을 이 형식으로 바꿔준다는거고
    return formatter.string(from: self)//위에서 작업한 자기 자신을 호출한다라는 의미임
  }
  
  var formattedDay: String {//이거는 날짜를 스트링형식으로 바꿔서 말해준다라고 하는임
    let now = Date()
    let calendar = Calendar.current // 캘린더 들어가보면 그중에 가장 최근이라는 인스턴스인 current 가 있음 // 즉 현재 캘린더를 사용한다라는 의미를 가짐
    
    let nowStartOfDay = calendar.startOfDay(for: now)
    let dateStartOfDay = calendar.startOfDay(for: self)
    let numOfDaysDifference = calendar.dateComponents([.day], from: nowStartOfDay, to: dateStartOfDay).day! // 이게 다른경우 차이를 계산해주는거임 public func dateComponents(_ components: Set<Calendar.Component>, from start: Date, to end: Date) -> DateComponents 이거가 dateComponents 들어가서본건데 선택과 나중날짜선택의 차이를 계산해주는 로직임

    //날짜의 형식을 계산해서 만들어준다 ,만약에 오늘과 선택한 날짜와 같다면 밑에 분기를 타서 저형식그대로 진행시킨디다.
    if numOfDaysDifference == 0 {
      return "오늘"
    } else {
      let formatter = DateFormatter()
      formatter.locale = Locale(identifier: "ko_KR")
      formatter.dateFormat = "M월 d일 E요일"
      return formatter.string(from: self)
    }
  }
  
    var fomttedVoiceRecorderTime : String {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "ko_KR")
        formatter.dateFormat = "yyyy.M.d"
        return formatter.string(from: self)
        //여기는 보이스레코더 부분에 사용할 현재날짜와 시간에 대한 내용이 들어가있다.
    }
   

}

