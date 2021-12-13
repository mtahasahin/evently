import React, { useRef, useState } from 'react';
import Link from 'next/link';
import { IoDocumentTextSharp, IoSettings } from 'react-icons/io5';
import { IoIosClose, IoMdSettings } from 'react-icons/io';
import { MdGroups } from 'react-icons/md';
import { FaTrashAlt } from 'react-icons/fa';
import useActiveEvent from '../../hooks/useActiveEvent';
import Button from '../elements/Button/Button';
import Modal from '../elements/Modal/Modal';
import { useForm } from 'react-hook-form';
import EventApi from '../../api/event.api';
import { useRouter } from 'next/router';
import { toast } from 'react-toastify';
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

const RemoveEventModal = ({ slug, showModal, setShowModal }) => {
  const router = useRouter();
  const { handleSubmit } = useForm();
  const onSubmit = () => {
    EventApi.removeEvent(slug).then(() => {
      toast.success('Event removed');
      router.replace('/');
    });
  };
  return (
    <Modal isOpen={showModal} setOpen={setShowModal}>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="p-4 border-b flex justify-between">
          <div className="text-lg font-bold">Remove Event</div>
          <IoIosClose
            size="1.5rem"
            className="cursor-pointer"
            onClick={() => setShowModal(false)}
          />
        </div>
        <div className="p-8 border-b flex flex-col items-center">
          <div className="pb-5 font-semibold">
            Are you sure you want to remove this event?
          </div>
          <div className="pb-1 text-sm">
            Don't forget that remove actions can't be undone.
          </div>
          <div className="pb-5 text-sm">
            All data of this event will be removed.
          </div>
        </div>
        <div className="p-4  flex flex-row justify-end gap-x-2">
          <Button
            type="button"
            onClick={() => setShowModal(false)}
            size="lg"
            appearance="white"
          >
            Cancel
          </Button>
          <Button size="lg" appearance="danger">
            Yes, remove
          </Button>
        </div>
      </form>
    </Modal>
  );
};

const EventSettingsDropdown = () => {
  const { slug } = useActiveEvent();
  const [menuOpened, setMenuOpened] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const toggleMenu = () => setMenuOpened((open) => !open);
  const closeMenu = () => setMenuOpened(false);
  const wrapperRef = useRef(null);
  useOutsideAlerter(wrapperRef, closeMenu);

  return (
    <>
      <RemoveEventModal
        slug={slug}
        showModal={showModal}
        setShowModal={setShowModal}
      />
      <div
        className="relative cursor-pointer select-none"
        ref={wrapperRef}
        onClick={toggleMenu}
      >
        <button type="button" className="bg-white shadow py-3 px-4 rounded">
          <IoSettings size="1rem" />
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
              <NavbarItem href={`/event/${slug}/edit`}>
                <span className={'flex gap-2 items-center'}>
                  <IoMdSettings size="1rem" />
                  Edit event
                </span>
              </NavbarItem>
              <NavbarItem href={`/event/${slug}/questions`}>
                <span className={'flex gap-2 items-center'}>
                  <IoDocumentTextSharp size="1rem" />
                  Manage Registration Form
                </span>
              </NavbarItem>
              <NavbarItem href={`/event/${slug}/attendees`}>
                <span className={'flex gap-2 items-center'}>
                  <MdGroups size="1rem" />
                  Go to attendees
                </span>
              </NavbarItem>
              <NavbarItem href="#" danger onClick={() => setShowModal(true)}>
                <span
                  className={
                    'flex gap-2 items-center text-red-500 active:text-white'
                  }
                >
                  <FaTrashAlt size="1rem" />
                  Remove Event
                </span>{' '}
              </NavbarItem>
            </ul>
          </div>
        )}
      </div>
    </>
  );
};

export default EventSettingsDropdown;
