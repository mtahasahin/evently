import React from 'react';
import Header from '../../modules/Header/Header';
import EventQuestionsNavbar from '../../modules/EventQuestionsNavbar';
import NotFound from '../../../../pages/404';
import useActiveEvent from '../../../hooks/useActiveEvent';
import EventPageHeader from '../../modules/EventPageHeader/EventPageHeader';
import Container from '../../Container';

const EventQuestionsLayout = (page) => {
  const { event, isError, isLoading } = useActiveEvent();

  if (isLoading) {
    return null;
  }

  if (isError) {
    return (
      <div className="flex flex-col gap-12 bg-white min-h-screen">
        <Header />
        <NotFound />
      </div>
    );
  }

  return (
    <div className="flex flex-col bg-white min-h-screen">
      <Header />
      <EventPageHeader />
      <Container>
        <div
          className={`flex flex-col lg:flex-row ${
            !event.organizing ? 'justify-center' : ''
          } gap-4`}
        >
          <div
            className={`w-full lg:w-1/4 ${!event.organizing ? 'hidden' : ''}`}
          >
            <EventQuestionsNavbar />
          </div>
          <div className="w-full lg:w-3/4 px-3 py-3">{page}</div>
        </div>
      </Container>
    </div>
  );
};

export default EventQuestionsLayout;
