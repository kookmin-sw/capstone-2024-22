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
    @Published var isCodeValid: Bool = false
    @Published var isLoggedIn: Bool = false
    @Published var loginError: Bool = false
    @Published var pathword: String = ""
    
    

    @Published var refreshToken: String = ""
    @Published var userRole: String = ""
    @Published var errorMessage: String = ""
    @Published var isCodeSent: Bool = false
    
   @Published var accessToken: String = ""
    
  var authToken: String = ""
    
  
    
    func requestAuthCode(){
        let parameters: [String: Any] = [
            "email": email,
            "isSignUp": "false"
        ]
        
        
        AF.request("http://211.205.171.117:8000/auth/code",
                   method: .post,
                   parameters: parameters,
                   encoding: JSONEncoding.default)
        .validate() // 상태 코드 200..<300 범위를 검증
        .responseDecodable(of: AuthResponse.self) { response in
           // DispatchQueue.global().sync {
                switch response.result {
                case .success(let authResponse):
                    // 성공 시 처리, 액세스 토큰 저장
                    
                    self.accessToken = authResponse.data.accessToken
                    
                    
                    print("AccessToken: \(authResponse.data.accessToken)")
                 
//                    self.verifyCode { success in
//                                       if success {
//                                           print("Verification successful.")
//                                       } else {
//                                           print("Verification failed.")
//                                       }
//                                   }
                    
                case .failure(let error):
                    // 실패 시 처리
                    print("Error: \(error)")
                   
                }
            //}
        }
    }

   
 
    func verifyCode(completion: @escaping (Bool) -> Void) {
        // URL 설정
        let url = "http://211.205.171.117:8000/auth/verify"
     
        // 헤더 설정: 인증 토큰과 콘텐트 타입 포함
        print("토큰 : \(self.accessToken)")
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(accessToken)",
            "Content-Type": "application/json;charset=UTF-8"
        ]
        
        // 파라미터 설정: 인증 코드 포함
        let parameters: [String: String] = [
            "code": verificationCode
        ]
        
        // Alamofire를 사용한 HTTP PATCH 요청
        AF.request(url, method: .patch, parameters: parameters, encoding: JSONEncoding.default, headers: headers)
            .validate()  // 상태 코드 200-299 사이인지 검증
            .responseDecodable(of : AuthResponseverify.self) { response in
                //DispatchQueue.main.async {
                    switch response.result {
                    case .success(let authResponse):
                        // 응답 성공, 인증 코드 유효
                        self.isCodeValid = true
                        print(authResponse.data.accessToken)
                        completion(true)
                        print("인증 성공")
                    case .failure(let error):
                        // 응답 실패, 인증 코드 무효
                        self.isCodeValid = false
                        completion(false)
                        print("인증 실패: \(error)")
                    }
              //  }
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

//
//{
//  "status" : 200,
//  "code" : "200",
//  "msg" : "UPDATE SUCCESS",
//  "detailMsg" : "",
//  "data" : {
//    "grantType" : "Bearer",
//    "accessToken" : "accessToken",
//    "refreshToken" : "refresh",
//    "role" : "ROLE_AUTH_USER"
//  }
//}
