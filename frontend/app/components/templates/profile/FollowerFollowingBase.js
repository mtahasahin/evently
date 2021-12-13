import React, {useEffect, useState} from 'react';
import ProfileSummary from "../../modules/ProfileSummary";
import NotFound from "../../../../pages/404";
import useActiveProfile from "../../../hooks/useActiveProfile";
import Container from "../../Container";
import Link from "next/link";
import AxiosInstance from "../../../api/AxiosInstance";
import {useRouter} from "next/router";
import Button from "../../elements/Button/Button";

const PrivateAccount = () => {
    const {username} = useActiveProfile();
    return (
        <div className="flex flex-col container mx-auto w-full justify-center items-center gap-y-2 py-16">
            <b className="font-semibold text-gray-700">This account is private.</b>
            <p className="text-sm text-gray-500">Only approved followers can see @{username}'s profile.</p>
        </div>
    )
}

const User = ({user, type}) => {
    const [showButtons, setShowButtons] = useState(type === "follower-requests");
    const acceptRequest = async () => {
        AxiosInstance.put(`/profile/follower-request/${user.username}`).then(res => {
            setShowButtons(false);
        });

    }

    const removeRequest = async () => {
        AxiosInstance.delete(`/profile/follower-request/${user.username}`).then(res => {
            setShowButtons(false);
        });
    }

    return (
        <div
            className="flex flex-col items-center col-span-12 sm:col-span-6 lg:col-span-3  flex justify-center bg-white shadow rounded p-10">
            <Link href={`/@${user.username}`}>
                <a>
                    <div className="flex flex-col items-center">
                        <img src={`https://i.pravatar.cc/300?${user.id}`} alt="avatar" className="rounded-full mb-2"
                             width="75px"/>
                        <div className="text-xl hover:underline font-semibold text-center">{user.name}</div>
                        <div className="text-sm text-gray-400 text-center">@{user.username}</div>
                    </div>
                </a>
            </Link>
            {showButtons &&
                <div className="flex mt-2 gap-1">
                    <Button appearance="primary" onClick={acceptRequest}>Accept</Button>
                    <Button appearance="danger" onClick={removeRequest}>Remove</Button>
                </div>
            }
        </div>
    );
}

const ShowMoreButton = ({onClick}) => {
    return (<div onClick={onClick}
                 className={`py-2 w-1/2 rounded text-gray-500 hover:bg-gray-200 text-center border border-gray-300 select-none cursor-pointer`}>Show
        more users</div>)
}

const FollowerFollowingBase = ({type}) => {
    const {profile, isLoading, isError} = useActiveProfile();
    const [page, setPage] = useState(0);
    const [result, setResult] = useState({content: [], totalElements: 0});
    const {query} = useRouter();

    const hasMore = result.content.length < result.totalElements;

    useEffect(() => {
        AxiosInstance.get(`/profile/${query.username.substring(1)}/${type}`, {
            params: {page: page}
        }).then(res => {
            setResult(prev => {
                return {
                    content: [...prev.content, ...res.data.data.content],
                    totalElements: res.data.data.totalElements
                }
            })
        });
    }, [query.username, page]);

    return (
        isError ? <NotFound/> :
            <>
                <ProfileSummary type={type}/>
                {!isLoading ? ((profile.profilePublic || profile.following || profile.canEdit) ?
                    <Container>
                        <div className="flex flex-col gap-1 justify-center items-center bg-gray-100 my-6 p-6">
                            <div className="w-full grid gap-6 grid-cols-12 my-6">
                                {result.content.map(e => <User user={e} type={type} key={e.id}/>)}
                            </div>
                            {result.content.length === 0 &&
                                <div className="text-center text-gray-500">There isn't anyone in here</div>}
                            {hasMore && <ShowMoreButton onClick={() => setPage(prev => prev + 1)} loading={false}/>}
                        </div>
                    </Container>
                    : <PrivateAccount/>) : null
                }
            </>
    );
};

export default FollowerFollowingBase;