//
//  Time.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI

struct Time {
    var hours : Int  //왜 var 야? let 은 안돼? 생각해서 let 으로 바꿔봄 근데 그러니까 Time 를 쓰는 녀석들한테서 오류가남 timer 뷰인데 여기서 이제 인스턴스를 받아서 변화가일어나잖아 근데 let 은 바뀌면 안되는녀석이니까 할당하지 못한다고 뜨더라
    var minutes : Int
    var seconds : Int
    
    var convertedSeconds : Int {
        return (hours * 3600) + (minutes * 60) + seconds
        
    } // 이게 이제 계산 프로퍼티를 정의해논부분인데 Void 가 아니니까 일단 값을 뱉어주기는 해야하고 그뱉는 로직이 시간,분을 모두 계산해서 초로 반환하네 어떻게 보면 메소드인거지 구조체안에 선언도되어있고 값을 리턴하니까 함수 겸 메소드라고 할수 있겠다 말이 좀이상하긴한데 메서드라고 하기에는 이제 func 를 안썼으니까 그렇다고 메서드라고 할순없어 정확하게는 지금 변수들로만 정의되어있으니까 근데 의미가 결국 계산을 해주는녀석이니까 개발자입장에서는 뭐라고 생각하던 그냥 어떤 연산을 하기위한 도구이기 때문에 그렇게 생각해도 문제는 없을듯
    
    static func fromSeconds(_ seconds : Int) -> Time {
        let hours = seconds / 3600
        let minutes = (seconds % 3600) / 60
        let remainingSeconds = (seconds % 3600) % 60
        return Time(hours: hours, minutes: minutes, seconds: remainingSeconds)
    }
}
//마지막 함수부분은 일단 static 이니까 정적 메소드겠지 타입메서드인거야 특정인스턴스를 안만들어도 되는녀석인거지 그리고 fromSeconds 라는 함수이름으로 언더스코어는 받을 매겨변수들을 얼추 생략하겠다라는 의미이고 Int 타입으로 매개변수를 받아서 Time 구조체인스턴스의 값으로 던져주겠다라는 의미임.
    //그래서 이함수안에서 정의된 변수들한테 계산식을 부여하고 계산을 한다음에 Time 인스턴스들한테 이름으로 던져주는거지 그러면 구조체인 Time 에서는 이제 반환값으로 해당 계산된 내용의 값들을 가질수있는거야 이제 인스턴스에 값을 넣었으니까 이걸 어딘가에서 쓸수도있겠지?
