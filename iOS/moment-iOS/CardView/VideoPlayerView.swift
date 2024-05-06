//
//  VideoPlayerView.swift
//  moment-iOS
//
//  Created by 양시관 on 5/6/24.
//

import Foundation
import SwiftUI
import AVKit
import UIKit
import Combine

struct VideoPlayerView: UIViewRepresentable {
    var url: URL
    @Binding var player: AVPlayer

    func makeUIView(context: Context) -> UIView {
        return PlayerUIView(player: player)
    }

    func updateUIView(_ uiView: UIView, context: Context) {
    }

    static func dismantleUIView(_ uiView: UIView, coordinator: ()) {
        // Cleanup if needed
    }

    class PlayerUIView: UIView {
        private var playerLayer: AVPlayerLayer?
        init(player: AVPlayer) {
            super.init(frame: .zero)
            let playerLayer = AVPlayerLayer(player: player)
            layer.addSublayer(playerLayer)
            self.playerLayer = playerLayer
        }

        required init?(coder: NSCoder) {
            fatalError("init(coder:) has not been implemented")
        }

        override func layoutSubviews() {
            super.layoutSubviews()
            playerLayer?.frame = bounds
        }
    }
}

