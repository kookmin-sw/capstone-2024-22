//
//  MemoListViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


class BillListViewModel : ObservableObject { // 옵서버블옵젝트를 따르는 클래스를 선언해줌 이유는 옵저버블 옵젝트를 가지고있는 다른 데이터모델들을 사용해야하기때문임
    @Published var memos : [Memo] //퍼블리쉬드로 Memo 데이터를 memos 에 넣어서 이클래스에서 memos 로 Memo 데이터에 접근할수있도록 해준다,
    @Published var isEditMemoMode : Bool //이것도 마찬가지로 이 클래스 내부에서 isEditMeomoMode 를 bool값으로 선언해서 상태를 조절해주려는 구현임
    @Published var removeMemos : [Memo] // ""
    @Published var isDisplayRemoveMemoAlert : Bool // ""
    //더미데이터 삽입
   
    
    var removeMemoCount : Int { // 인트형으로 값이 나올것을 암시해주고
        return removeMemos.count//removeMemo로 들어가서 .conunt 라는 인스턴스를 불러줌 보라색글씨니까 그냥 지원해주는거임 count 새주는거
    } // 코드 블럭으로 저장은 하지않는 값이 변경되면 그냥 계산된 값을 던져주는 코드 블럭
    
    var navigationBarRightBtnMode : NavigationBtnType { // 네비게이션 버튼 타입으로 리턴값을 주는거임
        isEditMemoMode ? .complete : .edit // 여기서 처럼 NavigationBtnType 이거를 주면 isEditMemoMode 가 참이면 complete 를 반환 아니면 에딧모드를 반환해서 쓰게해줌 이 인스턴스들이 NavigationBtnType 여기서 나온것들이라 이렇게 구현을 한거임
    }
    init(memos : [Memo] = [],
         isEditMemoMode: Bool = false,
         removeMemos: [Memo] = [],
         isDisplayRemoveMemoAlert: Bool = false
    )
    {
        self.memos = memos
        self.isEditMemoMode = isEditMemoMode
        self.removeMemos = removeMemos
        self.isDisplayRemoveMemoAlert = isDisplayRemoveMemoAlert
    } // 초기화해주고 자기클래스의 자기자신을 인스턴스화 해준거임
}

extension BillListViewModel { // 익스텐션으로 추가기능 만들어줬고
    func addMemo(_ memo : Memo) { // 함수이름 addMemo 로 _ 는 함수를 호출할때 매개변수의 레이블을 선언해주지않아도 됨을 의미한다. 그래서 memo 를 Memo 라는 배열 인스턴스를 담아주고
        memos.append(memo) //여기서 이제 Memo라는 배열에 끝에다가 append 를 사용해서 끝에다가 값을 추가해주는 역할ㅇ르 한다 , 그 인스턴스에 접근하는 방식이 . 으로 접근하는것이다
    }
    
    func updateMemo(_ memo: Memo) { // 업데이트메모 함수이고 _ 는 함수를 호출할때 매개변수의 레이블을 안만들어줘도 됨을 의미를 한다 Memo 를 memo 에 넣어줬음
        if let index = memos.firstIndex(where: { $0.id == memo.id}){ // 조건 식인데 만약에 index 라는 변수에다가 memos 에 첫번째 인덱스를 넣은거 즉 음 인터페이스를 사용하는 사람의 인덱스와 메모의 index 가같으면 index 에 첫번째 값이 들어가고
            memos[index] = memo// 메모스에 그 index를 넣는다
        }//memos 배열에서 id가 주어진 memo의 id와 일치하는 첫 번째 Memo 객체의 인덱스를 찾습니다. 여기서 $0은 memos 배열의 각 요소를 나타내며, 클로저 내에서 현재 요소를 의미합니다.
      //  조건은 where 클로저를 통해 각 Memo 객체의 id가 주어진 memo의 id와 일치하는지를 검사합니다.
        //일치하는 요소가 있으면 해당 요소의 인덱스가 index에 바인딩됩니다.
    }
    
    func removeMemo(_ memo: Memo) {
        if let index = memos.firstIndex(where: { $0.id == memo.id}) {
            memos.remove(at : index)
        }
    }//if let index = memos.firstIndex(where: { $0.id == memo.id}): 이 구문은 memos 배열에서 주어진 memo의 id와 일치하는 첫 번째 Memo 객체의 인덱스를 찾습니다.
   // 여기서 $0은 memos 배열의 현재 요소(Memo 객체)를 나타냅니다. where 클로저는 이 요소의 id가 인자로 받은 memo의 id와 일치하는지 검사합니다.
    //조건이 참이면, 즉 일치하는 id를 가진 Memo 객체가 있으면, 해당 객체의 인덱스가 index 변수에 할당됩니다.
    //memos.remove(at: index): id가 일치하는 Memo 객체의 인덱스를 찾았다면, 이 인덱스를 사용하여 memos 배열에서 해당 객체를 제거합니다
    
    func navigationRightBtnTapped() { //함수만들어주고 if 걸어주기
        if isEditMemoMode {
            if removeMemos.isEmpty {
                isEditMemoMode = false//이걸 끝내버리는거임 아예 편집모드가 없이 할수있는거임
                
            }else {
                //삭제 알럿 상태값 변경을 위한 메서드 호출
                setIsDisplayRemoveMemoAlert(true)
            }
        } else {
            isEditMemoMode = true
        }
    }
    
    func setIsDisplayRemoveMemoAlert(_ isDisplay : Bool ){
        isDisplayRemoveMemoAlert = isDisplay // 삭제하냐고 물어보는 인스턴스네
    }
    
    func memoRemoveSelectedBoxTapped(_ memo : Memo) {
        if let index = removeMemos.firstIndex(of : memo){ // 메모가 이미 삭제 인덱스에 들어가있다면 그걸 삭제한다
            removeMemos.remove(at: index)
        } else {
            removeMemos.append(memo) //그게아니라면 메모를 배열끝에 붙인다
        }
    }
    
    func removeBtnTapped() {
        memos.removeAll() {
            memo in
            removeMemos.contains(memo)
        }
        removeMemos.removeAll()
        isEditMemoMode = false
    }
}


