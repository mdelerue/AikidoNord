package com.aikidonord;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.widget.TextView;
import org.json.JSONObject;

import com.aikidonord.display.DisplayStage;
import com.aikidonord.metier.Stage;
import com.aikidonord.parsers.ListeStageParser;
import com.aikidonord.utils.DrawableOperation;
import com.aikidonord.utils.JSONRequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ProchainsStages extends FragmentActivity {

	static private ArrayList<Stage> lstage;
	protected ProgressDialog mProgressDialog;
	protected ViewPager viewPager;
	protected StageAdapter sAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_prochain_stage);
		
		this.viewPager = (ViewPager) findViewById(R.id.pager);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            this.mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.loading),
                    getResources().getString(R.string.loading), true);
            // si l'ouverture de l'activité vient d'un Intent (ce qui devrait toujours être le cas)
            String type = b.getString("type");
            String data = b.getString("data");

            new QueryForProchainStageTask().execute(this.mProgressDialog, this, type, data);
        }  else if (savedInstanceState == null) {
            this.mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.loading),
                    getResources().getString(R.string.loading), true);
		    // si on n'est pas dans le cas d'une restauration, on exécute la requête
		    // requête par défaut
		    new QueryForProchainStageTask().execute(this.mProgressDialog, this, null, null);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.prochain_stage, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        // Selon l'id, on déclenche une action
        switch (item.getItemId()) {
           case R.id.reload:
               this.mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.loading),
                       getResources().getString(R.string.loading), true);
       			new QueryForProchainStageTask().execute(this.mProgressDialog, this);
       			return true;
        }
        
        return false;
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
	
	@Override
	  public Object onRetainCustomNonConfigurationInstance() {
	    return lstage;
	}

    /**
     * Mise en page de la liste de stage
     * @param lstage
     */
	private void displayStage(ArrayList<Stage> lstage) {

		this.lstage = lstage;

        if (lstage.size() > 0) {
            // s'il y a des résultats

            ((ViewPager)findViewById(R.id.pager)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.tv_noresult)).setVisibility(View.GONE);

		    this.sAdapter = new StageAdapter(this.getSupportFragmentManager());
		    this.viewPager.setAdapter(sAdapter);

        } else {
            ((ViewPager)findViewById(R.id.pager)).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.tv_noresult)).setVisibility(View.VISIBLE);
        }

	}

    /**
     * Retour à la HP
     * @param v
     */
    public void retour_accueil(View v) {
        Intent intent = new Intent(this, AikidoNord.class);
        startActivity(intent);
    }

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
			//ProchainsStages.indexStage = mNum;

			return ds.formatData();
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}

	} // fin StageFragment

	/**
	 * Async
	 *
	 * @author Marc Delerue
	 *
	 */
	private class QueryForProchainStageTask extends
			AsyncTask<Object, Void, ArrayList<Stage>> {

		private ProgressDialog mProgressDialog;
		private Activity act;
        // type de recherche
        private String type;
        // data associée
        private String data;

		protected ArrayList<Stage> doInBackground(Object... o) {

			ArrayList<Stage> lstage = null;

			this.mProgressDialog = (ProgressDialog) o[0];
			this.act = (Activity) o[1];
            this.type = (String) o[2];
            this.data = (String) o[3];



			ListeStageParser lsp = new ListeStageParser(this.startQuerying());

			lstage = lsp.getListeStage();

            /*
            System.out.println("AIKIDONORD : " + lstage);
            System.out.println("AIKIDONORD : " + lstage.size());
            System.out.println("AIKIDONORD : data : " + this.data);
            */
			for (Stage s : lstage) {
				// url de l'image
				String src = s.getImg();

				if (src != null &&!src.equals("")) {

					// on tente de la récupérer dans le stockage
					Bitmap bmp = DrawableOperation.getBitmapFromStorage(s.getId(), s.getDateDebut(), this.act.getApplicationContext() );

					if (bmp == null) {
						// si ce n'est pas sur le disque, on l'écrit
						DrawableOperation.saveThumbnailOnStorage(src, s.getId(), s.getDateDebut(), this.act.getApplicationContext());
					}
				}
			}

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
			String from = getResources().getString(R.string.api_param_from);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
			from += "=" + sdf.format(new Date());

            String paramSupplementaire = "";
            if (type != null) {
                if (type.equals("intervenant")) {
                    paramSupplementaire = "&" + getResources().getString(R.string.api_param_anim) + "=" + this.data;
                } else if (type.equals("type")) {
                    try {
                    paramSupplementaire = "&" + getResources().getString(R.string.api_param_type) + "=" + URLEncoder.encode(this.data, "UTF-8");
                    } catch (UnsupportedEncodingException _uee) {

                    }

                }  else if (type.equals("lieu")) {
                    try {
                        paramSupplementaire = "&" + getResources().getString(R.string.api_param_lieu) + "=" + URLEncoder.encode(this.data, "UTF-8");
                    } catch (UnsupportedEncodingException _uee) {

                    }

                }
            }

            //System.out.println("AIKODONORD : " + url + "?" + from + paramSupplementaire);
			JSONObject jo = jr.getJSONFromUrl(url + "?" + from + paramSupplementaire);

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
			ProchainsStages.this.displayStage(lstage);

		}
	} // fin async

}
