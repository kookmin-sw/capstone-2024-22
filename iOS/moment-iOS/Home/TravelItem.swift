//
//  TravelItem.swift
//  moment-iOS
//
//  Created by 양시관 on 3/21/24.
//

import Foundation

struct TravelItem: Identifiable {
    var id = UUID()
    var startDate: String
    var endDate: String
    var title: String
}
