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


import Foundation
import AVFoundation

import SwiftUI
import AVFoundation

class VoiceRecorderViewModel: NSObject, ObservableObject, AVAudioPlayerDelegate {
  @Published var isDisplayRemoveVoiceRecorderAlert: Bool
  @Published var isDisplayAlert: Bool
  @Published var alertMessage: String
  
  /// 음성메모 녹음 관련 프로퍼티
  var audioRecorder: AVAudioRecorder?
  @Published var isRecording: Bool
  
  /// 음성메모 재생 관련 프로퍼티
  var audioPlayer: AVAudioPlayer?
  @Published var isPlaying: Bool
  @Published var isPaused: Bool
  @Published var playedTime: TimeInterval
  private var progressTimer: Timer?
  
  /// 음성메모된 파일
  var recordedFiles: [URL]
  
  /// 현재 선택된 음성메모 파일
  @Published var selectedRecoredFile: URL?
  
  init(
    isDisplayRemoveVoiceRecorderAlert: Bool = false,
    isDisplayErrorAlert: Bool = false,
    errorAlertMessage: String = "",
    isRecording: Bool = false,
    isPlaying: Bool = false,
    isPaused: Bool = false,
    playedTime: TimeInterval = 0,
    recordedFiles: [URL] = [],
    selectedRecoredFile: URL? = nil
  ) {
    self.isDisplayRemoveVoiceRecorderAlert = isDisplayRemoveVoiceRecorderAlert
    self.isDisplayAlert = isDisplayErrorAlert
    self.alertMessage = errorAlertMessage
    self.isRecording = isRecording
    self.isPlaying = isPlaying
    self.isPaused = isPaused
    self.playedTime = playedTime
    self.recordedFiles = recordedFiles
    self.selectedRecoredFile = selectedRecoredFile
  }
}

extension VoiceRecorderViewModel {
  func voiceRecordCellTapped(_ recordedFile: URL) {
    if selectedRecoredFile != recordedFile {
      stopPlaying()
      selectedRecoredFile = recordedFile
    }
  }
  
  func removeBtnTapped() {
    setIsDisplayRemoveVoiceRecorderAlert(true)
  }
  
  func removeSelectedVoiceRecord() {
    guard let fileToRemove = selectedRecoredFile,
          let indexToRemove = recordedFiles.firstIndex(of: fileToRemove) else {
      displayAlert(message: "선택된 음성메모 파일을 찾을 수 없습니다.")
      return
    }
    
    do {
      try FileManager.default.removeItem(at: fileToRemove)
      recordedFiles.remove(at: indexToRemove)
      selectedRecoredFile = nil
      stopPlaying()
      displayAlert(message: "선택된 음성메모 파일을 성공적으로 삭제했습니다.")
    } catch {
      displayAlert(message: "선택된 음성메모 파일 삭제 중 오류가 발생했습니다.")
    }
  }
  
  private func setIsDisplayRemoveVoiceRecorderAlert(_ isDisplay: Bool) {
    isDisplayRemoveVoiceRecorderAlert = isDisplay
  }
  
  private func setErrorAlertMessage(_ message: String) {
    alertMessage = message
  }
  
  private func setIsDisplayErrorAlert(_ isDisplay: Bool) {
    isDisplayAlert = isDisplay
  }
  
  private func displayAlert(message: String) {
    setErrorAlertMessage(message)
    setIsDisplayErrorAlert(true)
  }
}

// MARK: - 음성메모 녹음 관련
extension VoiceRecorderViewModel {
  func recordBtnTapped() {
    selectedRecoredFile = nil
    
    if isPlaying {
      stopPlaying()
      startRecording()
    } else if isRecording {
      stopRecording()
    } else {
      startRecording()
    }
  }
  
  private func startRecording() {
    let fileURL = getDocumentsDirectory().appendingPathComponent("새로운 녹음 \(recordedFiles.count + 1)")
    let settings = [
      AVFormatIDKey: Int(kAudioFormatMPEG4AAC),
      AVSampleRateKey: 12000,
      AVNumberOfChannelsKey: 1,
      AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue
    ]
    
    do {
      audioRecorder = try AVAudioRecorder(url: fileURL, settings: settings)
      audioRecorder?.record()
      self.isRecording = true
    } catch {
      displayAlert(message: "음성메모 녹음 중 오류가 발생했습니다.")
    }
  }
  
  private func stopRecording() {
    audioRecorder?.stop()
    self.recordedFiles.append(self.audioRecorder!.url)
    self.isRecording = false
  }
  
  private func getDocumentsDirectory() -> URL {
    let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
    return paths[0]
  }
}

// MARK: - 음성메모 재생 관련
extension VoiceRecorderViewModel {
  func startPlaying(recordingURL: URL) {
    do {
      audioPlayer = try AVAudioPlayer(contentsOf: recordingURL)
      audioPlayer?.delegate = self
      audioPlayer?.play()
      self.isPlaying = true
      self.isPaused = false
      self.progressTimer = Timer.scheduledTimer(
        withTimeInterval: 0.1,
        repeats: true
      ) { _ in
        self.updateCurrentTime()
      }
    } catch {
      displayAlert(message: "음성메모 재생 중 오류가 발생했습니다.")
    }
  }
  
  private func updateCurrentTime() {
    self.playedTime = audioPlayer?.currentTime ?? 0
  }
  
  private func stopPlaying() {
    audioPlayer?.stop()
    playedTime = 0
    self.progressTimer?.invalidate()
    self.isPlaying = false
    self.isPaused = false
  }
  
  func pausePlaying() {
    audioPlayer?.pause()
    self.isPaused = true
  }
  
  func resumePlaying() {
    audioPlayer?.play()
    self.isPaused = false
  }
  
  func audioPlayerDidFinishPlaying(_ player: AVAudioPlayer, successfully flag: Bool) {
    self.isPlaying = false
    self.isPaused = false
  }
  
  func getFileInfo(for url: URL) -> (Date?, TimeInterval?) {
    let fileManager = FileManager.default
    var creationDate: Date?
    var duration: TimeInterval?
    
    do {
      let fileAttributes = try fileManager.attributesOfItem(atPath: url.path)
      creationDate = fileAttributes[.creationDate] as? Date
    } catch {
      displayAlert(message: "선택된 음성메모 파일 정보를 불러올 수 없습니다.")
    }
    
    do {
      let audioPlayer = try AVAudioPlayer(contentsOf: url)
      duration = audioPlayer.duration
    } catch {
      displayAlert(message: "선택된 음성메모 파일의 재생 시간을 불러올 수 없습니다.")
    }
    
    return (creationDate, duration)
  }
}


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
    var body: some View {
        VStack {
            Spacer() // 상단 부분을 비워 뷰의 하단에만 내용이 표시되도록 합니다.
            
            VStack {
                // 여기에 Bottom Sheet 내부에 표시할 내용을 넣습니다.
                Text("녹음 중...")
                    .font(.title)
                    .padding()
                Button("닫기") {
                    withAnimation {
                        isPresented = false
                    }
                }
                .padding()
            }
            .frame(maxWidth: .infinity)
            .background(Color.white) // 시트의 배경색
            .cornerRadius(20) // 상단 모서리 둥글게
            .shadow(radius: 10) // 그림자 효과
        }
        .transition(.move(edge: .bottom)) // 하단에서 올라오는 애니메이션 효과
        .onTapGesture {
            withAnimation {
                isPresented = false // 시트 외부를 탭하면 시트를 닫습니다.
            }
        }
    }
}

//
//struct BottomSheetView: View {
//    @Environment(\.presentationMode) var presentationMode
//    @Binding var isAgree1: Bool
//    @Binding var isAgree2: Bool
//    @Binding var isAgree3 : Bool
//    @Binding var showingSheet: Bool
//    @State private var showingTermsSheet: Bool = false
//      @State private var selectedTermsIndex: Int = 0
//    
//    let termsTitles = ["이용 약관 동의", "개인정보 수집 및 이용 동의", "위치정보 이용 동의"]
//      
//      let Important = ["필수","필수","선택"]
//    
//    var body: some View {
//        VStack(spacing: 20) {
//            Spacer()
//            ForEach(0..<3) { index in
//                HStack {
//                    Text(Important[index])
//                        .font(.caption)
//                        .foregroundColor(Important[index] == "선택" ? .gray : .homeRed)
//                        .foregroundColor(.homeRed)
//                    
//                    Text(termsTitles[index])
//                        .font(.caption)
//                        .foregroundColor(.gray)
//                    
//                    Spacer()
//                    
//                    Button("보기") {
//                        selectedTermsIndex = index
//                                               showingTermsSheet = true
//                    }
//                    .foregroundColor(.homeRed)
//                    .font(.caption)
//                    
//                    Button(action: {
//                        switch index {
//                        case 0: isAgree1.toggle()
//                        case 1: isAgree2.toggle()
//                        case 2: isAgree3.toggle()
//                        default: break
//                        }
//                    }) {
//                        Image(systemName: isAgree1 && index == 0 || isAgree2 && index == 1 || isAgree3 && index == 2 ? "checkmark.square" : "square")
//                            .foregroundColor(.homeRed)
//                    }
//                }
//                .padding(.horizontal, 20)
//            }
//            Spacer()
//        }
//        .background(Color.white)
//        .cornerRadius(10)
//        .shadow(radius: 5)
//        .onDisappear {
//            if !(isAgree1 && isAgree2) {
//                showingSheet = false // Close the bottom sheet if not all required are agreed
//            }
//        }
//        .sheet(isPresented: $showingTermsSheet) {
//                  // 약관 내용 보기
//                  TermsContentSheetView(content: termsContents[selectedTermsIndex])
//              }
//    }
//}


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

