package foo;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.Collections;

import foo.UserManagement;

@WebServlet("/connection")
public class Connection extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
        String token = request.getParameter("token");
		HttpSession session = request.getSession();
		Object sessionId = session.getAttribute("id");
		String statusMessage = "{\"session\": false}";
		if(sessionId != null) {
			String sessionString = session.getAttribute("id").toString();
			statusMessage = "{\"session\": true, \"id\": \""+sessionString+"\"}";
		} 
		else if (token !=null){
			GooglePublicKeysManager manager = new GooglePublicKeysManager(new NetHttpTransport(), new GsonFactory());

			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(manager)
					.setAudience(Collections.singletonList("851071215458-a6qv6vpm4ljs5jli8j7n4fnk5aqfs60h.apps.googleusercontent.com"))
					.build();
					
			String credentials = token;
			try {
				GoogleIdToken idToken = verifier.verify(credentials);
				if (idToken != null) {
					Payload payload = idToken.getPayload();

					String userId = payload.getSubject();
					String name = (String) payload.get("name");

					UserManagement user = new UserManagement();
					user.addUser(userId, name);
					session.setAttribute("id", userId);
					statusMessage = "{\"session\": true, \"id\": \""+userId+"\"}";
				}
				else {
					System.out.println("token invalide");
				}	
			} catch (Exception e) {
			}
		}
		response.getWriter().write(statusMessage);
		return;
    }

}