import React from 'react';
import Header from '../../modules/Header/Header';
import Container from '../../Container';
import EventPageHeader from '../../modules/EventPageHeader/EventPageHeader';
import useActiveEvent from '../../../hooks/useActiveEvent';
import EventSidebar from '../../modules/EventSidebar';
import DisplayEventNavbar from '../../modules/DisplayEventNavbar';

const DisplayEventLayout = (page) => {
  const { isLoading } = useActiveEvent();

  if (isLoading) {
    return null;
  }

  return (
    <div className="flex flex-col min-h-screen">
      <Header />
      <EventPageHeader small />
      <Container>
        <div className="flex flex-col lg:flex-row justify-between">
          <div className="w-full lg:w-2/3 px-3 py-3">
            <DisplayEventNavbar />
            {page}
          </div>
          <div className="w-full lg:w-1/3 px-3 py-3 relative">
            <EventSidebar />
          </div>
        </div>
      </Container>
    </div>
  );
};

export default DisplayEventLayout;
