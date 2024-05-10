//
//  CustomSlider.swift
//  moment-iOS
//
//  Created by 양시관 on 5/11/24.
//

import Foundation
import SwiftUI
import UIKit
//
//struct CustomSlider: UIViewRepresentable {
//    @Binding var value: Float
//    var range: (Double, Double)
//
//    func makeUIView(context: Context) -> UISlider {
//        let slider = UISlider(frame: .zero)
//        slider.minimumValue = Float(range.0)
//        slider.maximumValue = Float(range.1)
//        slider.value = Float(value)
//        slider.addTarget(
//            context.coordinator,
//            action: #selector(Coordinator.valueChanged(_:)),
//            for: .valueChanged
//        )
//        slider.tintColor = .blue
//        slider.thumbTintColor = .red
//        slider.maximumTrackTintColor = .gray
//        slider.minimumTrackTintColor = .purple
//
//        return slider
//    }
//
//    func updateUIView(_ uiView: UISlider, context: Context) {
//        uiView.value = Float(value)
//    }
//
//    func makeCoordinator() -> Coordinator {
//        Coordinator(self)
//    }
//
//    class Coordinator: NSObject {
//        var slider: CustomSlider
//
//        init(_ slider: CustomSlider) {
//            self.slider = slider
//        }
//
//        @objc func valueChanged(_ sender: UISlider) {
//            slider.value = Float(sender.value)
//        }
//    }
//}
struct CustomSlider: UIViewRepresentable {
    @Binding var value: Float
    var range: (Double, Double)
    var thumbImageName: String  // 이미지 파일 이름을 저장하는 String 타입

    func makeUIView(context: Context) -> UISlider {
        let slider = UISlider(frame: .zero)
        slider.minimumValue = Float(range.0)
        slider.maximumValue = Float(range.1)
        slider.value = value

        // 이미지 설정
        if let image = UIImage(named: thumbImageName) {
            slider.setThumbImage(image, for: .normal)
        } else {
            print("Failed to load thumb image")
        }

        slider.addTarget(
            context.coordinator,
            action: #selector(Coordinator.valueChanged(_:)),
            for: .valueChanged
        )
        return slider
    }

    func updateUIView(_ uiView: UISlider, context: Context) {
        uiView.value = value
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    class Coordinator: NSObject {
        var slider: CustomSlider

        init(_ slider: CustomSlider) {
            self.slider = slider
        }

        @objc func valueChanged(_ sender: UISlider) {
            slider.value = Float(sender.value)
        }
    }
}
