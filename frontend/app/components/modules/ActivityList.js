import React from 'react';
import Link from 'next/link';
import { AiOutlineUserAdd } from 'react-icons/ai';
import { MdOutlineEvent } from 'react-icons/md';

const Activity = ({ activity }) => {
  const link =
    activity.activity_type === 'FOLLOWED_USER'
      ? `/@${activity.following_user.username}`
      : `/event/${activity.event.slug}`;

  const displayName =
    activity.activity_type === 'FOLLOWED_USER'
      ? activity.following_user.name
      : activity.event.name;

  return (
    <div className="flex gap-2 p-4 bg-white rounded">
      <div>
        {activity.activity_type === 'FOLLOWED_USER' ? (
          <AiOutlineUserAdd color="green" size="1.5rem" />
        ) : (
          <MdOutlineEvent color="orange" size="1.5rem" />
        )}
      </div>
      <div className="flex flex-col gap-2">
        <span className="text-sm text-gray-400">
          <span className="text-black font-semibold">{activity.user.name}</span>{' '}
          {activity.status_text}
        </span>
        <Link href={link}>
          <a className="hover:underline text-sm font-semibold">{displayName}</a>
        </Link>{' '}
      </div>
    </div>
  );
};

const ActivityList = ({ activities }) => {
  return (
    <div className="flex flex-col gap-6 rounded bg-gray-100 p-6">
      {activities.map((e) => (
        <Activity key={e.id} activity={e} />
      ))}
    </div>
  );
};

export default ActivityList;
