import React from 'react';
import EditPageLayout from "../../app/components/layouts/EditPageLayout/EditPageLayout";
import CloseAccountPage from "../../app/components/templates/edit/CloseAccountPage";

const CloseAccount = () => (
    <CloseAccountPage/>
);

CloseAccount.getLayout = page => EditPageLayout(page);


export default CloseAccount;