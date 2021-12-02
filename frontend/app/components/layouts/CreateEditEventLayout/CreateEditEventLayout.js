import React from 'react';
import Header from "../../modules/Header/Header";
import Container from "../../Container";

const CreateEditEventLayout = (page) => {
    return (
        <div className="flex flex-col gap-12 bg-gray-100 min-h-screen">
            <Header/>
            <Container>
                <div className="flex flex-col lg:flex-row justify-center">
                    <div className="w-full lg:w-3/4 px-3 py-3">
                        {page}
                    </div>
                </div>
            </Container>
        </div>
    );
};

export default CreateEditEventLayout;