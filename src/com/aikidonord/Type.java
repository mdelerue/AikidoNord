package com.aikidonord;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.aikidonord.display.IntervenantAdapter;
import com.aikidonord.display.TypeAdapter;
import com.aikidonord.metier.Animateur;
import com.aikidonord.utils.JSONRequest;
import com.aikidonord.utils.VerifConnexion;
import org.json.JSONArray;
import org.json.JSONObject;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Marc Delerue
 * Date: 25/05/13
 * Time: 16:43
 */
public class Type extends ActionBarActivity {
    ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_type);

        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.actionbar_titre_theme));

        if (VerifConnexion.isOnline(this)) {
            this.mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.loading),
                    getResources().getString(R.string.loading), true);
            new QueryForTypeTask().execute(this.mProgressDialog, this, this.getApplicationContext());
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getResources().getString(R.string.app_name));
            alertDialog.setMessage(getResources().getString(R.string.no_network));
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // if this button is clicked, close
                    // current activity
                    Type.this.finish();
                }
            });
            alertDialog.show();
        }
    }

    /**
     * Retour à la HP
     *
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
            this.context = (Context) o[2];

            ArrayList<String> listeType = this.parseJSON(this.startQuerying());


            return listeType;

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
                    R.string.api_types_json);


            JSONObject jo = jr.getJSONFromUrl(url + "?" + from);

            return jo;

        }

        /**
         * Parse le retour JSON de l'api
         *
         * @param jsonObject
         * @return
         */
        public ArrayList<String> parseJSON(JSONObject jsonObject) {

            ArrayList<String> l = new ArrayList<String>();

            try {
                JSONArray array = jsonObject.getJSONArray("types");

                if (array != null) {

                    for (int i = 0; i < array.length(); i++) {
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
        protected void onPostExecute(ArrayList<String> lType) {

            // Create items for the ListView
            TypeAdapter adapter = new TypeAdapter(this.context, R.layout.searchitem_type, lType, this.act);
            // specify the list adaptor
            ((ListView)findViewById(R.id.list)).setAdapter(adapter);
            this.mProgressDialog.dismiss();


        }
    } // fin async
}