import React, { Component } from 'react';


export default class Input extends Component {
    constructor () {
        super();

        this.state = { input: "" };
    }

    inputHandler (e) {
        this.setState({ input: e.target.value })
    }

    getData () {
        this.props.getData(this.state.input)
    }

    render () {
        return (
            <div>
                <input className="input" value={this.state.input} onChange={this.inputHandler.bind(this)} placeholder="Enter your page ID" />
                <button className="button" onClick={this.getData.bind(this)}>Get Data!</button>
            </div>
        )
    }
}
