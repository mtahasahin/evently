import { useEffect } from 'react';
import { useRouter } from 'next/router';
import useAuth from '../hooks/useAuth';

export default function Route({ protectedRoutes, publicRoutes, children }) {
  const router = useRouter();
  const { authenticated, loading } = useAuth();
  const pathIsProtected = protectedRoutes.indexOf(router.pathname) !== -1;
  const pathIsPublic = publicRoutes.indexOf(router.pathname) !== -1;

  useEffect(() => {
    if (!loading && !authenticated && pathIsProtected) {
      router.push('/login');
    } else if (!loading && authenticated && pathIsPublic) {
      router.push('/feed');
    }
  }, [router, loading, authenticated, pathIsProtected, pathIsPublic]);

  if (!router.isReady) {
    return null;
  }

  if ((loading || !authenticated) && pathIsProtected) {
    return null;
  }

  if ((loading || authenticated) && pathIsPublic) {
    return null;
  }

  return children;
}
