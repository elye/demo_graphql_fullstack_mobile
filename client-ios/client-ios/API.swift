//  This file was automatically generated and should not be edited.

import Apollo
import Foundation

public final class GetWikicountByKeywordQuery: GraphQLQuery {
  /// The raw GraphQL definition of this operation.
  public let operationDefinition =
    """
    query GetWikicountByKeyword($keyword: String!) {
      wikiCount(keyword: $keyword) {
        __typename
        keyword
        totalhits
      }
    }
    """

  public let operationName = "GetWikicountByKeyword"

  public var keyword: String

  public init(keyword: String) {
    self.keyword = keyword
  }

  public var variables: GraphQLMap? {
    return ["keyword": keyword]
  }

  public struct Data: GraphQLSelectionSet {
    public static let possibleTypes = ["Query"]

    public static let selections: [GraphQLSelection] = [
      GraphQLField("wikiCount", arguments: ["keyword": GraphQLVariable("keyword")], type: .object(WikiCount.selections)),
    ]

    public private(set) var resultMap: ResultMap

    public init(unsafeResultMap: ResultMap) {
      self.resultMap = unsafeResultMap
    }

    public init(wikiCount: WikiCount? = nil) {
      self.init(unsafeResultMap: ["__typename": "Query", "wikiCount": wikiCount.flatMap { (value: WikiCount) -> ResultMap in value.resultMap }])
    }

    public var wikiCount: WikiCount? {
      get {
        return (resultMap["wikiCount"] as? ResultMap).flatMap { WikiCount(unsafeResultMap: $0) }
      }
      set {
        resultMap.updateValue(newValue?.resultMap, forKey: "wikiCount")
      }
    }

    public struct WikiCount: GraphQLSelectionSet {
      public static let possibleTypes = ["WikiCount"]

      public static let selections: [GraphQLSelection] = [
        GraphQLField("__typename", type: .nonNull(.scalar(String.self))),
        GraphQLField("keyword", type: .nonNull(.scalar(String.self))),
        GraphQLField("totalhits", type: .scalar(String.self)),
      ]

      public private(set) var resultMap: ResultMap

      public init(unsafeResultMap: ResultMap) {
        self.resultMap = unsafeResultMap
      }

      public init(keyword: String, totalhits: String? = nil) {
        self.init(unsafeResultMap: ["__typename": "WikiCount", "keyword": keyword, "totalhits": totalhits])
      }

      public var __typename: String {
        get {
          return resultMap["__typename"]! as! String
        }
        set {
          resultMap.updateValue(newValue, forKey: "__typename")
        }
      }

      public var keyword: String {
        get {
          return resultMap["keyword"]! as! String
        }
        set {
          resultMap.updateValue(newValue, forKey: "keyword")
        }
      }

      public var totalhits: String? {
        get {
          return resultMap["totalhits"] as? String
        }
        set {
          resultMap.updateValue(newValue, forKey: "totalhits")
        }
      }
    }
  }
}
