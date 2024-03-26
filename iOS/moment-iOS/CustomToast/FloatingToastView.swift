//
//  FloatingToastView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/24/24.
//

import SwiftUI
import PopupView
//
 struct FloatingToastDeleteView: View {
     var body: some View {
         ZStack{
             HStack {
                 Spacer()
                 Text("녹음이 삭제되었습니다.")
                     .font(.caption)
                     .padding(.vertical, 10)
                 Spacer()
             }
             .background(Color.toastColor)
             .cornerRadius(4)
             .padding(.horizontal, 20)
             .offset(y: -90)
         }
     }
}

struct FloatingToastLoadView: View {
    var body: some View {
        ZStack{
            HStack {
                Spacer()
                Text("녹음이 저장되었습니다.")
                    .font(.caption)
                    .padding(.vertical, 10)
                Spacer()
            }
            .background(Color.toastColor)
            .cornerRadius(4)
            .padding(.horizontal, 20)
            .offset(y: -90)
        }
    }
}




#Preview {
    FloatingToastDeleteView()
}
