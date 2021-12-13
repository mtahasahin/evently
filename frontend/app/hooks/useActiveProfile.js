import useSWR from 'swr/immutable';
import AxiosInstance from "../api/AxiosInstance";
import {USER_PROFILE_URL} from "../api/urls";
import {useRouter} from "next/router";
import useAuth from "./useAuth";


const useActiveProfile = () => {
    const {query: {username}} = useRouter();

    const {user} = useAuth();

    const {
        data,
        mutate,
        error
    } = useSWR(USER_PROFILE_URL(username?.substring(1) ?? user.username), (url) => AxiosInstance.get(url).then(res => res.data));

    return {
        username: username?.substring(1),
        profile: data?.data,
        message: data?.message,
        isLoading: !error && !data,
        isError: error,
        reload: mutate
    }
}

export default useActiveProfile;