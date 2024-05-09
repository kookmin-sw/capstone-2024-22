//
//  Tab.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

enum Tab: Int, CaseIterable, Identifiable, Comparable {
    case Home, Bill,voiceRecorder, Like, setting

    var id: Int { self.rawValue }

    var selectedIconName: String {
        switch self {
        case .Home: return "Home_Se"
        case .Bill: return "Bill_Se"
        case .voiceRecorder: return ""
        case .Like: return "Like_Se"
        case .setting: return "Setting_Se"
        }
    }
    
    var unselectedIconName: String {
           switch self {
           case .Home: return "Home_Un"
           case .Bill: return "Bill_Un"
           case .voiceRecorder: return "" // 예시 이름입니다. 실제 이름으로 변경하세요.
           case .Like: return "Like_Un"
           case .setting: return "Setting_Un"
           }
       }

    var title: String {
        switch self {
        case .Home: return "홈"
        case .Bill: return "영수증"
        case .voiceRecorder: return "녹음하기"
        case .Like: return "즐겨찾기"
        case .setting: return "설정"
        }
    }

    static func < (lhs: Tab, rhs: Tab) -> Bool {
        lhs.rawValue < rhs.rawValue
    }
}
