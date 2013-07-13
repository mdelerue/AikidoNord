package com.aikidonord;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.aikidonord.display.IntervenantAdapter;
import com.aikidonord.display.LieuAdapter;
import com.aikidonord.display.TypeAdapter;
import com.aikidonord.metier.Animateur;
import com.aikidonord.utils.JSONRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Marc Delerue
 */
public class Lieu extends ListActivity {
    ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lieu);

        this.mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.loading),
                getResources().getString(R.string.loading),true);
        new QueryForTypeTask().execute(this.mProgressDialog, this, this.getApplicationContext());
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
     * Async
     *
     * @author Marc Delerue
     */
    private class QueryForTypeTask extends
            AsyncTask<Object, Void, ArrayList<String>> {

        private ProgressDialog mProgressDialog;
        private Activity act;
        private Context context;

        protected ArrayList<String> doInBackground(Object... o) {


            this.mProgressDialog = (ProgressDialog) o[0];
            this.act = (Activity) o[1];
            this.context = (Context)o[2];

            ArrayList<String> listeLieu = this.parseJSON(this.startQuerying());


            return listeLieu;

        }

        /**
         * requêtage de l'API.
         *
         * @return un JSONObject représentant la réponse de l'API
         */
        public JSONObject startQuerying() {

            JSONRequest jr = new JSONRequest();


            String from = getResources().getString(R.string.api_param_from);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
            from += "=" + sdf.format(new Date());

            String url = getResources().getString(
                    R.string.api_lieux_json);


            JSONObject jo = jr.getJSONFromUrl(url + "?" + from);

            return jo;

        }

        /**
         * Parse le retour JSON de l'api
         * @param jsonObject
         * @return
         */
        public ArrayList<String> parseJSON(JSONObject jsonObject) {

            ArrayList<String> l = new ArrayList<String>();

            try {
                JSONArray array = jsonObject.getJSONArray("lieux");

                if (array != null) {

                    for (int i=0; i<array.length(); i++) {
                        String type = array.getString(i);
                        l.add(type);

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
        protected void onPostExecute(ArrayList<String> lLieu) {

            // Create items for the ListView
            LieuAdapter adapter = new LieuAdapter(this.context, R.layout.searchitem_lieu, lLieu, this.act);
            // specify the list adaptor
            setListAdapter(adapter);
            this.mProgressDialog.dismiss();



        }
    } // fin async
}