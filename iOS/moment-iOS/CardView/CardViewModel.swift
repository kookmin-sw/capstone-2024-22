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
    let stt: String?
    let happy: Double?
    let sad: Double?
    let angry: Double?
    let neutral: Double?
    let disgust: Double?
    let question: String
    let recordFileStatus: String
    let imageUrls: [String]?
    let id: Int
    var loved: Bool
    
//    let tripFileID: Int
//      let recordedAt, recordFileName: String
//      let recordFileURL: String
//      let location: String
//      let recordFileLength: Int
//      let weather, temperature, stt: String
//      let happy, sad, angry, neutral: Double
//      let disgust: Double
//      let question, recordFileStatus: String
//      let imageUrls: [String]
//      let id: Int
//      let loved: Bool
}
//"{"status":200,"code":"200","msg":"SELECT SUCCESS","detailMsg":"","data":{"cardViews":[{"tripFileId":26,"recordedAt":"2024-05-05T07:22:25","recordFileName":"2024-05-05T07:22:26.082673290.m4a","recordFileUrl":"https://kmumoment.s3.ap-northeast-2.amazonaws.com/users/3/2024-05-05T07%3A22%3A26.082673290.m4a","location":"회천남로, 88, 양주시, 경기도, 대한민국","recordFileLength":28,"weather":"moderate rain","temperature":"19.9","stt":null,"happy":null,"sad":null,"angry":null,"neutral":null,"disgust":null,"question":"오늘 날씨는 어때요?","recordFileStatus":"WAIT","imageUrls":[],"id":54,"loved":false}]}}"




class CardViewModel: ObservableObject {
    @Published var cardItems: [cardViews] = []
   


    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjMsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNDQ3MDczNCwiZXhwIjoxNzU3NjcwNzM0fQ.pddeumunqT4tiE2yGI9aWXkn0Kxo7XeB9kFfpwQftbM"
    
    

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
//                DispatchQueue.main.async {
//                     self.cardViewsData = CardViewResponse.data.cardViews.first // 예제로 첫 번째 카드 뷰 데이터를 저장
//                 }
            case .failure(let error):
                print("Error while fetching card views: \(error.localizedDescription)")
                if let data = response.data, let errorString = String(data: data, encoding: .utf8) {
                              print("Error details: \(errorString)")
                          }
            }
        }
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
}
