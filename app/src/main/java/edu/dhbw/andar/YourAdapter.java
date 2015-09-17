package edu.dhbw.andar;

/**
 * Created by Lidiane on 17/09/2015.
 */

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.BaseAdapter;

import getcomp.educar.quadrics.R;

public class YourAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavDrawerItem> mNavItems;

    public YourAdapter(Context context, ArrayList<NavDrawerItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.menuicon, null);
        }
        else {
            view = convertView;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        iconView.setImageResource(mNavItems.get(position).icon);

        return view;
    }
}
