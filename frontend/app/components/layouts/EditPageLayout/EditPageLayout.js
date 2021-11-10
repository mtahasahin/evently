import React from 'react';
import UserHeader from "../../modules/Header/Header";
import EditPageNavbar from "../../modules/EditPageNavbar/EditPageNavbar";

const EditPageLayout = (page) => {
    return (
        <div className="flex flex-col gap-12 bg-gray-100 min-h-screen">
            <UserHeader/>
            <div className="container mx-auto w-full xl:w-4/5 max-w-screen-xl">
                <div className="flex flex-col lg:flex-row gap-4">
                    <div className="w-full lg:w-1/4">
                        <EditPageNavbar/>
                    </div>
                    <div className="w-full lg:w-3/4 px-3 py-3">
                        {page}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default EditPageLayout;