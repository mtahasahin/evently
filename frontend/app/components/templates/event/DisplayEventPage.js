import React from 'react';
import useActiveEvent from '../../../hooks/useActiveEvent';
import Image from 'next/image';

const DisplayEventPage = () => {
  const { event } = useActiveEvent();
  return (
    <div>
      <div className="lg:w-4/5">
        <Image
          src={event.imagePath}
          alt="Event image"
          width="100%"
          height="55%"
          layout="responsive"
          objectFit="contain"
        />
      </div>
      <div
        className="ck-content"
        dangerouslySetInnerHTML={{
          __html: event.description,
        }}
      />
    </div>
  );
};

export default DisplayEventPage;
