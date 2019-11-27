import React, {Component} from 'react';
import SearchBox from "./SearchBox";
import SearchResult from "./SearchResult";
import {useQuery} from '@apollo/react-hooks';
import Loading from "./loading"
import gql from 'graphql-tag';
import useFetch from "./useFetch";

const GET_WIKI_HIT = gql`
    query GetWikicountByKeyword($keyword: String!) {
        wikiCount(keyword: $keyword) {
            keyword
            totalhits
        }
    }
`;

export default class Container extends Component {
    constructor(props) {
        super(props);
        this.state = {
            keyword: ""
        };
    }

    onChange = keyword => {
        this.setState({
                keyword: this.getHitCount(keyword)
            }
        );
    };

    getHitCount(keyword) {
        return keyword
    }

    render() {
        return (
            <div>
                <div><SearchBox onSubmit={(keyword) => this.onChange(keyword)}/></div>
                <div><WikiHit keyword={this.state.keyword}/></div>
                <div><NormalFetch keyword={this.state.keyword}/></div>
            </div>
        );
    }
}

function WikiHit({keyword}) {
    const {data, loading, error} = useQuery(GET_WIKI_HIT, {variables: {keyword: keyword}});

    let result
    if (keyword === "") result ="No Result";
    else if (loading) return (<Loading />);
    else if (error)  result = error.message;
    else result = data.wikiCount.keyword + ":" + data.wikiCount.totalhits;

    return <div><SearchResult value={result}/></div>;
}

function NormalFetch({keyword}) {
    const query = {
        query: "query GetWikicountByKeyword($keyword: String!)" +
            "{ wikiCount                                      " +
            "   (keyword: $keyword) {                         " +
            "      keyword                                    " +
            "      totalhits                                  " +
            "    }                                            " +
            "}                                                ",
        variables : "{ \"keyword\": \"" + keyword + "\" }"
    };

    const res = useFetch(`http://localhost:4000`, query)
    let result
    if (keyword === "") result = "No Result";
    else if (!res.response) return (<Loading />);
    else {
        if (res.response == null) {
            result = "No Result"
        } else if (res.response.errors != null && res.response.errors.length > 0) {
            result = res.response.errors[0].message
        } else {
            result = res.response.data.wikiCount.keyword + ":" + res.response.data.wikiCount.totalhits;
        }
    }
    return <div><SearchResult value={result}/></div>;
}
