import React, {Component} from 'react';
import {useQuery} from "@apollo/react-hooks";
import SearchResult from "./SearchResult";
import Loading from "./loading"
import gql from 'graphql-tag';
import SearchBox from "./SearchBox";

const GET_WIKI_HIT = gql`
    query GetWikicountByKeyword($keyword: String!) {
        wikiCount(keyword: $keyword) {
            keyword
            totalhits
        }
    }
`;

export default function FetchApollo() {

        const [keyword, setKeyword] = React.useState("");
        const {data, loading, error} = useQuery(GET_WIKI_HIT, {variables: {keyword: keyword}});

        console.log("Keyword: " + keyword)
        let result
        if (keyword === "") result = "No Result";
        else if (loading) result = "loading"
        else if (error) result = error.message;
        else result = data.wikiCount.keyword + ":" + data.wikiCount.totalhits;

        return (
            <div>
                <div><SearchBox caption={"Apollo Wiki Search"} onSubmit={(keyword) => setKeyword(keyword)}/></div>
                {loading && keyword != "" ?
                    (<Loading />) :
                    (<div><SearchResult value={result}/></div>)
                }

            </div>
        );
}
