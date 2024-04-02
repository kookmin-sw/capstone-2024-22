//
//  SettingView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


import SwiftUI
import SwiftUI

struct SettingView: View {
    @EnvironmentObject private var homeBaseViewModel: HomeBaseViewModel
    
    var body: some View {
        ZStack {
            Color.homeBack.edgesIgnoringSafeArea(.all)
            VStack(spacing: 20) { // 각 항목 사이의 간격 조절
                Spacer().frame(height: 55)
                CustomTitleMainDivider()
                Spacer().frame(height: 20)
               // Spacer().frame(height: 30)
                // "알림설정" 버튼
                
                SettingButton(title: "알림설정", action: {
                    // 알림 설정 변경 액션
                    print("알림설정 변경")
                })
                
                // "계정 이메일 변경" 버튼
                SettingButton(title: "계정 이메일 변경", action: {
                    // 계정 이메일 변경 액션
                    print("계정 이메일 변경")
                })
                
                // "데이터 허용" 버튼
                SettingButton(title: "데이터 허용", action: {
                    // 데이터 허용 변경 액션
                    print("데이터 허용 변경")
                })
                
                // "문의하기" 버튼
                SettingButton(title: "문의하기", action: {
                    // 문의하기 액션
                    print("문의하기")
                })
                
                // "버전안내" 버튼
                SettingButton(title: "버전안내", action: {
                    // 버전안내 액션
                    print("버전안내")
                })
                Spacer()
            }
            .padding(.horizontal, 1) // 좌우 패딩
        }
    }
}

struct SettingButton: View {
    let title: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack {
                Text(title)
                    .foregroundColor(.primary) // 버튼 텍스트 색상 설정
                    .padding(.bottom, 4) // 텍스트와 밑줄 사이의 간격 조정
                    .background(GeometryReader { geometry in
                        // 밑줄을 텍스트 길이에 맞추어 추가
                        Path { path in
                            let width = geometry.size.width + 10
                            let height = geometry.size.height
                            path.move(to: CGPoint(x: -5, y: height))
                            path.addLine(to: CGPoint(x: width - 5, y: height))
                        }
                        .stroke(style: StrokeStyle(lineWidth: 1)) // 밑줄 굵기 설정
                        .foregroundColor(.black) // 밑줄 색상 설정
                    })
                Spacer()
            }
        }
        .padding(.vertical, 10) // 버튼의 상하 패딩 설정
        .padding(.horizontal, 35) // 좌우 패딩 설정
    }
}
  

#Preview {
    SettingView()
        .environmentObject(HomeBaseViewModel())
}
