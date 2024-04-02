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
    @State private var selectedButtonTitle: String?
    @State private var isNotificationToggleShownAlert: Bool = false // 토글 표시 상태를 관리하는 변수
    @State private var isNotificationsEnabledAlert: Bool = false // 토글 상태를 관리하는 변수
    @State private var isNotificationToggleShownData: Bool = false // 토글 표시 상태를 관리하는 변수
    @State private var isNotificationsEnabledData: Bool = false // 토글 상태를 관리하는 변수
    
    var body: some View {
        ZStack {
            Color.homeBack.edgesIgnoringSafeArea(.all)
            VStack(spacing: 20) { // 각 항목 사이의 간격 조절
                Spacer().frame(height: 55)
                CustomTitleMainDivider()
                Spacer().frame(height: 20)
               // Spacer().frame(height: 30)
                // "알림설정" 버튼
                
                VStack{
                    SettingButton(title: "알림설정", isSelected: selectedButtonTitle == "알림설정", action: {
                        selectedButtonTitle = "알림설정" // 버튼 선택 시 해당 버튼의 제목을 저장
                        isNotificationToggleShownAlert.toggle()
                    })
                    
                    HStack{
                        if isNotificationToggleShownAlert {
                            CustomToggleAlert(isOn: $isNotificationsEnabledAlert)
                        }
                        Spacer()
                    }
                }
                
                
                            
                
                // "계정 이메일 변경" 버튼
                SettingButton(title: "계정 이메일 설정", isSelected: selectedButtonTitle == "계정 이메일 설정", action: {
                                    selectedButtonTitle = "계정 이메일 설정" // 버튼 선택 시 해당 버튼의 제목을 저장
                                })
                
                // "데이터 허용" 버튼
                VStack(spacing:5){
                    SettingButton(title: "데이터 허용", isSelected: selectedButtonTitle == "데이터 허용", action: {
                        selectedButtonTitle = "데이터 허용" // 버튼 선택 시 해당 버튼의 제목을 저장
                        isNotificationToggleShownData.toggle()
                    })
                    HStack{
                        if isNotificationToggleShownData {
                            CustomToggleData(isOnData: $isNotificationsEnabledData)
                        }
                        Spacer()
                    }
                    HStack{
                        if isNotificationToggleShownData{
                            Text("셀룰러 데이터를 허용하면, 데이터 환경에서도 녹음카드 분석이 가능해요\n허용하지 않으면, WI-FI가 연결된 환경에서만 분석해요")
                                .font(.caption)
                                .foregroundColor(.natural500)
                          
                        }
                      
                    }
                }
                
                SettingButton(title: "버전안내", isSelected: selectedButtonTitle == "버전안내", action: {
                                    selectedButtonTitle = "버전안내" // 버튼 선택 시 해당 버튼의 제목을 저장
                                })
                SettingButton(title: "문의하기", isSelected: selectedButtonTitle == "문의하기", action: {
                                    selectedButtonTitle = "문의하기" // 버튼 선택 시 해당 버튼의 제목을 저장
                                })

                Spacer()
            }
            .padding(.horizontal, 1) // 좌우 패딩
        }
    }
}

struct SettingButton: View {
    let title: String
    var isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack {
                Text(title)
                    .foregroundColor(isSelected ? .homeRed : .primary) // 선택된 버튼의 텍스트 색상을 변경
                    .padding(.bottom, 4)
                    .background(GeometryReader { geometry in
                        // 밑줄 추가
                        Path { path in
                            let width = geometry.size.width + 10
                            let height = geometry.size.height
                            path.move(to: CGPoint(x: -5, y: height))
                            path.addLine(to: CGPoint(x: width - 5, y: height))
                        }
                        .stroke(style: StrokeStyle(lineWidth: 1))
                        .foregroundColor(isSelected ? .homeRed : .black) // 선택된 버튼의 밑줄 색상을 변경
                    })
                Spacer()
            }
        }
        .padding(.vertical, 10)
        .padding(.horizontal, 35)
    }
}

struct CustomToggleAlert: View {
    @Binding var isOn: Bool

    var body: some View {
        HStack(spacing:0) {
            // "켜기" 버튼
            Button(action: {
                self.isOn = true
            }) {
                Text("켜기")
                    .foregroundColor(self.isOn ? .white : .Natural500) // 선택된 상태에 따라 글씨 색 변경
                                        .frame(width: 60, height: 40)
                                        .background(self.isOn ? Color.homeRed : Color.Secondary50)
                                        .cornerRadius(5)
            }

            // "끄기" 버튼
            Button(action: {
                self.isOn = false
            }) {
                Text("끄기")
                    .foregroundColor(!self.isOn ? .Natural500 : .Natural500) // 선택된 상태에 따라 글씨 색 변경
                                        .frame(width: 60, height: 40)
                                        .background(!self.isOn ? Color.Natural300 : Color.Secondary50)
                                        .cornerRadius(5)
            }
        }
        .frame(width: 180, height: 30)
    }
}

struct CustomToggleData: View {
    @Binding var isOnData: Bool

    var body: some View {
        HStack(spacing:0) {
            // "켜기" 버튼
            Button(action: {
                self.isOnData = true
            }) {
                Text("켜기")
                    .foregroundColor(self.isOnData ? .white : .Natural500) // 선택된 상태에 따라 글씨 색 변경
                                        .frame(width: 60, height: 40)
                                        .background(self.isOnData ? Color.homeRed : Color.Secondary50)
                                        .cornerRadius(5)
            }

            // "끄기" 버튼
            Button(action: {
                self.isOnData = false
            }) {
                Text("끄기")
                    .foregroundColor(!self.isOnData ? .Natural500 : .Natural500) // 선택된 상태에 따라 글씨 색 변경
                                        .frame(width: 60, height: 40)
                                        .background(!self.isOnData ? Color.Natural300 : Color.Secondary50)
                                        .cornerRadius(5)
            }
        }
        .frame(width: 180, height: 30)
    }
}

#Preview {
    SettingView()
        .environmentObject(HomeBaseViewModel())
}
