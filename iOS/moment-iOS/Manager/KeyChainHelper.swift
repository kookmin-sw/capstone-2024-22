//
//  KeyChainHelper.swift
//  moment-iOS
//
//  Created by 양시관 on 5/18/24.
//

import Foundation
import KeychainAccess

class KeychainHelper {
    static let shared = KeychainHelper()
    private let keychain = Keychain(service: "YangCustom.moment-iOS")
    
    private init() {}
    
    func saveAccessToken(token: String) {
        do {
            try keychain.set(token, key: "accessToken")
            print("Access token saved successfully")
        } catch let error {
            print("Failed to save access token: \(error)")
        }
    }
    
    func getAccessToken() -> String? {
        do {
            let token = try keychain.get("accessToken")
            return token
        } catch let error {
            print("Failed to retrieve access token: \(error)")
            return nil
        }
    }
    
    func deleteAccessToken() {
        do {
            try keychain.remove("accessToken")
            print("Access token deleted successfully")
        } catch let error {
            print("Failed to delete access token: \(error)")
        }
    }
}
