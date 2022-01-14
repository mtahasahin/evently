import { AiOutlineMenu } from 'react-icons/ai';
import React, { useState } from 'react';
import Link from 'next/link';
import useAuth from '../../../hooks/useAuth';
import { MdNotifications } from 'react-icons/md';
import NavbarDropdown from '../ProfileNavbarDropdown';
import Button from '../../elements/Button/Button';
import Container from '../../Container';
import SearchBox from '../SearchBox';
import Image from 'next/image';

function NavbarItem({ href, children, ...props }) {
  return (
    <li className="pl-4 py-1">
      <Link href={href}>
        <a className="nav-link" {...props}>
          {children}
        </a>
      </Link>
    </li>
  );
}

function MobileNavbar({ closeMenu }) {
  const { authenticated, user, logout } = useAuth();
  return (
    <div className="flex flex-col lg:hidden fixed right-0 top-0 h-full w-3/4 bg-white z-50 shadow">
      <div className="flex justify-end" onClick={closeMenu}>
        <span className="p-2 select-none cursor-pointer">X</span>
      </div>
      {!authenticated ? (
        <>
          <div className="text-left text-lg">
            <ul>
              <NavbarItem href="/" onClick={closeMenu}>
                Home
              </NavbarItem>
              <NavbarItem
                href={authenticated ? '/explore/events' : '/events'}
                onClick={closeMenu}
              >
                Events
              </NavbarItem>
            </ul>
          </div>
          <div className="absolute bottom-2 left-0 flex flex-col gap-y-2 w-full p-2">
            <Link href="/login">
              <a>
                <Button fullWidth={true} size="xl" appearance="dark">
                  Log In
                </Button>
              </a>
            </Link>
            <Link href="/signup">
              <a>
                <Button fullWidth={true} size="xl" appearance="secondary">
                  Sign up
                </Button>
              </a>
            </Link>
          </div>
        </>
      ) : (
        <>
          <div className="text-left text-lg">
            <ul>
              <NavbarItem href="/profile" onClick={closeMenu}>
                Profile
              </NavbarItem>
              <NavbarItem
                href={`/@${user.username}/followers/requests`}
                onClick={closeMenu}
              >
                Follower Requests
              </NavbarItem>
              <NavbarItem href="/edit/profile" onClick={closeMenu}>
                Settings
              </NavbarItem>
              <NavbarItem href="" onClick={logout}>
                Logout
              </NavbarItem>
            </ul>
          </div>
          <Link href="/create/event">
            <a>
              <div
                className="absolute bottom-2 left-0 flex w-full p-2"
                onClick={closeMenu}
              >
                <Button fullWidth={true} size="xl" appearance="success">
                  Create Event
                </Button>
              </div>
            </a>
          </Link>
        </>
      )}
    </div>
  );
}

function Header() {
  const [showMobileMenu, setShowMobileMenu] = useState(false);
  const { authenticated } = useAuth();

  const toggleMenu = () => {
    setShowMobileMenu((show) => !show);
  };

  return (
    <>
      <div className="bg-white shadow z-50">
        <Container>
          {/*Mobile Navbar*/}
          <div className="lg:hidden h-12 flex w-full justify-end relative">
            <Link href="/">
              <a className="absolute flex items-center top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
                <Image src="/logo.svg" width="170px" height="40px" alt="logo" />
              </a>
            </Link>
            <button className="text-lg" onClick={toggleMenu}>
              <AiOutlineMenu />
            </button>
          </div>
          {/*Desktop Navbar*/}
          <div className="h-16 hidden lg:h-[5rem] items-center lg:justify-between flex-nowrap lg:flex">
            <nav className="gap-x-16 hidden lg:flex items-center">
              <Link href="/">
                <a className="sm:justify-center">
                  <Image
                    src="/logo.svg"
                    width="170px"
                    height="40px"
                    alt="logo"
                  />
                </a>
              </Link>
            </nav>
            <nav className="gap-x-4 hidden sm:flex items-stretch">
              <div className="flex sm:flex gap-x-6 items-center text-sm">
                <Link href="/">
                  <a className="text-gray-500 hover:text-gray-800 whitespace-nowrap">
                    Home
                  </a>
                </Link>
                <Link href={authenticated ? '/explore/events' : '/events'}>
                  <a className="text-gray-500 hover:text-gray-800 whitespace-nowrap">
                    Events
                  </a>
                </Link>
              </div>
              {authenticated && (
                <>
                  <SearchBox />
                  <Link href={'/create/event'}>
                    <a>
                      <Button appearance="success" size="lg">
                        Create Event
                      </Button>
                    </a>
                  </Link>
                </>
              )}
            </nav>
            <nav className="gap-x-4 hidden sm:flex items-stretch">
              {authenticated ? (
                <>
                  <NavbarDropdown />
                  <div className="flex items-stretch cursor-pointer select-none">
                    <MdNotifications
                      color="gray"
                      size="1.5rem"
                      className="h-full"
                    />
                  </div>
                </>
              ) : (
                <>
                  <Link href="/login">
                    <a>
                      <Button appearance="dark" size="xl">
                        Log In
                      </Button>
                    </a>
                  </Link>
                  <Link href="/signup">
                    <a>
                      <Button appearance="primary" size="xl">
                        Sign Up
                      </Button>
                    </a>
                  </Link>
                </>
              )}
            </nav>
          </div>
          {/*Mobile Menu*/}
          {showMobileMenu && (
            <MobileNavbar closeMenu={() => setShowMobileMenu(false)} />
          )}
        </Container>
      </div>
    </>
  );
}

export default Header;
