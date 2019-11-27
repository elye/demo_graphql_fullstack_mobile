import React from "react";

const useFetch = (url, body) => {
    const [response, setResponse] = React.useState(null);
    const [error, setError] = React.useState(null);

    React.useEffect(() => {
        const FetchData = async () => {
            try {
                const method = "POST"
                const headers = {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                };
                const options = {
                    method,
                    headers
                };
                if (body) options.body = JSON.stringify(body);
                const res = await fetch(url, options);
                const json = await res.json();
                setResponse(json);
            } catch (error) {
                setError(error);
            }
        };
        FetchData();
    }, [body]);
    return { response, error };
};

export default useFetch