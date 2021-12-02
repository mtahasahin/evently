import useSWR from 'swr/immutable';
import AxiosInstance from "../api/AxiosInstance";
import {GET_EVENT_URL} from "../api/urls";
import {useRouter} from "next/router";


const useActiveEvent = () => {
    const {query} = useRouter();
    const {slug, key} = query;
    const {
        data,
        mutate,
        error
    } = useSWR(GET_EVENT_URL(slug, key), (url) => AxiosInstance.get(url).then(res => res.data));

    return {
        slug: slug,
        event: data?.data,
        message: data?.message,
        isLoading: !error && !data,
        isError: error,
        reload: mutate
    }
}

export default useActiveEvent;