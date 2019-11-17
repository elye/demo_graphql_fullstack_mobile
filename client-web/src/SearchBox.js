import React, {Component} from 'react';

export default class SearchBox extends Component {
    render() {
        return (
            <form onSubmit={this.onSubmit}>
                <InputBox />
            </form>
        );
    }
}

class InputBox extends Component {
    render() {
        return (
            <input className="key-input"
                required
                type="text"
                name="key"
                placeholder="search key"
                onChange={this.onChange}
            />
        );
    }
}
