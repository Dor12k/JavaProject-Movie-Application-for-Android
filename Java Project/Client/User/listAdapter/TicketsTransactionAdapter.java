package com.dor.cmovies.listAdapter;

import android.annotation.SuppressLint;
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
import com.dor.cmovies.models.TicketTransaction;

import java.util.ArrayList;


public class TicketsTransactionAdapter extends ArrayAdapter<TicketTransaction> {

    private static final String TAG = "TicketsTransactionAdapter";

    private final int mResource;
    private final Context mContext;
    private int lastPosition = -1;
    private String MSG = "nun";


    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView ticketID;
        TextView showID;
        TextView showName;
        TextView showVenue;
        TextView showDate;
        TextView showTimeStart;
        TextView userName;
        TextView firstName;
        TextView lastName;
        TextView quantity;
        TextView ticketPrice;
    }

    /**
     * Default constructor for the TicketsTransactionListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public TicketsTransactionAdapter(Context context, int resource, ArrayList<TicketTransaction> objects, String list) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        MSG = list;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String ticketID = String.valueOf(getItem(position).getTransactionID());
        String showID = String.valueOf(getItem(position).getTicket().getShow().getShowID());
        String showName = getItem(position).getTicket().getShow().getShowName();
        String showVenue = getItem(position).getTicket().getShow().getShowVenue();
        String showDate = getItem(position).getTicket().getShow().getShowDate();
        String showTimeStart = getItem(position).getTicket().getShow().getShowTimeStart();
        String userName = getItem(position).getUser().getUserName();
        String firstName = getItem(position).getUser().getFirstName();
        String lastName = getItem(position).getUser().getLastName();
        String quantity = String.valueOf(getItem(position).getQuantity());
        String ticketPrice = String.valueOf(getItem(position).getTicket().getShow().getTicketPrice());

        //create the view result for showing the animation
        final View result;

        ViewHolder holder;

        if(convertView == null){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();

            if( this.MSG.equals("userTransactions") ){ // user transaction
                holder.ticketID = (TextView) convertView.findViewById(R.id.upTicketID);
                holder.showID = (TextView) convertView.findViewById(R.id.upShowID);
                holder.showName = (TextView) convertView.findViewById(R.id.upShowName);
                holder.showVenue = (TextView) convertView.findViewById(R.id.upShowVenue);
                holder.showDate = (TextView) convertView.findViewById(R.id.upShowDate);
                holder.showTimeStart = (TextView) convertView.findViewById(R.id.upShowTimeStart);
                holder.quantity = (TextView) convertView.findViewById(R.id.upQuantity);
                holder.ticketPrice = (TextView) convertView.findViewById(R.id.upTicketPrice);
            }

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

        if( this.MSG.equals("userTransactions") ){ // user tickets transactions list
            holder.ticketID.setText(ticketID);
            holder.showID.setText(showID);
            holder.showName.setText(showName + " ");
            holder.showVenue.setText(showVenue + " ");
            holder.showDate.setText(showDate + " ");
            holder.showTimeStart.setText(showTimeStart);
            holder.quantity.setText(quantity);
            holder.ticketPrice.setText(ticketPrice);
        }
        return convertView;
    }
}