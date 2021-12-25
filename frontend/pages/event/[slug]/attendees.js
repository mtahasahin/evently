import DisplayEventLayout from '../../../app/components/layouts/DisplayEventLayout/DisplayEventLayout';
import EventAttendeesPage from '../../../app/components/templates/event/EventAttendeesPage';

const Index = function () {
  return <EventAttendeesPage />;
};

Index.getLayout = (page) => DisplayEventLayout(page);

export default Index;
