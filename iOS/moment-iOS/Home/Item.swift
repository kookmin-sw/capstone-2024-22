//
//  Item.swift
//  moment-iOS
//
//  Created by 양시관 on 3/26/24.
//

import SwiftUI

struct Item: Identifiable {
    let id = UUID().uuidString
    let name: String
    let startdate: String
    let enddate : String
    var offset: CGFloat = 0
    var isSwiped: Bool = false
   
   
}
