package com.aikidonord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import com.aikidonord.fragments.FragmentProchainsStages;
import com.aikidonord.fragments.FragmentType;

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
        implements FragmentType.OnTypeSelectedListener {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_type);

        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.actionbar_titre_theme));
    }

    @Override
    public void onTypeSelected(String type) {

        FragmentProchainsStages psFragment =
                (FragmentProchainsStages) getSupportFragmentManager().findFragmentById(R.id.fragment_prochains_stages);

        if (psFragment != null && psFragment.isLaunched()) {

            // bon, on oublie le replace qui ne fonctionne pas
            // si le fragment est dans le layout est on appelle
            // directement le lancement de l'async du fragment.
            // merci à la doc quasi-inexistante de google là-dessus...
            psFragment.lancementAsync("type", type, true);


        } else {
            // à la bascule de tablette vers téléphone et clic, on arrive ici.
            // Donc on revérifie et on doublonne le code :(
            // dans le cas de l'affichage téléphone classique

            Intent i = new Intent(this, com.aikidonord.ProchainsStages.class);
            // données à envoyer à l'activité
            Bundle b = new Bundle();
            b.putString("type", "type");
            b.putString("data", String.valueOf(type));
            i.putExtras(b);
            this.startActivity(i);
        }

    }

}