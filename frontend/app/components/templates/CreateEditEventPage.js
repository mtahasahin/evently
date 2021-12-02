import React, {useEffect} from 'react';
import {useForm, useWatch} from "react-hook-form";
import FormGroup from "../elements/FormGroup/FormGroup";
import TextInput from "../elements/TextInput/TextInput";
import DatePicker from "../elements/DatePicker/DatePicker";
import Select from "../elements/Select/Select";
import {fuzzySearch} from "react-select-search-nextjs";
import Button from "../elements/Button/Button";
import timezones from "../../constants/timezone-list"
import languages from "../../constants/language-list";
import ErrorMessage from "../elements/ErrorMessage/ErrorMessage";
import useAuth from "../../hooks/useAuth";
import Checkbox from "../elements/Checkbox/Checkbox";
import RadioGroup from "../elements/RadioGroup/RadioGroup";
import Editor from "../elements/Editor/Editor";
import Dropzone from "../elements/Dropzone/Dropzone";
import EventApi from "../../api/event.api";
import moment from "moment-timezone";
import Loader from "react-loader-spinner";
import {toast} from "react-toastify";
import router from "next/router";
import useActiveEvent from "../../hooks/useActiveEvent";
import LoadingIndicator from "../elements/LoadingIndicator/LoadingIndicator";

const timezoneOptions = timezones.map(e => ({name: e, value: e}))
const languageOptions = languages.map(e => ({name: `${e.name} (${e.nativeName})`, value: e.code}))
const profileVisibilityOptions = [{name: "Everyone", value: "PUBLIC"}, {
    name: "Only those with link",
    value: "ONLY_WITH_LINK"
}];


const FormContent = ({isLoading, edit, errors, control, register, isOnlineEvent, prevImage, submitting}) => (
    <div className={`${isLoading ? "hidden" : ""}`}>
        <div className="bg-white rounded-t h-full w-full py-10 px-8">
            <div className="flex flex-col">
                <h1 className="text-2xl mb-5">{edit ? "Edit" : "Create"} Event</h1>
                <FormGroup>
                    <div className="w-full">
                        <TextInput label="WHAT SHOULD WE DO?" name="name" register={register}
                                   error={errors.name}/>
                        <ErrorMessage messages={errors.name?.message}/>
                    </div>
                </FormGroup>
                <FormGroup>
                    <div className="w-full">
                        <DatePicker label="START DATE" name="startDate" control={control}
                                    error={errors.startDate}
                                    showTimeSelect
                                    timeFormat="HH:mm"
                                    timeIntervals={15}
                                    timeCaption="time"
                                    dateFormat="yyyy-MM-dd HH:mm"/>
                        <ErrorMessage messages={errors.startDate?.message}/>
                    </div>
                    <div className="w-full">
                        <DatePicker label="END DATE" name="endDate" control={control}
                                    error={errors.endDate}
                                    showTimeSelect
                                    timeFormat="HH:mm"
                                    timeIntervals={15}
                                    timeCaption="time"
                                    dateFormat="yyyy-MM-dd HH:mm"/>
                        <ErrorMessage messages={errors.endDate?.message}/>
                    </div>
                    <Select options={timezoneOptions} search filterOptions={fuzzySearch}
                            placeholder=" "
                            name="timezone"
                            control={control}
                            label="TIMEZONE">
                    </Select>
                </FormGroup>
                <FormGroup>
                    <Dropzone name="image" control={control} displayedImage={prevImage}/>
                </FormGroup>
                <FormGroup>
                    <div className="flex flex-col space-y-2 w-full">
                        <div className="text-sm text-gray-400">EVENT DESCRIPTION</div>
                        <Editor name="description" control={control} editorLoaded={!isLoading}/>
                    </div>
                </FormGroup>
                <FormGroup>
                    <Checkbox label="This is an Online Event" name="eventLocationType"
                              control={control}/>
                </FormGroup>
                {isOnlineEvent ?
                    <FormGroup>
                        <div className="w-full">
                            <TextInput key="eventUrl" label="EVENT URL(required)" name="eventUrl"
                                       placeholder="https://example.com" register={register}
                                       error={errors.eventUrl}/>
                            <ErrorMessage messages={errors.eventUrl?.message}/>
                        </div>
                    </FormGroup>
                    :
                    <FormGroup>
                        <div className="w-full">
                            <TextInput key="location" label="EVENT LOCATION" name="location"
                                       register={register} error={errors.location}/>
                            <ErrorMessage messages={errors.location?.message}/>
                        </div>
                    </FormGroup>}
                <FormGroup>
                    <Select options={languageOptions} search filterOptions={fuzzySearch}
                            placeholder="Select language"
                            name="language"
                            control={control}
                            label="EVENT LANGUAGE">
                    </Select>
                </FormGroup>
                <FormGroup>
                    <div className="flex flex-col space-y-2">
                        <div className="text-sm text-gray-400">ATTENDEE LIMIT</div>
                        <div className="w-full">
                            <RadioGroup name="limited" direction="col" control={control}
                                        options={[{value: "false", label: "No Attendee Limit"}, {
                                            value: "true",
                                            label: <div>Attendee limit is <input type="number"
                                                                                 className="border-b text-center w-16 focus:outline-none"
                                                                                 pattern="[0-9]*" {...register("attendeeLimit")}/>
                                            </div>
                                        }]}/>
                            <ErrorMessage messages={errors.attendeeLimit?.message}/>
                        </div>
                    </div>
                </FormGroup>
                <FormGroup>
                    <div className="flex flex-col space-y-2">
                        <div className="text-sm text-gray-400">APPROVALS FOR ATTENDANCE</div>
                        <Checkbox
                            label="If you want to manage and approve who can attend this event check this option"
                            name="approvalRequired" control={control}/>
                        <div className="text-xs">Attendees will be on the <span
                            className="font-semibold">waitlist</span> and should be approved by
                            organizers
                            before the event.
                        </div>
                    </div>
                </FormGroup>
                <FormGroup>
                    <Select options={profileVisibilityOptions} search filterOptions={fuzzySearch}
                            name="visibility"
                            defaultValue={"PUBLIC"}
                            control={control}
                            label="Who can see this event?"/>
                </FormGroup>
            </div>
        </div>
        <div className="w-full px-8 py-6 bg-gray-50 rounded-b flex flex-row justify-end">
            <Button type="submit" appearance="success" size="lg" disabled={submitting}>
                    <span className="font-semibold flex justify-center items-center gap-x-2">
                    Publish
                    <Loader type="Oval"
                            color="White"
                            height={20}
                            width={20}
                            visible={submitting}/>

                    </span></Button>
        </div>
    </div>
)

const CreateEditEventPage = () => {
    const {slug, event, isLoading, isError, reload} = useActiveEvent();
    const edit = !!slug;

    const [isPageLoaded, setIsPageLoaded] = React.useState(false);
    const [submitting, setSubmitting] = React.useState(false);
    const [prevImage, setPrevImage] = React.useState(null);
    const {user} = useAuth();

    const {
        register,
        control,
        handleSubmit,
        formState: {errors},
        reset,
        setError,
    } = useForm({
        defaultValues: {
            timezone: moment.tz.guess(),
            startDate: moment().add(1, 'days'),
            endDate: moment().add(2, 'days'),
            language: user.profile.language ?? "en",
            limited: "false",
            description: "",
            approvalRequired: false,
            eventLocationType: false
        }
    });

    useEffect(() => {
        if (!edit) {
            setIsPageLoaded(true);
        } else {
            if (isLoading)
                return;
            if (isError || !event.organizing) {
                router.push("/404");
                return;
            }

            reset({
                name: event.name,
                description: event.description,
                startDate: moment.tz(event.startDate, moment.tz.guess()),
                endDate: moment.tz(event.endDate, moment.tz.guess()),
                timezone: event.timezone,
                location: event.location,
                language: event.language,
                limited: String(event.limited),
                attendeeLimit: event.attendeeLimit,
                approvalRequired: event.approvalRequired,
                visibility: event.visibility,
                eventLocationType: event.eventLocationType === "ONLINE",
                eventUrl: event.eventUrl,
            });
            setPrevImage(event.imagePath);
            setIsPageLoaded(true);
        }
    }, [event]);

    const isOnlineEvent = useWatch({control: control, name: "eventLocationType"});

    const onSubmit = data => {
        setSubmitting(true);
        if (edit) {
            EventApi.editEvent({slug, ...data}).then(res => {
                toast("Event updated.", {type: "success"});
                reload(res.data, false)
                    .then(() => {
                        router.push(`/event/${slug}`);
                    })
            }).catch(err => {
                toast(err.response.data.message, {type: "error"});
                err.response?.data?.errors?.forEach(err => {
                    setError(err.field, {type: "value", message: err.message});
                })
            })
                .finally(() => {
                    setSubmitting(false);
                })
        } else {
            EventApi.createEvent(data)
                .then(res => {
                    toast("Event created.", {type: "success"});
                    reload(res.data, false)
                        .then(() => {
                            router.push(`/event/${res.data.data.slug}`);
                        })
                })
                .catch(err => {
                    toast(err.response.data.message, {type: "error"});
                    err.response?.data?.errors.forEach(err => {
                        setError(err.field, {type: "value", message: err.message});
                    })
                })
                .finally(() => {
                    setSubmitting(false);
                })
        }
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div className="shadow-sm mb-6">
                <LoadingIndicator isLoading={!isPageLoaded}/>
                <FormContent edit={edit} isLoading={!isPageLoaded} control={control} register={register} errors={errors}
                             isOnlineEvent={isOnlineEvent}
                             prevImage={prevImage}
                             submitting={submitting}/>
            </div>
        </form>
    );
};
export default CreateEditEventPage;