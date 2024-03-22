//
//  TimerViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation

import UIKit
import Foundation
import Foundation
import UIKit

class LikeViewModel: ObservableObject {
  @Published var isDisplaySetTimeView: Bool
  @Published var time: Time
  @Published var timer: Timer?
  @Published var timeRemaining: Int
  @Published var isPaused: Bool
  var notificationService: NotificationService
  
  init(
    isDisplaySetTimeView: Bool = true,
    time: Time = .init(hours: 0, minutes: 0, seconds: 0),
    timer: Timer? = nil,
    timeRemaining: Int = 0,
    isPaused: Bool = false,
    notificationService: NotificationService = .init()
  ) {
    self.isDisplaySetTimeView = isDisplaySetTimeView
    self.time = time
    self.timer = timer
    self.timeRemaining = timeRemaining
    self.isPaused = isPaused
    self.notificationService = notificationService
  }
}

extension LikeViewModel {
  func settingBtnTapped() {
    isDisplaySetTimeView = false
    timeRemaining = time.convertedSeconds
    startTimer()
  }
  
  func cancelBtnTapped() {
    stopTimer()
    isDisplaySetTimeView = true
  }
  
  func pauseOrRestartBtnTapped() {
    if isPaused {
      startTimer()
    } else {
      timer?.invalidate()
      timer = nil
    }
    isPaused.toggle()
  }
}

private extension LikeViewModel {
  func startTimer() {
    guard timer == nil else { return }
    
    var backgroundTaskID: UIBackgroundTaskIdentifier?
    backgroundTaskID = UIApplication.shared.beginBackgroundTask {
      if let task = backgroundTaskID {
        UIApplication.shared.endBackgroundTask(task)
        backgroundTaskID = .invalid
      }
    }
    
    timer = Timer.scheduledTimer(
      withTimeInterval: 1,
      repeats: true
    ) { _ in
      if self.timeRemaining > 0 {
        self.timeRemaining -= 1
      } else {
        self.stopTimer()
        self.notificationService.sendNotification()
        
        if let task = backgroundTaskID {
          UIApplication.shared.endBackgroundTask(task)
          backgroundTaskID = .invalid
        }
      }
    }
  }
  
  func stopTimer() {
    timer?.invalidate()
    timer = nil
  }
}
