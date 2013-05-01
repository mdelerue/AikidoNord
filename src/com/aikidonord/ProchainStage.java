package com.aikidonord;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.aikidonord.display.DisplayStage;
import com.aikidonord.metier.Stage;
import com.aikidonord.parsers.ListeStageParser;
import com.aikidonord.utils.JSONRequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class ProchainStage extends FragmentActivity {

	protected ProgressDialog mProgressDialog;
	protected ViewPager viewPager;
	protected StageAdapter sAdapter;
	static private ArrayList<Stage> lstage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_prochain_stage);

		this.viewPager = (ViewPager) findViewById(R.id.pager);

		this.mProgressDialog = ProgressDialog.show(this, "Chargement",
				"Chargement", true);
		new QueryForProchainStageTask().execute(this.mProgressDialog);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.prochain_stage, menu);
		return true;
	}

	/**
	 * Surcharge de la sauvegarde
	 * Sauvegarde le la liste de stage à la destruction
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		
		savedInstanceState.putParcelableArrayList("stages", this.lstage);
		
		super.onSaveInstanceState(savedInstanceState);
	}
	
	/**
	 * surcharge de la restauration
	 * restaure la liste de stage et appelle l'affichage
	 */
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    // Restore state members from saved instance
	   ArrayList<Stage> stage = savedInstanceState.getParcelableArrayList("stages");
	   this.displayStage(stage);
	   
	}

	/**
	 * Mise en page du stage
	 * 
	 * @param stage
	 *            le stage à mettre en page
	 */
	private void displayStage(ArrayList<Stage> lstage) {

		this.lstage = lstage;

		this.sAdapter = new StageAdapter(this.getSupportFragmentManager());
		this.viewPager.setAdapter(sAdapter);

	}

	/**
	 * Async
	 * 
	 * @author Marc Delerue
	 * 
	 */
	private class QueryForProchainStageTask extends
			AsyncTask<Object, Void, ArrayList<Stage>> {

		private ProgressDialog mProgressDialog;

		protected ArrayList<Stage> doInBackground(Object... o) {

			ArrayList<Stage> lstage = null;

			this.mProgressDialog = (ProgressDialog) o[0];

			ListeStageParser lsp = new ListeStageParser(this.startQuerying());

			lstage = lsp.getListeStage();

			return lstage;

		}

		/**
		 * requêtage de l'API.
		 * 
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
			// setProgressPercent(progress[0]);
		}

		/**
		 * Exécution à la fin du traitement
		 */
		protected void onPostExecute(ArrayList<Stage> lstage) {

			this.mProgressDialog.dismiss();

			// mise en page
			ProchainStage.this.displayStage(lstage);

		}
	} // fin async

	/**
	 * Adapter
	 * 
	 * @author Marc Delerue
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
			return StageFragment.newInstance(position);
		}
	} // fin adapter

	/**
	 * StageFragment
	 * 
	 * @author garth
	 * 
	 */
	public static class StageFragment extends Fragment {
		int mNum;

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static StageFragment newInstance(int num) {
			StageFragment f = new StageFragment();

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
		 * UI - Appel à un DisplayStage qui crée la vue à renvoyer
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.stage, container, false);

			DisplayStage ds = new DisplayStage(lstage.get(mNum), v,
					this.getActivity());

			return ds.formatData();
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}

	} // fin StageFragment

}
