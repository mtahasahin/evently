import React from 'react';

const Container = ({ children }) => {
  return (
    <div className="w-full max-w-screen-xl xl:w-4/5 mx-auto px-5">
      {children}
    </div>
  );
};

export default Container;
