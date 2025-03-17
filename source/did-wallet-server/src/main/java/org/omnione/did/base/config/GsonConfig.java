/*
 * Copyright 2024 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omnione.did.base.config;

import com.google.gson.*;
import org.omnione.did.base.datamodel.enums.EccCurveType;
import org.omnione.did.base.datamodel.enums.OpenDidEnum;
import org.omnione.did.base.datamodel.enums.SymmetricCipherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Type;
import java.util.List;

/**
 * The GsonConfig class provides a configuration for Gson.
 * This class registers custom type adapters for specific enums used in the application.
 */
@Configuration
public class GsonConfig implements WebMvcConfigurer {

    /**
     * Configures and returns a Gson instance with custom type adapters.
     * This method registers custom adapters for the ServerTokenPurpose and VerifyAuthType enums,
     * allowing Gson to correctly serialize and deserialize these types.
     *
     * @return a configured Gson instance
     */
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(OpenDidEnum.class, new OpenDidEnumSerializer())
                .registerTypeAdapter(SymmetricCipherType.class, new SymmetricCipherTypeDeserializer())
                .create();
    }

//     This method creates and configures a GsonHttpMessageConverter that uses Gson for HTTP message conversion.
//     The GsonHttpMessageConverter is responsible for serializing and deserializing HTTP requests and responses using Gson.
//     By including this, Spring MVC will use Gson instead of the default Jackson for converting JSON.
//     However, Gson has limitations when serializing complex types like Page<AdminDto>, so it's disabled here.
//    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter(Gson gson) {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson);
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(gsonHttpMessageConverter(gson()));
    }

    public static class OpenDidEnumSerializer implements JsonSerializer<OpenDidEnum> {
        @Override
        public JsonElement serialize(OpenDidEnum src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getValue());
        }
    }

    public static class SymmetricCipherTypeDeserializer implements JsonDeserializer<SymmetricCipherType> {
        @Override
        public SymmetricCipherType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String displayName = json.getAsString();

            return SymmetricCipherType.fromDisplayName(displayName);
        }
    }
}
