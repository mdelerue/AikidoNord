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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aikidonord.R;
import com.aikidonord.display.DisplayStage;
import com.aikidonord.metier.Stage;
import com.aikidonord.parsers.ListeStageParser;
import com.aikidonord.utils.DrawableOperation;
import com.aikidonord.utils.JSONRequest;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class ProchainsStages extends Fragment {


    static private ArrayList<Stage> lstage;
    private WeakReference<QueryForProchainStageTask> asyncTaskWeakRef;

    protected ViewPager viewPager;
    protected StageAdapter sAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_prochain_stage, container, false);

        View rlLoading = view.findViewById(R.id.loadingPanel);
        View pager = view.findViewById(R.id.pager);

        this.viewPager = (ViewPager) view.findViewById(R.id.pager);

        Bundle b = getArguments();

        if (b != null) {

            rlLoading.setVisibility(View.VISIBLE);
            pager.setVisibility(View.GONE);

            // si l'ouverture de l'activité vient d'un Intent (ce qui devrait toujours être le cas)
            String type = b.getString("type");
            String data = b.getString("data");

            this.lancementAsync(type, data);


        } else if (savedInstanceState == null) {
            rlLoading.setVisibility(View.VISIBLE);
            pager.setVisibility(View.GONE);
            // si on n'est pas dans le cas d'une restauration, on exécute la requête
            // requête par défaut
            this.lancementAsync(null, null);
        }



        return view;
    }


    public static ProchainsStages newInstance(Bundle b) {
        ProchainsStages f = new ProchainsStages();

        // Supply index input as an argument.
        f.setArguments(b);

        return f;
    }


    /**
     * Mise en page de la liste de stage
     *
     * @param lstage liste des objets stages
     */
    private void displayStage(ArrayList<Stage> lstage) {

        this.lstage = lstage;

        if (lstage.size() > 0) {
            // s'il y a des résultats

            ((ViewPager) this.getActivity().findViewById(R.id.pager)).setVisibility(View.VISIBLE);
            ((TextView) this.getActivity().findViewById(R.id.tv_noresult)).setVisibility(View.GONE);
            ((RelativeLayout) this.getActivity().findViewById(R.id.loadingPanel)).setVisibility(View.GONE);

            this.sAdapter = new StageAdapter(getFragmentManager(), lstage);
            this.viewPager.setAdapter(sAdapter);

        } else {
            this.getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            ((ViewPager) this.getActivity().findViewById(R.id.pager)).setVisibility(View.GONE);
            ((TextView) this.getActivity().findViewById(R.id.tv_noresult)).setVisibility(View.VISIBLE);
        }

    }

    /**
     * Das subtilité pour faire de l'async dans des fragments
     */
    public void lancementAsync(String type, String data) {

        QueryForProchainStageTask asyncTask = new QueryForProchainStageTask(this);
        this.asyncTaskWeakRef = new WeakReference<QueryForProchainStageTask>(asyncTask);
        asyncTask.execute(this, type, data);

    }


    /**
     * Adapter
     *
     * @author Marc Delerue
     */
    public static class StageAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Stage> ls;

        public StageAdapter(FragmentManager fragmentManager, ArrayList<Stage> lsp) {
            super(fragmentManager);
            this.ls = lsp;
        }

        @Override
        public int getCount() {
            return this.ls.size();
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
                    this.getActivity(), mNum, lstage.size());
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
     */

    private static class QueryForProchainStageTask extends
            AsyncTask<Object, Void, ArrayList<Stage>> {


        private Activity act;
        private ProchainsStages fragment;
        // type de recherche
        private String type;
        // data associée
        private String data;

        private WeakReference<ProchainsStages> fragmentWeakRef;

        /**
         * Et oui, il y a un constructeur...
         *
         * @param fragment
         */
        private QueryForProchainStageTask(ProchainsStages fragment) {
            this.fragmentWeakRef = new WeakReference<ProchainsStages>(fragment);
        }

        protected ArrayList<Stage> doInBackground(Object... o) {

            this.fragment = (ProchainsStages) o[0];
            this.act = this.fragment.getActivity();

            this.type = (String) o[1];
            this.data = (String) o[2];


            ArrayList<Stage> lstage = null;

            ListeStageParser lsp = new ListeStageParser(this.startQuerying());

            lstage = lsp.getListeStage();


            for (Stage s : lstage) {
                // url de l'image
                String src = s.getImg();

                if (src != null && !src.equals("")) {

                    // on tente de la récupérer dans le stockage
                    Bitmap bmp = DrawableOperation.getBitmapFromStorage(s.getId(), s.getDateDebut(), this.act.getApplicationContext());

                    if (bmp == null) {
                        // si ce n'est pas sur le disque, on l'écrit
                        DrawableOperation.saveThumbnailOnStorage(src, s.getId(), s.getDateDebut(), this.act.getApplicationContext());
                    }
                }
            }

            return lstage;

        }

        public JSONObject startQuerying() {

            JSONRequest jr = new JSONRequest();

            String url = this.act.getResources().getString(
                    R.string.api_prochain_stage_json);
            String from = this.act.getResources().getString(R.string.api_param_from);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
            from += "=" + sdf.format(new java.util.Date());

            String paramSupplementaire = "";
            if (type != null) {
                if (type.equals("intervenant")) {
                    paramSupplementaire = "&" + this.act.getResources().getString(R.string.api_param_anim) + "=" + this.data;
                } else if (type.equals("type")) {
                    try {
                        paramSupplementaire = "&" + this.act.getResources().getString(R.string.api_param_type) + "=" + URLEncoder.encode(this.data, "UTF-8");
                    } catch (UnsupportedEncodingException _uee) {

                    }

                } else if (type.equals("lieu")) {
                    try {
                        paramSupplementaire = "&" + this.act.getResources().getString(R.string.api_param_lieu) + "=" + URLEncoder.encode(this.data, "UTF-8");
                    } catch (UnsupportedEncodingException _uee) {

                    }

                } else if (type.equals("date")) {
                    try {

                        String[] tab = data.split(" ");
                        String mois = "";

                        if (tab[0].equals("Janvier")) {
                            mois = "01";
                        } else if (tab[0].equals("Février")) {
                            mois = "02";
                        } else if (tab[0].equals("Mars")) {
                            mois = "03";
                        } else if (tab[0].equals("Avril")) {
                            mois = "04";
                        } else if (tab[0].equals("Mai")) {
                            mois = "05";
                        } else if (tab[0].equals("Juin")) {
                            mois = "06";
                        } else if (tab[0].equals("Juillet")) {
                            mois = "07";
                        } else if (tab[0].equals("Août")) {
                            mois = "08";
                        } else if (tab[0].equals("Septembre")) {
                            mois = "09";
                        } else if (tab[0].equals("Octobre")) {
                            mois = "10";
                        } else if (tab[0].equals("Novembre")) {
                            mois = "11";
                        } else if (tab[0].equals("Décembre")) {
                            mois = "12";
                        }

                        paramSupplementaire = "&" + this.act.getResources().getString(R.string.api_param_date) + "=" + URLEncoder.encode(tab[1] + "-" + mois, "UTF-8");
                    } catch (UnsupportedEncodingException _uee) {

                    }
                }
            }

            JSONObject jo = jr.getJSONFromUrl(url + "?" + from + paramSupplementaire);

            return jo;


        }


        protected void onProgressUpdate(Integer... progress) {
            // setProgressPercent(progress[0]);
        }

        //
        // Exécution à la fin du traitement
        //
        protected void onPostExecute(ArrayList<Stage> lStage) {

            this.fragment.displayStage(lStage);

        }
    } // fin async
}