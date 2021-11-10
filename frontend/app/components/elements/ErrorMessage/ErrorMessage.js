import React from 'react';

const ErrorMessage = ({messages}) => {
    if (typeof messages === 'string' || messages instanceof String) {
        return (<div className="font-semibold text-red-600 py-1 text-xs">
            {messages}
        </div>)
    } else if (Array.isArray(messages)) {
        return messages.map(message => (
            <div className="font-semibold text-red-600 py-1 text-xs">
                {message}
            </div>
        ));
    }
    return null;
};

export default ErrorMessage;