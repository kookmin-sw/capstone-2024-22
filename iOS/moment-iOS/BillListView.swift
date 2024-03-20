//
//  MemoListView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI

struct BillListView: View {
    
    @EnvironmentObject private var pathModel: PathModel
    @EnvironmentObject private var billListViewModel : BillListViewModel
    @EnvironmentObject private var homeBaseViewModel : HomeBaseViewModel
    
    
    var body: some View {
        
        ZStack{
            VStack{
                
                AnnouncementView()
                
                
            }
            
        }
        
    }
}

struct ReceiptGroupView: View {
    var body: some View {
        ScrollView{
            VStack{
                HStack
                {
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                }
                HStack
                {
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                }
                HStack
                {
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                    Rectangle()
                        .fill(Color.gray)
                        .frame(height: 200)
                        .padding()
                        .overlay(
                            Image(systemName: "pencil.tip")
                                .font(.largeTitle)
                                .foregroundColor(.customGray2)
                        )
                        .cornerRadius(10)
                }
            }
        }
        .navigationBarTitle("새 예시", displayMode: .inline)
    }
}



private struct AnnouncementView: View {
    @EnvironmentObject private var calendarViewModel: CalendarViewModel
    @EnvironmentObject private var billListViewModel: BillListViewModel
    
    // NavigationLink 활성화를 위한 State 변수들
    @State private var isShowingCreateView = false
    @State private var isShowingReceiptsView = false
    
    var body: some View {
        NavigationView {
            VStack(spacing: 15) {
                Spacer()
                HStack{
                    Spacer()
                    NavigationLink(destination: ReceiptGroupView(), isActive: $isShowingCreateView) {
                        Button("영수증 모아보기") {
                            isShowingCreateView = true
                        }
                        .foregroundColor(.black)
                        .padding()
                    }
                }
                // 큰 사각형과 시스템 이미지 추가
                Rectangle()
                    .fill(Color.gray)
                    .frame(height: 500)
                    .overlay(
                        Image(systemName: "pencil.tip")
                            .font(.largeTitle)
                            .foregroundColor(.customGray2)
                    )
                    .cornerRadius(10)
                
                Spacer().frame(height: 10)
                
                // "만들기" 버튼과 해당하는 NavigationLink
                NavigationLink(destination: ReceiptsView(), isActive: $isShowingReceiptsView) {
                    Button("만들기") {
                        isShowingReceiptsView = true
                    }
                    .foregroundColor(.white)
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color.blue)
                    .cornerRadius(10)
                }
                
                
                Spacer()
                
                
            }
            .font(.system(size: 16))
            .foregroundColor(.customGray2)
            .padding()
            .navigationBarTitle("", displayMode: .inline)
        }
    }
}





struct ReceiptsView: View {
    var body: some View {
        NavigationView {
            VStack {
                // 테두리만 있는 사각형 안의 텍스트
                NavigationLink(destination: ReceiptDetailView()) {
                    Text("여행1")
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(RoundedRectangle(cornerRadius: 10).stroke(Color.blue, lineWidth: 2))
                        .foregroundColor(.black)
                }
                .padding() // 네비게이션 링크에 패딩을 적용
            }
            .navigationBarTitle("영수증", displayMode: .inline)
        }
    }
}

struct ReceiptDetailView: View {
    var body: some View {
        Rectangle()
            .fill(Color.gray)
            .frame(height: 500)
            .overlay(
                Image(systemName: "pencil.tip")
                    .font(.largeTitle)
                    .foregroundColor(.customGray2)
            )
            .cornerRadius(10)
            .navigationBarTitle("영수증 상세", displayMode: .inline)
    }
}




#Preview {
    BillListView()
        .environmentObject(PathModel())
        .environmentObject(BillListViewModel())
}
