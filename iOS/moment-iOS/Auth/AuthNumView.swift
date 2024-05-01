//
//  AuthNumView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/19/24.
//

import SwiftUI

struct AuthNumView: View {
    @State private var email: String = ""
    @EnvironmentObject private var pathModel: PathModel
    //@Binding var isDisplayTooltip: Bool
    @EnvironmentObject var authViewModel: AuthViewModel
    @State private var tempCodeColor: Color = .white
    
    
  
    
    var body: some View {
        VStack{
            Button(action: {
                // "뒤로" 버튼의 액션: 현재 뷰를 종료
                pathModel.paths.append(.AuthView)
            }) {
                HStack {
                    Spacer().frame(width: 20)
                    Image("arrow1")
                    Text("로그인")
                        .padding()
                        .font(.pretendardSemiBold18)
                        .tint(Color.black)
                    Spacer()
                }
            }
            
            Spacer().frame(height: 190)
            VStack(spacing:20){
                
                HStack{
                    Spacer()
                    ZStack {
                        CustomTriangleShapeLeftdown()
                            .fill(.gray500)
                            .frame(width: 24, height: 12) // 삼각형 크기 지정
                            .offset(x: -100, y: 20)
                        
                        CustomRectangleShapeLeftdown(text: "입력하신 이메일로 인증번호가 전송되었어요\n메일함을 확인해 주세요")
                            .font(.pretendardMedium11)
                            .frame(width: 340, height: /*@START_MENU_TOKEN@*/100/*@END_MENU_TOKEN@*/)
                            .padding(.bottom, 72)
                        
                    }
                    .frame(width: 210, height: 25)
                    Spacer()
                        .frame(width: 120)
                }
                
                HStack(alignment: .bottom) {
                    Text("인증번호")
                        .foregroundColor(.gray600)
                        .font(.pretendardMedium11)
                        .padding(.top, 1)
                        .padding(.horizontal, 20)
                    
                    Spacer()
                }.padding(.bottom,1)
            }
            TextField("인증번호를 입력하세요", text: $authViewModel.verificationCode)
            
                .padding()
                .frame(height: 44)
                .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                .background(tempCodeColor)
                .padding(.horizontal,20)
            
            HStack{
                Text("해당 이메일은 본인 인증 수단으로써 활용되며\n비밀번호 불실 시 복구코드를 보내드리는 용도로 사용됩니다.")
                    .foregroundColor(.gray600)
                    .font(.pretendardMedium11)
                    .padding(.top, 1)
                    .padding(.horizontal, 20)
                Spacer()
            }
            VStack{
                HStack {
                    Spacer()
                    Button("인증번호 재전송") {
                        // 비밀번호 찾기 동작 구현
                    }
                    .font(.pretendardMedium11)
                    .foregroundColor(.black)
                    .padding(.bottom, 10)
                    .overlay(
                        Rectangle().frame(height: 1),
                        alignment: .bottom
                    )
                }.padding(.horizontal,20)
                    .padding(.top,10)
                
                HStack{
                    Spacer()
                    ZStack{
                        CustomTriangleShapeRightup()
                            .fill(.gray500)
                            .padding(.leading, 200)
                        
                        CustomRectangleShapeRightup(text: "동일한 이메일로 재전송 되었어요")
                            .font(.pretendardMedium11)
                    } .frame(width: 270, height: 25)
                }
            }
            
            
            Spacer()
            
            VStack{
                Button("다음") {
                    authViewModel.verifyCode { isValid in
                        if isValid {
                            pathModel.paths.append(.AddUser) // 인증 성공, 다음 화면으로
                        }
                        else {
                            tempCodeColor = .red  // 인증 실패, 색상 변경
                            DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                                self.tempCodeColor = .white  // 1.5초 후 색상 원래대로
                            }
                        }
                        
                        
                    }
                }
                .font(.pretendardSemiBold18)
                .frame(minWidth: 0, maxWidth: .infinity)
                .padding()
                .background(Color.homeRed)
                .foregroundColor(.white)
                .cornerRadius(3)
                
                
            }
            .padding()
            
        }.navigationBarBackButtonHidden(true)
    }
}


#Preview {
    AuthNumView()
}
