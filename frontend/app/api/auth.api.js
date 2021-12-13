import axios from 'axios';
import { LOGIN_URL, REGISTER_URL } from './urls';

const register = (name, email, password) => {
  return axios.post(REGISTER_URL, {
    name,
    email,
    password,
    language: navigator.language || navigator.userLanguage,
  });
};

const login = (email, password) => {
  return axios.post(LOGIN_URL, {
    email,
    password,
  });
};

const AuthApi = {
  register,
  login,
};

export default AuthApi;
