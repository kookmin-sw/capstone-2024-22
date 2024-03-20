//
//  Int-Extensions.swift
//  NaverRECORDAPP
//
//  Created by 양시관 on 1/10/24.
//

import Foundation


extension Int {
  var formattedTimeString: String {
    let time = Time.fromSeconds(self)
    let hoursString = String(format: "%02d", time.hours)
    let minutesString = String(format: "%02d", time.minutes)
    let secondsString = String(format: "%02d", time.seconds)
    
    return "\(hoursString) : \(minutesString) : \(secondsString)"
  }
  
  var formattedSettingTime: String {
    let currentDate = Date()
    let settingDate = currentDate.addingTimeInterval(TimeInterval(self))
    
    let formatter = DateFormatter()
    formatter.locale = Locale(identifier: "ko_KR")
    formatter.timeZone = TimeZone(identifier: "Asia/Seoul")
    formatter.dateFormat = "HH:mm"
    
    let formattedTime = formatter.string(from: settingDate)
    return formattedTime
  }
}
