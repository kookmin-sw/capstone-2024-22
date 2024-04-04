//
//  PasswordinsertView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/26/24.
//

import SwiftUI

struct PasswordinsertView: View {
    
    @State private var pathword: String = ""
    @State private var checkpathword: String = ""
    @EnvironmentObject private var pathModel: PathModel
   // @Environment(\.presentationMode) var presentationMode
    
        
    var body: some View {
        VStack{   
            Button(action: {
                // "뒤로" 버튼의 액션: 현재 뷰를 종료
                pathModel.paths.append(.PasswordView)
            }) {
                HStack {
                    
                    Image("arrow1")
                    Text("로그인")
                        .padding()
                        .font(.pretendardSemiBold18)
                        .tint(Color.black)
                    Spacer()
                }
            }
           
            Spacer().frame(height: 160)
            
            
            HStack(alignment: .bottom) {
                Text("새비밀번호")
                    .font(.pretendardMedium11)
                    .foregroundColor(.gray600)
                    .padding(.top, 1)
                    .padding(.horizontal, 10)
                
                Spacer()
            }
            
            TextField("비밀번호를 입력해주세요.", text: $pathword)
                .font(.pretendardMedium11)
                .padding()
                .frame(height: 44)
                .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                .padding(.bottom, 3)
                .padding(.horizontal, 10)
            
            HStack(alignment: .bottom) {
                Text("새 비밀번호 확인")
                    .foregroundColor(.gray600)
                    .font(.pretendardMedium11)
                    .padding(.top, 1)
                    .padding(.horizontal, 10)
                
                Spacer()
            }
            
            TextField("비밀번호를 다시 한번 입력해주세요.", text: $checkpathword)
                .padding()
                .frame(height: 44)
                .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                .padding(.bottom, 3)
                .padding(.horizontal, 10)
            
           
            
            Spacer()
            Button(action: {
                // 가입하기 버튼 동작 구현
                pathModel.paths.append(.LoginView)
            }, label: {
                Text("로그인하기")
                    .frame(minWidth: 0, maxWidth: .infinity)
                    .font(.pretendardSemiBold18)
                    .padding()
                    .background(Color.homeRed)
                    .foregroundColor(.white)
                    .cornerRadius(3)
            })
        }.navigationBarBackButtonHidden(true)
        .padding()
    }
}

#Preview {
    PasswordinsertView()
}
