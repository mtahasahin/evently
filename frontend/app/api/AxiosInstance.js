import axios from 'axios';

const AxiosInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_HOST_URL+"/api",
  headers: {
    'Content-Type': 'application/json',
    accept: 'application/json',
  },
});

export default AxiosInstance;

AxiosInstance.interceptors.request.use(function (config) {
  if (localStorage.getItem('access-token')) {
    config.headers['Authorization'] =
      'Bearer ' + localStorage.getItem('access-token');
  }
  return config;
});
