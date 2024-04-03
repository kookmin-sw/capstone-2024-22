//
//  CustomToolTip.swift
//  moment-iOS
//
//  Created by 양시관 on 3/20/24.
//

import SwiftUI

import SwiftUI

struct CustomToolTip: View {
    @State var isDisplayTooltip: Bool = true
    @State private var tooltipOpacity: Double = 1
    @State private var tooltipScale: CGFloat = 1
    
    var body: some View {
        VStack(spacing: 4) {
            HStack {
                Spacer()
                
                Text("Tooltip 안내")
                    .font(.pretendardMedium11)
                
                    .padding(.trailing, 16)
            }
            
            if isDisplayTooltip {
                TooltipView(isDisplayTooltip: $isDisplayTooltip)
                    .onAppear {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 4) {
                            isDisplayTooltip = false
                        }
                    }
            }
            
            Spacer()
        }
        .padding()
    }
}

// MARK: - Tooltip View
struct TooltipView: View {
    @Binding var isDisplayTooltip: Bool
    
    var body: some View {
        HStack {
            
            
            TooltipShapeRightup()
            TooltipShapeLeftdown()
                .onTapGesture {
                    isDisplayTooltip = false
                }
            Spacer()
        }
    }
}

// MARK: - Tooltip Shape
struct TooltipShapeRightup: View {
    
    var body: some View {
        ZStack {
            CustomTriangleShapeRightup()
                .fill(.gray500)
                .padding(.leading, 135)
            
            CustomRectangleShapeRightup(text: "Tooltip 입니다1!")
        }
        .frame(width: 210, height: 25)
    }
}

struct TooltipShapeLeftdown: View {
    
    var body: some View {
        ZStack {
            CustomTriangleShapeLeftdown()
                .fill(.gray500)
                .frame(width: 24, height: 12) // 삼각형 크기 지정
                .offset(x: 0, y: 20)
            
            CustomRectangleShapeLeftdown(text: "Tooltip 입니다!3")
                .padding(.bottom, 12)
        }
        .frame(width: 210, height: 25)
    }
}


// MARK: - Custom Rectangle Shape
struct CustomRectangleShapeRightup: View {
    var text: String
    
    init(text: String) {
        self.text = text
    }
    
    var body: some View {
        VStack {
            Spacer()
            
            Text(text)
                .font(.pretendardMedium11)
                .foregroundColor(.white)
                .padding(.vertical, 3)
                .padding(.horizontal, 16)
                .background(
                    RoundedRectangle(cornerRadius:3)
                        .fill(.gray500)
                )
        }
    }
}

// MARK: - Custom Triangle Shape
struct CustomTriangleShapeRightup: Shape {
    var width: CGFloat
    var height: CGFloat
    var radius: CGFloat
    
    init(
        width: CGFloat = 10,
        height: CGFloat = 10,
        radius: CGFloat = 1
    ) {
        self.width = width
        self.height = height
        self.radius = radius
    }
    
    func path(in rect: CGRect) -> Path {
        var path = Path()
        
        path.move(to: CGPoint(x: rect.minX + width / 2 - radius, y: rect.minY))
        path.addQuadCurve(
            to: CGPoint(x: rect.minX + width / 2 + radius, y: rect.minY),
            control: CGPoint(x: rect.minX + width / 2, y: rect.minY + radius)
        )
        path.addLine(to: CGPoint(x: rect.minX + width, y: rect.minY + height))
        path.addLine(to: CGPoint(x: rect.minX, y: rect.minY + height))
        
        path.closeSubpath()
        
        return path
    }
}


struct CustomRectangleShapeLeftdown: View {
    var text: String
    
    init(text: String) {
        self.text = text
    }
    
    var body: some View {
        VStack {
            Spacer()
            
            Text(text)
                .font(.pretendardMedium11)
                .foregroundColor(.white)
                .padding(.vertical, 10)
                .padding(.horizontal, 16)
                .background(
                    RoundedRectangle(cornerRadius:3)
                        .fill(.gray500)
                )
        }
    }
}

// MARK: - Custom Triangle Shape
struct CustomTriangleShapeLeftdown: Shape {
    var width: CGFloat
    var height: CGFloat
    var radius: CGFloat
    
    init(
        width: CGFloat = 10,
        height: CGFloat = 10,
        radius: CGFloat = 1
    ) {
        self.width = width
        self.height = height
        self.radius = radius
    }
    
    func path(in rect: CGRect) -> Path {
        var path = Path()
        
        // 아래쪽을 가리키는 삼각형을 그립니다.
        path.move(to: CGPoint(x: rect.minX, y: rect.minY))
        path.addLine(to: CGPoint(x: rect.minX + width / 2, y: rect.minY + height))
        path.addLine(to: CGPoint(x: rect.minX + width, y: rect.minY))
        path.closeSubpath()
        
        return path
    }
}


struct CustomToolTip_Previews: PreviewProvider {
    static var previews: some View {
        CustomToolTip()
    }
}
