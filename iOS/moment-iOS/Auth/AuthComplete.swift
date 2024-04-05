//
//  AuthComplete.swift
//  moment-iOS
//
//  Created by 양시관 on 3/20/24.
//

import SwiftUI

struct AuthComplete: View {
    @EnvironmentObject private var pathModel: PathModel
    
    var body: some View {
        VStack{
            Spacer()
            
            Text("회원가입이 완료되었어요\n만나서 반가워요 !")
                .multilineTextAlignment(.center)
                .font(.pretendardBold30)
            Spacer()
            Button(action: {
                // 가입하기 버튼 동작 구현
                pathModel.paths.append(.LoginView)
            }, label: {
                Text("로그인하기")
                    .multilineTextAlignment(.center)
                    .font(.pretendardSemiBold18)
                    .frame(minWidth: 0, maxWidth: .infinity)
                    .padding()
                    .background(Color.homeRed)
                    .foregroundColor(.white)
                    .cornerRadius(3)
            })
            
        }.padding()
            .navigationBarBackButtonHidden(true)
    }
}



#Preview {
    AuthComplete()
}
