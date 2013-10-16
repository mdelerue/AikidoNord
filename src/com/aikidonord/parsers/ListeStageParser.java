package com.aikidonord.parsers;

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

import java.util.ArrayList;

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


        if (this.json != null) {
            // si le retour json != null
            try {
                this.ParseObject();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    /**
     * Parse l'objet JSON et appelle un ProchainStageParser pour chaque stage
     *
     * @throws JSONException
     */
    private void ParseObject() throws JSONException {

        //System.out.println("AIKIDONORD : " + (this.json != null ? "OK" : "NULL"));
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
     *
     * @return
     */
    public ArrayList<Stage> getListeStage() {
        return this.lstage;
    }

}
