//
//  MemoListViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import SwiftUI
import UIKit
import Alamofire

class BillListViewModel: ObservableObject {
    @Published var items: [Item] = []
   
    
    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjMsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNDQ3MDczNCwiZXhwIjoxNzU3NjcwNzM0fQ.pddeumunqT4tiE2yGI9aWXkn0Kxo7XeB9kFfpwQftbM"
    
    func createReceipt(for tripId: Int) {
        let url = "http://wasuphj.synology.me:8000/core/receipt"
        let headers: HTTPHeaders = [
            .contentType("application/json"),
            .accept("application/json"),
            .authorization(bearerToken: authToken)  // 적절한 토큰으로 교체하세요.
        ]
        let parameters: [String: Any] = [
            "tripId": tripId,
            "mainDeparture": "mainDepartureValue",  // 이 값을 적절하게 설정하거나 외부에서 받아와야 할 수도 있습니다.
            "subDeparture": "subDepartureValue",
            "mainDestination": "mainDestinationValue",
            "subDestination": "subDestinationValue",
            "oneLineMemo": "oneLineMemoValue",
            "receiptThemeType": "A"  // A or B
        ]

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

    // 기존에 항목을 로드하는 등의 다른 함수들...
}
