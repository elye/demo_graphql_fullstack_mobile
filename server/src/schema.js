const { gql } = require('apollo-server');

const typeDefs = gql`
  type Query {
    wikiCount(keyword: String!): WikiCount
  }
  type WikiCount {
    keyword: String!
    totalhits: String
  }
`;

module.exports = typeDefs;
