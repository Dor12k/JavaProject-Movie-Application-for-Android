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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowsServices {

    //Need to change localhost to current ip
    public static final String QUERY_FOR_SHOW = "http://localhost:8080/movies/webapi/shows/";

    private Context context;
    private List<Show> list;
    private Show addedShow;

    public ShowsServices(){}

    public ShowsServices(Context context){
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);
        void onResponse(List<Show> list);
    }

    public interface ShowResponseListener{
        void onError(String message);
        void onResponse(Show show);
    }

    public void editShow(Show show, RequestQueue requestQueue) throws JSONException {
        String URL = QUERY_FOR_SHOW;

        JSONObject jsonParams = getJsonFromShow(show);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, URL, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonResponse) {
                Toast.makeText( context, "\n Movie Edited", Toast.LENGTH_SHORT).show();
            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "\nERROR", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(objectRequest);
    }

    public void insertShow(final Show show, RequestQueue requestQueue) throws JSONException {

        String URL = QUERY_FOR_SHOW;

        JSONObject jsonParams = getJsonFromShow(show);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonResponse) {
                Toast.makeText( context, "\n Movie Added", Toast.LENGTH_SHORT).show();
            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( context, "\n ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(objectRequest);
    }

    public void getShowsList(RequestQueue requestQueue, final VolleyResponseListener volleyResponseListener) {

        String URL = QUERY_FOR_SHOW;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                List<Show> list = new ArrayList<>();
                try {
                    Show show = new Show();
                    for(int n = 0; n < response.length(); n++)
                    {
                        JSONObject jsonResponse = response.getJSONObject(n);
                        show = getShowFromJson(jsonResponse);
                        list.add(show);
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

    public void deleteShow(long showID, RequestQueue requestQueue) throws JSONException {

        String URL = QUERY_FOR_SHOW + showID;

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("showID", showID);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText( context, "\nMovie Deleted", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "\nERROR", Toast.LENGTH_SHORT).show();
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

    public void getShow(long showID, RequestQueue requestQueue, final ShowResponseListener showResponseListener ) {
        String URL = QUERY_FOR_SHOW + showID;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonResponse) {
                Show show = new Show();
                try{
                    show = getShowFromJson(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showResponseListener.onResponse(show);
            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( context, "\nERROR", Toast.LENGTH_SHORT).show();
                Log.e( "Rest Response", error.toString());
            }
        });
        requestQueue.add(objectRequest);
    }

    public Show getShowFromJson(JSONObject jsonResponse) throws JSONException {

        long id = jsonResponse.getLong("showID");
        String showName = jsonResponse.getString("showName");
        String artistName = jsonResponse.getString("artistName");
        String showDescription = jsonResponse.getString("showDescription");
        String showVenue = jsonResponse.getString("showVenue");
        String showDate = jsonResponse.getString("showDate");
        String showTimeStart = jsonResponse.getString("showTimeStart");
        double ticketPrice = jsonResponse.getDouble("ticketPrice");
        int numberOfTickets = jsonResponse.getInt("numberOfTickets");
        int ticketsInvited = jsonResponse.getInt("ticketsInvited");
        int ticketsAvailable = jsonResponse.getInt("ticketsAvailable");
        String imageUrl = jsonResponse.getString("imageURL");
        Show show = new Show(id, showName, artistName, showDescription, showVenue, showDate, showTimeStart, ticketPrice, numberOfTickets, ticketsInvited, ticketsAvailable, imageUrl );
        return show;
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
        jsonParams.put("imageURL", show.getImageURL());

        return jsonParams;
    }

}
