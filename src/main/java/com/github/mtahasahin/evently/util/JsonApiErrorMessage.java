package com.github.mtahasahin.evently.util;


import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JsonApiErrorMessage extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final ErrorAttributeOptions errorAttributeOptions) {
        final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, errorAttributeOptions);
        final Map<String, Object> jsonApiErrorAttributes = new LinkedHashMap<>();
        jsonApiErrorAttributes.put("data", null);
        jsonApiErrorAttributes.put("resultType", "ERROR");
        jsonApiErrorAttributes.put("message", errorAttributes.get("error"));

        return jsonApiErrorAttributes;
    }

}
