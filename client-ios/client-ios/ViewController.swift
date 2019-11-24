//
//  ViewController.swift
//  client-ios
//
import Alamofire
import Apollo
import SwiftyJSON
import UIKit

class ViewController: UIViewController {

  @IBOutlet var editTextSearch: UITextField!
  @IBOutlet var loadingIndicator: UIActivityIndicatorView!
  @IBOutlet var labelResult: UILabel!

  private var request: DataRequest? = nil

  private let baseUrl = "http://ENTER_YOUR_HOST_HERE:4000"
  private lazy var apollo = ApolloClient(url: URL(string: self.baseUrl)!)

  @IBAction func onGetWikiHitApollo(_ sender: Any) {
    guard let searchText = editTextSearch.text, !searchText.isEmpty else {
      return
    }

    labelResult.text = ""
    loadingIndicator.startAnimating()
    fetchApolloData(searchText: searchText)
  }
  
  @IBAction func onGetWikiHit(_ sender: Any) {
    guard let searchText = editTextSearch.text, !searchText.isEmpty else {
      return
    }

    labelResult.text = ""
    loadingIndicator.startAnimating()
    fetchData(searchText: searchText)
  }

  var watcher: GraphQLQueryWatcher<GetWikicountByKeywordQuery>?

  private func fetchApolloData(searchText: String) {
    watcher = apollo.watch(query: GetWikicountByKeywordQuery(keyword: searchText)) { result in
      self.loadingIndicator.stopAnimating()
      switch result {
      case .success(let graphQLResult):
        guard
          let count = graphQLResult.data?.wikiCount?.totalhits,
          let keyword = graphQLResult.data?.wikiCount?.keyword,
          count.isNumber
          else {
            self.showApolloResult(data: "No data found")
            return
        }
        self.showApolloResult(data: "\(keyword):\(count)")
      case .failure(let error):
        self.showApolloResult(data: error.localizedDescription)
      }
    }
  }

  override func viewDidLoad() {
    super.viewDidLoad()
    loadingIndicator.hidesWhenStopped = true
  }

  override func viewDidDisappear(_ animated: Bool) {
    request?.cancel()
  }

  private func fetchData(searchText: String) {

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
    request = Alamofire.SessionManager.default.request(baseUrl, method: .post, parameters: parameters, encoding: JSONEncoding.default)
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

  private func showApolloResult(data: String) {
    showResult(data: "Apollo: \(data)")
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
