//
//  CardViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/31/24.
//

import Foundation
import SwiftUI

struct CardItem1: Identifiable {
    var id: Int
    var weatherImageName: String
    var date: String
    var dayOfWeek: String
    var time: String
    var exampleText: String
    var like: Bool
}

class CardViewModel: ObservableObject {
    @Published var cardItems: [CardItem1] = [
        CardItem1(id: 1, weatherImageName: "Weather_Sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요", like: true),
        CardItem1(id: 2, weatherImageName: "Weather_Sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요", like: true),
        CardItem1(id: 3, weatherImageName: "Weather_Sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요", like: true),
        CardItem1(id: 4, weatherImageName: "Weather_Sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요", like: true),
        CardItem1(id: 5, weatherImageName: "Weather_Sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요", like: true)
       
    ]
}
