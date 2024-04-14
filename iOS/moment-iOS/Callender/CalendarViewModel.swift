//
//  TodoViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation


import SwiftUI
import Alamofire
import Foundation



struct TripRegistration: Codable {
    var tripName: String
    var startDate: String
    var endDate: String
}


class CalendarViewModel : ObservableObject {
    @Published var tripName : String
    @Published var time : Date
    @Published var isDisplayCalendar : Bool
    @Published var day : Date
    @Published var startTime: Date?
    @Published var endTime: Date?
    @Published var selectedDays: [Date] = []
    // ui의 상태를 최신화해주는 published를 사용해준다.
    var authToken: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjEsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxMzAxMDQ0NywiZXhwIjoxNzU2MjEwNDQ3fQ.psufhNoH1wBQmSpw2TyLKfazOU8U96bJnTE1dh3bASs"//s
    
    init(
        tripName: String = "",
        time: Date = Date(),
        isDisplayCalendar: Bool = false,
        day : Date = Date(),
        startTime: Date? = nil,
        endTime: Date? = nil
    )
    {
        self.tripName = tripName
        self.time = time
        self.isDisplayCalendar = isDisplayCalendar
        self.day = day
        self.startTime = startTime
        self.endTime = endTime
        
    }
    func addSelectedDay(_ day: Date) {
        // 이미 두 날짜가 선택되었다면 초기화
        if selectedDays.count >= 2 {
            selectedDays.removeAll()
        }
        // 새 날짜 추가
        selectedDays.append(day)
        
        // 두 날짜가 모두 선택되었다면 시작과 종료 날짜 설정
        if selectedDays.count == 2 {
            selectedDays.sort() // 시작 날짜가 종료 날짜보다 먼저 오도록 정렬
            startTime = selectedDays.first
            endTime = selectedDays.last
        }
    }
    
    
    func registerTrip(completion: @escaping (Bool, String?, Int?) -> Void) {
        guard let start = startTime, let end = endTime, !tripName.isEmpty else {
            print(tripName,startTime,endTime)
            completion(false, "Invalid input: Ensure all fields are filled correctly.", nil)
            return
        }
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let tripData = TripRegistration(
            tripName: tripName,
            startDate: dateFormatter.string(from: start),
            endDate: dateFormatter.string(from: end)
        )
        
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(authToken)",
            "Accept": "application/json"
        ]
        
        AF.request("http://wasuphj.synology.me:8000/core/trip/register", method: .post, parameters: tripData, encoder: JSONParameterEncoder.default, headers: headers)
            .responseJSON { response in
                let statusCode = response.response?.statusCode
                switch response.result {
                case .success:
                    DispatchQueue.main.async {
                        completion(true, nil, statusCode)
                    }
                case .failure:
                    let errorMessage = response.error?.localizedDescription ?? "Unknown error"
                    DispatchQueue.main.async {
                        completion(false, errorMessage, statusCode)
                    }
                }
            }
    }
}

struct SimpleResponse: Codable {
    let status: Int
    let code: String
    let msg: String
    let detailMsg: String
}




extension CalendarViewModel {
    func setIsDisplayCalendar(
        _ isDispaly : Bool
    ){
        isDisplayCalendar = isDispaly // isDisplay의 값을 받아서 isDisplayCalendar 속성에 할당할거임 이제 이런게 동작을 캡슐화했다고 봐도 맞는거지
    }
}
