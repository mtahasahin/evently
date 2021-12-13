import EventApi from '../../api/event.api';
import Button from '../elements/Button/Button';
import { BiUserPlus } from 'react-icons/bi';
import { FaCheck, FaEye, FaUserCircle } from 'react-icons/fa';
import Link from 'next/link';
import moment from 'moment';
import React from 'react';

const ApproveButton = ({ slug, applicationId, callback }) => {
  const confirm = () => {
    EventApi.confirmApplication({
      slug,
      applicationId,
    }).then((res) => {
      callback();
    });
  };

  return (
    <Button
      onClick={confirm}
      className="rounded py-1 px-2 bg-white text-sm text-green-600 border border-green-600 hover:bg-green-600 hover:text-white"
    >
      <div className="flex items-center gap-1">
        <BiUserPlus />
        <span>Approve</span>
      </div>
    </Button>
  );
};

const GoingButton = () => {
  return (
    <Button size="sm" appearance="success">
      <div className="flex items-center gap-1">
        <FaCheck />
        <span>Going</span>
      </div>
    </Button>
  );
};

const AnswersButton = ({ slug, applicationId }) => {
  return (
    <Link href={`/event/${slug}/questions/answers/${applicationId}`}>
      <a className="rounded py-1 px-2 bg-white text-sm border hover:bg-gray-100">
        <div className="flex items-center gap-1">
          <FaEye />
          <span>Answers</span>
        </div>
      </a>
    </Link>
  );
};

const EventApplication = ({
  application,
  slug,
  approveCallback,
  showAnswersPage,
}) => {
  return (
    <div className="w-full border border-opacity-50 shadow-sm flex flex-row justify-between items-center p-2 rounded">
      <Link href={`/@${application.user.username}`}>
        <a className="flex items-center gap-2">
          <FaUserCircle size="1.75rem" />
          <div>
            <span>{application.user.name}</span>{' '}
            <span className="text-sm text-gray-500">
              @{application.user.username}
            </span>
          </div>
        </a>
      </Link>
      <div className="flex items-center space-x-2">
        <span className="text-sm bg-gray-50 px-2 py-0.5 rounded">
          {moment(application.createdAt).format('MMMM DD, YYYY hh:mm')}
        </span>
        {application.has_approved ? (
          <GoingButton />
        ) : (
          <ApproveButton
            applicationId={application.id}
            slug={slug}
            callback={approveCallback}
          />
        )}
        {showAnswersPage && (
          <AnswersButton applicationId={application.id} slug={slug} />
        )}
      </div>
    </div>
  );
};

export default EventApplication;
