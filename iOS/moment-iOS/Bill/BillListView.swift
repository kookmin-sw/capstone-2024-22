

//
//  MemoListView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI
import UIKit


struct BillListView: View {
    
    @EnvironmentObject private var pathModel: PathModel
    @EnvironmentObject private var billListViewModel : BillListViewModel
    @EnvironmentObject private var homeBaseViewModel : HomeBaseViewModel
    @EnvironmentObject var homeViewModel: HomeViewModel
    // @EnvironmentObject var sharedViewModel: SharedViewModel
    
   
    var body: some View {
        
        ZStack{
            Color(.homeBack).edgesIgnoringSafeArea(.all)
                .onTapGesture {
                                 hideKeyboard()
                             }
            
            VStack{
                
                AnnouncementView( homeBaseViewModel: homeBaseViewModel)
                
                
            }.onAppear {
                homeViewModel.fetchTrips()  // 뷰가 나타날 때 데이터를 로드합니다.
            }
            
        }
        
        
        
        
        
    }
    private func hideKeyboard() {
          UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
      }
}





 struct AnnouncementView: View {
    @EnvironmentObject private var calendarViewModel: CalendarViewModel
    @EnvironmentObject private var billListViewModel: BillListViewModel
    @EnvironmentObject var homeViewModel: HomeViewModel
     @StateObject var homeBaseViewModel : HomeBaseViewModel
    //@EnvironmentObject var sharedViewModel: SharedViewModel
    // NavigationLink 활성화를 위한 State 변수들
    @State private var isShowingCreateView = false
    @State private var isShowingReceiptsView = false
     
    
    var body: some View {
        NavigationView {
            VStack(spacing: 15) {
                Spacer().frame(height: 20)
                HStack{
                    Spacer()
                    NavigationLink(destination: ReceiptGroupView1( isCheckedStates: [false]).environmentObject(homeBaseViewModel), isActive: $isShowingCreateView) {
                        Button("영수증 모아보기") {
                            isShowingCreateView = true
                        }
                        .padding()
                        .font(.yjObangBold15)
                        .foregroundColor(Color.black)
                    }
                }
                Spacer().frame(height: 65)
                StatsCardView()
                
                Spacer().frame(height: 65)
                
                // "만들기" 버튼과 해당하는 NavigationLink
                NavigationLink(destination: ReceiptsView(), isActive: $isShowingReceiptsView) {
                    Button("만들기") {
                        isShowingReceiptsView = true
                    }
                    .foregroundColor(.white)
                    .font(.pretendardSemiBold18)
                    .padding()
                    .frame(width: 335,height: 52)
                    .background(Color.homeRed)
                    .cornerRadius(3)
                    .padding(.bottom,30)
                    
                }
                
                
                Spacer()
                
                
            }
            .font(.system(size: 16))
            .foregroundColor(.customGray2)
            .padding()
            .navigationBarTitle("", displayMode: .inline)
            
            .onAppear {
                homeViewModel.fetchTrips()  // 뷰가 나타날 때 데이터를 로드합니다.
            }
           
        }
    }
}

struct StatsCardView: View {
    @EnvironmentObject var homeViewModel: HomeViewModel
    //@EnvironmentObject var sharedViewModel: SharedViewModel
    
    var topColor: Color = .homeRed
    var textColor: Color = .white // 상단 바에 사용할 텍스트 색상
    
    var body: some View {
        VStack(spacing: 0) {
            //ZStack{
            // 상단 색상 바
            Image("BillView")
                .resizable()
                .scaledToFit()
                .frame(width: 314.09,height: 379.52)
            
        }
        .onAppear {
            homeViewModel.fetchTrips()  // 뷰가 나타날 때 데이터를 로드합니다.
        }
    }
}



struct ReceiptCell: View {
    let item: Item
    @EnvironmentObject var homeViewModel: HomeViewModel
    //@EnvironmentObject var sharedViewModel: SharedViewModel
    
    var body: some View {
        
        HStack(spacing: 15) {
            
            
            VStack(alignment: .leading, spacing: 10) {
                HStack{
                    
                    
                    VStack{
                        
                        Text(item.startdate)
                            .font(.pretendardMedium11)
                            .foregroundColor(.black)
                        
                        
                        Text(item.enddate)
                            .font(.pretendardMedium11)
                            .foregroundColor(.black)
                    }
                    Rectangle()
                        .fill(Color.homeRed)
                        .frame(width: 1, height: 42)
                        .padding(.leading, 5)
                        .padding(.trailing, 0)
                    
                }
            }
            .padding(.bottom,20)
            .padding(.horizontal,20)
            
            
            
            VStack{
                HStack(spacing: 10) {
                    
                    Spacer()
                    
                    
                    
                    Text(item.tripName)
                        .font(.pretendardExtrabold14)
                        .foregroundColor(.black)
                        .zIndex(2)
                    
                    Rectangle()
                        .fill(Color.homeRed)
                        .frame(width: 1, height: 42)
                        .padding(.leading, 3)
                        .padding(.trailing, 0)
                    
                }.padding(.horizontal,20)
            }
            
            
            
            
        }
        .onAppear {
            homeViewModel.fetchTrips()  // 뷰가 나타날 때 데이터를 로드합니다.
        }
    }
}

// ReceiptDetailView 정의
struct ReceiptDetailView: View {
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
    @State private var saveButtonTitle = "확인"
    @State private var backButtonTitle = "뒤로"
    @State private var inputText: String = ""
    @State private var EndLocationend : String = ""
    @State private var selectedTab = 0
    @State private var showingGroup = false
    @State private var showingCompleteBillView = false
    
    
    
    var snapshotManager: SnapshotManager?
   
    
    
    var body: some View {
        
        ZStack{
            
            
            VStack {
                
                HStack {
                    Button(action: {
                        
                        if backButtonTitle == "뒤로" {
                            // 내보내기 기능 실행
                            isDialogActive = true
                            print("두ㅏㅣ로가기")
                        } else {
                            // "내보내기" 버튼의 기능을 실행
                            // let image: UIImage
                            let snapshotManager: SnapshotManager
                            switch selectedTab {
                            case 0:
                                
                                
                                //  image = ReceiptBillView1(item: item).snapshot()
                                //print("나 여기있어!")
                                snapshotManager = SnapshotManager(rootView: AnyView(ReceiptBillView1(item: item) .environmentObject(sharedViewModel)
                                                                                   ))
                                
                            case 1:
                                
                                //image = ReceiptView().snapshot()
                                snapshotManager = SnapshotManager(rootView: AnyView(ReceiptView()
                                                                                    // .environmentObject(sharedViewModel)
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
                        if saveButtonTitle == "확인" {
                            //
                            self.showingCompleteBillView = true
                            
                            
                            
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
                ForEach(0..<1) { index in
                    ReceiptBillView1(item: item)
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
                            numberOfTabs: 2,
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
            
            
            NavigationLink(destination : ReceiptGroupView( isCheckedStates: [false]),isActive: $showingGroup){
                EmptyView()
                
            }
            NavigationLink(destination: ReceiptCompleteDetailView(item: item), isActive: $showingCompleteBillView) {
                EmptyView()
            }
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


struct ReceiptBillView1 : View {
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
                        Text("\(item.tripName)") // 여기에 원하는 텍스트를 입력합니다.
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
                        
                       
                        
                        
                        
                        
                        Spacer()
                        
                        
                        TextField("여행의 기록을 한줄로 기록하세요", text: $sharedViewModel.text, prompt: Text("여행의 기록을 한줄로 기록하세요")
                            .foregroundColor(.Natural200))
                        
                        .foregroundColor(.gray500)
                        .font(.pretendardMedium14)
                        .padding(.bottom,30)
                        .multilineTextAlignment(.center)
                        .onChange(of: sharedViewModel.text) { newValue in
                                        // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                        if newValue.count > 14 {
                                            sharedViewModel.text = String(newValue.prefix(14))
                                        }
                                    }
                        
                        
                        
                        
                        
                        
                        
                        
                        VStack(alignment:.center,spacing:0){
                            HStack(alignment:.center,spacing:1)
                            {
                                
                                Image("Locationred")
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 19, height: 19)
                                
                                
                                
                                
                                
                                TextFieldDynamicWidth(title: "여행의 시작은 여기부터", text: $sharedViewModel.inputText, onEditingChanged: { isEditing in
                                    
                                }, onCommit: {
                                    
                                })
                                
                                .font(.pretendardMedium14)
                                .foregroundColor(.homeRed)
                                .onChange(of: sharedViewModel.inputText) { newValue in
                                                // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                                if newValue.count > 14 {
                                                    sharedViewModel.inputText = String(newValue.prefix(14))
                                                }
                                            }
                                
                                
                                
                            }
                            .frame(maxWidth: .infinity)
                            
                            
                            
                            HStack{
                                
                                TextField("출발지",text:$sharedViewModel.StartLocatio,prompt: Text("출발지")
                                    .foregroundColor(.Natural200))
                                
                                
                                .foregroundColor(.homeRed)  // 글씨
                                .font(.pretendardExtrabold45)
                                
                                .multilineTextAlignment(.center)
                                .onChange(of: sharedViewModel.StartLocatio) { newValue in
                                                // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                                if newValue.count > 7 {
                                                    sharedViewModel.StartLocatio = String(newValue.prefix(7))
                                                }
                                            }
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
                                
                                TextFieldDynamicWidth(title: "기억속에 오래 저장할", text: $sharedViewModel.EndLocationend, onEditingChanged: { isEditing in
                                    
                                }, onCommit: {
                                    
                                })
                                .font(.pretendardMedium14)
                                .foregroundColor(.homeRed)
                                .onChange(of: sharedViewModel.EndLocationend) { newValue in
                                                // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                                if newValue.count > 14 {
                                                    sharedViewModel.EndLocationend = String(newValue.prefix(14))
                                                }
                                            }
                            }
                            
                            
                            
                            HStack{
                                
                                TextField("도착지",text:$sharedViewModel.EndLocation,prompt: Text("도착지")
                                    .foregroundColor(.Natural200))
                                .font(.pretendardExtrabold45)
                                .foregroundColor(.homeRed)  // 글씨
                                .multilineTextAlignment(.center)
                                .onChange(of: sharedViewModel.EndLocation) { newValue in
                                                // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                                if newValue.count > 7 {
                                                    sharedViewModel.EndLocation = String(newValue.prefix(7))
                                                }
                                            }
                                
                            }
                        }
                        
                        Spacer()
                        Image("cut")
                            .padding(.bottom,10)
                        
                        StatsView( item: item)
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
        .onAppear{
            sharedViewModel.fetchTripDetails(tripId: item.id)
        }
        
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


struct ReceiptsView: View {
    @EnvironmentObject var homeViewModel: HomeViewModel
    @StateObject var billListViewModel = BillListViewModel()
    @Environment(\.presentationMode) var presentationMode
    @State private var isLoading = false // 로딩 상태 관리
    @State private var selectedItem: Item? // 선택한 아이템 저장
    
    
    var body: some View {
        ZStack {
            // 로딩 뷰 조건부 표시
                        if isLoading {
                            LoadingViewBill()
                        }
            
            VStack {
                Button(action: {
                    self.presentationMode.wrappedValue.dismiss()
                }) {
                    HStack {
                        Text("뒤로")
                            .padding(.horizontal, 20)
                            .padding()
                            .font(.yjObangBold15)
                            .tint(Color.black)
                        Spacer()
                    }
                }
                
                ScrollView {
                    LazyVStack(spacing: 5) {
                        ForEach(homeViewModel.items) { item in
                            Button(action: {
                                selectedItem = item
                                isLoading = true
                                triggerLoading()
                            }) {
                                ReceiptCell(item: item)
                            }
                            .padding(.vertical, 10)
                            CustomHomeSubDivider()
                        }
                    }
                }
            }
            .onAppear {
                homeViewModel.fetchTrips()
            }
            
            // 자동 이동 로직
            if let selectedItem = selectedItem {
                
                NavigationLink("", destination: ReceiptDetailView(item: selectedItem)
                    .environmentObject(billListViewModel), isActive: $isLoading)
                
            }
        }
        .navigationBarBackButtonHidden()
    }
    


    // 로딩 트리거 함수
    func triggerLoading() {
        isLoading = true
        DispatchQueue.main.asyncAfter(deadline: .now()) {
            isLoading = false // 이 부분에서 NavigationLink가 활성화됩니다.
        }
    }
}

//
struct LoadingViewBill: View {
    var body: some View {
        VStack {
 
            ProgressView()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.black.opacity(0.5))
    }
}

struct CustomTabIndicator: View {
    var numberOfTabs: Int
    var selectedTab: Int
    var accentColor: Color
    var inactiveColor: Color
    
    
    var body: some View {
        HStack {
            ForEach(0..<numberOfTabs, id: \.self) { index in
                Rectangle()
                    .fill(index == selectedTab ? accentColor : inactiveColor)
                    .frame(width:20,height: 3)
                    .cornerRadius(3)
            }
        }
        // 추가적인 스타일링은 여기에...
    }
}

struct TextFieldDynamicWidth: View {
    let title: String
    @Binding var text: String
    let onEditingChanged: (Bool) -> Void
    let onCommit: () -> Void
    // @EnvironmentObject var sharedViewModel: SharedViewModel
    
    @State private var textRect = CGRect()
    
    var body: some View {
        ZStack {
            Text(text == "" ? title : text).background(GlobalGeometryGetter(rect: $textRect)).layoutPriority(1).opacity(0)
            HStack {
                TextField(title, text: $text, onEditingChanged: onEditingChanged, onCommit: onCommit)
                    .frame(width: textRect.width)
            }
        }
    }
}


struct GlobalGeometryGetter: View {
    @Binding var rect: CGRect
    // @EnvironmentObject var sharedViewModel: SharedViewModel
    
    var body: some View {
        return GeometryReader { geometry in
            self.makeView(geometry: geometry)
        }
    }
    
    func makeView(geometry: GeometryProxy) -> some View {
        DispatchQueue.main.async {
            self.rect = geometry.frame(in: .global)
        }
        
        return Rectangle().fill(Color.clear)
    }
}




struct StatsView: View {
    //  @EnvironmentObject var sharedViewModel: SharedViewModel
    @ObservedObject var viewModel = SharedViewModel()
    var item : Item
    var body: some View {
        
        HStack(spacing:30) {
            VStack(spacing: 10) {
                Text("여행 카드")
                    .font(.pretendardMedium11)
                    .foregroundColor(.gray500)
                    .multilineTextAlignment(.center)
               
                    Text("1")
                        .font(.pretendardExtrabold14)
                        .foregroundColor(.homeRed)
                        .multilineTextAlignment(.center)
                
                
                Text("여행 날짜")
                    .font(.pretendardMedium11)
                    .foregroundColor(.gray500)
                    .multilineTextAlignment(.center)
                Text("2024. 05. 05")
                    .font(.pretendardMedium11)
                    .foregroundColor(.homeRed)
                    .multilineTextAlignment(.center)
                Text("2024. 05. 06")
                    .font(.pretendardMedium11)
                    .foregroundColor(.homeRed)
                    .multilineTextAlignment(.center)
            }
            
            VStack(alignment: .leading,spacing: 9) {
                Text("여행 감정")
                    .font(.pretendardMedium11)
                    .foregroundColor(.gray500)
                //.padding(.vertical,10)
                    .padding(.top,25)
                    .padding(.bottom,5)
                
                HStack{
                    ProgressView(value: 0.99).frame(width: 109,height: 15)
                        .cornerRadius(3)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .tint(.homeRed)
                    
                    Image("netral")
                    
                    Text("99%")
                        .font(.pretendardMedium11)
                        .foregroundColor(.homeRed)
                        .frame(width: 30)
                    
                }
                
                HStack{
                    ProgressView(value: 0.0).frame(width: 109,height: 15)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .tint(.Natural300)
                    Image("sad")
                    Text("0%")
                        .font(.pretendardMedium11)
                        .frame(width: 30)
                    
                }
                
                HStack{
                    ProgressView(value: 0.0).frame(width: 109,height: 15)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .cornerRadius(3)
                        .tint(.Natural300)
                    Image("fun")
                    Text("0%")
                        .font(.pretendardMedium11)
                        .frame(width: 30)
                    
                }
                HStack{
                    ProgressView(value: 0.0).frame(width: 109,height: 15)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .cornerRadius(3)
                        .tint(.Natural300)
                    Image("angry")
                    Text("0%")
                        .font(.pretendardMedium11)
                        .frame(width: 30)
                    
                }
                Spacer().frame(height: 16)
            }
            
        }
        
        
        .onAppear{
            viewModel.fetchTripDetails(tripId: item.id)
        }
        
        
    }
       
}


struct CustomDialogBill: View {
    @Binding var isActive: Bool
    
    let title: String
    let message: String
    let yesAction: () -> Void
    let noAction: () -> Void
    @State private var showingCustomAlert = false
    
    @State private var offset: CGFloat = 1000
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        
        ZStack{
            Color(showingCustomAlert ? .black : .black)
                .opacity(showingCustomAlert ? 1.0 : 0.5)
                .edgesIgnoringSafeArea(.all)
                .animation(.easeInOut, value: showingCustomAlert)
            
            VStack {
                
                
                Text(message)
                
                    .font(.pretendardMedium14)
                    .multilineTextAlignment(.center)
                    .foregroundColor(.gray500)
                    .padding(.bottom)
                
                HStack { // 버튼 사이 간격을 0으로 조정
                    Button {
                        yesAction()
                        close()
                        presentationMode.wrappedValue.dismiss()
                    } label: {
                        ZStack {
                            
                            Text("나갈게요")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 36) // 버튼의 크기 조절
                    }
                    
                    Rectangle() // 빨간색 세로줄 추가
                        .fill(Color.gray500)
                        .frame(width: 2, height: 20)
                    
                    Button {
                        noAction()
                        close()
                    } label: {
                        ZStack {
                            
                            Text("돌아갈게요")
                                .font(.yjObangBold15)
                                .foregroundColor(Color.black)
                            
                        }
                        .frame(width: 116, height: 36) // 버튼의 크기 조절
                    }
                }.padding(.bottom,10)
                
            }
            
            .frame(width: 280, height: 144) // 다이얼로그의 크기 조절
            
            .background(.homeBack)
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


struct CustomDialogBillComplete: View {
    @Binding var isActive: Bool
    
    let title: String
    let message: String
    let yesAction: () -> Void
    let noAction: () -> Void
    @State private var showingCustomAlert = false
    
    @State private var offset: CGFloat = 0
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        
        ZStack{
            Color.black
                .opacity(showingCustomAlert ? 1.0 : 0.5) // 투명도 조정
                .onTapGesture {
                    close() // 배경 탭 시 다이얼로그 닫기
                }
                .animation(.easeInOut, value: showingCustomAlert)
                .edgesIgnoringSafeArea(.all)
            
            VStack {
                Text(title)
                    .font(.pretendardExtrabold16)
                    .multilineTextAlignment(.center)
                    .foregroundColor(.homeRed)
                    .padding(.bottom)
                
                Text(message)
                    .font(.pretendardMedium14)
                    .multilineTextAlignment(.center)
                    .foregroundColor(.gray500)
                    .padding(.bottom)
            }
            .frame(width: 280, height: 144) // 다이얼로그의 크기 조절
            .background(Color.homeBack)
            .clipShape(RoundedRectangle(cornerRadius: 0))
            .offset(y: offset) // 다이얼로그의 Y축 위치 조정
            .onAppear {
                DispatchQueue.main.asyncAfter(deadline: .now() + 2) { // 2초 후 실행
                    close() // 다이얼로그 닫기
                }
            }
        }
    }
    
    func close() {
        withAnimation(.spring()) {
            offset = 1000 // 다이얼로그가 아래로 이동하며 사라짐
            isActive = false  // 다이얼로그 비활성화
        }
    }
}



struct ReceiptView: View {
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
                TextField("출발지",text:$sharedViewModel.tripRecord,prompt: Text("여행의 기록을 한줄로 기록하세요 :)").foregroundColor(.gray500))
                    .font(.pretendardMedium14)
                    .foregroundColor(.gray500)  // 글씨
                    .onChange(of: sharedViewModel.tripRecord) { newValue in
                                   // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                   if newValue.count > 14 {
                                       sharedViewModel.tripRecord = String(newValue.prefix(14))
                                   }
                               }
                
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
                    
                    TextFieldDynamicWidth(title: "여행의 시작은 여기부터", text: $sharedViewModel.tripExplaneStart, onEditingChanged: { isEditing in
                        
                    }, onCommit: {
                        
                    })
                    .font(.pretendardMedium14)
                    .foregroundColor(.gray600)
                    .onChange(of: sharedViewModel.tripExplaneStart) { newValue in
                                   // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                   if newValue.count > 14 {
                                       sharedViewModel.tripExplaneStart = String(newValue.prefix(14))
                                   }
                               }
                }.frame(maxWidth: .infinity)
                    .padding(.bottom,8)
                
                
                HStack{
                    TextField("출발지",text:$sharedViewModel.tripnameStart,prompt: Text("출발지").foregroundColor(.Natural200))
                        .font(.pretendardExtrabold45)
                        .foregroundColor(.black)  // 글씨
                        .multilineTextAlignment(.center)
                        .onChange(of: sharedViewModel.tripnameStart) { newValue in
                                       // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                       if newValue.count > 7 {
                                           sharedViewModel.tripnameStart = String(newValue.prefix(7))
                                       }
                                   }
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
                    
                    TextFieldDynamicWidth(title: "기억속에 오래 저장할", text: $sharedViewModel.tripExplaneEnd, onEditingChanged: { isEditing in
                        
                    }, onCommit: {
                        
                    })
                    .font(.pretendardMedium14)
                    .foregroundColor(.gray600)
                    .onChange(of: sharedViewModel.tripExplaneEnd) { newValue in
                                   // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                   if newValue.count > 14 {
                                       sharedViewModel.tripExplaneEnd = String(newValue.prefix(14))
                                   }
                               }
                }  .padding(.bottom,8)
                
                
                
                HStack{
                    
                    TextField("도착지",text:$sharedViewModel.tripnameEnd,prompt: Text("도착지").foregroundColor(.Natural200))
                        .font(.pretendardExtrabold45)
                        .foregroundColor(.black)  // 글씨
                        .multilineTextAlignment(.center)
                        .onChange(of: sharedViewModel.tripnameEnd) { newValue in
                                       // 텍스트가 최대 길이를 초과하는 경우 잘라냄
                                       if newValue.count > 7 {
                                           sharedViewModel.tripnameEnd = String(newValue.prefix(7))
                                       }
                                   }
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


struct SentimentTrackerView: View {
    //@EnvironmentObject var sharedViewModel: SharedViewModel
    var body: some View {
        VStack(spacing:0){
            HStack{
                Text("전라도의 선유도")
                    .font(.pretendardMedium14)
                    .padding(.bottom,15)
                    .multilineTextAlignment(.center)
            }
            
            HStack{
                Spacer().frame(width: 142)
                Text("여행 감정")
                    .foregroundColor(.gray500)
                    .frame(width: 50)
                    .font(.pretendardMedium11)
                Spacer().frame(width: 50)
                Text("카드 갯수")
                    .foregroundColor(.gray500)
                    .font(.pretendardMedium11)
                    .frame(width: 50)
                Spacer().frame(width: 66)
            }
            HStack{
                Spacer()
                VStack{
                    HStack{
                        Image("netral")
                        ProgressView(value: 0.6).frame(width: 109,height: 15)
                            .cornerRadius(3)
                            .scaleEffect(x: 1, y: 1, anchor: .center)
                            .tint(.homeRed)
                    }
                    HStack{
                        Image("sad")
                        ProgressView(value: 0.6).frame(width: 109,height: 15)
                            .cornerRadius(3)
                            .scaleEffect(x: 1, y: 1, anchor: .center)
                            .tint(.black)
                    }
                    HStack{
                        Image("fun")
                        ProgressView(value: 0.6).frame(width: 109,height: 15)
                            .cornerRadius(3)
                            .scaleEffect(x: 1, y: 1, anchor: .center)
                            .tint(.Basic)
                    }
                    HStack{
                        Image("angry")
                        ProgressView(value: 0.6).frame(width: 109,height: 15)
                            .cornerRadius(3)
                            .scaleEffect(x: 1, y: 1, anchor: .center)
                            .tint(.green)
                    }
                }
                Spacer().frame(width: 48)
                VStack {
                    Spacer().frame(height: 27)
                    Text("27")
                        .foregroundColor(.homeRed)
                        .font(.yjObangBold20)
                        .frame(width: 60,height: 60)
                        .background(RoundedRectangle(cornerRadius: 8).stroke(Color.black, lineWidth: 2))
                    Spacer().frame(height: 25)
                    HStack{
                       
                        Text("2024 .03 .05/2024 .03 .06")
                            .frame(width: 150)
                            .font(.pretendardMedium11)
                            .tint(.natural500)
                            .padding(.horizontal,10)
                        Spacer()
                        
                    }
                    
                }
                .frame(width: 60, height: 60)
                .padding(.trailing,50)
      
                
            }
            
            
            
           
        }
        Spacer().frame(height: 30)
    }
}

