//
//  PathModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


import Foundation


class PathModel : ObservableObject { // 패쓰모델을 지정한것도 지정한것데 여기서 ObservavleObject를 사용하는 이유는 다른 모델에서 다른뷰에 데이터들을 확인하려고 하는거지
    @Published var paths : [PathType] // 즉 여기서 PathType 을 데이터가변하는걸 받아와서 위에 옵저버블옵젝트에서 구독하고있는 녀석들의 데이터가 변하면 그값들을 뷰단에서 업데이트해주는 역할을 한다.
    
    init(paths : [PathType] = []) // 초기화를 안해도 되는경우 생략이 가능한것을 얘기해, 그래서 그냥 초기값을 안주고 이렇게 해도 배열의 초기값 자체를 없다고 생각해도 되는듯
    
    {
        self.paths = paths // 클래스 안에있는 자기자신에 변수에 접근할 수 있도록 구현한거임
    }
}
