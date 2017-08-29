package com.mileskabal.instamiles;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class InstamilesLoader extends Activity {
    
	HttpClient client;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        client = new DefaultHttpClient();
        new ReadCategories().execute();
    }
    
    public JSONArray jsonCat(String URL) throws ClientProtocolException, IOException, JSONException{
    	StringBuilder url = new StringBuilder(URL);
    	HttpGet get = new HttpGet(url.toString());
    	HttpResponse r = client.execute(get);
    	int status = r.getStatusLine().getStatusCode();
    	if(status == 200){
    		HttpEntity e = r.getEntity();
    		String data = EntityUtils.toString(e);
    		JSONArray categories = new JSONArray(data);
    		return categories;
    	}
    	else{
    		return null;
    	}
    }
    
    public class ReadCategories extends AsyncTask<String[], Void, String[]>{
    	Bundle b=new Bundle();
    	
		@Override
		protected String[] doInBackground(String[]... params) {
			try {
				JSONArray json;
				json = jsonCat("http://mywebsite.com/android.php");
				String categories[] = new String[json.length()+1];
				String idCategories[] = new String[json.length()+1];
				String rootCategories[] = new String[json.length()+1];
				categories[0] = "Non Classés";
				idCategories[0] = "0";
				rootCategories[0] = "1";
				
				for (int i = 1; i < json.length()+1; i++) {
					JSONObject item = json.getJSONObject(i-1);
					categories[i] = item.getString("text");
					idCategories[i] = item.getString("id");
					rootCategories[i] = item.getString("root");
				}
				
				b.putStringArray("categories", categories);
				b.putStringArray("idCategories", idCategories);
				b.putStringArray("rootCategories", rootCategories);
				return categories;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			Intent listCat = new Intent("com.mileskabal.instamiles.LISTINGCAT");
			listCat.putExtras(b);
			startActivity(listCat);
		}
		
    }
    
    @Override
	protected void onPause() {
		super.onPause();
		finish();
	}
    
}