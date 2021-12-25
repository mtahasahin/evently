import React, { useEffect, useState } from 'react';
import AxiosInstance from '../../../api/AxiosInstance';
import Link from 'next/link';
import Image from 'next/image';
import Button from '../../elements/Button/Button';
import useActiveEvent from '../../../hooks/useActiveEvent';

const User = ({ user }) => {
  return (
    <div className="flex flex-col items-center col-span-6 sm:col-span-4 lg:col-span-2 flex justify-center bg-white shadow rounded py-3 px-5">
      <Link href={`/@${user.username}`}>
        <a>
          <div className="flex flex-col items-center">
            <div className="mb-2">
              <Image
                src={user.avatar}
                alt="avatar"
                height="75px"
                width="75px"
                className="rounded-full"
                layout="fixed"
              />
            </div>
            <div className="hover:underline font-semibold text-center">
              {user.name}
            </div>
          </div>
        </a>
      </Link>
    </div>
  );
};

const EventAttendeesPage = () => {
  const { event, slug, key } = useActiveEvent();
  const [page, setPage] = useState(0);
  const [users, setUsers] = useState([]);
  useEffect(() => {
    AxiosInstance.get(`/event/${slug}/attendees`, {
      params: { key: key, page: page, limit: 12 },
    })
      .then((res) => {
        console.log(res.data.data);
        setUsers((prev) => [...prev, ...res.data.data]);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [page]);

  return (
    <div className="grid grid-cols-12 gap-5 bg-gray-100 rounded p-4">
      {users.map((user) => (
        <User user={user} key={user.id} />
      ))}
      {users.length < event.attendeeCount && (
        <div className="flex flex-col items-center col-span-12">
          <Button
            onClick={() => {
              setPage(page + 1);
            }}
          >
            Load More
          </Button>
        </div>
      )}
    </div>
  );
};

export default EventAttendeesPage;
