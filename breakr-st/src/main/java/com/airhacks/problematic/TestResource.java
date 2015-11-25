package com.airhacks.problematic;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author airhacks.com
 */
@RequestScoped
@Path("tests")
public class TestResource {

    @Inject
    Brittle brittle;

    @Inject
    Slow slow;

    @GET
    @Path("slow/{iterations}")
    public String slow(@PathParam("iterations") int iterations) {
        String lastValue = null;
        for (int i = 0; i < iterations; i++) {
            lastValue = this.slow.tooSlow();
        }
        return lastValue;
    }

    @GET
    @Path("brittle/{iterations}")
    public String brittle(@PathParam("iterations") int iterations) {
        for (int i = 0; i < iterations; i++) {
            try {
                this.brittle.test(true);
            } catch (Throwable t) {
                System.out.println(" Message: " + t.getMessage());
                System.out.println("-");
            }
        }
        return "+";
    }

    @DELETE
    public void reset() {
        this.brittle.reset();
        this.slow.reset();
    }

}
