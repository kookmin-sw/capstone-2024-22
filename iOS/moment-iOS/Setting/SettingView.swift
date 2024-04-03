//
//  SettingView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI

struct SettingView: View {
    @EnvironmentObject private var homeBaseViewModel: HomeBaseViewModel
    @State private var selectedButtonTitle: String?
    @State private var isNotificationToggleShownAlert: Bool = false // 토글 표시 상태를 관리하는 변수
    @State private var isNotificationsEnabledAlert: Bool = false // 토글 상태를 관리하는 변수
    @State private var isNotificationToggleShownData: Bool = false // 토글 표시 상태를 관리하는 변수
    @State private var isNotificationsEnabledData: Bool = false // 토글 상태를 관리하는 변수
    @State private var isNotificationsEnabledVersion : Bool = false
    @State private var isShowEmail : Bool = false
    @Binding var showDialog : Bool
    
    
    var body: some View {
        ZStack {
            Color.homeBack.edgesIgnoringSafeArea(.all)
            
           
            VStack(spacing: 20) { // 각 항목 사이의 간격 조절
                Spacer().frame(height: 85)
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
                
                
                // "데이터 허용" 버튼
                VStack{
                    SettingButton(title: "데이터 허용", isSelected: selectedButtonTitle == "데이터 허용", action: {
                        selectedButtonTitle = "데이터 허용" // 버튼 선택 시 해당 버튼의 제목을 저장
                        isNotificationToggleShownData.toggle()
                    })
                    HStack{
                        if isNotificationToggleShownData {
                            CustomToggleData(isOnData: $isNotificationsEnabledData)
                        }
                        Spacer()
                    }.padding(.bottom,10)
                    HStack{
                        if isNotificationToggleShownData{
                            Text("셀룰러 데이터를 허용하면, 데이터 환경에서도 녹음카드 분석이 가능해요\n허용하지 않으면, WI-FI가 연결된 환경에서만 분석해요")
                                .font(.caption)
                                .foregroundColor(.natural500)
                            Spacer().frame(width: 25)
                        }
                      
                    }
                }
                VStack{
                    
                    SettingButton(title: "버전안내", isSelected: selectedButtonTitle == "버전안내", action: {
                        selectedButtonTitle = "버전안내" // 버튼 선택 시 해당 버튼의 제목을 저장
                        isNotificationsEnabledVersion.toggle()
                    })
                    HStack{
                        Spacer().frame(width: 35)
                        if isNotificationsEnabledVersion{
                            Text("V 1.1")
                                
                            Spacer().frame(width: 30)
                            Text("가장 최신버전이에요")
                                .font(.caption)
                        }
                        Spacer()
                            .padding(.horizontal,30)
                    }
                }
                VStack{
                    SettingButton(title: "문의하기", isSelected: selectedButtonTitle == "문의하기", action: {
                        selectedButtonTitle = "문의하기" // 버튼 선택 시 해당 버튼의 제목을 저장
                        isShowEmail.toggle()
                    })
                //    HStack{
                       
                    if isShowEmail {
                        VStack(alignment: .leading, spacing: 8) {
                            
                            HStack{
                                Spacer().frame(width: 12)
                                Text("kookmin@gmail.com")
                                    .font(.headline) // 이메일 주소에 대한 폰트 스타일 조정
                                Spacer()
                                
                            }
                            HStack{
                                Spacer().frame(width: 12)
                                Text("앗! 사용하시면서 불편한 점이 있으시다구요?\n이메일로 보내주시면 친절히 답변해드릴게요")
                                    .font(.caption) // 설명 텍스트에 대한 폰트 스타일 조정
                                    .foregroundColor(.gray) // 설명 텍스트의 색상 조정
                                    .multilineTextAlignment(.leading) // 텍스트를 왼쪽 정렬
                                Spacer()
                            }
                        }
                        .padding(.leading) // 왼쪽 패딩 추가로 내용을 좌측에 정렬
                    }
                        
                   // }
                    
                }

                Spacer()
                VStack(spacing: 20) {
                    Button(action: {
                        // 로그아웃 로직을 여기에 작성하세요
                        self.showDialog = true
                        print("로그아웃 처리")
                    }) {
                        HStack {
                            Text("로그아웃")
                                .foregroundColor(.blue) // 텍스트 색상을 지정할 수 있습니다.
                                .padding(.leading, 16) // 왼쪽 패딩을 추가하여 여백을 조정하세요.
                            Spacer() // 나머지 공간을 채워서 텍스트를 왼쪽으로 밀어냅니다.
                        }
                        .frame(maxWidth: .infinity, alignment: .leading) // HStack을 최대 너비로 설정하고 왼쪽 정렬
                    }

                    Button(action: {
                        // 탈퇴하기 로직을 여기에 작성하세요
                        print("탈퇴 처리")
                    }) {
                        HStack {
                            Text("탈퇴하기")
                                .foregroundColor(.red) // 텍스트 색상을 지정할 수 있습니다.
                                .padding(.leading, 16) // 왼쪽 패딩을 추가하여 여백을 조정하세요.
                            Spacer() // 나머지 공간을 채워서 텍스트를 왼쪽으로 밀어냅니다.
                        }
                        .frame(maxWidth: .infinity, alignment: .leading) // HStack을 최대 너비로 설정하고 왼쪽 정렬
                    }
                    Spacer().frame(height: 40)
                }
                .padding() // VStack에 대한 패딩 추가로 전체적인 여백

               
                           
                 

            }
            .padding(.horizontal, 1) // 좌우 패딩
            
            if showDialog {
                
                LogoutDialog(
                    isActive: $showDialog,
                    title: "로그아웃하시겠습니까?",
                    message: "",
                    yesAction: {
                       
                        showDialog = false
                    },
                    noAction: {
                        showDialog = false // 다이얼로그 닫기
                        
                    }
                )
                .transition(.opacity) // 다이얼로그 등장과 사라짐에 투명도 변화 적용
                .zIndex(1) // 다이얼로그가 다른 요소들 위에 오도록 설정
            }
        }
       
    }
}

struct LogoutDialog: View {
    @State private var showDialog = false
    @Binding var isActive: Bool
    
    let title: String
    let message: String
    let yesAction: () -> Void
    let noAction: () -> Void
    @State private var offset: CGFloat = 1000
    var body: some View {
        
        ZStack{
            Color(showDialog ? .black : .black)
                .opacity(showDialog ? 1.0 : 0.5)
                .edgesIgnoringSafeArea(.all)
                .animation(.easeInOut, value: showDialog)
            
            VStack {
                Text(title)
                    .font(.title2)
                    .bold()
                    .padding()
                
                Text(message)
                    .font(.body)
                    .padding(.bottom)
                
                HStack {
                    Button {
                        yesAction()
                        close()
                    } label: {
                        ZStack {
                            RoundedRectangle(cornerRadius: 20)
                                .foregroundColor(.green)
                            Text("네")
                                .font(.system(size: 16, weight: .bold))
                                .foregroundColor(.white)
                                .padding()
                        }
                        .frame(width: 120, height: 44) // 버튼의 크기 조절
                    }
                    .padding()
                    
                    Button {
                        noAction()
                        close()
                    } label: {
                        ZStack {
                            RoundedRectangle(cornerRadius: 20)
                                .foregroundColor(.gray)
                            Text("아니요")
                                .font(.system(size: 16, weight: .bold))
                                .foregroundColor(.white)
                                .padding()
                        }
                        .frame(width: 120, height: 44) // 버튼의 크기 조절
                    }
                    .padding()
                }
                
            }
            .fixedSize(horizontal: false, vertical: true)
            .padding()
            .background(.white)
            .clipShape(RoundedRectangle(cornerRadius: 20))
            
        }
        
        // .ignoresSafeArea()
    }
    
    func close() {
        withAnimation(.spring()) {
            offset = 1000
            isActive = false
        }
    }
}


struct GoodbyeDialog: View {
    @State private var showDialog = false
    @Binding var isActive: Bool
    
    let title: String
    let message: String
    let yesAction: () -> Void
    let noAction: () -> Void
    @State private var offset: CGFloat = 1000
    var body: some View {
        
        ZStack{
            Color(showDialog ? .black : .black)
                .opacity(showDialog ? 1.0 : 0.5)
                .edgesIgnoringSafeArea(.all)
                .animation(.easeInOut, value: showDialog)
            
            VStack {
                Text(title)
                    .font(.title2)
                    .bold()
                    .padding()
                
                Text(message)
                    .font(.body)
                    .padding(.bottom)
                
                HStack {
                    Button {
                        yesAction()
                        close()
                    } label: {
                        ZStack {
                            RoundedRectangle(cornerRadius: 20)
                                .foregroundColor(.green)
                            Text("네")
                                .font(.system(size: 16, weight: .bold))
                                .foregroundColor(.white)
                                .padding()
                        }
                        .frame(width: 120, height: 44) // 버튼의 크기 조절
                    }
                    .padding()
                    
                    Button {
                        noAction()
                        close()
                    } label: {
                        ZStack {
                            RoundedRectangle(cornerRadius: 20)
                                .foregroundColor(.gray)
                            Text("아니요")
                                .font(.system(size: 16, weight: .bold))
                                .foregroundColor(.white)
                                .padding()
                        }
                        .frame(width: 120, height: 44) // 버튼의 크기 조절
                    }
                    .padding()
                }
                
            }
            .fixedSize(horizontal: false, vertical: true)
            .padding()
            .background(.white)
            .clipShape(RoundedRectangle(cornerRadius: 20))
            
        }
        
        // .ignoresSafeArea()
    }
    
    func close() {
        withAnimation(.spring()) {
            offset = 1000
            isActive = false
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

