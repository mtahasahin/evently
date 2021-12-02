import React from 'react';
import Image from 'next/image';
import image404 from '../public/404.png';
import DefaultLayout from "../app/components/layouts/DefaultLayout/DefaultLayout";
import Link from "next/link";
import Button from "../app/components/elements/Button/Button";

const NotFound = () => {
    return (
        <div
            className="mx-auto my-auto items-center sm:justify-between max-w-screen-xl py-[40px] sm:py-[120px]">
            <div className="flex flex-col-reverse gap-y-6 sm:flex-row justify-between items-center">
                <div className="flex flex-col gap-y-6 items-start px-4">
                    <h1 className="text-4xl font-bold">The page you are looking for doesn't exist</h1>
                    <h3 className="text-lg text-gray-600">You might have mistyped the address, or the page might have
                        moved.</h3>
                    <Link href="/"><Button appearance="secondary" size="xl">Go to homepage</Button></Link>
                </div>
                <div className="">
                    <Image src={image404}/>
                </div>
            </div>
        </div>
    );
};

NotFound.getLayout = page => DefaultLayout(page)

export default NotFound