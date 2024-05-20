import SwiftUI
import UIKit

class SnapshotManager {
    var rootView: AnyView

    init(rootView: AnyView) {
        self.rootView = rootView
    }
    



    func captureSnapshot(completion: @escaping (UIImage) -> Void) {
        let hostingController = UIHostingController(rootView: rootView)
        let targetSize = UIScreen.main.bounds.size  // 화면 크기에 맞춤
        hostingController.view.frame = CGRect(origin: .zero, size: targetSize)
        hostingController.view.backgroundColor = .clear
        hostingController.view.layoutIfNeeded()  // 뷰가 완전히 로드될 수 있도록 한다.

        // 딜레이를 추가하여 뷰의 최신 상태가 렌더링될 시간을 제공
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            let renderer = UIGraphicsImageRenderer(size: targetSize)
            let image = renderer.image { _ in
                hostingController.view.drawHierarchy(in: hostingController.view.bounds, afterScreenUpdates: true)
            }
            completion(image)
        }
    }
}

