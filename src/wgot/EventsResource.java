package wgot;

import java.net.URI;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/event")
public class EventsResource {
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
    
    //Create new event and return the Json of the created event to the creator
    //Content-Type: application/x-www-form-urlencoded 
	//title=&idPromoter=&idLocation=&entranceFee=&startDate=&endDate=&idCategory=
	//date format: yyyy-MM-ddTHH:mm:ssZ
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
	public Response newEvent(@FormParam("title") String title,
						@FormParam("idPromoter") int promoter,
						@FormParam("idLocation") int location,
						@FormParam("entranceFee") float entranceFee,
						@FormParam("startDate") String start,
						@FormParam("endDate") String end,
						@FormParam("idCategory") int category
						) throws ParseException {
		DBHandler db = new DBHandler();
		
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		
		java.util.Date sdate = (java.util.Date)f.parse(start);
		Timestamp startDate = new Timestamp(sdate.getTime());

		java.util.Date edate = (java.util.Date)f.parse(end);
		Timestamp endDate = new Timestamp(edate.getTime());
	
		Event ev = new Event(-1, title, promoter, location, entranceFee, startDate, endDate, category);
		
		int r = db.createEvent(ev);
		if (r == -1){
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		URI u = uriInfo.getBaseUri();
		String n = u.getPath() + "event/" + r;
		URI newEventURI = u.resolve(n);
		Event newEventJson = db.getEventById(r);
		newEventJson.setCurBaseUrl(uriInfo.getBaseUri().toString());
		return Response.created(newEventURI).entity(newEventJson).build();
	}

	@GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
	public Map<String, Object> getEventsList(@QueryParam("categories") String cat, @QueryParam("search") String searchPhrase) {
		List<Event> events = new ArrayList<Event>();
		DBHandler db = new DBHandler();
		List<Integer> catList = new ArrayList<Integer>();
		
		if (cat != null && "".equals(cat) == false){
			//convert comma-separated input to actual list
			for (String s : cat.split(","))
				catList.add(Integer.parseInt(s));
		}
		events = db.getEventList(catList, searchPhrase);

        //loop through events to give them the current URI so that we can output it in the JSON
        String baseUri = uriInfo.getBaseUri().toString();
		for (Event e : events){
    		e.setCurBaseUrl(baseUri);
        }
        Map<String,Object> root = new HashMap<String,Object>();
        Map<String,String> meta = new HashMap<String,String>();
        meta.put("count", Integer.toString(events.size()));
        root.put("meta", meta);
        root.put("data", events);
		return root;
	}

	@Path("{idEvent}")
	public EventResource getEvent(){
		return new EventResource(uriInfo, request);
	}
}
