package com.dor.cmovies.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.dor.cmovies.R;
import com.dor.cmovies.models.Show;

import java.util.ArrayList;

public class ShowListAdapter extends ArrayAdapter<Show> {

    private static final String TAG = "ShowListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView showName;
        TextView showDate;
        TextView showTimeStart;
        //ImageView img;
    }

    /**
     * Default constructor for the ShowListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public ShowListAdapter(Context context, int resource, ArrayList<Show> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String showName = getItem(position).getShowName();
        String showDate = getItem(position).getShowDate();
        String showTimeStart = getItem(position).getShowTimeStart();

        //create the view result for showing the animation
        final View result;

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.showName = (TextView) convertView.findViewById(R.id.laShowNameTxt);
            holder.showDate = (TextView) convertView.findViewById(R.id.laShowDateTxt);
            holder.showTimeStart = (TextView) convertView.findViewById(R.id.laShowHourTxt);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.showName.setText(showName);
        holder.showDate.setText(showDate);
        holder.showTimeStart.setText(showTimeStart);

        return convertView;
    }
}
