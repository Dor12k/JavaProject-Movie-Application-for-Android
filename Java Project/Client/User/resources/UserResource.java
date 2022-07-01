package com.dor.cmovies.resources;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dor.cmovies.HomePage;
import com.dor.cmovies.MainPage;
import com.dor.cmovies.R;
import com.dor.cmovies.models.Authentication;
import com.dor.cmovies.models.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserResource extends AppCompatActivity {

    public static String USERNAME = "";
    public static String USER_EMAIL = "";
    //Need to change localhost to current ip
    public static final String QUERY_FOR_SHOW = "http://localhost:8080/cmovies/webapi/authentication/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        Intent intent = getIntent();


        ImageView imageView = findViewById(R.id.maImageView);
        Picasso.get()
                .load(R.drawable.movieslogo8)
                .fit()
                .into(imageView);


    }

    public void rRegisterBTN(View view) throws JSONException {

        EditText username = (EditText) findViewById(R.id.rUsernameEditTxt);
        String userName = username.getText().toString();

        EditText email = (EditText) findViewById(R.id.rUserEmailEditTxt);
        String userEmail = email.getText().toString();

        EditText firstname = (EditText) findViewById(R.id.rFirstEditTXT);
        String firstName = firstname.getText().toString();

        EditText lastname = (EditText) findViewById(R.id.rLastEditTXT);
        String lastName = lastname.getText().toString();

        EditText password = (EditText) findViewById(R.id.rPasswordEditTXT);
        String userPassword = password.getText().toString();

        USERNAME = userName;
        USER_EMAIL = userEmail;

        User user = new User(userName, firstName, lastName, userEmail, userPassword, false);

        if( userName.equals("") || userEmail.equals("")  || firstName.equals("")  || lastName.equals("")  || userPassword.equals("") ) {

            Toast.makeText(UserResource.this, "Please insert all fields", Toast.LENGTH_SHORT).show();

        } else {
            if (userEmail.indexOf('@') == -1 || userEmail.indexOf('.') == -1 ) {

                Toast.makeText(UserResource.this, "Please insert your email", Toast.LENGTH_SHORT).show();

            }else{

                String status = validation(user); // return failure/success login and user authentication
            }
        }
    }

    public String validation(User guest) throws JSONException {

        String validation = "Please try again";

        RequestQueue requestQueue = Volley.newRequestQueue(UserResource.this);

        JSONObject userParams = getJsonFromUser(guest);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, QUERY_FOR_SHOW, userParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String validation = response.getString("userStatus"); // return failure/success login and user authentication
                    login(validation); // manage failure/success login

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserResource.this, "\nERROR", Toast.LENGTH_SHORT).show();
                Log.e("Rest Response", error.toString());
            }
        });
        requestQueue.add(objectRequest);
        return validation;
    }

    public void login(String validation){

        if( validation.equals("isAdmin") ){
            Intent adminLogIn = new Intent(this, HomePage.class);
            adminLogIn.putExtra("userName", USERNAME);
            adminLogIn.putExtra("userEmail", USER_EMAIL);
            startActivity(adminLogIn);
        }
        else {
            if (validation.equals("isUser")) {
                Intent userLogIn = new Intent(this, HomePage.class);
                userLogIn.putExtra("userName", USERNAME);
                userLogIn.putExtra("userEmail", USER_EMAIL);
                startActivity(userLogIn);
            } else {
                Toast.makeText(UserResource.this, validation, Toast.LENGTH_SHORT).show();
            }
        }
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
