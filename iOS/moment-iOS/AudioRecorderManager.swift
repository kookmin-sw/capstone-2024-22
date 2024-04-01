//
//  AudioRecorderManager.swift
//  moment-iOS
//
//  Created by 양시관 on 3/24/24.
//

import Foundation

import AVFoundation

class AudioRecorderManager: NSObject, ObservableObject, AVAudioPlayerDelegate {
  /// 음성메모 녹음 관련 프로퍼티
  var audioRecorder: AVAudioRecorder?
  @Published var isRecording = false
  @Published var playbackProgress: Double = 0.0
  /// 음성메모 재생 관련 프로퍼티
  var audioPlayer: AVAudioPlayer?
  @Published var isPlaying = false
  @Published var isPaused = false
  
  /// 음성메모된 데이터
  var recordedFiles = [URL]()
}

// MARK: - 음성메모 녹음 관련 메서드
extension AudioRecorderManager {
  func startRecording() {
    let fileURL = getDocumentsDirectory().appendingPathComponent("recording-\(Date().timeIntervalSince1970).m4a")
    let settings = [
      AVFormatIDKey: Int(kAudioFormatMPEG4AAC),
      AVSampleRateKey: 12000,
      AVNumberOfChannelsKey: 1,
      AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue
    ]
    
    do {
      audioRecorder = try AVAudioRecorder(url: fileURL, settings: settings)
      audioRecorder?.record()
      self.isRecording = true
    } catch {
      print("녹음 중 오류 발생: \(error.localizedDescription)")
    }
  }
  
//  func stopRecording() {
//    audioRecorder?.stop()
//    self.recordedFiles.append(self.audioRecorder!.url)
//    self.isRecording = false
//      
//  }
    func stopRecording() {
        audioRecorder?.stop()
        if let url = audioRecorder?.url {
            self.recordedFiles.append(self.audioRecorder!.url)
            self.isRecording = false
            
            // 새로운 폴더 경로 생성
            let fileManager = FileManager.default
            let documentsDirectory = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first!
            let recordingsDirectory = documentsDirectory.appendingPathComponent("Recordings")
            
            do {
                // "Recordings" 폴더가 없으면 생성
                try fileManager.createDirectory(at: recordingsDirectory, withIntermediateDirectories: true, attributes: nil)
            } catch {
                print("Failed to create directory: \(error)")
            }
            
            // 녹음 파일을 "Recordings" 폴더로 이동
            let newFileURL = recordingsDirectory.appendingPathComponent(url.lastPathComponent)
            
            do {
                if fileManager.fileExists(atPath: newFileURL.path) {
                    // 동일한 이름의 파일이 이미 있으면 삭제
                    try fileManager.removeItem(at: newFileURL)
                }
                
                // 파일 이동
                try fileManager.moveItem(at: url, to: newFileURL)
                print("File moved to \(newFileURL.path)")
            } catch {
                print("Failed to move file: \(error)")
            }
        }
    }
  
  private func getDocumentsDirectory() -> URL {
    let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
    return paths[0]
      
  }
}

// MARK: - 음성메모 재생 관련 메서드
extension AudioRecorderManager {
  func startPlaying(recordingURL: URL) {
    do {
      audioPlayer = try AVAudioPlayer(contentsOf: recordingURL)
      audioPlayer?.delegate = self
      audioPlayer?.play()
      self.isPlaying = true
      self.isPaused = false
    } catch {
      print("재생 중 오류 발생: \(error.localizedDescription)")
    }
  }
  
  func stopPlaying() {
    audioPlayer?.stop()
    self.isPlaying = false
  }
  
  func pausePlaying() {
    audioPlayer?.pause()
    self.isPaused = true
  }
  
  func resumePlaying() {
    audioPlayer?.play()
    self.isPaused = false
  }
  
  func audioPlayerDidFinishPlaying(_ player: AVAudioPlayer, successfully flag: Bool) {
    self.isPlaying = false
    self.isPaused = false
  }
    
    private func updatePlaybackProgress() {
            guard let player = audioPlayer, player.duration > 0 else {
                return
            }

            Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] (timer) in
                guard let self = self, self.isPlaying else {
                    timer.invalidate()
                    return
                }
                self.playbackProgress = player.currentTime / player.duration
            }
        }
}
