import React from "react";
import SearchBox from "./SearchBox";
import SearchResult from "./SearchResult";
import Loading from "./loading";
import {BASEURL} from "./constant";

function FetchNormal() {
    const query = `
    query GetWikicountByKeyword($keyword: String!) {
        wikiCount
            (keyword: $keyword) {
                keyword 
                totalhits
            } 
    }`

    const [keyword, setKeyword] = React.useState(null);
    const [response, setResponse] = React.useState(null);
    const [error, setError] = React.useState(null);
    const [isLoading, setIsLoading] = React.useState(false);

    React.useEffect(() => {
        const FetchData = async () => {
            try {
                setError(null)
                setIsLoading(true)
                const method = "POST"
                const headers = {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                };
                const options = {
                    method,
                    headers
                };

                const body = {
                    query: query,
                    variables : { keyword }
                };

                if (keyword == null) {
                    setResponse("No Result");
                } else {
                    if (body) options.body = JSON.stringify(body);
                    const res = await fetch(BASEURL, options);
                    const json = await res.json();
                    setResponse(json.data.wikiCount.keyword + ":" + json.data.wikiCount.totalhits);
                }
            } catch (error) {
                setError(error.toString());
                setResponse(null)
            }
            setIsLoading(false)
        };
        FetchData();
    }, [keyword]);

    return (
        <div>
            <div><SearchBox caption={"Normal Wiki Search"} onSubmit={(keyword) => setKeyword(keyword) }/></div>
            <div>
                {error &&  <SearchResult value={error} />}
                {isLoading ?
                    (<Loading />) :
                    (response && <SearchResult value={response}/>)
                }
            </div>
        </div>
    );
}
export default FetchNormal