import CreateEditEventPage from '../../../app/components/templates/CreateEditEventPage';
import CreateEditEventLayout from '../../../app/components/layouts/CreateEditEventLayout/CreateEditEventLayout';

const Edit = function () {
  return <CreateEditEventPage />;
};

Edit.getLayout = (page) => CreateEditEventLayout(page);

export default Edit;
