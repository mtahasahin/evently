import React from 'react';
import Header from '../../modules/Header/Header';

const DefaultLayout = (page) => {
  return (
    <div className="min-h-screen">
      <Header />
      {page}
    </div>
  );
};

export default DefaultLayout;
