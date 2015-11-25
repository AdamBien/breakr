package com.airhacks.problematic;

import com.airhacks.breakr.Breakr;
import com.airhacks.breakr.CloseCircuit;
import com.airhacks.breakr.IgnoreCallsWhen;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

/**
 *
 * @author airhacks.com
 */
@Singleton
/**
 * Breakr is implemented as an Java EE interceptor. Annotate a
 * <code>@Singleton</code> CDI or EJB to activate the call supervision.
 */
@Interceptors(Breakr.class)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class Brittle {

    /**
     * With the optional <code>@IgnoreCallsWhen</code> you can override the
     * (timeout = 1s, maxFailures = 5).
     */
    @IgnoreCallsWhen(failures = 2)
    public String test(boolean exception) throws Exception {
        if (exception) {
            throw new Exception("For test purposes only");
        }
        return "works " + System.currentTimeMillis();
    }

    /**
     * Calling a method annotated with <code>@CloseCircuit</code> resets all
     * counters and closes the circuit.
     */
    @CloseCircuit
    public void reset() {
    }
}
