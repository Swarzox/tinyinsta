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

import java.util.*;

public class UserManagement {

	public Entity addUser(@Named("id") String id, @Named("name") String name) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction transac = datastore.beginTransaction();
		try {

			Query q = new Query("User").setFilter(new FilterPredicate("id", FilterOperator.EQUAL, id));
			PreparedQuery pq = datastore.prepare(q);
			List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
			if(result != null && result.size() > 0){
				return null;
			}

			Entity e = new Entity("User");
			e.setProperty("id", id);
			e.setProperty("name", name);
			e.setProperty("subscribers", new ArrayList<String>());
			e.setProperty("subscriptions", new ArrayList<String>());
			e.setProperty("likes", new ArrayList<String>());
			e.setProperty("date", new Date());
			datastore.put(e);

			transac.commit();
			return e;

		} finally {
			if (transac.isActive()) {
				transac.rollback();
			}
		}
	}

	public String addPost(@Named("id") String id, @Named("url") String url, @Named("description") String description) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction transac = datastore.beginTransaction();
		try {

			Query q = new Query("Post").setFilter(new FilterPredicate("url", FilterOperator.EQUAL, url));
			PreparedQuery pq = datastore.prepare(q);
			List<Entity> result = pq.asList(FetchOptions.Builder.withDefaults());
			if(result != null && result.size() > 0){
				return null;
			}

			Entity e = new Entity("Post");
			String uniqueID = UUID.randomUUID().toString();
			e.setProperty("owner", id);
			e.setProperty("id", uniqueID);
			e.setProperty("url", url);
			e.setProperty("likes", 0);
			e.setProperty("description", description);
			e.setProperty("date", new Date());
			datastore.put(e);

			transac.commit();
			return url;

		} finally {
			if (transac.isActive()) {
				transac.rollback();
			}
		}
	}	

}
