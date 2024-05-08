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
   

    
    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjQsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNTA5MTA4MywiZXhwIjoxNzU4MjkxMDgzfQ.XxixgGTkMGfNQPhQXm4Bt8Zz9rfRlq9UsY7wV0gxQUE"
    
    func createReceipt(for tripId: Int, themeType: String) {
        print(tripId)
        let url = "http://211.205.171.117:8000/core/receipt"
        let headers: HTTPHeaders = [
            .contentType("application/json"),
            .accept("application/json"),
            .authorization(bearerToken: authToken)  // 적절한 토큰으로 교체하세요.
        ]
        var parameters: [String: Any] = [
            "tripId": tripId,
            "receiptThemeType": themeType
        ]
//        "mainDeparture": "mainDepartureValue",
//        "subDeparture": "subDepartureValue",
//        "mainDestination": "mainDestinationValue",
//        "subDestination": "subDestinationValue",
//        "oneLineMemo": "oneLineMemoValue",
        
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
}
