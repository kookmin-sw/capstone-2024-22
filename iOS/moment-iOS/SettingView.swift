//
//  SettingView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


import SwiftUI

struct SettingView: View {
    @EnvironmentObject private var homeBaseViewModel : HomeBaseViewModel
    var body: some View {
        //타이틀뷰
        VStack{
            Text("알림설정")
                .padding()
            Text("문의하기")
                .padding()
            Text("버전안내")
                .padding()
            Text("이동통신망 사용알림")
                .padding()
            
            
        }
        //총 탭 카운트 뷰
        //총 탭 무브뷰
        
    }
}
//
//// MARK: - 타이틀뷰
//
//private struct TitleView : View
//{
//    fileprivate var body: some View {
//        HStack{
//            Text("설정")
//                .font(.system(size: 30,weight: .bold))
//                .foregroundColor(.customBlack)
//
//            Spacer()
//
//
//        }
//
//        .padding(.horizontal,30)
//        .padding(.top,45)
//    }
//}
//
//private struct TotalTabCountView: View {
//    @EnvironmentObject private var homeBaseViewModel : HomeBaseViewModel
//    fileprivate var body: some View{
//
//        HStack{
//            Spacer()
//            TabCountView(title: "To do", count:homeBaseViewModel.todosCount )
//            Spacer()
//                .frame(width: 70)
//            TabCountView(title: "메모", count:homeBaseViewModel.memosCount )
//            Spacer()
//                .frame(width: 70)
//            TabCountView(title: "음성메모", count:homeBaseViewModel.voiceRecordersCount )
//            Spacer()
//        }
//    }
//}
//
//private struct TabCountView : View {
//    private var title : String
//    private var count : Int
//
//    fileprivate init(title: String,
//                     count: Int
//    ) {
//        self.title = title
//        self.count = count
//    }
//
//    fileprivate var body: some View {
//        VStack(spacing : 5){
//            Text(title)
//                .font(.system(size: 14))
//                .foregroundColor(.customBlack)
//
//            Text("\(count)")
//                .font(.system(size: 30,weight: .bold))
//                .foregroundColor(.customBlack)
//        }
//    }
//}
//
//private struct TotalTabMoveView: View {
//    @EnvironmentObject private var homeBaseViewModel : HomeBaseViewModel
//    fileprivate var body: some View {
//        VStack{
//
//            //각 탭 4개 이동 뷰 컴포넌트
//
//        }
//    }
//}
//
//private struct TabMoveView : View {
//    private var title : String
//    private var tabAction : () -> Void
//
//    fileprivate init(title: String,
//                     tabAction: @escaping () -> Void
//    ) {
//        self.title = title
//        self.tabAction = tabAction
//    }
//    fileprivate var body: some View {
//        Button(
//            action : tabAction,
//            label: {
//                HStack{
//                    Text(title)
//                        .font(.system(size: 14))
//                        .foregroundColor(.customBlack)
//
//                    Spacer()
//
//                    Image("arrowRight")
//                }
//            }
//        )
//        .padding(.all,20)
//    }
//
//}

#Preview {
    SettingView()
        .environmentObject(HomeBaseViewModel())
}
