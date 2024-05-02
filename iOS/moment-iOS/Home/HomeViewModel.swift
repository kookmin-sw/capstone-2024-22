//
//  TodoListViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import Alamofire
import SwiftUI


class HomeViewModel : ObservableObject {//뷰모델을 만들어서 Todo 에 있는녀석 가져와서 뷰모델에서 뷰에 넣기 전에 데이터들을 잘 처리해준다 이렇게 해주는게 MVVM 패턴인거거덩
    
    @Published var tripFiles: [TripFile] = []
    @Published var todos : [Todo]
    @Published var isEditTodoMode : Bool
    @Published var removeTodos : [Todo]
    @Published var isDisplayRemoveTodoAlert : Bool
    @Published var tripName: String = ""
    @Published var tripStartDate: Date?
    @Published var tripEndDate: Date?
    @Published var showingDeleteAlert: Bool = false
    @Published var indexToDelete: Int? = nil
    
    @Published var items: [Item] = []
    var authToken: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjMsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNDQ3MDczNCwiZXhwIjoxNzU3NjcwNzM0fQ.pddeumunqT4tiE2yGI9aWXkn0Kxo7XeB9kFfpwQftbM"
        
        init() {
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy. MM. dd"
//            
//            items = [
//                       Item(tripName: "선유도여행", startdate: "2024. 04. 11", enddate: "2024. 04. 13"),
//                       Item(tripName: "일본여행", startdate: "2024. 05. 15", enddate: "2024. 05. 19"),
//                       Item(tripName: "뭐하기 여행", startdate: "2024. 06. 05", enddate: "2024. 06. 13"),
//                       Item(tripName: "좋은여행", startdate: "2024. 07. 05", enddate: "2024. 10. 13")
//                   ]
            self.todos = []
            self.isEditTodoMode = false
            self.removeTodos = []
            self.isDisplayRemoveTodoAlert = false
            self.tripName = ""
            
        }
    
    
    func getIndex(item: Item) -> Int {
        return items.firstIndex { item1 -> Bool in
            return item.id == item1.id
        } ?? 0
    }
    
    var removeTodosCount : Int {
        return removeTodos.count // 여기 카운트는 그냥 제공하는녀석이네 이걸 왜의심했냐면 removeTodos 에 갔는데 데이터셋 Todo 를 가지고있어서 가봤더니 count 가 없네 그래서 count 를 의심했음
        // 여튼 그렇게 해서 INt 형식으로 이번엔 값을 저장을 해주네 ㅇㅇ 아까 전에는 저장안하고 변하면 바로계산해서 쏴주기만했자나
    }
    var navigationBarRightBtnMode : NavigationBtnType { // 네비게이션 버튼타입에 가서 내가쓰고싶은 열거형 ㅌ친구들을 잘써준 모습
        isEditTodoMode ? .complete: .edit // 기본문법이니까 패스 ? 후 참이면 1 flase 면 두번째거
    }
    init(
        todos: [Todo] = [], // 이소스코드는 Todo 클래스를 타입으로 가지는 녀석을 빈배열로 초기화하는 소스코드임
        isEditModeTodoMode: Bool = false,
        removeTodos: [Todo] = [],// 이소스코드는 Todo 클래스를 타입으로 가지는 녀석을 빈배열로 초기화하는 소스코드임
        isDisplayRemoveTodoAlert: Bool = false
        
    )
    {
        self.todos = todos
        self.isEditTodoMode = isEditModeTodoMode
        self.removeTodos = removeTodos
        self.isDisplayRemoveTodoAlert = isDisplayRemoveTodoAlert
        
    }
    
    func updateTripInfo(name: String, startDate: Date?, endDate: Date?) {
        self.tripName = name
        self.tripStartDate = startDate
        self.tripEndDate = endDate
    }
    
    func prepareForDeletion(index: Int) {
        indexToDelete = index
        showingDeleteAlert = true
    }
 
    func deleteItem(itemToDelete: Item, completion: @escaping (Bool, String) -> Void) {
        let itemId = itemToDelete.id
        let url = "http://wasuphj.synology.me:8000/core/trip/\(itemId)"
        let headers: HTTPHeaders = ["Authorization": authToken, "Accept": "application/json"]

        AF.request(url, method: .delete, headers: headers)
          .responseDecodable(of: DeleteResponse.self) { response in
            switch response.result {
            case .success(let deleteResponse):
                if deleteResponse.status == 200 {
                    DispatchQueue.main.async {
                        self.items.removeAll { $0.id == itemToDelete.id }
                        completion(true, deleteResponse.msg)
                    }
                } else {
                    DispatchQueue.main.async {
                        completion(false, deleteResponse.msg)
                    }
                }
            case .failure(let error):
                print("Response: \(String(describing: response.data))") // 로그로 실제 응답 데이터 출력
                DispatchQueue.main.async {
                    completion(false, error.localizedDescription)
                }
            }
        }
    }
    
    
   
    func fetchTripFiles(for tripId: Int) {
        let urlString = "http://wasuphj.synology.me:8000/core/tripfile/\(tripId)"
        let headers: HTTPHeaders = [
            "Content-Type": "application/json;charset=UTF-8",
            "Authorization": authToken  // 여기서 실제 유저 ID 또는 인증 토큰으로 변경해야 할 수 있습니다.
        ]
        print("Fetching trip files for Trip ID: \(tripId)")

        AF.request(urlString, headers: headers).responseJSON { response in
            // HTTP 상태 코드 출력
            if let statusCode = response.response?.statusCode {
                print("HTTP Status Code: \(statusCode)")
            }

            switch response.result {
            case .success(let value):
                do {
                    print("여기오니?")
                    let data = try JSONSerialization.data(withJSONObject: value, options: [])
                    print(data)
                    let decoder = JSONDecoder()
                    let tripFiles = try decoder.decode(TripFileResponse.self, from: data)
                    print("Decoded Trip Files: \(tripFiles)")
                } catch {
                    print("Decoding error: \(error)")
                }
            case .failure(let error):
                print("Failed to fetch with error: \(error)")
            }
        }
    }


}

struct TripFileResponse: Codable {
    var status: Int
    var code: String
    var msg: String
    var detailMsg: String
    var data: TripFileData
}

struct TripFileData: Codable {
    var tripFiles: [TripFile]  // 'tripFiles' 대신 'trips'로 변경
}

struct TripFile: Codable {
    var id: Int
    var tripId: Int
    var email: String
    var yearDate : String
    var analyzingCount: Int

}




extension HomeViewModel {
    // 가장 가까운 여행의 상태와 이름을 반환하는 함수
    func tripStatusAndNameForToday() -> (status: String, name: String?) {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy. MM. dd"
        
        let today = Date()
        if let closestTrip = items.min(by: { abs(dateFormatter.date(from: $0.startdate)!.distance(to: today)) < abs(dateFormatter.date(from: $1.startdate)!.distance(to: today)) }) {
            if let startdate = dateFormatter.date(from: closestTrip.startdate), let enddate = dateFormatter.date(from: closestTrip.enddate) {
                if startdate > today {
                    let daysRemaining = Calendar.current.dateComponents([.day], from: today, to: startdate).day ?? 0
                    return ("\(daysRemaining)일 남음", closestTrip.tripName)
                } else if enddate > today {
                    let dayNumber = Calendar.current.dateComponents([.day], from: startdate, to: today).day ?? 0
                    return ("여행 \(dayNumber)일차", closestTrip.tripName)
                } else {
                    return ("여행 종료", closestTrip.tripName)
                }
            }
        }
        return ("어디로 떠나면 좋을까요?", nil)
    }
    
    func fetchTrips() {
           let headers: HTTPHeaders = ["Authorization": authToken, "Accept": "application/json"]
           AF.request("http://wasuphj.synology.me:8000/core/trip/all", method: .get, headers: headers)
               .responseDecodable(of: TripsResponse.self) { response in
                   switch response.result {
                   case .success(let data):
                       self.items = data.data.trips.map { trip in
                           Item(id: trip.id, tripName: trip.tripName, startdate: trip.startDate, enddate: trip.endDate)
                       }
                   case .failure(let error):
                       print("Failed to fetch trips: \(error.localizedDescription)")
                   }
               }
       }
}

