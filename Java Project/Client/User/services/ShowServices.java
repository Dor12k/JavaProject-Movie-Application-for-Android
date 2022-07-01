package com.dor.cmovies.services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dor.cmovies.R;
import com.dor.cmovies.models.Show;
import com.dor.cmovies.models.Ticket;
import com.dor.cmovies.models.TicketTransaction;
import com.dor.cmovies.models.User;


import org.json.JSONException;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


// this class create the hall seats tickets and manage the tickets transactions
public class ShowServices extends AppCompatActivity implements View.OnClickListener {

    Context context;

    private long showID;
    private String MSG = "0";
    private String USERNAME = "";
    public static String USER_EMAIL = "userEmail";
    private static final String TAG = "Hall";

    private ViewGroup layout;

    private int reservedSeats = 0;

    private final int STATUS_BOOKED = 0;
    private final int STATUS_NO_SEAT = -1;
    private final int STATUS_AVAILABLE = 1;
    private final int STATUS_RESERVED = 2;

    private final TicketServices ticketServices = new TicketServices(ShowServices.this);
    private final ShowsServices showsServices = new ShowsServices(ShowServices.this);
    private final UserServices userServices = new UserServices(ShowServices.this);


    private List<TextView> seatViewList = new ArrayList<>();


    private String selectedIds = "";

    private final int[][] hall = new int[15][20];
    private final int[][] userSeats = new int[2][10];


    public ShowServices(){}

    public ShowServices(Context context){ this.context = context;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hall_layout);
        Log.d(TAG, "onCreate: Started.");

        layout = findViewById(R.id.layoutSeat);

        Intent intent = getIntent();
        MSG = intent.getStringExtra("MSG");
        USERNAME = intent.getStringExtra("userName");
        USER_EMAIL = intent.getStringExtra("userEmail");
        showID = intent.getLongExtra("showID", 0);

        if( !MSG.equals("0") ){
            Toast.makeText(ShowServices.this, MSG, Toast.LENGTH_SHORT).show();

        }

        try {
            getTransactionsShow(showID, USERNAME, USER_EMAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button buyTicketsBtn = (Button) findViewById(R.id.hBuyTicketsBtn);
        buyTicketsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0;
                //Toast.makeText( Hall.this, "Buy Tickets" , Toast.LENGTH_SHORT).show();

                // count all the reserved seats and invite tickets
                for( int i = 0; i < userSeats[0].length; i++ ){

                    if( userSeats[0][i] != 0 ){

                        count++;

                        int seat = userSeats[0][i];
                        int rowSeat = userSeats[1][i];

                        Calendar calendar = Calendar.getInstance();
                        String purchaseDate = DateFormat.getDateInstance().format(calendar.getTime());

                        buySelectedTickets(showID, USERNAME, purchaseDate, seat, rowSeat);
                    }
                }
                if( count ==  0 ){

                    Toast.makeText( ShowServices.this, "Please Select Seat" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // buy selected tickets
    public void buySelectedTickets(long showID, String username, String purchaseDate, int seat, int rowSeat ){


        int QUANTITY = 1;

        //get show
        RequestQueue requestQueue = Volley.newRequestQueue(ShowServices.this);
        showsServices.getShow(showID, requestQueue, new ShowsServices.ShowResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText( ShowServices.this, "ERROR HomePage" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Show show) {

                double ticketPrice = show.getTicketPrice();
                Ticket ticket = new Ticket(show, purchaseDate, seat, rowSeat);

                //get user
                RequestQueue requestQueue = Volley.newRequestQueue(ShowServices.this);
                userServices.getUser(username, requestQueue, new UserServices.UserResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(ShowServices.this, "ERROR HomePage" , Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(User user) {

                        TicketTransaction ticketTransaction = new TicketTransaction(ticket, user, QUANTITY, ticketPrice);
                        long showID = ticketTransaction.getTicket().getShow().getShowID();


                        TicketTransaction ticketTransaction2 = new TicketTransaction(ticket, user, QUANTITY, ticketPrice);
                        long showID2 = ticketTransaction2.getTicket().getShow().getShowID();

                        ticketTransaction2.setTotalPrice(ticketPrice+1);


                        TicketServices ticket = new TicketServices(ticketTransaction, showID, ShowServices.this);
                        ticket.start();

                        //Delete the // for checking threads of 2 user that try to buy the same ticket
                        //TicketServices test1 = new TicketServices(ticketTransaction2, showID2, ShowServices.this);
                        //test1.start();


                    }
                });
            }
        });
    }

    //Get all show transaction tickets
    public void getTransactionsShow(long showID, String userName, String userEmail) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(ShowServices.this);
        ticketServices.getTransactionsShow(showID, userName, userEmail, requestQueue, new TicketServices.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText( ShowServices.this, "ERROR Hall" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(List<TicketTransaction> list) {

                // create the hall ass array
                createHall(hall, list);
            }
        });
    }

    // create hall seats from showTickets
    public void createHall(int[][] hall, List<TicketTransaction> showTickets) {

        int seat = 0;
        int rowSeat = 0;


        // create places with no seats
        for (int row = 0; row < hall.length; row++) {

            for (int col = 0; col < hall[row].length; col++) {

                if ( row == 2 || row == 10) {
                    hall[row][col] = STATUS_NO_SEAT; // no seat
                }
                else{
                    if ( (2 <  row &&  row < 10) || (10 <  row &&  row < 15) ) {
                        hall[row][col] = STATUS_AVAILABLE; // a seat
                    }
                }
            }
        }

        for (int col = 0; col < hall[0].length; col++) {

            for (int i = 0; i < hall.length; i++) {

                if ( col == 2 ) {

                    if( 14 < i && i < (hall[0].length) ) {
                        hall[i][col] = STATUS_NO_SEAT; // no seat
                    }
                }
                if ( col == 3 ) {

                    if( 2 < i && i < (hall[0].length -4) ) {
                        hall[i][col] = STATUS_NO_SEAT; // no seat
                    }
                }
                if ( col == (17) ){

                    if( 14 < i && i < (hall[0].length) ) {
                        hall[i][col] = STATUS_NO_SEAT; // no seat
                    }
                }
                if ( col == (16) ){

                    if( 2 < i && i < (hall[0].length -4) ) {
                        hall[i][col] = STATUS_NO_SEAT; // no seat
                    }
                }
                if( col == 4 || col == (15) ){

                    if ( 0 < i && i < 5 ){
                        hall[i][col] = STATUS_NO_SEAT; // no seat
                    }
                }
                if( col == 0 || (col == (19)) ){

                    if ( 0 <= i && i < 5 ){
                        hall[i][col] = STATUS_NO_SEAT; // no seat
                    }
                }
                if( col == 9 ){

                    if ( 6 < i && i < 11 ){
                        hall[i][col] = STATUS_NO_SEAT; // no seat

                    }
                }
            }
        }

        hall[8][3] = STATUS_NO_SEAT;
        hall[8][16] = STATUS_NO_SEAT;
        hall[10][4] = STATUS_NO_SEAT;
        hall[10][15] = STATUS_NO_SEAT;
        // end create places with no seats

        // create available seats
        for (int row = 0; row < hall.length; row++) {
            for (int j = 0; j < hall[0].length; j++) {
                if (hall[row][j] != STATUS_NO_SEAT) // if there is a seat
                    hall[row][j] = STATUS_AVAILABLE; // seat available
            }
        }

        // create booked seats
        for( int i = 0; i < showTickets.size(); i++ ){

            seat = showTickets.get(i).getTicket().getSeat() ;
            rowSeat = showTickets.get(i).getTicket().getRowSeat() -1;

            if( seat != 0 ){

                while( 20 < seat ){ // get right col

                    seat = seat - 20;
                }
                if (hall[rowSeat][seat-1] != -1) {// if there is a seat

                    hall[rowSeat][seat-1] = STATUS_BOOKED;
                }
            }
        }

        // create hall layout from hall array
        createHallLayout(hall);
    }

    // create hall seats layout from hall array
    public void createHallLayout(int[][] hall) {

        int seatSize = 100;
        int seatGaping = 10;

        layout = findViewById(R.id.layoutSeat);

        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
        layout.addView(layoutSeat);

        int count = 0;

        LinearLayout layout = new LinearLayout(this);

        // create hall layout
        for (int i = 0; i < hall.length; i++) {

            for (int j = 0; j < hall[i].length; j++) {
                count++;
                if (hall[i][j] == STATUS_NO_SEAT) { // no seat

                    TextView view = new TextView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                    view.setLayoutParams(layoutParams);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    view.setText("");
                    layout.addView(view);

                }
                else{ // create a seat

                    TextView view = new TextView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                    view.setPadding(0, 0, 0, 2 * seatGaping);
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                    view.setLayoutParams(layoutParams);
                    view.setGravity(Gravity.CENTER);
                    view.setText(count + "");
                    view.setId(count);

                    if (hall[i][j] == STATUS_BOOKED) { // seat booked

                        view.setBackgroundResource(R.drawable.ic_seats_booked);
                        view.setTextColor(Color.WHITE);
                        view.setTag(STATUS_BOOKED);
                    }
                    if (hall[i][j] == STATUS_AVAILABLE) { // seat available

                        view.setBackgroundResource(R.drawable.ic_seats_book);
                        view.setTextColor(Color.BLACK);
                        view.setTag(STATUS_AVAILABLE);
                    }
                    if (hall[i][j] == STATUS_RESERVED) { // seat reserved

                        view.setBackgroundResource(R.drawable.ic_seats_reserved);
                        view.setTextColor(Color.WHITE);
                        view.setTag(STATUS_RESERVED);
                    }
                    layout.addView(view);
                    seatViewList.add(view);
                    view.setOnClickListener(this);
                }
            }
            layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layoutSeat.addView(layout);
        }
    }

    @Override
    public void onClick(View view) {

        int seatTicket = 0;
        int rowTicket = 0;
        int colHall = 20;
        int seat = view.getId();

        if ((int) view.getTag() == STATUS_AVAILABLE) { // clicked on available seat

            if (selectedIds.contains(view.getId() + ",")) { // make seat from reserved to available

                selectedIds = selectedIds.replace(+view.getId() + ",", "");
                view.setBackgroundResource(R.drawable.ic_seats_book);

                for (int i = 0; i < userSeats[0].length; i++) { // delete reserved seat from data

                    if (userSeats[0][i] == seat) { // available place to save a seat

                        userSeats[0][i] = 0;
                        userSeats[1][i] = 0;

                        reservedSeats--;
                        i = 10;
                    }
                }
                rowTicket = seat / colHall;
                if ((seat % colHall) != 0)
                    rowTicket = rowTicket + 1;

                Toast.makeText(this, "Seat " + view.getId() + " row: " + rowTicket , Toast.LENGTH_SHORT).show();
            } else { // make seat from available to reserved

                if (10 <= reservedSeats) { // user not allow to invited more then 10 tickets in one time
                    Toast.makeText(this, "You reserved the max number of seats", Toast.LENGTH_SHORT).show();
                }
                else{
                    reservedSeats++; // count how many seats user reserved

                    selectedIds = selectedIds + view.getId() + ",";
                    view.setBackgroundResource(R.drawable.ic_seats_selected);


                    rowTicket = seat / colHall;
                    if ((seat % colHall) != 0)
                        rowTicket = rowTicket + 1;

                    for (int i = 0; i < userSeats[0].length; i++) {

                        if (userSeats[0][i] == 0) { // available place to save a seat

                            userSeats[0][i] = seat;
                            userSeats[1][i] = rowTicket;

                            i = 10;
                        }
                    }
                    Toast.makeText(this, "Seat " + view.getId() + " row: " + rowTicket , Toast.LENGTH_SHORT).show();
                }
            }
        } else if ((int) view.getTag() == STATUS_BOOKED) { // seat booked
            Toast.makeText(this, "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show();
        } else if ((int) view.getTag() == STATUS_RESERVED) { // seat reserved
            Toast.makeText(this, "Seat " + view.getId() + " is Reserved", Toast.LENGTH_SHORT).show();
        }
    }

}
