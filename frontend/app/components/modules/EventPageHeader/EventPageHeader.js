import React from 'react';
import Container from "../../Container";
import moment from "moment";
import useActiveEvent from "../../../hooks/useActiveEvent";
import Link from "next/link";
import EventSettingsDropdown from "../EventSettingsDropdown";
import ShareEventDropdown from "../ShareEventDropdown";

const CalendarMenu = ({date}) => {

    return (<div className="relative h-[72px] w-[64px] min-w-[64px] bg-white rounded border select-none cursor-pointer">
        <div className="absolute top-0 left-0 right-0 bg-red-500 h-8 rounded-t flex justify-center items-center">
            <span className="text-white">{moment(date).format("MMM")}</span>
        </div>
        <div className="absolute bottom-0 left-0 right-0 h-8 flex justify-center items-center">
            <span className="text-black font-bold">{moment(date).format("DD")}</span>
        </div>

    </div>)
}

const EventTitle = ({title, slug}) => {
    return (<Link href={`/event/${slug}`}><a className="text-3xl font-semibold hover:underline">{title}</a></Link>)
}

const EventPageHeader = ({small}) => {
    const {event} = useActiveEvent();
    return (
        <div className={`py-[30px] bg-gray-100 mb-3`}>
            <Container>
                <div className={`flex flex-nowrap justify-between items-center ${small ? "lg:w-2/3 lg:pr-4" : ""}`}>
                    <div className="flex items-center gap-5">
                        <CalendarMenu date={event.startDate}/>
                        <EventTitle title={event.name} slug={event.slug}/>
                    </div>
                    <div className="flex items-center gap-5">
                        <ShareEventDropdown/>
                        {event.organizing && <EventSettingsDropdown/>}
                    </div>
                </div>
            </Container>
        </div>
    );
};

export default EventPageHeader;