package wgot;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;

import io.jsonwebtoken.*;

@Path("/authentication")
public class Authentication {
	
	@POST
	@Produces("application/json")
	@Consumes("application/x-www-form-urlencoded")
	public Response startSession(@FormParam("userName") String username, @FormParam("password") String password){
		try{
		authenticate(username, password);
		String token = issueToken(username);
		return Response.ok(token).build();
		} catch (Exception e) {
		return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	private void authenticate(String username, String password) throws Exception {
		DBHandler db = new DBHandler();
		if(db.verifyPassword(username, password) == false){
			throw new Exception();
		}
		
	}
	
	private String issueToken(String username) {
		//creates a JSON Web Token that contains the username
		SignatureAlgorithm signAlg = SignatureAlgorithm.HS256;
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("supersecretpassword"); //TODO: better way to save password
	    Key signKey = new SecretKeySpec(apiKeySecretBytes, signAlg.getJcaName());
		
		JwtBuilder builder = Jwts.builder().setSubject(username).signWith(signAlg,signKey);
		
		return builder.compact();
	}
}

