package com.aikidonord;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
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
     * Lance l'activité intervenant au clic sur le bouton;
     * @param v
     */
    public void launch_intervenant(View v) {
        Intent intent = new Intent(this, Intervenant.class);
        startActivity(intent);
    }

}