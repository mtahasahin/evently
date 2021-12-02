import useSWR from 'swr';
import AxiosInstance from "../api/AxiosInstance";
import {GET_EVENT_URL} from "../api/urls";


const useEvent = (slug, key) => {
    const {
        data,
        mutate,
        error
    } = useSWR(GET_EVENT_URL(slug, key), (url) => AxiosInstance.get(url).then(res => res.data));

    return {
        event: data?.data,
        message: data?.message,
        isLoading: !error && !data,
        isError: error,
        reload: mutate
    }
}

export default useEvent;