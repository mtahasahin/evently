import React from 'react';
import ChangePasswordPage from '../../app/components/templates/edit/ChangePasswordPage';
import EditPageLayout from '../../app/components/layouts/EditPageLayout/EditPageLayout';

const Password = function () {
  return <ChangePasswordPage />;
};

Password.getLayout = (page) => EditPageLayout(page);

export default Password;
