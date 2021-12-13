import React, { useRef, useState } from 'react';
import Container from '../Container';
import Link from 'next/link';
import { useRouter } from 'next/router';
import AxiosInstance from '../../api/AxiosInstance';
import moment from 'moment-timezone';
import useAuth from '../../hooks/useAuth';
import useOutsideAlerter from '../../hooks/useOutsideAlerter';
import Image from 'next/image';

const EventSearchResult = ({ event }) => {
  const { user } = useAuth();
  const timezone = user?.profile?.timezone ?? moment.tz.guess();
  return (
    <div className="w-full flex flex-col gap-3 mb-6">
      <div className="font-semibold text-sm">
        {moment(event.startDate)
          .tz(event.timezone)
          .tz(timezone)
          .format('MMM DD, dddd')}
      </div>
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
                .format('MMM DD, H:m A')}
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
              <Image
                className="rounded"
                src={event.imagePath}
                alt="event"
                width={16 * 10}
                height={9 * 10}
              />
            </a>
          </Link>
        </div>
      </div>
    </div>
  );
};

const UserSearchResult = ({ user }) => {
  return (
    <div className="col-span-6 lg:col-span-3 flex justify-center bg-gray-50 rounded p-10">
      <Link href={`/@${user.username}`}>
        <a className="text-xl font-semibold hover:underline">
          <div className="flex flex-col items-center">
            <Image
              src="https://i.pravatar.cc/300"
              alt="avatar"
              className="rounded-full"
              width="75px"
              height="75px"
            />
            <div className="mt-2 text-xl font-semibold text-center">
              {user.profile.name}
            </div>
          </div>
        </a>
      </Link>
    </div>
  );
};

const SelectSearchCategory = ({ searchData, setSearchData }) => {
  const [menuOpened, setMenuOpened] = useState(false);
  const toggleMenu = () => setMenuOpened((open) => !open);
  const closeMenu = () => setMenuOpened(false);

  const setToEvent = () => {
    if (searchData.category !== 'events') {
      setSearchData({
        category: 'events',
        results: [],
      });
    }
  };

  const setToUser = () => {
    if (searchData.category !== 'users') {
      setSearchData({
        category: 'users',
        results: [],
      });
    }
  };

  const wrapperRef = useRef(null);
  useOutsideAlerter(wrapperRef, closeMenu);

  const NavbarItem = ({ children, ...props }) => {
    return (
      <a
        className={`pl-4 pr-6 text-black font-normal text-lg py-1.5 block hover:bg-gray-50 active:bg-yellow-400 active:text-white`}
        {...props}
      >
        {children}
      </a>
    );
  };

  return (
    <div
      className="relative cursor-pointer select-none"
      ref={wrapperRef}
      onClick={toggleMenu}
    >
      <span className="text-black font-normal border-b border-yellow-500">
        {searchData.category}
      </span>
      {menuOpened && (
        <div className="absolute bg-white shadow py-2 mt-2 left-0 right-auto whitespace-nowrap rounded text-sm z-10">
          <ul>
            <NavbarItem onClick={setToEvent}>
              <span className={'flex gap-2 items-center'}>events</span>
            </NavbarItem>
            <NavbarItem onClick={setToUser}>
              <span className={'flex gap-2 items-center'}>users</span>
            </NavbarItem>
          </ul>
        </div>
      )}
    </div>
  );
};

const SearchPage = () => {
  const { query } = useRouter();
  const [searchData, setSearchData] = React.useState({
    category: 'events',
    results: [],
  });

  React.useEffect(() => {
    if (query.query) {
      AxiosInstance.post(`/search/${searchData.category}`, {
        query: query.query,
        hitsPerPage: 16,
      }).then((res) => {
        setSearchData((prev) => ({
          ...prev,
          results: res.data.data[prev.category],
        }));
      });
    }
  }, [query.query, searchData.category]);

  return (
    <Container>
      <div className="bg-gray-900 text-white rounded-lg pt-24 pb-6 text-center text-xl font-bold ">
        Search results for: {query.query}
      </div>
      <div className="flex gap-1 text-gray-400 text-2xl font-thin py-8 px-2">
        I'm looking for
        <SelectSearchCategory
          searchData={searchData}
          setSearchData={setSearchData}
        />
      </div>
      {searchData.category === 'events' &&
        (searchData.results.length > 0 ? (
          searchData.results.map((e) => (
            <EventSearchResult event={e} key={e.id} />
          ))
        ) : (
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
        ))}
      {searchData.category === 'users' &&
        (searchData.results.length > 0 ? (
          <div className="w-full grid gap-6 grid-cols-12">
            {searchData.results.map((e) => (
              <UserSearchResult user={e} key={e.id} />
            ))}
          </div>
        ) : (
          <div className="h-96 bg-gray-100 flex flex-col justify-center items-center">
            <Image
              src="/no-item-found.png"
              alt="no user found"
              width="150px"
              height="100%"
            />
            <div className="text-gray-500 text-sm mt-8">
              We couldn't find any users for this filters
            </div>
          </div>
        ))}
    </Container>
  );
};

export default SearchPage;
