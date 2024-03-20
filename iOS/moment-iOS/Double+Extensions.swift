//
//  Double+Extensions.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation



import Foundation

extension Double { // extension 을 사용하여 더블 타입에 추가적인 기능을 만들어
    var formattedTimeInterval : String { // 이코드블럭을 설명하자면 더블형태의 시와 분초들을 스트링으로 바꿔서 보여주는거임
        let totalSeconds = Int(self) // 여기서 Int 만 더블형을 int 로 형변환을 ㅐ주면서 소수점 밑에를 버려주고
        let seconds = totalSeconds % 60// 60으로 나눠서 더이상 나눌수없는 값들을 남겨서 초로 변환해줌
        let minutes = (totalSeconds / 60) % 60
        
        return String(format: "%02d:%02d", minutes, seconds) // 그렇게 해서 return String(format: "%02d:%02d", minutes, seconds): minutes와 seconds를 이용하여 "분:초" 형식의 문자열을 생성합니다. %02d는 두 자리 숫자로 포맷팅하며, 필요한 경우 앞에 0을 붙여 두 자리를 맞춥니다. 예를 들어, 3분 7초는 "03:07"로 포맷팅됩니다
    }
}
