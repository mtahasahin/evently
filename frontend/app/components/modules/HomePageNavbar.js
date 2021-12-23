import React from 'react';
import { useRouter } from 'next/router';
import Link from 'next/link';
import { HiLightningBolt } from 'react-icons/hi';
import { BiWorld } from 'react-icons/bi';
import { MdEvent } from 'react-icons/md';

const HomePageNavbarItem = ({ Icon, text, link }) => {
  const router = useRouter();
  const selected = router.pathname === link;
  let classes =
    'flex w-full pl-3 pr-5 py-3 items-center rounded-lg hover:bg-gray-50 transition space-x-2';
  if (selected) classes += ' bg-gray-100';
  return (
    <Link href={link}>
      <a className={classes}>
        <Icon size="1.5rem" color={selected ? 'orange' : 'gray'} />
        <span className="font-semibold">{text}</span>
      </a>
    </Link>
  );
};

const HomePageNavbar = () => {
  return (
    <nav className="flex flex-col space-y-2">
      <HomePageNavbarItem
        link="/feed"
        text={'Activity Feed'}
        Icon={HiLightningBolt}
      />
      <HomePageNavbarItem
        link="/explore/events"
        text={'Explore'}
        Icon={BiWorld}
      />
      <HomePageNavbarItem link="/my-events" text={'My Events'} Icon={MdEvent} />
    </nav>
  );
};

export default HomePageNavbar;
