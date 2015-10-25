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

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author airhacks.com
 */
public class Breakr {

    private long errorCounter;
    private static final int FAILURES_COUNT = 5;
    private static final int TIMEOUT_IN_MS = 1000;

    @AroundInvoke
    public Object guard(InvocationContext ic) throws Exception {
        long maxNbrOfFailures = FAILURES_COUNT;
        long timeout = TIMEOUT_IN_MS;

        IgnoreCallsWhen configuration = ic.
                getMethod().
                getAnnotation(IgnoreCallsWhen.class);

        if (configuration != null) {
            maxNbrOfFailures = configuration.failures();
            timeout = configuration.slowerThanMillis();
        }

        long start = System.currentTimeMillis();
        try {
            if (errorCounter >= maxNbrOfFailures) {
                return null;
            }
            return ic.proceed();
        } catch (Exception ex) {
            errorCounter++;
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - start;
            if (duration >= timeout) {
                errorCounter++;
            }
        }
    }

}
