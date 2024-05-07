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
    

}





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
