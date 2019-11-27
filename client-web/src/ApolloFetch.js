import React from 'react';
import {useQuery} from "@apollo/react-hooks";
import SearchResult from "./SearchResult";
import Loading from "./loading"
import gql from 'graphql-tag';

const GET_WIKI_HIT = gql`
    query GetWikicountByKeyword($keyword: String!) {
        wikiCount(keyword: $keyword) {
            keyword
            totalhits
        }
    }
`;

function ApolloFetch({keyword}) {
    const {data, loading, error} = useQuery(GET_WIKI_HIT, {variables: {keyword: keyword}});

    let result
    if (keyword === "") result ="No Result";
    else if (loading) return (<Loading />);
    else if (error)  result = error.message;
    else result = data.wikiCount.keyword + ":" + data.wikiCount.totalhits;

    return <div><SearchResult value={result}/></div>;
}

export default ApolloFetch
