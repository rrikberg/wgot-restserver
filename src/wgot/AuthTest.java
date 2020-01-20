package wgot;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("authtest")
public class AuthTest {
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public String Test(){
    	return "auth successful";
    }
}
