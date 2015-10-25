package com.airhacks.problematic;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
        this.slow.tooSlow();
        return "+";
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

    @GET
    @Path("working/{iterations}")
    public String working(@PathParam("iterations") int iterations) {
        String retVal = "";
        for (int i = 0; i < iterations; i++) {
            try {
                retVal += this.brittle.test(false);
            } catch (Exception ex) {
            }
        }
        return "+" + retVal;
    }

}
