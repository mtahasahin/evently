import AxiosInstance from "./AxiosInstance";
import {USER_PROFILE_URL, FOLLOW_USER_URL, UNFOLLOW_USER_URL} from "./urls";

const getProfile = ({username}) => {
    return AxiosInstance
        .get(USER_PROFILE_URL(username))
}

const followUser = ({username}) => {
    return AxiosInstance
        .put(FOLLOW_USER_URL(username))
}

const unfollowUser = ({username}) => {
    return AxiosInstance
        .delete(UNFOLLOW_USER_URL(username))
}


export default {
    getProfile,
    followUser,
    unfollowUser
};