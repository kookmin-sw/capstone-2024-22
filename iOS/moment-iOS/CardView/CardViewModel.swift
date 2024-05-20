//
//  CardViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/31/24.
//

import Foundation
import SwiftUI
import Alamofire

struct CardViewResponse: Codable {
    let status: Int
    let code: String
    let msg: String
    let detailMsg: String
    let data: CardViewData
}

struct CardViewData: Codable {
    let cardViews: [cardViews]
}

struct cardViews: Codable,Identifiable {
    let tripFileId: Int
    let recordedAt: String//Date
    let recordFileName: String
    let recordFileUrl: String//URL
    let location: String
    let recordFileLength: Int
    let weather: String
    let temperature: String
    let stt: String
    let happy: Double
    let sad: Double
    let angry: Double
    let neutral: Double
    let disgust: Double
    let question: String
    let recordFileStatus: String
    let imageUrls: [String]
    let id: Int
    var loved: Bool
    
    
}



struct EmotionDataCard {
    let name: String
    let score: Double
    let color: Color
    let image: String
}

extension String {
    func parseISODate() -> Date? {
        let dateFormatter = ISO8601DateFormatter()
        dateFormatter.formatOptions = [.withFullDate, .withTime, .withDashSeparatorInDate, .withColonSeparatorInTime]
        return dateFormatter.date(from: self)
    }
}



class CardViewModel: ObservableObject {
    @Published var cardItems: [cardViews] = []
    @Published var emotions: [EmotionDataCard] = []
    @Published var cardItemsLike: [LikeCardViewData] = []
    
//    init() {
//        updateEmotions(happy: 24.09, sad: 15.9, angry: 14.95, neutral: 21.14, disgust: 23.91)
//    }
    
    
//    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjIsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNTQyNDgzMiwiZXhwIjoxNzU4NjI0ODMyfQ.iHg2ACmOB_hzoSlwsTfzGc_1gn6OHYmAxD0b2wgqNJg"
    
    var authToken: String {
        get {
            // 키체인에서 토큰을 가져옵니다
            if let token = KeychainHelper.shared.getAccessToken() {
                return "Bearer \(token)"
            } else {
                // 토큰이 없는 경우 기본값 또는 빈 문자열을 반환합니다
                return ""
            }
        }
    }
    
    
     func updateEmotions(happy: Double, sad: Double, angry: Double, neutral: Double, disgust: Double) {
        let total = happy + sad + angry + neutral + disgust
        emotions = [
            EmotionDataCard(name: "Happy", score: happy / total, color: .Basic, image: "fun"),
            EmotionDataCard(name: "Sad", score: sad / total, color: .unsafeColor, image: "sad"),
            EmotionDataCard(name: "Angry", score: angry / total, color: .StrangeColor, image: "angry"),
            EmotionDataCard(name: "Neutral", score: neutral / total, color: .homeRed, image: "netral"),
            EmotionDataCard(name: "Disgust", score: disgust / total, color: .black, image: "disgustFace")
        ].sorted { $0.score > $1.score }
    }
    
    func fetchAllCardViews(tripFileId: Int) {
        
        print(tripFileId)
        let urlString = "http://211.205.171.117:8000/core/cardView/all/\(tripFileId)"
        let headers: HTTPHeaders = [
            "Authorization": authToken,
            "Content-Type": "application/json"
        ]
        
        let decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .iso8601  // ISO8601 포맷으로 날짜를 파싱하도록 설정
        decoder.keyDecodingStrategy = .convertFromSnakeCase  // snake_case to camelCase 자동 변환
        
        AF.request(urlString, headers: headers).responseDecodable(of: CardViewResponse.self, decoder: decoder) { response in
            if let statusCode = response.response?.statusCode{
                print("카드 상태코드 연결 되었어??: \(statusCode)")
                
            }
            
            switch response.result {
                
            case .success(let CardViewResponse)://네트워크 통신이 성공을 하면 일로타게되지 근데 여기서 파싱이 안되니까 지금 밑으로 안들어오고 바로 에러처리 쪽으로 넘어가는거자나
                print("Successfully fetched card views: \(CardViewResponse)")
                print("카드뷰스에 들어가는 데이터: \(cardViews.self)")
                print("카드뷰 리스폰스 \(CardViewResponse.self)")
                DispatchQueue.main.async {
                    self.cardItems = CardViewResponse.data.cardViews
                }
                for cardView in CardViewResponse.data.cardViews {
                    print("Record File Name: \(cardView.recordFileName)")
                    print("Location: \(cardView.location)")
                    print("Recorded At: \(cardView.recordedAt)")
                    print("Weather: \(cardView.weather)")
                    // 여기에 더 많은 필드를 추가하여 출력할 수 있습니다.
                }
                
            case .failure(let error):
                print("Error while fetching card views: \(error.localizedDescription)")
                if let data = response.data, let errorString = String(data: data, encoding: .utf8) {
                    print("Error details: \(errorString)")
                }
            }
        }
    }
    
    
    func formatDateAndTime(dateString: String) -> (dateString: String, timeString: String) {
        guard let date = dateString.parseISODate() else {
            return ("Invalid date", "Invalid time")
        }
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"  // 날짜 형식
        let formattedDate = dateFormatter.string(from: date)
        
        let timeFormatter = DateFormatter()
        timeFormatter.dateFormat = "HH:mm"  // 시간 형식
        let formattedTime = timeFormatter.string(from: date)
        
        return (formattedDate, formattedTime)
    }
    
    

    func updateCardViewLikeStatus(cardViewId: Int) {
        let urlString = "http://211.205.171.117:8000/core/cardView/like/\(cardViewId)"
        let headers: HTTPHeaders = [
            "Authorization": authToken,
            "Content-Type": "application/json"
        ]
        
        AF.request(urlString, method: .put, headers: headers).responseJSON { response in
            switch response.result {
            case .success(let value):
                if let json = value as? [String: Any], let status = json["status"] as? Int, status == 200 {
                    print("Update successful:", json)
                    self.updateLocalCardViewLovedStatus(cardViewId: cardViewId)
                }
            case .failure(let error):
                print("Error updating like status: \(error.localizedDescription)")
            }
        }
    }
    
    // 로컬 데이터를 업데이트하는 함수
    private func updateLocalCardViewLovedStatus(cardViewId: Int) {
        if let index = cardItems.firstIndex(where: { $0.id == cardViewId }) {
            DispatchQueue.main.async {
                self.cardItems[index].loved.toggle()
            }
        }
    }
    
    
    func fetchLikedCardViews() {
        let urlString = "http://211.205.171.117:8000/core/cardView/like"  // 서버 URL에 맞게 수정하세요.
        let headers: HTTPHeaders = [
            "Authorization": authToken,
            "Content-Type": "application/json"
        ]
        
        AF.request(urlString, headers: headers).responseDecodable(of: StateCardLike.self) { response in
            if let statusCode = response.response?.statusCode{
                print("즐겨찾기 상태코드 연결 되었어??: \(statusCode)")
                
            }
            switch response.result {
            case .success(let responseData):
                DispatchQueue.main.async {
                    self.cardItemsLike = responseData.data.cardViews
                    print("Liked card views fetched successfully.")
                    responseData.data.cardViews.forEach { cardView in
                        print("""
                                               ID: \(cardView.id),
                                               Trip File ID: \(cardView.tripFileId),
                                               Recorded At: \(cardView.recordedAt),
                                               Record File Name: \(cardView.recordFileName),
                                               Location: \(cardView.location),
                                               Weather: \(cardView.weather),
                                               Temperature: \(cardView.temperature),
                                               Happiness: \(cardView.happy),
                                               Sadness: \(cardView.sad),
                                               Anger: \(cardView.angry),
                                               Neutral: \(cardView.neutral),
                                               Disgust: \(cardView.disgust),
                                               STT: \(cardView.stt),
                                               Loved Status: \(cardView.loved)
                                               """)
                    }
                }
            case .failure(let error):
                print("Failed to fetch liked card views: \(error.localizedDescription)")
            }
        }
    }
    
    func uploadImages(_ images: [UIImage], to cardViewId: Int) {
        let url = "http://wasuphj.synology.me:8000/core/cardView/image/\(cardViewId)"
        let headers: HTTPHeaders = [
            "Authorization": authToken,
            "Content-Type": "multipart/form-data"
        ]

        AF.upload(multipartFormData: { multipartFormData in
            // Loop through the image array
            images.enumerated().forEach { index, image in
                if let imageData = image.jpegData(compressionQuality: 0.5) {
                    multipartFormData.append(imageData, withName: "images", fileName: "image\(index).jpg", mimeType: "image/jpeg")
                }
            }
        }, to: url, method: .post, headers: headers).responseJSON { response in
            switch response.result {
            case .success(let value):
                print("Image upload success: \(value)")
            case .failure(let error):
                print("Image upload failed: \(error.localizedDescription)")
            }
        }
    }

    
}


struct StateCardLike: Codable {
    let status: Int
    let code, msg, detailMsg: String
    let data: LikeClass
}

// MARK: - DataClass
struct LikeClass: Codable {
    let cardViews: [LikeCardViewData]
}

// MARK: - CardView
struct LikeCardViewData: Codable,Identifiable {
    let tripFileId: Int
    let recordedAt, recordFileName: String
    let recordFileUrl: String
    let location: String
    let recordFileLength: Int
    let weather, temperature, stt: String
    let happy, sad, angry, neutral: Double
    let disgust: Double
    let question, recordFileStatus: String
    let imageUrls: [String]
    let id: Int
    let loved: Bool
    
}
