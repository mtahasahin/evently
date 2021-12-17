import React from 'react';
import Header from '../../modules/Header/Header';
import Container from '../../Container';
import HomePageNavbar from '../../modules/HomePageNavbar';

const HomeLayout = (page) => {
  return (
    <div className="flex flex-col min-h-screen">
      <Header />
      <Container>
        <div className="grid grid-cols-12 gap-2 lg:flex-row justify-between mt-8">
          <div className="col-span-2">
            <HomePageNavbar />
          </div>
          <div className="col-span-7"> {page} </div>
          <div className="col-span-3">RIGHTMENU</div>
        </div>
      </Container>
    </div>
  );
};

export default HomeLayout;
