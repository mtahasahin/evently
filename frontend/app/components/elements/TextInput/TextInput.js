import React from 'react';
import cn from 'classnames';
import styles from './TextInput.module.css';

const TextInput = ({ name, label, register, error, ...props }) => {
  if (label) {
    return (
      <div className="relative space-y-2 w-full">
        <label className="uppercase text-sm text-gray-400">{label}</label>
        <input
          type="text"
          {...register(name)}
          className={cn(
            !error ? styles.base : undefined,
            error ? styles.error : undefined
          )}
          {...props}
        />
      </div>
    );
  } else {
    return (
      <input
        type="text"
        {...register(name)}
        className={cn(
          !error ? styles.base : undefined,
          error ? styles.error : undefined
        )}
        {...props}
      />
    );
  }
};

export default TextInput;
