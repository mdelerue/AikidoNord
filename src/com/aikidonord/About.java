package com.aikidonord;

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


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Marc Delerue on 09/01/14.
 */
public class About extends ActionBarActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);


        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.about));


        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);

            TextView tv_titre = (TextView) findViewById(R.id.tv_titre_about);
            tv_titre.setText(getResources().getString(R.string.app_name) + " - version " + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {

        }
    }


    /**
     * Envoi de mail
     *
     * @param v
     */
    public void launch_email_dev(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setIcon(R.drawable.logo);
        alert.setTitle("Envoi de mail aux développeurs");
        alert.setMessage("Message :");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Do something with value!
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email_dev)});
                i.putExtra(Intent.EXTRA_SUBJECT, "Aikido Nord - Android - Retour bug");
                i.putExtra(Intent.EXTRA_TEXT, value);
                try {
                    startActivity(Intent.createChooser(i, "Envoyer un mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(About.this, getResources().getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.dismiss();
            }
        });

        alert.show();
    }

    public void lauch_formulaire(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_form)));
        startActivity(browserIntent);
    }

    public void lauch_github(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_github)));
        startActivity(browserIntent);
    }
}