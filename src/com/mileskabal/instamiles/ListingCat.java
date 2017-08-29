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

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ListingCat extends ListActivity{
	
	HttpClient client;
	String[] categories;
	String[] idcategories;
	String[] rootcategories;
	String[] infos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bundle mybundle=this.getIntent().getExtras();
		categories=mybundle.getStringArray("categories");
		idcategories=mybundle.getStringArray("idCategories");
		rootcategories=mybundle.getStringArray("rootCategories");
		infos = new String[2];
		
		setListAdapter(new CustommAdapter<String>(ListingCat.this, android.R.layout.simple_list_item_1, categories));
		
		/*
		ListView lv = getListView();
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override 
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) { 
				onLongListItemClick(v,pos,id); 
				return true; 
			}

			private void onLongListItemClick(View v, int pos, long id) {
				AlertDialog.Builder adb = new AlertDialog.Builder(ListingCat.this);
				adb.setTitle("CATEGORIE");
				adb.setMessage("longpos:"+categories[pos]+" - "+idcategories[pos]);
				adb.setPositiveButton("Ok", null);
				adb.show();
			} 
		});
		*/
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		infos[0] = categories[position];
		infos[1] = ""+position;
		new ReadLinks().execute(idcategories[position]);
		/*
		AlertDialog.Builder adb = new AlertDialog.Builder(ListingCat.this);
		adb.setTitle("ID de la CATEGORIE");
		adb.setMessage("Id : "+idcategories[position]);
		adb.setPositiveButton("Ok", null);
		adb.show();
		*/
	}
	
	public JSONArray jsonLinks(String ID) throws ClientProtocolException, IOException, JSONException{
    	StringBuilder url = new StringBuilder("http://mywebsite.com/android.php?id=");
    	url.append(ID);
    	client = new DefaultHttpClient();
    	HttpGet get = new HttpGet(url.toString());
    	HttpResponse r = client.execute(get);
    	int status = r.getStatusLine().getStatusCode();
    	if(status == 200){
    		HttpEntity e = r.getEntity();
    		String data = EntityUtils.toString(e);
    		JSONArray links = new JSONArray(data);
    		return links;
    	}
    	else{
    		return null;
    	}
    }
	
	public class ReadLinks extends AsyncTask<String, Void, String>{
    	Bundle b=new Bundle();
    	
		@Override
		protected String doInBackground(String... params) {
			try {
				JSONArray json;
				json = jsonLinks(params[0]);
				String titre[] = new String[json.length()];
				String url[] = new String[json.length()];
				String commentaire[] = new String[json.length()];
				String date[] = new String[json.length()];
				String idlink[] = new String[json.length()];
				for (int i = 0; i < json.length(); i++) {
					JSONObject item = json.getJSONObject(i);
					titre[i] = item.getString("titre");
					url[i] = item.getString("url");
					commentaire[i] = item.getString("commentaire");
					date[i] = item.getString("date");
					idlink[i] = item.getString("id");
				}
				
				b.putStringArray("titre", titre);
				b.putStringArray("url", url);
				b.putStringArray("commentaire", commentaire);
				b.putStringArray("date", date);
				b.putStringArray("idlink", idlink);
				b.putStringArray("infos", infos);
				
				//b.putStringArray("categories", titre);
				//b.putStringArray("idCategories", url);
				//b.putStringArray("rootCategories", commentaire);
				return "link";
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
		protected void onPostExecute(String result) {
			Intent listCat = new Intent("com.mileskabal.instamiles.LISTINGLINK");
			listCat.putExtras(b);
			startActivity(listCat);
		}
	}
	
	public class CustommAdapter<E> extends ArrayAdapter<E>{

		public CustommAdapter(Context context, int textViewResourceId,E[] objects) {
			super(context, textViewResourceId, objects);
		}

		public View getView(int position, View convertView, ViewGroup parent){
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
			}

			if(convertView != null){
				if(rootcategories[position].equals("0")){
					LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
					((TextView)convertView).setTextSize(12);
					((TextView)convertView).setPadding(32, 0, 0, 0);
					/*
					BitmapDrawable bmpd = new BitmapDrawableNoMinimumSize(getResources(), R.drawable.body_background_dark);
				    bmpd.setTileModeX(TileMode.REPEAT);
				    bmpd.setTileModeY(TileMode.REPEAT);
				    convertView.setBackgroundDrawable(bmpd);
				    */
				}
				else{
					LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
					((TextView)convertView).setPadding(16, 0, 0, 0);
					//((TextView)convertView).setTextColor(Color.argb(255,51,181,229));
				}
			}
			return super.getView(position, convertView, parent);
		}
	}
	

	public class BitmapDrawableNoMinimumSize extends BitmapDrawable {

	    public BitmapDrawableNoMinimumSize(Resources res, int resId) {
	        super(res, ((BitmapDrawable)res.getDrawable(resId)).getBitmap());
	    }

	    @Override
	    public int getMinimumHeight() {
	        return 0;
	    }
	    @Override
	    public int getMinimumWidth() {
	         return 0;
	    }
	}
	

}
