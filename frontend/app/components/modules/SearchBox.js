import React, {useRef} from 'react';
import useOutsideAlerter from "../../hooks/useOutsideAlerter";
import {AiFillCloseCircle, AiOutlineSearch} from "react-icons/ai";
import useSWR from "swr/immutable";
import AxiosInstance from "../../api/AxiosInstance";
import useDebounce from "../../hooks/useDebounce";
import Link from "next/link";
import LoadingIndicator from "../elements/LoadingIndicator/LoadingIndicator";
import Loader from "react-loader-spinner";

const UserSearchResult = ({user, ...props}) => {

    return (
        <Link href={`/@${user.username}`}>
            <a {...props}>
                <div className="flex rounded items-center p-1.5 hover:bg-gray-50">
                    <img src="https://i.pravatar.cc/300?1" alt="avatar" className="w-8 h-8 rounded-full mr-2"/>
                    <span className="text-sm">{user.profile.name}</span>
                </div>
            </a>
        </Link>
    )
}

const EventSearchResult = ({event, ...props}) => {

    return (
        <Link href={`/event/${event.slug}`}>
            <a {...props}>
                <div className="flex rounded items-center p-1.5 hover:bg-gray-50">
                    <img src={event.imagePath} alt="event" className="h-10 w-16 rounded mr-2"/>
                    <span className="text-sm break-normal">{event.name}</span>
                </div>
            </a>
        </Link>
    )
}

const SearchBox = () => {
    const [menuOpened, setMenuOpened] = React.useState(false);
    const openMenu = () => setMenuOpened(true);
    const closeMenu = () => setMenuOpened(false);

    const [query, setQuery] = React.useState('');
    const debouncedQuery = useDebounce(query, 500);

    const wrapperRef = useRef(null);
    useOutsideAlerter(wrapperRef, closeMenu)


    const {
        data,
        error,
    } = useSWR(() => (debouncedQuery && menuOpened ? ['/search', debouncedQuery] : null), (url, query) => AxiosInstance.post(url, {
        query,
        hitsPerPage: 5
    }).then(res => res.data.data));
    const isLoading = !error && !data;
    const showWaiting = query !== debouncedQuery;
    const clearBox = () => setQuery('');

    return (
        <>
            <div className="relative" ref={wrapperRef}>
                <div className="h-full navbar-search-wrapper relative ml-4">
                    <label className="absolute left-3 top-2 bottom-2"><AiOutlineSearch
                        color="rgba(156, 163, 175)" className="h-full w-full"/></label>
                    <input
                        onChange={(e) => setQuery(e.target.value)}
                        onMouseDown={openMenu}
                        value={query}
                        className="h-full py-2 px-9 bg-gray-100 rounded ring-1 text-sm ring-gray-200 outline-none h-full"
                        placeholder="Search on Evently"/>
                    <div className="absolute right-2 -translate-y-1/2 top-1/2">
                        <Loader color="gray" height="20" width="20" type="Oval" visible={showWaiting}/>
                        {(!showWaiting && !!query.length) &&
                            <AiFillCloseCircle color="gray" className="h-full w-full cursor-pointer"
                                               onClick={clearBox}/>}
                    </div>
                </div>
                <div
                    className={`absolute ${(menuOpened && debouncedQuery.length >= 2) ? "flex flex-col" : "hidden"} ml-[-100px] justify-between bg-white shadow mt-2 w-[600px] left-auto rounded z-10`}>

                    {isLoading && <div className="w-full text-center"><LoadingIndicator isLoading={true}/></div>}
                    {!isLoading && <>
                        <div className="flex p-4">
                            <div className="w-1/2">
                                <div className="text-xs text-gray-500 mb-2">USERS</div>
                                {(data && data.users && !!data.users.length) ? data.users.map(user => <UserSearchResult
                                        key={user.id} user={user} onClick={closeMenu}/>)
                                    : <div className="text-sm text-gray-500">No users found</div>}
                            </div>
                            <div className="w-1/2">
                                <div className="text-xs text-gray-500 mb-2">EVENTS</div>
                                {(data && data.events && !!data.events.length) ? data.events.map(event => <EventSearchResult
                                        key={event.id} event={event} onClick={closeMenu}/>)
                                    : <div className="text-sm text-gray-500">No events found</div>}
                            </div>
                        </div>
                        <div className="bg-gray-50 border-t p-3 text-gray-700 text-sm">
                            <Link href={`/search/${debouncedQuery}`}><a onClick={closeMenu}>Show more results for
                                "{debouncedQuery}"</a></Link>
                        </div>

                    </>}
                </div>
            </div>
        </>
    );
};

export default SearchBox;