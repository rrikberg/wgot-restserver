package wgot;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("/place")
public class LocationsResource {
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
    @Context
    SecurityContext securityContext;

    //name=&city=&zipCode=&address=
	@Secured
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
	public Response newLocation(@FormParam("name") String name,
			@FormParam("city") String city,
			@FormParam("zipCode") int zipCode,
			@FormParam("address") String address
			) throws ParseException {
	DBHandler db = new DBHandler();

	Location loc = new Location(-1, name, city, zipCode, address);
	
	String username = securityContext.getUserPrincipal().getName();
	User user= db.getUserByName(username);
	int myId = user.getIdUser();
	
	int r = db.createLocation(loc, myId);
	if (r == -1){
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	URI u = uriInfo.getBaseUri();
	String n = u.getPath() + "place/" + r;
	URI newLocationURI = u.resolve(n);
	Location newLocationJson = db.getLocationById(r);
	newLocationJson.setCurBaseUrl(uriInfo.getBaseUri().toString());
	return Response.created(newLocationURI).entity(newLocationJson).build();
	}
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Map<String, Object> getLocations() {
    	DBHandler db = new DBHandler();
        List<Location> locations = new ArrayList<Location>();
        locations = db.getAllLocations();

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

	@Path("{idLoc}")
	public LocationResource getLocation(){
		return new LocationResource(uriInfo, request);
	}
}
