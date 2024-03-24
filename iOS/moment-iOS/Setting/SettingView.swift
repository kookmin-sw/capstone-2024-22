//
//  SettingView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


import SwiftUI

struct SettingView: View {
    @EnvironmentObject private var homeBaseViewModel : HomeBaseViewModel
    
    var color: Color = .gray600 // 여기에서 색상을 변경할 수 있습니다.
    var width: CGFloat = 350 // 길이 조절
    var thickness: CGFloat = 1 // 굵기 조절
    
    var body: some View {
        //타이틀뷰
        ZStack {
            Color.homeBack.edgesIgnoringSafeArea(.all)
            VStack(spacing:30){
                Rectangle()
                    .fill(color)
                    .frame(width: width, height: thickness)
                    .edgesIgnoringSafeArea(.horizontal)
                    .padding(.top,50)
                
                HStack{
                    Text("알림설정")
                        .padding(.horizontal,35)
                        .padding(.top,20)
                    Spacer()
                }
                
                HStack{
                    Text("계정 이메일 변경")
                        .padding(.horizontal,35)
                    Spacer()
                }
                HStack{
                    Text("데이터 허용")
                        .padding(.horizontal,35)
                    Spacer()
                }
                HStack{
                    Text("문의하기")
                        .padding(.horizontal,35)
                    Spacer()
                }
                HStack{
                    Text("버전안내")
                        .padding(.horizontal,35)
                    Spacer()
                }
                 
                    
                
                Spacer()
            }
        }
    
      
    }
}



  

#Preview {
    SettingView()
        .environmentObject(HomeBaseViewModel())
}
