import React, { useState } from 'react';
import useActiveEvent from '../../hooks/useActiveEvent';
import moment from 'moment';
import { BsClockFill } from 'react-icons/bs';
import useAuth from '../../hooks/useAuth';
import {
  FaBell,
  FaCheck,
  FaMapMarkerAlt,
  FaQuestionCircle,
} from 'react-icons/fa';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { useForm } from 'react-hook-form';
import EventApi from '../../api/event.api';
import Modal from '../elements/Modal/Modal';
import { IoIosClose } from 'react-icons/io';
import Button from '../elements/Button/Button';
import { toast } from 'react-toastify';

const LiveBadge = () => (
  <span className="bg-red-500 rounded-2xl px-2 py-1 text-white text-xs font-semibold select-none mr-2">
    LIVE
  </span>
);

const EventStatusText = () => {
  const { event } = useActiveEvent();
  const isEventLive = event.eventStarted && !event.eventEnded;
  const fromNow = moment(event.startDate).tz(event.timezone).fromNow();
  return (
    <span className="text-white text-left w-full">
      {isEventLive && (
        <>
          <LiveBadge />
          <span className="text-sm">The event is live!</span>
        </>
      )}
      {!isEventLive && `Event will start ${fromNow}`}
    </span>
  );
};

const EnterEventButton = () => {
  const { event } = useActiveEvent();
  return (
    <a
      href={event.eventUrl}
      className={`${
        event.eventStarted
          ? 'bg-yellow-500 text-white'
          : 'bg-transparent text-yellow-500'
      } border border-yellow-500 w-full text-sm font-semibold py-2 px-4 rounded text-center`}
    >
      Enter Event
    </a>
  );
};

const LiveCard = () => {
  const { event } = useActiveEvent();
  if (event.eventEnded || !event.canSee || event.eventLocationType !== 'ONLINE')
    return null;

  return (
    <>
      <div className="flex py-5 px-6 flex-col gap-4 justify-center items-center bg-gray-900 rounded">
        <EventStatusText />
        <EnterEventButton />
      </div>
    </>
  );
};

const EventTime = () => {
  const { event } = useActiveEvent();
  const { user } = useAuth();
  const startDate = moment(event.startDate).tz(event.timezone);
  const endDate = moment(event.endDate).tz(event.timezone);
  const eventStartEndSameDay = startDate.isSame(endDate, 'day');

  const displayStartDate = user.profile.timezone
    ? startDate.clone().tz(user.profile.timezone).format('dddd, MMMM Do YYYY')
    : startDate.format('dddd, MMMM Do YYYY');
  const displayStartTime = user.profile.timezone
    ? startDate.clone().tz(user.profile.timezone).format('HH:mm')
    : startDate.format('HH:mm');

  const displayEndDate = user.profile.timezone
    ? endDate.clone().tz(user.profile.timezone).format('dddd, MMMM Do YYYY')
    : endDate.format('dddd, MMMM Do YYYY');
  const displayEndTime = user.profile.timezone
    ? endDate.clone().tz(user.profile.timezone).format('HH:mm')
    : endDate.format('HH:mm');

  const timezone = user.profile.timezone
    ? user.profile.timezone
    : event.timezone;

  return eventStartEndSameDay ? (
    <>
      <div className="flex flex-col w-full gap-1 mb-2">
        <span className="mb-1">
          <BsClockFill color="lightgray" />
        </span>
        <span className="text-sm text-gray-200">{displayStartDate}</span>
        <span className="text-sm text-gray-400">
          {displayStartTime} - {displayEndTime}
        </span>
        <div className="text-gray-400 w-full text-xs mb-6">{timezone}</div>
      </div>
    </>
  ) : (
    <>
      <div className="flex flex-col w-full gap-1 mb-2">
        <span className="mb-1">
          <BsClockFill color="lightgray" />
        </span>
        <span className="text-gray-400 text-xs">START DATE</span>
        <span className="text-sm text-gray-200">{displayStartDate}</span>
        <span className="text-sm text-gray-400">{displayStartTime}</span>
      </div>
      <div className="flex flex-col w-full gap-1">
        <span className="text-gray-400 text-xs">END DATE</span>
        <span className="text-sm text-gray-200">{displayEndDate}</span>
        <span className="text-sm text-gray-400">{displayEndTime}</span>
        <div className="text-gray-400 w-full text-xs mb-6">{timezone}</div>
      </div>
    </>
  );
};

const EventLocation = () => {
  const { event } = useActiveEvent();

  return (
    <>
      <div className="flex flex-col w-full gap-1 mb-4">
        <span className="mb-1">
          <FaMapMarkerAlt color="lightgray" />
        </span>
        {event.eventLocationType === 'IN_PERSON' ? (
          <span className="text-gray-200 text-sm">{event.location}</span>
        ) : (
          <>
            <span className="text-white text-sm">Online</span>
            {event.canSee && (
              <Link href={event.eventUrl}>
                <a target="_blank" className="text-sm text-gray-400 underline">
                  Go to Event Address
                </a>
              </Link>
            )}
          </>
        )}
      </div>
    </>
  );
};

const ConfirmRSVPChangeModal = ({ showModal, setShowModal }) => {
  const { event, reload } = useActiveEvent();
  const { handleSubmit } = useForm();
  const onSubmit = () => {
    EventApi.cancelEventApplication({
      slug: event.slug,
    }).then(() => {
      setShowModal(false);
      reload();
    });
  };
  return (
    <Modal isOpen={showModal} setOpen={setShowModal}>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="p-4 border-b flex justify-between">
          <div className="text-lg font-bold">You're losing your spot!</div>
          <IoIosClose
            size="1.5rem"
            className="cursor-pointer"
            onClick={() => setShowModal(false)}
          />
        </div>
        <div className="p-8 border-b flex flex-col items-center">
          <div className="pb-5 font-semibold">
            Are you sure about losing your spot?
          </div>
          <div className="pb-1 text-sm text-center">
            Don't forget that if you change your rsvp status, you may need to
            join event waiting list.
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
            I'm sure
          </Button>
        </div>
      </form>
    </Modal>
  );
};

const EventRSVP = () => {
  const [showModal, setShowModal] = useState(false);
  const { event } = useActiveEvent();

  const isOrganizing = event.organizing;
  const isJoined = event.joined;
  const isWaitingApproval = event.waitingApproval;
  const isEnded = event.eventEnded;

  const RemainingSeatsText = () => (
    <span className="ml-2 text-gray-400 text-sm">
      {!event.limited
        ? 'No attendee limit'
        : `${event.attendeeLimit - event.attendeeCount} seats left`}
    </span>
  );

  return (
    !isEnded && (
      <>
        <ConfirmRSVPChangeModal
          showModal={showModal}
          setShowModal={setShowModal}
        />
        <div className="flex flex-col items-start w-full gap-1 mb-2">
          <FaQuestionCircle color="orange" />
          {isOrganizing && (
            <span className="text-white text-sm text-bold">
              You're organizing!
              <RemainingSeatsText />
            </span>
          )}
          {(isJoined || isWaitingApproval) && (
            <div className="flex w-full justify-between items-center text-white text-sm text-bold">
              <div>
                {isJoined ? (
                  <span className="text-white text-sm text-bold">
                    You're going!
                    <RemainingSeatsText />
                  </span>
                ) : (
                  "You're on the organizer's waitlist"
                )}
              </div>
              <div className="flex items-center gap-2">
                <a
                  className="text-blue-400 hover:underline"
                  href="#"
                  onClick={() => setShowModal(true)}
                >
                  {' '}
                  Change{' '}
                </a>
                {isJoined && (
                  <div className="bg-green-400 rounded-full p-2 max-w-max">
                    <FaCheck />
                  </div>
                )}
                {isWaitingApproval && (
                  <div className="bg-yellow-400 rounded-full p-2 max-w-max">
                    <FaBell />
                  </div>
                )}
              </div>
            </div>
          )}
          {!(isJoined || isWaitingApproval || isOrganizing) && (
            <div className="flex w-full justify-between items-center text-white text-sm text-bold">
              <div className="flex gap-1">
                <div>Are you going?</div>
                <RemainingSeatsText />
              </div>
            </div>
          )}
        </div>
      </>
    )
  );
};

function EventAttendButton() {
  const { event, reload } = useActiveEvent();
  const router = useRouter();
  const isOrganizing = event.organizing;
  const isJoined = event.joined;
  const isWaitingApproval = event.waitingApproval;
  const isEnded = event.eventEnded;
  const isFull = event.userCount >= event.attendeeLimit;

  const attendEvent = () => {
    if (event.questions.length === 0) {
      EventApi.applyToEvent({
        slug: event.slug,
        answers: [],
      }).then((res) => {
        reload(res.data, false);
        toast.success(res.data.message);
      });
    } else {
      router.replace(`/event/${event.slug}/questions`);
    }
  };

  return (
    !(isJoined || isWaitingApproval || isOrganizing || isEnded || isFull) && (
      <button
        className="bg-green-500 w-full py-4 rounded-b text-white text-center -mt-1"
        onClick={attendEvent}
      >
        Attend
      </button>
    )
  );
}

const ViewWaitlistButton = () => {
  const { event } = useActiveEvent();

  return (
    <Link href={`/event/${event.slug}/questions/answers`}>
      <a>
        <Button appearance="secondary" fullWidth size="xl">
          <div className="flex gap-1 text-center justify-center items-center text-sm">
            <FaCheck />
            View Attendee Waitlist
          </div>
        </Button>
      </a>
    </Link>
  );
};

const BlackCard = () => {
  const { event } = useActiveEvent();
  return (
    <div>
      <div className="flex py-5 px-6 flex-col gap-2 justify-center items-center bg-gray-900 rounded">
        <EventTime />
        <EventLocation />
        <EventRSVP />
        {event.organizing && event.approvalRequired && <ViewWaitlistButton />}
      </div>
      <EventAttendButton />
    </div>
  );
};

const CreatedBy = () => {
  const { event } = useActiveEvent();
  return (
    <div className="flex flex-col gap-0.5 p-4 rounded bg-gray-100">
      <div className="text text-gray-400 text-xs">CREATED BY</div>
      <Link href={`/@${event.organizer.username}`}>
        <a className="hover:underline hover:text-blue-600 font-semibold">
          {event.organizer.name}
        </a>
      </Link>
    </div>
  );
};

const EventSidebar = () => {
  return (
    <div className="flex flex-col gap-5 w-full lg:mt-[-117px]">
      <LiveCard />
      <BlackCard />
      <CreatedBy />
    </div>
  );
};

export default EventSidebar;
