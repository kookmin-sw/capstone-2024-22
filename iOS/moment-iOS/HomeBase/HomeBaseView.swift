//
//  HomeView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI

//
//  VoiceRecorderViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//



struct HomeBaseView: View {
    @EnvironmentObject private var pathModel: PathModel
    var edges = UIApplication.shared.windows.first?.safeAreaInsets
    @StateObject private var homeBaseViewModel = HomeBaseViewModel()
    @Namespace var animation // 탭 전환 애니메이션을 위한 네임스페이스
    @State private var showPartialSheet = false // 커스텀 시트 표시 여부

    var body: some View {
        ZStack {
            VStack {
                Spacer() // 상단 컨텐츠를 위한 공간

                // 현재 선택된 탭에 따라 표시되는 뷰
                ZStack {
                    switch homeBaseViewModel.selectedTab {
                    case .Home:
                        HomeView()
                    case .Bill:
                        BillListView()
                    case .voiceRecorder:
                        VoiceRecorderView()
                    case .Like:
                        LikeView()
                    case .setting:
                        SettingView()
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)

                // 커스텀 탭 바
                ZStack(alignment: .bottom) {
                    Rectangle()
                        .fill(Color.black) // 구분선 색상 설정
                        .frame(width: UIScreen.main.bounds.width, height: 1) // 화면 너비에 맞춘 구분선
                        .offset(y: -edges!.bottom - 70) // 구분선 조정
                    
                    HStack(spacing: 0) {
                        ForEach(Tab.allCases, id: \.self) { tab in
                            TabButton(title: tab, selectedTab: $homeBaseViewModel.selectedTab, animation: animation, isRecording: $homeBaseViewModel.isRecording)
                            
                            if tab != Tab.allCases.last {
                                Spacer(minLength: 0) // 탭 버튼 사이의 공간
                            }
                        }
                    }
                    .padding(.horizontal, 30)
                    .padding(.bottom, edges?.bottom == 0 ? 15 : edges!.bottom)

                    // 중앙 이미지 추가
                    Image("Recode") // 중앙 버튼 이미지
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 50, height: 50)
                        .offset(y: -edges!.bottom - 40) // 이미지 위치 조정
                }
                .background(Color.homeBack) // 탭 바 배경색
            }
            .edgesIgnoringSafeArea(.bottom)
            .background(Color.homeBack.ignoresSafeArea(.all, edges: .all))
            
            if showPartialSheet {
                  Color.black.opacity(0.5) // 어두운 배경 적용
                      .edgesIgnoringSafeArea(.all)
                      .onTapGesture {
                          withAnimation {
                              showPartialSheet = false // 어두운 배경을 탭하면 시트 닫기
                          }
                      }
              }

            // 커스텀 시트 부분
            if showPartialSheet {
                BottomSheetView1(isPresented: $showPartialSheet)
            }
        }
        .onChange(of: homeBaseViewModel.isRecording) { newValue in
            withAnimation {
                showPartialSheet = newValue
            }
        }
    }
}

struct BottomSheetView1: View {
    @Binding var isPresented: Bool
    @State private var timeElapsed = 0
    @State private var timerRunning = false
    @State private var recordBtn = false
    @State private var tooltipOpacity = 1.0
    let timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()

    var body: some View {
        VStack {
            Spacer()

            VStack {
               
                Spacer()
                ZStack{
                    
                    CustomTriangleShapeLeftdown()
                        .fill(.gray500)
                        .frame(width: 24, height: 12) // 삼각형 크기 지정
                        .offset(x: 10, y: 53)
                    
                    
                    CustomRectangleShapeLeftdown(text: "녹음은 한번에 최대 10분까지 가능해요 \n최대시간을 넘어가면 자동 종료 후 저장됩니다")
                        .multilineTextAlignment(.center)
                        .frame(width: 340, height: /*@START_MENU_TOKEN@*/100/*@END_MENU_TOKEN@*/)
                       
                }
                .opacity(tooltipOpacity)  // 투명도 적용
                                       .onAppear {
                                           // 5초 후에 투명도를 0으로 변경하여 툴팁을 서서히 사라지게 함
                                           DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
                                               withAnimation(.easeOut(duration: 2.0)) {
                                                   tooltipOpacity = 0
                                               }
                                           }
                                       }
                
                
                Text("\(timeString(time: timeElapsed))")
                    .font(.caption)
                    .padding()
                    .frame(height: 44)
                    .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                    .padding(.horizontal,15)
                
                    .onReceive(timer) { _ in
                        if timerRunning {
                            timeElapsed += 1
                        }
                    }
                    .onAppear {
                        // 시트가 나타날 때 타이머 시작
                        timerRunning = true
                    }
                    .onDisappear {
                        // 시트가 사라질 때 타이머 중지
                        timerRunning = false
                        timeElapsed = 0  // 시간 초기화
                    }

                Text("열심히 듣고 있어요")  // 여기에 원하는 텍스트를 추가하세요.
                   
                    .frame(height: 44)
                    .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                    .padding(.horizontal,20)
                

                // 녹음 버튼
                Button(action: {
                    recordBtn.toggle()
                    timerRunning.toggle()  // 버튼을 눌러 타이머 시작/정지
                }) {
                    Image(systemName: recordBtn ? "stop.fill" : "record.circle")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 50, height: 50)
                        .foregroundColor(.red)
                        .padding(.bottom, 10)
                }
                    .padding()

            }
            .frame(maxWidth: .infinity, maxHeight: 284)

            .background(Color.homeBack) // 시트의 배경색
        }
        .transition(.move(edge: .bottom)) // 하단에서 올라오는 애니메이션 효과
        .onTapGesture {
            withAnimation {
                isPresented = false // 시트 외부를 탭하면 시트를 닫습니다.
            }
        }
    }

    func timeString(time: Int) -> String {
        let minutes = time / 60 % 60
        let seconds = time % 60
        return String(format:"%02i:%02i", minutes, seconds)
    }

}



// 탭 버튼 컴포넌트
struct TabButton: View {
    let title: Tab
    @Binding var selectedTab: Tab
    var animation: Namespace.ID
    @Binding var isRecording: Bool
    
    var body: some View {
        Button(action: {
                   if title == .voiceRecorder {
                       // 녹음 버튼이 눌렸을 때의 동작
                       isRecording.toggle() // 녹음 상태 토글
                       print("녹음시작")
                   } else {
                       withAnimation { selectedTab = title }
                   }
               }) {
            VStack(spacing: 1) {
                // 상단 인디케이터
                ZStack {
                    CustomShape()
                        .fill(Color.clear)
                        .frame(width: 45, height: 6)
                    
                    if selectedTab == title {
                        CustomShape()
                            .fill(Color.homeRed) // 선택된 탭의 색상
                            .frame(width: 45, height: 6)
                            .matchedGeometryEffect(id: "Tab_Change", in: animation)
                    }
                }
                .padding(.bottom, 3)
                
                // 아이콘 및 텍스트
                Image(selectedTab == title ? title.selectedIconName : title.unselectedIconName)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 28, height: 45)
                    .foregroundColor(selectedTab == title ? .blue : .gray)
                
                Text(title.title)
                    .font(.caption)
                    .fontWeight(.bold)
                    .foregroundColor(selectedTab == title ? .black : .gray)
            }
            .frame(maxWidth: .infinity)
        }
    }
}


struct CustomShape: Shape {
    
    func path(in rect: CGRect) -> Path {
        
        let path = UIBezierPath(roundedRect: rect, byRoundingCorners: [.bottomLeft,.bottomRight], cornerRadii: CGSize(width: 10, height: 15))
        
        return Path(path.cgPath)
    }
}





//struct HomeBaseView: View {
//    @EnvironmentObject private var pathModel: PathModel
//    @StateObject private var homeBaseViewModel = HomeBaseViewModel()
//
//    var body: some View {
//        ZStack { // 가장 바깥쪽 VStack 추가
//            TabView(selection: $homeBaseViewModel.selectedTab) {
//                HomeView()
//                    .tabItem {
//                        Image(homeBaseViewModel.selectedTab == .Home ? "Home_Se" : "Home_Un")
//                    }
//                    .tag(Tab.Home)
//
//                BillListView()
//                    .tabItem {
//                        Image(homeBaseViewModel.selectedTab == .Bill ? "Bill_Se" : "Bill_Un")
//                    }
//                    .tag(Tab.Bill)
//
//                VoiceRecorderView()
//                    .tabItem {
//                        Image("Recode") // 변경할 필요가 없으므로 하나의 이미지만 사용
//                    }
//                    .tag(Tab.voiceRecorder)
//
//                LikeView()
//                    .tabItem {
//                        Image(homeBaseViewModel.selectedTab == .Like ? "Like_Se" : "Like_Un")
//                    }
//                    .tag(Tab.Like)
//
//                SettingView()
//                    .tabItem {
//                        Image(homeBaseViewModel.selectedTab == .setting ? "Setting_Se" : "Setting_Un")
//                    }
//                    .tag(Tab.setting)
//            }
//            .environmentObject(homeBaseViewModel) // 하위 뷰들이 homeBaseViewModel에 접근할 수 있게 합니다.
//
//            SeperatorLineView(selectedTab: $homeBaseViewModel.selectedTab) // 인디케이터 추가
//                .padding(.top, -5) // 필요에 따라 여백 조절
//        }
//    }
//}
//
//
//private struct SeperatorLineView: View {
//    @Binding var selectedTab: Tab
//
//    private var indicatorPosition: CGFloat {
//        switch selectedTab {
//        case .Home:
//            return 30 // Home 탭 위치
//        case .Bill:
//            return 90 // Bill 탭 위치
//        case .voiceRecorder:
//            return 150 // voiceRecorder 탭 위치
//        case .Like:
//            return 210 // Like 탭 위치
//        case .setting:
//            return 270 // setting 탭 위치
//        }
//    }
//
//    var body: some View {
//        VStack {
//            Spacer()
//            ZStack(alignment: .bottomLeading) { // 변경: TopLeading -> BottomLeading
//                Rectangle()
//                    .fill(Color.clear)
//                    .frame(height: 2)
//                Rectangle()
//                    .fill(Color.red)
//                    .frame(width: 30, height: 2) // 인디케이터 크기 설정
//                    .offset(x: indicatorPosition, y: 0) // 변경: 인디케이터 위치 조정 없음, 바텀으로 변경
//            }
//            .frame(height: 2) // Seperator 높이 설정
//
//            Rectangle()
//                .fill(LinearGradient(gradient: Gradient(colors: [Color.black]), startPoint: .top, endPoint: .bottom))
//                .frame(height: 0.5)
//        }
//        .padding(.bottom, 60)
//    }
//}

