import React from 'react';
import useActiveEvent from '../../hooks/useActiveEvent';
import Link from 'next/link';
import { useRouter } from 'next/router';

const NavbarItem = ({ title, link }) => {
  const router = useRouter();
  const selected = router.asPath === link;
  return (
    <Link href={link}>
      <a
        className={`pb-2 ${
          selected ? 'text-black border-b border-yellow-400' : 'text-gray-600'
        }`}
      >
        {title}
      </a>
    </Link>
  );
};

const DisplayEventNavbar = () => {
  const { slug } = useActiveEvent();
  return (
    <nav className="flex gap-8 bg-white border-b mb-5">
      <NavbarItem title="Details" link={`/event/${slug}`} />
      <NavbarItem title="Attendees" link={`/event/${slug}/attendees`} />
    </nav>
  );
};

export default DisplayEventNavbar;
