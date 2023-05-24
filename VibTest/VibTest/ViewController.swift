//
//  ViewController.swift
//  VibTest
//
//  Created by 주효진 on 2023/05/12.
//
import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var pkvVibrationCategory: UIPickerView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // 피커 뷰 delegate, dataSource
        pkvVibrationCategory.delegate = self
        pkvVibrationCategory.dataSource = self
    }

    @IBAction func btnActVibrate(_ sender: Any) {
        print("btn ok")
        let index = pkvVibrationCategory.selectedRow(inComponent: 0)
        Vibration.allCases[index].vibrate()
    }
    
}

extension ViewController: UIPickerViewDelegate, UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        Vibration.allCases.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        Vibration.allCases[row].rawValue
    }
}
