import React from 'react';
import DefaultLayout from '../../app/components/layouts/DefaultLayout/DefaultLayout';
import SearchPage from '../../app/components/templates/SearchPage';
import ExploreEventsPage from '../../app/components/templates/ExploreEventsPage';
import HomeLayout from '../../app/components/layouts/HomeLayout/HomeLayout';

const Events = () => <ExploreEventsPage />;

Events.getLayout = (page) => HomeLayout(page);

export default Events;
