package com.androidbelieve.drawerwithswipetabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ServiceCategoryAdapter extends RecyclerView.Adapter<ServiceCategoryAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ServiceAlbum> albumList;
    static  ServiceAlbum Ads;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,date,subcat,city;
        public ImageView thumbnail, overflow;
        public View v;
        public MyViewHolder(View view) {
            super(view);
            Log.v("Holder created" ,"here");
            title = (TextView) view.findViewById(R.id.tv_status);
            count = (TextView) view.findViewById(R.id.tv_price);
            thumbnail = (ImageView) view.findViewById(R.id.iv_ads);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            date=(TextView)view.findViewById(R.id.tv_date);
            v=view.findViewById(R.id.card_view_ads);
            city=(TextView) view.findViewById(R.id.tv_subcat);
            subcat=(TextView)view.findViewById(R.id.tv_specs);
        }
    }


    public ServiceCategoryAdapter(Context mContext, final ArrayList<ServiceAlbum> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ad_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ServiceAlbum album = albumList.get(position);
        Log.v("Holder added" ,"here");
        holder.title.setText(album.getName());
        holder.title.setTypeface(null, Typeface.BOLD);
        holder.title.setTextSize(18.0f);
        holder.city.setText(album.getCity());
        String temp="₹ " + album.getNumOfSongs();
        holder.count.setText(temp );
        holder.count.setTextSize(18.0f);
        holder.date.setText(album.getDate());
        Log.v("cat in holder",album.getsubCat());
        holder.subcat.setText(album.getsubCat());
        holder.subcat.setTextSize(15.0f);
        holder.city.setTextSize(15.0f);
        holder.date.setTextSize(15.0f);
        Drawable errord=mContext.getResources().getDrawable(R.drawable.folder_placeholder);
        Picasso.with(mContext).load(album.getLink()).placeholder(R.drawable.image_placeholder).fit().error(errord).into(holder.thumbnail);
        final String aid=album.getSid();
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext,ServiceActivity.class);
                if(album.getPid().equals(AccessToken.getCurrentAccessToken().getUserId()))
                i=new Intent(mContext,MyServiceActivity.class);
                i.putExtra("sid",aid);
                mContext.startActivity(i);
            }
        });
        holder.setIsRecyclable(false);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ads=album;
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        if(Ads.getPid().equals(AccessToken.getCurrentAccessToken().getUserId()))
            inflater.inflate(R.menu.menu_my_ad, popup.getMenu());
        else
            inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_share:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi. Sending this from RnG");
                    sendIntent.setType("text/plain");
                    mContext.startActivity(Intent.createChooser(sendIntent, "Hello"));
                    return true;
                case R.id.action_play_next:
                    GenericAsyncTask g=new GenericAsyncTask(mContext, Config.link+"reportservice.php?sid=" + Ads.getSid() + "&pid=" + AccessToken.getCurrentAccessToken().getUserId(), "", new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            String out=(String)output;
                            if(out.equals("1"))
                            {

                                AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
                                alertbox.setTitle("Report");
                                alertbox.setMessage("Reported successfully");
                                alertbox.show();

                            }
                            else
                            {
                                AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
                                alertbox.setTitle("Report");
                                alertbox.setMessage("You have already reported this ad!");
                                alertbox.show();
                                Log.v("output of async",out);
                            }
                        }
                    });
                    g.execute();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
