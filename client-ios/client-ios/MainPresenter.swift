//
//  MainPresenter.swift
//  client-ios
//

import Foundation
import Alamofire
import SwiftyJSON

class MainPresenter {

  private let view: MainView
  private var request: DataRequest? = nil

  init(view: MainView) {
    self.view = view
  }

  func fetchData(searchText: String) {

    let search = """
    query GetWikicountByKeyword($keyword: String!)
    { wikiCount
        (keyword: $keyword) {
          keyword
          totalhits
        }
    }
    """

    let parameters: Parameters = [
      "query": search,
      "variables": "{ \"keyword\": \"\(searchText)\" }"
    ]

    request?.cancel()
    request = Alamofire.SessionManager.default.request(
      ViewController.baseUrl, method: .post, parameters: parameters, encoding: JSONEncoding.default)
      .responseData { (resData) -> Void in
        self.view.stopLoading()

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
              self.view.showResult(data: "No data found")
              return
          }
          self.view.showResult(data: "\(keyword):\(count)")
        case let .failure(error):
          self.view.showResult(data: error.localizedDescription)
        }
    }
  }

  func cancelFetch() {
    request?.cancel()
  }
}
