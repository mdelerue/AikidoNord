package com.aikidonord;


import org.json.JSONObject;

import com.aikidonord.metier.Stage;
import com.aikidonord.parsers.ProchainStageParser;
import com.aikidonord.utils.JSONRequest;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;

public class ProchainStage extends Activity {
	
	protected ProgressDialog mProgressDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prochain_stage);
		
		
		this.mProgressDialog = ProgressDialog.show(this, "Chargement",
				"Chargement",
				true);
		new QueryForProchainStageTask().execute(this.mProgressDialog, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.prochain_stage, menu);
		return true;
	}
	
	
		
	
	/**
	 * Async
	 * @author garth
	 *
	 */
	 private class QueryForProchainStageTask extends AsyncTask<Object, Void, Stage> {
		 
		 
		 private ProgressDialog mProgressDialog;
		 private Activity activity;
		 private Stage stage;
		 
		 
	     protected Stage doInBackground(Object... o) {
	    	 
	    	 Stage stage = null;
	         
	         this.mProgressDialog = (ProgressDialog)o[0];
	         this.activity = (Activity)o[1];
	         this.stage = (Stage)o[2];
	         
	         
	         
	         ProchainStageParser psp = new ProchainStageParser(this.startQuerying());			
	         stage = psp.parseObject();

	         return stage;
				
	     }


	     
	     public JSONObject startQuerying() {

	    	 
	 		JSONRequest jr = new JSONRequest();

	 		String url = getResources().getString(
	 				R.string.api_prochain_stage_json);
	 				

	 		JSONObject jo = jr.getJSONFromUrl(url);

	 		return jo;

	 	}
	     

	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(Stage stage) {
	    	     	 
	    	 this.mProgressDialog.dismiss();
	    	 
	    	 // mettre en page
	    	 
	    	 
	     }
	 }

}
