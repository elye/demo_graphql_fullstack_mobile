import React, {Component} from 'react';
import SearchBox from "./SearchBox";
import NormalFetch from "./NormalFetch";
import WikiFetch from "./WikiFetch";

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
                <div><SearchBox caption={"Apollo Wiki Search"} onSubmit={(keyword) => this.onChange(keyword)}/></div>
                <div><WikiFetch keyword={this.state.keyword}/></div>
                <div><NormalFetch /></div>
            </div>
        );
    }
}
