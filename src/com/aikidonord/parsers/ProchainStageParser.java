package com.aikidonord.parsers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aikidonord.metier.Stage;

public class ProchainStageParser {

	private JSONObject json;
	private Stage stage = null;

	// JSON Node names
	private static final String TAG_STAGE = "stage";
	private static final String TAG_JOURS = "jours";
	private static final String TAG_DATE = "date";
	private static final String TAG_HORAIRES = "horaires";
	private static final String TAG_HEUREDEBUT = "heureDebut";
	private static final String TAG_HEUREFIN = "heureFin";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_LIEU = "lieu";
	private static final String TAG_SALLE = "salle";
	private static final String TAG_RUE = "rue";
	private static final String TAG_CODEPOSTAL = "codepostal";
	private static final String TAG_VILLE = "ville";
	private static final String TAG_TARIF = "tarif";
	private static final String TAG_TYPE = "type";
	private static final String TAG_ANIMATEURS = "animateurs";
	private static final String TAG_NOM = "nom";
	private static final String TAG_GRADE = "grade";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd", Locale.FRANCE);
	
	public ProchainStageParser(JSONObject json) {

		this.json = json;
		this.stage = new Stage();
		
		

	}

	public Stage parseObject() {

		try {

			// objet stage :
			JSONObject j_stage = json.getJSONObject(TAG_STAGE);

			// gestion description globale
			if (j_stage.has(TAG_DESCRIPTION)) {
				this.stage.setDescription(j_stage.getString(TAG_DESCRIPTION));
			}

			// gestion type
			if (j_stage.has(TAG_TYPE)) {
				this.stage.setType(j_stage.getString(TAG_TYPE));
			}

			// gestion tarif
			if (j_stage.has(TAG_TARIF)) {
				this.stage.setTarif(j_stage.getString(TAG_TARIF));
			}
			
			// gestion date début
			this.stage.setDateDebut(sdf.parse(j_stage.getString(TAG_DATE)));

			// gestion lieu
			if (j_stage.has(TAG_LIEU)) {
				HashMap<String, String> mapLieu = new HashMap<String, String>();

				// on récupère l'objet lieu
				JSONObject j_lieu = j_stage.getJSONObject(TAG_LIEU);

				// gestion salle
				if (j_lieu.has(TAG_SALLE)) {
					mapLieu.put(TAG_SALLE, j_lieu.getString(TAG_SALLE));
				}

				// gestion rue
				if (j_lieu.has(TAG_RUE)) {
					mapLieu.put(TAG_RUE, j_lieu.getString(TAG_RUE));
				}

				// gestion codepostal
				if (j_lieu.has(TAG_CODEPOSTAL)) {
					mapLieu.put(TAG_CODEPOSTAL,
							j_lieu.getString(TAG_CODEPOSTAL));
				}

				// gestion ville
				if (j_lieu.has(TAG_VILLE)) {
					mapLieu.put(TAG_VILLE, j_lieu.getString(TAG_VILLE));
				}

				// on balance la map générée dans l'objet
				this.stage.setMapLieu(mapLieu);

			} // fin gestion lieu

			// Tableau Animateur
			JSONArray j_animateurs = j_stage.has(TAG_ANIMATEURS) ? j_stage.getJSONArray(TAG_ANIMATEURS) : null;

			if (j_animateurs != null) {
				for (int i = 0; i < j_animateurs.length(); i++) {

					String[] tabA = new String[2];

					JSONObject j_animateur = j_animateurs.getJSONObject(i);

					// nom
					if (j_animateur.has(TAG_NOM)) {
						tabA[0] = j_animateur.getString(TAG_NOM);
					} else {
						tabA[0] = null;
					}

					// grade
					if (j_animateur.has(TAG_GRADE)) {
						tabA[1] = j_animateur.getString(TAG_GRADE);
					} else {
						tabA[1] = null;
					}

					// on envoi l'animateur dans l'objet
					this.stage.addANimateur(tabA);

				} // fin parcours animateurs

			} // fin tableau animateurs

			// Tableau Jour
			JSONArray j_jours = j_stage.has(TAG_JOURS) ? j_stage.getJSONArray(TAG_JOURS) : null;

			if (j_jours != null) {
				for (int i = 0; i < j_jours.length(); i++) {
					// pour chaque jour
					
					JSONObject j_jour = j_jours.getJSONObject(i);
					
					// on récupère la date
					Date d = sdf.parse(j_jour.getString(TAG_DATE));
					
					JSONArray j_horaires = j_jour.getJSONArray(TAG_HORAIRES);
					
					// on parcoure les horaires
					for (int j = 0; j <j_horaires.length(); j++) {
						
						JSONObject h = j_horaires.getJSONObject(j);
						
						String[] tab = new String[3];
						
						// on remplit le tableau
						tab[0] = h.getString(TAG_HEUREDEBUT);
						tab[1] = h.getString(TAG_HEUREFIN);
						
						if (h.has(TAG_DESCRIPTION)) {
							tab[2] = h.getString(TAG_DESCRIPTION);
						} else {
							tab[2] = null;
						}
						
						// on ajoute l'horaire à l'objet Stage
						this.stage.addHoraire(d, tab);
						
					} // fin parcours horaires
					
				
				}// fin parcours des jours
			} // fin test si jour != null
			

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return this.stage;
	}

}
