import React, {useEffect} from 'react';
import {MdClose} from "react-icons/md";
import {FaComments} from "react-icons/fa";
import TextInput from "../../../elements/TextInput/TextInput";
import {useForm} from "react-hook-form";
import TextArea from "../../../elements/TextArea/TextArea";
import Button from "../../../elements/Button/Button";
import EventApi from "../../../../api/event.api";
import ErrorMessage from "../../../elements/ErrorMessage/ErrorMessage";
import {toast} from "react-toastify";
import useActiveEvent from "../../../../hooks/useActiveEvent";
import {useRouter} from "next/router";

const Alert = () => {
    const [show, setShow] = React.useState(true);
    return (show && <div
            className="w-full relative flex flex-row p-5 rounded bg-yellow-100 text-yellow-900 border border-yellow-200">
            <div><FaComments size="1.5rem"/></div>
            <div className="w-full mx-3 opacity-80">
                <div className="font-semibold mb-1.5">Registration Form</div>
                <div className="text-sm mb-1">Please answer the required questions below to attend this event. We will
                    get
                    you notified when organizer approve your attendance.
                </div>
            </div>
            <div>
                <button type="button" onClick={() => setShow(false)}
                        className="absolute top-0 right-0 p-3 hover:text-black"><MdClose/></button>
            </div>
        </div>
    )
}

const Question = ({title, description, isRequired, type, error, name, register}) => {
    return <div className="w-full relative flex flex-col space-y-2 p-5 rounded border border-opacity-50 shadow-sm">
        <div className="flex flex-row justify-between">
            <div className="font-semibold">{title}</div>
            <div className="text-sm text-gray-400">{isRequired ? "Required" : "Optional"}</div>
        </div>
        <div className="text-sm">{description}</div>
        <div className="pt-3">
            {type === "SHORT_TEXT" &&
                <div className="w-full">
                    <TextInput name={name} placeholder="Type your answer here" register={register}/>
                    <ErrorMessage messages={error}/>
                </div>}

            {type === "LONG_TEXT" &&
                <div className="w-full">
                    <TextArea name={name} placeholder="Type your answer here" register={register}/>
                    <ErrorMessage messages={error}/>
                </div>}
        </div>
    </div>
}

const DisplayEventQuestionsPage = () => {
    const {event, slug, reload} = useActiveEvent();
    const router = useRouter();

    const [questions, setQuestions] = React.useState([]);
    const {register, handleSubmit} = useForm();

    useEffect(() => {
        if (event) {
            setQuestions(event.questions);
        }
    }, [event]);

    const onSubmit = data => {
        const answers = Object.entries(data).map((entry) => ({
                questionId: entry[0],
                answer: entry[1]
            })
        );
        EventApi.applyToEvent({slug, answers})
            .then(res => {
                reload(res.data, false)
                toast(res.data.message, {type: "success"});
                router.replace(`/event/${slug}`)
            })
            .catch(res => {
                toast(res.response.data.message, {type: "error"});
                res.response.data.errors?.forEach(error => {
                    setQuestions(questions => {
                        const question = questions.indexOf(questions.find(q => q.id == error.field));
                        const new_questions = [...questions];
                        new_questions[question].error = error.message;
                        return new_questions;
                    });
                })
            });
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div className="flex flex-col space-y-5">

                <Alert/>
                {questions.map(question => {
                    return <Question key={question.id} title={question.title} description={question.description}
                                     isRequired={question.required} type={question.type} error={question.error}
                                     name={`${question.id}`} register={register}/>
                })}
                {!!questions.length ? <div className="self-end">
                    <Button appearance="success" size="lg">Submit My Answers</Button>
                </div> : <span className="text-gray-500 text-center text-sm p-5">No questions</span>}
            </div>
        </form>
    );
};

export default DisplayEventQuestionsPage;