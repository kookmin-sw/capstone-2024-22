//
//  TimerView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation

import SwiftUI



struct LikeView: View {
    @StateObject var likeViewModel = LikeViewModel()
    
    var body: some View {
       TitleView()
        
    }
}

// MARK: - 타이틀 뷰
private struct TitleView: View {
    fileprivate var body: some View {
        ScrollView{
            VStack {
                
                LikeCardView()
                
            }
        }
    }
}


private struct LikeCardView : View {
    fileprivate var body : some View {
        RoundedRectangle(cornerRadius: 20)
            .fill(Color.white) // Change this to your desired card background color
            .frame(height: 200) // Adjust the height as needed
            .shadow(radius: 10) // Adjust the shadow to your liking
            .overlay(
                Text("여기에 카드 내용을 입력하세요") // Placeholder text
                    .foregroundColor(.gray) // Change this to your desired text color
            )
            .padding(.horizontal, 30) // Same horizontal padding as the title for alignment
    }
}



struct TimerView_Previews: PreviewProvider {
    static var previews: some View {
        LikeView()
    }
}
