//
//  OnboardingContent.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


import Foundation
import SwiftUI
//1. flow 시작 모델객체를 먼저 설정해주고 그다음에 뷰모델로 만들러간다 -> onboardingviewModel 로 넘어간다 .

struct OnboardingContent : Hashable {  // Hashable 는 뭘까 이거를 왜씀? 결국 이녀석을 쓰는 이유자체는 객체를 해시 테이블 기반의 데이터 구조에서 효율적으로 사용할 수 있게 하기 위함이지.
    //해시어블은 있잖아 일단 데이터구조를 디게 간단하게 만들어주는데 이게 key value 로 바뀌어가는느낌이거덩 해시함수를 거쳐서 배열에다가 인덱스를 맞춰서 값들을 넣어주는 역할을 함으로써 데이터의 구조화등에 대해서 이득이 된다. 해시테이블기반 구조 중요함
    
    
    var imageFileName: String // 해당 구조쳉에서 쓰려고하는 녀석들을 선언해준거다
    var title : String
    var subTitle: String
    
    init(
        imageFileName : String, //초기화 잘 받아준거임 init 으로 잘해주고있고 이걸 근데 꼭 해줘야하나?
        title : String,
        subTitle: String
    )
    {
        self.imageFileName = imageFileName //self 는 같은 클래스나 구조체안에서 변수들에 접근하기 위해서 사용하네 그래서 자기자신이라는 의믜에 self 를 써서 접근하는거구나
        self.title = title
        self.subTitle = subTitle
    }
}
