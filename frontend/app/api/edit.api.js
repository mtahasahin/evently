import AxiosInstance from "./AxiosInstance";
import {PROFILE_URL, CHANGE_PASSWORD_URL, CLOSE_ACCOUNT_URL} from "./urls";

const updateProfile = ({
                           email,
                           username,
                           profile: {
                               name,
                               dateOfBirth,
                               location,
                               profilePublic,
                               timezone,
                               language,
                               about,
                               websiteUrl,
                               facebookUsername,
                               twitterUsername,
                               instagramUsername,
                               githubUsername
                           }
                       }) => {
    return AxiosInstance.put(PROFILE_URL, {
        email,
        username,
        profile: {
            name,
            dateOfBirth,
            location,
            profilePublic,
            timezone,
            language,
            about,
            websiteUrl,
            facebookUsername,
            twitterUsername,
            instagramUsername,
            githubUsername
        }
    });
};

const getProfile = () => {
    return AxiosInstance
        .get(PROFILE_URL)
}

const changePassword = ({currentPassword, newPassword}) => {
    return AxiosInstance
        .put(CHANGE_PASSWORD_URL, {
            currentPassword,
            newPassword,
        });
};

const closeAccount = ({password}) => {
    return AxiosInstance
        .delete(CLOSE_ACCOUNT_URL, {params: {password}});
};

export default {
    updateProfile,
    getProfile,
    changePassword,
    closeAccount,
};