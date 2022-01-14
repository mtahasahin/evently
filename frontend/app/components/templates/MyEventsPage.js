import React from 'react';
import moment from 'moment-timezone';
import useAuth from '../../hooks/useAuth';
import languages from '../../constants/language-list';
import AxiosInstance from '../../api/AxiosInstance';
import EventCardList from '../modules/EventCardList';

const MyEventsPage = () => {
    const [searchData, setSearchData] = React.useState({
        page: 0,
        past: false,
        results: [],
    });

    const setPast = () => setSearchData(prev => ({ ...prev, past: true }));
    const setUpcoming = () => setSearchData(prev => ({ ...prev, past: false }));

    const { user } = useAuth();
    const timezone = user?.profile?.timezone ?? moment.tz.guess();

    React.useEffect(() => {
        AxiosInstance.get(`/event/my-events`, {params:{page:searchData.page,limit:10,past:searchData.past}}).then((res) => {
            setSearchData((prev) => ({
                ...prev,
                results: res.data.data,
            }));
        });
    }, [searchData.past]);

    return (
        <div className="w-full px-5 my-2">
            <div className="flex justify-end items-baseline gap-1 text-gray-400 font-thin pb-8 px-2">
                <div className="flex gap-1 bg-gray-100 rounded-lg text-sm px-1 py-1">
                    <button className={`${searchData.past?"bg-gray-100":"bg-white font-semibold text-black"} py-2 px-3 rounded-lg`} onClick={setUpcoming}>Upcoming</button>
                    <button className={`${searchData.past?"bg-white font-semibold text-black":"bg-gray-100"} py-2 px-3 rounded-lg`} onClick={setPast}>Past</button>
                </div>
            </div>
            <EventCardList events={searchData.results} timezone={timezone} />
        </div>
    );
};

export default MyEventsPage;
