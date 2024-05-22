//
//  SettingView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI
import Alamofire

struct SettingView: View {
    @EnvironmentObject private var homeBaseViewModel: HomeBaseViewModel
    @State private var selectedButtonTitle: String? = nil
    @State private var isNotificationToggleShownAlert: Bool = false // 토글 표시 상태를 관리하는 변수
    @State private var isNotificationsEnabledAlert: Bool = false // 토글 상태를 관리하는 변수
    @State private var isNotificationToggleShownData: Bool = false // 토글 표시 상태를 관리하는 변수
    @State private var isNotificationsEnabledData: Bool = false // 토글 상태를 관리하는 변수
    @State private var isNotificationsEnabledVersion : Bool = false
    @State private var isShowEmail : Bool = false
    @Binding var showDialog : Bool
    @Binding var showDialogGoodbye : Bool
    @State private var firebaseToken: String = "c9cnFm9_nECdu25ruUK0Cp:APA91bHD_BbbL7FAlu91S1u4BZRK0JXcueZIH8343CXCmuAX6mjkYShohze7GuuwyLlwqdjuIHh_0e-MFSGnqd1Mv83dWMDySamauZ0uPoHb3bVz1FAdmPherPcVDqND8AKUDOCHxMY-"

    var body: some View {
        ZStack {
            Color.homeBack.edgesIgnoringSafeArea(.all)
                .onTapGesture {
                    self.showDialog = false
                    self.showDialogGoodbye = false
                }
            
            VStack(spacing: 20) { // 각 항목 사이의 간격 조절
                Spacer().frame(height: 85)
                CustomTitleMainDivider()
                Spacer().frame(height: 20)
                
                VStack {
                    SettingButton(title: "알림설정", isSelected: selectedButtonTitle == "알림설정") {
                        withAnimation {
                            selectedButtonTitle = selectedButtonTitle == "알림설정" ? nil : "알림설정"
                        }
                    }
                    
                    HStack {
                        if selectedButtonTitle == "알림설정" {
                            CustomToggleAlert(isOn: $isNotificationsEnabledAlert)
                        }
                        Spacer()
                    }
                    
                    SettingButton(title: "데이터 허용", isSelected: selectedButtonTitle == "데이터 허용") {
                        withAnimation {
                            selectedButtonTitle = selectedButtonTitle == "데이터 허용" ? nil : "데이터 허용"
                        }
                    }
                    
                    HStack {
                        if selectedButtonTitle == "데이터 허용" {
                            CustomToggleData(isOnData: $isNotificationsEnabledData)
                        }
                        Spacer()
                    }
                    .padding(.bottom, 2)
                    
                    HStack {
                        Spacer().frame(width: 27)
                        if selectedButtonTitle == "데이터 허용" {
                            Text("셀룰러 데이터를 허용하면, 데이터 환경에서도 녹음카드 분석이 가능해요\n허용하지 않으면, WI-FI가 연결된 환경에서만 분석해요")
                                .font(.pretendardMedium11)
                                .foregroundColor(.natural500)
                            Spacer().frame(width: 25)
                        }
                    }
                    
                    SettingButton(title: "버전안내", isSelected: selectedButtonTitle == "버전안내") {
                        selectedButtonTitle = selectedButtonTitle == "버전안내" ? nil : "버전안내"
                        isNotificationsEnabledVersion.toggle()
                    }
                    if selectedButtonTitle == "버전안내" {
                        HStack {
                            Spacer().frame(width: 35)
                            Text("V 1.1")
                                .font(.pretendardMedium14)
                            Spacer().frame(width: 30)
                            Text("가장 최신버전이에요")
                                .font(.pretendardMedium11)
                            Spacer()
                                .padding(.horizontal, 30)
                        }
                    }
                    
                    SettingButton(title: "문의하기", isSelected: selectedButtonTitle == "문의하기") {
                        selectedButtonTitle = selectedButtonTitle == "문의하기" ? nil : "문의하기"
                        isShowEmail.toggle()
                    }
                    if selectedButtonTitle == "문의하기" {
                        VStack(alignment: .leading, spacing: 8) {
                            HStack {
                                Spacer().frame(width: 12)
                                Text("kookmin@gmail.com")
                                    .font(.pretendardMedium14)
                                    .padding(5)
                                    .background(Color.customSky)
                                    .cornerRadius(5)
                                    .tint(.black)
                                Spacer()
                            }
                            HStack {
                                Spacer().frame(width: 12)
                                Text("앗! 사용하시면서 불편한 점이 있으시다구요?\n이메일로 보내주시면 친절히 답변해드릴게요")
                                    .font(.pretendardMedium11)
                                    .foregroundColor(.gray)
                                    .multilineTextAlignment(.leading)
                                Spacer()
                            }
                        }
                        .padding(.leading)
                    }
                    
                    Spacer()
                    
                    VStack(spacing: 20) {
                        Button(action: {
                            self.showDialog = true
                            print("로그아웃 처리")
                        }) {
                            HStack {
                                Text("로그아웃")
                                    .font(.pretendardMedium14)
                                    .foregroundColor(.black)
                                    .padding(.leading, 16)
                                Spacer()
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        
                        Button(action: {
                            print("탈퇴 처리")
                            self.showDialogGoodbye = true
                        }) {
                            HStack {
                                Text("탈퇴하기")
                                    .font(.pretendardMedium14)
                                    .foregroundColor(.black)
                                    .padding(.leading, 16)
                                Spacer()
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        Spacer().frame(height: 40)
                    }
                    .padding()
                    
//                    Button(action: {
//                        updateUserSettings()
//                    }) {
//                        Text("Update Settings")
//                            .font(.pretendardMedium14)
//                            .padding()
//                            .background(Color.blue)
//                            .foregroundColor(.white)
//                            .cornerRadius(8)
//                    }
                }
                .padding(.horizontal, 1)
                
                if showDialog {
                    LogoutDialog(
                        isActive: $showDialog,
                        title: "정말 로그아웃 하시나요?",
                        message: "",
                        yesAction: {
                            showDialog = false
                        },
                        noAction: {
                            showDialog = false
                        }
                    )
                    .transition(.opacity)
                    .zIndex(1)
                } else if showDialogGoodbye {
                    GoodbyeDialog(
                        isActive: $showDialogGoodbye,
                        title: "정말 탈퇴 하시나요..?",
                        message: "새로운 계정으로 만나는건 가능하지만\n지금까지의 우리 추억은 모두 사라져요",
                        yesAction: {
                            showDialogGoodbye = false
                        },
                        noAction: {
                            showDialogGoodbye = false
                        }
                    )
                    .transition(.opacity)
                    .zIndex(1)
                }
            }
        }
    }
    
    func updateUserSettings() {
        let settings = UserSettings(notification: isNotificationsEnabledAlert, dataUsage: isNotificationsEnabledData, firebaseToken: firebaseToken)
        UserService.shared.updateUserSettings(userId: 1, settings: settings) { result in
            switch result {
            case .success(let success):
                if success {
                    print("Settings updated successfully")
                } else {
                    print("Failed to update settings")
                }
            case .failure(let error):
                print("Error: \(error.localizedDescription)")
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
    
    var body: some View {
        ZStack{
            Color(showDialog ? .black : .black)
                .opacity(showDialog ? 0.5 : 0.5) // 배경색 투명도 조정
                .edgesIgnoringSafeArea(.all)
                .animation(.easeInOut, value: showDialog)
            
            VStack {
                Spacer()
                HStack {
                    Text("정말")
                    Text("로그아웃")
                        .foregroundColor(.homeRed)
                    Text("하시나요?")
                }
                .font(.pretendardExtrabold16)
       
                .padding()
                
                
                
                HStack { // 버튼 사이 간격을 0으로 조정
                    Button {
                        yesAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("네")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 38) // 버튼의 크기 조절
                    }
                    
                    Rectangle() // 빨간색 세로줄 추가
                        .fill(Color.gray500)
                        .frame(width: 2, height: 20)
                    
                    Button {
                        noAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("아니요")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 38) // 버튼의 크기 조절
                    }
                }.padding(.bottom,10)
                
                Spacer()
            }
            .frame(width: 280, height: 114) // 다이얼로그의 크기 조절
            .background(.white)
            .clipShape(RoundedRectangle(cornerRadius: 0))
        }
    }
    
    func close() {
        withAnimation(.spring()) {
            isActive = false
        }
    }
}



struct GoodbyeDialog: View {
    @State private var showDialogGoodbye = false
    @Binding var isActive: Bool
    
    let title: String
    let message: String
    let yesAction: () -> Void
    let noAction: () -> Void
    @State private var offset: CGFloat = 1000
    var body: some View {
        
        ZStack{
            Color(showDialogGoodbye ? .black : .black)
                .opacity(showDialogGoodbye ? 1.0 : 0.5)
                .edgesIgnoringSafeArea(.all)
                .animation(.easeInOut, value: showDialogGoodbye)
            
            VStack {
                Spacer()
                
                HStack{
                    Text("정말")
                    Text("탈퇴")
                        .foregroundColor(.homeRed)
                    Text("하시나요..?")
                }
                    .font(.pretendardExtrabold16)
                    
                    .padding()
                
                Text(message)
                    .font(.pretendardMedium14)
                    .foregroundColor(.gray500)
                    .padding(.bottom)
                
                HStack { // 버튼 사이 간격을 0으로 조정
                    Button {
                        yesAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("네")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 38) // 버튼의 크기 조절
                    }
                    
                    Rectangle() // 빨간색 세로줄 추가
                        .fill(Color.gray500)
                        .frame(width: 2, height: 20)
                    
                    Button {
                        noAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("아니요")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 38) // 버튼의 크기 조절
                    }
                }.padding(.bottom,10)
                // Spacer()
            }
            .frame(width: 280, height: 178) // 다이얼로그의 크기 조절
            
            .background(.white)
            .clipShape(RoundedRectangle(cornerRadius: 0))
            
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
        Button(action: {
            action() // 상위 뷰에서 정의한 액션 실행
            // isSelected = true // 현재 버튼을 선택된 상태로 만듬
        }) {
            HStack {
                Text(title)
                    .font(.pretendardMedium16) // .pretendardMedium16 대신 사용한 예시
                    .foregroundColor(isSelected ? .homeRed : .primary) // .homeRed 대신 사용한 예시
                    .padding(.bottom, 4)
                    .background(GeometryReader { geometry in
                        Path { path in
                            let width = geometry.size.width + 10
                            let height = geometry.size.height
                            path.move(to: CGPoint(x: -5, y: height))
                            path.addLine(to: CGPoint(x: width - 5, y: height))
                        }
                        .stroke(style: StrokeStyle(lineWidth: 1))
                        .foregroundColor(isSelected ? .homeRed : .black) // .homeRed 대신 사용한 예시
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
                    .font(.pretendardMedium14)
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
                    .font(.pretendardMedium14)
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
                    .font(.pretendardMedium14)
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
                    .font(.pretendardMedium14)
                    .foregroundColor(!self.isOnData ? .Natural500 : .Natural500) // 선택된 상태에 따라 글씨 색 변경
                    .frame(width: 60, height: 40)
                    .background(!self.isOnData ? Color.Natural300 : Color.Secondary50)
                    .cornerRadius(5)
            }
        }
        .frame(width: 180, height: 30)
    }
}

