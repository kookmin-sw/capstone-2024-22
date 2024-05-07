//
//  SharedViewModel.swift
//  moment-iOS
//
//  Created by 양시관 on 5/7/24.
//

import Foundation

class SharedViewModel: ObservableObject {
    @Published var isSaved: Bool = false
    
    
    @Published var text : String =  ""
    @Published var inputText : String = ""
    @Published var StartLocatio : String = ""
    @Published var EndLocationend : String = ""
    @Published var EndLocation : String = ""
    
    
    @Published var tripRecord : String = ""
    @Published var tripExplaneStart : String = ""
    @Published var tripnameStart : String = ""
    @Published var tripExplaneEnd : String = ""
    @Published var tripnameEnd: String = ""

    
    
}
