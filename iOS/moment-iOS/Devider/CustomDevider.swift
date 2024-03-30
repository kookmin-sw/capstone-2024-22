//
//  CustomDevider.swift
//  moment-iOS
//
//  Created by 양시관 on 3/13/24.
//
//
//  CustomDevider.swift
//  moment-iOS
//
//  Created by 양시관 on 3/13/24.
//

import Foundation
import SwiftUI

struct CustomHomeVDivider: View {
    var color: Color = .gray600 // 여기에서 색상을 변경할 수 있습니다.
    var width: CGFloat = 335 // 길이 조절
    var thickness: CGFloat = 1 // 굵기 조절
    
    var body: some View {
        Rectangle()
            .fill(color)
            .frame(width: width, height: thickness)
            .edgesIgnoringSafeArea(.horizontal)
    }
}

struct CustomHomeVDividerCard: View {
    var color: Color = .gray600 // 여기에서 색상을 변경할 수 있습니다.
    var width: CGFloat = 312 // 길이 조절
    var thickness: CGFloat = 2 // 굵기 조절
    
    var body: some View {
        Rectangle()
            .fill(color)
            .frame(width: width, height: thickness)
            .edgesIgnoringSafeArea(.horizontal)
    }
}

struct CustomDividerCardView: View {
    var color: Color = .gray600 // 여기에서 색상을 변경할 수 있습니다.
    var width: CGFloat = 340 // 길이 조절
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

struct CustomHomeMainDivider: View {
    var color: Color = .black // 여기에서 색상을 변경할 수 있습니다.
    var width: CGFloat = 300 // 길이 조절
    var thickness: CGFloat = 1 // 굵기 조절
    
    var body: some View {
        Rectangle()
            .fill(color)
            .frame(width: width, height: thickness)
            .edgesIgnoringSafeArea(.horizontal)
    }
}

struct CustomTitleMainDivider: View {
    var color: Color = .gray600 // 여기에서 색상을 변경할 수 있습니다.
    var width: CGFloat = 335 // 길이 조절
    var thickness: CGFloat = 2 // 굵기 조절
    
    var body: some View {
        Rectangle()
            .fill(color)
            .frame(width: width, height: thickness)
            .edgesIgnoringSafeArea(.horizontal)
    }
}
struct CustomHomeMainDividerthick: View {
    var color: Color = .black // 여기에서 색상을 변경할 수 있습니다.
    var width: CGFloat = 335 // 길이 조절
    var thickness: CGFloat = 1.5 // 굵기 조절
    
    var body: some View {
        Rectangle()
            .fill(color)
            .frame(width: width, height: thickness)
            .edgesIgnoringSafeArea(.horizontal)
    }
}

struct CustomEmotionViewDivider: View {
    var color: Color = .gray600 // 여기에서 색상을 변경할 수 있습니다.
    var width: CGFloat = 312 // 길이 조절
    var thickness: CGFloat = 2 // 굵기 조절
    
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
                    .fill(currentPage == index ? Color.gray600 : Color.gray0)
                    .frame(width: 20, height: 3)
                    .transition(.opacity)
            }
        }
        .animation(.easeInOut, value: currentPage)
    }
}
