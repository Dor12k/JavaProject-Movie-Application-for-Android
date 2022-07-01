package com.dor.cmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dor.cmovies.listAdapter.RecyclerViewAdapter;
import com.dor.cmovies.models.Show;
import com.dor.cmovies.services.ShowsServices;
import com.dor.cmovies.services.UserServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private final String MSG = "";
    private final String message = "";
    private  String USERNAME = "username";
    private  String USER_EMAIL = "userEmail";
    private final static String TAG = "HomePage";
    private ArrayList<Show> shows = new ArrayList<>();
    private final ShowsServices showsServices = new ShowsServices(HomePage.this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_layout);
        Log.d(TAG, "onCreate: Started.");

        Intent intent = getIntent();
        USERNAME = intent.getStringExtra("userName");
        USER_EMAIL = intent.getStringExtra("userEmail");
        getShowsList();

        //Go to user profile
        Button userProfileBtn = (Button) findViewById(R.id.hpUserProfile);
        userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProfile = new Intent(HomePage.this, UserServices.class);
                userProfile.putExtra("userName", USERNAME);
                userProfile.putExtra("userEmail", USER_EMAIL);
                startActivity(userProfile);
            }
        });

        ImageView imageView = findViewById(R.id.hpImageView);
        Picasso.get().load(R.drawable.moviesiconn).resize(430,200).centerCrop().into(imageView);
    }

    //Get all the shows
    public void getShowsList(){
        RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);
        showsServices.getShowsList(requestQueue, new ShowsServices.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText( HomePage.this, "ERROR HomePage" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(List<Show> list) {
                showListAdapter(list);
            }
        });
    }

    //Create list views
    public void showListAdapter(final List<Show> list){


        shows = (ArrayList<Show>) list;

        RecyclerView moviesList = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, list, USERNAME, USER_EMAIL);
        moviesList.setLayoutManager(new GridLayoutManager(this, 3));
        moviesList.setAdapter(myAdapter);


    }

}
