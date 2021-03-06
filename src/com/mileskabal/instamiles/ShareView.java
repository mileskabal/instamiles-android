package com.mileskabal.instamiles;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

import com.mileskabal.instamiles.R;
import com.mileskabal.instamiles.ShareView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ShareView extends Activity{
	
	HttpClient client;
	String[] idCategories;
	String idEnCours;
	TextView lienText;
	TextView titreText;
	TextView commText;
	Button valider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		
		client = new DefaultHttpClient();
		
		lienText = (TextView) findViewById(R.id.etLien);
		titreText = (TextView) findViewById(R.id.etTitre);
		commText = (TextView) findViewById(R.id.etComm);
		valider = (Button) findViewById(R.id.btValider);
		
		Intent intent = getIntent();
        if(intent.getAction().equals(Intent.ACTION_SEND)){
        	Bundle extras = intent.getExtras();
        	String namevar = extras.getString(Intent.EXTRA_TEXT);
        	lienText.setText(namevar);
        }
		
        new ReadCat().execute();
	}
	
	public void onButtonValiderClicked(View v) throws UnsupportedEncodingException {
		String querylien = URLEncoder.encode(lienText.getText().toString(), "utf-8");
		String querytitre = URLEncoder.encode(titreText.getText().toString(), "utf-8");
		String querycomm = URLEncoder.encode(commText.getText().toString(), "utf-8");
		String getstring = "&idcat="+idEnCours+"&url="+querylien+"&titre="+querytitre+"&comm="+querycomm;
		String url = "http://mywebsite.com/android.php?share=im"+getstring;
		new SaveLinkAction().execute(url);
	}
	
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	//parent.getItemAtPosition(pos).toString();
	    	idEnCours = idCategories[pos];
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	      // Do nothing.
	    }
	}
	
	
	public String savelink(String URL) throws ClientProtocolException, IOException{
    	StringBuilder url = new StringBuilder(URL);
    	HttpGet get = new HttpGet(url.toString());
    	HttpResponse r = client.execute(get);
    	int status = r.getStatusLine().getStatusCode();
    	if(status == 200){
    		HttpEntity e = r.getEntity();
    		String data = EntityUtils.toString(e);
    		return data;
    	}
    	else{
    		return null;
    	}
    }
	
	
	public class SaveLinkAction extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			try {
				String retour;
				retour = savelink(params[0]);
				return retour;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result.equals("ok")){
				finish();
			}
			else{
				AlertDialog.Builder adb = new AlertDialog.Builder(ShareView.this);
				adb.setTitle("Erreur");
				adb.setMessage(result);
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
			
		}
		
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
	
	public class ReadCat extends AsyncTask<String[], Void, String[]>{

		@Override
		protected String[] doInBackground(String[]... params) {
			try {
				JSONArray json;
				json = jsonCat("http://mywebsite.com/android.php");
				String categories[] = new String[json.length()+1];
				categories[0] = "Non class�";
				idCategories = new String[json.length()+1];
				idCategories[0] = "0";
				for (int i = 1; i < json.length()+1; i++) {
					JSONObject item = json.getJSONObject(i-1);
					if(item.getString("root").equals("0")){
						categories[i] = "---"+item.getString("text");
					}
					else{
						categories[i] = item.getString("text");
					}
					idCategories[i] = item.getString("id");
				}
				
				return categories;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			Spinner spinner = (Spinner) findViewById(R.id.spinnerCat);
			//String categories[] = {"","Android","D�veloppement"};
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShareView.this,android.R.layout.simple_spinner_item,result);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			//int i = adapter.getPosition("selected");
			//spinner.setSelection(i);
		    spinner.setAdapter(adapter);
		    spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		}
		
    }
	
	
}
