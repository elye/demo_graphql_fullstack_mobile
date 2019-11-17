import React, {Component} from 'react';

export default class SearchResult extends Component {
    render() {
        return (
            <h3>{this.props.value}</h3>
        );
    }
}
