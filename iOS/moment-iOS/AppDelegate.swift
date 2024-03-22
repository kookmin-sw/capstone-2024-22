//
//  AppDelegate.swift
//  moment-iOS
//
//  Created by 양시관 on 3/5/24.
//

import Foundation
import UIKit


class AppDelegate: NSObject, UIApplicationDelegate {
  var notificationDelegate = NotificationDelegate()
  
  func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
  ) -> Bool {
    UNUserNotificationCenter.current().delegate = notificationDelegate
    return true
  }
}


    // 커밋테스트
