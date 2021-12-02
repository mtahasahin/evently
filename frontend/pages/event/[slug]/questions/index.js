import EventQuestionsLayout from "../../../../app/components/layouts/EventQuestionsLayout/EventQuestionsLayout";
import DisplayEventQuestionsPage from "../../../../app/components/templates/event/questions/DisplayEventQuestionsPage";

const Index = () => (
    <DisplayEventQuestionsPage/>
);


Index.getLayout = page => EventQuestionsLayout(page);

export default Index;