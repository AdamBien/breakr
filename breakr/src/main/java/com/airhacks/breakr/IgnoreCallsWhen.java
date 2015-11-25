package com.airhacks.breakr;

/*
 * #%L
 * breakr
 * %%
 * Copyright (C) 2015 Adam Bien
 * %%
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
 * #L%
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * This optional annotation can be used to individually override the default
 * configuration of the circuit breaker.
 *
 * @author airhacks.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreCallsWhen {

    public static final long TIMEOUT = 1000;
    public static final long MAX_FAILURES = 3;

    /**
     * 1s max execution time of any public method of the monitored class.
     */
    long slowerThanMillis() default TIMEOUT;

    /**
     * The max number of failures (exception occurrences and timeouts) before
     * the circuit opens.
     */
    long failures() default MAX_FAILURES;
}
