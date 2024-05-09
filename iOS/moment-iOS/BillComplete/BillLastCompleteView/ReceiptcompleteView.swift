//
//  ReceiptcompleteView.swift
//  moment-iOS
//
//  Created by 양시관 on 5/10/24.
//

import Foundation
import SwiftUI



struct ReceiptcompleteView: View {
    var receipt: Receipt
    @EnvironmentObject var sharedViewModel: SharedViewModel
    @Environment(\.presentationMode) var presentationMode
    @State private var isDialogActive = false
    @State private var isDialogActiveBillCom = false
    var topColor: Color = .homeRed
    var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
   // let item : Item
    @State private var text: String = ""
    @State private var starttrip: String = ""
    @State private var StartLocation : String = ""
    @State private var EndLocation : String = ""
    @State private var isEditing: Bool = false
    @State private var saveButtonTitle = "저장"
    @State private var backButtonTitle = "뒤로"
    @State private var inputText: String = ""
    @State private var EndLocationend : String = ""
    @State private var selectedTab = 0
    @State private var forceUpdate = ""
    
    
    var body: some View{
        VStack(spacing: 0) {
            
            
            Rectangle()
                .fill(topColor)
                .frame(height: 50) // 상단 바의 높이를 설정합니다.
                .overlay(
                    HStack{
                        Text("암스테르담 성당 여행") // 여기에 원하는 텍스트를 입력합니다.
                            .foregroundColor(textColor) // 텍스트 색상 설정
                        
                            .font(.pretendardMedium14)
                            .padding()
                        Spacer()
                        Image("Logo")
                            .padding()
                    }
                )
            
            // }
            // 나머지 카드 부분
            Rectangle()
                .fill(Color.Secondary50)
                .frame(height: 603)
                .border(.gray500)
                .overlay(
                    VStack(alignment:.center){
                        
                        Text("티켓이 발행된 날짜는 2024.04.08 입니다 이 티켓이 발행된 날짜는 2024 04 08 입니다 이 ")
                            .font(.pretendardMedium8)
                            .foregroundColor(.homeRed)
                        
                        
                        
                        
                        Spacer()
                        
                        
                        Text(sharedViewModel.text)
                            .foregroundColor(.Natural200)
                            .foregroundColor(.gray500)
                            .font(.pretendardMedium14)
                            .padding(.bottom,30)
                            .multilineTextAlignment(.center)
                        
                        
                        
                        
                        
                        
                        
                        VStack(alignment:.center,spacing:0){
                            HStack(alignment:.center,spacing:1)
                            {
                                
                                Image("Locationred")
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 19, height: 19)
                                
                                
                                
                                Text(sharedViewModel.inputText)
                                    .font(.pretendardMedium14)
                                    .foregroundColor(.homeRed)
                                    .multilineTextAlignment(.center)
                                
                                
                                
                            }
                            .frame(maxWidth: .infinity)
                            
                            
                            
                            HStack{
                                Text(sharedViewModel.StartLocatio)
                                
                                    .font(.pretendardExtrabold45)
                                    .foregroundColor(.homeRed)  // 글씨
                                    .multilineTextAlignment(.center)
                            }
                        }
                        
                        
                        Image("airplane")
                            .padding(.bottom,20)
                        
                        VStack(spacing:0){
                            HStack(alignment: .center,spacing:0)
                            {
                                Image("Locationred")
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 19, height: 19)
                                
                                Text(sharedViewModel.EndLocationend)
                                    .font(.pretendardMedium14)
                                    .foregroundColor(.homeRed)
                            }
                            
                            
                            
                            HStack{
                                
                                Text(receipt.mainDeparture)
                                
                                    .font(.pretendardExtrabold45)
                                    .foregroundColor(.homeRed)  // 글씨
                                    .multilineTextAlignment(.center)
                            }
                        }
                        
                        Spacer()
                        Image("cut")
                            .padding(.bottom,10)
                        
                        StatsView()
                        Spacer()
                    }
                )
        }
        
        
        .frame(width: 335, height: 653)
        
        .cornerRadius(5) // 모서리를 둥글게 처리합니다.
        .overlay(
            RoundedRectangle(cornerRadius: 3)
                .stroke(Color.Secondary50, lineWidth: 1)
        )
        
    }

    

}
