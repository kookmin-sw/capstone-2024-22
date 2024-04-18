//
//  Color+Extensions.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI
//일단 extension 을 먼저 설정을 해준다.

extension Color { // 왜 extension 을 썼지? : extension 은 원래있던 클래스나 이넘등의 경우에서 추가로 함수나 메서드등을 추가해줄수있는 녀석이네, 그래서 나는 색깔의 이름들을 새로 초기화해서 내가 쓰기편하게 하려고 이런거를 사용하는구나,
    
    
    static let customBackgroundGreen = Color("Green") // 색ㄱ깔을 정의하는데 왜 static let 을 썻을까? : 일단 인스턴스를 만들필요가 없음, namespace는 또 제공을 함 그러니까 이런 색상등처럼 프로젝트 전역에 쓰여야할때 object를 다넘겨주지않아도 되니까 이렇게 진행하나봄
    
     
    static let customBlack = Color("Black")
    static let customCoolGray = Color("YellowBlue")
    static let customGray0 = Color("Gray0")
    static let customGray1 = Color("Gray1")
    static let customGray2 = Color("Gray2")
    static let customGreen = Color("customGreen")
    static let customIconGray = Color("Gray3")
    static let customOrange = Color("Orange")
    static let customSky = Color("SkyBlue")
    static let customWhite = Color("White")
    static let homeRed = Color("homeRed1")
    static let toastColor = Color("ToastColor")
    static let unsafeColor = Color("unsafeColor")
    static let StrangeColor = Color("StrangeColor")
    static let LikeColor = Color("LikeColor")
    static let Secondary50 = Color("Secondary50")
    static let Natural500 = Color("Natural500")
    static let Natural50 = Color("Natural50")
    static let Natural300 = Color("Natural300")
    static let Natural200 = Color("Natural200")
    static let Natural100 = Color("Natural100")
    
    
}
