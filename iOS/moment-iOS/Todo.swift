//
//  Todo.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation

import Foundation


struct Todo : Hashable { // 해시어블프로토콜을 한 이유에 대해서는 저번에도 말을 하긴했는데 혼자하라고하면 좀 애매할거같긴하다 이런 데이터형식들을 테이블 셀형태로 잘 만들어야해서 이걸 선택하는거긴함
    
    var title : String
    var time : Date
    var day : Date // 이거 Date 타입 들어가보면 해시어블을 따르네 Date 타입을 지원을하나보네
    var selected : Bool
    
    var convertedDayAndTime : String
    {
        String("\(day.formattedDay) - \(time.formattedTime) 에 알림")
    }
    //일단 반환을 하기위한 String 타입이며 날짜와 시간을 나눠서 사람이 알기쉬운형태로 보여주랴고 이렇게 return 을 하는게아닌 값이바뀔시 바로 그려주려고 이렇게 만듬 즉 저장을 안하는 개념임
}
