package wgot;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("/user")
public class UsersResource {
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
    @Context
    SecurityContext securityContext;
    
	//Content-Type: application/x-www-form-urlencoded
	//firstName=&lastName=&userName=&email=&password=
    //TODO: return something else than a string?
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public String newEvent(@FormParam("firstName") String fname,
						@FormParam("lastName") String lname,
						@FormParam("userName") String uname,
						@FormParam("email") String email,
						@FormParam("password") String pw)  {
		
		DBHandler db = new DBHandler();
		
		//user id and userType id should not be set through rest API
		User u = new User(-1, fname, lname, uname, email, pw, -1);
		String r = db.createUser(u);
		
		return r;
	}
	//return info for user currently logged in
	@Path("/me")
	public UserResource getEvent(){
		return new UserResource(uriInfo, request, securityContext);
	}
}
