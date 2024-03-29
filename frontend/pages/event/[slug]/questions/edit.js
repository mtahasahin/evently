import EventQuestionsLayout from '../../../../app/components/layouts/EventQuestionsLayout/EventQuestionsLayout';
import EditEventQuestionsPage from '../../../../app/components/templates/event/questions/EditEventQuestionsPage';

const Edit = function () {
  return <EditEventQuestionsPage />;
};

Edit.getLayout = (page) => EventQuestionsLayout(page);

export default Edit;
