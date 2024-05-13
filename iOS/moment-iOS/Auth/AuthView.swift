//
//  AuthView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/18/24.
//

import SwiftUI
import Alamofire

struct AuthView: View {
    @State private var email: String = ""
    @EnvironmentObject private var pathModel: PathModel
    
    @State private var isAutoLogin: Bool = false
    @State private var isShowingBottom = false
    @State private var isAgreeRequired1: Bool = false
    @State private var isAgreeRequired2: Bool = false
    @State private var isAgreeRequired3: Bool = false
    @Environment(\.presentationMode) var presentationMode
    @StateObject private var authViewModel = AuthViewModel()
    
    var heightFactor: CGFloat {
        UIScreen.main.bounds.height > 800 ? 3.6 : 3
    }
    
    var offset: CGFloat {
        isShowingBottom ? 0 : UIScreen.main.bounds.height / heightFactor
    }
    
    
    var body: some View {
        NavigationView {
            
            ZStack {
              
                    Color(.homeBack).edgesIgnoringSafeArea(.all)
                        .onTapGesture {
                                   hideKeyboard()
                               }
                VStack {
                    Button(action: {
                        // "뒤로" 버튼의 액션: 현재 뷰를 종료
                        pathModel.paths.append(.LoginView)
                    }) {
                        HStack {
                            Spacer().frame(width: 20)
                            Image("arrow1")
                            Text("로그인")
                                .padding()
                                .font(.pretendardSemiBold18)
                                .tint(Color.black)
                            Spacer()
                        }
                    }
                    
                    Spacer()
                    HStack(alignment: .bottom) {
                        Text("이메일")
                            .font(.pretendardMedium11)
                            .foregroundColor(.gray600)
                            .font(.caption)
                            .padding(.top, 1)
                            .padding(.horizontal, 20)
                        
                        Spacer()
                    }.padding(.bottom,1)
                    
                    TextField("인증 가능한 이메일을 입력하세요. ", text: $email)
                        .padding()
                        .frame(height: 44)
                        .overlay(Rectangle().frame(height: 1), alignment: .bottom)
                        .padding(.horizontal,20)
                    
                   
                    
                    HStack {
                        Text("해당 이메일은 본인인증 수단으로서 활용되며\n비밀번호 분실시 복구코드를 보내드리는 용도로 사용됩니다.")
                            .font(.pretendardMedium11)
                            .foregroundColor(.gray600)
                            .padding(.top, 3)
                            .fixedSize(horizontal: false, vertical: true)
                        Spacer()
                    }
                    .padding([.bottom, .horizontal], 20)
                    
                    // Checkbox and Button
                    HStack {
                        Spacer()
                        // "이용약관" 버튼
                        Text("이용약관")
                            .foregroundColor(.homeRed)
                            .font(.pretendardMedium11)
                            .padding(.bottom, 10)
                        
                        Text("및")
                            .font(.pretendardMedium11)
                            .foregroundColor(.black)
                            .padding(.bottom, 10)
                        
                        // "개인정보 수집동의서" 버튼
                        Text("개인정보 수집동의서")
                            .foregroundColor(.homeRed)
                            .font(.pretendardMedium11)
                            .padding(.bottom, 10)
                        
                        
                        Text("확인")
                            .font(.pretendardMedium11)
                            .foregroundColor(.black)
                            .padding(.bottom, 10)
                        
                        Button(action: {
                            isShowingBottom = true
                        }) {
                            Image(systemName: isAutoLogin ? "checkmark.square.fill" : "square")
                                .foregroundColor(isAutoLogin ? .homeRed : .gray)
                                .foregroundColor(.black)
                                .padding(.bottom, 10)
                        }
                    }.padding(.horizontal,20)
                    
                    Spacer()
                    
                    // Next Button
                    VStack {
                        Button(
                            action: {
                                authViewModel.email = email
                                authViewModel.requestAuthCode()
                                pathModel.paths.append(.AuthNumView)
                            },
                            label: {
                                Text("다음")
                                    .font(.pretendardSemiBold18)
                                    .frame(minWidth: 0, maxWidth: .infinity)
                                    .padding()
                                    .background(Color.homeRed)
                                    .foregroundColor(.white)
                                    .cornerRadius(3)
                            }
                        )
                    }.padding()
                }
                
                // Dark Overlay
                if isShowingBottom {
                    Color.black.opacity(0.5)
                        .edgesIgnoringSafeArea(.all)
                        .transition(.opacity)
                        .onTapGesture {
                            withAnimation {
                                isShowingBottom = false
                            }
                        }
                }
                
                // Bottom Sheet
                if isShowingBottom {
                    GeometryReader { proxy in
                        VStack {
                            Spacer()
                            BottomSheetView(isAgree1: $isAgreeRequired1, isAgree2: $isAgreeRequired2, isAgree3: $isAgreeRequired3, showingSheet: $isShowingBottom)
                                .frame(
                                    width: proxy.size.width,
                                    height: proxy.size.height / heightFactor,
                                    alignment: .center
                                )
                                .offset(y: offset)
                                .animation(.easeInOut(duration: 0.49))
                        }
                    }
                    .edgesIgnoringSafeArea(.bottom)
                    .transition(.move(edge: .bottom))
                }
            }
            .onChange(of: isAgreeRequired1) { _ in
                updateAutoLoginStatus()
            }
            .onChange(of: isAgreeRequired2) { _ in
                updateAutoLoginStatus()
            }
        }.navigationBarBackButtonHidden(true)
    }
    
    func updateAutoLoginStatus() {
        if isAgreeRequired1 && isAgreeRequired2 {
            isAutoLogin = true
        } else {
            isAutoLogin = false
        }
    }
    private func hideKeyboard() {
          UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
      }
}

struct BottomSheetView: View {
    @Environment(\.presentationMode) var presentationMode
    @Binding var isAgree1: Bool
    @Binding var isAgree2: Bool
    @Binding var isAgree3 : Bool
    @Binding var showingSheet: Bool
    @State private var showingTermsSheet: Bool = false
      @State private var selectedTermsIndex: Int = 0
    
    let termsTitles = ["이용 약관 동의", "개인정보 수집 및 이용 동의", "위치정보 이용 동의"]
      
      let Important = ["(필수)","(필수)","(선택)"]
    
    var body: some View {
        VStack(spacing: 20) {
            Spacer()
            ForEach(0..<3) { index in
                HStack {
                    Text(Important[index])
                        .font(.pretendardMedium11)
                        .foregroundColor(Important[index] == "선택" ? .gray : .homeRed)
                        .foregroundColor(.homeRed)
                    
                    Text(termsTitles[index])
                        .font(.pretendardMedium11)
                        .foregroundColor(.gray)
                    
                    Spacer()
                    
                    Button("보기") {
                        selectedTermsIndex = index
                                               showingTermsSheet = true
                    }
                    .foregroundColor(.homeRed)
                    .font(.pretendardMedium11)
                    
                    Button(action: {
                        switch index {
                        case 0: isAgree1.toggle()
                        case 1: isAgree2.toggle()
                        case 2: isAgree3.toggle()
                        default: break
                        }
                    }) {
                        Image(systemName: isAgree1 && index == 0 || isAgree2 && index == 1 || isAgree3 && index == 2 ? "checkmark.square" : "square")
                            .foregroundColor(.homeRed)
                    }
                }
                .padding(.horizontal, 20)
            }
            Spacer()
        }
        .background(Color.white)
        .cornerRadius(10)
        .shadow(radius: 5)
        .onDisappear {
            if !(isAgree1 && isAgree2) {
                showingSheet = false // Close the bottom sheet if not all required are agreed
            }
        }
        .sheet(isPresented: $showingTermsSheet) {
                  // 약관 내용 보기
                  TermsContentSheetView(content: termsContents[selectedTermsIndex])
              }
    }
}

// 상세 약관 내용을 보여주는 뷰
struct TermsContentSheetView: View {
    @Environment(\.presentationMode) var presentationMode
    let content: String
    
    var body: some View {
        NavigationView {
            ScrollView {
                Text(content)
                    .font(.pretendardExtrabold16)
                    .padding()
            }
            .navigationBarTitle("약관 확인", displayMode: .inline)
            .navigationBarItems(trailing: Button("닫기") {
                presentationMode.wrappedValue.dismiss()
            })
        }
    }
}

// 각 약관의 내용
let termsContents = [
    "이용 약관의 상세 내용입니다...",
    "개인정보 수집 및 이용에 대한 상세 내용입니다...",
    "위치정보 이용에 대한 상세 내용입니다..."
]



#Preview {
    AuthView()
}
