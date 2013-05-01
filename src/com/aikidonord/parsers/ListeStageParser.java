package com.aikidonord.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aikidonord.metier.Stage;

public class ListeStageParser {

	private JSONObject json;
	private ArrayList<Stage> lstage;

	// JSON Node names
	private static final String TAG_STAGES = "stages";

	public ListeStageParser(JSONObject json) {

		this.json = json;
		this.lstage = new ArrayList<Stage>();
		
		try {
			this.ParseObject();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Parse l'objet JSON et appelle un ProchainStageParser pour chaque stage
	 * @throws JSONException
	 */
	private void ParseObject() throws JSONException {

		JSONArray stages = this.json.has(TAG_STAGES) ? this.json
				.getJSONArray(TAG_STAGES) : null;

		if (stages != null) {

			for (int i = 0; i < stages.length(); i++) {

				JSONObject stage = stages.getJSONObject(i);

				ProchainStageParser psp = new ProchainStageParser(stage);

				this.lstage.add(psp.parseObject());

			} // fin parcours tableau

		} // fin test si != null

	}
	
	/**
	 * renvoi la liste des stages construite
	 * @return
	 */
	public ArrayList<Stage> getListeStage() {
		return this.lstage;
	}

}
