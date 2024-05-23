//
//  ModifySelectDayView.swift
//  moment-iOS
//
//  Created by 양시관 on 5/23/24.
//

import Foundation
import Foundation
import UIKit
import SwiftUI

struct ModifySelectDayView: View {
    @ObservedObject var calendarViewModel: CalendarViewModel
    @State private var selectedTab: Int = 0
    @State private var tripModifyName: String // 여행 이름을 위한 상태
    @EnvironmentObject var homeViewModel: HomeViewModel
    @State private var isCalendarVisible: Bool = false
    @Environment(\.presentationMode) var presentationMode
    
    let tripId: Int
    let initialTripName: String
    let initialStartDate: String
    let initialEndDate: String
    
    init(calendarViewModel: CalendarViewModel, tripId: Int, tripName: String, startDate: String, endDate: String) {
    
        
        self.calendarViewModel = calendarViewModel
              self.tripId = tripId
              self._tripModifyName = State(initialValue: tripName)
              self.initialTripName = tripName
              self.initialStartDate = startDate
              self.initialEndDate = endDate
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        if let startDate = dateFormatter.date(from: startDate) {
            self.calendarViewModel.startTime = startDate
        }
        
        if let endDate = dateFormatter.date(from: endDate) {
            self.calendarViewModel.endTime = endDate
        }
    }
    

    private var dateFormatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy.MM.dd"
        return formatter
    }
    var body: some View {
        ZStack {
            Color(.homeBack).edgesIgnoringSafeArea(.all)
                .onTapGesture {
                    hideKeyboard()
                }
            VStack(alignment: .center) {
                Spacer()
                HStack {
                    Button(action: {
                        self.presentationMode.wrappedValue.dismiss()
                    }) {
                        Text("뒤로")
                            .font(.yjObangBold15)
                            .foregroundColor(.black)
                    }
                    Spacer()
                    Button("완료") {
                        if let startDate = calendarViewModel.startTime, let endDate = calendarViewModel.endTime {
                            calendarViewModel.updateTrip(tripId: tripId, tripName: tripModifyName, startDate: startDate, endDate: endDate) { success, errorMessage in
                                if success {
                                    print("Trip successfully updated!")
                                } else {
                                    print("Failed to update trip: \(errorMessage ?? "Unknown error")")
                                }
                                self.presentationMode.wrappedValue.dismiss()
                            }
                        }
                    }
                    .font(.yjObangBold15)
                    .foregroundColor(.black)
                }
                .padding(.horizontal)
                .padding(.bottom, 10)
                CustomSlectedVDividerCard()
                Spacer().frame(height: 80)

                TextField("여행파일의 이름은 13글자까지 가능해요", text: $tripModifyName)
                    .padding()
                    .font(.pretendardMedium16)
                    .multilineTextAlignment(.center)
                    .overlay(
                        Rectangle().frame(height: 1)
                            .padding(.horizontal, 12)
                            .foregroundColor(Color.black), alignment: .bottom
                    )
                    .padding(.top, 20)
                    .onTapGesture {
                        hideKeyboard()
                    }

                HStack(spacing: 20) {
                    CustomModifyTabField(selectedTab: $selectedTab, date: calendarViewModel.startTime, dateFormatter: dateFormatter, title: "출발 날짜", tag: 0, isCalendarVisible: $isCalendarVisible)

                    CustomModifyTabField(selectedTab: $selectedTab, date: calendarViewModel.endTime, dateFormatter: dateFormatter, title: "도착 날짜", tag: 1, isCalendarVisible: .constant(false))
                }
                .padding(.horizontal)

                if isCalendarVisible {
                    SwiftUIFSCalendarWrapper(startDate: $calendarViewModel.startTime, endDate: $calendarViewModel.endTime, tab: selectedTab)
                        .padding(.top, -10)
                        .frame(width: 340, height: 345)
                        .onChange(of: calendarViewModel.startTime) { newStartDate in
                            calendarViewModel.addSelectedDay(newStartDate ?? Date())
                        }
                        .onChange(of: calendarViewModel.endTime) { newEndDate in
                            calendarViewModel.addSelectedDay(newEndDate ?? Date())
                        }
                    Spacer().frame(height: 110)
                } else {
                    Color.clear.frame(width: 340, height: 460)
                }
            }
            .animation(.easeInOut, value: isCalendarVisible)
            .navigationBarBackButtonHidden(true)
            .padding()
        }
    }

    func hideKeyboard() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}

struct CustomModifyTabField: View {
    @Binding var selectedTab: Int
    var date: Date?
    var dateFormatter: DateFormatter
    let title: String
    let tag: Int
    @Binding var isCalendarVisible: Bool

    var body: some View {
        Button(action: {
            self.selectedTab = self.tag
            self.isCalendarVisible.toggle()
        }) {
            Text(date != nil ? dateFormatter.string(from: date!) : title)
                .font(.pretendardMedium16)
                .padding(.vertical, 20)
                .frame(width: 160)
                .background(self.selectedTab == self.tag ? Color.white : Color.homeBack)
                .foregroundColor(.black)
                .cornerRadius(5)
                .onTapGesture {
                    self.selectedTab = self.tag
                    if self.tag == 0 {
                        self.isCalendarVisible = true
                    }
                }
                .overlay(
                    Rectangle().frame(height: 1)
                        .foregroundColor(.black),
                    alignment: .bottom
                )
        }
        .background(self.selectedTab == self.tag ? Color.white : Color.clear)
    }
}
























