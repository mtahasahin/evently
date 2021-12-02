import React from 'react';
import UserHeader from "../../modules/Header/Header";
import EditPageNavbar from "../../modules/EditPageNavbar/EditPageNavbar";
import Container from "../../Container";

const EditPageLayout = (page) => {
    return (
        <div className="flex flex-col gap-12 bg-gray-100 min-h-screen">
            <UserHeader/>
            <Container>
                <div className="flex flex-col lg:flex-row gap-4 w-full">
                    <div className="w-full lg:w-1/4">
                        <EditPageNavbar/>
                    </div>
                    <div className="w-full lg:w-3/4 px-3 py-3">
                        {page}
                    </div>
                </div>
            </Container>
        </div>
    );
};

export default EditPageLayout;