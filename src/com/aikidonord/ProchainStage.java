package com.aikidonord;


import org.json.JSONObject;

import com.aikidonord.display.DisplayStage;
import com.aikidonord.metier.Stage;
import com.aikidonord.parsers.ProchainStageParser;
import com.aikidonord.utils.JSONRequest;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.widget.LinearLayout;


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
	 * Mise en page du stage
	 * @param stage le stage à mettre en page
	 */
	private void displayStage( Stage stage) {
				
		DisplayStage ds = new DisplayStage(this.getApplicationContext(), R.layout.stage,
				stage,(LinearLayout)findViewById(R.id.linearLayoutProchainStage),
				this);

	}
	
			
	
	/**
	 * Async
	 * @author Marc Delerue
	 *
	 */
	 private class QueryForProchainStageTask extends AsyncTask<Object, Void, Stage> {
		 
		 
		 private ProgressDialog mProgressDialog;
		 private Activity activity;
		 
		 
		 
	     protected Stage doInBackground(Object... o) {
	    	 
	    	 Stage stage = null;
	         
	         this.mProgressDialog = (ProgressDialog)o[0];
	         this.activity = (Activity)o[1];
	         
	         
	         
	         
	         ProchainStageParser psp = new ProchainStageParser(this.startQuerying());			
	         stage = psp.parseObject();

	         return stage;
				
	     }


	     /**
	      * requêtage de l'API.
	      * @return un JSONObject représentant la réponse de l'API
	      */
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

	     /**
	      * Exécution à la fin du traitement
	      */
	     protected void onPostExecute(Stage stage) {
	    	     	 
	    	 this.mProgressDialog.dismiss();
	    	 
	    	 // mise en page
	    	 ProchainStage.this.displayStage(stage);
	    	 
	    	 
	     }
	 }

}
