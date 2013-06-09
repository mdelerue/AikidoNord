package com.aikidonord;

import java.util.Calendar;
import java.util.Date;

import android.graphics.Point;
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

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int orientation = display.getRotation();




        int ivSize = 0;
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.layout_button);

        if (orientation == Surface.ROTATION_270 || orientation == Surface.ROTATION_90){
            ivSize = width/4;
            rl.getLayoutParams().height = ivSize;
            rl.getLayoutParams().width = ivSize * 4;
        } else {
            ivSize = width/2;
            rl.getLayoutParams().height = ivSize * 2;
            rl.getLayoutParams().width = ivSize * 2;
        }

        ImageView iv = (ImageView)findViewById(R.id.iv_choix_date_main);
        iv.getLayoutParams().width = ivSize;
        iv.getLayoutParams().height = ivSize;
        iv = (ImageView)findViewById(R.id.iv_choix_theme_main);
        iv.getLayoutParams().width = ivSize;
        iv.getLayoutParams().height = ivSize;
        iv = (ImageView)findViewById(R.id.iv_choix_lieux_main);
        iv.getLayoutParams().width = ivSize;
        iv.getLayoutParams().height = ivSize;
        iv = (ImageView)findViewById(R.id.iv_choix_intervenant_main);
        iv.getLayoutParams().width = ivSize;
        iv.getLayoutParams().height = ivSize;

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
     * @param v
     */
    public void launch_intervenant(View v) {
        Intent intent = new Intent(this, Intervenant.class);
        startActivity(intent);
    }

    /**
     * Lance l'activité type au clic sur l'image
     * @param v
     */
    public void launch_type(View v) {
        Intent intent = new Intent(this, Type.class);
        startActivity(intent);
    }

    /**
     * Lance l'activité type au clic sur l'image
     * @param v
     */
    public void launch_lieu(View v) {
        Intent intent = new Intent(this, Lieu.class);
        startActivity(intent);
    }
}
