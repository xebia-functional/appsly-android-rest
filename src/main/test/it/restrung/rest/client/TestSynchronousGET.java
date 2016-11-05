package it.restrung.rest.client;

import java.io.Serializable;

import it.restrung.rest.marshalling.response.JSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * This tes is an example of using the synchronous get to obtain information from 
 * an REST API.
 * @author Javier J.
 *
 */
public class TestSynchronousGET extends AndroidTestCase {

	Context context;
	
	protected void setUp() throws Exception {
		super.setUp();
		context = this.getContext();
	}

	/**
	 * Web http://www.omdbapi.com exposed an unoficial API for the
	 * Internet Movie Database
	 * This test request info of a movie from OMDBApi.
	 */
	public void testSynchronousGET_PromethousMovieInfo() {
		MovieFactory movieFactory = null;
		try {
			movieFactory = RestClientFactory.getClient().get(
					new ContextAwareAPIDelegate<MovieFactory>(context, MovieFactory.class) {
						@Override
						public void onResults(MovieFactory result) {
							fail("onResult method is not called");
						}
						@Override
						public void onError(Throwable e) {
							fail("onError method is not called");
							
						}} , "http://www.omdbapi.com/?t=prometheus", 1000);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception");
		}
		
		Movie movie = movieFactory.movie;
		assertNotNull(movie);
		assertEquals("Prometheus", movie.title);
		assertEquals("2012", movie.year);
		assertEquals("Ridley Scott", movie.director);
	}
	
	
	
	static class MovieFactory implements JSONResponse {
		Movie movie;
		
		@Override
		public void fromJSON(JSONObject jsonObject) throws JSONException {
			movie = new Movie();
			movie.title = jsonObject.getString("Title");
			movie.year = jsonObject.getString("Year");
			movie.director = jsonObject.getString("Director");
		}
	}


	static class Movie implements Serializable {
		String title;
		String year;
		String director;
	}
		
}
