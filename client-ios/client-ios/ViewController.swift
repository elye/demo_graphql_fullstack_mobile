//
//  ViewController.swift
//  client-ios
//
import Apollo
import UIKit

class ViewController: UIViewController, MainView {

  @IBOutlet var editTextSearch: UITextField!
  @IBOutlet var loadingIndicator: UIActivityIndicatorView!
  @IBOutlet var labelResult: UILabel!

  private static let baseUrl = "http://ENTER_YOUR_HOST_HERE:4000"
  private let apollo = ApolloClient(url: URL(string: ViewController.baseUrl)!)

  private var mainPresenter: MainPresenter? = nil

  required init?(coder: NSCoder) {
    super.init(coder: coder)
    self.mainPresenter = MainPresenter(view: self)
  }

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
    self.mainPresenter?.fetchData(searchText: searchText)
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
    self.mainPresenter?.cancelFetch()
  }

  private func showApolloResult(data: String) {
    showResult(data: "Apollo: \(data)")
  }

  func showResult(data: String) {
    labelResult.text = data
  }

  func stopLoading() {
    loadingIndicator.stopAnimating()
  }
}

extension String  {
    var isNumber: Bool {
        return !isEmpty && rangeOfCharacter(from: CharacterSet.decimalDigits.inverted) == nil
    }
}
