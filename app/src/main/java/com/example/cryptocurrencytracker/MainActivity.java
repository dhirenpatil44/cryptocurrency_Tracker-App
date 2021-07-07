package com.example.cryptocurrencytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ArrayList<CurrencyModel> currencyModelArrayList;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        currencyModelArrayList = new ArrayList<>();

        customAdapter = new CustomAdapter(this,currencyModelArrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);

        fetchData();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterCurrency(s.toString());
            }
        });
    }

    private void filterCurrency(String currency){
        ArrayList<CurrencyModel> filterList = new ArrayList<>();

        for (CurrencyModel item : currencyModelArrayList){
            if(item.getName().toLowerCase().contains(currency.toLowerCase())){
                filterList.add(item);
            }
        }
        if (filterList.isEmpty()){
            Toast.makeText(this, "No currency found regarding your search", Toast.LENGTH_SHORT).show();
        }
        else {
            customAdapter.filterArrayList(filterList);
        }
    }

    private void fetchData(){
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue  requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String name = jsonObject.getString("name");
                        String symbol = jsonObject.getString("symbol");

                        JSONObject quote = jsonObject.getJSONObject("quote");
                        JSONObject USD = quote.getJSONObject("USD");

                        double price = USD.getDouble("price");

                        currencyModelArrayList.add(new CurrencyModel(name,symbol,price));

                    }
                    customAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "try again", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> header = new HashMap<>();
                header.put("X-CMC_PRO_API_KEY","523dbd7c-b5b8-4019-87e7-2cea85c08b7d");
                return header;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }
}