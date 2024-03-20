//
//  MemoViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


class MemoViewModel : ObservableObject {
    @Published var memo : Memo
    
    init(memo: Memo) {
        self.memo = memo
    }
}
//메모 뷰모델 구현 해주면서 viewmodel 을 사용할수있게 통일화해줌 사실 여기아무것도 없어도 되는데 viewmodel 을 사용하려고 해주는겅미
