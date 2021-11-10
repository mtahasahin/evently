import React from 'react';
import ProfileSummary from "../modules/ProfileSummary";
import ActivityList from "../modules/ActivityList";
import ProfileSocial from "../modules/ProfileSocial";
import NotFound from "../../../pages/404";
import useProfile from "../../hooks/useProfile";

const PrivateAccount = ({username}) => {
    return (
        <div className="flex flex-col container mx-auto w-full justify-center items-center gap-y-2 py-16">
            <b className="font-semibold text-gray-700">This account is private.</b>
            <p className="text-sm text-gray-500">Only approved followers can see @{username}'s profile.</p>
        </div>
    )
}

const ProfilePage = ({username}) => {
    const {profile, isLoading, isError} = useProfile(username);
    return (
        isError ? <NotFound/> :
            <>
                <ProfileSummary username={username}/>
                {!isLoading ? ((profile.profilePublic || profile.following || profile.canEdit) ?
                    <div className={"max-w-screen-xl mx-auto grid grid-cols-3 mt-6"}>
                        <div className="col-span-2">
                            <ActivityList activities={null}/>
                        </div>
                        <div className="col-span-1">
                            <ProfileSocial profile={profile}/>
                        </div>
                    </div>
                    : <PrivateAccount username={username}/>) : null
                }
            </>
    );
};

export default ProfilePage;