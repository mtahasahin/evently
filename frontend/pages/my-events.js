import React from 'react';
import MyEventsPage from '../app/components/templates/MyEventsPage';
import HomeLayout from '../app/components/layouts/HomeLayout/HomeLayout';

const Events = function () {
    return (
        <MyEventsPage/>
    );
};

Events.getLayout = (page) => HomeLayout(page);

export default Events;
