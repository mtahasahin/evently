import styles from './Checkbox.module.css';
import cn from 'classnames';
import React from 'react';
import { Controller } from 'react-hook-form';

const HiddenCheckbox = ({ inputRef, ...props }) => {
  return (
    <input
      type="checkbox"
      ref={inputRef}
      className={styles.hidden}
      {...props}
    />
  );
};

const StyledCheckbox = ({ children, checked }) => {
  return (
    <div
      className={cn(
        styles.styled,
        checked ? styles.selected : styles['not-selected']
      )}
    >
      {checked && children}
    </div>
  );
};

const CheckboxContainer = ({ children }) => {
  return <div className={styles.container}>{children}</div>;
};

const BaseCheckbox = ({ className, checked, inputRef, ...props }) => (
  <CheckboxContainer className={className}>
    <HiddenCheckbox checked={checked} inputRef={inputRef} {...props} />
    <StyledCheckbox checked={checked}>
      <div className={styles.icon} />
    </StyledCheckbox>
  </CheckboxContainer>
);

const Checkbox = ({ name, label, control, defaultValue, ...props }) => (
  <Controller
    control={control}
    defaultValue={defaultValue || false}
    render={({ field }) => (
      <label className="flex items-start">
        <BaseCheckbox
          checked={field.value}
          onChange={field.onChange}
          onBlur={field.onBlur}
          inputRef={field.ref}
          {...props}
        />
        <span className="ml-2 text-sm">{label}</span>
      </label>
    )}
    name={name}
  />
);

export default Checkbox;
