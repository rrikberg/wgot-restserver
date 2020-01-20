package wgot;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

public class EventResource {
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
    
    public EventResource(UriInfo uriInfo, Request request){
        this.uriInfo = uriInfo;
        this.request = request;
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Event getEvent(@PathParam("idEvent") int idE) {
    	DBHandler db = new DBHandler();
		Event ev = db.getEventById(idE);
		ev.setCurBaseUrl(uriInfo.getBaseUri().toString());
        return ev;
    }
}
