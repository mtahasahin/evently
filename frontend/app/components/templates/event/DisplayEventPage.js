import React from 'react';
import useActiveEvent from '../../../hooks/useActiveEvent';
import Image from 'next/image';

const DisplayEventPage = () => {
  const { event } = useActiveEvent();
  return (
    <div>
      <div className="lg:w-4/5">
        {event.imagePath && (
          <Image
            src={event.imagePath}
            alt="Event image"
            width="100%"
            height="55%"
            layout="responsive"
            objectFit="contain"
          />
        )}
      </div>
      <div className="text-2xl mt-2">Description</div>
      <div
        className="ck-content mt-4 lg:w-4/5"
        dangerouslySetInnerHTML={{
          __html: event.description,
        }}
      />
    </div>
  );
};

export default DisplayEventPage;
