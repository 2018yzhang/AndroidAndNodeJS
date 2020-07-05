package com.example.androidproject;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>{
    JSONArray results;
    public ItemAdapter(JSONArray results){
        this.results = results;
    }

    @NonNull
    @Override
    //RecyclerView.ViewHolder
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        if(i==50){
            return;
        }
        else{
            JSONObject item = null;
            try {
                item = results.getJSONObject(i);
                String imageUri = item.getString("galleryURL");
                Picasso.with(viewHolder.itemPhoto.getContext()).load(imageUri).into(viewHolder.itemPhoto);
                viewHolder.item_title.setText(item.getString("title").toUpperCase());
                if(item.getString("shippingServiceCost").equals("0.0")){
                    String value = "<p><b>Free</b> Shipping</p>";
                    viewHolder.shipping_price.setText(HtmlCompat.fromHtml(value,0));
                }
                else{
                    String value = "<p>Ships for <b>$"+item.getString("shippingServiceCost")+"</b></p>";
                    viewHolder.shipping_price.setText(HtmlCompat.fromHtml(value,0));
                }

                viewHolder.condition.setText(item.getString("conditionDisplay"));
                viewHolder.selling_price.setText("$"+item.getString("price"));
                if(item.getString("topRatedListing").equals("true")){
                    viewHolder.top_rate.setVisibility(View.VISIBLE);
                }
                final JSONObject finalItem = item;
                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                        try {
                            intent.putExtra("title",finalItem.getString("title"));
                            intent.putExtra("itemId", finalItem.getString("itemId"));
                            intent.putExtra("viewItemURL",finalItem.getString("viewItemURL"));
                            intent.putExtra("price",finalItem.getString("price"));
                            intent.putExtra("shippingServiceCost",finalItem.getString("shippingServiceCost"));
                            intent.putExtra("shippingInfo",finalItem.getString("shippingInfo"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        v.getContext().startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }


    @Override
    public int getItemCount() {
        return results.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_title;
        TextView shipping_price;
        TextView condition;
        TextView selling_price;
        TextView top_rate;
        LinearLayout parent;
        ImageView itemPhoto;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            item_title = itemView.findViewById(R.id.item_title);
            itemPhoto = (ImageView)itemView.findViewById(R.id.item_photo);
            shipping_price = (TextView)itemView.findViewById(R.id.shipping_price);
            condition = (TextView)itemView.findViewById(R.id.condition);
            selling_price = (TextView)itemView.findViewById(R.id.selling_price);
            top_rate = (TextView)itemView.findViewById(R.id.topRate);
            top_rate.setVisibility(View.GONE);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }

    }
}

