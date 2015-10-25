package com.airhacks.breakr;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author airhacks.com
 */
public class Breakr {

    private long errorCounter;
    private static final int THRESHOLD = 5;

    @AroundInvoke
    public Object guard(InvocationContext ic) throws Exception {
        try {
            if (errorCounter >= THRESHOLD) {
                return null;
            }
            return ic.proceed();
        } catch (Exception ex) {
            errorCounter++;
            throw ex;
        }
    }

}
