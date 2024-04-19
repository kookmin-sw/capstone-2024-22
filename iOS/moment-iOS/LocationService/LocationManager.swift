//
//  LocationManager.swift
//  moment-iOS
//
//  Created by 양시관 on 4/19/24.
//

import Foundation
import CoreLocation

class LocationManager : NSObject, ObservableObject, CLLocationManagerDelegate
{
    let manager = CLLocationManager()
       @Published var degrees: Double = 0
       
       override init() {
           super.init()
           manager.delegate = self
           manager.startUpdatingHeading()
           manager.requestWhenInUseAuthorization()
       }
       
       func locationManager(_ manager: CLLocationManager, didUpdateHeading newHeading: CLHeading) {
           degrees = newHeading.trueHeading
       }
    func requestLocation() {
        manager.requestLocation()
    }
   }

