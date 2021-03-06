package com.androidbelieve.drawerwithswipetabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MyAdActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    private TextView name,desc,rent,date,city,age,deposit,crent,maxrent,subcat,tvrentw,tvrentm;
    private String aid;
    private MenuItem star;
    private Button rating_comments;
    private RatingBar ratingBar;
    private String canrent;
    private boolean set=false;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_my_ad);
        final TextView[] count=new TextView[5];
        count[0]=(TextView)findViewById(R.id.count1);
        count[1]=(TextView)findViewById(R.id.count2);
        count[2]=(TextView)findViewById(R.id.count3);
        count[3]=(TextView)findViewById(R.id.count4);
        count[4]=(TextView)findViewById(R.id.count5);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        //rating_comments= (Button) findViewById(R.id.btn_rate_comment);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle("MANNNNNYNYYYYYY");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_name));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        aid = getIntent().getStringExtra("AID");
        name=(TextView)findViewById(R.id.tv_name);
        desc=(TextView)findViewById(R.id.tv_desc);
        rent=(TextView)findViewById(R.id.tv_rent);
        tvrentw=(TextView)findViewById(R.id.tv_rentw);
        tvrentm=(TextView)findViewById(R.id.tv_rentm);
        city=(TextView)findViewById(R.id.tv_location);
        age=(TextView)findViewById(R.id.tv_prod_age);
        deposit=(TextView)findViewById(R.id.tv_prod_dep);
        date=(TextView)findViewById(R.id.tv_date);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar1);
        crent=(TextView)findViewById(R.id.crent);
        maxrent=(TextView)findViewById(R.id.tv_max_rent);
        subcat = (TextView) findViewById(R.id.tv_subcat);
        ratingBar.setMax(5);
        ratingBar.setFocusable(false);
        ratingBar.setFocusableInTouchMode(false);
        ratingBar.setClickable(false);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching ad Please wait");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        //rating_comments.setClickable(false);
        new GetAd(aid,AccessToken.getCurrentAccessToken().getUserId()).execute();
        new GenericAsyncTask(this, Config.link+"sendrating.php?aid=" + aid, "", new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                int i=Integer.parseInt((String)output);
                ratingBar.setProgress(i);
                progressDialog.dismiss();
            }
        }).execute();
        new GenericAsyncTask(this, Config.link + "sendcomment.php?aid=" + aid, "", new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                if(output!=null)
                {

                    String out=(String)output;
                    String[] delim=out.split(";;");
                    for(String x:delim)
                    {
                        String[] temp = x.split(",,");
                       try {
                           count[Integer.parseInt(temp[0])].setText(temp[1]);
                       }
                       catch (Exception e)
                       {
                           e.printStackTrace();
                           Log.v("Temp string",x);
                       }
                    }
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }*/


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    class GetAd extends AsyncTask<String, String, String> {
        String aid,pid;
        GetAd(String aid,String pid) {
            this.aid = aid;
            this.pid=pid;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {

                String link = Config.link+"fetchad.php?aid=" + aid+"&pid="+pid;
                Log.v("link",link);
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.v("Result", result);
            } catch (Exception e) {
                // Oops
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jobj = new JSONObject(result);
                fillAdd(jobj.getJSONArray("result"));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }
    static String Month(Date d)
    {
        int i=d.getMonth();
        switch(i)
        {
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "Mar";
            case 3: return "Apr";
            case 4: return "May";
            case 5: return "Jun";
            case 6: return "July";
            case 7: return "Aug";
            case 8: return "Sept";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default:return "Dec";
        }

    }


    void fillAdd(JSONArray jarray)
    {
        try {
            JSONObject c = jarray.getJSONObject(0);
            String prod_name=c.getString("PROD_NAME");
            String rent_name=c.getString("RENT");
            String desc_str=c.getString("DESC");
            String timestamp=c.getString("TIMESTAMP");
            String city=c.getString("LOCATION");
            String age=c.getString("AGE");
            String deposit=c.getString("DEPOSIT");
            String subc = c.getString("SUBCAT");
            canrent=c.getString("CANRATE");
            String rentw=c.getString("RENTW");
            String rentm=c.getString("RENTM");

            subcat.setText(subc);
            String[] crent=c.getString("crent").split(",");
            String temp="";
            for(String x:crent)
                temp+="\u2022  "+x+"\n";
            this.crent.setText(temp);

            String maxrent=c.getString("maxrent");
            maxrent = maxrent.trim();
            Log.v("maxrent",maxrent);
            int num=Character.digit(maxrent.charAt(maxrent.length()-1),10);
            Log.v("maxrent",Integer.toString(num));
            //maxrent="Around "+Integer.toString(num)+maxrent.substring(0,maxrent.length()-1);
            String temp2=new String(Integer.toString(num)+" "+maxrent.substring(0,maxrent.length()-1));
            Log.v("maxrent",temp2);

            this.maxrent.setText(temp2);


            Date today=new Date();
            String ddate;
            Date yesterday=new Date();
            yesterday.setTime(today.getTime()-((long)864E5));
            Date date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
            date.setTime(date.getTime()+19800000);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
            Log.v("timeStamp",timestamp);
            Log.v("date",date.toString());
            if(sdf.format(today).equals(sdf.format(date)))
                ddate="Today ";
            else if(sdf.format(yesterday).equals(sdf.format(date)))
                ddate="Yesterday ";
            else
            {

                //this.date=date.getDay()+" "+Month(date)+" ";
                ddate=new SimpleDateFormat("d MMMM").format(date);
                if(!(today.getYear()==date.getYear()))
                    ddate+=" "+date.getYear()+" ";
            }
            this.date.setText(ddate);
            this.city.setText(city);
            int fage = Integer.parseInt(age);
            if(fage<12 && fage!=1)
                this.age.setText(age + " Months");
            else if(fage==1)
                this.age.setText(age + " Month");
            else if(fage>12 && fage%12==0)
                this.age.setText(Integer.toString(fage/12) + " Years");
            else if(fage==12)
                this.age.setText("1 Year");
            else{
                int rage=fage%12;
                fage=fage/12;
                this.age.setText(Integer.toString(fage) + " Years & " + Integer.toString(rage) + " Months");
            }
            this.deposit.setText("₹ "+ deposit);
            JSONArray links=c.getJSONArray("LINKS");
            ArrayList<String> alllinks=new ArrayList<>();
            for(int i=0;i<links.length();i++)
                alllinks.add(links.getJSONObject(i).getString("link"));

            name.setText(prod_name);
            toolbar.setTitle(prod_name);
            desc.setText(desc_str);
            for(String x:crent){
                String[] per=x.split("s");
                if(per[0].equals("Day") && !rent_name.equals("0")) {
                    rent.setText("₹ " + rent_name + "/" + per[0].toLowerCase());
                    rent.setVisibility(View.VISIBLE);
                }
                if(per[0].equals("Week") && !rentw.equals("0")) {
                    tvrentw.setText("₹ " + rentw + "/" + per[0].toLowerCase());
                    tvrentw.setVisibility(View.VISIBLE);
                }
                if(per[0].equals("Month") && !rentm.equals("0")) {
                    tvrentm.setText("₹ " + rentm + "/" + per[0].toLowerCase());
                    tvrentm.setVisibility(View.VISIBLE);
                }}
            for(String name : alllinks){
                //Log.v("");
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView.image(name).setScaleType(BaseSliderView.ScaleType.Fit);

                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);

        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
    public void onShare(){
        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"http://www.rentandget.co.in/ads/"+aid);
        startActivity(Intent.createChooser(intent,"Sharing Option"));
    }
    public void onRateAndComment(View view){
        Intent i=new Intent(this,RateActivity.class);
        i.putExtra("aid",aid);
        i.putExtra("CANRATE",canrent);
        startActivity(i);
    }

    /*void getMenus(Menu menu)
    {
        star=menu.findItem(R.id.action_wishlist);
        GenericAsyncTask g=new GenericAsyncTask(this, "http://rng.000webhostapp.com/checkwishlist.php?aid=" + aid + "&pid=" + AccessToken.getCurrentAccessToken().getUserId(), "", new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String out=(String)output;
                if(out.equals("1"))
                {
                    star.setIcon(android.R.drawable.btn_star_big_on);
                    set=!set;
                    Log.v("output of async",out);
                }
                else
                {
                    star.setIcon(android.R.drawable.btn_star_big_off);
                    set=false;
                    Log.v("output of async",out);
                }
            }
        });
        g.execute();
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_my_ad, menu);
        //getMenus(menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_share:
                onShare();
                return true;
           /* case R.id.action_wishlist:
                GenericAsyncTask g=new GenericAsyncTask(this, "http://rng.000webhostapp.com/wishlist.php?aid=" + aid + "&pid=" + AccessToken.getCurrentAccessToken().getUserId(), "", new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if(set)
                            star.setIcon(android.R.drawable.btn_star_big_off);
                        else
                            star.setIcon(android.R.drawable.btn_star_big_on);

                        set=!set;
                    }
                });
                g.execute();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}