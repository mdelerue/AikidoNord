package com.aikidonord.display;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.aikidonord.metier.Animateur;
import com.aikidonord.R;

/**
 *
 * @author Marc Delerue
 * I used
 *         http://nickcharlton.net/post/building-custom-android-listviews &
 *         http://blog.sachinshah.name/?p=62
 */

public class IntervenantAdapter extends BaseAdapter {

    // store the context (as an inflated layout)
    private LayoutInflater inflater;
    // store the resource (searchitem_intervenant.xml)
    private int resource;

    private ArrayList<Animateur> data;
    private Activity parentActivity;
    private Context context;

    public IntervenantAdapter(Context context, int resource, ArrayList<Animateur> data, Activity parentActivity) {
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
    public Animateur getItem(int arg0) {
        return this.data.get(arg0);
    }

    public long getItemId(int position) {
        // temp
        return this.data.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // reuse a given view, or inflate a new one from the xml
        View view;

        if (convertView == null) {
            view = this.inflater.inflate(resource, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.tv_nom = (TextView) view
                    .findViewById(R.id.searchList_animateurNom);

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
        Animateur anim = this.data.get(position);

        // Nom
        TextView tv = holder.tv_nom;

        // set the value
        String texte = anim.getNom() + " " + (anim.getPrenom() != null ? anim.getPrenom() : "");
        tv.setText(texte);




        // return the final view object
        return view;
    }




    // ViewHolder Class
    static class ViewHolder {

        TextView tv_nom;


    }

}
