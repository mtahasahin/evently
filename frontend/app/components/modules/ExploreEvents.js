import React, { useEffect, useState } from 'react';
import useAuth from '../../hooks/useAuth';
import moment from 'moment-timezone';
import Link from 'next/link';
import Image from 'next/image';
import AxiosInstance from '../../api/AxiosInstance';

const EventSearchResult = ({ event }) => {
  const { user } = useAuth();
  const timezone = user?.profile?.timezone ?? moment.tz.guess();
  return (
    <div className="col-span-12 sm:col-span-6 lg:col-span-3 flex flex-col gap-3 mb-6">
      <div className="bg-gray-50 rounded border p-6 flex flex-col justify-between">
        <div className="flex">
          <Link href={`/event/${event.slug}`}>
            <a>
              {event.imagePath && (
                <Image
                  className="rounded"
                  src={event.imagePath}
                  alt="event"
                  width={16 * 20}
                  height={9 * 20}
                />
              )}
            </a>
          </Link>
        </div>
        <div className="flex flex-col gap-2">
          <Link href={`/event/${event.slug}`}>
            <a className="text-lg font-semibold line-clamp-1 hover:underline">
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
      </div>
    </div>
  );
};

const ExploreEvents = () => {
  const [events, setEvents] = useState([]);
  useEffect(() => {
    AxiosInstance.post('/search/events/getRandomEvents').then((res) => {
      setEvents(res.data.data.events.slice(0, 4));
    });
  }, []);
  return (
    <div className="w-full flex justify-center my-6">
      <div className="w-4/5 flex flex-col gap-2">
        <span className="text-2xl">Upcoming Events</span>
        <div className="grid grid-cols-12 gap-2 flex-wrap">
          {events.map((event) => (
            <EventSearchResult event={event} key={event.id} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default ExploreEvents;
