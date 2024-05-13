//
//  CustomSlider.swift
//  moment-iOS
//
//  Created by 양시관 on 5/11/24.
//

import Foundation
import SwiftUI
import UIKit


import SwiftUI
import UIKit

struct CustomSlider: UIViewRepresentable {
    @Binding var value: Float
    var range: (Double, Double)
    var thumbImageName: String

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

        // 회색 트랙 설정
        let trackTintColor = UIColor.systemGray
        slider.minimumTrackTintColor = trackTintColor
        slider.maximumTrackTintColor = trackTintColor

        // 트랙 이미지 설정 (선택적)
        let minTrackImage = UIImage(named: "play")?.stretchableImage(withLeftCapWidth: 0, topCapHeight: 0)
        let maxTrackImage = UIImage(named: "play")?.stretchableImage(withLeftCapWidth: 0, topCapHeight: 0)
        slider.setMinimumTrackImage(minTrackImage, for: .normal)
        slider.setMaximumTrackImage(maxTrackImage, for: .normal)

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
