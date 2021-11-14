import React from 'react';
import Header from "../../modules/Header/Header";

const CreateEditEventLayout = (page) => {
    return (
        <div className="flex flex-col gap-12 bg-gray-100 min-h-screen">
            <Header/>
            <div className="container mx-auto w-full xl:w-4/5 max-w-screen-xl">
                <div className="flex flex-col lg:flex-row justify-center">
                    <div className="w-full lg:w-3/4 px-3 py-3">
                        {page}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CreateEditEventLayout;