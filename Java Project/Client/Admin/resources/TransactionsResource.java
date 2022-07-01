package com.dor.cmovies.resources;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dor.cmovies.R;
import com.dor.cmovies.listadapter.TicketsTransactionAdapter;
import com.dor.cmovies.models.TicketTransaction;
import com.dor.cmovies.services.TicketServices;
import com.dor.cmovies.services.UserServices;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TransactionsResource extends AppCompatActivity {

    private String userEmail = "";
    private String USERNAME = "";
    private String MSG = "";
    private long showID = 0;

    private final static String TAG = "TransactionsList";
    private ArrayList<TicketTransaction> transactions = new ArrayList<>();
    private final TicketServices ticketServices = new TicketServices(TransactionsResource.this);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions_layout);
        Log.d(TAG, "onCreate: Started.");

        Intent intent = getIntent();
        MSG = intent.getStringExtra("MSG");
        USERNAME = intent.getStringExtra("username");
        userEmail = intent.getStringExtra("userEmail");
        showID = intent.getLongExtra("showID", 0);

        if( MSG.equals("transactionShow")){ // get transaction list of a show
            try {
                getTransactionsShow(showID, USERNAME, userEmail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(MSG.equals("transactionsList")){ // get transaction list of all shows
            getTransactionsList();
        }

        Button refreshList = (Button)findViewById(R.id.tlRefreshBtn);
               refreshList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( !(MSG.equals("transactionShow")))
                             getTransactionsList();
                    }
                });
    }

    //Get all shows transactions tickets
    public void getTransactionsList(){

        RequestQueue requestQueue = Volley.newRequestQueue(TransactionsResource.this);
        ticketServices.getTransactionsList(requestQueue, new TicketServices.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText( TransactionsResource.this, "ERROR TransactionList" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(List<TicketTransaction> list) {
                //Toast.makeText( TransactionsResource.this, "Success" , Toast.LENGTH_SHORT).show();
                ticketsTransactionAdapter(list);
            }
        });
    }

    //Get all show transaction tickets
    public void getTransactionsShow(long showID, String username, String userEmail) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(TransactionsResource.this);
        ticketServices.getTransactionsShow(showID, username, userEmail, requestQueue, new TicketServices.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText( TransactionsResource.this, "ERROR Transaction List" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(List<TicketTransaction> list) {
                //Toast.makeText( TransactionsResource.this, "Success" , Toast.LENGTH_SHORT).show();
                ticketsTransactionAdapter(list);
            }
        });
    }

    //Create the list of transactions
    public void ticketsTransactionAdapter(final List<TicketTransaction> list){

        transactions = (ArrayList<TicketTransaction>) list;
        ListView mListView = (ListView) findViewById(R.id.tlistView);
        TicketsTransactionAdapter adapter = new TicketsTransactionAdapter(TransactionsResource.this, R.layout.transactions_adapter_layout, transactions, "showsTransactions");
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TicketTransaction transaction = transactions.get(position);
                Intent userProfile = new Intent(TransactionsResource.this, UserServices.class);
                userProfile.putExtra("userEmail", transaction.getUser().getUserEmail());
                startActivity(userProfile);
            }
        });
    }

}
