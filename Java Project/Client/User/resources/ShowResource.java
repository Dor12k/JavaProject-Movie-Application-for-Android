package com.dor.cmovies.resources;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dor.cmovies.services.ShowServices;
import com.dor.cmovies.R;
import com.dor.cmovies.models.Show;
import com.dor.cmovies.services.ShowsServices;
import com.dor.cmovies.services.TicketServices;
import com.dor.cmovies.services.UserServices;

public class ShowResource extends AppCompatActivity {

    Context context;
    private long showID;
    private String MSG = "";
    private String USERNAME = "userName";
    private static String USER_EMAIL = "userEmail";
    private static final String TAG = "Event";
    private final ShowsServices showsServices = new ShowsServices(ShowResource.this);

    public ShowResource(){}

    public ShowResource(Context context){ this.context = context;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout);
        Log.d(TAG, "onCreate: Started.");

        Intent intent = getIntent();
        USERNAME = intent.getStringExtra("userName");
        USER_EMAIL = intent.getStringExtra("userEmail");
        showID = intent.getLongExtra("showID", 0);
        System.out.println(USER_EMAIL);

        Button pickSeatsBtn = (Button) findViewById(R.id.eHallBtn);
        pickSeatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // need to buyTickets()

                Intent hall = new Intent(ShowResource.this, ShowServices.class);
                hall.putExtra("MSG", MSG);
                hall.putExtra("showID", showID);
                hall.putExtra("userName", USERNAME);
                hall.putExtra("userEmail", USER_EMAIL);

                startActivity(hall);
            }
        });

        getShow(showID);
    }

    public void getShow(long showID){
        RequestQueue requestQueue = Volley.newRequestQueue(ShowResource.this);
        showsServices.getShow(showID, requestQueue, new ShowsServices.ShowResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText( ShowResource.this, "EROR HomePage" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Show show) {
                createShowPage(show); // design the show page
            }
        });
    }

    public void createShowPage(Show show){

        TextView showName = (TextView) findViewById(R.id.eEventNameTXT);
        showName.setText("\n" + show.getShowName() + "\n");

        TextView artistName = (TextView) findViewById(R.id.eArtistNameTXT);
        artistName.setText("\t" + show.getArtistName() + " \n");

        TextView showDescription = (TextView) findViewById(R.id.eEventDescriptionTXT);
        showDescription.setText("\t" + show.getShowDescription() + "\n");

        TextView showDate = (TextView) findViewById(R.id.eEventDateTXT);
        showDate.setText("\t" + "Show Time:  \t" + show.getShowDate() + " \t" + show.getShowTimeStart() + "\n");


        TextView showVenue = (TextView) findViewById(R.id.eEventVenueTXT);
        showVenue.setText("\t" + "Location: \t" + show.getShowVenue() + "\n");

        TextView showPTicketPrice = (TextView) findViewById(R.id.epTicketPriceTXT);
        TextView showTicketPrice = (TextView) findViewById(R.id.eTicketPriceTXT);
        TextView showCTicketPrice = (TextView) findViewById(R.id.ecTicketPriceTXT);
        showPTicketPrice.setText("\t" + "Ticket Price:  " + "\n");
        showTicketPrice.setText(String.valueOf(show.getTicketPrice()));
        showCTicketPrice.setText(" $ \n");

        ImageView imageView = findViewById(R.id.eImageView);
        String url = show.getImageURL();
        Glide.with(getApplicationContext())
                .load(Uri.parse(url))
                .apply(new RequestOptions().override(150, 350))
                .centerCrop()
                .into(imageView);

    }

}
