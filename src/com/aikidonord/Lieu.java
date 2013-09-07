package com.aikidonord;

/*

Copyright (C) 2013  Marc Delerue

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.aikidonord.display.LieuAdapter;
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
 */
public class Lieu extends ActionBarActivity {
    ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lieu);

        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.actionbar_titre_lieu));

        if (VerifConnexion.isOnline(this)) {
            this.mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.loading),
                    getResources().getString(R.string.loading), true);
            new QueryForLieuTask().execute(this.mProgressDialog, this, this.getApplicationContext());
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
                    Lieu.this.finish();
                }
            });
            alertDialog.show();
        }
    }




    /**
     * Async
     *
     * @author Marc Delerue
     */
    private class QueryForLieuTask extends
            AsyncTask<Object, Void, ArrayList<String>> {

        private ProgressDialog mProgressDialog;
        private Activity act;
        private Context context;

        protected ArrayList<String> doInBackground(Object... o) {


            this.mProgressDialog = (ProgressDialog) o[0];
            this.act = (Activity) o[1];
            this.context = (Context) o[2];

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
         *
         * @param jsonObject
         * @return
         */
        public ArrayList<String> parseJSON(JSONObject jsonObject) {

            ArrayList<String> l = new ArrayList<String>();

            try {
                JSONArray array = jsonObject.getJSONArray("lieux");

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
        protected void onPostExecute(ArrayList<String> lLieu) {

            // Create items for the ListView
            LieuAdapter adapter = new LieuAdapter(this.context, R.layout.searchitem_lieu, lLieu, this.act);
            // specify the list adaptor
            ((ListView)findViewById(R.id.list)).setAdapter(adapter);
            this.mProgressDialog.dismiss();


        }
    } // fin async
}