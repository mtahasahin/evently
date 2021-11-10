import {useContext} from "react";
import {AuthContext} from "../context/AuthContext/AuthProvider";
import AuthApi from "../api/auth.api";

const useAuth = () => {
    const {value, setValue} = useContext(AuthContext);
    return {
        authenticated: value.authenticated,
        user: value.user,
        loading: value.loading,
        accessToken: value.accessToken,
        refreshToken: value.refreshToken,
        login: (email, password) => {
            setValue(prev => ({...prev, loading: true}))
            return AuthApi.login(email, password).then(res => {
                setAccessToken(res.data.data.accessToken)
                setRefreshToken(res.data.data.refreshToken)
                setValue({
                    authenticated: true,
                    user: null,
                    loading: false,
                    accessToken: res.data.data.accessToken,
                    refreshToken: res.data.data.refreshToken
                });
            }).finally(() => setValue(value => ({...value, loading: false})));
        },
        signup: (name, email, password) => {
            setValue(prev => ({...prev, loading: true}))
            return AuthApi.register(name, email, password).then(res => {
                setAccessToken(res.data.data.accessToken)
                setRefreshToken(res.data.data.refreshToken)
                setValue({
                    authenticated: true,
                    user: null,
                    loading: false,
                    accessToken: res.data.data.accessToken,
                    refreshToken: res.data.data.refreshToken
                });
            }).finally(() => setValue(value => ({...value, loading: false})))
        },
        logout: () => {
            removeAccessToken();
            removeRefreshToken();
            setValue({authenticated: false, user: null, loading: false, accessToken: null, refreshToken: null});
        },
        reload: () => {
            setValue(prev => ({...prev, user:null, loading: true}))
        }
    }
}

export default useAuth


const setAccessToken = token => localStorage.setItem("access-token", token);
const getAccessToken = () => localStorage.getItem("access-token");
const removeAccessToken = () => localStorage.removeItem("access-token");

const setRefreshToken = token => localStorage.setItem("refresh-token", token);
const getRefreshToken = () => localStorage.getItem("refresh-token");
const removeRefreshToken = () => localStorage.removeItem("refresh-token");