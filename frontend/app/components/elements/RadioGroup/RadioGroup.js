import styles from "./RadioGroup.module.css";
import cn from "classnames";
import React from "react";
import {Controller} from "react-hook-form";

const HiddenRadio = ({name, value, onChange, inputRef}) => (
    <input type="radio" name={name} value={value} ref={inputRef} onChange={onChange} className={styles.hidden}/>
)

const StyledRadio = ({children, selected}) => (
    <div className={cn(styles.styled, selected ? styles.checked : styles['not-checked'])}>
        {selected && children}
    </div>
)

const RadioContainer = ({children}) => (
    <div className={styles.container}>{children}</div>
)

const BaseRadio = ({name, className, value, selected, ...props}) => (
    <RadioContainer className={className}>
        <HiddenRadio value={value} name={name} {...props} />
        <StyledRadio value={value} name={name} selected={selected}>
            <div className={styles.icon}/>
        </StyledRadio>
    </RadioContainer>
)

const RadioGroup = ({name, options, direction, control, ...props}) => (
    <Controller control={control} name={name} render={({field}) => {
        return (
            <div className={`flex flex-${direction} gap-4`}>
                {options.map(opt => (<label className="flex items-start" key={opt.value}>
                        <BaseRadio
                            name={field.name}
                            value={opt.value}
                            selected={field.value === opt.value}
                            onChange={field.onChange}
                            onBlur={field.onBlur}
                            {...props}
                        />
                        <span className="ml-2 text-sm">{opt.label}</span>
                    </label>
                ))}
            </div>
        )
    }
    }/>
)

export default RadioGroup;