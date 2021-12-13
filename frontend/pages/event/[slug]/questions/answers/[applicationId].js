import EventQuestionsLayout from '../../../../../app/components/layouts/EventQuestionsLayout/EventQuestionsLayout';
import DisplayAnswersPage from '../../../../../app/components/templates/event/questions/DisplayAnswersPage';

const DisplayAnswer = () => <DisplayAnswersPage />;

DisplayAnswer.getLayout = (page) => EventQuestionsLayout(page);

export default DisplayAnswer;
