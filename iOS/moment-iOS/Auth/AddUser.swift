//
//  AddUser.swift
//  moment-iOS
//
//  Created by 양시관 on 3/20/24.
//

import SwiftUI

struct AddUser: View {
    @State private var email: String = ""
    @State private var pathword: String = ""
    @State private var checkpathword: String = ""
    @EnvironmentObject private var pathModel: PathModel
    var body: some View {
        VStack{
            Button(action: {
            // "뒤로" 버튼의 액션: 현재 뷰를 종료
            pathModel.paths.append(.AuthView)
        }) {
            HStack {
                
                   
                Image("arrow1")
                    .padding(.horizontal,10)
                Text("로그인")
                    
                    .font(.pretendardSemiBold18)
                    .tint(Color.black)
                Spacer()
            }
        }
            
            Spacer().frame(height: 90)
            
            HStack(alignment: .bottom) {
                Text("아이디")
                    .foregroundColor(.gray600)
                    .font(.pretendardMedium11)
                    .padding(.top, 1)
                    .padding(.horizontal, 10)
                
                Spacer()
            }
            
            TextField("이메일을 입력해 주세요", text: $email)
                .padding()
                .frame(height: 44)
                .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                .padding(.bottom, 3)
                .padding(.horizontal, 10)
            
            HStack(alignment: .bottom) {
                Text("비밀번호")
                    .foregroundColor(.gray600)
                    .font(.pretendardMedium11)
                    .padding(.top, 1)
                    .padding(.horizontal, 10)
                
                Spacer()
            }
            
            TextField("비밀번호를 입력해주세요", text: $pathword)
                .padding()
                .frame(height: 44)
                .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                .padding(.bottom, 3)
                .padding(.horizontal, 10)
            
            HStack(alignment: .bottom) {
                Text("비밀번호 확인")
                    .foregroundColor(.gray600)
                    .font(.pretendardMedium11)
                    .padding(.top, 1)
                    .padding(.horizontal, 10)
                
                Spacer()
            }
            
            TextField("비밀번호를 다시 한번 입력해 주세요", text: $checkpathword)
                .padding()
                .frame(height: 44)
                .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                .padding(.bottom, 3)
                .padding(.horizontal, 10)
            
            Spacer()
            Button(action: {
                // 가입하기 버튼 동작 구현
                pathModel.paths.append(.AuthComplete)
            }, label: {
                Text("가입하기")
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
    AddUser()
}
