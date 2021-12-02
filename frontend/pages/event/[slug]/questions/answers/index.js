import DisplayEventApplicationsPage
    from "../../../../../app/components/templates/event/questions/DisplayEventApplicationsPage";
import EventQuestionsLayout from "../../../../../app/components/layouts/EventQuestionsLayout/EventQuestionsLayout";

const Index = () => (
    <DisplayEventApplicationsPage fetchAll={false}/>
);

Index.getLayout = page => EventQuestionsLayout(page);

export default Index;