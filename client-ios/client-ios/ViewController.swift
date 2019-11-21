//
//  ViewController.swift
//  client-ios
//
import Alamofire
import SwiftyJSON
import UIKit

class ViewController: UIViewController {

  @IBOutlet var editTextSearch: UITextField!
  @IBOutlet var loadingIndicator: UIActivityIndicatorView!
  @IBOutlet var labelResult: UILabel!

  private var request: DataRequest? = nil

  @IBAction func onGetWikiHit(_ sender: Any) {
    guard let searchText = editTextSearch.text, !searchText.isEmpty else {
      return
    }

    labelResult.text = ""
    loadingIndicator.startAnimating()
    fetchData(searchText: searchText)
  }

  override func viewDidLoad() {
    super.viewDidLoad()
    loadingIndicator.hidesWhenStopped = true
  }

  override func viewDidDisappear(_ animated: Bool) {
    request?.cancel()
  }


  private func fetchData(searchText: String) {

    let url = "http://EDIT_YOUR_HOST_HERE:4000"

    let search = """
    { wikiCount
        (keyword: \"\(searchText)\") {
          keyword
          totalhits
        }
    }
    """

    let parameters: Parameters = [
      "query": search
    ]

    request?.cancel()
    request = Alamofire.SessionManager.default.request(url, method: .post, parameters: parameters, encoding: JSONEncoding.default)
      .responseData { (resData) -> Void in
        self.loadingIndicator.stopAnimating()

        switch (resData.result) {
        case let .success(data):
          let swiftyJsonVar = JSON(data)
          let countObj = swiftyJsonVar["data"]["wikiCount"]["totalhits"]
          let keywordObj = swiftyJsonVar["data"]["wikiCount"]["keyword"]
          guard
            let count = countObj.rawValue as? String,
            let keyword = keywordObj.rawValue as? String,
            count.isNumber
            else {
              self.showResult(data: "No data found")
              return
          }
          self.showResult(data: "\(keyword):\(count)")
        case let .failure(error):
          self.showResult(data: error.localizedDescription)
        }
    }

  }

  private func showResult(data: String) {
    labelResult.text = data
  }
}

extension String  {
    var isNumber: Bool {
        return !isEmpty && rangeOfCharacter(from: CharacterSet.decimalDigits.inverted) == nil
    }
}
