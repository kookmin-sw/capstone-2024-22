//
//  Item.swift
//  moment-iOS
//
//  Created by 양시관 on 3/26/24.
//

import SwiftUI


struct Item: Identifiable,Equatable {
    let id: Int
    let tripName: String
    let startdate: String
    let enddate: String
    var offset: CGFloat = 0
    var isSwiped: Bool = false
    let analyzingCount: Int  // 추가된 필드

    // 생성자는 id를 Int로 받고 이를 String으로 변환하여 저장합니다.
    init(id: Int, tripName: String, startdate: String, enddate: String ,analyzingCount : Int ) {
        self.id = id
        self.tripName = tripName
        self.startdate = startdate
        self.enddate = enddate
        self.analyzingCount = analyzingCount
    }
}
