//
//  Player.swift
//  AudioPlayer
//
//  Created by Vinod Supnekar on 26/07/23.
//

import Foundation
import SwiftUI
import AVKit
import Combine
import AVFoundation



class AudioPlayer: ObservableObject {
    var player: AVPlayer?
    var playerItem: AVPlayerItem?
    var timeObserverToken: Any?
    
    @Published var isPlaying = false
    @Published var progress: Float = 0.0

    init(url: URL) {
        setupPlayer(url: url)
    }
    
    func setupPlayer(url: URL) {
        playerItem = AVPlayerItem(url: url)
        player = AVPlayer(playerItem: playerItem)
        
        // Observe the player item's completion
        NotificationCenter.default.addObserver(self, selector: #selector(playerDidFinishPlaying), name: .AVPlayerItemDidPlayToEndTime, object: playerItem)
        
        addPeriodicTimeObserver()
    }
    
    @objc func playerDidFinishPlaying(note: NSNotification) {
        DispatchQueue.main.async {
            self.progress = 0.0
            self.isPlaying = false
            self.player?.seek(to: CMTime.zero) // Reset the player to the beginning
        }
    }
    
    func playPauseAction() {
        guard let player = player else { return }
        
        if isPlaying {
            player.pause()
        } else {
            player.play()
        }
        isPlaying.toggle()
    }
    
    private func addPeriodicTimeObserver() {
        let interval = CMTime(seconds: 1, preferredTimescale: CMTimeScale(NSEC_PER_SEC))
        timeObserverToken = player?.addPeriodicTimeObserver(forInterval: interval, queue: .main) { [weak self] time in
            guard let self = self, let duration = self.playerItem?.duration else { return }
            let durationSeconds = CMTimeGetSeconds(duration)
            let currentTime = CMTimeGetSeconds(time)
            self.progress = Float(currentTime / durationSeconds)
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
        if let token = timeObserverToken {
            player?.removeTimeObserver(token)
        }
    }
}




struct CustomAudioPlayerView: View {
    @ObservedObject var audioPlayer: AudioPlayer

    var body: some View {
        VStack {
          
            ZStack{
                CustomSlider(value: $audioPlayer.progress, range: (0, 1), thumbImageName: "thumb")
                    .frame(width: 335)
            }
            Button(action: {
                audioPlayer.playPauseAction()
            }) {
                Image(systemName: audioPlayer.isPlaying ? "pause" : "play")
                  
                    .scaledToFit()
                    .frame(width: 24, height: 24)
            }
        }
        .padding()
    }
}
