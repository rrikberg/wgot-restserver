package wgot;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("/promoter")
public class PromotersResource {
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
    @Context
    SecurityContext securityContext;
    
	@Path("{idPromo}/place/")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Map<String, Object> getLocations(@PathParam("idPromo") int idP) {
    	DBHandler db = new DBHandler();
        List<Location> locations = new ArrayList<Location>();
   		locations = db.getLocationsByPromoter(idP);

        //loop through locations to give them the current URI so that we can output it in the JSON
        String baseUri = uriInfo.getBaseUri().toString();
        for (Location l : locations){
    		l.setCurBaseUrl(baseUri);
        }
        
        Map<String,Object> root = new HashMap<String,Object>();
        Map<String,String> meta = new HashMap<String,String>();
        meta.put("count", Integer.toString(locations.size()));
        root.put("meta", meta);
        root.put("data", locations);
        return root;
    }
	// get a list of the locations belonging to the logged in user
	@Secured
    @GET
	@Path("me/place")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
	public Response getMyLocations(){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
    	
		DBHandler db = new DBHandler();
		User user= db.getUserByName(username);
    	int myId = user.getIdUser();
		
		URI u = uriInfo.getBaseUri();
		String n = u.getPath() + "promoter/" + myId + "/place";
		URI myplaces = u.resolve(n);
	    return Response.temporaryRedirect(myplaces).build();
	}
}
