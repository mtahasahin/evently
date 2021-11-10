import React from 'react';

const FormGroup = ({children}) => {
    return (
        <div className="mb-6 gap-5 flex flex-col lg:flex-row ">
            {children}
        </div>
    );
};

export default FormGroup;