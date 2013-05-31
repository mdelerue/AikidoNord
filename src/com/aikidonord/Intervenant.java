package com.aikidonord;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import com.aikidonord.display.IntervenantAdapter;
import com.aikidonord.metier.Animateur;
import com.aikidonord.utils.JSONRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Martc Delerue
 * Date: 25/05/13
 * Time: 16:43
 */
public class Intervenant extends ListActivity {
    ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intervenant);

        this.mProgressDialog = ProgressDialog.show(this, "Chargement",
                "Chargement",true);
                new QueryForAnimateurTask().execute(this.mProgressDialog, this, this.getApplicationContext());
    }


    /**
     * Async
     *
     * @author Marc Delerue
     */
    private class QueryForAnimateurTask extends
            AsyncTask<Object, Void, ArrayList<Animateur>> {

        private ProgressDialog mProgressDialog;
        private Activity act;
        private Context context;

        protected ArrayList<Animateur> doInBackground(Object... o) {


            this.mProgressDialog = (ProgressDialog) o[0];
            this.act = (Activity) o[1];
            this.context = (Context)o[2];

            ArrayList<Animateur> listeIntervenant = this.parseJSON(this.startQuerying());


            return listeIntervenant;

        }

        /**
         * requêtage de l'API.
         *
         * @return un JSONObject représentant la réponse de l'API
         */
        public JSONObject startQuerying() {

            JSONRequest jr = new JSONRequest();

            String url = getResources().getString(
                    R.string.api_animateurs_json);


            JSONObject jo = jr.getJSONFromUrl(url);

            return jo;

        }

        /**
         * Parse le retour JSON de l'api
         * @param jsonObject
         * @return
         */
        public ArrayList<Animateur> parseJSON(JSONObject jsonObject) {

            ArrayList<Animateur> l =new ArrayList<Animateur>();

            try {
                JSONArray array = jsonObject.getJSONArray("animateurs");

                if (array != null) {

                    for (int i=0; i<array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        l.add(new Animateur(o.getInt("id"), o.getString("nom"), null, null));

                    }    // fin parcours JSONArray

                }

            } catch (Exception _e) {

            }

            return l;
        }

        protected void onProgressUpdate(Integer... progress) {
            // setProgressPercent(progress[0]);
        }

        /**
         * Exécution à la fin du traitement
         */
        protected void onPostExecute(ArrayList<Animateur> lInter) {

             // Create items for the ListView
            IntervenantAdapter adapter = new IntervenantAdapter(this.context, R.layout.searchitem_intervenant, lInter, this.act);
            // specify the list adaptor
            setListAdapter(adapter);
            this.mProgressDialog.dismiss();



        }
    } // fin async
}