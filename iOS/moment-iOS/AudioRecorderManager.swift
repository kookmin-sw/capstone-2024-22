//
//  AudioRecorderManager.swift
//  moment-iOS
//
//  Created by 양시관 on 3/24/24.
//

import Foundation

import AVFoundation
import CoreLocation


class AudioRecorderManager: NSObject, ObservableObject, AVAudioPlayerDelegate,CLLocationManagerDelegate {
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
    var locationManager = CLLocationManager()
    @Published var currentLocation: CLLocation?

    
    override init() {
            super.init()
            locationManager.delegate = self
            locationManager.requestWhenInUseAuthorization()  // 사용자에게 위치 정보 사용 권한을 요청
        }
    func startRecording() {
        locationManager.startUpdatingLocation()
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
    
   
    func stopRecording() {
        locationManager.stopUpdatingLocation()
      
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
            if let location = currentLocation {
                        fetchWeatherData(latitude: location.coordinate.latitude, longitude: location.coordinate.longitude)
                    }
        }
        
    }
    
    private func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
        
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.last {
            currentLocation = location  // 최신 위치 정보를 저장
            print("Current location: \(location)")
            
            geocodeLocation(location: location)
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Failed to find user's location: \(error.localizedDescription)")
    }
    
    func geocodeLocation(location: CLLocation) {
        let geocoder = CLGeocoder()
        geocoder.reverseGeocodeLocation(location) { (placemarks, error) in
            if let error = error {
                print("역 지오코딩 실패: \(error.localizedDescription)")
                return
            }
            
            if let placemarks = placemarks, let placemark = placemarks.first {
                // 여기에서 placemark 객체에 접근하여 상세 주소 정보를 가져옵니다.
                let address = [
                    placemark.thoroughfare,  // 도로명
                    placemark.subThoroughfare,  // 도로명에 대한 상세 번호
                    placemark.locality,  // 도시
                    placemark.administrativeArea,  // 주, 도
                    placemark.country  // 나라
                ].compactMap { $0 }.joined(separator: ", ")
                
                print("현재 위치의 주소: \(address)")
            } else {
                print("받아온 위치 정보가 없습니다.")
            }
        }
    }
    func fetchWeatherData(latitude: Double, longitude: Double) {
        let apiKey = "040a03e00601edb517a2b4ba4ce5550d" // 여기에 발급받은 API 키를 입력하세요.
        let urlString = "https://api.openweathermap.org/data/2.5/weather?lat=\(latitude)&lon=\(longitude)&appid=\(apiKey)&units=metric"
        guard let url = URL(string: urlString) else {
            print("Invalid URL")
            return
        }

        let task = URLSession.shared.dataTask(with: url) { data, response, error in
            guard let data = data, error == nil else {
                print("Error fetching weather data: \(error?.localizedDescription ?? "Unknown error")")
                return
            }

            do {
                // JSON 데이터를 파싱합니다.
                let json = try JSONSerialization.jsonObject(with: data, options: [])
                print("Weather Data: \(json)")
            } catch {
                print("Failed to decode weather data: \(error.localizedDescription)")
            }
        }
        task.resume()
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
