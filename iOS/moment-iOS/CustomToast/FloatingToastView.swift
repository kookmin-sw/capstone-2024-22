//
//  FloatingToastView.swift
//  moment-iOS
//
//  Created by 양시관 on 3/24/24.
//

import SwiftUI
import PopupView
//
 struct FloatingToastView: View {
     var body: some View {
         ZStack{
             HStack {
                 Spacer()
                 Text("This is Floating")
                     .font(.headline)
                     .padding(.vertical, 10)
                 Spacer()
             }
             .background(Color.white)
             .cornerRadius(20)
             .padding(.horizontal, 20)
         }
     }
}

#Preview {
    FloatingToastView()
}
