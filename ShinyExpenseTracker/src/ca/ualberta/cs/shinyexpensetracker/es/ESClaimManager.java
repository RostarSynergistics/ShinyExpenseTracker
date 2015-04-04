// Code taken from: https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ESClient.java
// April 3rd, 2015

package ca.ualberta.cs.shinyexpensetracker.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.Entity;
import android.util.Log;
import ca.ualberta.cs.shinyexpensetracker.es.data.ElasticSearchResponse;
import ca.ualberta.cs.shinyexpensetracker.es.data.ElasticSearchSearchResponse;
import ca.ualberta.cs.shinyexpensetracker.es.data.Hits;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaim;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

public class ESClaimManager {
	
	// HTTP Connector
	private HttpClient httpclient = new DefaultHttpClient();

	// JSON Utilities
	private Gson gson = new Gson();
	
	private static final String RESOURCE_URI = "http://cmput301.softwareprocess.es:8080/cmput301w15t08/";
	public static final String CLAIM_INDEX = "claim/";
	public static final String SEARCH_PRETTY = "?pretty=1&q=";

	/**
	 * Consumes the POST/Insert operation of the service
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	
	public void insertClaim(ExpenseClaim claim) throws IllegalStateException, IOException{
		HttpPost httpPost = new HttpPost(RESOURCE_URI+CLAIM_INDEX+claim.getId()+SEARCH_PRETTY);
		StringEntity stringentity = null;
		try {
			stringentity = new StringEntity(gson.toJson(claim));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setHeader("Accept","application/json");

		httpPost.setEntity(stringentity);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String status = response.getStatusLine().toString();
		System.out.println(status);
		HttpEntity entity = response.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
		String output;
		System.err.println("Output from Server -> ");
		while ((output = br.readLine()) != null) {
			System.err.println(output);
		}
	}
	
	/**
	 * Consumes the Get operation of the service
	 * @param claimID
	 * @return
	 */
	public ExpenseClaim getClaim(String claimID){
		ExpenseClaim claim = null;
		try{
			HttpGet getRequest = new HttpGet(RESOURCE_URI+CLAIM_INDEX+claimID);

			getRequest.addHeader("Accept","application/json");

			HttpResponse response = httpclient.execute(getRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);

			Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<ExpenseClaim>>(){}.getType();
			ElasticSearchResponse<ExpenseClaim> esResponse = gson.fromJson(json, elasticSearchResponseType);
			claim = esResponse.getSource();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return claim;
	}
	
	/**
	 * Search by keywords
	 * @param str
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	
	public void searchClaims(String str) throws ClientProtocolException, IOException {
		HttpGet searchRequest = new HttpGet(RESOURCE_URI+SEARCH_PRETTY+java.net.URLEncoder.encode(str,"UTF-8"));
		searchRequest.setHeader("Accept","application/json");
		HttpResponse response = httpclient.execute(searchRequest);
		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = getEntityContent(response);

		Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<ExpenseClaim>>(){}.getType();
		ElasticSearchSearchResponse<ExpenseClaim> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
		System.err.println(esResponse);
		for (ElasticSearchResponse<ExpenseClaim> r : esResponse.getHits()) {
			ExpenseClaim claim = r.getSource();
			System.err.println(claim);
		}
	}
	
	/**
	 * Advanced search (logical operators)
	 * @param str
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void searchsearchClaims(String str) throws ClientProtocolException, IOException {
		HttpPost searchRequest = new HttpPost(RESOURCE_URI+SEARCH_PRETTY);
		String query = 	"{\"query\" : {\"query_string\" : {\"default_field\" : \"claims\",\"query\" : \"" + str + "\"}}}";
		StringEntity stringentity = new StringEntity(query);

		searchRequest.setHeader("Accept","application/json");
		searchRequest.setEntity(stringentity);

		HttpResponse response = httpclient.execute(searchRequest);
		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = getEntityContent(response);

		Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<ExpenseClaim>>(){}.getType();
		ElasticSearchSearchResponse<ExpenseClaim> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
		System.err.println(esResponse);
		for (ElasticSearchResponse<ExpenseClaim> r : esResponse.getHits()) {
			ExpenseClaim claim = r.getSource();
			System.err.println(claim);
		}
	}
	
	/**
	 * Delete a claim specified by the claimID
	 * @param claimID
	 * @throws IOException
	 */
	public void deleteClaim(String claimID) throws IOException {
		HttpDelete httpDelete = new HttpDelete(RESOURCE_URI+CLAIM_INDEX+claimID);
		httpDelete.addHeader("Accept","application/json");

		HttpResponse response = httpclient.execute(httpDelete);

		String status = response.getStatusLine().toString();
		System.out.println(status);

		HttpEntity entity = response.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
		String output;
		System.err.println("Output from Server -> ");
		while ((output = br.readLine()) != null) {
			System.err.println(output);
		}
	}
	
	/**
	 * Get the HTTP response and return json string
	 * @param response
	 * @return
	 * @throws IOException
	 */
	String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
	}
}