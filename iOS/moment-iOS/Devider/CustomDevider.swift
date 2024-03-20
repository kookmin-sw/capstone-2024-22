//
//  CustomDevider.swift
//  moment-iOS
//
//  Created by 양시관 on 3/13/24.
//

import Foundation
import SwiftUI

struct CustomHomeVDivider: View {
    var color: Color = .black // 여기에서 색상을 변경할 수 있습니다.
    var width: CGFloat = 335 // 길이 조절
    var thickness: CGFloat = 1 // 굵기 조절
    
    var body: some View {
        Rectangle()
            .fill(color)
            .frame(width: width, height: thickness)
            .edgesIgnoringSafeArea(.horizontal)
    }
}

struct CustomHomeSubDivider: View {
    var color: Color = .gray // 여기에서 색상을 변경할 수 있습니다.
    var width: CGFloat = 300 // 길이 조절
    var thickness: CGFloat = 0.5 // 굵기 조절
    
    var body: some View {
        Rectangle()
            .fill(color)
            .frame(width: width, height: thickness)
            .edgesIgnoringSafeArea(.horizontal)
    }
}




struct CustomPageIndicator: View {
    var numberOfPages: Int
    @Binding var currentPage: Int
    
    var body: some View {
        HStack {
            ForEach(0..<numberOfPages, id: \.self) { index in
                Rectangle()
                    .fill(currentPage == index ? Color.black : Color.white)
                    .frame(width: 20, height: 3)
                    .transition(.opacity)
            }
        }
        .animation(.easeInOut, value: currentPage)
    }
}
