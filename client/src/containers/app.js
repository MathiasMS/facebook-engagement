import React, { Component } from 'react';
import FacebookChart from '../components/chart';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import Input from '../components/input';
import { loadData, returnPage } from '../actions/facebook';

class App extends Component {

	getData (pageId) {
		this.props.loadData(pageId);
	}


	render() {
        if (this.props.facebook.loading) {
        	return <div className="container">
				<h1>Loading...</h1>
			</div>
		}
		else if (this.props.facebook.data) {
			return (
				<div className="container">
					<h1>Your page insights :)</h1>
					<FacebookChart data={this.props.facebook.data} />
					<button onClick={this.props.returnPage}>Find another Page</button>
				</div>
			)
		}
		else if (this.props.facebook.failed) {
            return (
				<div className="container">
					<h1>There was a problem loading your data</h1>
					<button onClick={this.props.returnPage}>Find another Page</button>
				</div>
            )
		}

		return (
			<div className="container">
				<h1>Please Enter your page ID</h1>
				<Input getData={this.getData.bind(this)} />
			</div>
        );
	}
}

function mapStateToProps (state) {
	return ({ facebook: state.facebook });
}
function mapDispatchToProps (dispatch) {
    return bindActionCreators({ loadData, returnPage }, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(App);