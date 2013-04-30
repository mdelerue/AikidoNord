package com.aikidonord;


import java.util.List;

import org.json.JSONObject;

import com.aikidonord.display.DisplayStage;
import com.aikidonord.metier.Stage;
import com.aikidonord.parsers.ListeStageParser;
import com.aikidonord.parsers.ProchainStageParser;
import com.aikidonord.utils.JSONRequest;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;


public class ProchainStage extends FragmentActivity {
	
	protected ProgressDialog mProgressDialog;
	protected ViewPager viewPager;
	protected StageAdapter sAdapter; 
	static private List<Stage> lstage;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_prochain_stage);
		
		this.viewPager = (ViewPager)findViewById(R.id.pager);
		
		
		this.mProgressDialog = ProgressDialog.show(this, "Chargement",
				"Chargement",
				true);
		new QueryForProchainStageTask().execute(this.mProgressDialog);
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
	private void displayStage( List<Stage> lstage) {
		
		/*
		DisplayStage ds = new DisplayStage(this.getApplicationContext(), R.layout.stage,
				stage,(LinearLayout)findViewById(R.id.linearLayoutProchainStage),
				this);
		*/
		this.lstage = lstage;
		
		this.sAdapter = new StageAdapter(this.getSupportFragmentManager());
		this.viewPager.setAdapter(sAdapter);
		

	}
	
			
	
	/**
	 * Async
	 * @author Marc Delerue
	 *
	 */
	 private class QueryForProchainStageTask extends AsyncTask<Object, Void, List<Stage> > {
		 
		 
		 private ProgressDialog mProgressDialog;		 
		 
		 
	     protected List<Stage> doInBackground(Object... o) {
	    	 
	    	 List<Stage> lstage = null;
	         
	         this.mProgressDialog = (ProgressDialog)o[0];
	         
	         
	         
	         
	         ListeStageParser lsp = new ListeStageParser(this.startQuerying());
	         			
	         lstage = lsp.getListeStage();

	         return lstage;
				
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
	     protected void onPostExecute(List<Stage> lstage) {
	    	     	 
	    	 this.mProgressDialog.dismiss();
	    	 
	    	 
	    	 // mise en page
	    	 ProchainStage.this.displayStage(lstage);
	    	 
	    	 
	     }
	 } // fin async
	 
	 
	 
	 /**
	  * Adapter
	  * @author garth
	  *
	  */
	 public static class StageAdapter extends FragmentStatePagerAdapter {
		 
	        public StageAdapter(FragmentManager fragmentManager) {
	            super(fragmentManager);
	        }

	        @Override
	        public int getCount() {
	            return lstage.size();
	        }

	        @Override
	        public Fragment getItem(int position) {
	            return ArrayListFragment.newInstance(position);
	        }
	    }

	 	/**
	 	 * ListFragment
	 	 * @author garth
	 	 *
	 	 */
	    public static class ArrayListFragment extends Fragment {
	        int mNum;

	        /**
	         * Create a new instance of CountingFragment, providing "num"
	         * as an argument.
	         */
	        static ArrayListFragment newInstance(int num) {
	            ArrayListFragment f = new ArrayListFragment();

	            // Supply num input as an argument.
	            Bundle args = new Bundle();
	            args.putInt("num", num);
	            f.setArguments(args);

	            return f;
	        }

	        /**
	         * When creating, retrieve this instance's number from its arguments.
	         */
	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);
	            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
	        }

	        /**
	         * The Fragment's UI is just a simple text view showing its
	         * instance number.
	         */
	        @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                Bundle savedInstanceState) {
	            View v = inflater.inflate(R.layout.stage, container, false);
	            
	            DisplayStage ds = new DisplayStage(lstage.get(mNum), v, this.getActivity());

	            
	            return ds.formatData();
	        }

	        @Override
	        public void onActivityCreated(Bundle savedInstanceState) {
	            super.onActivityCreated(savedInstanceState);
	            /*
	            setListAdapter(new ArrayAdapter<String>(getActivity(),
	                    android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
	            */
	        }

	        
	    }

}
