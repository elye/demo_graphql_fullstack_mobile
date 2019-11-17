import React, {Component} from 'react';

export default class SearchBox extends Component {
    state = { keyword: '' };

    onSubmit = _ => {
        this.props.onSubmit(this.state.keyword)
    };

    onChange = event => {
        const keyword = event.target.value;
        this.setState(({ keyword }));
    };

    render() {
        return (
            <form onSubmit={this.onSubmit}>
                <div >
                    <InputBox onChange={this.onChange}/>
                </div>
                <button className="key-submit-button" type="submit">
                    Get Wiki Hit
                </button>
            </form>
        );
    }
}

function InputBox(props) {
    return (
        <input className="key-input"
               required
               type="text"
               name="key"
               placeholder="search key"
               onChange={props.onChange}
        />
    );
}
