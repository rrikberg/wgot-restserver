package wgot;

import java.security.Principal;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

public class UserResource {
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
    @Context
    SecurityContext securityContext;
    
    public UserResource(UriInfo uriInfo, Request request, SecurityContext securityContext){
        this.uriInfo = uriInfo;
        this.request = request;
        this.securityContext = securityContext;
    }
    
	@Secured
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public User getUser() {
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
    	DBHandler db = new DBHandler();
		User user = db.getUserByName(username);
        return user;
    }
}
