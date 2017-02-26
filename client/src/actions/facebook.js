import { FETCH , RETURN} from '../constants';
import axios from 'axios';

export function loadData (pageId) {
    return {
        type: FETCH,
        payload: axios.get(`getData/${pageId}`)
    }
}
export function returnPage() {
    return {
        type: RETURN,

    }
}