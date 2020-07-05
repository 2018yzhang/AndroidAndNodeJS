package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

class urlData {
    static String url = "";
}
class keywordValue {
    static String keywords = "";
}

public class GridActivity extends AppCompatActivity {
    String resultCount;
    ProgressBar processBar;
    TextView loadting_text;
    TextView norecord;
    TextView resultTips;
    RequestQueue queue;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    LinearLayout showResults;
    String keywords;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        processBar=(ProgressBar)findViewById(R.id.progressBar1);
        processBar.setVisibility(View.VISIBLE);
        loadting_text = (TextView)findViewById(R.id.loading_text1);
        loadting_text.setVisibility(View.VISIBLE);

        norecord = (TextView)findViewById(R.id.norecord);
        norecord.setVisibility(View.GONE);

        resultTips = (TextView)findViewById(R.id.resultTips);
        resultTips.setVisibility(View.GONE);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeFresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                Intent intent = getIntent();
                String url = intent.getStringExtra("url");
                getResult(url);
            }
        });
        /**


         */



        recyclerView =(RecyclerView)findViewById(R.id.items);
        recyclerView.setVisibility(View.GONE);

        showResults = (LinearLayout)findViewById(R.id.showResult);
        showResults.setVisibility(View.GONE);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if(url!=null && !url.isEmpty()) urlData.url = url;
        keywords = intent.getStringExtra("keywords");
        if(keywords!=null && !keywords.isEmpty()) keywordValue.keywords = keywords;
        queue = Volley.newRequestQueue(this);

        getResult(url);


    }

    public void getResult(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlData.url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try{
                    JSONArray jsonArray1 = response.getJSONArray("findItemsAdvancedResponse");
                    resultCount = jsonArray1.getJSONObject(0).getJSONArray("paginationOutput").getJSONObject(0).getJSONArray("totalEntries").getString(0);


                    if(resultCount.equals("0")){

                        showResult(resultCount, jsonArray1);
                    }
                    else{

                        JSONArray result = jsonArray1.getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
                        JSONArray finalResult = clearResult(result);
                        showResult(resultCount, finalResult);
                    }
                    if(swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObjectRequest);
        /**
         processBar.setVisibility(View.GONE);
         loadting_text.setVisibility(View.GONE);
         int resultCount = 5;
         if(resultCount==0){
         norecord.setVisibility(View.VISIBLE);
         Toast.makeText(getApplicationContext(),"No Records",Toast.LENGTH_SHORT).show();
         }
         else{
         /**
         swipeRefresh.setVisibility(View.VISIBLE);

         swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
        //textView.setText("Tutorialspoint.com "+i);
        swipeRefresh.setRefreshing(false);
        }
        });
        recyclerView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter();

    }
         */


    }
    public JSONArray clearResult(JSONArray result) throws JSONException {

        JSONArray finalresult = new JSONArray();
        for(int i=0; i<result.length();i++ ){
            JSONObject item = result.getJSONObject(i);
            System.out.println(item.has("condition"));
            if((item.has("itemId"))&&(item.has("galleryURL"))&&(item.has("title"))
                    &&(item.has("topRatedListing"))
                    &&(item.has("shippingInfo"))&&(item.has("sellingStatus"))&&(item.has("viewItemURL"))){
            /*if((!item.getJSONArray("itemId").getString(0).isEmpty())&&(!item.getJSONArray("galleryURL").getString(0).isEmpty())&&(!item.getJSONArray("title").getString(0).isEmpty())
                    &&(item.getJSONArray("condition").length()>0)&&(!item.getJSONArray("topRatedListing").getString(0).isEmpty())
                    &&(item.getJSONArray("shippingInfo").length()>0)&&(item.getJSONArray("sellingStatus").length()>0)){
             */
                String itemId = item.getJSONArray("itemId").getString(0);
                String galleryURL = "";
                if(item.getJSONArray("galleryURL").getString(0).equals("https://thumbs1.ebaystatic.com/pict/04040_0.jpg")){
                    galleryURL = "https://www.csci571.com/hw/hw6/images/ebay_default.jpg";
                }
                else{
                    galleryURL = item.getJSONArray("galleryURL").getString(0);
                }
                String viewItemURL  = item.getJSONArray("viewItemURL").getString(0);;


                String title = item.getJSONArray("title").getString(0);
                String conditionDisplay = "";
                JSONObject con;
                if(item.has("condition")){
                    con = item.getJSONArray("condition").getJSONObject(0);
                    if(con.has("conditionDisplayName")){
                        conditionDisplay = con.getJSONArray("conditionDisplayName").getString(0);
                    }
                }
                else{
                    conditionDisplay = "N/A";
                }
                String topRatedListing = item.getJSONArray("topRatedListing").getString(0);
                //JSONArray shipping = item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost");
                String shippingServiceCost = "" ;
                JSONObject obj = item.getJSONArray("shippingInfo").getJSONObject(0);
                if (obj.has("shippingServiceCost")) {
                    shippingServiceCost = obj.getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                }

                String price = "";
                JSONObject obj1 = item.getJSONArray("sellingStatus").getJSONObject(0);
                if(obj1.has("convertedCurrentPrice")){
                    price = obj1.getJSONArray("convertedCurrentPrice").getJSONObject(0).getString("__value__");
                }
                //&&(!shippingServiceCost.isEmpty())&&(!price.isEmpty())
                System.out.println("--------------------------");
                System.out.println(conditionDisplay.isEmpty());
                System.out.println("--------------------------");
                System.out.println(shippingServiceCost.isEmpty());
                System.out.println("--------------------------");
                System.out.println(price.isEmpty());
                if((!conditionDisplay.isEmpty())&&(!shippingServiceCost.isEmpty())&&(!price.isEmpty())){
                    JSONObject temp = new JSONObject();
                    temp.put("itemId", itemId);
                    temp.put("viewItemURL", viewItemURL);
                    temp.put("galleryURL",galleryURL);
                    temp.put("title",title);
                    temp.put("conditionDisplay",conditionDisplay);
                    temp.put("topRatedListing",topRatedListing);
                    temp.put("shippingServiceCost",shippingServiceCost);
                    temp.put("price",price);
                    temp.put("shippingInfo",obj);
                    finalresult.put(temp);
                }

            }

        }
        return finalresult;
    }
    public void showResult(String resultCount, JSONArray finalResult){

        processBar.setVisibility(View.GONE);
        loadting_text.setVisibility(View.GONE);
        showResults.setVisibility(View.VISIBLE);
        System.out.println("------------------------------");

        if(resultCount.equals("0")){
            norecord.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"No Records",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);

            if(Integer.parseInt(resultCount)>50){
                String value = "<b>Showing <span style='color:#5da3cf'>50</span> results for <span style='color:#5da3cf'>"+keywordValue.keywords+"</span></b>";
                resultTips.setText(HtmlCompat.fromHtml(value,0));
            }
            else{
                String value = "<b>Showing <span style='color:#5da3cf'>"+resultCount+"</span> results for <span style='color:#5da3cf'>"+keywordValue.keywords+"</span></b>";
                resultTips.setText(HtmlCompat.fromHtml(value,0));
            }
            System.out.println(finalResult);
            resultTips.setVisibility(View.VISIBLE);
            itemAdapter = new ItemAdapter(finalResult);
            RecyclerView.LayoutManager manager = new GridLayoutManager(this,
                    2);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(itemAdapter);

            //openGridActivity(finalResult);
        }
    }
}