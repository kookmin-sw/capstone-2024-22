//
//  SharedViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 5/7/24.
//

import Foundation
import Alamofire

class SharedViewModel: ObservableObject {
    @Published var isSaved: Bool = false
    
    
    @Published var text : String =  ""
    @Published var inputText : String = ""
    @Published var StartLocatio : String = ""
    @Published var EndLocationend : String = ""
    @Published var EndLocation : String = ""
    
    
    @Published var tripRecord : String = ""
    @Published var tripExplaneStart : String = ""
    @Published var tripnameStart : String = ""
    @Published var tripExplaneEnd : String = ""
    @Published var tripnameEnd: String = ""
    
    @Published var items: [Item] = []
    @Published var totalReceiptCount = 0
    
    @Published var receipts = [Receipt]()
    //@Published var pagination : Pagination?
    @Published var receiptsBillData : [TripData] = []
    @Published var tripData : TripData?
    
    
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
    
    func createReceipt(for tripId: Int, themeType: String) {
        print(tripId)
        let url = "http://211.205.171.117:8000/core/receipt"
        let headers: HTTPHeaders = [
            "Authorization": authToken,
            "Content-Type": "application/json"
        ]
        
        var parameters: [String: Any] = [
            "tripId": tripId,
            "receiptThemeType": themeType
        ]

        
        switch themeType {
           case "A":
               parameters["mainDeparture"] = text
               parameters["subDeparture"] = inputText
               parameters["mainDestination"] = StartLocatio
               parameters["subDestination"] = EndLocationend
               parameters["oneLineMemo"] = EndLocation
               
           case "B":
              
            parameters["mainDeparture"] = tripRecord
            parameters["subDeparture"] = tripExplaneStart
            parameters["mainDestination"] = tripnameStart
            parameters["subDestination"] = tripExplaneEnd
            parameters["oneLineMemo"] = tripnameEnd
               
           default:
               break
           }

        AF.request(url, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: headers).responseJSON { response in
            if let statusCode = response.response?.statusCode {
                       print("HTTP Status Code: \(statusCode)")
                   }
            switch response.result {
            case .success(let data):
                print("Receipt creation successful: \(data)")
                // 필요한 경우 UI 업데이트
            case .failure(let error):
                print("Error occurred during receipt creation: \(error.localizedDescription)")
                // 에러 처리
            }
        }
    }

        func fetchReceipts() {
            let url = "http://211.205.171.117:8000/core/receipt/all?page=0&size=100"
            
            let headers: HTTPHeaders = [
                "Authorization": authToken,
                "Content-Type": "application/json"
            ]

            AF.request(url, headers: headers).responseDecodable(of: ReceiptsResponse.self) { response in
                switch response.result {
                case .success(let receiptsResponse):
                    DispatchQueue.main.async {
                        self.receipts = receiptsResponse.data.receiptList
                    }
                  //  print("Received data: \(receiptsResponse)")  // 전체 응답 데이터 출력
                case .failure(let error):
                    print("Error fetching receipts: \(error.localizedDescription)")
                }
            }
        }
    
    func deleteReceipts(with ids: [Int]) {
           let url = "http://211.205.171.117:8000/core/receipt/delete"
           let headers: HTTPHeaders = [
               "Authorization": authToken,
               "Content-Type": "application/json"
           ]
           let parameters: [String: Any] = [
               "receiptIds": ids.map { ["receiptId": $0] }
           ]

           AF.request(url, method: .delete, parameters: parameters, encoding: JSONEncoding.default, headers: headers).responseJSON { response in
               switch response.result {
               case .success(let data):
                   print("Deletion successful: \(data)")
                   // 성공 시, 로컬 데이터 업데이트
                   DispatchQueue.main.async {
                       self.receipts.removeAll { receipt in
                           ids.contains(receipt.id)
                       }
                   }
               case .failure(let error):
                   print("Deletion failed: \(error)")
               }
           }
       }
    
    
    func fetchTripDetails(tripId: Int) {
        let url = "http://211.205.171.117:8000/core/trip/\(tripId)"
        let headers: HTTPHeaders = [
            "Content-Type": "application/json;charset=UTF-8",
            "Authorization": authToken
           
        ]
        
        AF.request(url, method: .get, headers: headers).responseDecodable(of: TripDetail.self) { response in
            switch response.result {
            case .success(let tripDetail):
                print("Trip Details: \(tripDetail)")
            case .failure(let error):
                print("Error fetching trip details: \(error)")
            }
        }
    }
    

}

struct TripDetail: Decodable {// 영수증 만드는 페이지에 대한 데이터모델
    let status: Int
    let code: String
    let msg: String
    let detailMsg: String
    let data: TripData
}

struct TripData: Decodable,Identifiable { // 영수증 만드는 페이지에 대한 데이터모델
    let id: Int
    let email: String
    let startDate: String
    let endDate: String
    let analyzingCount: Int
    let tripName: String
    let numOfCard: Int
    let happy: Double
    let sad: Double
    let angry: Double
    let neutral: Double
    let disgust: Double
}

struct Receipt: Identifiable, Decodable {
    let id: Int
    let tripId: Int?
    let mainDeparture: String
    let subDeparture: String
    let mainDestination: String
    let subDestination: String
    let oneLineMemo: String
    let receiptThemeType: String
    let createdAt: String
    let happy: Double
    let sad: Double
    let angry: Double
    let neutral: Double
    let disgust: Double
    let numOfCard: Int
    let stDate : String
    let edDate : String
    let tripName : String
  

}

// API 응답을 위한 모델
struct ReceiptsResponse: Decodable {
    let status: Int
    let code: String
    let msg: String
    let detailMsg: String
    let data: ReceiptData
}

struct ReceiptData: Decodable {
    let receiptList: [Receipt]
    let pagination: Pagination
}

struct Pagination: Decodable {
    let totalPages: Int
    let totalElements: Int
    let currentPage: Int
    let currentElements: Int
}
