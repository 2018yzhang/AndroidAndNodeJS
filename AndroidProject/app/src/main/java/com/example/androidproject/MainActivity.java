package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String url = "";
    String keywords = "";
    double maxprice = 0.0;
    double minprice = 0.0;
    String new_ = "";
    String used_ = "";
    String unspecified = "";
    String option = "";
    String optionURL = "";

    EditText keywordInput;
    EditText maxInput;
    EditText minInput;

    CheckBox new_Input, used_Input, unspecifiedInput;

    Button submitButton;
    Button clearButton;


    TextView keywordWarning;
    TextView priceWarning;
    Spinner sortSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keywordWarning = (TextView)findViewById(R.id.keywords_warning);
        keywordWarning.setVisibility(View.GONE);
        priceWarning = (TextView)findViewById(R.id.price_warning);
        priceWarning.setVisibility(View.GONE);

        keywordInput = (EditText)findViewById(R.id.keywords);
        maxInput = (EditText)findViewById(R.id.maxprice);
        minInput = (EditText)findViewById(R.id.minprice);

        new_Input = findViewById(R.id.new_);
        used_Input = findViewById(R.id.used_);
        unspecifiedInput = findViewById(R.id.unspecified_);

         sortSpinner = findViewById(R.id.sortBy);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this, R.array.sortBy, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(this);

        submitButton = (Button)findViewById(R.id.submit);
        //queue = Volley.newRequestQueue(this);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResult();
            }
        });
        clearButton = (Button)findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                keywordWarning.setVisibility(View.GONE);

                priceWarning.setVisibility(View.GONE);

                keywordInput.setText("");
                maxInput.setText("");
                minInput.setText("");

                new_Input.setChecked(false);
                used_Input.setChecked(false);
                unspecifiedInput.setChecked(false);
                sortSpinner.setSelection(0);
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        option = adapterView.getItemAtPosition(i).toString();
        String[] sortBy_values = getResources().getStringArray(R.array.sortBy_values);
        optionURL = sortBy_values[i];
        //Toast.makeText(adapterView.getContext(),option,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getResult() {
            keywords = keywordInput.getText().toString();
            if(keywords.isEmpty()){
                keywordWarning.setVisibility(View.VISIBLE);
            }
            else{
                keywordWarning.setVisibility(View.GONE);
            }

            if(maxInput.getText().toString().isEmpty()&&!minInput.getText().toString().isEmpty()){
                maxprice = Double.MAX_VALUE;
                minprice = Double.valueOf(minInput.getText().toString());

            }
            else if(minInput.getText().toString().isEmpty()&&!maxInput.getText().toString().isEmpty()){
                minprice = 0.0;
                maxprice = Double.valueOf(maxInput.getText().toString());
            }
            else if(minInput.getText().toString().isEmpty()&&maxInput.getText().toString().isEmpty()){
                minprice = 0.0;
                maxprice = Double.MAX_VALUE;
            }
            else{
                maxprice = Double.valueOf(maxInput.getText().toString());
                minprice = Double.valueOf(minInput.getText().toString());
            }
            if(maxprice<0.0||minprice<0.0||maxprice<minprice){
                priceWarning.setVisibility(View.VISIBLE);
            }
            else{
                priceWarning.setVisibility(View.GONE);
            }
            if(maxprice<0.0||minprice<0.0||maxprice<minprice||keywords.isEmpty()){
                return;
            }

            if (new_Input.isChecked()) {
                new_ = "New";
            }
            if (used_Input.isChecked()) {
                used_ = "Used";
            }
            if (unspecifiedInput.isChecked()) {
                unspecified = "Unspecified";
            }
            String url = "https://mypythonproject571-1.wl.r.appspot.com/api/item?keywords="+keywords+"&sortOrder="+optionURL;
            if(maxprice==Double.MAX_VALUE){
                url+="&MinPrice="+String.valueOf(minprice)+"&MaxPrice=";
            }
            else{
                url+="&MinPrice="+String.valueOf(minprice)+"&MaxPrice="+String.valueOf(maxprice);
            }
            url+="&New="+new_+"&Used="+used_+"&Unspecified="+unspecified;
            openGridActivity(url);
            //10.2.8.243
            //String url = "https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YidanZha-Shopping-PRD-12eb6be68-9759c54c&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&keywords="+keywords+"&paginationInput.entriesPerPage=100&sortOrder=BestMatch&itemFilter(0).name=MaxPrice&itemFilter(0).value=25&itemFilter(0).paramName=Currency&itemFilter(0).paramValue=USD";
            //String url = "https://hw6-6666.wl.r.appspot.com/api/item?keywords="+keywords+ "&sortOrder=BestMatch";
            //String url = "http://10.2.8.243:8080/api/item?keywords="+keywords+ "&sortOrder=BestMatch";
        /**
         JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response)
        {
        try{
        JSONArray jsonArray1 = response.getJSONArray("findItemsAdvancedResponse");
        //JSONArray jsonArray = response.get("findItemsAdvancedResponse");
        //Object result = response.get("findItemsAdvancedResponse");
        //apiResult["findItemsAdvancedResponse"][0]["searchResult"][0]["item"]
        //["findItemsAdvancedResponse"][0]["paginationOutput"][0]["totalEntries"][0]
        //System.out.println(jsonArray1);
        //System.out.println(jsonArray1.getJSONObject(0));
        String resultCount = jsonArray1.getJSONObject(0).getJSONArray("paginationOutput").getJSONObject(0).getJSONArray("totalEntries").getString(0);
        //System.out.println(resultCount);
        JSONArray result = jsonArray1.getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
        //System.out.println(result);
        //System.out.println(result.getJSONObject(0).getJSONArray("title").getString(0));
        JSONArray finalResult = clearResult(result);
        System.out.println(finalResult);
        //openGridActivity(finalResult);
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
         */

         



    }

    public void openGridActivity(String url){
        Intent intent = new Intent(this, GridActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("keywords",keywords);
        startActivity(intent);
    }

    /**
     public JSONArray clearResult(JSONArray result) throws JSONException {
     JSONArray finalresult = new JSONArray();
     for(int i=0; i<result.length();i++ ){
     JSONObject item = result.getJSONObject(i);

     if((!item.getJSONArray("itemId").getString(0).isEmpty())&&(!item.getJSONArray("galleryURL").getString(0).isEmpty())&&(!item.getJSONArray("title").getString(0).isEmpty())
     &&(item.getJSONArray("condition").length()>0)&&(!item.getJSONArray("topRatedListing").getString(0).isEmpty())
     &&(item.getJSONArray("shippingInfo").length()>0)&&(item.getJSONArray("sellingStatus").length()>0)){
     String itemId = item.getJSONArray("itemId").getString(0);
     String galleryURL = item.getJSONArray("galleryURL").getString(0);
     String title = item.getJSONArray("title").getString(0);
     String conditionDisplay = item.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0);
     if(conditionDisplay.isEmpty()){
     conditionDisplay = "N/A";
     }
     String topRatedListing = item.getJSONArray("topRatedListing").getString(0);
     String shippingServiceCost = item.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
     String price = item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("convertedCurrentPrice").getJSONObject(0).getString("__value__");
     if((!conditionDisplay.isEmpty())&&(!shippingServiceCost.isEmpty())&&(!price.isEmpty())){
     JSONObject temp = new JSONObject();
     temp.put("itemId", itemId);
     temp.put("galleryURL",galleryURL);
     temp.put("title",title);
     temp.put("conditionDisplay",conditionDisplay);
     temp.put("topRatedListing",topRatedListing);
     temp.put("shippingServiceCost",shippingServiceCost);
     temp.put("price",price);
     finalresult.put(temp);
     }

     }

     }
     return finalresult;
     }
     */

}