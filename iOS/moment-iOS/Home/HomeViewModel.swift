//
//  TodoListViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


class HomeViewModel : ObservableObject {//뷰모델을 만들어서 Todo 에 있는녀석 가져와서 뷰모델에서 뷰에 넣기 전에 데이터들을 잘 처리해준다 이렇게 해주는게 MVVM 패턴인거거덩
    
    
    @Published var todos : [Todo]
    @Published var isEditTodoMode : Bool
    @Published var removeTodos : [Todo]
    @Published var isDisplayRemoveTodoAlert : Bool
    @Published var tripName: String = ""
    @Published var tripStartDate: Date?
    @Published var tripEndDate: Date?
    @Published var showingDeleteAlert: Bool = false
    @Published var indexToDelete: Int? = nil
    //@Published var tripInfo: (name: String, startDate: Date?, endDate: Date?) = ("", nil, nil)
   
    
    var removeTodosCount : Int {
        return removeTodos.count // 여기 카운트는 그냥 제공하는녀석이네 이걸 왜의심했냐면 removeTodos 에 갔는데 데이터셋 Todo 를 가지고있어서 가봤더니 count 가 없네 그래서 count 를 의심했음
        // 여튼 그렇게 해서 INt 형식으로 이번엔 값을 저장을 해주네 ㅇㅇ 아까 전에는 저장안하고 변하면 바로계산해서 쏴주기만했자나
    }
    var navigationBarRightBtnMode : NavigationBtnType { // 네비게이션 버튼타입에 가서 내가쓰고싶은 열거형 ㅌ친구들을 잘써준 모습
        isEditTodoMode ? .complete: .edit // 기본문법이니까 패스 ? 후 참이면 1 flase 면 두번째거
    }
    init(
        todos: [Todo] = [], // 이소스코드는 Todo 클래스를 타입으로 가지는 녀석을 빈배열로 초기화하는 소스코드임
         isEditModeTodoMode: Bool = false,
         removeTodos: [Todo] = [],// 이소스코드는 Todo 클래스를 타입으로 가지는 녀석을 빈배열로 초기화하는 소스코드임
         isDisplayRemoveTodoAlert: Bool = false
        
        )
    {
        self.todos = todos
        self.isEditTodoMode = isEditModeTodoMode
        self.removeTodos = removeTodos
        self.isDisplayRemoveTodoAlert = isDisplayRemoveTodoAlert
       
    }
    
    func updateTripInfo(name: String, startDate: Date?, endDate: Date?) {
           self.tripName = name
           self.tripStartDate = startDate
           self.tripEndDate = endDate
       }
    
    func prepareForDeletion(index: Int) {
        indexToDelete = index
        showingDeleteAlert = true
    }

    func deleteItem() {
        if let index = indexToDelete {
           // items.remove(at: index)
            indexToDelete = nil
            showingDeleteAlert = false
        }
    }
    
    
}

extension HomeViewModel { //TodoListViewModel의 새로운 기능을 추가하는용도의 소스코드
    
    func selectedBoxTapped(_ todo: Todo) {
        if let index = todos.firstIndex(where: { $0 == todo }) {
            todos[index].selected.toggle()
        }
    }// 모르겠다 시발 ㅋㅋ
    
    func addTodo(_ todo: Todo) {
        todos.append(todo)
    } // todo 리스트에 투두를 하나 추가하는 함수인거임 append 를 사용한게 그 이유임 뷰에서 보여줄때는 어떠한 장치를 써야할듯 함
    
    func navigationRightBtnTapped() {
        if isEditTodoMode {
            if removeTodos.isEmpty { // isEmpty 들어가보니까 스트링타입이 같은지를 파악해서 없으면 반환하는 거네
                isEditTodoMode = false // 제거할게없으면 편집모드가 불가능하니까 false 로 두고 그게아니면
            }
            else {
                setIsDisplayRemoveTodoAlert(true) // 그때는 보여주는 역할을 하네 그게 1
            }
        }else {
            isEditTodoMode = true//3 그게또 아닐경우엔 무조건 투두를 만들어야하니까 이렇게 조건을 준듯
        }
    }
    
    
    func setIsDisplayRemoveTodoAlert(_ isDisplay: Bool) { // 여기에서 set 을 ㅗ보여주는 역할을 하고 2
        isDisplayRemoveTodoAlert = isDisplay
    }
    
    func todoRemoveSelectedBoxTapped(_ todo: Todo) {
        if let index = removeTodos.firstIndex(of: todo) { // todo의 첫번째 요소를 찾아서 그인덱스를 추출해서 index에 넣고 그러면 여기서 문제가 생기는데 where 와 of 의 차이점을 알아보면 where 는 클로저가 존재하는가 를 일단 따지고 나서 그요소와 처음으로 같은인덱스를 가져오는거고 of 는 배열내에서 특정한 값과 동일한 요소를 찾을 때 사용하는거임
            removeTodos.remove(at: index)}//at으로 특정index 에 접근해서 제거하는 루트로 가는거같다
        else {
            removeTodos.append(todo)// 그게 아니라면 즉, 인덱스를 찾지못했을때는 todo를 만들어버리는개념임
        }
    }
    
    func removeBtnTapped() {
        todos.removeAll {
             todo in
            removeTodos.contains(todo)
        }
        removeTodos.removeAll()
        isEditTodoMode = false
    }
    
}
