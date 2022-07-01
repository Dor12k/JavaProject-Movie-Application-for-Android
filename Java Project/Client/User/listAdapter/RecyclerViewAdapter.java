package com.dor.cmovies.listAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dor.cmovies.R;
import com.dor.cmovies.models.Show;
import com.dor.cmovies.resources.ShowResource;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{

    private List<Show> mData ;
    private Context mContext ;
    private String USERNAME = "";
    private String USER_EMAIL = "";


    public RecyclerViewAdapter(Context mContext, List<Show> mData, String username, String useremail) {
        this.mContext = mContext;
        this.mData = mData;

        USERNAME = username;
        USER_EMAIL = useremail;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.movieName.setText(mData.get(position).getShowName());

        Picasso.get().load(mData.get(position).getImageURL()).fit().into(holder.movieImg);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // movie page
                Intent intent = new Intent(mContext, ShowResource.class);

                intent.putExtra("userName", USERNAME);
                intent.putExtra("userEmail", USER_EMAIL);
                intent.putExtra("showID", mData.get(position).getShowID());

                // start the activity
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView movieName;
        ImageView movieImg;
        CardView cardView ;
        String imageURL;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            movieName = (TextView) itemView.findViewById(R.id.caShowNameTXT) ;
            movieImg = (ImageView) itemView.findViewById(R.id.movieIMG);
            cardView = (CardView) itemView.findViewById(R.id.cardViewID);

        }
    }


}

/*

        holder.imageURL.

                Glide.with(getApplicationContext())
                .load(Uri.parse(url))
                .apply(new RequestOptions().override(150, 350))
                .centerCrop()
                .into(imageView);
 */