package wgot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "user")
public class User {
	private int idUser;
	private String firstname;
	private String lastname;
	private String username;
	private String email;
	private String password;
	private int idUserType;
	
	public User(int idU, String fname, String lname, String uname, String email, String pw, int idUT){
		//when first adding to database the id should be given by the database, use -1 in constructor for this
		if (idU != -1){
			this.idUser = idU;
		}
		this.firstname = fname;
		this.lastname = lname;
		this.username = uname;
		this.email = email;
		this.password = pw;
		//by default the user should just be a normal user, not promoter or admin
		if (idUT != -1){
			this.idUserType = idUT;
		} else {
			this.idUserType = 3; //TODO: decide what id normal user has
		}
	}
	public int getIdUser() {
		return idUser;
	}
	public String getFirstname() {
		return firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	public int getIdUserType() {
		return idUserType;
	}
}
