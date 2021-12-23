const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

export const PROFILE_URL = BASE_URL + '/profile';
export const AUTH_URL = BASE_URL + '/auth';
export const EVENT_URL = BASE_URL + '/event';

export const LOGIN_URL = AUTH_URL + '/login';
export const REGISTER_URL = AUTH_URL + '/register';
export const REFRESH_TOKEN_URL = AUTH_URL + '/refresh-token';
export const CHANGE_PASSWORD_URL = AUTH_URL + '/change-password';
export const CLOSE_ACCOUNT_URL = AUTH_URL + '/close-account';

export const USER_PROFILE_URL = (username) => PROFILE_URL + '/' + username;
export const FOLLOW_USER_URL = (username) =>
  PROFILE_URL + '/' + username + '/following';
export const UNFOLLOW_USER_URL = (username) =>
  PROFILE_URL + '/' + username + '/following';

export const CREATE_EVENT_URL = EVENT_URL;
export const GET_EVENT_URL = (slug, key) =>
  slug ? EVENT_URL + '/' + slug + `?key=${key ?? ''}` : null;
export const EDIT_EVENT_URL = (slug) => EVENT_URL + '/' + slug;
export const REMOVE_EVENT_URL = (slug) => EVENT_URL + '/' + slug;

export const EDIT_EVENT_QUESTIONS_URL = (slug) =>
  EVENT_URL + '/' + slug + '/questions';
export const APPLY_TO_EVENT_URL = (slug) => EVENT_URL + '/' + slug + '/answers';
export const CANCEL_APPLICATION_URL = (slug) =>
  EVENT_URL + '/' + slug + '/answers';
export const GET_EVENT_APPLICATIONS_URL = (slug, index, fetchAll) =>
  slug &&
  EVENT_URL + '/' + slug + `/answers${fetchAll ? '/all' : ''}?page=${index}`;
export const CONFIRM_APPLICATION_URL = (slug, applicationId) =>
  slug && EVENT_URL + '/' + slug + `/answers/${applicationId}/approve`;
export const GET_ANSWERS_URL = (slug, applicationId) =>
  slug && EVENT_URL + '/' + slug + `/answers/${applicationId}`;
