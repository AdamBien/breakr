package com.airhacks.breakr;

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
        long start = System.currentTimeMillis();
        long maxNbrOfFailures = FAILURES_COUNT;
        long timeout = TIMEOUT_IN_MS;

        IgnoreCallsWhen configuration = ic.
                getMethod().
                getAnnotation(IgnoreCallsWhen.class);

        if (configuration != null) {
            maxNbrOfFailures = configuration.failures();
            timeout = configuration.timeout();
        }

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
            if (duration > timeout) {
                errorCounter++;
            }
        }
    }

}
