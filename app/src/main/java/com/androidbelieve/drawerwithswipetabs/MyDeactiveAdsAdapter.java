package com.androidbelieve.drawerwithswipetabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyDeactiveAdsAdapter extends RecyclerView.Adapter<MyDeactiveAdsAdapter.MyViewHolder> {

    private Context mContext;
    private List<MyAds> adList;
    static MyAds myAds;
    static int pos=0;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView status,specs,price,date,subcat;
        public ImageView ads,overflow;
        private View v;

        public MyViewHolder(View view) {
            super(view);
            v=view.findViewById(R.id.card_view_ads);
            cardView = (CardView) view.findViewById(R.id.card_view_ads);
            status = (TextView) view.findViewById(R.id.tv_status);
            price = (TextView) view.findViewById(R.id.tv_price);
            specs = (TextView) view.findViewById(R.id.tv_specs);
            date = (TextView) view.findViewById(R.id.tv_date);
            ads = (ImageView) view.findViewById(R.id.iv_ads);
            subcat = (TextView) view.findViewById(R.id.tv_subcat);
            overflow = (ImageView) view.findViewById(R.id.overflow);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "MANNY", Toast.LENGTH_SHORT).show();

                    return true;
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return false;
                }
            });
        }
    }

    public MyDeactiveAdsAdapter(Context mContext, List<MyAds> adList) {
        this.mContext = mContext;
        this.adList = adList;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MyAds ad = adList.get(position);
        holder.status.setText("Deactivated");
        holder.status.setTextColor(Color.parseColor("#F44336"));
        holder.specs.setText(ad.getSpecs());
        holder.date.setText(ad.getDate());
        holder.price.setText("₹ " + ad.getPrice() );
        holder.subcat.setText(ad.getSubcat());
        holder.price.setTextColor(Color.parseColor("#FB6542"));
        holder.specs.setTypeface(null, Typeface.BOLD);
        holder.status.setTextSize(17.0f);
        holder.specs.setTextSize(18.0f);
        holder.price.setTextSize(18.0f);
        holder.subcat.setTextSize(15.0f);
        holder.date.setTextSize(15.0f);

        Picasso.with(mContext).load(ad.getLink()).placeholder(R.drawable.image_placeholder).into(holder.ads);
        final String aid=ad.getAid();
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAds=ad;
                pos=position;
                showPopupMenu(holder.overflow);
            }
        });
        holder.v.setOnClickListener(new View.OnClickListener() {
         @Override
          public void onClick(View view) {
          Intent i=new Intent(mContext,MyAdActivity.class);
         i.putExtra("AID",aid);
         mContext.startActivity(i);

        }
        });
        holder.setIsRecyclable(false);

     //   Glide.with(mContext).load(ad.getImage_ads()).into(holder.ads);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ad_card, parent, false);

        return new MyViewHolder(itemView);
    }


    public int getItemCount() {
        return adList.size();
    }


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_deactivated_ad, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit: {
                    //Toast.makeText(mContext, "Edit Ad", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext,EditAdActivity.class);
                    intent.putExtra("AID",myAds.getAid());
                    mContext.startActivity(intent);
                    return true;}
                case R.id.action_delete: {
                    GenericAsyncTask g=new GenericAsyncTask(mContext, Config.link+"deletead.php?aid=" + myAds.getAid(), "", new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            String out=(String)output;
                            if(out.equals("success")) {
                                adList.remove(pos);
                                pos = 0;
                                notifyDataSetChanged();
                            }
                        }
                    });
                    g.execute();
                  //  Toast.makeText(mContext, "Delete Ad", Toast.LENGTH_SHORT).show();
                }
                return true;
                case R.id.action_activate:
                    GenericAsyncTask g=new GenericAsyncTask(mContext, Config.link+"changestatus.php?aid=" + myAds.getAid()+"&status=PENDING", "", new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            String out=(String)output;
                            if(out.equals("success")) {
                                adList.remove(pos);
                                pos = 0;
                                notifyDataSetChanged();
                            }
                        }
                    });
                    g.execute();
                    //Toast.makeText(mContext, "Deactivate Ad", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }

    }
}

