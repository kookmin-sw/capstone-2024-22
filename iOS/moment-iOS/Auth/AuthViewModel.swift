//
//  AuthViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 4/12/24.
//

import Foundation
import Foundation
import SwiftUI
import Alamofire

class AuthViewModel: ObservableObject {
    @Published var email: String = ""
    // 필요한 경우 추가적인 상태 변수를 선언

    func requestAuthCode() {
        let parameters: [String: Any] = [
            "email": email,
            "isSignUp": "true"
        ]

        AF.request("http://wasuphj.synology.me:8000/auth/code",
                   method: .post,
                   parameters: parameters,
                   encoding: JSONEncoding.default)
            .validate() // 상태 코드 200..<300 범위를 검증
            .responseDecodable(of: AuthResponse.self) { response in
                switch response.result {
                case .success(let authResponse):
                    DispatchQueue.main.async {
                        // 성공 시 처리
                        print("AccessToken: \(authResponse.data.accessToken)")
                        // 상태 업데이트 로직
                    }
                case .failure(let error):
                    // 실패 시 처리
                    print("Error: \(error)")
                }
            }
    }
}

// 서버 응답을 위한 구조체
struct AuthResponse: Codable {
    var status: Int
    var code: String
    var msg: String
    var detailMsg: String
    var data: AuthData
}

struct AuthData: Codable {
    var grantType: String
    var accessToken: String
    var role: String
    var userId: Int
}
