//
//  TimerView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation

import SwiftUI



struct LikeView: View {
    @ObservedObject var viewModel = LikeViewModel()
    var day: Date
    var item: Item
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject var audioRecorderManager: AudioRecorderManager
    @State private var isDeleteMode = false // 삭제 모드 상태
    @State private var isSelected = false // 체크박스 선택 여부
    @State private var showConfirmationDialog = false // 커스텀 다이얼로그 표시 여부
    @ObservedObject var cardViewModel : CardViewModel

    var body: some View {
        ScrollView {
            VStack {
                
          Text("")
          Text("f")
            }
        }
    }
}
