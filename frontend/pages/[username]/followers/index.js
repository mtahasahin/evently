import React from 'react';
import DefaultLayout from "../../../app/components/layouts/DefaultLayout/DefaultLayout";
import FollowerFollowingBase from "../../../app/components/templates/profile/FollowerFollowingBase";

const Index = () => (
    <FollowerFollowingBase type="followers"/>
)

Index.getLayout = page => DefaultLayout(page)

export default Index;