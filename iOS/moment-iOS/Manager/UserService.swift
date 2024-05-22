//
//  UserService.swift
//  moment-iOS
//
//  Created by 양시관 on 5/23/24.
//

import Foundation
import Alamofire

struct UserSettings: Codable {
    var notification: Bool
    var dataUsage: Bool
    var firebaseToken: String
}

class UserService {
    static let shared = UserService()
    private init() {}

    let baseURL = "http://211.205.171.117:8000"
    
    
    var authToken: String {
        get {
            if let token = KeychainHelper.shared.getAccessToken() {
                return "Bearer \(token)"
            } else {
                return ""
            }
        }
    }

    func updateUserSettings(userId: Int, settings: UserSettings, completion: @escaping (Result<Bool, Error>) -> Void) {
        let url = "\(baseURL)/core/user/setting"
        let headers: HTTPHeaders = [
            "Content-Type": "application/json;charset=UTF-8",
            "Authorization": authToken
        ]

        let parameters: [String: Any] = [
            "notification": true,//settings.notification
            "dataUsage": true,//settings.dataUsage
        "firebaseToken":"c9cnFm9_nECdu25ruUK0Cp:APA91bHD_BbbL7FAlu91S1u4BZRK0JXcueZIH8343CXCmuAX6mjkYShohze7GuuwyLlwqdjuIHh_0e-MFSGnqd1Mv83dWMDySamauZ0uPoHb3bVz1FAdmPherPcVDqND8AKUDOCHxMY-"//settings.firebaseToken
        ]

        AF.request(url, method: .patch, parameters: parameters, encoding: JSONEncoding.default, headers: headers)
            .validate()
            .responseDecodable(of: Fcm.self) { response in
                switch response.result {
                case .success(let value):
                    if value.status == 200 {
                        completion(.success(true))
                    } else {
                        let error = NSError(domain: "", code: value.status, userInfo: [NSLocalizedDescriptionKey: value.msg])
                        completion(.failure(error))
                    }
                case .failure(let error):
                    completion(.failure(error))
                }
            }
    }
}

// MARK: - Fcm
struct Fcm: Codable {
    let status: Int
    let code, msg, detailMsg: String
    let data: DataClassFCM
}

// MARK: - DataClass
struct DataClassFCM: Codable {
}
