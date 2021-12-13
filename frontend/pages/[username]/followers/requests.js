import React from 'react';
import DefaultLayout from '../../../app/components/layouts/DefaultLayout/DefaultLayout';
import FollowerFollowingBase from '../../../app/components/templates/profile/FollowerFollowingBase';

const Index = function () {
  return <FollowerFollowingBase type="follower-requests" />;
};

Index.getLayout = (page) => DefaultLayout(page);

export default Index;
