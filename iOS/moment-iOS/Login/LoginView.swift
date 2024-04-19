//
//  LoginView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/18/24.
//

import SwiftUI
import Foundation
import UIKit


struct LoginView: View {
    @EnvironmentObject private var pathModel: PathModel
    @StateObject private var authViewModel = AuthViewModel()
    @State private var isLoading: Bool = false

    var body: some View {
        VStack {
            StartHomeBtnView(authViewModel:authViewModel) {
//                isLoading = true
//                authViewModel.loginUser()
                isLoading = true
                authViewModel.loginUser()
            }

            if isLoading {
                LoadingView()  // 로딩 뷰 활성화
            }
            if authViewModel.loginError {
                           Text("로그인 실패: 잘못된 이메일 또는 비밀번호")
                               .foregroundColor(.red)
                               .padding()
                       }
            
        }.onChange(of: authViewModel.isLoggedIn) { isLoggedIn in
            if isLoggedIn {
                pathModel.paths.append(.homeBaseView)  // 로그인 성공
            }
        }
        
    }


}



 struct StartHomeBtnView: View {
    @EnvironmentObject private var pathModel: PathModel
    @ObservedObject var authViewModel: AuthViewModel
    @State private var email: String = ""
    @State private var password: String = ""
    @State private var isPasswordVisible: Bool = false
    @State private var isAutoLogin: Bool = false  // 자동로그인 상태 관리
    
    var onLogin: () -> Void
    
     var body: some View {
        Spacer()
        VStack {
            HStack(alignment: .bottom) {
                Text("아이디")
                    .foregroundColor(.gray600)
                    .font(.pretendardMedium11)
                    .padding(.top, 1)
                    .padding(.horizontal, 10)
                
                Spacer()
            }
            TextField("이메일을 입력해 주세요.", text: $authViewModel.email)
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
            HStack {
                if isPasswordVisible {
                    TextField("비밀번호를 입력해 주세요.", text: $authViewModel.password)
                } else {
                    SecureField("비밀번호를 입력해 주세요.", text: $authViewModel.password)
                }
                Button(action: {
                    isPasswordVisible.toggle()
                }) {
                    Image(systemName: isPasswordVisible ? "eye.fill" : "eye.slash.fill")
                        .foregroundColor(.gray)
                }
            }
            .padding()
            .frame(height: 44)
            .overlay(Rectangle().frame(height: 1), alignment: .bottom)
            .padding(.horizontal, 10)
            
            HStack {
                Button("비밀번호 찾기") {
                    pathModel.paths.append(.PasswordView)
                }
                .font(.pretendardMedium11)
                .foregroundColor(.black)
                .padding(.bottom, 10)
                .overlay(
                    Rectangle().frame(height: 1),
                    alignment: .bottom
                )
                
                Spacer()
                Text("자동 로그인")
                    .font(.pretendardMedium11)
                Button(action: {
                    isAutoLogin.toggle()
                }) {
                    Image(systemName: isAutoLogin ? "checkmark.square.fill" : "square")
                        .foregroundColor(isAutoLogin ? .homeRed : .gray)
                }
            }
            .padding([.top, .horizontal], 10)
            
        }
        .background(Color.white.opacity(0.2))
        .cornerRadius(3.0)
        .padding(.bottom, 10)
        .padding()
        
        Spacer()
        VStack {
            Button(action: {
                // 로그인 버튼 동작 구현
                authViewModel.loginUser()
                pathModel.paths.append(.homeBaseView)
            }, label: {
                Text("로그인")
                    .font(.pretendardSemiBold18)
                    .frame(minWidth: 0, maxWidth: .infinity)
                    .padding()
                    .background(email.isEmpty || password.isEmpty ? Color.gray : Color.homeRed)
                    .foregroundColor(.white)
                    .cornerRadius(3)
            })
           /* .disabled(email.isEmpty || password.isEmpty) */ // 이메일과 비밀번호 입력 여부에 따라 버튼 활성화
            .padding(.bottom, 20)
            
            Button(action: {
                // 가입하기 버튼 동작 구현
                pathModel.paths.append(.AuthView)
            }, label: {
                Text("가입하기")
                    .font(.pretendardSemiBold18)
                    .frame(minWidth: 0, maxWidth: .infinity)
                    .padding()
                    .background(Color.homeRed)
                    .foregroundColor(.white)
                    .cornerRadius(3)
            })
        }
        .padding()
    }
}

struct IsLoginView: View {
    @EnvironmentObject private var pathModel: PathModel
    @State private var isLoading: Bool = false
    @StateObject private var authViewModel = AuthViewModel()
    var body: some View {
        ZStack {
            StartHomeBtnView(authViewModel: authViewModel, onLogin: {
                isLoading = true
                //loginUser()
            })
            
            if isLoading {
                LoadingView() // 로딩 상태일 때 LoadingView를 표시
            }
        }
    }
    
//    private func loginUser() {
//        // 로그인 로직 구현. 예시로 2초 후에 로그인이 완료되었다고 가정
//        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
//            isLoading = false  // 로그인 시도 후 isLoading 상태 업데이트
//            pathModel.paths.append(.homeBaseView) // 로그인 성공 시 홈 뷰로 전환
//        }
//    }
}


struct LoadingView: View {
    var body: some View {
        VStack {
            ProgressView() // 기본 원형 프로그레스 뷰
                .progressViewStyle(CircularProgressViewStyle(tint: .blue)) // 프로그레스 뷰 색상 변경
                .scaleEffect(2) // 프로그레스 뷰 크기 조절
            Text("Loading...")
                .font(.headline)
                .foregroundColor(.gray)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity) // 부모 뷰에 꽉 차게 확장
        .background(Color.white) // 배경 색상 설정
        .edgesIgnoringSafeArea(.all) // Safe area 무시하고 전체 화면에 표시
    }
}


#Preview {
    LoginView()
}
