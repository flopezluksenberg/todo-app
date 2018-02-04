package com.flopezluksenberg.todo

import android.content.Context
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule

class ObjectMapperHelper {
    companion object {
        private var INSTANCE: ObjectMapper? = null

        @Synchronized fun getInstance(): ObjectMapper {
            if (INSTANCE == null) {
                INSTANCE = ObjectMapper().let {
                    it.registerModule(KotlinModule())
                    it.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
                    it.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    it.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
                    it.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                }
            }
            return INSTANCE!!
        }
    }
}

val Context.objectMapper: ObjectMapper get() = ObjectMapperHelper.getInstance()