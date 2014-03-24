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

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.aikidonord.display.DisplayStage;
import com.aikidonord.metier.Stage;
import com.aikidonord.parsers.ListeStageParser;
import com.aikidonord.utils.DrawableOperation;
import com.aikidonord.utils.JSONRequest;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ProchainsStages extends ActionBarActivity {

    static private ArrayList<Stage> lstage;

    //protected ViewPager viewPager;
    //protected StageAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_prochain_stage);



        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.actionbar_titre_stage));

        FrameLayout fl = (FrameLayout)findViewById(R.id.frags);

        com.aikidonord.fragments.ProchainsStages newFragment
                = new com.aikidonord.fragments.ProchainsStages()
                .newInstance(getIntent().getExtras());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frags, newFragment).commit();
        /*
        com.aikidonord.fragments.ProchainsStages psFragment =
                (com.aikidonord.fragments.ProchainsStages) getSupportFragmentManager().findFragmentById(R.id.fragment_prochains_stages);

        if (psFragment != null && savedInstanceState != null ) {
            psFragment.lancementAsync(savedInstanceState.getString("type"), savedInstanceState.getString("data"));
        }
        */



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
        //this.displayStage(stage);

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return lstage;
    }


}
