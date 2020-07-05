package com.example.androidproject;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONException;
import org.json.JSONObject;

public class InfoAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;
    JSONObject item;
    String shipping;
    String price;

    JSONObject shippingInfo;
    public InfoAdapter(Context context, FragmentManager fm, int totalTabs, JSONObject item, String shipping, String price, String shippingInfo) throws JSONException {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.item = item;
        this.shipping = shipping;
        this.price=price;
        this.shippingInfo = new JSONObject(shippingInfo);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProductFragment productFragment = new ProductFragment(item, shipping, price);
                return productFragment;
            case 1:
                SellingFragment sellingFragment = new SellingFragment(item);
                return sellingFragment;
            case 2:
                ShippingFragment shippingFragment = new ShippingFragment(shippingInfo);
                return shippingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}

