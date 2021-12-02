import React from 'react';
import useSWRInfinite from "swr/infinite";
import {GET_EVENT_APPLICATIONS_URL} from "../../../../api/urls";
import AxiosInstance from "../../../../api/AxiosInstance";
import EventApplication from "../../../modules/EventApplication";
import clone from 'just-clone';
import useActiveEvent from "../../../../hooks/useActiveEvent";

const PAGE_SIZE = 5;


const ShowMoreButton = ({onClick, loading}) => {
    return (<div onClick={onClick}
                 className={`p-1 rounded w-full text-center border bg-gray-50 ${loading ? "animate-pulse" : ""} select-none cursor-pointer`}>{loading ? "LOADING..." : "LOAD MORE"}</div>)
}

const DisplayEventApplicationsPage = ({fetchAll}) => {
    const {slug} = useActiveEvent();

    const {data, error, mutate, size, setSize, isValidating} = useSWRInfinite(
        index => GET_EVENT_APPLICATIONS_URL(slug, index, fetchAll),
        url => AxiosInstance.get(url).then(res => res.data.data.content),
        {
            revalidateIfStale: false,
            revalidateOnFocus: false,
            revalidateOnReconnect: false,
            revalidateOnMount: true,
        }
    );

    const applications = data ? [].concat(...data) : [];
    const isLoadingInitialData = !data && !error;
    const isLoadingMore = isLoadingInitialData ||
        (size > 0 && data && typeof data[size - 1] === "undefined");
    const isEmpty = data?.[0]?.length === 0;
    const isReachingEnd = isEmpty || (data && data[data.length - 1]?.length < PAGE_SIZE);
    const isRefreshing = isValidating && data && data.length === size;

    const setApplicationApproved = (applicationId, paginationIndex) => {
        const newData = clone(data);
        const item_index = data[paginationIndex].indexOf(data[paginationIndex].find(application => application.id === applicationId));
        newData[paginationIndex][item_index].has_approved = true;
        mutate(newData, false);
    }

    return (
        <div className="flex flex-col gap-2">
            {applications.map((application, index) => (
                <EventApplication key={application.id} application={application} slug={slug} showAnswersPage={true}
                                  approveCallback={() => setApplicationApproved(application.id, Math.floor(index / PAGE_SIZE))}/>
            ))}
            {(!isReachingEnd) && <ShowMoreButton loading={isLoadingMore} onClick={() => setSize(size => size + 1)}/>}
            {isEmpty && <div className="text-center text-gray-500">No applications yet</div>}
        </div>
    );
};

export default DisplayEventApplicationsPage;