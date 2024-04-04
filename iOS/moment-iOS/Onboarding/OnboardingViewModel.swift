//
//  OnboardingViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation



import Foundation
//2. flow 이제 뷰모델을 만들었으니까 뷰를 만들러 가보자



class OnboardingViewModel : ObservableObject{ // 이거는 뭐냐면 일단 클래스 선언한거고 ObservableObject 프로토콜을 왜 사용하는거냐면 온보딩 뷰모델을 구독하는 녀석들에 대한 데이터의 변화를 동적으로 관리해주는 녀석이야 이유는 일단 스위프트에 강점 인것도있지 왜냐면 선언형을 따르면서 MVVM 패턴을 유지해줄수있는 큰이유중에 하나거덩 이녀석이 데이터를 구독하고 있다가 여기에있는 데이터들이 다른곳에서 쓰이면 그걸 구독하고 업데이트하는역할을 하는거지  한마디로 이걸 사용하는 데이터들은 다른곳에서 데이터가 변하면 그걸 구독하고 있다가 데이터를 알려주는거지 MVVM 패턴에서 왜 유리하냐면 이런게있어야. 아니다 일단 우선적으로 MVVM 은 사용자의 뷰와 인터렉션과 데이터들을 구분해놓은 디자인 패턴인데 이런게 있기때문에 데이터는 바인딩해서 사용할 수 있는거고 뷰단에서는 그것만 사용할 수 있게되는거지
    
    @Published var onboardingContents : [OnboardingContent] // 퍼블리쉬드 가 뭐냐면 온보딩 컨텐트에 있는 데이터들이 있잖아 그럼 걔네들의 데이터를 사용하고 있다고 선언해놓는거야 그럼 여기서 보고있다가 위에 옵져버블 옵젝트에서 데이터의 변화를 느끼고 뷰에다가 데이터를 새로고침하는거지!
    //그리고 여기에서 배열로 타입을 넣어줬기 떄문에 밑에 init 부분인.초기화 부분에서 그 행동을 해주는거야 상태값을 추적하기위해서
    
    
    init(
        onboardingContents: [OnboardingContent] = [
            .init(imageFileName: "onboarding1", title: "언제", subTitle: "홈뷰에서 여행기록"),
            .init(imageFileName: "onboarding2", title: "왜?", subTitle: "여행가자!"), //배열로 타입을 넣어줬기 떄문에 밑에 init 부분인.초기화 부분에서 그 행동을 해주는거야
            .init(imageFileName: "onboarding3", title: "어쩌라고", subTitle: "여행가자!2"),
            .init(imageFileName: "onboarding4", title: "좋다", subTitle: "여행가자!3")
        ]
    
    ) {
        
        self.onboardingContents = onboardingContents //이거는 전에 나왔던거처럼 그냥 똑같이 같은 클래스나 구조체안에다가 자기자신의 값에 접근하고 싶을때 사용하는것이지
        
    }
}
