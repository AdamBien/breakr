package com.airhacks.breakr;

import static com.airhacks.rulz.jaxrsclient.HttpMatchers.successful;
import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class BreakrIT {

    @Rule
    public JAXRSClientProvider provider = JAXRSClientProvider.buildWithURI("http://localhost:8080/breakr-st/resources/tests");

    @Test
    public void timeout() {
        WebTarget target = provider.target();
        Response response = target.
                path("slow").
                path("10").
                request().
                get(Response.class);
        assertThat(response, successful());
        String result = response.readEntity(String.class);
        assertThat(result, is("+"));
    }

    @Test
    public void brittle() {
        WebTarget target = provider.target();
        Response response = target.
                path("brittle").
                path("10").
                request().
                get(Response.class);
        assertThat(response, successful());
        String result = response.readEntity(String.class);
        assertThat(result, is("+"));
    }

}
