//
//  ApolloPresenter.swift
//  client-ios
//
import Apollo
import Foundation

class ApolloPresenter {

  private let view: MainView
  private let apollo = ApolloClient(url: URL(string: ViewController.baseUrl)!)
  private var watcher: GraphQLQueryWatcher<GetWikicountByKeywordQuery>?

  init(view: MainView) {
    self.view = view
  }

  func fetchData(searchText: String) {
    watcher = apollo.watch(query: GetWikicountByKeywordQuery(keyword: searchText)) { result in
      self.view.stopLoading()
      switch result {
      case .success(let graphQLResult):
        guard
          let count = graphQLResult.data?.wikiCount?.totalhits,
          let keyword = graphQLResult.data?.wikiCount?.keyword,
          count.isNumber
          else {
            self.showResult(data: "No data found")
            return
        }
        self.showResult(data: "\(keyword):\(count)")
      case .failure(let error):
        self.showResult(data: error.localizedDescription)
      }
    }
  }

  private func showResult(data: String) {
    self.view.showResult(data: "Apollo :" + data)
  }
}
