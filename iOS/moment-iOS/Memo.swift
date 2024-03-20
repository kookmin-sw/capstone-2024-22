//
//  Memo.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


struct Memo : Hashable { // 데이터 메모리관리에 유리하기에 고유값들을 지정하기위한 hashable 선택
    
    var title : String
    var content : String
    var date : Date
    var id = UUID() // UUID를 호출 할때 마다 고유의 id 값을 불러와주는 녀석임 이런걸 쓰는이유는 충돌날 가능성이 극히 낮음
    
    var convertedDate : String{
        String("\(date.formattedDay) - \(date.formattedTime)")
    } // 그냥 코드 블럭이다 값을 return 하는게 아닌 계산값을 내보내주는 녀석임 저번에 했엇디 .
}
