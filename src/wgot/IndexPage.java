package wgot;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class IndexPage {
	  @GET
	  @Produces(MediaType.TEXT_HTML)
	  public String sayHtmlHello() {
		  return "<h2>Some instructions on how to use the API:</h2>"
		  		+ "List of events: GET request with <b>Accept: application/json</b> header to <a href=\"/wgot/rest/event/\">/wgot/rest/event</a><br />"
		  		+ "Single event: GET request with <b>Accept: application/json</b> header to <b>/wgot/rest/event/&lt;id&gt;</b><br />"
		  		+ "<font color=\"red\">New:</font> List Events By Category: GET request with <b>Accept: application/json</b> header to <b>/wgot/rest/event/?categories=&lt;categoryid1&gt;,&lt;categoryid2&gt,...</b><br />"
		  		+ "<font color=\"red\">New:</font> Search Events: GET request with <b>Accept: application/json</b> header to <b>/wgot/rest/event/?search=&lt;text&gt;</b><br />"
		  		+ "Categories and Search can be used at the same time with <b>/wgot/rest/event/?search=&lt;text&gt;&amp;categories=&lt;categoryid1&gt;,&lt;categoryid2&gt,...</b><br /><br />"
		  		+ "Add event: POST request with <b>Content-Type: application/x-www-form-urlencoded </b> header and body containing<br/>"
		  		+ "<b>title=&idPromoter=&idLocation=&entranceFee=&startDate=&endDate=&idCategory=</b><br>"
		  		+ "and date in ISO8601 format, <b>yyyy-MM-ddTHH:mm:ssZ</b> to <b>/wgot/rest/event/</b><br />"
		  		+ "use idPromoter=1, idLocation = 1 for now <br /><br />"
		  		+ "Add user: POST request with <b>Content-Type: application/x-www-form-urlencoded </b> header and body containing<br/>"
		  		+ "<b>firstName=&lastName=&userName=&email=&password=</b><br />"
		  		+ "to <b>/wgot/rest/user/</b><br /><br />"
		  		+ "To Authenticate send POST request with <b>Content-Type: application/x-www-form-urlencoded </b> header and body containing<br/>"
		  		+ "<b>userName=&password=</b><br />"
		  		+ "to <b>/wgot/rest/authentication</b><br />"
		  		+ "the server will respond with a JSON Web Token that can be used for authentication by adding it as an HTTP header to requests in this format<br />"
		  		+ "<b>Authorization: Bearer &lt;token&gt;</b><br />"
		  		+ "You can test if the auth works by sending a GET request with the header to <b>/wgot/rest/authtest</b><br /><br />"
		  		+ "To get list of Location by Promoter: GET request with <b>Accept: application/json</b> to <b>/wgot/rest/promoter/&lt;Promoter Id&gt;/place </b>, "
		  		+ "if you are logged in you can use <b>me</b> as user id to get your own locations.<br />"
		  		+ "List of all locations: GET Request with <b>Accept: application/json</b> header to <b>/wgot/rest/place</b><br />"
		  		+ "To add locations: POST request with <b>Content-Type: application/x-www-form-urlencoded</b> and body containing "
		  		+ "<b>name=&city=&zipCode=&address=</b> to <b>/wgot/rest/place/</b>. (Requires you to be logged in). <br /><br />"
		  		+ "To get you own user info: GET Request with <b>Accept: application/json</b> header to <b>/wgot/rest/user/me</b> (Requires you to be logged in). ";
		  		
	  }
}
