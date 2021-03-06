package com.androidbelieve.drawerwithswipetabs;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;



public class InfScrollviewListener extends RecyclerView.OnScrollListener {


    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 6;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    private String category;
    private GenericAsyncTask genericAsyncTask;
    private boolean listend=false;
    private String sort="",filter="";
    public InfScrollviewListener(AlbumsAdapter adapter, List<Album> albumList,String category)
    {

        this.adapter=adapter;
        this.albumList=albumList;
        this.category=category;
    }

    void updateSortandFilter(String sort,String filter)
    {
        this.sort=sort;
        this.filter=filter;
    }



    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        super.onScrolled(recyclerView, dx, dy);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                Log.v("Loading is",Boolean.toString(listend));
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            totalItemCount = linearLayoutManager.getItemCount();
            lastVisibleItem = linearLayoutManager
                    .findLastVisibleItemPosition();
            if(lastVisibleItem==0||totalItemCount==0)
                return;
            if (!loading&& totalItemCount <= (lastVisibleItem + visibleThreshold)&&!listend) {
                Log.v("Checking","check"+Integer.toString(lastVisibleItem + 1));
                //new GetJSON("http://rng.000webhostapp.com/viewads.php?category", lastVisibleItem+1, adapter, albumList).execute();
                GenericAsyncTask genericAsyncTask=new GenericAsyncTask(null, Config.link+"viewads.php?category="+ URLEncoder.encode(category)+"&order="+sort+"&filter="+URLEncoder.encode(filter)+"&OFF="+ Integer.toString(lastVisibleItem + 1), "", new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        String out=(String)output;
                        Log.v("out",out);
                        if(!out.equals("{\"result\":[]}"))
                        {
                            try {
                                JSONObject jobj = new JSONObject(out);
                                prepareAlbum(jobj.getJSONArray("result"));
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            listend = true;
                        }
                    }
                });
                genericAsyncTask.execute();
                loading = true;

            }
        }
    }
    void stopAsyncTask() throws NullPointerException
    {
        if(genericAsyncTask!=null)
            if(genericAsyncTask.getStatus()== AsyncTask.Status.FINISHED)
                genericAsyncTask.cancel(true);
    }
    void prepareAlbum(JSONArray jarray)
    {
        for(int i=0;i<jarray.length();i++)
        {
            try {
                JSONObject ad = jarray.getJSONObject(i);
                String pid=ad.getString("PID");
                String name = ad.getString("PROD_NAME");
                String aid = ad.getString("AID");
                String cat=ad.getString("CATEGORY");
                int amount = Integer.parseInt(ad.getString("AMOUNT"));
                String date = ad.getString("DATE");
                int rentw =  Integer.parseInt(ad.getString("RENTW"));
                int rentm =  Integer.parseInt(ad.getString("RENTM"));
                String crent = ad.getString("crent");
                String[] temp = crent.split(",");
                Album a=new Album(cat,pid,name,amount,i,aid,date,rentw,rentm,temp[0]);
                albumList.add(a);

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    loading=false;
    }
}
