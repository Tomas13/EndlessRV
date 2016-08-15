package learn.example.com.endlessrv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String urlJsonObj = "http://crm.astanalrt.com/notifications/getnotifications/";
    private ArrayList<News> newsList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private ListViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView lvItems = (ListView) findViewById(R.id.lvItems);

        makeJsonObjectRequest(1);

        mAdapter = new ListViewAdapter(getApplicationContext(), newsList);
        lvItems.setAdapter(mAdapter);


        // Attach the listener to the AdapterView onCreate
        lvItems.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });




    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
//        newsList.clear();

        Toast.makeText(MainActivity.this, "CustomLoadMore " + offset, Toast.LENGTH_SHORT).show();
        makeJsonObjectRequest(offset);

    }

    private void makeJsonObjectRequest(final int page) {

//        progressBar.setVisibility(View.VISIBLE);


//        newsList.clear();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj + page, new JSONObject(), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                String json = response.toString();

                JSONTokener tokener = new JSONTokener(json);
                JSONObject finalResult = null;
                try {
                    finalResult = new JSONObject(tokener);


                    if (finalResult.getJSONArray("data").length() == 0) {

//                        noNewsTextView.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);

                    } else if (finalResult.getJSONArray("data").length() < page) {
                        Toast.makeText(MainActivity.this, "END of list", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        newsList.clear();

                        for (int i = 0; i < finalResult.getJSONArray("data").length(); i++) {

                            Object object = finalResult.getJSONArray("data").getJSONArray(i);
                            JSONArray jsonArray = new JSONArray(object.toString());

                            String date = jsonArray.getString(0);
                            String text = jsonArray.getString(1);
                            News movie = new News(date, text);

                            newsList.add(movie);
                        }

                        mAdapter.notifyDataSetChanged();

                    }

                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        Application.getInstance().addToRequestQueue(jsonObjReq);
    }
}
