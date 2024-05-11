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
    @Published var totalReceiptCount = 0
   
    
    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjIsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNTQyNDgzMiwiZXhwIjoxNzU4NjI0ODMyfQ.iHg2ACmOB_hzoSlwsTfzGc_1gn6OHYmAxD0b2wgqNJg"
 
    
    func fetchReceiptCount() {
            let url = "http://211.205.171.117:8000/core/receipt/count"
            let headers: HTTPHeaders = [
                .authorization(bearerToken: authToken),// 적절한 토큰으로 교체하세요.
                .accept("application/json")
            ]

            AF.request(url, method: .get, headers: headers).responseDecodable(of: ReceiptCountResponse.self) { response in
                switch response.result {
                case .success(let countResponse):
                    DispatchQueue.main.async {
                        self.totalReceiptCount = countResponse.data.count
                    }
                case .failure(let error):
                    print("Error fetching receipt count: \(error)")
                }
            }
        }

    // 기존에 항목을 로드하는 등의 다른 함수들...
}


struct ReceiptCountResponse: Codable {
    struct Data: Codable {
        let count: Int
    }
    let status: Int
    let code: String
    let msg: String
    let detailMsg: String
    let data: Data
}
