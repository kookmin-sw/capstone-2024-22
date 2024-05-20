//
//  DailyViewmodel.swift
//  moment-iOS
//
//  Created by 양시관 on 4/8/24.
//

import Foundation
import Foundation
import SwiftUI
import Alamofire

struct TripFilesResponse: Codable {
    let status: Int
    let code: String
    let msg: String
    let detailMsg: String
    let data: TripFilesData
}

struct TripFilesData: Codable {
    let tripFiles: [TripFileDaily]
}

struct TripFileDaily: Codable, Identifiable {
    let id: Int
    let tripId: Int
    let email: String
    let yearDate: String
    let analyzingCount: Int
    let totalCount: Int
}


class DailyItemViewModel: ObservableObject {
    @Published var tripDailyFiles: [TripFileDaily] = []
//        let authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjIsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNTQyNDgzMiwiZXhwIjoxNzU4NjI0ODMyfQ.iHg2ACmOB_hzoSlwsTfzGc_1gn6OHYmAxD0b2wgqNJg"
    
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

        func fetchTripFiles() {
            let urlString = "http://211.205.171.117:8000/core/tripfile/untitled"
            let headers: HTTPHeaders = [
                "Authorization": authToken,
                "Content-Type": "application/json"
            ]

            AF.request(urlString, headers: headers).responseDecodable(of: TripFilesResponse.self) { response in
                switch response.result {
                case .success(let responseData):
                    DispatchQueue.main.async {
                        self.tripDailyFiles = responseData.data.tripFiles
                        print("Trip files fetched successfully. Total files: \(self.tripDailyFiles.count)")
                                      for file in self.tripDailyFiles {
                                          print("""
                                              ID: \(file.id), TripID: \(file.tripId), Email: \(file.email),
                                              Date: \(file.yearDate), Analyzing Count: \(file.analyzingCount),
                                              Total Count: \(file.totalCount)
                                              """)
                                      }
                    }
                case .failure(let error):
                    print("Failed to fetch trip files: \(error.localizedDescription)")
                }
            }
        }
}
