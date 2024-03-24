//
//  HomeView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI
import PopupView

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
    @ObservedObject  var audioRecorderManager: AudioRecorderManager
    @State var isPresentedFloating: Bool = false
    @State private var wasDeleted = false
    
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
                BottomSheetView1(isPresented: $showPartialSheet, audioRecorderManager: audioRecorderManager, wasDeleted: $wasDeleted)
            }
        }
        .onChange(of: homeBaseViewModel.isRecording) { newValue in
            withAnimation {
                showPartialSheet = newValue
            }
        }
        .onChange(of: wasDeleted) { newValue in
                    if newValue {
                        // 토스트 메시지를 표시하는 로직
                        withAnimation {
                            isPresentedFloating = true
                        }
                        // 3초 후 토스트 메시지 숨김
                        DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                            isPresentedFloating = false
                            wasDeleted = false // 상태 초기화
                        }
                    }
                }
                // PopupView 토스트 메시지 구성
                .popup(isPresented: $isPresentedFloating) {
                    FloatingToastView() // 사용자 정의 토스트 뷰
                } customize: {
                    $0.type(.floater())
                      .position(.bottom)
                      .autohideIn(3.0)
                      .animation(.spring())
                      .closeOnTapOutside(true)
                      .backgroundColor(.clear)
                }
    }
}

struct BottomSheetView1: View {
    @Binding var isPresented: Bool
    @State private var timeElapsed = 0
    @State private var timerRunning = false
    @State private var recordBtn = false
    @State private var tooltipOpacity = 1.0
    //@StateObject private var voiceRecorderViewModel = VoiceRecorderViewModel()
    @ObservedObject  var audioRecorderManager: AudioRecorderManager
    @State var isPresentedFloating : Bool = false
    @Binding var wasDeleted: Bool
    
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
                
                
                
                Text(timeString(time: timeElapsed))
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
                        timerRunning = true
                    }
                    .onDisappear {
                        timerRunning = false
                        timeElapsed = 0
                    }
                
                // 추가 텍스트
                Text("열심히 듣고 있어요")
                    .frame(height: 44)
                    .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                    .padding(.horizontal,20)
                
                // 녹음 및 취소/저장 버튼
                HStack {
                    if recordBtn {
                        Button(action: {
                            // 취소 로직
                            print("녹음 취소")
                            isPresented = false
                            wasDeleted = true
                        
                        }) {
                            Image(systemName: "trash")
                                .foregroundColor(.red)
                                .frame(width: 30, height: 30)
                        }
                        .padding(.trailing, 20)
                    }
                    
                    Button(action: {
                        recordBtn.toggle()
                        timerRunning.toggle()
                        //voiceRecorderViewModel.recordBtnTapped()
                        audioRecorderManager.isRecording
                        ? audioRecorderManager.stopRecording()
                        : audioRecorderManager.startRecording()
                        
                        
                    }) {
                        Image(systemName: audioRecorderManager.isRecording ? "stop.fill" : "record.circle")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 50, height: 50)
                            .foregroundColor(.red)
                    }
                    
                    if recordBtn {
                        Button(action: {
                            // 저장 로직
                            print("녹음 저장")
                            isPresented = false
                            //  voiceRecorderViewModel.stopRecording()  // ViewModel에서 녹음 중지
                            recordBtn = false  // 버튼 상태 업데이트
                            timerRunning = false  // 타이머 중지
                            if let lastRecordedFile = audioRecorderManager.recordedFiles.last {
                                print(lastRecordedFile.lastPathComponent) // 마지막으로 레코드된 녹음 파일의 이름 확인함
                            } else {
                                print("No recordings available")
                            }
                            
                            
                            
                            //TODO: - 이제 여기에서 서버에다가 내 녹음파일을 보내는 작업을 추가 해야하고
                            //TODO: - 녹음파일을 보냄과 동시에로다가 오픈 소스에서 받아온 기온과 온도들도 같이 보내야함
                        }) {
                            Image(systemName: "square.and.arrow.down")
                                .foregroundColor(.blue)
                                .frame(width: 30, height: 30)
                        }
                        .padding(.leading, 20)
                    }
                }
                .padding()
                
                //TODO: - 저장파일 확인용
                
                
            }
            .frame(maxWidth: .infinity, maxHeight: 284)
            .background(Color.homeBack)
            
        }
        .transition(.move(edge: .bottom)) // 하단에서 올라오는 애니메이션 효과
        
        
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


