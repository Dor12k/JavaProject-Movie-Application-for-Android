package com.dor.cmovies.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dor.cmovies.HomePage;
import com.dor.cmovies.R;
import com.dor.cmovies.listAdapter.TicketsTransactionAdapter;
import com.dor.cmovies.TicketPage;
import com.dor.cmovies.models.Show;
import com.dor.cmovies.models.TicketTransaction;
import com.dor.cmovies.models.User;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserServices extends AppCompatActivity {


    private String MSG = "tickets cost";
    private String USERNAME = "username";
    private String userEmail = "userEmail";
    private final static String TAG = "UserProfile";
    //Need to change localhost to current ip
    private static final String QUERY_FOR_USERS = "http://localhost:8080/movies/webapi/user/";

    private ArrayList<TicketTransaction> transactions = new ArrayList<>();
    private final TicketServices ticketServices = new TicketServices(UserServices.this);

    Context context;

    public UserServices(){}

    public UserServices(Context context){
        this.context = context;
    }

    public interface UserResponseListener{
        void onError(String message);
        void onResponse(User user);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);
        Log.d(TAG, "onCreate: Started.");

        ImageView userImage = (ImageView) findViewById(R.id.upUserImage2);
        userImage.setImageResource(R.drawable.profile);

        Intent intent = getIntent();
        MSG = intent.getStringExtra("MSG");
        USERNAME = intent.getStringExtra("userName");
        userEmail = intent.getStringExtra("userEmail");

        getUser(USERNAME); // build user profile

        Button backToHomePage = (Button) findViewById(R.id.upGoToHomePageBtn);
        backToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProfile = new Intent(UserServices.this, HomePage.class);
                userProfile.putExtra("userName", USERNAME);
                userProfile.putExtra("userEmail", userEmail);
                startActivity(userProfile);
            }
        });
    }

    public void editProfile(User user){

        String ticketID = "Ticket ID";
        String showID = "Show ID";
        String showName = "Show \nName";
        String showVenue = "Show \nVenue";
        String showDate = "Show \nDate";
        String showTimeStart = "Show \nTime Start";
        String quantity = "Quantity";
        String ticketPrice = "Ticket \nPrice";

        TextView ticketID2 = findViewById(R.id.upTicketID2);
        TextView showID2 = findViewById(R.id.upShowID2);
        TextView showName2 = findViewById(R.id.upShowName2);
        TextView showVenue2 = findViewById(R.id.upShowVenue2);
        TextView showDate2 = findViewById(R.id.upShowDate2);
        TextView showTimeStart2 = findViewById(R.id.upShowTimeStart2);
        TextView quantity2 = findViewById(R.id.upQuantity2);
        TextView ticketPrice2 = findViewById(R.id.upTicketPrice2);

        ticketID2.setText(ticketID);
        showID2.setText(showID);
        showName2.setText(showName);
        showVenue2.setText(showVenue);
        showDate2.setText(showDate);
        showTimeStart2.setText(showTimeStart);
        quantity2.setText(quantity);
        ticketPrice2.setText(ticketPrice);

        TextView userName2 = findViewById(R.id.upUsernameTXT);
        TextView firstName2 = findViewById(R.id.upFirstNameTXT);
        TextView lastName2 = findViewById(R.id.upLastNameTXT);
        TextView userEmail2 = findViewById(R.id.upUserEmailTXT);

        userName2.setText(user.getUserName());
        firstName2.setText(user.getFirstName());
        lastName2.setText(user.getLastName());
        userEmail2.setText(user.getUserEmail());

        String email = user.getUserEmail();

        try {
            getUserTransactionsList(email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUser(String username){

        RequestQueue requestQueue = Volley.newRequestQueue(UserServices.this);
        this.getUser(username, requestQueue, new UserResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(UserServices.this, "EROR HomePage" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(User user) { // set user profile details
                editProfile(user);
            }
        });
    }

    public void getUser(String userEmail, RequestQueue requestQueue, final UserResponseListener userResponseListener ){

        String URL = QUERY_FOR_USERS + userEmail;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonResponse) {
                User user = new User();
                try{
                    user = getUserFromJason(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userResponseListener.onResponse(user);
            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( context, "\nERROR", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(objectRequest);
    }

    public void getUserTransactionsList(String userEmail) throws JSONException {

        RequestQueue requestQueue = Volley.newRequestQueue(UserServices.this);
        ticketServices.getUserTransactionsList(userEmail, requestQueue, new TicketServices.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText( UserServices.this, "ERROR UserProfile" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(List<TicketTransaction> list) {
                userTicketsTransactionAdapter(list);
            }
        });
    }

    public void userTicketsTransactionAdapter(final List<TicketTransaction> list){

        transactions = (ArrayList<TicketTransaction>) list;

        ListView mListView = (ListView) findViewById(R.id.upTransactionListView);
        TicketsTransactionAdapter adapter = new TicketsTransactionAdapter(UserServices.this, R.layout.user_profile_adapter, transactions, "userTransactions");
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: User Ticket: " + transactions.get(position));

                long transactionID = transactions.get(position).getTransactionID();
                String movieImage = transactions.get(position).getTicket().getShow().getImageURL();

                Intent transaction = new Intent(UserServices.this, TicketPage.class);
                transaction.putExtra("userName", USERNAME);
                transaction.putExtra("userEmail", userEmail);
                transaction.putExtra("movieImage", movieImage);
                transaction.putExtra("transactionID", transactionID);

                startActivity(transaction);

            }
        });
    }

    public User getUserFromJason(JSONObject jsonResponse) throws JSONException {

        String userName = jsonResponse.getString("userName");
        String firstName = jsonResponse.getString("firstName");
        String lastName = jsonResponse.getString("lastName");
        String userEmail = jsonResponse.getString("userEmail");
        String userPassword = jsonResponse.getString("userPassword");
        String isAdmin = jsonResponse.getString("isAdmin");

        if( isAdmin.equals("yes")) {
            return new User(userName, firstName, lastName, userEmail, userPassword, true);
        }
        else {
            return new User(userName, firstName, lastName, userEmail, userPassword, false);
        }
    }
}
