import React from 'react';
import useActiveEvent from "../../../hooks/useActiveEvent";

const DisplayEventPage = () => {
    const {event} = useActiveEvent();
    return (
        <div>
            <img src={event.imagePath} alt={event.name} className="lg:w-4/5"/>
            <div className="ck-content" dangerouslySetInnerHTML={{__html: event.description}}/>
        </div>
    );
};

export default DisplayEventPage;