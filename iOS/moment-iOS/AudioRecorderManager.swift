//
//  AudioRecorderManager.swift
//  moment-iOS
//
//  Created by 양시관 on 3/24/24.
//

import Foundation

import AVFoundation
import Alamofire
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
    @Published var currentAddress: String?
    

    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjMsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNDQ3MDczNCwiZXhwIjoxNzU3NjcwNzM0fQ.pddeumunqT4tiE2yGI9aWXkn0Kxo7XeB9kFfpwQftbM"
    
    override init() {
            super.init()
            locationManager.delegate = self
            locationManager.requestWhenInUseAuthorization()  // 사용자에게 위치 정보 사용 권한을 요청
        }
    
    func iso8601String(from date: Date) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        formatter.timeZone = TimeZone(secondsFromGMT: 0)  // UTC 시간대 사용
        return formatter.string(from: date)
    }
    func startRecording() {
        locationManager.startUpdatingLocation()
        //굳이 시작할때부터 계속 위치를 업데이트할 필요없음
        
        let fileURL = getDocumentsDirectory().appendingPathComponent("recording-\(Date().timeIntervalSince1970).m4a")// 녹음 파일 저장 경로 설정
        //녹음설정하기
        let settings = [
            AVFormatIDKey: Int(kAudioFormatMPEG4AAC),
            AVSampleRateKey: 12000,
            AVNumberOfChannelsKey: 1,
            AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue
        ]
        
        do {
            // AVAudioRecorder 인스턴스 생성 및 녹음 시작
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
        
        if let url = audioRecorder?.url, let location = currentLocation {
            self.recordedFiles.append(url)
            self.isRecording = false
            
            let fileManager = FileManager.default
            let documentsDirectory = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first!
            let recordingsDirectory = documentsDirectory.appendingPathComponent("Recordings")
            
            do {
                try fileManager.createDirectory(at: recordingsDirectory, withIntermediateDirectories: true, attributes: nil)
                let newFileURL = recordingsDirectory.appendingPathComponent(url.lastPathComponent)
                if fileManager.fileExists(atPath: newFileURL.path) {
                    try fileManager.removeItem(at: newFileURL)
                }
                try fileManager.moveItem(at: url, to: newFileURL)
                
                let group = DispatchGroup()
                var finalTemperature: String?
                var finalWeather: String?
                var finalAddress: String?
                
                group.enter()
                fetchWeatherData(latitude: location.coordinate.latitude, longitude: location.coordinate.longitude) { temperature, weather in
                    finalTemperature = temperature
                    finalWeather = weather
                    group.leave()
                }
                
                group.enter()
                geocodeLocation(location: location) { address in
                    finalAddress = address
                    group.leave()
                }
                
                group.notify(queue: .main) {
                    let recordedAt = self.iso8601String(from: Date())
                    if let temperature = finalTemperature, let weather = finalWeather, let address = finalAddress {
                        print("Date: \(recordedAt), Location: \(address), Temperature: \(temperature), Weather: \(weather)")
                        self.uploadRecordedData(recordFileURL: newFileURL, location: address, recordedAt: recordedAt, temperature: temperature, weather: weather, question: "오늘 날씨는 어때요?")
                        print(newFileURL)
                    } else {
                        print("Failed to fetch all required data.")
                    }
                }
            } catch {
                print("Failed to create directory or move file: \(error)")
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
            
            geocodeLocation(location: location) { address in
                if let address = address {
                    print("Resolved Address: \(address)")
                    // 필요한 추가 작업 수행
                } else {
                    print("Address resolution failed.")
                }
            }
        }
    }

    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Failed to find user's location: \(error.localizedDescription)")
    }
    
    func geocodeLocation(location: CLLocation, completion: @escaping (String?) -> Void) {
        let geocoder = CLGeocoder()
        geocoder.reverseGeocodeLocation(location) { placemarks, error in
            if let error = error {
                print("Geocoding failed: \(error.localizedDescription)")
                completion(nil)  // 오류가 발생하면 nil 반환
                return
            }
            
            if let placemarks = placemarks, let placemark = placemarks.first {
                let address = [
                    placemark.thoroughfare,
                    placemark.subThoroughfare,
                    placemark.locality,
                    placemark.administrativeArea,
                    placemark.country
                ].compactMap { $0 }.joined(separator: ", ")
                
                completion(address)  // 성공적으로 주소를 구성하면 반환
            } else {
                print("No address found.")
                completion(nil)  // 주소를 찾지 못하면 nil 반환
            }
        }
    }


    
    
    func fetchWeatherData(latitude: Double, longitude: Double, completion: @escaping (String?, String?) -> Void) {
        let apiKey = "040a03e00601edb517a2b4ba4ce5550d"
        let urlString = "https://api.openweathermap.org/data/2.5/weather?lat=\(latitude)&lon=\(longitude)&appid=\(apiKey)&units=metric"
        guard let url = URL(string: urlString) else {
            print("Invalid URL")
            completion(nil, nil)
            return
        }

        let task = URLSession.shared.dataTask(with: url) { data, response, error in
            guard let data = data, error == nil else {
                print("Error fetching weather data: \(error?.localizedDescription ?? "Unknown error")")
                completion(nil, nil)
                return
            }

            let weatherData = self.parseWeatherData(data: data)
            if let temperature = weatherData?.temperature, let weather = weatherData?.weather {
                completion(temperature, weather)
            } else {
                completion(nil, nil)
            }
        }
        task.resume()
    }

    
    
    func parseWeatherData(data: Data) -> (temperature: String, weather: String)? {
        do {
            // JSON 데이터를 딕셔너리로 변환합니다.
            if let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any],
               let main = json["main"] as? [String: Any],
               let weatherArray = json["weather"] as? [[String: Any]],
               let weather = weatherArray.first {
                let temperature = String(format: "%.1f", main["temp"] as! Double)  // 온도를 문자열로 변환
                let weatherDescription = weather["description"] as! String  // 날씨 상태를 문자열로 변환
                return (temperature, weatherDescription)
            }
        } catch {
            print("Failed to decode weather data: \(error.localizedDescription)")
        }
        return nil
    }


    func uploadRecordedData(recordFileURL: URL, location: String, recordedAt: String, temperature: String, weather: String, question: String) {
        let urlString = "http://211.205.171.117:8000/core/cardView/upload"  // 서버의 URL
      
        print(recordFileURL)
        
        let headers: HTTPHeaders = ["Authorization": authToken, "Accept": "application/json"]
        
        AF.upload(multipartFormData: { multipartFormData in
            // 녹음 파일을 멀티파트 폼 데이터에 추가
            multipartFormData.append(recordFileURL, withName: "recordFile", fileName: recordFileURL.lastPathComponent, mimeType: "audio/*")

            // JSON 메타데이터 만들기
            let jsonPart: [String: Any] = [
                "location": location,
                "recordedAt": recordedAt,
                "temperature": temperature,
                "weather": weather,
                "question": question
            ]
            
            if let jsonData = try? JSONSerialization.data(withJSONObject: jsonPart, options: []) {
                multipartFormData.append(jsonData, withName: "uploadRecord", mimeType: "application/json")
            }
            
        }, to: urlString, method: .post, headers: headers).responseJSON { response in
            switch response.result {
            case .success(let value):
                print("Upload successful: \(value)")
            case .failure(let error):
                print("Upload failed: \(error)")
            }
        }
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




//    func stopRecording() {
//        locationManager.stopUpdatingLocation()
//        audioRecorder?.stop()
//
//        if let url = audioRecorder?.url, let location = currentLocation {
//            self.recordedFiles.append(url)
//            self.isRecording = false
//
//            let fileManager = FileManager.default
//            let documentsDirectory = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first!
//            let recordingsDirectory = documentsDirectory.appendingPathComponent("Recordings")
//
//            do {
//                try fileManager.createDirectory(at: recordingsDirectory, withIntermediateDirectories: true, attributes: nil)
//                let newFileURL = recordingsDirectory.appendingPathComponent(url.lastPathComponent)
//                if fileManager.fileExists(atPath: newFileURL.path) {
//                    try fileManager.removeItem(at: newFileURL)
//                }
//                try fileManager.moveItem(at: url, to: newFileURL)
//                print("File moved to \(newFileURL.path)")
//
//                let group = DispatchGroup()
//                var finalTemperature: String?
//                var finalWeather: String?
//                var finalAddress: String?
//
//                group.enter()
//                fetchWeatherData(latitude: location.coordinate.latitude, longitude: location.coordinate.longitude) { temperature, weather in
//                    finalTemperature = temperature
//                    finalWeather = weather
//                    group.leave()
//                }
//
//                group.enter()
//                geocodeLocation(location: location) { address in
//                    finalAddress = address
//                    group.leave()
//                }
//
//                group.notify(queue: .main) {
//                    if let temperature = finalTemperature, let weather = finalWeather, let address = finalAddress {
//                        self.uploadRecordedData(recordFileURL: newFileURL, location: address, recordedAt: DateFormatter.localizedString(from: Date(), dateStyle: .medium, timeStyle: .short), temperature: temperature, weather: weather, question: "오늘 날씨는 어때요?")
//                        print("데이터가 성공적으로 업로드 되었습니다.")
//                    } else {
//                        print("데이터 전송이 실패하였습니다")
//                    }
//                }
//            } catch {
//                print("Failed to create directory or move file: \(error)")
//            }
//        }
//    }
