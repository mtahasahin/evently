import DisplayEventLayout from "../../../app/components/layouts/DisplayEventLayout/DisplayEventLayout";
import DisplayEventPage from "../../../app/components/templates/event/DisplayEventPage";

const Index = () => (
    <DisplayEventPage/>
);

Index.getLayout = page => DisplayEventLayout(page);

export default Index;

