import React, { useEffect, useState } from 'react';
import Image from 'next/image';
import useAuth from '../../hooks/useAuth';
import moment from 'moment-timezone';
import Link from 'next/link';
import AxiosInstance from '../../api/AxiosInstance';

const Event = ({ event }) => {
  const { user } = useAuth();
  const timezone = user?.profile?.timezone ?? moment.tz.guess();
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
                <a className="hover:underline">{event.organizer.name}</a>
              </Link>
            </div>
          </div>
          <div className="text-sm text-gray-600 font-light">
            {event.attendeeCount} people{' '}
            {event.eventEnded ? 'went' : 'are going'} to the event
          </div>
        </div>
      </div>
    </div>
  );
};

const Activity = function ({ activity }) {
  const { user } = useAuth();
  const timezone = user?.profile?.timezone ?? moment.tz.guess();
  const link =
    activity.activity_type === 'FOLLOWED_USER'
      ? `/@${activity.following_user.username}`
      : `/event/${activity.event.slug}`;

  const displayName =
    activity.activity_type === 'FOLLOWED_USER'
      ? activity.following_user.name
      : activity.event.name;

  return (
    <div className="flex py-4 gap-3">
      <div className="h-[80px]">
        <Image
          src="https://i.pravatar.cc/300"
          height="50"
          width="50"
          className="rounded-full"
        />
      </div>
      <div className="flex flex-col w-full">
        <div className="h-[56px] text-sm text-gray-500 pt-1">
          <div>
            <Link href={`/@${activity.user.username}`}>
              <a className="font-semibold text-black hover:underline">
                {activity.user.name}
              </a>
            </Link>
            <span className="ml-1">{activity.status_text}</span>
            <Link href={link}>
              <a className="font-semibold text-black ml-1 hover:underline">
                {displayName}
              </a>
            </Link>
          </div>
          <div>
            {moment(activity.created_at).tz('UTC', true).tz(timezone).fromNow()}{' '}
            - {activity.user.username}
          </div>
        </div>
        <div>
          {activity.activity_type === 'FOLLOWED_USER' ? null : (
            <Event event={activity.event} />
          )}
        </div>
      </div>
    </div>
  );
};

const ActivityFeedPage = () => {
  const [activities, setActivities] = useState([]);

  useEffect(() => {
    AxiosInstance.get('/activity')
      .then((res) => {
        setActivities(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <div className="px-4">
      <h1 className="text-2xl mb-2">Activity Feed</h1>
      <div className="w-full divide-y">
        {activities.map((activity) => (
          <Activity activity={activity} key={activity.id} />
        ))}
      </div>
    </div>
  );
};

export default ActivityFeedPage;
