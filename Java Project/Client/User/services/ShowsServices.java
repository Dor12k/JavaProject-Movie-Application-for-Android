package com.dor.cmovies.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dor.cmovies.models.Show;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ShowsServices {

    private Context context;

    //Need to change localhost to current ip
    private static final String QUERY_FOR_SHOW = "http://localhost:8080/movies/webapi/shows/";

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

}
