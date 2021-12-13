import React from 'react';
import { useRouter } from 'next/router';
import NotFound from '../404';
import DefaultLayout from '../../app/components/layouts/DefaultLayout/DefaultLayout';
import ProfilePage from '../../app/components/templates/profile/ProfilePage';

const Index = function () {
  const router = useRouter();
  const { username } = router.query;
  if (!router.isReady) {
    return null;
  }
  if (!username?.startsWith('@')) {
    return <NotFound />;
  }

  return <ProfilePage />;
};

Index.getLayout = (page) => DefaultLayout(page);

export default Index;
