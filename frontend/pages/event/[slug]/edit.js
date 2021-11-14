import React from 'react';
import CreateEditEventPage from "../../../app/components/templates/CreateEditEventPage";
import {useRouter} from "next/router";
import CreateEditEventLayout from "../../../app/components/layouts/CreateEditEventLayout/CreateEditEventLayout";
import useAuth from "../../../app/hooks/useAuth";

const Edit = () => {
    const {authenticated} = useAuth();
    const router = useRouter();
    const {slug} = router.query;
    if (!router.isReady || !authenticated) {
        return null
    }
    return (
        <CreateEditEventPage edit={true} slug={slug}/>
    )
};

Edit.getLayout = page => CreateEditEventLayout(page);

export default Edit;

