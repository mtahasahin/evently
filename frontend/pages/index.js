import React from 'react';
import DefaultLayout from '../app/components/layouts/DefaultLayout/DefaultLayout';
import HomePage from '../app/components/templates/HomePage';

const Index = function () {
  return <HomePage />;
};

Index.getLayout = (page) => DefaultLayout(page);

export default Index;
