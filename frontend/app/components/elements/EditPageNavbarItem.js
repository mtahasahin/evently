import React from 'react';
import Link from "next/link";
import {useRouter} from "next/router";

const EditPageNavbarItem = ({Icon, text, link}) => {
    const router = useRouter();
    const selected = router.pathname === link;
    let classes = "flex w-full px-4 py-2 rounded hover:bg-gray-50 transition space-x-4 ";
    classes += `${selected ? "bg-gray-50" : null}`
    return (
        <Link href={link}>
            <a className={classes}>
                <Icon size="1.5rem" color="gray"/>
                <span>{text}</span>
            </a>
        </Link>
    );
};

export default EditPageNavbarItem;