package com.dor.cmovies;

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
import com.dor.cmovies.models.TicketTransaction;
import com.dor.cmovies.services.TicketServices;
import com.dor.cmovies.services.UserServices;

import org.json.JSONException;

public class TicketPage extends AppCompatActivity {

    private static String movieImage = "";
    private final static String TAG = "Transaction";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_layout);
        Log.d(TAG, "onCreate: Started.");

        Intent intent = getIntent();
        movieImage = intent.getStringExtra("movieImage");
        String userName = intent.getStringExtra("userName");
        String userEmail = intent.getStringExtra("userEmail");
        long transactionID = intent.getLongExtra("transactionID", 0);

        try {
            Toast.makeText( TicketPage.this, "Success" , Toast.LENGTH_SHORT).show();
            getTransaction(userEmail, transactionID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button deleteShowBtn = (Button) findViewById(R.id.uttDeleteBtn);
        deleteShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTransaction(transactionID);

                Intent homePage = new Intent(TicketPage.this, HomePage.class);
                homePage.putExtra("MSG", "edit");
                startActivity(homePage);
            }
        });

    }

    public void getTransaction(String userEmail, long transactionID ) throws JSONException {

        TicketServices ticketServices = new TicketServices(TicketPage.this);

        RequestQueue requestQueue = Volley.newRequestQueue(TicketPage.this);
        ticketServices.getTransaction(transactionID, userEmail, requestQueue, new TicketServices.TransactionResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText( TicketPage.this, "EROR HomePage" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(TicketTransaction transaction) {
                //Toast.makeText( TicketPage.this, "Success" , Toast.LENGTH_SHORT).show();
                createLayout(transaction);
            }
        });
    }

    public void deleteTransaction(long transactionID){

        TicketServices ticketServices = new TicketServices(TicketPage.this);
        RequestQueue requestQueue = Volley.newRequestQueue(TicketPage.this);

        ticketServices.deleteTicketTransaction(transactionID, requestQueue);
    }

    public void createLayout(TicketTransaction ticketTransaction){

        TextView ticketIDTXT = (TextView) findViewById(R.id.uttTicketID2);
        TextView showIDTXT = (TextView) findViewById(R.id.uttShowID2);
        TextView showNameTXT = (TextView) findViewById(R.id.uttShowName2);
        TextView showVenueTXT = (TextView) findViewById(R.id.uttShowVenue2);
        TextView showDateTXT = (TextView) findViewById(R.id.uttShowDate2);
        TextView showTimeStartTXT = (TextView) findViewById(R.id.uttShowTimeStart2);
        TextView quantityTXT = (TextView) findViewById(R.id.uttQuantity2);
        TextView seatTXT = (TextView) findViewById(R.id.uttSeat2);
        TextView rowSeatTXT = (TextView) findViewById(R.id.uttRowSeat2);
        TextView ticketPriceTXT = (TextView) findViewById(R.id.uttTicketPrice2);
        TextView totalPriceTXT = (TextView) findViewById(R.id.uttTicketTotalPrice2);


        String ticketID = String.valueOf(ticketTransaction.getTransactionID());
        String showID = String.valueOf(ticketTransaction.getTicket().getShow().getShowID());
        String showName = (ticketTransaction).getTicket().getShow().getShowName();
        String showVenue = (ticketTransaction).getTicket().getShow().getShowVenue();
        String showDate = (ticketTransaction).getTicket().getShow().getShowDate();
        String showTimeStart = (ticketTransaction).getTicket().getShow().getShowTimeStart();
        String seat = String.valueOf((ticketTransaction).getTicket().getSeat());
        String rowSeat = String.valueOf((ticketTransaction).getTicket().getRowSeat());
        String quantity = String.valueOf((ticketTransaction).getQuantity());
        String ticketPrice = String.valueOf((ticketTransaction).getTicket().getShow().getTicketPrice());
        String totalPrice = String.valueOf((ticketTransaction).getTotalPrice());


        ticketIDTXT.setText(ticketID);
        showIDTXT.setText(showID);
        showNameTXT.setText(showName + " ");
        showVenueTXT.setText(showVenue + " ");
        showDateTXT.setText(showDate + " ");
        showTimeStartTXT.setText(showTimeStart);
        quantityTXT.setText(quantity);
        seatTXT.setText(seat);
        rowSeatTXT.setText(rowSeat);
        ticketPriceTXT.setText(ticketPrice);
        totalPriceTXT.setText(totalPrice);

        ImageView imageView = findViewById(R.id.uttImageView);
        String url = movieImage;
        Glide.with(getApplicationContext())
                .load(Uri.parse(url))
                .apply(new RequestOptions().override(200, 446))
                .centerCrop()
                .into(imageView);
    }
}
