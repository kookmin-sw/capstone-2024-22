//
//  Model.swift
//  moment-iOS
//
//  Created by 양시관 on 4/16/24.
//

import Foundation

struct SimpleResponse: Codable {
    let status: Int
    let code: String
    let msg: String
    let detailMsg: String
}

struct TripRegistrationResponse: Codable {
    let status: Int
    let code: String
    let msg: String
    let detailMsg: String
    let data: EmptyData // 사용하지 않는 data 필드를 위한 빈 구조체
    struct EmptyData: Codable {} // 빈 객체를 올바르게 디코드할 수 있음
    
}

struct TripsResponse: Codable {
    let status: Int
    let code: String
    let msg: String
    let detailMsg: String
    let data: TripsData
}

struct TripsData: Codable {
    let trips: [Trip]
}

struct Trip: Codable {
    let id: Int
    let email: String
    let startDate: String
    let endDate: String
    let analyzingCount: Int
    let tripName: String
}

struct TripRegistration: Codable {
   
    var startDate: String
    var endDate: String
    var tripName: String
}

struct DeleteResponse: Codable {
    let status: Int
    let code: String
    let msg: String
    let detailMsg: String
    let data: EmptyData?

    struct EmptyData: Codable {}
}
