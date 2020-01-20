package wgot;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

public class LocationResource {
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
    
    public LocationResource(UriInfo uriInfo, Request request){
        this.uriInfo = uriInfo;
        this.request = request;
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Location getLocation(@PathParam("idLoc") int idL) {
    	DBHandler db = new DBHandler();
		Location loc = db.getLocationById(idL);
		loc.setCurBaseUrl(uriInfo.getBaseUri().toString());
        return loc;
    }
}

