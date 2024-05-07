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
    @Published var pathword: String = ""
    
    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjQsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNTA5MTA4MywiZXhwIjoxNzU4MjkxMDgzfQ.XxixgGTkMGfNQPhQXm4Bt8Zz9rfRlq9UsY7wV0gxQUE"
    
    func requestAuthCode() {
        let parameters: [String: Any] = [
            "email": email,
            "isSignUp": "true"
        ]
        
        AF.request("http://211.205.171.117:8000/auth/code",
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
        // 인증 토큰을 포함하는 헤더
        let headers: HTTPHeaders = [
            "Authorization": authToken,  // 토큰을 사용할 때는 'Bearer' 접두어를 붙여야 합니다.
            "Accept": "application/json"
        ]

        // 인증 코드를 파라미터로 사용
        let parameters: [String: String] = [
            "code": verificationCode
        ]
        
        // Alamofire를 사용한 네트워크 요청
        AF.request("http://211.205.171.117:8000/auth/verify", method: .patch, parameters: parameters, encoding: JSONEncoding.default, headers: headers)
            .validate()  // 상태 코드가 200-299인지 자동으로 검증
            .responseJSON { response in
                print("Response: \(response)")
                switch response.result {
                case .success:
                    DispatchQueue.main.async {
                        self.isCodeValid = true
                        completion(true)
                        print("인증 성공")
                    }
                case .failure(let error):
                    print("실패 응답: \(error)")
                    DispatchQueue.main.async {
                        self.isCodeValid = false
                        completion(false)
                        print("인증 실패")
                    }
                }
            }
    }
    
    func changePassword() {
            let url = "http://211.205.171.117:8000/auth/password"
            let parameters: [String: String] = [
                "code": verificationCode,
                "newPassword": pathword
            ]
            let headers: HTTPHeaders = [
                "Authorization": authToken,
                "Content-Type": "application/json;charset=UTF-8"
            ]

            AF.request(url, method: .patch, parameters: parameters, encoding: JSONEncoding.default, headers: headers)
                .validate()
                .responseJSON { response in
                    switch response.result {
                    case .success:
                        print("비밀번호 변경 성공: \(response)")
                       // completion(true)
                    case .failure(let error):
                        print("비밀번호 변경 실패: \(error)")
                        //completion(false)
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
        
        AF.request("http://211.205.171.117:8000/auth/login", method: .post, parameters: parameters, encoder: JSONParameterEncoder.default)
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
