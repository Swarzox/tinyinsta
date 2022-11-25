package foo;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.server.spi.auth.EspAuthenticator;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;

@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "helloworld.example.com", ownerName = "helloworld.example.com", packagePath = ""))

public class InstaEndpoint {

	
	@ApiMethod(name = "userById", httpMethod = HttpMethod.GET)
	public Object userById(@Named("id") String id) {
		Query q = new Query("User")
				.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, id));
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		return (result != null && result.size() > 0) ? result.get(0).getProperty("id") : null;
	}
	
	@ApiMethod(name = "userIdByName", httpMethod = HttpMethod.GET)
	public Object userIdByName(@Named("name") String name) {
		Query q = new Query("User")
				.setFilter(new FilterPredicate("name", FilterOperator.EQUAL, name));
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		return (result != null && result.size() > 0) ? result.get(0).getProperty("id") : null;
	}
	
	@ApiMethod(name = "userPost", httpMethod = HttpMethod.GET)
	public Object userPost(@Named("id") String id) {
		Query q = new Query("Post")
				.setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, id));
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		return (result != null && result.size() > 0) ? result : null;
	}
	
	
	@ApiMethod(name = "userLikes", httpMethod = HttpMethod.GET)
	public Object userLikes(@Named("id") String id) {
		Query q = new Query("User")
				.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, id));
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		return (result != null && result.size() > 0) ? result.get(0).getProperty("likes") : null;
	}
	
	@ApiMethod(name = "isFollowing", httpMethod = HttpMethod.GET)
	public Object isFollowing(HttpServletRequest req, @Named("tocheck") String tocheck) {
		if(userById(tocheck) == null) {
			return null;
		}
		String userId = req.getSession().getAttribute("id").toString();
		Query q = new Query("User")
				.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, userId));
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		if(result == null || result.size() < 1) {
			return null;
		}
		Entity user = result.get(0);
		if(user.getProperty("subscriptions") == null) {
			return null;
		}
		ArrayList<String> subscriptions = (ArrayList<String>) user.getProperty("subscriptions");
		if(subscriptions == null || !subscriptions.contains(tocheck)) {
			return null;
		}
		return "yes";
	}
	
	public Object incrementLikePost(@Named("post") String src, @Named("increment") int increment) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction transac = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));

		try {


			Query q = new Query("Post")
					.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, src));
			PreparedQuery pq = datastore.prepare(q);
			List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
			if(result == null || !(result.size() > 0)) {
				return null;
			}
			
			Entity likesEntity = result.get(0);
			long likes = (long) likesEntity.getProperty("likes");
			likes = likes + increment;
			
			likesEntity.setProperty("likes", likes);
			datastore.put(likesEntity);
			

			transac.commit();
			return "yes";

			} finally {
				if (transac.isActive()) {
					transac.rollback();
				}
			}
	}
	
	@ApiMethod(name = "likePost", httpMethod = HttpMethod.GET)
	public Object likePost(HttpServletRequest req, @Named("post") String src) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction transac = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));

		try {

			String userId = req.getSession().getAttribute("id").toString();

			Query q = new Query("User")
					.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, userId));
			PreparedQuery pq = datastore.prepare(q);
			List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
			if(result == null || !(result.size() > 0)) {
				return null;
			}
			
			Entity user = result.get(0);
			
			ArrayList<String> likes = (ArrayList<String>) user.getProperty("likes");
			if(likes == null){
				likes = new ArrayList<String>();
			}
			
			if(likes.contains(src)) {
				return null;
			}
			
			likes.add(src);
			
			user.setProperty("likes", likes);
			datastore.put(user);
			
			incrementLikePost(src, 1);
			
			transac.commit();
			return "yes";

			} finally {
				if (transac.isActive()) {
					transac.rollback();
				}
			}
	}
	
	@ApiMethod(name = "unlikePost", httpMethod = HttpMethod.GET)
	public Object unlikePost(HttpServletRequest req, @Named("post") String src) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction transac = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));

		try {

			String userId = req.getSession().getAttribute("id").toString();

			Query q = new Query("User")
					.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, userId));
			PreparedQuery pq = datastore.prepare(q);
			List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
			if(result == null || !(result.size() > 0)) {
				return null;
			}
			
			Entity user = result.get(0);
			
			ArrayList<String> likes = (ArrayList<String>) user.getProperty("likes");
			if(likes == null){
				return null;
			}
			
			if(!likes.contains(src)) {
				return null;
			}
			
			likes.remove(src);
			
			user.setProperty("likes", likes);
			datastore.put(user);
			
			incrementLikePost(src, -1);

			transac.commit();
			return "yes";

			} finally {
				if (transac.isActive()) {
					transac.rollback();
				}
			}
	}
	
	@ApiMethod(name = "followUser", httpMethod = HttpMethod.GET)
	public Object followUser(HttpServletRequest req, @Named("tofollow") String tofollow) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction transac = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));

		try {
			if(userById(tofollow) == null) {
				return null;
			}
			String userId = req.getSession().getAttribute("id").toString();
			
			if(tofollow == userId) {
				return null;
			}
		
			Query q = new Query("User")
					.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, userId));
			PreparedQuery pq = datastore.prepare(q);
			List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
			if(result == null || !(result.size() > 0)) {
				return null;
			}
			
			Entity user = result.get(0);
			
			ArrayList<String> subscriptions = (ArrayList<String>) user.getProperty("subscriptions");
			if(subscriptions == null){
				subscriptions = new ArrayList<String>();
			}
			
			q = new Query("User")
					.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, tofollow));
			pq = datastore.prepare(q);
			result = pq.asList(FetchOptions.Builder.withDefaults());
			if(result == null || result.size() < 1) {
				return null;
			}
			
			Entity following = result.get(0);
			
			if(following == null) {
				return null;
			}
			ArrayList<String> subscribers = (ArrayList<String>) following.getProperty("subscribers");
			if(subscribers == null){
				subscribers = new ArrayList<String>();
			}
			
			if( (subscriptions.contains(tofollow) && !subscribers.contains(userId)) || (!subscriptions.contains(tofollow) && subscribers.contains(userId))) {
				subscriptions.remove(tofollow);
				subscribers.remove(userId);
				return null;
			} else if(subscriptions.contains(tofollow) || subscribers.contains(userId)) {
				return null;
			}
			
			subscriptions.add(tofollow);
			subscribers.add(userId);
			
			user.setProperty("subscriptions", subscriptions);
			following.setProperty("subscribers", subscribers);
			
			datastore.put(user);
			datastore.put(following);
			

			transac.commit();
			return "yes";

			} finally {
				if (transac.isActive()) {
					transac.rollback();
				}
			}
	}
	
	@ApiMethod(name = "unfollowUser", httpMethod = HttpMethod.GET)
	public Object unfollowUser(HttpServletRequest req, @Named("tounfollow") String tounfollow) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction transac = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));

		try {
			
			if(userById(tounfollow) == null) {
				return null;
			}
			
			String userId = req.getSession().getAttribute("id").toString();
			
			if(tounfollow == userId) {
				return null;
			}

			Query q = new Query("User")
					.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, userId));
			PreparedQuery pq = datastore.prepare(q);
			List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
			if(result == null || !(result.size() > 0)) {
				return null;
			}
			
			Entity user = result.get(0);
			
			ArrayList<String> subscriptions = (ArrayList<String>) user.getProperty("subscriptions");
			if(subscriptions == null){
				subscriptions = new ArrayList<String>();
			}
			
			q = new Query("User")
					.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, tounfollow));
			pq = datastore.prepare(q);
			result = pq.asList(FetchOptions.Builder.withDefaults());
			if(result == null || result.size() < 1) {
				return null;
			}
			Entity unfollowing = result.get(0);
			
			if(unfollowing == null) {
				return null;
			}
			ArrayList<String> subscribers = (ArrayList<String>) unfollowing.getProperty("subscribers");
			if(subscribers == null){
				return null;
			}
			
			if(subscriptions.contains(tounfollow)) {
				subscriptions.remove(tounfollow);	
			}
			if(subscribers.contains(userId)) {
				subscribers.remove(userId);	
			}
			
			user.setProperty("subscriptions", subscriptions);
			unfollowing.setProperty("subscribers", subscribers);
			
			datastore.put(user);
			datastore.put(unfollowing);
			

			transac.commit();
			return "yes";

			} finally {
				if (transac.isActive()) {
					transac.rollback();
				}
			}
	}
	
	@ApiMethod(name = "feedUser", httpMethod = HttpMethod.GET)
	public ArrayList<Entity> feedUser(HttpServletRequest req) {
		String userId = req.getSession().getAttribute("id").toString();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("User")
			.setFilter(new FilterPredicate("id", FilterOperator.EQUAL, userId));
		PreparedQuery pq = datastore.prepare(q);
		List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
		if(result == null || !(result.size() > 0)) {
			return null;
		}
		
		ArrayList<Entity> feed = new ArrayList<>();
			
		Entity user = result.get(0);
		List<String> subscriptionsId = (List<String>) user.getProperty("subscriptions");
		if(subscriptionsId == null || subscriptionsId.size() < 1){
			return null;
		}
		
		for(String subId : subscriptionsId) {
			q = new Query("Post")
					.setFilter(new FilterPredicate("owner", FilterOperator.EQUAL, subId));
			pq = datastore.prepare(q);
			result = pq.asList(FetchOptions.Builder.withDefaults());
			feed.addAll(result);
		}
		
		Collections.sort(feed, new Comparator<Entity>() {
		    public int compare(Entity o1, Entity o2) {
				Date a = (Date) o1.getProperty("date");
				Date b = (Date) o2.getProperty("date");
				int comp = a.compareTo(b);
				if (comp > 0)
				  return -1;
				else if (comp == 0)
				  return 0;
				else
				  return 1;
		    }
		});
		return feed;
	}
	
}
