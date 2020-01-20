package wgot;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "event")
public class Event {
	
	private int idEvent;
	private String title;
	private int idPromoter;
	private int idLocation;
	private float entranceFee;
	private Timestamp startDate;
	private Timestamp endDate;
	private int idCategory;

	
	public Event(int idE, String t, int promo, int loc, float entr, Timestamp start, Timestamp end, int cat) throws ParseException{
		//when first adding to database the id should be given by the database, use -1 in constructor for this
		if (idE != -1){
			this.idEvent = idE;
		}
		this.title = t;
		this.idPromoter = promo;
		this.idLocation = loc;
		this.entranceFee = entr;
		this.startDate = start;
		this.endDate = end;
		this.idCategory = cat;
		
	}
	@JsonProperty("id")
	public int getIdEvent() {
		return idEvent;
	}
	@JsonIgnore
	public String getTitle() {
		return title;
	}
	@JsonProperty("publisher")	
	public int getIdPromoter() {
		return idPromoter;
	}
	@JsonIgnore
	public int getIdLocation() {
		return idLocation;
	}
	@JsonIgnore
	public float getEntranceFee() {
		return entranceFee;
	}
	@JsonIgnore
	public Timestamp getStartDate() {
		return startDate;
	}
	@JsonIgnore
	public Timestamp getEndDate() {
		return endDate;
	}
	@JsonProperty("category")
	public int getIdCategory() {
		return idCategory;
	}
	
	//rest is for Json only
	DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public String getstart_time(){
		return f.format(startDate.getTime());
	}
	public String getend_time(){
		return f.format(endDate.getTime());
	}
	public Map<String,String> getName(){
		Map<String,String> m = new HashMap<>();
		m.put("en", title);
		m.put("sv", "");
		m.put("fi", "");
		return m;
	}
	@JsonProperty("@type")
	public String gettype(){
		return "event";
	}
	//the following is probably stupid. TODO fix this?????
	//to get urls into the json output, the current URI has to be set from the class that actually sends the json file
	private String currentBaseUrl ="";
	public void setCurBaseUrl(String s){
		currentBaseUrl = s;
	}
	@JsonProperty("@id")
	public String getUrl(){
		String u= currentBaseUrl + "event/" + idEvent;
		return u;
	}
}
