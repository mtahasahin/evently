import React from 'react';
import ActivityFeedPage from '../app/components/templates/ActivityFeedPage';
import HomeLayout from '../app/components/layouts/HomeLayout/HomeLayout';

const Feed = function () {
  return <ActivityFeedPage />;
};

Feed.getLayout = (page) => HomeLayout(page);

export default Feed;
