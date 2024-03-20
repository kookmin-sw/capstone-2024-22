//
//  HomeViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


import Foundation


class HomeBaseViewModel: ObservableObject {//클래스가 구현되어있고 옵저버블옵젝트 프로토콜을 따르면서 homeview 에서 이클래스안에 데이터와 인스턴스를 사용할수있도록 이 프로토콜을 사용한거다 그래서 여기서 변한 친구들의 데이터들을 이프로토콜을 구독하는 녀석들한테 데이터를 업데이트해줄수있음
  @Published var selectedTab: Tab // 여기서일단 어떤 뷰에 있는 지알려줄려고 이넘타입의 클래스인 Tab 열거형의 타입을 따른다고 알려주고있고 퍼블리시드는 이변수가 변하게되면 해당 데이터를 업뎃해주겠다라는 모습이보임 근데 그럼 State랑 은뭐가다름 ? 둘다똑같잖아 ㅋㅋ
    //자 집중을 해보면 퍼블리드랑 State랑으 차이점은 State는 뷰 내부에서 데이터에 대한 변화를 알려주고 업뎃용이고 Published 는 모델링하는부분처럼 데이터를 다른 뷰에서도 관리를 해야할때 사용함 한마디로 데이터의 변화 추적? 이라고해야될거같음 좀더 큰 범위라고 생각하면 편할듯
    // 그럼 Observable이랑은 뭐가 다르냐고 생각하면 Observable얘가 제일 큰범위인거같음 얘는 객체 자체에 접근을 할때 주로 쓰이고 사실상 전역변수즉 앱전체에서의 객체의 데이터변화를 관리할때 사용하는듯함
  @Published var todosCount: Int // Int 타음을 지정하나봄 즉 데이터가 변화하는 todosCount의 데이터가 변화할대 그 데이터의 형식은 Int 안에서 찾아봐라 그래야한다 라는 의미인듯
  @Published var memosCount: Int//
  @Published var voiceRecordersCount: Int//
  
  init(
    selectedTab: Tab = .Home,
    todosCount: Int = 0,
    memosCount: Int = 0,
    voiceRecordersCount: Int = 0
  ) {
    self.selectedTab = selectedTab
    self.todosCount = todosCount
    self.memosCount = memosCount
    self.voiceRecordersCount = voiceRecordersCount
  }
}// 초기화해주고 클래스안에 인스턴스에 대한 접근

extension HomeBaseViewModel { // class 의 추가적인 기능을 만들기 위해서 extension 으로 확장해주고
  func setTodosCount(_ count: Int) {
    todosCount = count // 이거는 이제 투두리스트에 몇개의 투두리스트가있는지 세는 녀석인데 그건 숫자니까 Int 로 지정해주었고 함수매개변수로 count 받아서 todosCount 에다가 이거 받아서 변경해주는거임
  }
  
  func setMemosCount(_ count: Int) {
    memosCount = count
  }
  
  func setVoiceRecordersCount(_ count: Int) {
    voiceRecordersCount = count
  }
  
  func changeSelectedTab(_ tab: Tab) {
    selectedTab = tab
  }
}
// 이하 생략 모두 같음
