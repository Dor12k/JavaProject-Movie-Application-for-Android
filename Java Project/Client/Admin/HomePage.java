package com.dor.cmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dor.cmovies.listadapter.ShowListAdapter;
import com.dor.cmovies.models.Show;
import com.dor.cmovies.resources.ShowResource;
import com.dor.cmovies.resources.ShowsResource;
import com.dor.cmovies.resources.TransactionsResource;
import com.dor.cmovies.services.ShowsServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private String MSG = "";
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

        ImageView imageView = findViewById(R.id.hpImageView);
        Picasso.get().load(R.drawable.moviesiconn).resize(430,200).centerCrop().into(imageView);

        getShowsList(); // create list of movies

        Button insertShow = (Button) findViewById(R.id.hpInsertBtn);
        insertShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSG = "insert";
                Intent insertShow = new Intent(HomePage.this, ShowsResource.class);
                insertShow.putExtra("MSG", MSG);
                startActivity(insertShow);
                getShowsList();

            }
        });

        Button transactionsShows = (Button) findViewById(R.id.hpTransactionBtn);
        transactionsShows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSG = "transactionsList";
                Intent transactionsShows = new Intent(HomePage.this, TransactionsResource.class);
                transactionsShows.putExtra("MSG", MSG);
                startActivity(transactionsShows);
            }
        });
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
                //Toast.makeText( HomePage.this, " " , Toast.LENGTH_SHORT).show();
                showListAdapter(list);
            }
        });
    }

    //Create list views
    public void showListAdapter(final List<Show> list){
        shows = (ArrayList<Show>) list;
        ListView mListView = (ListView) findViewById(R.id.hpListView);
        ShowListAdapter adapter = new ShowListAdapter(HomePage.this, R.layout.list_adapter, shows);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Show show = shows.get(position);
                Intent event = new Intent(HomePage.this, ShowResource.class); // move to movie page
                event.putExtra("userName", USERNAME);
                event.putExtra("userEmail", USER_EMAIL);
                event.putExtra("showID", show.getShowID());
                startActivity(event);
            }
        });
    }
}
