//
//  CardViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/31/24.
//

import Foundation
import SwiftUI
import Alamofire

struct CardItem1: Identifiable {
    var id: Int
    var tripFileId: Int
    var recordedAt: String
    var recordFileName: String
    var recordFileUrl: String
    var location: String
    var recordFileLength: Int
    var weather: String
    var temperature: String
    var stt: String
    var happy: Double
    var sad: Double
    var angry: Double
    var neutral: Double
    var disgust: Double  // 추가된 필드
    var question: String
    var loved: Bool
    var recordFileStatus: String
    var imageUrls: [String]  // 추가된 필드
    
    init(id: Int, tripFileId: Int, recordedAt: String, recordFileName: String, recordFileUrl: String, location: String, recordFileLength: Int, weather: String, temperature: String, stt: String, happy: Double, sad: Double, angry: Double, neutral: Double, disgust: Double, question: String, loved: Bool, recordFileStatus: String, imageUrls: [String]) {
        self.id = id
        self.tripFileId = tripFileId
        self.recordedAt = recordedAt
        self.recordFileName = recordFileName
        self.recordFileUrl = recordFileUrl
        self.location = location
        self.recordFileLength = recordFileLength
        self.weather = weather
        self.temperature = temperature
        self.stt = stt
        self.happy = happy
        self.sad = sad
        self.angry = angry
        self.neutral = neutral
        self.disgust = disgust
        self.question = question
        self.loved = loved
        self.recordFileStatus = recordFileStatus
        self.imageUrls = imageUrls
    }
}




class CardViewModel: ObservableObject {
    @Published var cardItems: [CardItem1] = []
    
    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjMsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNDQ3MDczNCwiZXhwIjoxNzU3NjcwNzM0fQ.pddeumunqT4tiE2yGI9aWXkn0Kxo7XeB9kFfpwQftbM"
    

//    func fetchAllCardViews(tripFileIds: Int) {
//      //  for tripFileId in tripFileIds {
//            let urlString = "http://wasuphj.synology.me:8000/core/cardView/all/\(tripFileId)"
//            let headers: HTTPHeaders = [
//                "Authorization": authToken,
//                "Content-Type": "application/json"
//            ]
//            
//            AF.request(urlString, headers: headers).responseJSON { response in
//                switch response.result {
//                case .success(let value):
//                    if let json = value as? [String: Any], let data = json["data"] as? [String: Any], let cardViews = data["cardViews"] as? [[String: Any]] {
//                        self.parseCardViews(cardViews)//서버통신은 완료된듯함
//                    }
//                case .failure(let error):
//                    print("Error while fetching card views for tripFileId \(tripFileId): \(error.localizedDescription)")
//                }
//            }
//        }
    


    
//    private func parseCardViews(_ cardViews: [[String: Any]]) {
//        for cardView in cardViews {
//            if let id = cardView["id"] as? Int,
//               let tripFileId = cardView["tripFileId"] as? Int,
//               let recordedAt = cardView["recordedAt"] as? String,
//               let recordFileName = cardView["recordFileName"] as? String,
//               let recordFileUrl = cardView["recordFileUrl"] as? String,
//               let location = cardView["location"] as? String,
//               let recordFileLength = cardView["recordFileLength"] as? Int,
//               let weather = cardView["weather"] as? String,
//               let temperature = cardView["temperature"] as? String,
//               let stt = cardView["stt"] as? String,
//               let happy = cardView["happy"] as? Double,
//               let sad = cardView["sad"] as? Double,
//               let angry = cardView["angry"] as? Double,
//               let neutral = cardView["neutral"] as? Double,
//               let disgust = cardView["disgust"] as? Double,
//               let question = cardView["question"] as? String,
//               let loved = cardView["loved"] as? Bool,
//               let recordFileStatus = cardView["recordFileStatus"] as? String,
//               let imageUrls = cardView["imageUrls"] as? [String] { // 새 필드
//                let newItem = CardItem1(id: id, tripFileId: tripFileId, recordedAt: recordedAt, recordFileName: recordFileName, recordFileUrl: recordFileUrl, location: location, recordFileLength: recordFileLength, weather: weather, temperature: temperature, stt: stt, happy: happy, sad: sad, angry: angry, neutral: neutral, disgust: disgust, question: question, loved: loved, recordFileStatus: recordFileStatus, imageUrls: imageUrls)
//                DispatchQueue.main.async {
//                    self.cardItems.append(newItem)
//                    print("Added new item: \(newItem)") // 로그 출력
//                }
//            }
//        }
//    }

    
    func updateCardViewLikeStatus(cardViewId: Int) {
           let urlString = "http://wasuphj.synology.me:8000/core/cardView/like/\(cardViewId)"
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
}
