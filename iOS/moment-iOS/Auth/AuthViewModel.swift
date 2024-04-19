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
    @Published var password : String = ""
    @Published var verificationCode: String = ""
    @Published var isCodeValid: Bool? = nil
    @Published var isLoggedIn: Bool = false
    @Published var loginError: Bool = false
    
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
    
    func verifyCode(completion: @escaping (Bool) -> Void) {
        let parameters: [String: String] = [
            "code": verificationCode
        ]
        
        AF.request("http://wasuphj.synology.me:8000/auth/verify", method: .patch, parameters: parameters, encoding: JSONEncoding.default)
            .validate()
            .responseJSON { response in
                switch response.result {
                case .success:
                    DispatchQueue.main.async {
                        self.isCodeValid = true
                        completion(true)
                    }
                case .failure:
                    DispatchQueue.main.async {
                        self.isCodeValid = false
                        completion(false)
                    }
                }
            }
    }
    
    func loginUser() {
        let parameters = [
            "email": email,
            "password": password
        ]
        //alexj99@naver.com
        //1234
        
        AF.request("http://wasuphj.synology.me:8000/auth/login", method: .post, parameters: parameters, encoder: JSONParameterEncoder.default)
            .validate(statusCode: 200..<300)
            .responseJSON { response in
                switch response.result {
                case .success(let value):
                    DispatchQueue.main.async {
                        self.isLoggedIn = true
                        self.loginError = false
                        print("Response JSON: \(value)")
                    }
                case .failure(let error):
                    DispatchQueue.main.async {
                        self.isLoggedIn = false
                        self.loginError = true
                        if let data = response.data, let errorString = String(data: data, encoding: .utf8) {
                            print("Error: \(error) \nServer Response: \(errorString)")  // 에러와 서버 응답 함께 출력
                        }
                    }
                }
                print("Sending parameters: \(parameters)")

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
