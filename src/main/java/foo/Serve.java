package foo;
import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;

import foo.UserManagement;

@WebServlet("/serve")
public class Serve extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Object sessionId = request.getSession().getAttribute("id");
		if(sessionId == null) {
			return;
		}
		String googleId = sessionId.toString();
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("img");
		
        String description = request.getParameter("description");
        if(description == null || description.length() < 1){
            description = "Ma description...";
        }

        if(blobKeys == null || blobKeys.isEmpty()) {
            return;
        }
		BlobKey blobKey = blobKeys.get(0);
        BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
        if (blobInfo.getSize() == 0) {
            blobstoreService.delete(blobKey);
            return;
        }
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
        String url = imagesService.getServingUrl(options);
		
		UserManagement user = new UserManagement();
		String uploadUrl = user.addPost(googleId, url, description);
		response.setContentType("text/html");
		if(uploadUrl != null) {
			response.getOutputStream().println(uploadUrl);
		} else {
			response.getOutputStream().println("");
		}
		
    }
}