import React from 'react';
import Header from '../../modules/Header/Header';
import Container from '../../Container';
import HomePageNavbar from '../../modules/HomePageNavbar';

const HomeLayout = (page) => {
  return (
    <div className="flex flex-col min-h-screen">
      <Header />
      <Container>
        <div className="grid grid-cols-12 gap-2 md:flex-row justify-between mt-8">
          <div className="md:col-span-2 col-span-12">
            <HomePageNavbar />
          </div>
          <div className="md:col-span-7 col-span-12"> {page} </div>
        </div>
      </Container>
    </div>
  );
};

export default HomeLayout;
