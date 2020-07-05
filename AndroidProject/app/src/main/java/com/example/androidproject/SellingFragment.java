package com.example.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.Map;

public class SellingFragment extends Fragment {
    JSONObject item;
    TextView seller_info_list;
    TextView return_policy_list;
    public SellingFragment(JSONObject item){

        this.item = item;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here

        try {

            JSONObject brandobj = this.item.getJSONObject("Seller");
            Iterator keys = brandobj.keys();
            LinearLayout sv = (LinearLayout)view.findViewById (R.id.seller_info);
            while (keys.hasNext()) {
                String key = (String) keys.next();

                    String[] r = key.split("(?=\\p{Upper})");
                    String title = "";
                    for(String ss : r){
                        title = title +" "+ ss.substring(0,1).toUpperCase()+ss.substring(1);
                    }

                    String value = brandobj.getString((String) key);
                    TextView rowTextView = new TextView(view.getContext());
                    rowTextView.setText(HtmlCompat.fromHtml("<li><b> "+title+": </b>"+value+"</li>",0));
                    sv.addView(rowTextView);
                LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) rowTextView.getLayoutParams();
                lp1.setMargins(50,0,0,0);


            }
            JSONObject brandobj2 = this.item.getJSONObject("ReturnPolicy");
            Iterator keys2 = brandobj2.keys();
            LinearLayout sv2 = (LinearLayout)view.findViewById (R.id.return_policies);
            while (keys2.hasNext()) {
                String key = (String) keys2.next();

                String[] r = key.split("(?=\\p{Upper})");
                String title = "";
                for(String ss : r){
                    title = title +" "+ ss.substring(0,1).toUpperCase()+ss.substring(1);
                }
                String value = brandobj2.getString((String) key);
                TextView rowTextView = new TextView(view.getContext());
                rowTextView.setText(HtmlCompat.fromHtml("<li><b> "+title+": </b>"+value+"</li>",0));
                sv2.addView(rowTextView);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rowTextView.getLayoutParams();
                lp.setMargins(50,0,0,0);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selling, container, false);
    }
}