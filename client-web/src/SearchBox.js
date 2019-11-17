import React, {Component} from 'react';

export default class SearchBox extends Component {
    render() {
        return (
            <form onSubmit={this.onSubmit}>
                <div > <InputBox /> </div>
                <button className="key-submit-button" type="submit">
                    Get Wiki Hit
                </button>
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
