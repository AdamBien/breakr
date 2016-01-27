# breakr
A Minimalistic [Circuit Breaker](http://martinfowler.com/bliki/CircuitBreaker.html) for Java EE applications

breakr comes as a single, 6 kB jar without any further configuration. Just add the dependency below to your `pom.xml` and
put the annotation `@Interceptors(Breakr.class)` on a brittle class.

Checkout [porcupine](https://github.com/AdamBien/porcupine) for monitorable threadpools (bulkheads and handshaking) implementation.

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

## Implementing custom behavior

The decision when the circuit is opened or closed are made by the class `com.airhacks.breakr.Circuit`

```java


public class Circuit {

    protected AtomicLong failureCounter;

    @PostConstruct
    public void init() {
        this.failureCounter = new AtomicLong(0);
    }

    public void newException(Exception ex) {
        this.failureCounter.incrementAndGet();
    }

    public void newTimeout(long duration) {
        this.failureCounter.incrementAndGet();
    }

    public boolean isOpen(long maxFailures) {
        return (this.failureCounter.get() >= maxFailures);
    }

    public void reset() {
        failureCounter.set(0);
    }

}

```

To override the default behavior, simply specialize the class and inject your own behavior:

```java

@ApplicationScoped
@Specializes
public class CustomCircuit extends Circuit {

    private boolean open;

    @Override
    public boolean isOpen(long maxFailures) {
        return open;
    }

    public long getFailureCount() {
        return failureCounter.get();
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}

```