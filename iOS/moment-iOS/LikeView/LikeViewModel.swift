//
//  TimerViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation

import UIKit
import Foundation
import Foundation
import UIKit

struct CardItem: Identifiable {
    let id: Int
    let weatherImageName: String
    let date: String
    let dayOfWeek: String
    let time: String
    let exampleText: String
    let Like : Bool
}


class LikeViewModel: ObservableObject {
    @Published var cardItems: [CardItem] = [
        CardItem(id: 1, weatherImageName: "sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요", Like: true),
        CardItem(id: 2, weatherImageName: "sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요", Like: true),
        CardItem(id: 3, weatherImageName: "sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요", Like: true),
        CardItem(id: 4, weatherImageName: "sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요", Like: true),
        CardItem(id: 5, weatherImageName: "sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요", Like: true)
       
    ]
}
