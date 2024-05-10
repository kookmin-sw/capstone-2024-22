//
//  ReceiptCompleteviewB.swift
//  moment-iOS
//
//  Created by 양시관 on 5/11/24.
//

import Foundation
import SwiftUI


struct ReceiptCompleteviewB: View {
    var receipt: Receipt
    
        @State var currentPage = 0 // 현재 페이지를 나타내는 상태 변수
        var topColor: Color = .homeRed
        var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
        @EnvironmentObject var sharedViewModel: SharedViewModel
        @State private var tripRecord : String = ""
        @State private var tripExplaneStart : String = ""
        @State private var tripnameStart : String = ""
        @State private var tripnameEnd: String = ""
        @State private var tripExplaneEnd : String = ""
        
        
        var body: some View {
            
            VStack(spacing: 0) {
                Spacer().frame(height: 18)
                HStack{
                    
                    Spacer().frame(width: 40)
                    Text(sharedViewModel.tripRecord)
                        .font(.pretendardMedium14)
                        .foregroundColor(.gray500)  // 글씨
                        .multilineTextAlignment(.center)
                    
                    Spacer()
                }
                
                Spacer().frame(height: 70)
                
                
                VStack(alignment:.center,spacing:0){
                    HStack(alignment:.center,spacing:1)
                    {
                        
                        Image("Location")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 19, height: 19)
                        
                        Text(sharedViewModel.tripExplaneStart)
                            .font(.pretendardMedium14)
                            .foregroundColor(.gray600)
                            .multilineTextAlignment(.center)
                    }.frame(maxWidth: .infinity)
                        .padding(.bottom,8)
                    
                    
                    HStack{
                        Text(sharedViewModel.tripnameStart)
                            .font(.pretendardExtrabold45)
                            .foregroundColor(.black)  // 글씨
                            .multilineTextAlignment(.center)
                    }
                    .padding(.bottom,20)
                }
                
                
                Image("Subway")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 40,height: 91)
                    .padding(.bottom,20)
                
                
                
                
                VStack(spacing:0){
                    HStack(alignment: .center,spacing:0)
                    {
                        Image("Location")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 19, height: 19)
                        
                        Text(sharedViewModel.tripExplaneEnd)
                            .font(.pretendardMedium14)
                            .foregroundColor(.gray600)
                            .multilineTextAlignment(.center)
                    }  .padding(.bottom,8)
                    
                    
                    
                    HStack{
                        
                        Text(sharedViewModel.tripnameEnd)
                            .font(.pretendardExtrabold45)
                            .foregroundColor(.black)  // 글씨
                            .multilineTextAlignment(.center)
                    }
                    .padding(.bottom,20)
                    Spacer()
                }
                
                SentimentTrackerView()
                
                
                
                
            }
            // .fill(Color.white) // 전체 색상을 회색으로 설정
            .frame(width: 335, height: 653) // 335,653
            .cornerRadius(5) // 모서리를 둥글게 처리합니다.
            .overlay(
                RoundedRectangle(cornerRadius: 3)
                    .stroke(Color.gray500, lineWidth: 1)
                
            )
            //왼쪽 두꺼운 부분의 사각형
            .overlay(
                
                
                Rectangle() // 색칠할 부분의 모양
                    .fill(Color.homeRed) // 색칠할 색상
                    .frame(width: 30, height: 653), // 색칠할 영역의 크기
                alignment: .leading
                
            ) .overlay(
                Image("LogoVertical") // 다른 이미지 오버레이 추가
                
                    .frame(width: 30, height: 645.23), // 이미지 프레임 설정
                
                
                alignment: .topLeading // 이미지 정렬 설정
            )
            //오른족 얇은 부분의 사각형
            .overlay(
                Rectangle() // 색칠할 부분의 모양
                    .fill(Color.homeRed) // 색칠할 색상
                    .frame(width: 10, height: 596.25), // 색칠할 영역의 크기
                alignment: .init(horizontal: .trailing, vertical: .bottom)
            )
            .overlay(
                Rectangle() // 색칠할 부분의 모양
                    .offset(x:15,y:-270)
                    .fill(Color.homeRed) // 색칠할 색상
                    .frame(width: 305, height: 2.22) // 색칠할 영역의 크기
                
                
                
            )
            .overlay(
                Image("Circledivider")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 466, height: 12)
                    .offset(y:130)
            )
            
            
        }
    
}
