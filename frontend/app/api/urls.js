const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

export const PROFILE_URL = BASE_URL+'profile/';
export const AUTH_URL = BASE_URL+'auth/';

export const LOGIN_URL = AUTH_URL+'login';
export const REGISTER_URL = AUTH_URL+'register';
export const REFRESH_TOKEN_URL = AUTH_URL+'refresh-token';
export const CHANGE_PASSWORD_URL = AUTH_URL+'change-password';
export const CLOSE_ACCOUNT_URL = AUTH_URL+'close-account';

export const USER_PROFILE_URL = (username) => PROFILE_URL+username;
export const FOLLOW_USER_URL = (username) => PROFILE_URL+username+'/following';
export const UNFOLLOW_USER_URL = (username) => PROFILE_URL+username+'/following';
