//
//  BillComplete.swift
//  moment-iOS
//
//  Created by 양시관 on 5/8/24.
//

import Foundation
import SwiftUI
import UIKit


//TODO: -
struct ReceiptGroupView: View {
    @EnvironmentObject var sharedViewModel: SharedViewModel
    @EnvironmentObject var homeBaseViewModel : HomeBaseViewModel
    @Environment(\.presentationMode) var presentationMode
    @State private var isDeleteMode = false // 삭제 모드 상태
    @State private var selectedReceiptId: Int?
    @State private var isSelected = false // 체크박스 선택 여부
    @State private var isEditing = false // 편집 모드 상태
    @State private var showConfirmationDialog = false // 커스텀 다이얼로그 표시 여부
    @State private var navigateToTargetView = false
    @State var isCheckedStates: [Bool]
    @State private var checkboxStates: [Int: Bool] = [:]
    
    
    
    
    
    
    let columns: [GridItem] = Array(repeating: .init(.flexible()), count: 2)
    
    var body: some View {
        
        //TODO: - 여기서는 뒤로가기버튼의 위치가 AnnounceView 여야함
        NavigationView{
            VStack{
                HStack {
                    Button(action: {
                        if isEditing {
                            // 완료 버튼 클릭 시 수행할 액션
                            // 예: 편집 내용 저장
                            print("Editing completed")
                            isEditing.toggle() // 편집 상태를 비활성화
                            checkboxStates = [:] // 모든 체크박스 상태 초기화
                            
                        } else {
                            // 뒤로 버튼 클릭 시 수행할 액션
                            // 예: 뷰를 닫거나 이전 화면으로 이동
                            print("Going back")
                            presentationMode.wrappedValue.dismiss()
                        }
                    }) {
                        HStack {
                            Spacer().frame(width: 10)
                            Text(isEditing ? "완료" : "뒤로")
                                .font(.yjObangBold15)
                                .tint(Color.black)
                        }
                    }
                    
                    
                    
                    // presentationMode.wrappedValue.dismiss()
                    Spacer()
                    
                    if isEditing {
                        Button("삭제") {
                            
                            self.showConfirmationDialog = true
                            print("삭제")
                            let idsToDelete = checkboxStates.compactMap { $0.value ? $0.key : nil }
                            sharedViewModel.deleteReceipts(with: idsToDelete)
                        }.font(.yjObangBold15)
                            .tint(Color.black)
                        Spacer().frame(width: 10)
                    } else {
                        Button("편집") { // 편집모드인 삭제
                            isEditing.toggle()
                        }.font(.yjObangBold15)
                            .tint(Color.black)
                        Spacer().frame(width: 10)
                    }
                    
                    
                    
                    
                    
                    
                    
                }
                .padding()
                
                
                ScrollView {
                    LazyVGrid(columns: columns, spacing: 20) {
                        ForEach(sharedViewModel.receipts) { receipt in
                            ZStack {
                                // NavigationLink to navigate to detail view when a receipt is selected.
                                if receipt.receiptThemeType == "A" {
                                    NavigationLink(destination: ReceiptcompleteView(receipt: receipt, sharedViewModel: sharedViewModel), tag: receipt.id, selection: $selectedReceiptId) {
                                                                  EmptyView()
                                                              }
                                                          } else {
                                                              NavigationLink(destination: ReceiptCompleteviewB(receipt: receipt), tag: receipt.id, selection: $selectedReceiptId) {
                                                                  EmptyView()
                                                              }
                                                          }
                              
                               
                                
                                // Receipt view
                                Group {
                                    if receipt.receiptThemeType == "A" {
                                        ReceiptATypeView(receipt: receipt, isEditing: isEditing, isChecked: Binding(
                                            get: { checkboxStates[receipt.id, default: false] },
                                            set: { newValue in checkboxStates[receipt.id] = newValue }
                                        ))
                                    } else {
                                        ReceiptBTypeView(receipt: receipt, isEditing: isEditing, isChecked: Binding(
                                            get: { checkboxStates[receipt.id, default: false] },
                                            set: { newValue in checkboxStates[receipt.id] = newValue }
                                        ))
                                    }
                                }
                                .onTapGesture {
                                    if isEditing {
                                        checkboxStates[receipt.id] = !(checkboxStates[receipt.id] ?? false)
                                    } else {
                                        selectedReceiptId = receipt.id 
                                    }
                                }
                            }
                            .frame(width: 125, height: 244)
                            .padding()
                        }
                    }
                }
                
            }
        }
        .navigationBarBackButtonHidden()
        .onAppear(){
            sharedViewModel.fetchReceipts()
        }
    }
}

struct ReceiptBTypeDetailView: View {
    var receipt: Receipt
    
    var body: some View {
        Text("B Type Detail for \(receipt.mainDeparture)")
        // 상세 페이지 내용 구성
    }
}


struct ReceiptATypeView: View {
    var receipt: Receipt
    var isEditing: Bool
    @Binding var isChecked: Bool
    var topColor: Color = .homeRed
    var textColor: Color = .white
    
    var body: some View {
        ZStack(alignment: .bottomTrailing){
            VStack(spacing:0) {
                Rectangle()
                    .fill(topColor)
                    .frame(height: 20)
                    .overlay(
                        HStack{
                            Text("\(receipt.tripName)")
                                .foregroundColor(textColor)
                                .font(.pretendardMedium5)
                                .padding()
                            Spacer()
                            Image("LogoSmall")
                                .padding()
                            
                        }
                    )
                Rectangle()
                    .fill(Color.Secondary50)
                    .frame(height: 224)
                    .border(.gray500)
                    .overlay(
                        VStack(alignment: .center){
                            Spacer().frame(height:11)
                            
                            Text("\(receipt.mainDeparture)")//placeholder : 여행의 기록을 한줄로 기록하세요임
                                .foregroundColor(.gray500)
                                .font(.pretendardMedium5)
                                .padding(.top,4)
                                .padding(.bottom,13)
                                .multilineTextAlignment(.center)
                            
                            
                            VStack(alignment:.center,spacing: 0){
                                HStack(alignment:.center,spacing:1){
                                    Image("LocationSmall")//여기부터 하면됨
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width: 6,height: 6)
                                    
                                    Text("\(receipt.subDeparture)")
                                        .font(.pretendardMedium5)
                                        .foregroundColor(.homeRed)
                                }
                                .frame(maxWidth:.infinity)
                                
                                
                                HStack{
                                    Text("\(receipt.mainDestination)")
                                        .foregroundColor(.homeRed)  // 글씨
                                        .font(.pretendardExtrabold18)
                                        .multilineTextAlignment(.center)
                                }
                                
                            }
                            
                            Image("airplaneSmall")
                                .padding(.bottom,20)
                            
                            VStack(spacing:0){
                                HStack(alignment:.center,spacing: 0){
                                    Image("LocationSmall")
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width:6,height: 6)
                                    
                                    Text("\(receipt.subDestination)")
                                        .font(.pretendardMedium5)
                                        .foregroundColor(.homeRed)
                                }
                                
                                HStack{
                                    Text("\(receipt.oneLineMemo)")
                                        .font(.pretendardExtrabold18)
                                        .foregroundColor(.homeRed)  // 글씨
                                        .multilineTextAlignment(.center)
                                }
                            }.padding(.bottom,4)
                            //     Spacer()
                            Image("CutSmall")
                            // .padding()
                            
                            //TODO: - statsView 들어가야함
                            StatsSmallView(receipt: receipt)
                            Spacer()
                            
                        }
                        
                        
                    )
                
                
                
            }
            .cornerRadius(5)
            .overlay(
                RoundedRectangle(cornerRadius:3)
                    .stroke(Color.Secondary50,lineWidth: 1)
            )
            if isChecked {
                Color.black.opacity(0.3) // 반투명 검정색 오버레이
            }
            
            if isEditing {
                Checkbox(isChecked: .constant(isChecked))
                    .padding() // 체크박스 주변에 패딩 추가
                    .alignmentGuide(.trailing) { d in d[.trailing] }
                    .alignmentGuide(.bottom) { d in d[.bottom] }
                //                if isChecked == true {
                //
                //                }
            }
            
            
            
        }
        
    }
    
}

struct Checkbox: View {
    @Binding var isChecked: Bool
    
    var body: some View {
        Image(systemName: isChecked ? "checkmark.square.fill" : "square")
            .foregroundColor(isChecked ? .homeRed : .gray) // 선택 상태에 따라 색상 변경
            .onTapGesture {
                self.isChecked.toggle()
            }
    }
}


struct StatsSmallView : View {
    var receipt: Receipt
    // var pagination : Pagination
    @EnvironmentObject var sharedViewModel: SharedViewModel
    var body: some View{
        HStack(spacing:15) {
            VStack(spacing:2) {
                Text("여행 카드")
                    .font(.pretendardMedium5)
                    .foregroundColor(.gray500)
                    .multilineTextAlignment(.center)
                Text("\(receipt.numOfCard)")
                    .font(.yjObangBold4)
                    .foregroundColor(.homeRed)
                    .multilineTextAlignment(.center)
                    .padding(.bottom,5)
                
                Text("여행 날짜")
                    .font(.pretendardMedium5)
                    .foregroundColor(.gray500)
                    .multilineTextAlignment(.center)
                Text("\(receipt.stDate)")
                    .font(.pretendardMedium4)
                    .foregroundColor(.homeRed)
                    .multilineTextAlignment(.center)
                
                Text("\(receipt.edDate)")
                    .font(.pretendardMedium4)
                    .foregroundColor(.homeRed)
                    .multilineTextAlignment(.center)
                
                Spacer().frame(height: 8)
            }
            
            VStack(alignment:.leading,spacing: 2){
                Text("여행 감정")
                    .font(.pretendardMedium5)
                    .foregroundColor(.gray500)
                //.padding(.vertical,10)
                    .padding(.top,9)
                    .padding(.bottom,5)
                
                HStack{
                    ProgressView(value: 0.6).frame(width: 40,height: 3)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .cornerRadius(3)
                        .tint(.homeRed)
                    
                    Image("netralSmall")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 5,height: 5)
                    
                    Text("60%")
                        .font(.pretendardMedium4)
                        .foregroundColor(.homeRed)
                        .frame(width: 9)
                    
                }
                
                HStack{
                    ProgressView(value: 0.3).frame(width: 40,height: 3)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .cornerRadius(3)
                        .tint(.Natural300)
                    Image("sadSmall")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 5,height: 5)
                    Text("30%")
                        .font(.pretendardMedium4)
                        .frame(width: 9)
                    
                }
                
                HStack{
                    ProgressView(value: 0.5).frame(width: 40,height: 3)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .cornerRadius(3)
                        .tint(.Natural300)
                    Image("funSmall")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 5,height: 5)
                    Text("50%")
                        .font(.pretendardMedium4)
                        .frame(width: 9)
                    
                }
                HStack{
                    ProgressView(value: 0.2).frame(width: 40,height: 3)
                        .scaleEffect(x: 1, y: 2, anchor: .center)
                        .cornerRadius(3)
                        .tint(.Natural300)
                    Image("angrySmall")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 5,height: 5)
                    Text("20%")
                        .font(.pretendardMedium4)
                        .frame(width: 9)
                    
                }
                Spacer().frame(height: 16)
            }
            
        }
    }
}



struct ReceiptBTypeView: View {
    var receipt: Receipt
    var isEditing: Bool
    @Binding var isChecked: Bool
    
    
    var body: some View {
        ZStack(alignment: .bottomTrailing){
            VStack(spacing:0) {
                Spacer().frame(height: 9)
                HStack{
                    Spacer().frame(width: 20)
                    Text("\(receipt.mainDeparture)")
                        .font(.pretendardMedium5)
                        .foregroundColor(.gray500)
                    
                    Spacer()
                    
                }
                Spacer().frame(height: 35)
                
                VStack(alignment:.center,spacing: 0){
                    HStack(alignment:.center,spacing: 1){
                        Image("LocationSmall")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 6, height: 6)
                        
                        Text("\(receipt.subDeparture)")
                            .font(.pretendardMedium5)
                            .foregroundColor(.gray600)
                    }.frame(maxWidth: .infinity)
                        .padding(.bottom,4)
                    
                    HStack{
                        Text("\(receipt.mainDestination)")
                            .font(.pretendardExtrabold18)
                            .foregroundColor(.black)  // 글씨
                            .multilineTextAlignment(.center)
                    }
                    .padding(.bottom,5)
                }
                
                Image("subwaySmall")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 15,height: 34)
                    .padding(.bottom,5)
                
                VStack(spacing:0){
                    HStack(alignment:.center,spacing: 0)
                    {
                        Image("LocationSmall")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 6, height: 6)
                        
                        Text("\(receipt.subDestination)")
                            .font(.pretendardMedium5)
                            .foregroundColor(.gray600)
                        
                    }
                    .padding(.bottom,4)
                    
                    
                    
                    HStack{
                        Text("\(receipt.oneLineMemo)")
                            .font(.pretendardExtrabold18)
                            .foregroundColor(.black)  // 글씨
                            .multilineTextAlignment(.center)
                    }
                    .padding(.bottom,10)
                    Spacer()
                }
                SentimentTrackerSmallView(receipt: receipt)
                
                
            }
            .frame(width:124.34,height:244)
            .cornerRadius(5) // 모서리를 둥글게 처리합니다.
            .overlay(
                RoundedRectangle(cornerRadius: 3)
                    .stroke(Color.Natural500, lineWidth: 1)
                
            )
            .overlay(
                
                
                Rectangle() // 색칠할 부분의 모양
                    .fill(Color.homeRed) // 색칠할 색상
                    .frame(width: 10, height: 244), // 색칠할 영역의 크기
                alignment: .leading
                
            ) .overlay(
                Image("LogoVerticalSmall") // 다른 이미지 오버레이 추가
                    .resizable()
                    .scaledToFit()
                    .frame(width: 6, height: 233) // 이미지 프레임 설정
                    .offset(x:-58)
            )
            //오른족 얇은 부분의 사각형
            .overlay(
                Rectangle() // 색칠할 부분의 모양
                    .fill(Color.homeRed) // 색칠할 색상
                    .frame(width: 3.74, height: 223), // 색칠할 영역의 크기
                alignment: .init(horizontal: .trailing, vertical: .bottom)
            )
            .overlay(
                Rectangle() // 색칠할 부분의 모양
                    .offset(x:5,y:-101)
                    .fill(Color.homeRed) // 색칠할 색상
                    .frame(width: 115, height: 1) // 색칠할 영역의 크기
                
                
                
            )
            .overlay(
                Image("CircleDividerSmall")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 129.66, height: 4.48)
                    .offset(x:4,y:50)
            )
            if isChecked {
                Color.black.opacity(0.3) // 반투명 검정색 오버레이
            }
            
            if isEditing {
                Checkbox(isChecked: .constant(isChecked))
                    .padding() // 체크박스 주변에 패딩 추가
                    .alignmentGuide(.trailing) { d in d[.trailing] }
                    .alignmentGuide(.bottom) { d in d[.bottom] }
                //                if isChecked == true {
                //
                //                }
            }
            
            
        }
    }
}



struct SentimentTrackerSmallView : View {
    var receipt : Receipt
    
    var body: some View {
        VStack(spacing:0) {
            HStack{
                Text("\(receipt.tripName)")
                    .font(.pretendardMedium5)
                    .padding(.bottom,7)
                    .multilineTextAlignment(.center)
            }
            
            HStack{
                Spacer().frame(width:40)
                Text("여행 감정")
                    .foregroundColor(.gray500)
                    .frame(width: 20)
                    .font(.pretendardMedium5)
                Spacer().frame(width:15)
                Text("카드 갯수")
                    .foregroundColor(.gray500)
                    .font(.pretendardMedium5)
                    .frame(width:20)
                Spacer().frame(width:33)
                
            }
            HStack{
                // Spacer()
                VStack(spacing: 0){
                    HStack{
                        Image("netralSmallB")
                        ProgressView(value: 0.6).frame(width: 31.13,height: 1)
                            .cornerRadius(3)
                            .scaleEffect(x: 1, y: 1, anchor: .center)
                            .tint(.homeRed)
                    
                        
                    }
                    HStack{
                        Image("funSmallB")
                        ProgressView(value: 0.6).frame(width: 31.13,height: 1)
                            .cornerRadius(3)
                            .scaleEffect(x: 1, y: 1, anchor: .center)
                            .tint(.black)
                    }
                    HStack{
                        Image("sadSmallB")
                        ProgressView(value: 0.6).frame(width: 31.13,height: 1)
                            .cornerRadius(3)
                            .scaleEffect(x: 1, y: 1, anchor: .center)
                            .tint(.Basic)
                    }
                    HStack{
                        Image("angrySmallB")
                        ProgressView(value: 0.6).frame(width: 31.13,height: 1)
                            .cornerRadius(3)
                            .scaleEffect(x: 1, y: 1, anchor: .center)
                            .tint(.green)
                    }
                    
                }
                // Spacer().frame(width:10)
                VStack{
                    Spacer()
                    Text("\(receipt.numOfCard)")
                        .foregroundColor(.homeRed)
                        .font(.yjObangBold8)
                        .frame(width: 30,height: 30)
                        .background(RoundedRectangle(cornerRadius: 8).stroke(Color.black, lineWidth: 1))
                    Spacer()
                }
                
            }
        }
     
    }
}


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
    @State private var isCheckedStates = false
    
    var snapshotManager: SnapshotManager?
    
    
    
    var body: some View {
        
        ZStack{
            
            
            VStack {
                
                HStack {
                    
                    Button(action: {
                        presentationMode.wrappedValue.dismiss()
                    }) {
                        Text("수정")
                            .padding(.leading, 20)
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
                        .padding(.horizontal, 10)
                       
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
                            //TODO: - 완료버튼 부분기능부분임
                            
                            switch selectedTab {
                            case 0:
                                
                                //TODO: - 이제 여기에서 0번인경우에의 함수를 호출해줘야함
                                sharedViewModel.createReceipt(for: item.id, themeType: "A")
                                
                            case 1:
                                sharedViewModel.createReceipt(for: item.id, themeType: "B")
                                
                                
                            default:
                                return
                            }
                            
                            
                            //TODO: - 여기에다가 selectedTab 에 따라서 값을 따로 줘야하자나
                            
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
            
            //문제 있을수도
            NavigationLink(destination : ReceiptGroupView( isCheckedStates: [false]),isActive: $showingGroup){
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

