//
//  PasswordView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/22/24.
//

import SwiftUI

struct PasswordView: View {
    @State private var iD: String = ""
    @EnvironmentObject private var pathModel: PathModel
    var body: some View {
        VStack{
            Spacer().frame(height: 240)
            HStack(alignment: .bottom) {
                Text("아이디")
                    .foregroundColor(.gray600)
                    .font(.pretendardMedium11)
                    .padding(.top, 1)
                    .padding(.horizontal, 20)
                
                Spacer()
            }.padding(.bottom,1)
            
            TextField("인증 가능한 이메일을 입력하세요. ", text: $iD)
                .padding()
                .frame(height: 44)
                .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                .padding(.horizontal,20)
            
            // Text and Spacer
            HStack {
                Text("해당 이메일로 비밀번호 초기화 코드가 발송됩니다.")
                    .font(.pretendardMedium11)
                    .foregroundColor(.gray600)
                    .padding(.top, 3)
                    .fixedSize(horizontal: false, vertical: true)
                Spacer()
            }
            .padding([.bottom, .horizontal], 20)
            
            Spacer()
            HStack{
            Button(
                action: {
                    pathModel.paths.append(.PasswordfindView)
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
            
        }
    }
}

#Preview {
    PasswordView()
}
