package wgot;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "place")
public class Location {
	private int idLocation;
	private String name;
	private String city;
	private int zipCode;
	private String address;
	
	public Location(int idL, String name, String city, int zipCode, String address) {
		//when first adding to database the id should be given by the database, use -1 in constructor for this
		if (idL != -1){
			this.idLocation = idL;
		}
		this.name = name;
		this.city = city;
		this.zipCode = zipCode;
		this.address = address;
	}
	
	@JsonProperty("id")
	public int getIdLocation() {
		return idLocation;
	}
	@JsonIgnore //for adding to db
	public String getName() {
		return name;
	}
	
	@JsonProperty("name") //for outputting in json
	public Map<String,String> getNameJson(){
		Map<String,String> m = new HashMap<>();
		m.put("en", name);
		m.put("sv", "");
		m.put("fi", "");
		return m;
	}
	
	@JsonIgnore //for adding to db
	public String getCity() {
		return city;
	}
	@JsonProperty("address_locality")//for outputting in json
	public Map<String,String> getCityJson() {
		Map<String,String> m = new HashMap<>();
		m.put("en", city);
		m.put("sv", "");
		m.put("fi", "");
		return m;
	}
	
	@JsonProperty("postal_code")
	public int getZipCode() {
		return zipCode;
	}
	
	@JsonIgnore //for adding to db
	public String getAddress() {
		return address;
	}
	
	@JsonProperty("street_address")//for outputting in json
	public Map<String,String> getAddressJson() {
		Map<String,String> m = new HashMap<>();
		m.put("en", address);
		m.put("sv", "");
		m.put("fi", "");
		return m;
	}
	@JsonProperty("@type")
	public String gettype(){
		return "place";
	}
	//the following is probably stupid. TODO fix this?????
	//to get urls into the json output, the current URI has to be set from the class that actually sends the json file
	private String currentBaseUrl ="";
	public void setCurBaseUrl(String s){
		currentBaseUrl = s;
	}
	@JsonProperty("@id")
	public String getUrl(){
		String u= currentBaseUrl + "place/" + idLocation;
		return u;
	}
}
