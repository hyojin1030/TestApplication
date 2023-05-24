//
//  Vibration.swift
//  VibTest
//
//  Created by 주효진 on 2023/05/12.
//

import UIKit
import AVFoundation

enum Vibration: String, CaseIterable {
    
    static var allCases: [Vibration] {
        let defaultList = [
            error,
            success,
            warning,
            light,
            medium,
            heavy,
            selection,
            oldSchool,
        ]
        
        if #available(iOS 13.0, *) {
            return defaultList + [soft, rigid,]
        }
        
        return defaultList
    }
    
    case error
    case success
    case warning
    case light
    case medium
    case heavy
    case selection
    /// 옛 진동 방식
    case oldSchool
    
    @available(iOS 13.0, *)
    case soft
    
    @available(iOS 13.0, *)
    case rigid
    
    public func vibrate() {
        switch self {
        case .error:
            UINotificationFeedbackGenerator().notificationOccurred(.error)
        case .success:
            UINotificationFeedbackGenerator().notificationOccurred(.success)
        case .warning:
            UINotificationFeedbackGenerator().notificationOccurred(.warning)
        case .light:
            UIImpactFeedbackGenerator(style: .light).impactOccurred(intensity: 1.0)
        case .medium:
            UIImpactFeedbackGenerator(style: .medium).impactOccurred(intensity: 1.0)
        case .heavy:
            UIImpactFeedbackGenerator(style: .heavy).impactOccurred(intensity: 1.0)
        case .soft:
            if #available(iOS 13.0, *) {
                UIImpactFeedbackGenerator(style: .soft).impactOccurred(intensity: 1.0)
            }
        case .rigid:
            if #available(iOS 13.0, *) {
                UIImpactFeedbackGenerator(style: .rigid).impactOccurred(intensity: 1.0)
            }
        case .selection:
            UISelectionFeedbackGenerator().selectionChanged()
        case .oldSchool:
            AudioServicesPlaySystemSound(SystemSoundID(kSystemSoundID_Vibrate))
        }
    }
}
