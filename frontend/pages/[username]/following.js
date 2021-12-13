import React from 'react';
import DefaultLayout from '../../app/components/layouts/DefaultLayout/DefaultLayout';
import FollowerFollowingBase from '../../app/components/templates/profile/FollowerFollowingBase';

const Following = function () {
  return <FollowerFollowingBase type="following" />;
};

Following.getLayout = (page) => DefaultLayout(page);

export default Following;
