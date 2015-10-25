# breakr
A Minimalistic Circuit Breaker for Java EE applications

#Usage

## Ignoring exception throwers

```java
@Singleton
<b>@Interceptors(Breakr.class)</b>
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class Brittle {

    <b>@IgnoreCallsWhen(failures = 2)</b>
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
<b>@Interceptors(Breakr.class)</b>
public class Slow {

    <b>@IgnoreCallsWhen(slowerThanMillis = 10)</b>
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
