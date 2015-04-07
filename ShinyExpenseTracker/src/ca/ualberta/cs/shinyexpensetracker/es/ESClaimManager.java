// Code taken from: https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ESClient.java
// April 3rd, 2015

package ca.ualberta.cs.shinyexpensetracker.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ca.ualberta.cs.shinyexpensetracker.es.data.ElasticSearchResponse;
import ca.ualberta.cs.shinyexpensetracker.models.ExpenseClaimList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This class will implement all the elastic search functionality
 *
 */
public class ESClaimManager {

	// HTTP Connector
	private HttpClient httpclient = new DefaultHttpClient();

	// JSON Utilities
	private Gson gson = new Gson();

	private static final String RESOURCE_URI = "http://cmput301.softwareprocess.es:8080/cmput301w15t08/";
	public static final String CLAIM_LIST_INDEX = "claimlist/";
	public static final String CLAIMLIST = "TotalList";

	/**
	 * Adds a claimList to our server
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void insertClaimList(ExpenseClaimList claimController) throws IllegalStateException, IOException {
		HttpPost httpPost = new HttpPost(RESOURCE_URI + CLAIM_LIST_INDEX + CLAIMLIST);
		StringEntity stringentity = null;

		try {
			stringentity = new StringEntity(gson.toJson(claimController));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setHeader("Accept", "application/json");

		httpPost.setEntity(stringentity);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.wtf("Fail", "Fail");
		} catch (IOException e) {
			e.printStackTrace();
			Log.wtf("Fail", "Fail");
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
	 * Gets the saved claim list from the server
	 * 
	 * @return
	 */
	public ExpenseClaimList getClaimList() {
		ExpenseClaimList claimList = null;
		try {
			HttpGet getRequest = new HttpGet(RESOURCE_URI + CLAIM_LIST_INDEX + CLAIMLIST);

			getRequest.addHeader("Accept", "application/json");

			HttpResponse response = httpclient.execute(getRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);

			UserType elasticSearchResponseType = new TypeToken<ElasticSearchResponse<ExpenseClaimList>>() {
			}.getType();

			ElasticSearchResponse<ExpenseClaimList> esResponse = gson.fromJson(json, elasticSearchResponseType);
			claimList = esResponse.getSource();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return claimList;
	}

	/**
	 * Get the HTTP response and return json string
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		String output;
		String json = "";
		while ((output = br.readLine()) != null) {
			json += output;
		}
		return json;
	}
}