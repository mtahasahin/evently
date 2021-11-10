import React from 'react';
import SelectSearch from 'react-select-search-nextjs';
import {Controller} from "react-hook-form";


const Select = ({name, label, control, defaultValue, ...props}) => {
    if (label) {
        return <Controller control={control} defaultValue={defaultValue} render={({field}) => (
            <div className="relative space-y-3 w-full">
                <label className="uppercase text-sm text-gray-400">{label}</label>
                <SelectSearch {...props} onChange={field.onChange} onBlur={field.onBlur}
                              value={field.value || defaultValue}/>
            </div>
        )} name={name}/>

    } else {
        return <Controller control={control} defaultValue={defaultValue} render={({field}) => (
            <SelectSearch {...props} onChange={field.onChange} onBlur={field.onBlur}
                          value={field.value || defaultValue}/>
        )} name={name}/>
    }
};

export default Select;