//
//  CustomNavigationBar.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation

import SwiftUI

struct CustomNavigationBar: View {
  let isDisplayLeftBtn: Bool
  let isDisplayRightBtn: Bool // 있을지없을지 처럼 Bool 타입으로 만든 이유에 대해서는 내가 편집을 하는 페이지엥있으면 편집버튼이다른 녀석으로 바뀌어야될꺼아냐 그래서 Bool 로 먼저 판단을 해주는거임
  let leftBtnAction: () -> Void
  let rightBtnAction: () -> Void
  let rightBtnType: NavigationBtnType // 그래서 여기서 이넘들의 값들을 가져와서 타입을 판단해서 사용하려고 이렇게 만들어놓음
  
  init(
    isDisplayLeftBtn: Bool = true,
    isDisplayRightBtn: Bool = true,
    leftBtnAction: @escaping () -> Void = {}, //Swift에서 @escaping 속성은 클로저(Closure)가 함수의 매개변수로 전달될 때 사용됩니다. @escaping 속성을 가진 클로저는 함수의 실행이 끝난 후에도 클로저가 호출될 수 있음을 나타냅니다. 즉, 클로저가 함수의 범위를 "탈출(escape)"할 수 있다는 의미입니다 이 속성은 주로 비동기 작업이나, 클로저가 변수에 저장되어 나중에 호출될 때 필요합니다. 함수가 종료된 후에도 이 클로저가 실행될 수 있기 때문입니다. 이 이유가 왼쪽 버튼액션이 만약에 버튼이 눌릴경우에만 함수를 호출한다거나 할대 디게 유용하다고 함
    rightBtnAction: @escaping () -> Void = {},
    rightBtnType: NavigationBtnType = .edit // .edit 는 초기값을 전달하는 녀석임 그래서 NavigationBtnType 여기서 이제 열거형을 가저와서 사용하는느낌임
  ) {
    self.isDisplayLeftBtn = isDisplayLeftBtn
    self.isDisplayRightBtn = isDisplayRightBtn
    self.leftBtnAction = leftBtnAction
    self.rightBtnAction = rightBtnAction
    self.rightBtnType = rightBtnType
  }// self 로 해당 구조체안에 접근해서 초기화해주는 역할을 함
   
    
    
  var body: some View {
    HStack {
      if isDisplayLeftBtn {
        Button(
          action: leftBtnAction,
          label: { Image("leftArrow") }
        )
      } // 만약 레프트 버튼이 눌렸을때 어떤 액션을 이뤄내느지 어떤 이미지를 가지고있는지에 대해서 정의해줌
      
      Spacer()
      
      if isDisplayRightBtn {
        Button(
          action: rightBtnAction,
          label: {
            if rightBtnType == .close {
              Image("close")
            } else {
              Text(rightBtnType.rawValue)// 아 저위에 .edit 이부분에서 edit 이 문법인줄알았는데 그게아니네 그냥 이넘에 edit 를 불러는거였네 그게 원시값이네
                .foregroundColor(.customBlack)// 여기는 오른쪽 버튼 근데 여기서 분기중에 if 문에서 닫힘 이넘타입이 들어와있으면 클로스 이미지 띄워주고
            }
          }
        )
      }
    }
    .padding(.horizontal, 20)
    .frame(height: 20)
  }
}

struct CustomNavigationBar_Previews: PreviewProvider {
  static var previews: some View {
    CustomNavigationBar()
  }
}
