import React from 'react';
import DatePicker from "react-datepicker";
import {Controller} from "react-hook-form";

const MyDatePicker = ({name, label, defaultValue, control, ...props}) => {
    if (label) {
        return <Controller control={control} defaultValue={defaultValue} render={({field}) => (
            <div className="relative space-y-2 w-full">
                <label className="uppercase text-sm text-gray-400">{label}</label>
                <DatePicker {...props} onChange={field.onChange} onBlur={field.onBlur}
                            selected={Date.parse(field.value) || defaultValue}/>
            </div>
        )} name={name}/>

    } else {
        return <Controller control={control} defaultValue={defaultValue} render={({field}) => (
            <DatePicker {...props} onChange={field.onChange} onBlur={field.onBlur}
                        selected={Date.parse(field.value) || defaultValue}/>
        )} name={name}/>
    }
};

export default MyDatePicker;