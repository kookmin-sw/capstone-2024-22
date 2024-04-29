//
//  View+Extensions.swift
//  moment-iOS
//
//  Created by 양시관 on 4/17/24.
//

import Foundation
import SwiftUI

extension View {
    func snapshot() -> UIImage {
        let controller = UIHostingController(rootView: self)
        let view = controller.view
        
        let targetSize = CGSize(width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height) // 예시 크기
        view?.bounds = CGRect(origin: .zero, size: targetSize)

       // view?.bounds = CGRect(origin: .zero, size: targetSize)
        view?.backgroundColor = .clear
        
        let renderer = UIGraphicsImageRenderer(size: targetSize)
        return renderer.image { _ in
            view?.drawHierarchy(in: controller.view.bounds, afterScreenUpdates: true)
        }
    }
}
