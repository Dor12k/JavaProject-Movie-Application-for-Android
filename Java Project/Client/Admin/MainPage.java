package com.dor.cmovies;

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
import com.dor.cmovies.models.Authentication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainPage extends AppCompatActivity {

    public static String USERNAME ="";
    public static String USER_EMAIL = "";
    //Need to change localhost to current ip
    public static final String QUERY_FOR_USERS = "http://localhost:8080/movies/webapi/authentication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        ImageView imageView = findViewById(R.id.maImageView);
        Picasso.get()
               .load(R.drawable.movieslogo8)
               .fit()
               .into(imageView);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String userName = sharedPreferences.getString("UserName", null);
        String userPassword = sharedPreferences.getString("Password", null);

        if( userName != null){
            EditText etName = (EditText) findViewById(R.id.maUsernameEditTxt);
            etName.setText(userName);
        }
        if( userPassword != null){
            EditText etPassword = (EditText) findViewById(R.id.maPasswordEditTXT);
            etPassword.setText(userPassword);
        }
    }

    public void maLogInBTN(View view){

        EditText username = (EditText) findViewById(R.id.maUsernameEditTxt);
        String userName = username.getText().toString();

        EditText password = (EditText) findViewById(R.id.maPasswordEditTXT);
        String userPassword = password.getText().toString();

        USERNAME = userName;

        Authentication authentication = new Authentication(userName, userPassword);

        String status = validation(authentication); // return failure/success login and user authentication

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("UserName", userName);
        editor.putString("Password", userPassword);

        editor.commit();
    }

    public String validation(Authentication authentication){

        String validation = "Please try again";

        RequestQueue requestQueue = Volley.newRequestQueue(MainPage.this);

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("userName", authentication.getUserName());
        jsonParams.put("userPassword", authentication.getUserPassword());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, QUERY_FOR_USERS, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String validation = response.getString("userStatus"); // return failure/success login and user authentication

                    if( validation.equals("isUser") || validation.equals("isAdmin") )
                        USER_EMAIL = response.getString("userEmail");

                    login(validation, USER_EMAIL); // manage failure/success login
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainPage.this, "\nEROR", Toast.LENGTH_SHORT).show();
                Log.e("Rest Response", error.toString());
            }
        });
        requestQueue.add(objectRequest);
        return validation;
    }

    public void login(String validation, String userEmail){

        if( validation.equals("isAdmin") ){ // success login and move to home page
            Intent adminLogIn = new Intent(this, HomePage.class);
            adminLogIn.putExtra("username", USERNAME);
            adminLogIn.putExtra("userEmail", USER_EMAIL);
            startActivity(adminLogIn);
        }
        else { // login fail
            Toast.makeText(MainPage.this, validation, Toast.LENGTH_SHORT).show();
        }
    }
}
