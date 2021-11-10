import React from 'react';
import cn from 'classnames';
import styles from './SocialMediaInput.module.css'

const SocialMediaInput = ({name, label, precedingUrl, register, ...props}) => {
    if (label) {
        return (
            <div className="relative space-y-3 w-full">
                <label className="uppercase text-sm text-gray-400">{label}</label>
                <div className="flex flex-row">
                    <div
                        className="bg-gray-200 rounded-l h-full border border-r-0 p-3 text-gray-500">{precedingUrl}</div>
                    <input type="text"
                           {...register(name)}
                           className={cn(styles.base)}
                           {...props}
                    />
                </div>
            </div>
        );
    } else {
        return (
            <input type="text"
                   {...register(name)}
                   className={cn(styles.base)}
                   {...props}
            />
        )
    }
};

export default SocialMediaInput;