import React from 'react';
import EditPageLayout from "../../app/components/layouts/EditPageLayout/EditPageLayout";
import EditProfilePage from "../../app/components/templates/edit/EditProfilePage";


const Profile = () => (
    <EditProfilePage/>
)

Profile.getLayout = page => EditPageLayout(page);


export default Profile;