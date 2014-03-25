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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.aikidonord.R;
import com.aikidonord.display.DateAdapter;
import com.aikidonord.utils.JSONRequest;
import com.aikidonord.utils.VerifConnexion;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class FragmentDate extends ListFragment {

    private WeakReference<QueryForDateTask> asyncTaskWeakRef;

    // occurence de l'interface qui va communiquer avec l'activité
    private OnDateSelectedListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_date, null /*container, false*/);


        View rlLoading = view.findViewById(R.id.loadingPanel);
        //View listView = view.getListView();

        if (VerifConnexion.isOnline(this.getActivity())) {
            rlLoading.setVisibility(View.VISIBLE);

            // on va fair l'impasse là dessus vu que je ne suis pas bien sûr
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
                    FragmentDate.this.getActivity().finish();
                }
            });
            alertDialog.show();
        }


        return view;
    }

    @Override
    /**
     * Au clic sur un élément de la liste.
     */
    public void onListItemClick(ListView l, View v, int position, long id) {

        String date = (String) l.getItemAtPosition(position);

        FragmentManager fm = getFragmentManager();

        if (fm.findFragmentById(R.id.fragment_prochains_stages) != null) {

            // affichage tablette
            mCallback.onDateSelected(date);


        } else {

            // dans le cas de l'affichage téléphone classique

            Intent i = new Intent(this.getActivity(), com.aikidonord.ProchainsStages.class);
            // données à envoyer à l'activité
            Bundle b = new Bundle();
            b.putString("type", "date");
            b.putString("data", String.valueOf(date));
            i.putExtras(b);
            this.getActivity().startActivity(i);

        }


    }



    // Container Activity must implement this interface
    public interface OnDateSelectedListener {
        public void onDateSelected(String lieu);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDateSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLieuSelectedListener");
        }
    }


    /**
     * Das subtilité pour faire de l'async dans des fragments
     */
    private void lancementAsync() {
        QueryForDateTask asyncTask = new QueryForDateTask(this);
        this.asyncTaskWeakRef = new WeakReference<QueryForDateTask>(asyncTask);
        asyncTask.execute(this);
    }


    /**
     * Async
     *
     * @author Marc Delerue
     */

    private static class QueryForDateTask extends
            AsyncTask<Object, Void, ArrayList<String>> {


        private Activity act;
        private Context context;
        private ListFragment lFragment;

        private WeakReference<FragmentDate> fragmentWeakRef;

        /**
         * Et oui, il y a un constructeur...
         *
         * @param fragment
         */
        private QueryForDateTask(FragmentDate fragment) {
            this.fragmentWeakRef = new WeakReference<FragmentDate>(fragment);
        }

        protected ArrayList<String> doInBackground(Object... o) {

            this.lFragment = (ListFragment) o[0];
            this.act = this.lFragment.getActivity();
            this.context = this.lFragment.getActivity().getApplicationContext();

            ArrayList<String> listeDate = this.parseJSON(this.startQuerying());


            return listeDate;

        }

        public JSONObject startQuerying() {

            JSONRequest jr = new JSONRequest();


            String from = this.act.getResources().getString(R.string.api_param_from);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
            from += "=" + sdf.format(new java.util.Date());

            String url = this.act.getResources().getString(
                    R.string.api_dates_json);


            JSONObject jo = jr.getJSONFromUrl(url + "?" + from);

            return jo;

        }

        //Parse le retour JSON de l'api
        public ArrayList<String> parseJSON(JSONObject jsonObject) {

            ArrayList<String> l = new ArrayList<String>();

            try {
                JSONArray array = jsonObject.getJSONArray("dates");

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
        protected void onPostExecute(ArrayList<String> lDate) {

            // Create items for the ListView
            DateAdapter adapter = new DateAdapter(this.context, R.layout.searchitem_date, lDate, this.act);

            // on change l'affichage
            this.act.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            this.lFragment.getListView().setVisibility(View.VISIBLE);

            // specify the list adaptor
            this.lFragment.getListView().setAdapter(adapter);


        }
    } // fin async


}