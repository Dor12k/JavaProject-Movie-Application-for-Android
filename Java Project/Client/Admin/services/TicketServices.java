package com.dor.cmovies.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

public class TicketServices {

    private Context context;
    private List<TicketTransaction> list;
    //Need to change localhost to current ip
    private static final String QUERY_FOR_TICKETS = "http://localhost:8080/movies/webapi/tickets/";

    public TicketServices(Context context){
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);
        void onResponse(List<TicketTransaction> list) throws JSONException;
    }

    public interface TransactionResponseListener {
        void onError(String message);
        void onResponse(TicketTransaction transaction);
    }

    public void getTransactionsShow(long showID, String username, String userEmail, RequestQueue requestQueue, final VolleyResponseListener volleyResponseListener) throws JSONException {

        String URL = QUERY_FOR_TICKETS + username + "/" + userEmail + "/" + String.valueOf(showID);

        JSONObject jsonParams = new JSONObject();
                   jsonParams.put("userName", username);
                   jsonParams.put("userEmail", userEmail);
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
                try {
                    volleyResponseListener.onResponse(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( context, "\nERROR" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(arrayRequest);
    }

    public void getTransactionsList(RequestQueue requestQueue, final VolleyResponseListener volleyResponseListener) {

        String URL = QUERY_FOR_TICKETS;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                List<TicketTransaction> list = new ArrayList<>();
                try {
                    TicketTransaction transaction = new TicketTransaction();
                    for(int n = 0; n < response.length(); n++)
                    {
                        JSONObject jsonResponse = response.getJSONObject(n);
                        transaction = getTransactionFromJson(jsonResponse);
                        list.add(transaction);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    volleyResponseListener.onResponse(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

        String URL = QUERY_FOR_TICKETS + userEmail;

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
                try {
                    volleyResponseListener.onResponse(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( context, "\nERROR" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(arrayRequest);
    }

    public void deleteTicketTransaction(long ticketID, RequestQueue requestQueue){

        String URL = QUERY_FOR_TICKETS + ticketID;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText( context, "\nTicket transaction deleted", Toast.LENGTH_SHORT).show();
                Log.e( "Rest Response", "\nTicket transaction deleted");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "\nERROR", Toast.LENGTH_SHORT).show();
                Log.e("Rest Response", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getTransaction(long transactionID, String userEmail, RequestQueue requestQueue, final TransactionResponseListener transactionResponseListener) throws JSONException {

        String URL = QUERY_FOR_TICKETS + userEmail + "/" + transactionID;

        JSONObject jsonParams = new JSONObject();
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

    public TicketTransaction getTransactionFromJson(JSONObject jsonResponse) throws JSONException {

        int quantity = jsonResponse.getInt("quantity");
        double totalPrice = jsonResponse.getDouble("totalPrice");
        long transactionID = jsonResponse.getLong("transactionID");


        JSONObject jTicket = jsonResponse.getJSONObject("ticket");

        int seat = jTicket.getInt("seat");
        int rowSeat = jTicket.getInt("rowSeat");
        String purchaseDate = jTicket.getString("purchasDate");

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

        Ticket ticket = new Ticket(seat, rowSeat, show, purchaseDate);

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
