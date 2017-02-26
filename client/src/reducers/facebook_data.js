import { FETCH_PENDING, FETCH_FULFILLED, FETCH_REJECTED, RETURN} from '../constants';

const initialState = {
    loading: false,
    failed: false,
    data: null,
};


export default function (state = initialState, action) {
    switch (action.type) {
        case FETCH_PENDING:
            return Object.assign({}, state, {data: null, loading: true, failed: false });
        case FETCH_FULFILLED:
            let { finalLikesArray: likes, finalSharesArray: shares  } = parseResponse(action.payload.data);
            return Object.assign({}, state, {data: { likes, shares }, loading: false, failed: false });
        case FETCH_REJECTED:
            return Object.assign({}, state, {data: null, loading: false, failed: true });
        case RETURN:
            return initialState;
        default:
            return state;
    }
}

function parseResponse (data) {
    let dataByHourObject = {};

    // Order likes and shares by hour in an object
    data.forEach((dataPerHour) => {
        dataByHourObject[dataPerHour.time] = { likes: dataPerHour.likes, shares: dataPerHour.shares }
    });

    let finalSharesArray = [];
    let finalLikesArray = [];

    // Push shares and likes in each respective array
    // In case there is no data for that hour, push a 0
    for (let i = 0; i < 24; i++) {
        if (dataByHourObject[i]) {
            finalLikesArray.push(dataByHourObject[i].likes);
            finalSharesArray.push(dataByHourObject[i].shares);
        }

        else {
            finalLikesArray.push(0);
            finalSharesArray.push(0);
        }
    }

    // Return data to plot the graphs
    return {
        finalLikesArray,
        finalSharesArray,
    }
}