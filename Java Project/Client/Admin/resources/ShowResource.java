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
import com.dor.cmovies.HomePage;
import com.dor.cmovies.R;
import com.dor.cmovies.models.Show;
import com.dor.cmovies.models.TicketTransaction;
import com.dor.cmovies.services.ShowsServices;
import com.dor.cmovies.services.TicketServices;

import org.json.JSONException;

import java.util.List;

public class ShowResource extends AppCompatActivity {

    Context context;
    private long showID;
    private  String USERNAME = "username";
    private  String USER_EMAIL = "userEmail";
    private static final String TAG = "Event";
    private final ShowsServices showsServices = new ShowsServices(ShowResource.this);
    private final TicketServices ticketServices = new TicketServices(ShowResource.this);

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

        if( showID != 0 ){
            getShow(showID); // create show page

        }

        Button editShowBtn = (Button) findViewById(R.id.eEditBtn);
        editShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editShowResource = new Intent(ShowResource.this, ShowsResource.class);
                editShowResource.putExtra("MSG", "edit");
                editShowResource.putExtra("showID", showID);
                startActivity(editShowResource);
            }
        });

        Button deleteShowBtn = (Button) findViewById(R.id.eDeleteBtn);
        deleteShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteShow(showID, USERNAME, USER_EMAIL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button transactionShow = (Button) findViewById(R.id.eTransactionBtn);
        transactionShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transactionsShows = new Intent(ShowResource.this, TransactionsResource.class);
                transactionsShows.putExtra("showID", showID);
                transactionsShows.putExtra("userName", USERNAME);
                transactionsShows.putExtra("userEmail", USER_EMAIL);
                transactionsShows.putExtra("MSG", "transactionShow");

                startActivity(transactionsShows);
            }
        });
    }

    public void getShow(long showID){
        RequestQueue requestQueue = Volley.newRequestQueue(ShowResource.this);
        showsServices.getShow(showID, requestQueue, new ShowsServices.ShowResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText( ShowResource.this, "ERROR MoviePage" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Show show) {
                createShowPage(show); // design the show page
            }
        });
    }

    public void createShowPage(Show show){

        TextView showName = (TextView) findViewById(R.id.eEventNameTxt);
        showName.setText("\n" + show.getShowName() + "\n");

        TextView artistName = (TextView) findViewById(R.id.eArtistsNameTxt);
        artistName.setText("\t" + show.getArtistName() + " \n");

        TextView showDescription = (TextView) findViewById(R.id.eEventDescriptionTxt);
        showDescription.setText("\t" + show.getShowDescription() + "\n");

        TextView showDate = (TextView) findViewById(R.id.eEventDateTxt);
        showDate.setText("\t" + "Show Time:  \t" + show.getShowDate() + " \t" + show.getShowTimeStart() + "\n");

        TextView showVenue = (TextView) findViewById(R.id.eEventVenueTxt);
        showVenue.setText("\t" + "Location: \t" + show.getShowVenue() + "\n");

        TextView showTicketPrice = (TextView) findViewById(R.id.eTicketPriceTxt);
        showTicketPrice.setText("\t" + "Ticket Price:  " + show.getTicketPrice() + " $ \n");

        TextView numOfTicketsTXT = (TextView) findViewById(R.id.eNumOfTicketsTXT);
        numOfTicketsTXT.setText("\t" + "Number \nOf Tickets\n" + show.getNumberOfTickets() + "\n");

        TextView ticketsAvailableTXT = (TextView) findViewById(R.id.eTicketsAvailableTXT);
        ticketsAvailableTXT.setText("\t" + "Tickets \nAvailable\n" + show.getTicketsAvailable() + "\n");

        TextView ticketsInvitedTXT = (TextView) findViewById(R.id.eTicketsInvitedTXT);
        ticketsInvitedTXT.setText("\t" + "Tickets \nInvited\n" + show.getTicketsInvited() + "\n");

        ImageView imageView = findViewById(R.id.eImageView);
        String url = show.getImageURL();
        Glide.with(getApplicationContext())
                .load(Uri.parse(url))
                .apply(new RequestOptions().override(150, 350))
                .centerCrop()
                .into(imageView);
    }

    public void deleteShow(long showID, String userName, String userEmail) throws JSONException {

        RequestQueue requestQueue = Volley.newRequestQueue(ShowResource.this);

        ticketServices.getTransactionsShow(showID, userName, userEmail, requestQueue, new TicketServices.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText( ShowResource.this, "ERROR Transaction List" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(List<TicketTransaction> list) throws JSONException {
                if( 0 < list.size() ){
                    String message = "Can not delete movie that sold tickets";
                    Toast.makeText( ShowResource.this, message , Toast.LENGTH_SHORT).show();
                }
                else{
                    showsServices.deleteShow(showID, requestQueue);
                    Intent homePage = new Intent(ShowResource.this, HomePage.class);
                    homePage.putExtra("MSG", "edit");
                    startActivity(homePage);
                }
            }
        });


    }

}
