package com.mileskabal.instamiles;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ListingLink extends Activity {
	
	private ListView mylistView;
	String[] url;
	String[] titre;
	String[] commentaire;
	String[] datelink;
	String[] idlink;
	String[] infos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linksview);
		
		mylistView = (ListView) findViewById(R.id.lvView);
		
		Bundle mybundle=this.getIntent().getExtras();
		titre=mybundle.getStringArray("titre");
		url=mybundle.getStringArray("url");
		datelink=mybundle.getStringArray("date");
		idlink=mybundle.getStringArray("idlink");
		commentaire=mybundle.getStringArray("commentaire");
		infos=mybundle.getStringArray("infos");
		
		this.setTitle(infos[0]);
		
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
 
        HashMap<String, String> map;
 
        for (int i = 0; i < titre.length; i++) {
        	map = new HashMap<String, String>();
            map.put("titre", titre[i]);
            map.put("description", url[i]);
            listItem.add(map);
        }

        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.linksitem, new String[] {"titre", "description"}, new int[] {R.id.tvLinksTitre, R.id.tvLinksDescription});
 
        mylistView.setAdapter(mSchedule);        
        
        mylistView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		String urlstring = url[position];
        		Intent i = new Intent(Intent.ACTION_VIEW);
        		i.setData(Uri.parse(urlstring));
        		startActivity(i);
        	}
        });
        
        mylistView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int pos, long id) {
				// TODO Auto-generated method stub
				
				Bundle b=new Bundle();
				b.putString("positionlink", ""+pos);
				b.putString("titre", titre[pos]);
				b.putString("url", url[pos]);
				b.putString("commentaire", commentaire[pos]);
				b.putString("idlink", idlink[pos]);
				b.putString("categorie", infos[1]);
				
				Intent editLink = new Intent(ListingLink.this,EditLink.class);
				editLink.putExtras(b);
				final int idIntent=1;
				startActivityForResult(editLink,idIntent);
				return false;
			}
		});
        		
	}

	
	@Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			// [0]: pos - [1]: titre - [2]: lien - [3]: comm - [4]: id  
			String[] extraData=data.getStringArrayExtra("retourEdit");
			
			int posedit = Integer.parseInt(extraData[0]);
			titre[posedit] = extraData[1];
			url[posedit] = extraData[2];
			commentaire[posedit] = extraData[3];
			
			ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	        HashMap<String, String> map;
	        for (int i = 0; i < titre.length; i++) {
	        	map = new HashMap<String, String>();
	        	map.put("titre", titre[i]);
		        map.put("description", url[i]);	
	            listItem.add(map);
	        }

	        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.linksitem, new String[] {"titre", "description"}, new int[] {R.id.tvLinksTitre, R.id.tvLinksDescription});
	        mylistView.setAdapter(mSchedule);
		}
    }
	
	
}
