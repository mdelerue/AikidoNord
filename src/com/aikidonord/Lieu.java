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

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import com.aikidonord.fragments.ProchainsStages;

/**
 * Created with IntelliJ IDEA.
 * User: Marc Delerue
 */
public class Lieu extends ActionBarActivity implements com.aikidonord.fragments.Lieu.OnLieuSelectedListener {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lieu);

        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.actionbar_titre_lieu));


    }

    @Override
    public void onLieuSelected(String lieu) {

        ProchainsStages psFragment =
                (ProchainsStages) getSupportFragmentManager().findFragmentById(R.id.fragment_prochains_stages);

        if (psFragment != null) {

            Bundle b = new Bundle();
            b.putString("type", "lieu");
            b.putString("data", lieu);


            // Check what fragment is currently shown, replace if needed.

            // Make new fragment to show this selection.
            psFragment = ProchainsStages.newInstance(b);

            // Execute a transaction, replacing any existing fragment
            // with this one inside the frame.

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.fragment_prochains_stages, psFragment);

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }


        //psFragment.re  .getActivity().startActivityFromFragment();s onActivityCreated(b);

    }
}
