package com.aikidonord;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Marc Delerue
 * Date: 25/05/13
 * Time: 16:43
 * Copyright (C) 2013  Marc Delerue
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

public class Type extends ActionBarActivity
        implements com.aikidonord.fragments.Type.OnTypeSelectedListener {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_type);

        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.actionbar_titre_theme));
    }

    @Override
    public void onTypeSelected(String type) {

        com.aikidonord.fragments.ProchainsStages psFragment =
                (com.aikidonord.fragments.ProchainsStages) getSupportFragmentManager().findFragmentById(R.id.fragment_prochains_stages);

        if (psFragment != null) {

            // bon, on oublie le replace qui ne fonctionne pas
            // si le fragment est dans le layout est on appelle
            // directement le lancement de l'async du fragment.
            // merci à la doc quasi-inexistante de google là-dessus...
            psFragment.lancementAsync("type", type, true);


        }

    }

}