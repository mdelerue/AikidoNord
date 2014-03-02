package com.aikidonord.display;

/**
 * Created with IntelliJ IDEA.
 * User: garth
 * Date: 09/06/13
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.aikidonord.ProchainsStages;
import com.aikidonord.R;

import java.util.ArrayList;


/**
 * @author Marc Delerue
 *         I used
 *         http://nickcharlton.net/post/building-custom-android-listviews &
 *         http://blog.sachinshah.name/?p=62
 *         <p/>
 *         Copyright (C) 2013  Marc Delerue
 *         <p/>
 *         This program is free software; you can redistribute it and/or
 *         modify it under the terms of the GNU General Public License
 *         as published by the Free Software Foundation; either version 2
 *         of the License, or (at your option) any later version.
 *         <p/>
 *         This program is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU General Public License for more details.
 *         <p/>
 *         You should have received a copy of the GNU General Public License
 *         along with this program; if not, write to the Free Software
 *         Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

public class LieuAdapter extends BaseAdapter {

    // store the context (as an inflated layout)
    private LayoutInflater inflater;
    // store the resource (searchitem_intervenant.xml)
    private int resource;

    private ArrayList<String> data;
    private Activity parentActivity;
    private Context context;

    public LieuAdapter(Context context, int resource, ArrayList<String> data, Activity parentActivity) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.data = data;
        this.context = context;
        this.parentActivity = parentActivity;

    }

    /**
     * Return the size of data set
     */
    public int getCount() {
        return this.data.size();
    }

    /**
     * Return an object of the data set
     */
    public String getItem(int arg0) {
        return this.data.get(arg0);
    }

    public long getItemId(int position) {
        /*
          voir http://stackoverflow.com/questions/18706854/android-nullpointerexception-onitemclick-in-listview-after-adding-new-item-to-l
          à priori retourner la position devrait suffire
          return Integer.getInteger(this.data.get(position));
         */
        return position;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // reuse a given view, or inflate a new one from the xml
        View view;

        if (convertView == null) {
            view = this.inflater.inflate(resource, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.tv_lieu = (TextView) view
                    .findViewById(R.id.searchList_lieu);

            view.setTag(holder);

        } else {
            view = convertView;
        }

        // bind the data to the view object
        return this.bindData(view, position);

    }

    /**
     * Bind the provided data to the view. This is the only method not required
     * by base adapter.
     */
    public View bindData(View view, int position) {

        // make sure it's worth drawing the view
        if (this.data.get(position) == null) {
            return view;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        // pull out the object
        String lieu = this.data.get(position);

        // Nom
        TextView tv = holder.tv_lieu;

        // set the value


        String texte = lieu;
        tv.setText(texte);

        final String lieuOC = lieu;


        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(parentActivity, ProchainsStages.class);
                // données à envoyer à l'activité
                Bundle b = new Bundle();
                b.putString("type", "lieu");
                b.putString("data", String.valueOf(lieuOC));
                i.putExtras(b);
                parentActivity.startActivity(i);
            }
        });


        // return the final view object
        return view;
    }


    // ViewHolder Class
    static class ViewHolder {

        TextView tv_lieu;


    }

}
