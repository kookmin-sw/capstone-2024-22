//
//  ContentView.swift
//  moment-iOS
//
//  Created by 양시관 on 1/27/24.
//

import SwiftUI

struct ContentView: View { //왜 구조체여야할까 -> 일단 class 와의 차이점이 명확하다 class 는 다른곳에서도 참조를 해야하는데 구조체는 안그래도 됨 또한 메모리 관리가 디게 효율적임 , 구조체는 값타입이고 class 는 참조타입임 즉 구조체는 각자의 복사본을 가지는 개념이라서 그럼
    //그래서 다른데서 바꿔도 영향을 안주는게 가장큼 그리고 View 는 이 구조체를 뷰라고 인식하는 프로토콜임 규칙같은 느낌인거지 그니까 저거를 안쓰면 내가 뷰를 그린다라는것을 컴퓨터가 인식을못함 그래서 그럼 그리고 stack 에 저장해서 사용하기때문에 빈번한 메모리관리에서 유리함
    
    var body: some View { // body가 이제 난 뷰를 그릴게 이거임 some 은 뭐냐면 어떤 타입이던 반환할 수 있게 해주는거임 예를 들면
        //내가 엄청 많은 뷰를 이안에다가 그린다고 예시를 잡는다면 이제 some 라는게 유연성을 주는거지 이안에서 어떤일이일어나던 컴파일시 기준으로
        //그걸맞춰서 반환해준다는거임 이로써 유연성을 줄 수 있는거지 그러니까 개발자입장에서도 편한거고
        
        VStack { // 세로로 새운다라는 의미임 세로로 쭉 쌓아놓겠다~
            Image(systemName: "globe")// 이미지 사진불러오는부분
                .imageScale(.large)//이미지크기에 대한 정의
                .foregroundStyle(.tint) // 이미지색상에 대한 정의
            Text("Hello, world!")// 글씨 .
        }
        .padding()// Vstack 과 내부요소에 대한 공간을 주겠다라는 의미임
    }
}

#Preview {
    ContentView()
}
