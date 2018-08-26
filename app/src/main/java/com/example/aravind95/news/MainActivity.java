package com.example.aravind95.news;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<NewsCard> newsList;
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        CardView cardView = findViewById(R.id.cardview);

        final JsonObjectRequest request = createRequest();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(request);
            }
        });

        loadNews(request);
    }

    private JsonObjectRequest createRequest() {
        String apiKey = "cba44ac468a847028d6f2b00c29d9807";
        String url = "https://newsapi.org/v2/top-headlines?country=in&" +
                "apiKey=" + apiKey;

        return new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray articles = null;
                newsList = new ArrayList<>();
                try {
                    articles = response.getJSONArray("articles");
                } catch (JSONException e) {
                    System.out.println(e.getMessage() + "at onResponse");
                    e.printStackTrace();
                }
                for (int index = 0; index < articles.length(); index++) {
                    JSONObject article = null;
                    String title = null, description = null, imageUrl = null;
                    try {
                        article = articles.getJSONObject(index);
                        title = article.getString("title");
                        description = article.getString("description");
                        imageUrl = article.getString("urlToImage");
                        newsList.add(new NewsCard(title, description, imageUrl));
                    } catch (JSONException e) {
                        System.out.println(e.getMessage() + "at onResponse");
                        e.printStackTrace();
                    }

                }

                newsAdapter = new NewsAdapter(newsList);
                recyclerView.setAdapter(newsAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this.getApplicationContext(), "You may not be connected to internet", Toast.LENGTH_SHORT)
                .show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private <T> void loadNews(Request<T> request) {
        GlobalRequestQueue.getInstance(this.getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
