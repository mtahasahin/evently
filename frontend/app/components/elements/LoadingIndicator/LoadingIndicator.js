import Loader from 'react-loader-spinner';
import React from 'react';

const LoadingIndicator = ({ isLoading }) => (
  <div
    className={`bg-white rounded-t h-full w-full py-10 px-8 flex justify-center ${
      !isLoading ? 'hidden' : ''
    }`}
  >
    <Loader type="Oval" color="gray" height={40} width={40} visible={true} />
  </div>
);

export default LoadingIndicator;
