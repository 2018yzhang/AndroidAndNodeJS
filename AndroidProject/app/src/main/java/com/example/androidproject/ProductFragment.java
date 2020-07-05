package com.example.androidproject;

import android.os.Bundle;
import android.os.health.SystemHealthManager;
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

import java.security.Key;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ProductFragment extends Fragment {
    JSONObject item;
    String shipping_info;
    String price_info;
    TextView title;
    TextView price;
    TextView shipping;
    LinearLayout productInfo;
    LinearLayout subtitleLay;
    LinearLayout brandLay;
    TextView subtitle;
    TextView brand;
    TextView nameListText;

    LinearLayout speci;
    HorizontalScrollView horizontalScrollView;

    Map<String, String> namelist;

    public ProductFragment(JSONObject item, String shipping, String price){
        this.item = item;
        this.shipping_info = shipping;
        this.price_info = price;
        namelist = new HashMap<String, String>();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        title = (TextView)view.findViewById(R.id.item_title);
        price = (TextView)view.findViewById(R.id.item_price);
        shipping = (TextView)view.findViewById(R.id.item_shipping);
        productInfo = (LinearLayout)view.findViewById(R.id.product_features);
        productInfo.setVisibility(View.GONE);
        speci = (LinearLayout)view.findViewById(R.id.specification);
        speci.setVisibility(View.GONE);
        subtitleLay = (LinearLayout)view.findViewById(R.id.subtitle);
        subtitleLay.setVisibility(View.GONE);
        brandLay = (LinearLayout)view.findViewById(R.id.brand);
        brandLay.setVisibility(View.GONE);
        subtitle = (TextView)view.findViewById(R.id.subtitle_value);
        brand = (TextView)view.findViewById(R.id.brand_value);
        try {

            horizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.img_horizontal_grid);
            JSONArray imgArray = this.item.getJSONArray("PictureURL");

            LinearLayout sv = (LinearLayout)view.findViewById (R.id.img_horizontal_container);
            for (int i=0 ; i<imgArray.length(); i++){
                ImageView iv = new ImageView (view.getContext());
                LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.MATCH_PARENT);

                iv.setPadding(4,0,0,0);

                String imageUri = imgArray.getString(i);
                Picasso.with(iv.getContext()).load(imageUri).into(iv);
                iv.setLayoutParams(imageViewLayoutParams);
                sv.addView(iv);
            }
            title.setText(this.item.getString("Title"));
            price.setText("$"+this.price_info);
            shipping.setText("Ships for $"+this.shipping_info);
            String brandvalue = "";
            JSONObject brandobj;

            if(this.item.has("ItemSpecifics")){

                brandobj = this.item.getJSONObject("ItemSpecifics");
                System.out.println("check++++++++"+brandobj.has("NameValueList"));
                if(brandobj.has("NameValueList")){
                    JSONArray brandarr = brandobj.getJSONArray("NameValueList");
                    System.out.println("jsonArray "+brandarr);
                    for(int i =0; i<brandarr.length(); i++){
                        JSONObject nameitem = brandarr.getJSONObject(i);
                        if(nameitem.getString("Name").equals("Brand")){

                            brandvalue=nameitem.getJSONArray("Value").getString(0);
                        }
                        else{

                            LinearLayout sv2 = (LinearLayout)view.findViewById (R.id.specification);
                            if(!nameitem.getString("Name").equals("Brand")) {
                                String[] r = nameitem.getString("Name").split("(?=\\p{Upper})");
                                String title = "";
                                for (String ss : r) {
                                    title = title + " " + ss.substring(0, 1).toUpperCase() + ss.substring(1);
                                }
                                String value = nameitem.getJSONArray("Value").getString(0);
                                TextView rowTextView = new TextView(view.getContext());
                                rowTextView.setText(HtmlCompat.fromHtml("<li><b> " + title + ": </b>" + value + "</li>", 0));
                                sv2.addView(rowTextView);
                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rowTextView.getLayoutParams();
                                lp.setMargins(100,0,0,0);
                            }

                            if(sv2.getChildCount()>1){
                                sv2.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }
            }
            String subtitlevalue = "";
            if(this.item.has("Subtitle")){
                subtitlevalue = this.item.getString("Subtitle");
            }
            if(!subtitlevalue.isEmpty()||!brandvalue.isEmpty()){
                productInfo.setVisibility(View.VISIBLE);
                if(!subtitlevalue.isEmpty()){
                    subtitle.setText(this.item.getString("Subtitle"));
                    subtitleLay.setVisibility(View.VISIBLE);
                }
                if(!brandvalue.isEmpty()){
                    brand.setText(brandvalue);
                    brandLay.setVisibility(View.VISIBLE);
                }
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }
}
