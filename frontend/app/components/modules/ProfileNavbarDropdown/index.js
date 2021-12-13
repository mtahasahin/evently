import React, { useRef, useState } from 'react';
import { FaUserCircle } from 'react-icons/fa';
import Link from 'next/link';
import useAuth from '../../../hooks/useAuth';
import useOutsideAlerter from '../../../hooks/useOutsideAlerter';

const NavbarItem = ({ children, href, ...props }) => {
  return (
    <Link href={href}>
      <a
        className="px-4 py-1.5 block hover:bg-gray-50 active:bg-yellow-400 active:text-white"
        {...props}
      >
        {children}
      </a>
    </Link>
  );
};

const NavbarDropdown = () => {
  const { user, logout } = useAuth();
  const [menuOpened, setMenuOpened] = useState(false);
  const toggleMenu = () => setMenuOpened((open) => !open);
  const closeMenu = () => setMenuOpened(false);
  const wrapperRef = useRef(null);
  useOutsideAlerter(wrapperRef, closeMenu);

  return (
    <div
      className="relative cursor-pointer select-none"
      ref={wrapperRef}
      onClick={toggleMenu}
    >
      <FaUserCircle size="2rem" className="h-full" />
      {menuOpened && (
        <div className="absolute bg-white shadow py-2 mt-4 right-0 left-auto whitespace-nowrap rounded text-sm z-10">
          <ul>
            <NavbarItem href="/profile">Profile</NavbarItem>
            <NavbarItem href={`/@${user.username}/followers/requests`}>
              Follower Requests
            </NavbarItem>
            <NavbarItem href="/edit/profile">Settings</NavbarItem>
            <NavbarItem href="" onClick={logout}>
              Logout
            </NavbarItem>
          </ul>
        </div>
      )}
    </div>
  );
};

export default NavbarDropdown;
