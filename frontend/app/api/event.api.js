import AxiosInstance from "./AxiosInstance";
import {CREATE_EVENT_URL, EDIT_EVENT_URL, GET_EVENT_URL} from "./urls";
import moment from "moment-timezone";


const convertEventForm = (parameters) => {
    parameters.startDate = moment(parameters.startDate).toISOString(true);
    parameters.endDate = moment(parameters.endDate).toISOString(true);
    parameters.eventLocationType = parameters.eventLocationType ? "ONLINE" : "IN_PERSON"
    if(parameters.limited === "false") {
        parameters.attendeeLimit = null;
    }
    const formData = new FormData();
    Object.keys(parameters).forEach((key) => {
        if(typeof parameters[key] === "boolean") {
            formData.append(key, String(parameters[key]));
        }
        else if(parameters[key]) {
            formData.append(key, parameters[key]);
        }
    })

    return formData;
}

const createEvent = ({name,startDate,endDate,timezone,image,description,eventLocationType,eventUrl,location,language,limited,attendeeLimit,approvalRequired,visibility}) => {
   const formData = convertEventForm({name,startDate,endDate,timezone,image,description,eventLocationType,eventUrl,location,language,limited,attendeeLimit,approvalRequired,visibility});

    return AxiosInstance
        .post(CREATE_EVENT_URL,formData,{
                headers: {
                    'Content-Type': 'multipart/form-data',
                }
            }
        )
}

const editEvent = ({slug,name,startDate,endDate,timezone,image,description,eventLocationType,eventUrl,location,language,limited,attendeeLimit,approvalRequired,visibility}) => {
    debugger;
    const formData = convertEventForm({name,startDate,endDate,timezone,image,description,eventLocationType,eventUrl,location,language,limited,attendeeLimit,approvalRequired,visibility});

    return AxiosInstance
        .put(EDIT_EVENT_URL(slug),formData,{
                headers: {
                    'Content-Type': 'multipart/form-data',
                }
            }
        )
}

const getEvent = (slug) => {
    return AxiosInstance.get(GET_EVENT_URL(slug));
}


export default {
    createEvent,
    editEvent,
    getEvent
}