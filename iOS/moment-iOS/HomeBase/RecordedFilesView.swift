//
//  RecordedFilesView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/23/24.
//

import SwiftUI

import SwiftUI

struct RecordedFilesView: View {
    @ObservedObject var viewModel: VoiceRecorderViewModel
    
    var body: some View {
        List(viewModel.recordedFiles, id: \.self) { fileUrl in
            Text(fileUrl.lastPathComponent)
                .onTapGesture {
                    // 여기에 파일을 재생하거나 다른 작업을 수행할 수 있습니다.
                    // 예를 들어, 파일 재생을 시작:
                    viewModel.startPlaying(recordingURL: fileUrl)
                }
        }
    }
}

struct RecordedFilesView_Previews: PreviewProvider {
    static var previews: some View {
        // 프리뷰를 위한 임시 VoiceRecorderViewModel 생성
        let tempViewModel = VoiceRecorderViewModel()
        // 테스트를 위해 몇 개의 더미 파일 URL을 추가할 수 있습니다.
    

        // RecordedFilesView에 임시 ViewModel을 전달하여 프리뷰 생성
        RecordedFilesView(viewModel: tempViewModel)
    }
}
