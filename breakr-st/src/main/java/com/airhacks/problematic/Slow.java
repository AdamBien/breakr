package com.airhacks.problematic;

import com.airhacks.breakr.Breakr;
import com.airhacks.breakr.CloseCircuit;
import com.airhacks.breakr.IgnoreCallsWhen;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

/**
 *
 * @author airhacks.com
 */
@Singleton
@Interceptors(Breakr.class)
public class Slow {

    @IgnoreCallsWhen(slowerThanMillis = 10)
    public String tooSlow() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
        return "Slow: " + System.currentTimeMillis();
    }

    @CloseCircuit
    public void reset() {
    }

}
