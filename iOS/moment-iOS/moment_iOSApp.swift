//
//  moment_iOSApp.swift
//  moment-iOS
//
//  Created by 양시관 on 1/27/24.
//

import SwiftUI
import FirebaseCore
import FirebaseMessaging

@main // 앱이 시작한다는 시작점을 알려주는
struct moment_iOSApp: App {
    
    var homeViewModel = HomeViewModel()
    var authViewModel = AuthViewModel()
    var homeBaseViewModel = HomeBaseViewModel()
   // var sharedViewModel = SharedViewModel()
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    //@ 부터 Adaptor 까지 기존 uikit 에 있던 기능들을 swiftui 에서 사용할 수 있도록 해놓은것이구만
   // AppDelegate 클래스의 인스턴스를 appDelegate 라는 이름으로 만들어 SwiftUI 생명주기와 함께 동작하도록 하는거구만여
    var body: some Scene {
        WindowGroup {// 여기서 앱메인 뷰를 그리고
            OnboardingView() // 이게 어떤 뷰부터 시작할지를 정해주는거구만
                .environmentObject(homeViewModel)
                .environmentObject(authViewModel) 
                .environmentObject(homeBaseViewModel)
                .environmentObject(SharedViewModel())
            
        }
        
        
    }
}


class AppDelegate: NSObject, UIApplicationDelegate{
    
    let gcmMessageIDKey = "gcm.message_id"
    
    // 앱이 켜졌을 때
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        
        // 파이어베이스 설정
        FirebaseApp.configure()
        
        // Setting Up Notifications...
        // 원격 알림 등록
        if #available(iOS 10.0, *) {
            // For iOS 10 display notification (sent via APNS)
            UNUserNotificationCenter.current().delegate = self
            
            let authOption: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
                options: authOption,
                completionHandler: {_, _ in })
        } else {
            let settings: UIUserNotificationSettings =
            UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
            application.registerUserNotificationSettings(settings)
        }
        
        application.registerForRemoteNotifications()
        
        
        // Setting Up Cloud Messaging...
        // 메세징 델리겟
        Messaging.messaging().delegate = self
        
        UNUserNotificationCenter.current().delegate = self
        return true
    }
    
    // fcm 토큰이 등록 되었을 때
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }
    
}

// Cloud Messaging...
extension AppDelegate: MessagingDelegate{
    
    // fcm 등록 토큰을 받았을 때
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {

        print("토큰을 받았다")
        // Store this token to firebase and retrieve when to send message to someone...
        let dataDict: [String: String] = ["token": fcmToken ?? ""]
        
        // Store token in Firestore For Sending Notifications From Server in Future...
        
        print(dataDict)
     
    }
}

// User Notifications...[AKA InApp Notification...]

@available(iOS 10, *)
extension AppDelegate: UNUserNotificationCenterDelegate {
  
    // 푸시 메세지가 앱이 켜져있을 때 나올떄
  func userNotificationCenter(_ center: UNUserNotificationCenter,
                              willPresent notification: UNNotification,
                              withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions)
                                -> Void) {
      
    let userInfo = notification.request.content.userInfo

    
    // Do Something With MSG Data...
    if let messageID = userInfo[gcmMessageIDKey] {
        print("Message ID: \(messageID)")
    }
    
    
    print(userInfo)

    completionHandler([[.banner, .badge, .sound]])
  }

    // 푸시메세지를 받았을 떄
  func userNotificationCenter(_ center: UNUserNotificationCenter,
                              didReceive response: UNNotificationResponse,
                              withCompletionHandler completionHandler: @escaping () -> Void) {
    let userInfo = response.notification.request.content.userInfo

    // Do Something With MSG Data...
    if let messageID = userInfo[gcmMessageIDKey] {
        print("Message ID: \(messageID)")
    }
      
    print(userInfo)

    completionHandler()
  }
}
