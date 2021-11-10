import React, {useEffect, useRef, useState} from 'react';
import {FaUserCircle} from "react-icons/fa";
import Link from "next/link";
import useAuth from "../../../hooks/useAuth";

function useOutsideAlerter(ref, callback) {
    useEffect(() => {
        /**
         * Alert if clicked on outside of element
         */
        function handleClickOutside(event) {
            if (ref.current && !ref.current.contains(event.target)) {
                callback()
            }
        }

        // Bind the event listener
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            // Unbind the event listener on clean up
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [ref]);
}

const NavbarItem = ({children, href, ...props}) => {
    return (
            <Link href={href}>
                <a className="px-4 py-1.5 block hover:bg-gray-50 active:bg-yellow-400 active:text-white" {...props}>{children}</a>
            </Link>
    )
}

const NavbarDropdown = () => {
    const {logout} = useAuth();
    const [menuOpened, setMenuOpened] = useState(false);
    const toggleMenu = () => setMenuOpened(open => !open);
    const closeMenu = () => setMenuOpened(false);
    const wrapperRef = useRef(null);
    useOutsideAlerter(wrapperRef, closeMenu)

    return (
        <div className="relative cursor-pointer select-none" ref={wrapperRef} onClick={toggleMenu}>
            <FaUserCircle size="2rem" className="h-full"/>
            {menuOpened &&
            <div className="absolute bg-white shadow py-2 mt-4 right-0 left-auto whitespace-nowrap rounded text-sm">
                <ul>
                    <NavbarItem href="/profile">Profile</NavbarItem>
                    <NavbarItem href="/follower-requests">Follower
                        Requests</NavbarItem>
                    <NavbarItem href="/edit/profile">Settings</NavbarItem>
                    <NavbarItem href="" onClick={logout}>Logout</NavbarItem>
                </ul>
            </div>}
        </div>
    );
};

export default NavbarDropdown;