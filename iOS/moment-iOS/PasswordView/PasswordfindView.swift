//
//  PasswordfindView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/26/24.
//

import SwiftUI
import Foundation

struct PasswordfindView: View {
    @State private var iD: String = ""
    @EnvironmentObject private var pathModel: PathModel
    //@Binding var isDisplayTooltip: Bool
 //   @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        ZStack {
            Color.clear
                .onTapGesture {
                    hideKeyboard()
                }
            VStack{
                Button(action: {
                    // "뒤로" 버튼의 액션: 현재 뷰를 종료
                    pathModel.paths.append(.PasswordView)
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
                
                Spacer()
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
                            .font(.pretendardMedium11)
                            .foregroundColor(.gray600)
                            .padding(.top, 1)
                            .padding(.horizontal, 20)
                        
                        Spacer()
                    }.padding(.bottom,1)
                }
                TextField("복구코드6자리를 입력해 주세요.", text: $iD)
                    .padding()
                    .frame(height: 44)
                    .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                    .padding(.horizontal,20)
                
                HStack{
                    Text("인터넷 상태에 따라 소요시간이 발생할 수 있습니다.")
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
                    Button(
                        action: {
                            pathModel.paths.append(.PasswordinsertView)
                        },
                        label: {
                            Text("다음")
                                .font(.pretendardSemiBold18)
                                .frame(minWidth: 0, maxWidth: .infinity)
                                .padding()
                                .background(Color.homeRed)
                                .foregroundColor(.white)
                                .cornerRadius(3)
                        }
                    )
                }.padding()
                
                
                
            }.navigationBarBackButtonHidden(true)
        }
       
    }
    private func hideKeyboard() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}

#Preview {
    PasswordfindView()
}
