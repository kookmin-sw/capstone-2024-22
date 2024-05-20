//
//  PathType.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation



import Foundation


enum PathType: Hashable { // 이넘은 열거형으로 써본결과 가장 제한이없는 녀석인듯 음 그러니까 좀 편하게 같은 유형의 친구들을 모아놓는걸 의미한다잉
    case homeBaseView 
    //이거는 지금이뷰만 봐서는 정확한 의미를 쓰긴힘들지만 다하고 와서 한번 더 볼듯 근데 강의를 보며 느낀거는 홈뷰로 가는 길을 지정해주려고 만들어준거임
   // case todoView//위와 같지만 이번엔 todoView 로 이동한다는 개념으로 구현해놈
  //  case memoView(isCreateMode : Bool , memo : Memo?) // 이것도 다른애들과같기는한데 메모뷰에서 참조해야하는값들이 있나보네 그래서 이렇게 해놓은거같에 의미로 따지자면 메모를 만드는 뷰로 들어갈건지를 Bool 타입으로 지정해놓은거고 memo 는 메모가 있는지없는지도 판별해야되니까 ? 인 옵셔널로 표현해놓은거지
    case LoginView
    
    case AuthView
    case AuthNumView
    case AddUser
    case AuthComplete
    case PasswordView
    case PasswordfindView
    case PasswordinsertView
    
    
}
