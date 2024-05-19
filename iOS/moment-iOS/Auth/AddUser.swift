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
    @ObservedObject var authViewModel: AuthViewModel
    var body: some View {
        ZStack {
            Color.clear
                .onTapGesture {
                    hideKeyboard()
                }
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
                
                TextField("이메일을 입력해 주세요", text: $authViewModel.email)
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
                
                TextField("비밀번호를 입력해주세요", text: $authViewModel.pathword)
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
                    //로그인 유저 함수 호출해서 서버에다가 내가 텍스트필드에 입력한 아이디 비밀번호 올리기 
                    authViewModel.changePassword()
                    
                    
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
    private func hideKeyboard() {
          UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
      }
}
