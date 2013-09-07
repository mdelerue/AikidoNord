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

import java.util.Calendar;
import java.util.Date;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AikidoNord extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aikido_nord);


        // on complète le label
        TextView label_saison = (TextView) findViewById(R.id.tv_label_saison);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // on prend arbitrairement le 30 juillet comme date de fin de saison
        String intitule = "";

        if (cal.get(Calendar.MONTH) > 7) {
            intitule += cal.get(Calendar.YEAR) + " - "
                    + String.valueOf(cal.get(Calendar.YEAR) + 1);
        } else {
            intitule += String.valueOf(cal.get(Calendar.YEAR) - 1) + " - "
                    + cal.get(Calendar.YEAR);
        }

        label_saison.setText(label_saison.getText() + " " + intitule);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.aikido_nord, menu);

        return true;
    }


    /**
     * Lance l'activité intervenant au clic sur l'image
     *
     * @param v
     */
    public void launch_intervenant(View v) {
        Intent intent = new Intent(this, Intervenant.class);
        startActivity(intent);
    }

    /**
     * Lance l'activité type au clic sur l'image
     *
     * @param v
     */
    public void launch_type(View v) {
        Intent intent = new Intent(this, Type.class);
        startActivity(intent);
    }

    /**
     * Lance l'activité type au clic sur l'image
     *
     * @param v
     */
    public void launch_lieu(View v) {
        Intent intent = new Intent(this, Lieu.class);
        startActivity(intent);
    }

    /**
     * Lance l'activité date au clic sur l'image
     *
     * @param v
     */
    public void launch_date(View v) {
        Intent intent = new Intent(this, DateActivity.class);
        startActivity(intent);
    }

    /**
     * Lance le site web au clic sur le footer
     *
     * @param v
     */
    public void lauch_siteWeb(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_site_web)));
        startActivity(browserIntent);

    }

}
