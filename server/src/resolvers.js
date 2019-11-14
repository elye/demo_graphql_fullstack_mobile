module.exports = {
    Query: {
        wikiCount: (_, { keyword }, { dataSources }) =>
            dataSources.wikiCountAPI.getWikiCountByKeyword({ keyword: keyword })
    }
};
