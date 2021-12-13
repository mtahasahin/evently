import React from 'react';
import { CgProfile } from 'react-icons/cg';
import { IoMdKey } from 'react-icons/io';
import { FaTrashAlt } from 'react-icons/fa';
import { useRouter } from 'next/router';
import Link from 'next/link';

const EditPageNavbarItem = ({ Icon, text, link }) => {
  const router = useRouter();
  const selected = router.pathname === link;
  let classes =
    'flex w-full px-4 py-2 rounded hover:bg-gray-50 transition space-x-4';
  if (selected) classes += ' bg-gray-50';
  return (
    <Link href={link}>
      <a className={classes}>
        <Icon size="1.5rem" color="gray" />
        <span>{text}</span>
      </a>
    </Link>
  );
};

const EditPageNavbar = () => {
  return (
    <nav className="flex flex-col space-y-2">
      <EditPageNavbarItem
        link="/edit/profile"
        text={'Basic Information'}
        Icon={CgProfile}
      />
      <EditPageNavbarItem
        link="/edit/password"
        text={'Change Password'}
        Icon={IoMdKey}
      />
      <EditPageNavbarItem
        link="/edit/close-account"
        text={'Close Account'}
        Icon={FaTrashAlt}
      />
    </nav>
  );
};

export default EditPageNavbar;
