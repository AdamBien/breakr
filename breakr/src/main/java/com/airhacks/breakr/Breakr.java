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
import java.lang.reflect.Method;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author airhacks.com
 */
public class Breakr {

    private static final long FAILURES_COUNT = 5;
    private static final long TIMEOUT_IN_MS = 1000;

    @Inject
    Circuit circuit;

    @AroundInvoke
    public Object guard(InvocationContext ic) throws Exception {
        long maxFailures = FAILURES_COUNT;
        long maxDuration = TIMEOUT_IN_MS;
        Method method = ic.getMethod();
        IgnoreCallsWhen configuration = method.
                getAnnotation(IgnoreCallsWhen.class);
        if (configuration != null) {
            maxFailures = configuration.failures();
            maxDuration = configuration.slowerThanMillis();
        }

        long start = System.currentTimeMillis();
        try {
            if (circuit.isOpen(maxFailures)) {
                return null;
            }
            return ic.proceed();
        } catch (Exception ex) {
            circuit.newException(ex);
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - start;

            if (duration > maxDuration) {
                this.circuit.newTimeout(duration);
            }
        }
    }

}
