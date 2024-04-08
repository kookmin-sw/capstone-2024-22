//
//  DailyCardViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 4/8/24.
//

import Foundation

struct CardItemDaily: Identifiable {
    var id: Int
    var weatherImageName: String
    var date: String
    var dayOfWeek: String
    var time: String
    var exampleText: String
    var like: Bool
}

class DailyViewModel: ObservableObject {
    @Published var DailyItems: [CardItemDaily] = [
        CardItemDaily(id: 1, weatherImageName: "Weather_Sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:03", exampleText: "꽤나 즐거운 대화였네요1qjsdlrjems dlrp ", like: true),
        CardItemDaily(id: 2, weatherImageName: "Weather_Sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:10", exampleText: "꽤나 즐거운 대화였네요adfasdfasdfasdfasdf", like: true),
        CardItemDaily(id: 3, weatherImageName: "Weather_Sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:15", exampleText: "꽤나 즐거운 대화였네요asdfasdfasdfsdafsdafdsafdsafdsf", like: true),
        CardItemDaily(id: 4, weatherImageName: "Weather_Sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:19", exampleText: "꽤나즐거운 대화였네요dfgdfghdfghteww", like: true),
        CardItemDaily(id: 5, weatherImageName: "Weather_Sunny", date: "2024.03.05", dayOfWeek: "화요일", time: "15:24", exampleText: "꽤나 즐거운 대화였네요1243411", like: true)
       
    ]
}
