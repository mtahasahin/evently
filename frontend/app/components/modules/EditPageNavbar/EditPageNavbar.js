import React from 'react';
import EditPageNavbarItem from "../../elements/EditPageNavbarItem";
import {CgProfile} from "react-icons/cg";
import {IoMdKey} from "react-icons/io";
import {FaTrashAlt} from "react-icons/fa";

const EditPageNavbar = () => {
    return (
        <nav className="flex flex-col space-y-2">
            <EditPageNavbarItem link="/edit/profile" text={"Basic Information"} Icon={CgProfile}/>
            <EditPageNavbarItem link="/edit/password" text={"Change Password"} Icon={IoMdKey}/>
            <EditPageNavbarItem link="/edit/close-account" text={"Close Account"} Icon={FaTrashAlt}/>
        </nav>
    );
};

export default EditPageNavbar;