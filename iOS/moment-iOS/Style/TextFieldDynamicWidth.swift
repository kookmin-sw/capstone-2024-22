//
//  TextFieldDynamicWidth.swift
//  moment-iOS
//
//  Created by 양시관 on 4/18/24.
//

import Foundation
import SwiftUI

struct TextFieldDynamicWidth: View {
    let title: String
    @Binding var text: String
    let onEditingChanged: (Bool) -> Void
    let onCommit: () -> Void
    
    @State private var textRect = CGRect()
    
    var body: some View {
        ZStack {
            Text(text.isEmpty ? title : text)
                .background(GlobalGeometryGetter(rect: $textRect))
                .layoutPriority(1)
                .opacity(0)
            TextField(title, text: $text, onEditingChanged: onEditingChanged, onCommit: onCommit)
                .frame(width: textRect.width)
        }
    }
}
