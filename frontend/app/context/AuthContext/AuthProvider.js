import { createContext, useEffect, useState } from 'react';
import { PROFILE_URL } from '../../api/urls';
import AxiosInstance from '../../api/AxiosInstance';

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [value, setValue] = useState({
    authenticated: false,
    user: null,
    loading: true,
    accessToken: null,
    refreshToken: null,
  });

  useEffect(() => {
    const accessToken = getAccessToken();
    const refreshToken = getRefreshToken();
    if (accessToken && !value.user) {
      AxiosInstance(PROFILE_URL)
        .then((res) =>
          setValue({
            authenticated: true,
            user: res.data.data,
            loading: false,
            accessToken: accessToken,
            refreshToken: refreshToken,
          })
        )
        .catch((res) =>
          setValue({
            authenticated: false,
            user: null,
            loading: false,
            accessToken: null,
            refreshToken: null,
          })
        );
    } else if (!accessToken) {
      setValue({
        authenticated: false,
        user: null,
        loading: false,
        accessToken: null,
        refreshToken: null,
      });
    }
  }, [value.accessToken, value.user]);

  const getAccessToken = () => localStorage.getItem('access-token');

  const getRefreshToken = () => localStorage.getItem('refresh-token');

  return (
    <AuthContext.Provider value={{ value, setValue }}>
      {children}
    </AuthContext.Provider>
  );
};
