//
//  DailyViewmodel.swift
//  moment-iOS
//
//  Created by 양시관 on 4/8/24.
//

import Foundation
import Foundation
import SwiftUI


struct DailyItem: Identifiable {
    
    var id : Int
    var name: String
    var date: String // 예시로 날짜만 사용
}


class DailyItemViewModel: ObservableObject {
    @Published var dailyItems: [DailyItem] = [
        DailyItem(id: 1, name: "3개의 파일이 있어요", date: "2024.01.01"),
        DailyItem(id: 2, name: "1개의 파일이 있어요", date: "2024.01.01"),
        DailyItem(id: 3, name: "4개의 파일이 있어요", date: "2024.01.01"),
        DailyItem(id: 4, name: "2개의 파일이 있어요", date: "2024.01.01")
    ]
}
