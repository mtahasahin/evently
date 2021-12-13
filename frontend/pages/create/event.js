import React from 'react';
import CreateEditEventPage from '../../app/components/templates/CreateEditEventPage';
import CreateEditEventLayout from '../../app/components/layouts/CreateEditEventLayout/CreateEditEventLayout';

const Event = function () {
  return <CreateEditEventPage edit={false} />;
};

Event.getLayout = (page) => CreateEditEventLayout(page);

export default Event;
