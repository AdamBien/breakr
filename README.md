# breakr
A Minimalistic [Circuit Breaker](http://martinfowler.com/bliki/CircuitBreaker.html) for Java EE applications

breakr comes as a single, 6 kB jar without any further configuration. Just add the dependency below to your `pom.xml` and 
put the annotation `@Interceptors(Breakr.class)` on a brittle class.

#Installation

```xml

        <dependency>
            <groupId>com.airhacks</groupId>
            <artifactId>breakr</artifactId>
            <version>[CURRENT_VERSION]</version>
        </dependency>

```

#Usage

## Ignoring exception throwers

```java
import com.airhacks.breakr.Breakr;
import com.airhacks.breakr.CloseCircuit;
import com.airhacks.breakr.IgnoreCallsWhen;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

/**
 * Breakr is implemented as an Java EE interceptor. Annotate a
 * <code>@Singleton</code> CDI or EJB to activate the call supervision.
 */
@Singleton
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

```

## Ignoring slow methods

```java

@Singleton
@Interceptors(Breakr.class)
public class Slow {

    @IgnoreCallsWhen(slowerThanMillis = 10)
    public String tooSlow() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {}
        return "Slow: " + System.currentTimeMillis();
    }

}
```
