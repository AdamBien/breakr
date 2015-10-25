# breakr
A Minimalistic Circuit Breaker for Java EE applications

#Usage

## Ignoring exception throwers

```java
@Singleton
@Interceptors(Breakr.class)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class Brittle {

    @IgnoreCallsWhen(failures = 2)
    public String test(boolean exception) throws Exception {
        if (exception) {
            throw new Exception("For test purposes only");
        }
        return "works " + System.currentTimeMillis();
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
        } catch (InterruptedException ex) {
            Logger.getLogger(Slow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Slow: " + System.currentTimeMillis();
    }

}
``