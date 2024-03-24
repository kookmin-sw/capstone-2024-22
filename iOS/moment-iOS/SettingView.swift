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
    @StateObject var audioRecorderManager = AudioRecorderManager()
    var body: some View {
        //타이틀뷰
        VStack{
            Text("알림설정")
                .padding()
            Text("문의하기")
                .padding()
            Text("버전안내")
                .padding()
            Text("이동통신망 사용알림")
                .padding()
            
            
        }
    
      
    }
}

#Preview {
    SettingView()
        .environmentObject(HomeBaseViewModel())
}
