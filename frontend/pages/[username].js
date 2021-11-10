import React from 'react';
import {useRouter} from "next/router";
import NotFound from "./404";
import DefaultLayout from "../app/components/layouts/DefaultLayout/DefaultLayout";
import ProfilePage from "../app/components/templates/ProfilePage";

const Username = () => {
    const router = useRouter();
    const {username} = router.query;
    if (!router.isReady) {
        return null
    }
    if (!username?.startsWith("@")) {
        return <NotFound/>
    }

    return (
        <ProfilePage username={username.substring(1, username.length)}/>
    );
};

Username.getLayout = page => DefaultLayout(page)

export default Username;