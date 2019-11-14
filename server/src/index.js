const { ApolloServer } = require('apollo-server');
const typeDefs = require('./schema');
const resolvers = require('./resolvers');

const WikiCountAPI = require('./datasources/wikicount');
const server = new ApolloServer({ 
  typeDefs,
  resolvers,
  dataSources: () => ({
    wikiCountAPI: new WikiCountAPI()
  })
});

server.listen(3000).then(({ url }) => {
  console.log(`🚀 Server ready at ${url}`);
});
