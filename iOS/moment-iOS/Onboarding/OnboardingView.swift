//
//  OnboardingView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI
//4. 여기서 이제 뷰를 만들어줌 하위뷰들을 많이 쪼개서 만들어주는게 재사용들의 부분에서 되게 좋다

struct OnboardingView: View {
    @StateObject private var onboardingViewModel = OnboardingViewModel()
    @StateObject private var pathModel = PathModel()
    @StateObject private var homeViewModel = HomeViewModel()
    @StateObject private var billListViewModel = BillListViewModel()
    @StateObject var audioRecorderManager = AudioRecorderManager()
    @State private var showingCustomAlert = false
    @StateObject private var cardviewModel = CardViewModel()
    //@StateObject private var viewModel = OnboardingViewModel()
        @State private var currentPage = 0
    
    var body: some View {
        
       
        NavigationStack(path : $pathModel.paths){
             // OnboardingContentView(onboardingViewModel: onboardingViewModel)
            VStack {
                      TabView(selection: $currentPage) {
                          ForEach(0..<onboardingViewModel.onboardingContents.count, id: \.self) { index in
                              VStack {
                              
                                  Image(onboardingViewModel.onboardingContents[index].imageFileName)
                                      .resizable()
                                      .scaledToFit()
                                      
                                
                              }.tag(index)
                            
                          }
                      } .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                
                      
                if currentPage == onboardingViewModel.onboardingContents.count - 1 {
                               // 마지막 인덱스에 도달했을 때 보여줄 버튼
                    Spacer().frame(height: 10)
                    
                    Button(
                        action: {
                            pathModel.paths.append(.LoginView)
                        },
                        label: {
                            Text("시작하기")
                                .font(.pretendardSemiBold18)
                                .frame(width: 335,height: 32)
                                .padding()
                                .background(Color.homeRed)
                                .foregroundColor(.white)
                                .cornerRadius(3)
                        }
                    )
                      
                    Spacer().frame(height: 40)
                } else {
                    // 그 외의 페이지에서 보여줄 버튼
                    Button(action: {
                        // 현재 인덱스가 마지막이 아닐 때의 버튼 액션
                        if currentPage < onboardingViewModel.onboardingContents.count - 1 {
                           
                            pathModel.paths.append(.LoginView)
                        }
                    }) {
                        HStack {
                            Spacer()
                            Text("바로 시작하기") .font(.pretendardMedium14)
                                                       .foregroundColor(.black)
                            Image("arrowST")
                        }
                        .padding(.bottom , 50)
                                          .padding()
                    }
                }
                      
//                HStack{
//                  Spacer()
//                    Button(
//                        action : {
//                            pathModel.paths.append(.LoginView)
//                        },
//                        label :{
//                            HStack{
//                                Text("바로 시작하기")
//                                    .font(.pretendardMedium14)
//                                    .foregroundColor(.black)
//                                
//                                Image("arrowST")
//                                    
//                                
//                            }
//                        }
//                    )
//                    .padding(.bottom , 50)
//                    .padding()
//                      
//                }
                Spacer()
                    .frame(height: 30)
                
                  }
          
              
                .navigationDestination(for: PathType.self, destination: { // 여기서 for 부분에서 PathType 부분에서 해당 네비게이션을 정의를 하는부분인거임
                    PathType in
                    switch PathType {
                    case .homeBaseView :
                        HomeBaseView(audioRecorderManager: audioRecorderManager, cardViewModel: cardviewModel)
                            .navigationBarBackButtonHidden()
                            .environmentObject(homeViewModel)// 이렇게. environment 를 달아놧다는것은 해당뷰에서도
                            .environmentObject(billListViewModel)//안에 들어가있는 녀석을 호출햇 ㅓ사용할수있다는 말을 뜻한다
                            .environmentObject(cardviewModel)
                        
//                    case.todoView :
//                        TodoView()
//                            .navigationBarBackButtonHidden()
//                            .environmentObject(homeViewModel)
                        
                    case let .memoView(isCreateMode, memo):
                                 MemoView(
                                   memoViewModel: isCreateMode
                                   ? .init(memo: .init(title: "", content: "", date: .now))
                                   : .init(memo: memo ?? .init(title: "", content: "", date: .now)),
                                   isCreateMode: isCreateMode
                                 )
                                   .navigationBarBackButtonHidden()
                                   .environmentObject(billListViewModel)
                    case .LoginView:
                        LoginView()
                            .navigationBarBackButtonHidden()
                            .environmentObject(homeViewModel)
                        
                    case .AuthView:
                        AuthView()
                            .environmentObject(homeViewModel)
                        
                    case .AuthNumView:
                        AuthNumView()
                            .environmentObject(homeViewModel)
                    case .AddUser:
                        AddUser()
                            .environmentObject(homeViewModel)
                        
                    case .AuthComplete:
                        AuthComplete()
                            .environmentObject(homeViewModel)
                        
                    case .PasswordView:
                        PasswordView()
                            .environmentObject(homeViewModel)
                    case .PasswordfindView:
                        PasswordfindView()
                            .environmentObject(homeViewModel)
                    case .PasswordinsertView:
                        PasswordinsertView()
                            .environmentObject(homeViewModel)
                    }
                }
                )
        }
        .environmentObject(pathModel)
        
        
        
    }
}

//시작하기 버튼
private struct StartBtnView : View{
    @EnvironmentObject private var pathModel : PathModel
    
    fileprivate var body: some View{
        Button(
            action : {
                pathModel.paths.append(.LoginView)
            },
            label :{
                HStack{
                    Text("바로 시작하기")
                        .font(.pretendardMedium14)
                        .foregroundColor(.black)
                    
                    Image("arrowST")
                        
                    
                }
            }
        )
        .padding(.bottom , 50)
    }
}
