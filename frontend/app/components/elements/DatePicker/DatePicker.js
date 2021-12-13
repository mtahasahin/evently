import React from 'react';
import DatePicker from 'react-datepicker';
import { Controller } from 'react-hook-form';
import cn from 'classnames';
import styles from '../TextInput/TextInput.module.css';

const MyDatePicker = ({
  name,
  label,
  defaultValue,
  control,
  error,
  ...props
}) => {
  if (label) {
    return (
      <Controller
        control={control}
        defaultValue={defaultValue}
        render={({ field }) => (
          <div className="space-y-2 w-full">
            <label className="uppercase text-sm text-gray-400">{label}</label>
            <DatePicker
              {...props}
              onChange={field.onChange}
              onBlur={field.onBlur}
              selected={Date.parse(field.value) || defaultValue}
              className={cn(
                !error ? styles.base : undefined,
                error ? styles.error : undefined
              )}
            />
          </div>
        )}
        name={name}
      />
    );
  } else {
    return (
      <Controller
        control={control}
        defaultValue={defaultValue}
        render={({ field }) => (
          <DatePicker
            {...props}
            onChange={field.onChange}
            onBlur={field.onBlur}
            selected={Date.parse(field.value) || defaultValue}
            className={cn(
              !error ? styles.base : undefined,
              error ? styles.error : undefined
            )}
          />
        )}
        name={name}
      />
    );
  }
};

export default MyDatePicker;
