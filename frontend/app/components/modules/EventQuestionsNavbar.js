import React from 'react';
import {FaCheck} from "react-icons/fa";
import {useRouter} from "next/router";
import Link from "next/link";
import {IoDocumentTextSharp} from "react-icons/io5";
import {RiWechatLine} from "react-icons/ri";
import {MdGroups} from "react-icons/md";
import useActiveEvent from "../../hooks/useActiveEvent";

const EventQuestionsNavbarItem = ({Icon, text, link}) => {
    const router = useRouter();
    const selected = router.asPath === link;
    let classes = "flex w-full py-3 hover:px-2 hover:text-black items-end hover:bg-gray-50 transition-all space-x-2 font-medium text-sm text-gray-600 border-b";
    classes += `${selected ? " border-b-2 border-b-yellow-400 text-black" : ""}`
    return (
        <Link href={link}>
            <a className={classes}>
                <Icon size="1.2rem" color="gray"/>
                <span>{text}</span>
            </a>
        </Link>
    );
}

const EventQuestionsNavbar = () => {
    const {event, slug} = useActiveEvent();

    const no_questions = event.questions.length === 0;

    return (
        <nav className="flex flex-col px-3">
            {!no_questions && <EventQuestionsNavbarItem link={`/event/${slug}/questions`} text={"List Forms"}
                                                        Icon={IoDocumentTextSharp}/>}
            <EventQuestionsNavbarItem link={`/event/${slug}/questions/edit`}
                                      text={no_questions ? "Create a Form" : "Edit Questions"} Icon={RiWechatLine}/>
            <EventQuestionsNavbarItem link={`/event/${slug}/questions/answers`} text={"Waiting List"} Icon={MdGroups}/>
            <EventQuestionsNavbarItem link={`/event/${slug}/questions/answers/all`} text={"Show All Answers"}
                                      Icon={FaCheck}/>
        </nav>
    );
};

export default EventQuestionsNavbar;