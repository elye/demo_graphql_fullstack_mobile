import React, {Component} from 'react';
import SearchBox from "./SearchBox";
import SearchResult from "./SearchResult";


export default class Container extends Component {
    constructor(props) {
        super(props);
        this.state = {
            result: "No Result"
        };
    }

    onChange = keyword => {
        this.setState(s => ({ result:keyword }));
    };

    render() {
        return (
            <div>
                <div><SearchBox onSubmit={(keyword) => this.onChange(keyword)}/></div>
                <div><SearchResult value={this.state.result}/></div>
            </div>
        );
    }
}
