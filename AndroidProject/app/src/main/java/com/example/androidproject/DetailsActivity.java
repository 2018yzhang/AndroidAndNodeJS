package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {
    ProgressBar processBar;
    TextView loadting_text;

    RequestQueue queue;
    TabLayout tab;
    ViewPager viewPager;
    JSONObject item;
    String viewItemURL;
    String shipping;
    String price;
    String shippingInfo;

    private int[] tabIcons = {
            R.drawable.information_variant_selector,
            R.drawable.ic_seller,
            R.drawable.truck_delivery_selector,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        processBar=(ProgressBar)findViewById(R.id.progressBar2);
        processBar.setVisibility(View.VISIBLE);
        loadting_text = (TextView)findViewById(R.id.loading_text);
        loadting_text.setVisibility(View.VISIBLE);

        tab=(TabLayout)findViewById(R.id.tab);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        String tab1 = "<b>PRODUCT</b>";
        String tab2 = "<b>SELLER INFO</b>";
        String tab3 = "<b>SHIPPING</b>";
        tab.addTab(tab.newTab().setText(HtmlCompat.fromHtml(tab1, 0)));
        tab.addTab(tab.newTab().setText(HtmlCompat.fromHtml(tab2, 0)));
        tab.addTab(tab.newTab().setText(HtmlCompat.fromHtml(tab3, 0)));
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        setUpTabIcons();

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String itemId = intent.getStringExtra("itemId");
        viewItemURL =intent.getStringExtra("viewItemURL");
        shipping = intent.getStringExtra("shippingServiceCost");
        price = intent.getStringExtra("price");
        shippingInfo = intent.getStringExtra("shippingInfo");
        setTitle(title);
        queue = Volley.newRequestQueue(this);
        getDetails(itemId);
    }
    private void setUpTabIcons() {
        tab.getTabAt(0).setIcon(tabIcons[0]);
        tab.getTabAt(1).setIcon(tabIcons[1]);
        tab.getTabAt(2).setIcon(tabIcons[2]);
    }
    public void getDetails(String itemId){
        String url = "https://mypythonproject571-1.wl.r.appspot.com/api/details?itemId="+itemId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try{
                    item = response.getJSONObject("Item");

                    showDetails(item);

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
    }
    public void showDetails(JSONObject item) throws JSONException {

        processBar.setVisibility(View.GONE);
        loadting_text.setVisibility(View.GONE);

        final InfoAdapter adapter = new InfoAdapter(this,getSupportFragmentManager(), this.tab.getTabCount(), item, shipping, price, shippingInfo);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tab));

        this.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.viewItemURL));
            startActivity(browserIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}