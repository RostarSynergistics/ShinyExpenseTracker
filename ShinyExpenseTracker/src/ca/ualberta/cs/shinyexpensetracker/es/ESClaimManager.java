// Code take from: https://github.com/ramish94/AndroidElasticSearch, March 29, 2015

package ca.ualberta.cs.shinyexpensetracker.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.util.Log;
import ca.ualberta.cs.shinyexpensetracker.es.data.Hits;
import ca.ualberta.cs.shinyexpensetracker.es.data.SearchHit;
import ca.ualberta.cs.shinyexpensetracker.es.data.SearchResponse;
import ca.ualberta.cs.shinyexpensetracker.es.data.SimpleSearchCommand;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;

public class ESClaimManager implements IClaimManager {
	
	private static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t08/claim/_search";
	private static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t08/claim/";
	private static final String TAG = "ClaimSearch";

	private Gson gson;

	public ESClaimManager() {
		gson = new Gson();
	}
	
	/**
	 * Get claims with the specified search string. If the search does not
	 * specify fields, it searches on all the fields.
	 */
	public List<ExpenseClaim> searchClaim(String searchString, String field) {
		List<ExpenseClaim> result = new ArrayList<ExpenseClaim>();

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost; 
		try {
			httpPost = createSearchRequest(searchString, field);

			HttpResponse response;
			
			response = httpClient.execute(httpPost);
			SearchResponse<ExpenseClaim> sr = parseSearchResponse(response);
			Hits<ExpenseClaim> hits = sr.getHits();
			result.add(hits.getHits().get(1).getSource());
			
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			} catch (ClientProtocolException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		return result;
	}
	
	/**
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	
	private SearchResponse<ExpenseClaim> parseSearchResponse(
			HttpResponse response) throws IOException {
		String json;
		json = getEntityContent(response);

		Type searchResponseType = new TypeToken<SearchResponse<ExpenseClaim>>() {
		}.getType();
		
		SearchResponse<ExpenseClaim> esResponse = gson.fromJson(json, searchResponseType);

		return esResponse;
	}
	
	/**
	 * Creates a search request from a search string and a field
	 * @throws UnsupportedEncodingException 
	 */
	private HttpPost createSearchRequest(String searchString, String field) throws UnsupportedEncodingException {
		HttpPost searchRequest = new HttpPost(SEARCH_URL);

		String[] fields = null;
		if (field != null) {
			fields = new String[1];
			fields[0] = field;
		}
		
		SimpleSearchCommand command = new SimpleSearchCommand(searchString,	fields);
		
		String query = command.getJsonCommand();
		Log.i(TAG, "Json command: " + query);

		StringEntity stringEntity;
		stringEntity = new StringEntity(query);

		searchRequest.setHeader("Accept", "application/json");
		searchRequest.setEntity(stringEntity);

		return searchRequest;
	}
	
	/**
	 * Return claim with a specific id
	 */
	public ExpenseClaim getClaim(int id) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(RESOURCE_URL + id);

		HttpResponse response;

		try {
			response = httpClient.execute(httpGet);
			SearchHit<ExpenseClaim> sr = parseClaimHit(response);
			return sr.getSource();

		} catch (Exception e) {
			e.printStackTrace();
		} 

		return null;
	}
	
	/**
	 * Parses a claim hit from JSON
	 * @param response
	 * @return
	 */

	private SearchHit<ExpenseClaim> parseClaimHit(
			HttpResponse response) {
		try {
			String json = getEntityContent(response);
			Type searchHitType = new TypeToken<SearchHit<ExpenseClaim>>() {}.getType();
			
			SearchHit<ExpenseClaim> sr = gson.fromJson(json, searchHitType);
			return sr;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Add a new claim
	 */
	public void addClaim(ExpenseClaim expenseClaim) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPost addRequest = new HttpPost(RESOURCE_URL + expenseClaim.getId());

			StringEntity stringEntity = new StringEntity(gson.toJson(expenseClaim));
			addRequest.setEntity(stringEntity);
			addRequest.setHeader("Accept", "application/json");

			HttpResponse response = httpClient.execute(addRequest);
			String status = response.getStatusLine().toString();
			Log.i(TAG, status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a Claim at a given id
	 */
	public void deleteClaim(int id) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpDelete deleteRequest = new HttpDelete(RESOURCE_URL + id);
			deleteRequest.setHeader("Accept", "application/json");

			HttpResponse response = httpClient.execute(deleteRequest);
			String status = response.getStatusLine().toString();
			Log.i(TAG, status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets content from an HTTP response
	 */
	public String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}
		
}