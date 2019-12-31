package com.gfe.imageservice.configuration.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

public class EmptyStringDeserializer extends JsonDeserializer<String>{

	@Override 
	public Class<?> handledType() {
		return String.class;
	}
	
	@Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.getCurrentToken() == JsonToken.VALUE_STRING && "".equals(jp.getText())) {
            return null;
        }

        return StringDeserializer.instance.deserialize(jp, ctxt);
    }
}
