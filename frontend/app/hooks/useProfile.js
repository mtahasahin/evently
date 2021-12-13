import useSWR from 'swr';
import AxiosInstance from '../api/AxiosInstance';
import { USER_PROFILE_URL } from '../api/urls';

const useProfile = (username) => {
  const { data, mutate, error } = useSWR(USER_PROFILE_URL(username), (url) =>
    AxiosInstance.get(url).then((res) => res.data)
  );

  return {
    profile: data?.data,
    message: data?.message,
    isLoading: !error && !data,
    isError: error,
    reload: mutate,
  };
};

export default useProfile;
