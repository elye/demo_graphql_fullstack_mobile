const { RESTDataSource } = require('apollo-datasource-rest');

class WikiCountAPI extends RESTDataSource {
  constructor() {
    super();
    this.baseURL = 'https://en.wikipedia.org/w/';
  }

  async getWikiCountByKeyword({ keyword }) {
    const response = await this.get('api.php',
        [["action", "query"], ["format", "json"], ["list", "search"], ["srsearch", keyword]]);
    return this.wikiCountReducer(keyword, response.query.searchinfo.totalhits);
  }

  wikiCountReducer(keyword, totalhits) {
    return {
      keyword: keyword,
      totalhits: `${totalhits}`
    };
  }
}

module.exports = WikiCountAPI;
