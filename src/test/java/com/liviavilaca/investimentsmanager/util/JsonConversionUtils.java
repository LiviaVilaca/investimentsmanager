package com.liviavilaca.investimentsmanager.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


class IgnoreJacksonWriteOnlyAccess extends JacksonAnnotationIntrospector {

    @Override
    public JsonProperty.Access findPropertyAccess(Annotated m) {
        JsonProperty.Access access = super.findPropertyAccess(m);
        if (access == JsonProperty.Access.WRITE_ONLY) {
            return JsonProperty.Access.AUTO;
        }
        return access;
    }
}

public class JsonConversionUtils {

    public static String asJsonString(Object objectDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.setAnnotationIntrospector(new IgnoreJacksonWriteOnlyAccess());
            objectMapper.registerModules(new JavaTimeModule());

            return objectMapper.writeValueAsString(objectDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
