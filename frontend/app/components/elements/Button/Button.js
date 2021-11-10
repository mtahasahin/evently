import React from 'react';
import cn from 'classnames';
import styles from './Button.module.css';

const Button = ({appearance, size, fullWidth, children, ...props}) => {
    return (
        <button
            className={cn(
                styles.base,
                appearance === 'primary' ? styles.primary : undefined,
                appearance === 'secondary' ? styles.secondary : undefined,
                appearance === 'success' ? styles.success : undefined,
                appearance === 'dark' ? styles.dark : undefined,
                appearance === 'danger' ? styles.danger : undefined,
                appearance === 'white' ? styles.white : undefined,
                !appearance ? styles.primary : undefined,
                size === 'md' ? styles.medium : undefined,
                size === 'lg' ? styles.large : undefined,
                size === 'xl' ? styles["x-large"] : undefined,
                !size ? styles.medium : undefined,
                fullWidth ? 'w-full' : undefined,
            )} {...props}>
            {children}
        </button>
    );
};

export default Button;