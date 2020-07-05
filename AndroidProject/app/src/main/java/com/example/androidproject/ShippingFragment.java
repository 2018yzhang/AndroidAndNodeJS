package com.example.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ShippingFragment extends Fragment {
    JSONObject item;
    TextView shippingInfo;
    public ShippingFragment(JSONObject item){
        this.item = item;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here


        try {
            Iterator keys = this.item.keys();
            LinearLayout sv = (LinearLayout)view.findViewById (R.id.ship_info);
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if(!key.equals("shippingServiceCost")){
                    String[] r = key.split("(?=\\p{Upper})");
                    String title = "";
                    for(String ss : r){
                        title = title +" "+ ss.substring(0,1).toUpperCase()+ss.substring(1);
                    }
                    String value = this.item.getJSONArray((String) key).getString(0);
                    TextView rowTextView = new TextView(view.getContext());
                    rowTextView.setText(HtmlCompat.fromHtml("<li><b> "+title+": </b>"+value+"</li>",0));
                    sv.addView(rowTextView);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rowTextView.getLayoutParams();
                    lp.setMargins(50,0,0,0);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shipping, container, false);
    }
}
