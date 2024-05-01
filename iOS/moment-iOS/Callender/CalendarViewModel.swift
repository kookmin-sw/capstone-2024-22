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



class CalendarViewModel : ObservableObject {
    @Published var tripName : String
    @Published var time : Date
    @Published var isDisplayCalendar : Bool
    @Published var day : Date
    @Published var startTime: Date?
    @Published var endTime: Date?
    @Published var selectedDays: [Date] = []
    // ui의 상태를 최신화해주는 published를 사용해준다.
    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjMsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNDQ3MDczNCwiZXhwIjoxNzU3NjcwNzM0fQ.pddeumunqT4tiE2yGI9aWXkn0Kxo7XeB9kFfpwQftbM"
    
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
    
    private let dateFormatter: DateFormatter = {
           let formatter = DateFormatter()
           formatter.dateFormat = "yyyy-MM-dd"
           return formatter
       }()
    
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
    

    func registerTrip(tripName: String, startDate: Date, endDate: Date, completion: @escaping (Bool, String?) -> Void) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let formattedStartDate = dateFormatter.string(from: startDate)
        let formattedEndDate = dateFormatter.string(from: endDate)

        let tripData = [
            "startDate": formattedStartDate,
            "endDate": formattedEndDate,
            "tripName": tripName
        ]

        let headers: HTTPHeaders = ["Authorization": authToken, "Accept": "application/json"]

        AF.request("http://wasuphj.synology.me:8000/core/trip/register", method: .post, parameters: tripData, encoder: JSONParameterEncoder.default, headers: headers)
            .responseDecodable(of: TripRegistrationResponse.self) { response in
                switch response.result {
                case .success(let responseData):
                    print("Response: \(responseData)")
                    if responseData.status == 201 {
                        completion(true, nil) // 성공 시
                    } else {
                        completion(false, responseData.msg) // 실패 시 메시지 반환
                    }
                case .failure(let error):
                    print("Error: \(error)")
                    completion(false, error.localizedDescription)
                }
            }
    }

  
}




extension CalendarViewModel {
    func setIsDisplayCalendar(
        _ isDispaly : Bool
    ){
        isDisplayCalendar = isDispaly // isDisplay의 값을 받아서 isDisplayCalendar 속성에 할당할거임 이제 이런게 동작을 캡슐화했다고 봐도 맞는거지
    }
}
