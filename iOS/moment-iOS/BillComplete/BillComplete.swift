//
//  BillComplete.swift
//  moment-iOS
//
//  Created by 양시관 on 5/8/24.
//

import Foundation
import SwiftUI






struct ReceiptCompleteDetailView: View {
    @EnvironmentObject var homeViewModel: HomeViewModel
    @EnvironmentObject var billListViewModel: BillListViewModel
    @EnvironmentObject var sharedViewModel: SharedViewModel
    @Environment(\.presentationMode) var presentationMode
    @State private var isDialogActive = false
    @State private var isDialogActiveBillCom = false
    var topColor: Color = .homeRed
    var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
    //let item : Item
    @State var item: Item
    @State private var text: String = ""
    @State private var starttrip: String = ""
    @State private var StartLocation : String = ""
    @State private var EndLocation : String = ""
    @State private var isEditing: Bool = false
    @State private var saveButtonTitle = "저장"
    @State private var backButtonTitle = ""
    @State private var inputText: String = ""
    @State private var EndLocationend : String = ""
    @State private var selectedTab = 0
    @State private var showingGroup = false
    @State private var showingCompleteBillView = false
    @State private var showExportButton = false
    
    
    var snapshotManager: SnapshotManager?
    
    
    
    var body: some View {
        
        ZStack{
            
            
            VStack {
                
                HStack {
                    
                    Button(action: {
                                           presentationMode.wrappedValue.dismiss()
                                       }) {
                                           Text("수정")
                                               .padding(.horizontal, 20)
                                               .padding()
                                               .font(.yjObangBold15)
                                               .foregroundColor(.black)
                                       }
                    
                  
                                       
                    
                    Button(action: {
                        
                        if backButtonTitle == "" {
                            // 내보내기 기능 실행
                            isDialogActive = true
                            print("두ㅏㅣ로가기")
                        } else {
                            // "내보내기" 버튼의 기능을 실행
                            // let image: UIImage
                            let snapshotManager: SnapshotManager
                            switch selectedTab {
                            case 0:
                                
                                
                                
                                snapshotManager = SnapshotManager(rootView: AnyView(ReceiptCompleteView(item: item) .environmentObject(sharedViewModel)
                                                                                   ))
                                
                            case 1:
                                
                                
                                snapshotManager = SnapshotManager(rootView: AnyView(ReceiptSecondView()
                                        .environmentObject(sharedViewModel)
                                                                                   ))
                            default:
                                
                                //  image = UIImage()
                                return
                            }
                            //   showShareSheet(image)
                            snapshotManager.captureSnapshot { image in
                                DispatchQueue.main.async {
                                    showShareSheet(image)
                                }
                            }
                            print("내보내기")
                        }
                    }) {
                        HStack {
                            Text(backButtonTitle)
                        }
                        .padding(.horizontal, 20)
                        .padding()
                        .font(.yjObangBold15)
                        .tint(Color.black)
                    }
                    Spacer()
                    
                    Button(action: {
                        if saveButtonTitle == "저장" {
                            print(sharedViewModel.isSaved)
                            sharedViewModel.isSaved.toggle()
                            
                            print(sharedViewModel.isSaved)
                            
                            if sharedViewModel.isSaved {
                                saveButtonTitle = "완료"
                                backButtonTitle = "내보내기"
                                isDialogActiveBillCom = true
                                
                            }
                            //  self.showingCompleteBillView = true
                            
                            
                            
                        } else {
                            // 완료 버튼의 기능
                            
                            
                            self.showingGroup = true
                            print("완료")
                        }
                    }) {
                        HStack {
                            Text(saveButtonTitle)
                        }
                        .padding(.horizontal, 20)
                        .padding()
                        .font(.yjObangBold15)
                        .foregroundColor(saveButtonTitle == "완료" ? .homeRed : .black)
                    }
                }
                Spacer()
            }
            .zIndex(1)
            
            
            TabView(selection: $selectedTab) {
                ForEach(0..<4) { index in
                    ReceiptCompleteView(item: item)
                        .tag(0)
                    // .environmentObject(SharedViewModel())
                    ReceiptView()
                        .tag(1)
                    //  .environmentObject(SharedViewModel())
                }
                
                
                
                //TODO: - 여기에다가 두번째 디자인 카드를 추가하고 index 1 번으로 추가한다
                
                
            } .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                .zIndex(0)
                .overlay(
                    VStack {
                        Spacer()
                        CustomTabIndicator(
                            numberOfTabs: 4,
                            selectedTab: selectedTab,
                            accentColor: .gray500,
                            inactiveColor: .Natural100
                        )
                    }
                        .padding(.bottom), alignment: .bottom
                )
            
            if isDialogActive {
                CustomDialogBill(isActive: $isDialogActive, title: "", message: "앗! 지금 화면을 그냥 나가면 열심히 만든 영수증이 저장되지않아요", yesAction: {
                    //TODO: - 영수증의 시작뷰로 돌아가야함
                    print("저장됨")
                    // 여기에 저장 로직 추가
                }, noAction: {
                    //TODO: - dismiss하기
                    
                    print("취소됨")
                })
            }
            
            if isDialogActiveBillCom{
                
                CustomDialogBillComplete(isActive: $isDialogActiveBillCom, title: "축하해요", message: "첫번째 영수증이 만들어졌어요!\n 나도이제 여행자", yesAction: {
                    //TODO: - 영수증의 시작뷰로 돌아가야함
                    print("저장됨")
                    // 여기에 저장 로직 추가
                }, noAction: {
                    //TODO: - dismiss하기
                    
                    print("취소됨")
                })
            }
            
            
            NavigationLink(destination : ReceiptGroupView(),isActive: $showingGroup){
                EmptyView()
                
            }
            //                NavigationLink(destination: ReceiptCompleteView(item: item), isActive: $showingCompleteBillView) {
            //                    EmptyView()
            //                                }
        }
        .background(.homeBack)
        .navigationBarBackButtonHidden()
        
        
    }
    // 얘는 문제가 없는거같고
    private func showShareSheet(_ image: UIImage) {
        
        guard let rootVC = UIApplication.shared.windows.first?.rootViewController else {
            return
        }
        
        // UIActivityViewController 인스턴스 생성
        let activityVC = UIActivityViewController(activityItems: [image], applicationActivities: nil)
        
        
        if let popoverController = activityVC.popoverPresentationController {
            popoverController.sourceView = rootVC.view
            popoverController.sourceRect = CGRect(x: rootVC.view.bounds.midX, y: rootVC.view.bounds.midY, width: 0, height: 0)
            popoverController.permittedArrowDirections = []
        }
        
        // activityVC를 present합니다.
        rootVC.present(activityVC, animated: true, completion: nil)
    }
    
}

struct ReceiptCompleteView : View {
    @EnvironmentObject var homeViewModel: HomeViewModel
    @EnvironmentObject var sharedViewModel: SharedViewModel
    @Environment(\.presentationMode) var presentationMode
    @State private var isDialogActive = false
    @State private var isDialogActiveBillCom = false
    var topColor: Color = .homeRed
    var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
    let item : Item
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
                                
                                Text(sharedViewModel.EndLocation)
                               
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
    private func showShareSheet(_ image: UIImage) {
        
        guard let rootVC = UIApplication.shared.windows.first?.rootViewController else {
            return
        }
        
        // UIActivityViewController 인스턴스 생성
        let activityVC = UIActivityViewController(activityItems: [image], applicationActivities: nil)
        
        // iPad에서는 popover로 표시될 위치를 지정해야 할 수 있습니다.
        if let popoverController = activityVC.popoverPresentationController {
            popoverController.sourceView = rootVC.view
            popoverController.sourceRect = CGRect(x: rootVC.view.bounds.midX, y: rootVC.view.bounds.midY, width: 0, height: 0)
            popoverController.permittedArrowDirections = []
        }
        
        // activityVC를 present합니다.
        rootVC.present(activityVC, animated: true, completion: nil)
    }
    
    func captureSnapshot() {
        let hostingController = UIHostingController(rootView: ReceiptBillView1(item:item) //.environmentObject(sharedViewModel)
        )
        let targetSize = hostingController.view.intrinsicContentSize
        hostingController.view.bounds = CGRect(origin: .zero, size: targetSize)
        hostingController.view.layoutIfNeeded()
        
        let renderer = UIGraphicsImageRenderer(size: targetSize)
        let image = renderer.image { _ in
            hostingController.view.drawHierarchy(in: hostingController.view.bounds, afterScreenUpdates: true)
        }
        
    }
}



struct ReceiptSecondView: View {
    @State var currentPage = 0 // 현재 페이지를 나타내는 상태 변수
    var topColor: Color = .homeRed
    var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
     @EnvironmentObject var sharedViewModel: SharedViewModel
    @State private var tripRecord : String = ""
    @State private var tripExplaneStart : String = ""
    @State private var tripnameStart : String = ""
    @State private var tripnameEnd: String = ""
    @State private var tripExplaneEnd : String = ""
    
    
    var body: some View {
        
        VStack(spacing: 0) {
            Spacer().frame(height: 18)
            HStack{
                
                Spacer().frame(width: 40)
                Text(sharedViewModel.tripRecord)
                    .font(.pretendardMedium14)
                    .foregroundColor(.gray500)  // 글씨
                    .multilineTextAlignment(.center)
                
                Spacer()
            }
            
            Spacer().frame(height: 70)
            
            
            VStack(alignment:.center,spacing:0){
                HStack(alignment:.center,spacing:1)
                {
                    
                    Image("Location")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 19, height: 19)
                    
                    Text(sharedViewModel.tripExplaneStart)
                    .font(.pretendardMedium14)
                    .foregroundColor(.gray600)
                    .multilineTextAlignment(.center)
                }.frame(maxWidth: .infinity)
                    .padding(.bottom,8)
                
                
                HStack{
                    Text(sharedViewModel.tripnameStart)
                        .font(.pretendardExtrabold45)
                        .foregroundColor(.black)  // 글씨
                        .multilineTextAlignment(.center)
                }
                .padding(.bottom,20)
            }
            
            
            Image("Subway")
                .resizable()
                .scaledToFit()
                .frame(width: 40,height: 91)
                .padding(.bottom,20)
            
            
            
            
            VStack(spacing:0){
                HStack(alignment: .center,spacing:0)
                {
                    Image("Location")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 19, height: 19)
                    
                    Text(sharedViewModel.tripExplaneEnd)
                    .font(.pretendardMedium14)
                    .foregroundColor(.gray600)
                    .multilineTextAlignment(.center)
                }  .padding(.bottom,8)
                
                
                
                HStack{
                    
                    Text(sharedViewModel.tripnameEnd)
                        .font(.pretendardExtrabold45)
                        .foregroundColor(.black)  // 글씨
                        .multilineTextAlignment(.center)
                }
                .padding(.bottom,20)
                Spacer()
            }
            
            SentimentTrackerView()
            
            
            
            
        }
        // .fill(Color.white) // 전체 색상을 회색으로 설정
        .frame(width: 335, height: 653) // 335,653
        .cornerRadius(5) // 모서리를 둥글게 처리합니다.
        .overlay(
            RoundedRectangle(cornerRadius: 3)
                .stroke(Color.gray500, lineWidth: 1)
            
        )
        //왼쪽 두꺼운 부분의 사각형
        .overlay(
            
            
            Rectangle() // 색칠할 부분의 모양
                .fill(Color.homeRed) // 색칠할 색상
                .frame(width: 30, height: 653), // 색칠할 영역의 크기
            alignment: .leading
            
        ) .overlay(
            Image("LogoVertical") // 다른 이미지 오버레이 추가
            
                .frame(width: 30, height: 645.23), // 이미지 프레임 설정
            
            
            alignment: .topLeading // 이미지 정렬 설정
        )
        //오른족 얇은 부분의 사각형
        .overlay(
            Rectangle() // 색칠할 부분의 모양
                .fill(Color.homeRed) // 색칠할 색상
                .frame(width: 10, height: 596.25), // 색칠할 영역의 크기
            alignment: .init(horizontal: .trailing, vertical: .bottom)
        )
        .overlay(
            Rectangle() // 색칠할 부분의 모양
                .offset(x:15,y:-270)
                .fill(Color.homeRed) // 색칠할 색상
                .frame(width: 305, height: 2.22) // 색칠할 영역의 크기
            
            
            
        )
        .overlay(
            Image("Circledivider")
                .resizable()
                .scaledToFit()
                .frame(width: 466, height: 12)
                .offset(y:130)
        )
        
        
    }
    private func showShareSheet(_ image: UIImage) {
        
        guard let rootVC = UIApplication.shared.windows.first?.rootViewController else {
            return
        }
        
        // UIActivityViewController 인스턴스 생성
        let activityVC = UIActivityViewController(activityItems: [image], applicationActivities: nil)
        
        // iPad에서는 popover로 표시될 위치를 지정해야 할 수 있습니다.
        if let popoverController = activityVC.popoverPresentationController {
            popoverController.sourceView = rootVC.view
            popoverController.sourceRect = CGRect(x: rootVC.view.bounds.midX, y: rootVC.view.bounds.midY, width: 0, height: 0)
            popoverController.permittedArrowDirections = []
        }
        
        // activityVC를 present합니다.
        rootVC.present(activityVC, animated: true, completion: nil)
    }
    
    func captureSnapshot() {
        let hostingController = UIHostingController(rootView: ReceiptView())
        let targetSize = hostingController.view.intrinsicContentSize
        hostingController.view.bounds = CGRect(origin: .zero, size: targetSize)
        hostingController.view.layoutIfNeeded()
        
        let renderer = UIGraphicsImageRenderer(size: targetSize)
        let image = renderer.image { _ in
            hostingController.view.drawHierarchy(in: hostingController.view.bounds, afterScreenUpdates: true)
        }
        showShareSheet(image)
    }
    
}

