package com.dor.cmovies.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.dor.cmovies.MySingleton;
import com.dor.cmovies.models.Show;
import com.dor.cmovies.models.Ticket;
import com.dor.cmovies.models.TicketTransaction;
import com.dor.cmovies.models.User;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TicketServices extends Thread{

    int c = 0;
    int count = 0;
    long showID = 0;
    private String MSG = "0";
    private Show addedShow;
    private Context context;
    private List<TicketTransaction> list;
    private TicketTransaction ticketTransaction;
    //Need to change localhost to current ip
    public static final String QUERY_FOR_TRANSACTION = "http://localhost:8080/movies/webapi/tickets/";

    public TicketServices(){}

    public TicketServices(TicketTransaction ticketTransaction1, Context context){
        this.ticketTransaction = ticketTransaction1;
        this.context = context;
    }

    public TicketServices(TicketTransaction ticketTransaction1, long showID, Context context){

        this.showID = showID;
        this.context = context;
        this.ticketTransaction = ticketTransaction1;
    }

    public TicketServices(Context context){
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);
        void onResponse(List<TicketTransaction> list);
    }

    public interface TransactionResponseListener {
        void onError(String message);
        void onResponse(TicketTransaction transaction);
    }

    public void run() {

        synchronized (this.getClass()){

            try {
                this.buySelectedTicket();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized void buySelectedTicket() throws JSONException {

        buySelectedTicket(showID);

    }

    // this method make sure that two users does not buy the same ticket
    public void buySelectedTicket(long showID) throws JSONException {

        String USERNAME = this.ticketTransaction.getUser().getUserName();
        String USER_EMAIL = this.ticketTransaction.getUser().getUserEmail();
        String URL = QUERY_FOR_TRANSACTION + USERNAME + "/" + USER_EMAIL + "/" + showID;

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("showID", showID);

        RequestFuture<JSONArray> arrayRequestFuture = RequestFuture.newFuture();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, arrayRequestFuture, arrayRequestFuture);

        MySingleton.getInstance(context).addToRequestQueue(arrayRequest);

        try {

            JSONArray response = arrayRequestFuture.get(60, TimeUnit.SECONDS);

            List<TicketTransaction> list = new ArrayList<>();
            TicketTransaction transaction = new TicketTransaction();

            for(int n = 0; n < response.length(); n++){

                JSONObject jsonResponse = response.getJSONObject(n);
                transaction = getTransactionFromJson(jsonResponse);
                list.add(transaction);

            }

            int availableSeats = 1;
            int unavailableSeats = 0;
            int status = availableSeats;

            for (int i = 0; i < list.size(); i++) {

                int seat = ticketTransaction.getTicket().getSeat();
                int rowSeat = ticketTransaction.getTicket().getRowSeat();

                if (list.get(i).getTicket().getSeat() == seat && list.get(i).getTicket().getRowSeat() == rowSeat) {

                    status = unavailableSeats;
                    String message = "Seat: " + seat + " is not available. please try again";

                    Intent hall = new Intent(context, ShowServices.class);
                    hall.putExtra("MSG", message);
                    hall.putExtra("showID", showID);
                    hall.putExtra("userName", ticketTransaction.getUser().getUserName());
                    context.startActivity(hall);
                }
            }

            if (status == availableSeats) {

                try {

                    JSONObject transactionParams = getJsonFromTransaction(ticketTransaction);
                    RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, QUERY_FOR_TRANSACTION, transactionParams, requestFuture, requestFuture);
                    MySingleton.getInstance(context).addToRequestQueue(request);

                    try {

                        JSONObject object= requestFuture.get(60,TimeUnit.SECONDS);

                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                      catch (ExecutionException e) {
                        e.printStackTrace();

                    }
                      catch(TimeoutException e) {

                        e.printStackTrace();
                    }

                    Intent buyTicket = new Intent(context, UserServices.class);
                    buyTicket.putExtra("showID", showID);
                    buyTicket.putExtra("userName", ticketTransaction.getUser().getUserName());
                    context.startActivity(buyTicket);

                } catch (JSONException e) {
                    Toast.makeText(context, "\n ERROR TicketServices", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        } catch (InterruptedException e) {

            e.printStackTrace();
        }
         catch (ExecutionException e) {
             e.printStackTrace();

        }
        catch(TimeoutException e) {

             e.printStackTrace();
        }

    }

    public void getTransactionsShow(long showID, String userName, String userEmail, RequestQueue requestQueue, final VolleyResponseListener volleyResponseListener) throws JSONException {

        String URL = QUERY_FOR_TRANSACTION + userName + "/" + userEmail + "/" + showID;

        JSONObject jsonParams = new JSONObject();
                   jsonParams.put("showID", showID);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                List<TicketTransaction> list = new ArrayList<>();
                TicketTransaction transaction = new TicketTransaction();
                try {
                    for(int n = 0; n < response.length(); n++){

                        JSONObject jsonResponse = response.getJSONObject(n);
                        transaction = getTransactionFromJson(jsonResponse);
                        list.add(transaction);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( context, "\nERROR" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(arrayRequest);
    }

    public void getUserTransactionsList(String userEmail, RequestQueue requestQueue, final VolleyResponseListener volleyResponseListener) throws JSONException {

        String URL = QUERY_FOR_TRANSACTION + userEmail;

        JSONObject jsonParams = new JSONObject();
                   jsonParams.put("userEmail", userEmail);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                List<TicketTransaction> list = new ArrayList<>();
                TicketTransaction transaction = new TicketTransaction();
                try {
                    for(int n = 0; n < response.length(); n++)
                    {
                        JSONObject jsonResponse = response.getJSONObject(n);
                        transaction = getTransactionFromJson(jsonResponse);
                        list.add(transaction);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( context, "\nERROR" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(arrayRequest);
    }

    public void getTransaction(long transactionID, String userEmail, RequestQueue requestQueue, final TransactionResponseListener transactionResponseListener) throws JSONException {

        String URL = QUERY_FOR_TRANSACTION + userEmail + "/" + transactionID;

        JSONObject jsonParams = new JSONObject();
        //jsonParams.put("userName", );
        jsonParams.put("userEmail", userEmail);
        jsonParams.put("transactionID",transactionID);


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, jsonParams, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                TicketTransaction transaction = new TicketTransaction();
                try {
                    transaction = getTransactionFromJson(response);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                transactionResponseListener.onResponse(transaction);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( context, "\nERROR" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e( "Rest Response", error.toString());

            }
        });
        requestQueue.add(objectRequest);
    }

    public JSONObject getJsonFromTransaction(TicketTransaction ticketTransaction) throws JSONException {

        JSONObject jsonParams = new JSONObject();

        jsonParams.put("transactionID", 0);
        jsonParams.put("quantity", ticketTransaction.getQuantity());
        jsonParams.put("totalPrice", ticketTransaction.getTotalPrice());

        JSONObject ticketParams = getJsonFromTicket(ticketTransaction.getTicket());
        JSONObject userParams = getJsonFromUser(ticketTransaction.getUser());

        jsonParams.put("ticket", ticketParams);
        jsonParams.put("user", userParams);

        return jsonParams;
    }

    public JSONObject getJsonFromTicket(Ticket ticket) throws JSONException {

        JSONObject jsonParams = new JSONObject();

        JSONObject show = getJsonFromShow(ticket.getShow());

        jsonParams.put("purchasDate", ticket.getPurchasDate());
        jsonParams.put("rowSeat", ticket.getRowSeat());
        jsonParams.put("seat", ticket.getSeat());

        jsonParams.put("show", show);

        return jsonParams;
    }

    public JSONObject getJsonFromShow(Show show) throws JSONException {

        JSONObject jsonParams = new JSONObject();

        jsonParams.put("showID", show.getShowID());
        jsonParams.put("showName", show.getShowName());
        jsonParams.put("artistName", show.getArtistName());
        jsonParams.put("showDescription", show.getShowDescription());
        jsonParams.put("showVenue", show.getShowVenue());
        jsonParams.put("showDate", show.getShowDate());
        jsonParams.put("showTimeStart", show.getShowTimeStart());
        jsonParams.put("ticketPrice", show.getTicketPrice());
        jsonParams.put("numberOfTickets", show.getNumberOfTickets());
        jsonParams.put("ticketsInvited", show.getTicketsInvited());
        jsonParams.put("ticketsAvailable", show.getTicketsAvailable());

        return jsonParams;
    }

    public JSONObject getJsonFromUser(User user) throws JSONException {

        JSONObject userParams = new JSONObject();

        userParams.put("userName", user.getUserName());
        userParams.put("firstName", user.getFirstName());
        userParams.put("lastName", user.getLastName());
        userParams.put("userEmail", user.getUserEmail());
        userParams.put("userPassword", user.getUserPassword());
        userParams.put("isAdmin", "no");

        return userParams;
    }

    public TicketTransaction getTransactionFromJson(JSONObject jsonResponse) throws JSONException {

        int quantity = jsonResponse.getInt("quantity");
        double totalPrice = jsonResponse.getDouble("totalPrice");
        long transactionID = jsonResponse.getLong("transactionID");

        JSONObject jTicket = jsonResponse.getJSONObject("ticket");

        JSONObject jShow = jTicket.getJSONObject("show");

        long showID = jShow.getLong("showID");
        String showName = jShow.getString("showName");
        String artistName = jShow.getString("artistName");
        String showDescription = jShow.getString("showDescription");
        String showVenue = jShow.getString("showVenue");
        String showDate = jShow.getString("showDate");
        String showTimeStart = jShow.getString("showTimeStart");
        double ticketPrice = jShow.getDouble("ticketPrice");
        int numberOfTickets = jShow.getInt("numberOfTickets");
        int ticketsInvited = jShow.getInt("ticketsInvited");
        int ticketsAvailable = jShow.getInt("ticketsAvailable");
        String imageUrl = jShow.getString("imageURL");
        Show show = new Show(showID, showName, artistName, showDescription, showVenue, showDate, showTimeStart, ticketPrice, numberOfTickets, ticketsInvited, ticketsAvailable, imageUrl );


        int seat = jTicket.getInt("seat");
        int rowSeat = jTicket.getInt("rowSeat");
        String purchaseDate = jTicket.getString("purchasDate");
        Ticket ticket = new Ticket(show, purchaseDate, seat, rowSeat);

        JSONObject jUser = jsonResponse.getJSONObject("user");

        String userName = jUser.getString("userName");
        String firstName = jUser.getString("firstName");
        String lastName = jUser.getString("lastName");
        String userEmail = jUser.getString("userEmail");
        String userPassword = jUser.getString("userPassword");
        boolean isAdmin = jUser.getBoolean("isAdmin");

        User user = new User(userName,firstName,lastName,userEmail,userPassword,isAdmin);

        TicketTransaction transaction = new TicketTransaction(transactionID, ticket, user, quantity, totalPrice);
        return transaction;
    }
}
