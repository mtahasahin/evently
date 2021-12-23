import React from 'react';
import ExploreEventsPage from '../app/components/templates/ExploreEventsPage';
import DefaultLayout from '../app/components/layouts/DefaultLayout/DefaultLayout';
import Container from '../app/components/Container';

const Events = function () {
  return (
    <Container>
      <ExploreEventsPage />
    </Container>
  );
};

Events.getLayout = (page) => DefaultLayout(page);

export default Events;
