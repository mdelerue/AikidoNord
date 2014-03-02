/*
Copyright (C) 2014  Marc Delerue

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

package com.aikidonord.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aikidonord.R;
import com.aikidonord.display.LieuAdapter;
import com.aikidonord.utils.JSONRequest;
import com.aikidonord.utils.VerifConnexion;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class Lieu extends ListFragment {

    private WeakReference<QueryForLieuTask> asyncTaskWeakRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_lieu, null /*container, false*/);


        View rlLoading = view.findViewById(R.id.loadingPanel);
        //View listView = view.getListView();

        if (VerifConnexion.isOnline(this.getActivity())) {
            rlLoading.setVisibility(View.VISIBLE);

            // on va fair l'impasse la dessus vu que je ne suis pas bien sûr
            // de la manière dont il faut opérer tant que la vue n'a pas été renvoyée.
            //listView.setVisibility(View.GONE);
            this.lancementAsync();
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();
            alertDialog.setTitle(getResources().getString(R.string.app_name));
            alertDialog.setMessage(getResources().getString(R.string.no_network));
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // if this button is clicked, close
                    // current activity
                    Lieu.this.getActivity().finish();
                }
            });
            alertDialog.show();
        }


        return view;
    }


    /**
     * Das subtilité pour faire de l'async dans des fragments
     */
    private void lancementAsync() {
        QueryForLieuTask asyncTask = new QueryForLieuTask(this);
        this.asyncTaskWeakRef = new WeakReference<QueryForLieuTask>(asyncTask );
        asyncTask.execute(this);
    }



    /**
     * Async
     *
     * @author Marc Delerue
     */

    private static class QueryForLieuTask extends
            AsyncTask<Object, Void, ArrayList<String>> {


        private Activity act;
        private Context context;
        private ListFragment lFragment;

        private WeakReference<Lieu> fragmentWeakRef;

        /**
         * Et oui, il y a un constructeur...
         * @param fragment
         */
        private QueryForLieuTask (Lieu fragment) {
            this.fragmentWeakRef = new WeakReference<Lieu>(fragment);
        }

        protected ArrayList<String> doInBackground(Object... o) {

            this.lFragment = (ListFragment) o[0];
            this.act = this.lFragment.getActivity();
            this.context = this.lFragment.getActivity().getApplicationContext();

            ArrayList<String> listeLieu = this.parseJSON(this.startQuerying());


            return listeLieu;

        }

        public JSONObject startQuerying() {

            JSONRequest jr = new JSONRequest();


            String from = this.act.getResources().getString(R.string.api_param_from);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
            from += "=" + sdf.format(new java.util.Date());

            String url = this.act.getResources().getString(
                    R.string.api_lieux_json);


            JSONObject jo = jr.getJSONFromUrl(url + "?" + from);

            return jo;

        }

        //Parse le retour JSON de l'api
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

        //
        // Exécution à la fin du traitement
        //
        protected void onPostExecute(ArrayList<String> lLieu) {

            // Create items for the ListView
            LieuAdapter adapter = new LieuAdapter(this.context, R.layout.searchitem_lieu, lLieu, this.act);

            // on change l'affichage
            this.act.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            this.lFragment.getListView().setVisibility(View.VISIBLE);

            // specify the list adaptor
            this.lFragment.getListView().setAdapter(adapter);


        }
    } // fin async




}