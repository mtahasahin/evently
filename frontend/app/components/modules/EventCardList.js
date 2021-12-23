import moment from 'moment-timezone';
import Link from 'next/link';
import Image from 'next/image';
import React from 'react';

function groupBy(list, keyGetter) {
  const map = new Map();
  list.forEach((item) => {
    const key = keyGetter(item);
    const collection = map.get(key);
    if (!collection) {
      map.set(key, [item]);
    } else {
      collection.push(item);
    }
  });
  return map;
}

const EventCard = ({ event, timezone }) => {
  return (
    <div className="w-full flex flex-col gap-3 mb-6">
      <div className="bg-gray-50 rounded border p-6 flex justify-between">
        <div className="flex flex-col gap-2">
          <Link href={`/event/${event.slug}`}>
            <a className="text-xl font-semibold hover:underline">
              {event.name}
            </a>
          </Link>
          <div className="text-sm flex">
            <div>
              {moment(event.startDate)
                .tz(event.timezone)
                .tz(timezone)
                .format('MMM DD, h:mm A')}
            </div>
            <div className="ml-6">
              <span className="text-gray-400">From</span>{' '}
              <Link href={`/@${event.organizer.username}`}>
                <a className="hover:underline">
                  {event.organizer.profile.name}
                </a>
              </Link>
            </div>
          </div>
          <div className="text-sm text-gray-600 font-light">
            {event.attendeeCount} members{' '}
            {event.eventEnded ? 'went' : 'are joining'} to the event
          </div>
        </div>
        <div className="hidden md:flex">
          <Link href={`/event/${event.slug}`}>
            <a>
              {event.imagePath && (
                <Image
                  className="rounded"
                  src={event.imagePath}
                  alt="event"
                  width={16 * 10}
                  height={9 * 10}
                />
              )}
            </a>
          </Link>
        </div>
      </div>
    </div>
  );
};

const NoEventFound = () => {
  return (
    <div className="h-96 bg-gray-100 flex flex-col justify-center items-center">
      <Image
        src="/no-item-found.png"
        alt="no event found"
        width="150px"
        height="100%"
      />
      <div className="text-gray-500 text-sm mt-8">
        We couldn't find any events for this filters
      </div>
    </div>
  );
};

const EventCardList = ({ events, timezone }) => {
  if (events.length === 0) {
    return <NoEventFound />;
  }

  const getStartDay = (event) => {
    return moment(event.startDate)
      .tz(event.timezone)
      .tz(timezone)
      .startOf('day')
      .format('MMM DD, dddd');
  };

  const groupedEvents = groupBy(events, getStartDay);

  return Array.from(groupedEvents).map(([date, events]) => (
    <div key={date}>
      <div className="font-semibold mb-1">{date}</div>
      <div className="flex flex-col gap-2">
        {events.map((event) => (
          <EventCard key={event.id} event={event} timezone={timezone} />
        ))}
      </div>
    </div>
  ));
};

export default EventCardList;
