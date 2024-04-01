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
    
    var body: some View {
        
       
        NavigationStack(path : $pathModel.paths){
              OnboardingContentView(onboardingViewModel: onboardingViewModel)
         
          
              
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



// MARK: - 온보딩 컨텐츠 뷰
private struct OnboardingContentView: View {
    
    @ObservedObject private var onboardingViewModel : OnboardingViewModel
    
    fileprivate init(onboardingViewModel: OnboardingViewModel) {
        self.onboardingViewModel = onboardingViewModel
    }
    
    fileprivate var body: some View{
        VStack{
            //온보딩 셀리스트뷰
            OnboardingCellListView(onboardingViewModel: onboardingViewModel)
            // 시작 버튼뷰
            Spacer()
            StartBtnView()
        }
        .edgesIgnoringSafeArea(.top)
    }
}
//MARK : - 온보딩 셀 리스트 뷰
private struct OnboardingCellListView : View{
    @ObservedObject private var onboardingViewModel : OnboardingViewModel
    @State private var selectedIndex : Int
    
    fileprivate
    init(onboardingViewModel: OnboardingViewModel,
        selectedIndex: Int = 0
    ) {
        self.onboardingViewModel = onboardingViewModel
        self.selectedIndex = selectedIndex
    }
    
    fileprivate var body: some View{
        TabView(selection: $selectedIndex){
            ForEach(Array(onboardingViewModel.onboardingContents.enumerated()),id : \.element){
               index, onboardingContent in
                OnboardingCellView(onboardingContent: onboardingContent)
                    .tag(index)
            }
        }
        .tabViewStyle(.page(indexDisplayMode: .never))
        .frame(width: UIScreen.main.bounds.width,height: UIScreen.main.bounds.height / 1.5)
        .background(
        selectedIndex % 2 == 0
        ? Color.customSky
        : Color.customCoolGray
        
        )
        .clipped()
    }
}

//MARK : - 온보딩 셀 뷰
private struct OnboardingCellView : View{
    private var onboardingContent: OnboardingContent
    
    fileprivate init(onboardingContent: OnboardingContent){
        self.onboardingContent = onboardingContent
    }
    
    fileprivate var body: some View{
        VStack{
            Image(onboardingContent.imageFileName)
                .resizable()
                .scaledToFit()
            
            
            HStack{
                Spacer()
                VStack{
                    Spacer()
                        .frame(height: 46)
                    
                    Text(onboardingContent.title)
                        .font(.system(size: 16,weight: .bold))
                    
                    Spacer()
                        .frame(height: 5)
                    
                    Text(onboardingContent.subTitle)
                        .font(.system(size: 16))
                }
                
                Spacer()
            }
            .background(Color.customWhite)
            .cornerRadius(0)
        }
        .shadow(radius: 10)
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
                    Text("시작하기")
                        .font(.system(size: 16,weight : .medium))
                        .foregroundColor(.customBackgroundGreen)
                    
                    Image("arrow")
                        .renderingMode(.template)
                        .foregroundColor(.green)
                }
            }
        )
        .padding(.bottom , 50)
    }
}
