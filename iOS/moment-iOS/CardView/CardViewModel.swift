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
    var question: String
    var loved: Bool
    var recordFileStatus: String
    
    
    
     init(id: Int, tripFileId: Int, recordedAt: String, recordFileName: String, recordFileUrl: String, location: String, recordFileLength: Int, weather: String, temperature: String, stt: String, happy: Double, sad: Double, angry: Double, neutral: Double, question: String, loved: Bool, recordFileStatus: String) {
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
         self.question = question
         self.loved = loved
         self.recordFileStatus = recordFileStatus
     }
 
}



class CardViewModel: ObservableObject {
    @Published var cardItems: [CardItem1] = []
    
    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjEsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxMDkzMDMyMCwiZXhwIjoxNzU0MTMwMzIwfQ.mVy33lNv-by6bWXshsT4xFOwZSWGkOW76GWimliqHP4"
    
    func fetchAllCardViews() {
        let urlString = "http://wasuphj.synology.me:8000/core/cardView/all/1"
        let headers: HTTPHeaders = [
            "Authorization": authToken,
            "Content-Type": "application/json"
        ]
        
        AF.request(urlString, headers: headers).responseJSON { response in
            switch response.result {
            case .success(let value):
                if let json = value as? [String: Any], let data = json["data"] as? [String: Any], let cardViews = data["cardViews"] as? [[String: Any]] {
                    self.parseCardViews(cardViews)
                }
            case .failure(let error):
                print("Error while fetching card views: \(error.localizedDescription)")
            }
        }
    }
    
    private func parseCardViews(_ cardViews: [[String: Any]]) {
        for cardView in cardViews {
            if let id = cardView["id"] as? Int,
               let tripFileId = cardView["tripFileId"] as? Int,
               let recordedAt = cardView["recordedAt"] as? String,
               let recordFileName = cardView["recordFileName"] as? String,
               let recordFileUrl = cardView["recordFileUrl"] as? String,
               let location = cardView["location"] as? String,
               let recordFileLength = cardView["recordFileLength"] as? Int,
               let weather = cardView["weather"] as? String,
               let temperature = cardView["temperature"] as? String,
               let stt = cardView["stt"] as? String,
               let happy = cardView["happy"] as? Double,
               let sad = cardView["sad"] as? Double,
               let angry = cardView["angry"] as? Double,
               let neutral = cardView["neutral"] as? Double,
               let question = cardView["question"] as? String,
               let loved = cardView["loved"] as? Bool,
               let recordFileStatus = cardView["recordFileStatus"] as? String {
                let newItem = CardItem1(id: id, tripFileId: tripFileId, recordedAt: recordedAt, recordFileName: recordFileName, recordFileUrl: recordFileUrl, location: location, recordFileLength: recordFileLength, weather: weather, temperature: temperature, stt: stt, happy: happy, sad: sad, angry: angry, neutral: neutral, question: question, loved: loved, recordFileStatus: recordFileStatus)
                DispatchQueue.main.async {
                    self.cardItems.append(newItem)
                }
            }
        }
    }
}
