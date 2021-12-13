import React, { useRef, useState } from 'react';
import Link from 'next/link';
import { FaLink, FaShare } from 'react-icons/fa';
import { BsFacebook, BsTelegram, BsTwitter, BsWhatsapp } from 'react-icons/bs';
import useOutsideAlerter from '../../hooks/useOutsideAlerter';

const NavbarItem = ({ children, href, danger, ...props }) => {
  return (
    <Link href={href}>
      <a
        className={`pl-6 pr-8 py-1.5 block hover:bg-gray-50 ${
          danger ? 'active:bg-red-500' : 'active:bg-yellow-400'
        } active:text-white`}
        {...props}
      >
        {children}
      </a>
    </Link>
  );
};

const ShareEventDropdown = () => {
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
      <button
        type="button"
        className="bg-white flex gap-1 items-center shadow py-2 px-4 rounded text-sm "
      >
        <FaShare size="1rem" /> Share
      </button>
      {menuOpened && (
        <div className="absolute bg-white shadow py-2 mt-2 right-0 left-auto whitespace-nowrap rounded text-sm z-10">
          <div
            className="absolute -top-1.5 right-6 w-0 h-0"
            style={{
              borderLeft: '7px solid transparent',
              borderRight: '7px solid transparent',
              borderBottom: '7px solid white',
            }}
          />
          <ul>
            <NavbarItem href="#" onClick={() => {}}>
              <span className={'flex gap-2 items-center'}>
                <FaLink size="1rem" />
                Copy Link
              </span>
            </NavbarItem>
            <NavbarItem href={`#`}>
              <span className={'flex gap-2 items-center'}>
                <BsFacebook size="1rem" />
                Facebook
              </span>
            </NavbarItem>
            <NavbarItem href={`#`}>
              <span className={'flex gap-2 items-center'}>
                <BsTwitter size="1rem" />
                Twitter
              </span>
            </NavbarItem>
            <NavbarItem href={`#`}>
              <span className={'flex gap-2 items-center'}>
                <BsWhatsapp size="1rem" />
                Whatsapp
              </span>
            </NavbarItem>
            <NavbarItem href={`#`}>
              <span className={'flex gap-2 items-center'}>
                <BsTelegram size="1rem" />
                Telegram
              </span>
            </NavbarItem>
          </ul>
        </div>
      )}
    </div>
  );
};

export default ShareEventDropdown;
