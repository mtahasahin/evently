import React, { useEffect } from 'react';
import { useRouter } from 'next/router';
import useAuth from '../../app/hooks/useAuth';

const OAuth2RedirectHandler = () => {
  const router = useRouter();
  const { oauthlogin } = useAuth();

  useEffect(() => {
    const token = router.query.token;
    const error = router.query.error;

    if (token) {
      oauthlogin(token);
    } else {
      router.push(`/login?error=${error}`);
    }
  }, [router.query]);

  return null;
};

export default OAuth2RedirectHandler;
