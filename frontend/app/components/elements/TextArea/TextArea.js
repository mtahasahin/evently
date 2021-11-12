import React from 'react';
import cn from 'classnames';
import styles from './TextArea.module.css';

const TextArea = ({name, label, register, ...props}) => {
    if (label) {
        return <div className="relative space-y-2 w-full">
            <label className="uppercase text-sm text-gray-400">{label}</label>
            <textarea
                {...register(name)}
                className={cn(styles.base)}
                {...props}
            />
        </div>
    } else {
        return <textarea
            {...register(name)}
            className={cn(styles.base)}
            {...props}
        />
    }
};

export default TextArea;