package com.dor.cmovies.resources;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dor.cmovies.HomePage;
import com.dor.cmovies.R;
import com.dor.cmovies.models.Show;
import com.dor.cmovies.services.ShowsServices;

import org.json.JSONException;

import static java.lang.Double.parseDouble;


//class for edit ot insert a new show
public class ShowsResource extends AppCompatActivity {

    Context context;
    private long showID;
    private String MSG = "";
    private final static String TAG = "Shows Resource";
    private final ShowsServices showsServices = new ShowsServices(ShowsResource.this);

    public ShowsResource(){}

    public ShowsResource(Context context){
        this.context = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_resource_layout);
        Log.d(TAG, "onCreate: Started.");

        Intent intent = getIntent();
        MSG = intent.getStringExtra("MSG");
        showID = intent.getLongExtra("showID", 0);

        if( showID != 0 )
            getShow(showID);

        Button insertShow = (Button) findViewById(R.id.srInsertBtn);
        insertShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show show = getShowFromText(); // return show from edittext

                if( show != null ){

                    if( MSG.equals("insert") ) { // insert new show
                        try {

                            insertShow(show);
                            Intent homePage = new Intent(ShowsResource.this, HomePage.class); // back to shows list in homepage
                            startActivity(homePage);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if( MSG.equals("edit") ){ // edit show
                        try {

                            show.setShowID(showID);
                            editShow(show);
                            Intent homePage = new Intent(ShowsResource.this, HomePage.class); // back to shows list in homepage
                            startActivity(homePage);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    public void insertShow(final Show show) throws JSONException {

        if( show == null ){
            Toast.makeText( ShowsResource.this, "illegal fields" , Toast.LENGTH_SHORT).show();
        }
        else{

            RequestQueue requestQueue = Volley.newRequestQueue(ShowsResource.this);
            showsServices.insertShow(show, requestQueue);
        }
    }

    public void editShow(final Show show) throws JSONException {

        if( show == null ){
            Toast.makeText( ShowsResource.this, "illegal fields" , Toast.LENGTH_SHORT).show();
        }
        else{

            RequestQueue requestQueue = Volley.newRequestQueue(ShowsResource.this);
            showsServices.editShow(show, requestQueue);
        }
    }

    public void getShow(long showID){

        if( showID != 0 ) {
            RequestQueue requestQueue = Volley.newRequestQueue(ShowsResource.this);
            showsServices.getShow(showID, requestQueue, new ShowsServices.ShowResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText( ShowsResource.this, "ERROR MoviePage" , Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onResponse(Show show) {
                    getTextFromShow(show, show.getShowID()); // design the show page
                }
            });
        }
        else{
            Show show = new Show();
            getTextFromShow(show, show.getShowID());
        }

    }

    public void getTextFromShow(Show show, long showID){


        EditText name = (EditText) findViewById(R.id.srShowNameETXT);
        EditText artist = (EditText) findViewById(R.id.srShowArtistETXT);
        EditText description = (EditText) findViewById(R.id.srShowDescriptionETXT);
        EditText venue = (EditText) findViewById(R.id.srShowVenueETXT);
        EditText date = (EditText) findViewById(R.id.srShowDateETXT);
        EditText timeStart = (EditText) findViewById(R.id.srShowTimeStartETXT);
        EditText priceTickets = (EditText) findViewById(R.id.srTicketPriceETXT);
        EditText numOfTickets = (EditText) findViewById(R.id.srNumOfTicketsETXT);
        EditText invitedTickets = (EditText) findViewById(R.id.srTicketsInvitedETXT);
        EditText availableTickets = (EditText) findViewById(R.id.srTicketsAvailableETXT);
        EditText showImageURL = (EditText) findViewById(R.id.srImageUrlETXT);

        if( showID != 0 ){

            name.setText(show.getShowName());
            artist.setText(show.getArtistName());
            description.setText(show.getShowDescription());
            venue.setText(show.getShowVenue());
            date.setText(show.getShowDate());
            timeStart.setText(show.getShowTimeStart());
            priceTickets.setText(String.valueOf(show.getTicketPrice()));
            numOfTickets.setText(String.valueOf(show.getNumberOfTickets()));
            invitedTickets.setText(String.valueOf(show.getTicketsInvited()));
            availableTickets.setText(String.valueOf(show.getTicketsAvailable()));
            showImageURL.setText(show.getImageURL());
        }
    }

    public Show getShowFromText() {

        EditText name = (EditText) findViewById(R.id.srShowNameETXT);
        EditText artist = (EditText) findViewById(R.id.srShowArtistETXT);
        EditText description = (EditText) findViewById(R.id.srShowDescriptionETXT);
        EditText venue = (EditText) findViewById(R.id.srShowVenueETXT);
        EditText date = (EditText) findViewById(R.id.srShowDateETXT);
        EditText timeStart = (EditText) findViewById(R.id.srShowTimeStartETXT);
        EditText priceTickets = (EditText) findViewById(R.id.srTicketPriceETXT);
        EditText numOfTickets = (EditText) findViewById(R.id.srNumOfTicketsETXT);
        EditText invitedTickets = (EditText) findViewById(R.id.srTicketsInvitedETXT);
        EditText availableTickets = (EditText) findViewById(R.id.srTicketsAvailableETXT);
        EditText showImageURL = (EditText) findViewById(R.id.srImageUrlETXT);

        String showName = name.getText().toString();
        String artistName = artist.getText().toString();
        String showDescription = description.getText().toString();
        String showVenue = venue.getText().toString();
        String showDate = date.getText().toString();
        String showTimeStart = timeStart.getText().toString();
        double ticketPrice = parseDouble(priceTickets.getText().toString());
        int numberOfTickets = Integer.parseInt(numOfTickets.getText().toString());
        int ticketsInvited = Integer.parseInt(invitedTickets.getText().toString());
        int ticketsAvailable = Integer.parseInt(availableTickets.getText().toString());
        String showImage = showImageURL.getText().toString();


        if (showName.equals("") || artistName.equals("") || showDescription.equals("") || showVenue.equals("") ||
            showDate.equals("") || showTimeStart.equals("") || showImage.equals("") ) {

            Toast.makeText(ShowsResource.this, "Error: Please insert all the values", Toast.LENGTH_SHORT).show();
            return null;
        }
        else {

            if ((0 <= ticketPrice && 0 <= numberOfTickets && 0 <= ticketsInvited && 0 <= ticketsAvailable) &&
               ((ticketsAvailable + ticketsInvited) == numberOfTickets)) { // check  valid values

                return new Show(0, showName, artistName, showDescription, showVenue, showDate, showTimeStart, ticketPrice, numberOfTickets, ticketsInvited, ticketsAvailable, showImage);
            }
            else {
                Toast.makeText(ShowsResource.this, "Error: illegal values", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
    }
}
