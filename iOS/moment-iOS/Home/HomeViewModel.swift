//
//  TodoListViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import Alamofire
import SwiftUI


class HomeViewModel: ObservableObject {
    @Published var tripFiles: [TripFile] = []
    @Published var todos: [Todo] = []
    @Published var isEditTodoMode: Bool = false
    @Published var removeTodos: [Todo] = []
    @Published var isDisplayRemoveTodoAlert: Bool = false
    @Published var tripName: String = ""
    @Published var tripStartDate: Date?
    @Published var tripEndDate: Date?
    @Published var showingDeleteAlert: Bool = false
    @Published var indexToDelete: Int? = nil
    private var dayIndexMapping: [String: Int] = [:]
    @Published var items: [Item] = []
    
    var authToken: String {
        get {
            if let token = KeychainHelper.shared.getAccessToken() {
                return "Bearer \(token)"
            } else {
                return ""
            }
        }
    }
        
    init() {
        self.todos = []
        self.isEditTodoMode = false
        self.removeTodos = []
        self.isDisplayRemoveTodoAlert = false
        self.tripName = ""
    }

    func calculateDayIndexes() {
        var uniqueDates = Set<String>()
        for file in tripFiles {
            uniqueDates.insert(file.yearDate)
        }
        let sortedDates = uniqueDates.sorted()
        dayIndexMapping = Dictionary(uniqueKeysWithValues: sortedDates.enumerated().map { ($1, $0) })
    }
    
    func dayIndex(for date: String) -> Int {
        return dayIndexMapping[date] ?? 0
    }
    
    func getIndex(item: Item) -> Int {
        return items.firstIndex { $0.id == item.id } ?? 0
    }
    
    var removeTodosCount: Int {
        return removeTodos.count
    }
    
    var navigationBarRightBtnMode: NavigationBtnType {
        isEditTodoMode ? .complete : .edit
    }
    
    init(
        todos: [Todo] = [],
        isEditModeTodoMode: Bool = false,
        removeTodos: [Todo] = [],
        isDisplayRemoveTodoAlert: Bool = false
    ) {
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
        let url = "http://211.205.171.117:8000/core/trip/\(itemId)"
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
                    print("Response: \(String(describing: response.data))")
                    DispatchQueue.main.async {
                        completion(false, error.localizedDescription)
                    }
                }
            }
    }
   
    func fetchTripFiles(for tripId: Int) {
        let urlString = "http://211.205.171.117:8000/core/tripfile/\(tripId)"
        let headers: HTTPHeaders = [
            "Content-Type": "application/json;charset=UTF-8",
            "Authorization": authToken
        ]
        print("Fetching trip files for Trip ID: \(tripId)")

        AF.request(urlString, method: .get, headers: headers).responseJSON { response in
            if let statusCode = response.response?.statusCode {
                print("HTTP Status Code: \(statusCode)")
            }
            
            switch response.result {
            case .success(let value):
                do {
                    let data = try JSONSerialization.data(withJSONObject: value, options: [])
                    let decoder = JSONDecoder()
                    let tripFileResponse = try decoder.decode(TripFileResponse.self, from: data)
                    DispatchQueue.main.async {
                        self.tripFiles = tripFileResponse.data.tripFiles
                    }
                } catch {
                    print("Decoding error: \(error)")
                }
            case .failure(let error):
                print("Failed to fetch with error: \(error)")
            }
        }
    }

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
        AF.request("http://211.205.171.117:8000/core/trip/all", method: .get, headers: headers)
            .responseDecodable(of: TripsResponse.self) { response in
                switch response.result {
                case .success(let data):
                    self.items = data.data.trips.map { trip in
                        Item(id: trip.id, tripName: trip.tripName, startdate: trip.startDate, enddate: trip.endDate)
                    }
                    self.sortTrips()
                case .failure(let error):
                    print("Failed to fetch trips: \(error.localizedDescription)")
                }
            }
    }

    private func sortTrips() {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy. MM. dd"
        
        let currentDate = Date()
        items.sort { (item1, item2) -> Bool in
            guard let date1 = dateFormatter.date(from: item1.startdate),
                  let date2 = dateFormatter.date(from: item2.startdate) else { return false }
            
            if date1 < currentDate && date2 < currentDate {
                return date1 > date2
            } else if date1 >= currentDate && date2 >= currentDate {
                return date1 < date2
            } else {
                return date1 >= currentDate
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
    var tripFiles: [TripFile]
}

struct TripFile: Codable {
    var id: Int//얘로 카드뷰를 조회한다
    var tripId: Int
    var email: String
    var yearDate : String
    var totalCount: Int
    
}
//

