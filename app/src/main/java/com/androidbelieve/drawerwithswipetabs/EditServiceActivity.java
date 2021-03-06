package com.androidbelieve.drawerwithswipetabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.androidbelieve.drawerwithswipetabs.ServiceFragment.setListViewHeightBasedOnChildren;

/**
 * Created by Karan Bheda on 22-Nov-16.
 */

public class EditServiceActivity extends AppCompatActivity {
    private ArrayList<String> links=new ArrayList<>();
    private boolean imageshown=false;
    private ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    private ViewPager viewPager;
    private Uri fileUri;
    private Toolbar toolbar;
    private TextView tel;
    final Activity a=this;
    private Spinner spinner;
    private String sid;
    private Button location;
    private static final int CITY_SEARCH_REQUEST=123;
    private Spinner spinner2;
    private int CAMERA_PIC_REQUEST = 10;
    private RecyclerView recyclerView;
    private EditServiceActivity.HorizontalAdapter HorizontalAdapter;
    private Fragment fragment;
    private int num;
    private ArrayList<Bitmap>images;
    protected ListView lvlinks;
    private ImageButton btnGal,btnPhoto;
    private String item;
    EditServiceActivity.LinksAdapter linksAdapter;
    private EditText inputPname,inputPdesc,inputPrent,inputPtags;
    private TextInputLayout inputLayoutPname, inputLayoutPdesc, inputLayoutPrent,inputLayoutPtags;
    private TextView city;
    private Button btnSignUp;
    private String subcat;
    private Button setasthumb;
    private int submit=0;
    View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_services);
        city=(TextView)findViewById(R.id.tv_city);
        inputLayoutPname = (TextInputLayout) findViewById(R.id.input_layout_pname);
        inputLayoutPdesc = (TextInputLayout) findViewById(R.id.input_layout_pdesc);
        inputLayoutPrent = (TextInputLayout) findViewById(R.id.input_layout_prent);
        inputLayoutPtags = (TextInputLayout) findViewById(R.id.input_layout_ptags);
        inputPname = (EditText) findViewById(R.id.input_pname);
        inputPdesc = (EditText) findViewById(R.id.input_pdesc);
        inputPrent = (EditText) findViewById(R.id.input_prent);
        inputPtags = (EditText) findViewById(R.id.input_ptags);
        btnSignUp=(Button)findViewById(R.id.submit);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_name));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
                if(submit!=1)
                    return;
                if(inputPtags.getText().equals(""))
                {
                    inputPtags.setError("Please Enter some tags!");
                    inputLayoutPtags.setError("Enter space separated values!");
                    return;
                }
                if(item==null)
                {
                    TextView errorText = (TextView)spinner.getSelectedView();
                    errorText.setError("Please select a category!");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.requestFocus();
                    Toast.makeText(EditServiceActivity.this,"Please select a proper category!!",Toast.LENGTH_SHORT);
                    return;
                }
                else if(item.equals("Select a Category"))
                {
                    TextView errorText = (TextView)spinner.getSelectedView();
                    errorText.setError("Please select a category!");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.requestFocus();
                    Toast.makeText(EditServiceActivity.this,"Please select a proper category!!",Toast.LENGTH_SHORT);
                    return;
                }
                else if (subcat==null)
                {
                    TextView errorText = (TextView)spinner2.getSelectedView();
                    errorText.setError("Please select a sub category!");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.requestFocus();
                    Toast.makeText(EditServiceActivity.this,"Please select a proper category!!",Toast.LENGTH_SHORT);
                    return;
                }
                else if (subcat.equals("Select a Sub-Category"))
                {
                    TextView errorText = (TextView)spinner2.getSelectedView();
                    errorText.setError("Please select a sub category!");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.requestFocus();
                    Toast.makeText(EditServiceActivity.this,"Please select a proper category!!",Toast.LENGTH_SHORT);
                    return;
                }

                /*String[] temp=inputPtags.getText().toString().split(" ");
                StringBuffer sbuff=new StringBuffer("");
                for(String x:temp)
                    sbuff.append(x+",");
                String tags= URLEncoder.encode(sbuff.toString());*/
                String tags=URLEncoder.encode(inputPtags.getText().toString().replace(","," "));

                GenericAsyncTask g=new GenericAsyncTask(a, Config.link+"editservice.php", "", new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if(output!=null) {
                            AlertDialog.Builder alertbox = new AlertDialog.Builder(EditServiceActivity.this);
                            if (((String) output).contains("success")) {
                                alertbox.setTitle("Service Edited");
                                alertbox.setMessage("Service has been edited posted successfully. Your Service is pending currently and will be activated within 48 hours");
                                alertbox.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                            } else {
                                alertbox.setMessage("There was some error, Please try again");
                            }
                            alertbox.show();
                            Log.v("result",(String)output);
                        }
                    }
                });
                g.setProgressView(true);
                g.setPostParams("tags",tags,"sid",sid,"sname",inputPname.getText().toString(),"category",item,"subcat",subcat,"description",inputPdesc.getText().toString(),"startrange",inputPrent.getText().toString(),"city",city.getText().toString(),"pid", AccessToken.getCurrentAccessToken().getUserId(),"num",Integer.toString(images.size()),"numlinks",Integer.toString(links.size()));
                g.setImagePost(images,0);
                int i=0;
                for(String x:links)
                {
                    g.setExtraPost("link"+Integer.toString(i++),x);
                }

                //g.execute();
            }
        });
        setasthumb=(Button)findViewById(R.id.thumb_button_1);
        spinner = (Spinner) findViewById(R.id.sp_types);
        final List<String> categories = new ArrayList<String>();
        spinner2 = (Spinner)findViewById(R.id.sp_subtypes);
        final List<String> categories2 = new ArrayList<String>();
        images=new ArrayList<Bitmap>();
        sid=getIntent().getStringExtra("sid");
        categories.add("Select a Category");
        categories.add("Graphics and Design");
        categories.add("Digital Marketing");
        categories.add("Writing and Translation");
        categories.add("Video and Animation");
        categories.add("Music and Audio");
        categories.add("Programming and Tech");
        categories.add("Advertising");
        categories.add("Business");
        categories.add("Lifestyle");
        categories.add("Gifts");
        categories2.add("Select a Sub-Category");
        categories2.add(" ");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter2);

        tel=(TextView)findViewById(R.id.tv_lll);
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(),AdLinksActivity.class),22);
            }
        });
        viewPager=(ViewPager)findViewById(R.id.pager);
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager(), images, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePager();
            }
        });
        viewPager.setAdapter(imageFragmentPagerAdapter);
        recyclerView=(RecyclerView)findViewById(R.id.rr);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        HorizontalAdapter=new EditServiceActivity.HorizontalAdapter(this,images);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(HorizontalAdapter);
        btnGal=(ImageButton)findViewById(R.id.btn_select);
        lvlinks=(ListView)findViewById(R.id.lv_links);
        setListViewHeightBasedOnChildren(lvlinks);
        linksAdapter=new EditServiceActivity.LinksAdapter();
        final Activity a=this;
        btnGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(images.size()==5) {
                    Toast.makeText(getApplicationContext(), "You cant give more images!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ImagePicker.create(a).folderMode(true).folderTitle("All pictures").multi().limit(5-images.size()).start(1);


            }
        });
        btnPhoto=(ImageButton)findViewById(R.id.btn_capture);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addImageView(pics);
               /* Intent data = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(data, CAMERA_PIC_REQUEST);*/
                if(images.size()==5) {
                    Toast.makeText(getApplicationContext(), "You cant give more images!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent data = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri();
                data.putExtra( MediaStore.EXTRA_OUTPUT, fileUri );

                startActivityForResult(data, CAMERA_PIC_REQUEST);

            }
        });




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                if (item == "Graphics and Design") {
                    categories2.clear();
                    categories2.add("Logo Design");
                    categories2.add("Business Card & Strategies");
                    categories2.add("Illustration");
                    categories2.add("Cartoons & Caricatures");
                    categories2.add("Flyers & Posters");
                    categories2.add("Book Cover & Packaging");
                    categories2.add("Web & Mobile Design");
                    categories2.add("Social Media Design");
                    categories2.add("Banner Ads");
                    categories2.add("Photoshop Editing");
                    categories2.add("2D & 3D Models");
                    categories2.add("T-Shirts");
                    categories2.add("Presentation Design");
                    categories2.add("Infographics");
                    categories2.add("Vector Tracing");
                    categories2.add("Invitation");
                    categories2.add("Others");
                    categories.remove("Select a Category");
                    //categories2.remove("Select a Sub-Category");
                }
                else if (item == "Digital Marketing") {
                    categories2.clear();
                    //spinner2.
                    categories2.add("Social Media Marketing");
                    categories2.add("SEO");
                    categories2.add("Web Traffic");
                    categories2.add("Content Marketing");
                    categories2.add("Video Advertising");
                    categories2.add("Email Marketing");
                    categories2.add("SEM");
                    categories2.add("Marketing Strategy");
                    categories2.add("Web Analytics");
                    categories2.add("Influencer Marketing");
                    categories2.add("Local Listing");
                    categories2.add("Domain Research");
                    categories2.add("Mobile Advertising");
                    categories2.add("Others");
                    categories.remove("Select a Category");
                    //categories2.remove("Select a Sub-Category");

                }
                else if (item == "Writing and Translation") {
                    categories2.clear();
                    categories2.add("Resumes & Cover Letters");
                    categories2.add("ProofReading & Editing");
                    categories2.add("Translation");
                    categories2.add("Creative Writing");
                    categories2.add("Business Copywriting");
                    categories2.add("Research & Summaries");
                    categories2.add("Articles & Blog Posts");
                    categories2.add("Press Release");
                    categories2.add("Transcription");
                    categories2.add("Legal Writing");
                    categories2.add("Others");
                    categories.remove("Select a Category");
                    //categories2.remove("Select a Sub-Category");
                }
                else if (item == "Video and Animation") {
                    categories2.clear();
                    categories2.add("Whiteboard & Explainer Videos");
                    categories2.add("Intros & Animated Logos");
                    categories2.add("Promotional & Brand Videos");
                    categories2.add("Editing & Post Production");
                    categories2.add("Lyrics & Music Videos");
                    categories2.add("Spokesperson & Testimonial");
                    categories2.add("Animated Character & Modeling");
                    categories2.add("Video Greetings");
                    categories2.add("Others");
                    categories.remove("Select a Category");
                    //categories2.remove("Select a Sub-Category");
                }
                if (item == "Music and Audio") {

                    categories2.clear();
                    categories2.add("Voice Over");
                    categories2.add("Mixing & Mastering");
                    categories2.add("Producers & Consumers");
                    categories2.add("Singer-Songwriters");
                    categories2.add("Session Musicians & Singers");
                    categories2.add("Jingles & Drops");
                    categories2.add("Sound Effects");
                    categories2.add("Others");
                    categories.remove("Select a Category");
                    //categories2.remove("Select a Sub-Category");
                }
                else if (item == "Programming and Tech") {

                    categories2.clear();
                    categories2.add("Wordpress");
                    categories2.add("Website Builder & CMS");
                    categories2.add("Website Programming");
                    categories2.add("E-Commerce");
                    categories2.add("Mobile Apps & Web");
                    categories2.add("Desktop Application");
                    categories2.add("Support & IT");
                    categories2.add("Data Analyst & Reports");
                    categories2.add("Convert Files");
                    categories2.add("Databases");
                    categories2.add("User Testing");
                    categories2.add("QA");
                    categories2.add("Others");
                    categories.remove("Select a Category");
                    //categories2.remove("Select a Sub-Category");
                }
                else if (item == "Advertising") {

                    categories2.clear();
                    categories2.add("Music Promotion");
                    categories2.add("Radio");
                    categories2.add("Banner Advertising");
                    categories2.add("Outdoor Advertising");
                    categories2.add("Flyer & Handouts");
                    categories2.add("Hold your sign");
                    categories2.add("Human Billboards");
                    categories2.add("Pet Models");
                    categories2.add("Others");
                    categories.remove("Select a Category");
                    //categories2.remove("Select a Sub-Category");
                }
                else if (item == "Business") {
                    categories2.clear();
                    categories2.add("Virtual Assistant");
                    categories2.add("Market Research");
                    categories2.add("Business Plans");
                    categories2.add("Branding Services");
                    categories2.add("Legal Consulting");
                    categories2.add("Financial Consulting");
                    categories2.add("Business Tips");
                    categories2.add("Presentations");
                    categories2.add("Career Advice");
                    categories2.add("Others");
                    categories.remove("Select a Category");
                    //categories2.remove("Select a Sub-Category");
                }
                else if (item == "Lifestyle") {
                    categories2.clear();
                    categories2.add("Animal Care & Pets");
                    categories2.add("Relationship Advice");
                    categories2.add("Diet & Weight Loss");
                    categories2.add("Health & Fitness");
                    categories2.add("Weddiing Planning");
                    categories2.add("Makeup,Styling & Beauty");
                    categories2.add("Online Private Lessons");
                    categories2.add("Astrology & Fortune Telling");
                    categories2.add("Spiritual & Healing");
                    categories2.add("Cooking Recipes");
                    categories2.add("Parenting Tips");
                    categories2.add("Travel");
                    categories2.add("Others");
                    categories.remove("Select a Category");
                    //categories2.remove("Select a Sub-Category");
                }
                else if (item == "Gifts") {

                    categories2.clear();
                    categories2.add("Greeting Cards");
                    categories2.add("Unusual Cards");
                    categories2.add("Arts and Crafts");
                    categories2.add("Handmade Jewellery");
                    categories2.add("Gifts for Geeks");
                    categories2.add("Postcards From");
                    categories2.add("Recycled Crafts");
                    categories2.add("Others");
                    categories.remove("Select a Category");
                    //categories2.remove("Select a Sub-Category");
                }
                spinner2.setSelection(1,true);
                spinner2.setSelection(0,true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                subcat=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        GenericAsyncTask genericAsyncTask=new GenericAsyncTask(this, Config.link+"getserviceforedit.php?sid=" + sid, "", new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                //Add json here
                String out=(String)output;
                try {

                    fillAdd(new JSONObject(out).getJSONArray("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        genericAsyncTask.setProgressView(true);
        genericAsyncTask.execute();
        location=(Button)findViewById(R.id.btn_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data= new Intent(EditServiceActivity.this,SearchActivity.class);
                startActivityForResult(data,CITY_SEARCH_REQUEST);
            }
        });


        setasthumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageshown=false;
                Collections.swap(images,0,viewPager.getCurrentItem());
                HorizontalAdapter.setPosition(0);
                Toast.makeText(getApplicationContext(), "Changed Thumbnail", Toast.LENGTH_SHORT).show();
                hidePager();
                HorizontalAdapter.notifyDataSetChanged();
                reInstantiatePager();
            }
        });

    }


    private void submitForm()
    {
        if (!validatePname())
        {
            return;
        }

        if (!validatePdesc()) {
            return;
        }

        if (!validatePrent()) {
            return;
        }
        if(city.toString().trim().isEmpty())
        {
            return;
        }
        submit=1;
        //Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
    }





    private boolean validatePname() {
        if (inputPname.getText().toString().trim().isEmpty()) {
            inputLayoutPname.setError(getString(R.string.err_msg_name));
            requestFocus(inputPname);
            return false;
        } else {
            inputLayoutPname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePdesc() {
        if (inputPdesc.getText().toString().trim().isEmpty()) {
            inputLayoutPdesc.setError(getString(R.string.err_msg_desc));
            requestFocus(inputPdesc);
            return false;
        } else {
            inputLayoutPdesc.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validatePrent() {
        if (inputPrent.getText().toString().trim().isEmpty()) {
            inputLayoutPrent.setError(getString(R.string.err_msg_rent));
            requestFocus(inputPrent);
            return false;
        } else {
            inputLayoutPrent.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    void fillAdd(JSONArray jarray)
    { try {
        JSONObject c = jarray.getJSONObject(0);

        String prod_name=c.getString("SNAME");
        String rent_name=c.getString("RENT");
        String desc_str=c.getString("DESC");
        String cat=c.getString("CAT");

        String timestamp=c.getString("TIMESTAMP");
        String city=c.getString("CITY");
        this.city.setText(city);
        String subcat=c.getString("SUBCAT");
        spinner.setSelection(0);
        spinner2.setSelection(0);
        JSONArray links=c.getJSONArray("SLINKS");

        String allprojlinks="";
        for(int i=0;i<links.length();i++) {
            this.links.add(links.getJSONObject(i).getString("link"));
            Log.v("links",links.getJSONObject(i).getString("link"));
            allprojlinks+=links.getJSONObject(i).getString("link")+"\n";
        }
        int l = this.links.size();
        lvlinks.setVisibility(View.VISIBLE);
        setListViewHeightBasedOnChildren(lvlinks);

        lvlinks.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        lvlinks.setAdapter(linksAdapter);
        int h =  50*l;
        lvlinks.setMinimumHeight(h);



        JSONArray ilinks=c.getJSONArray("LINKS");
        ArrayList<String> alllinks=new ArrayList<>();
        for(int i=0;i<ilinks.length();i++)
            alllinks.add(ilinks.getJSONObject(i).getString("link"));

        //canrent=c.getString("CANRATE");


        /*this.subcat.setText(subcat);
        name.setText(prod_name);
        desc.setText(desc_str);
        rent.setText("₹ "+ rent_name);
        */
        inputPname.setText(prod_name);
        inputPdesc.setText(desc_str);
        inputPrent.setText(rent_name);


        final int length[]={alllinks.size()};
        Log.v("length",Integer.toString(length[0]));
        for(String x:alllinks) {

            Picasso.with(this).load(x).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    images.add(bitmap);
                    HorizontalAdapter.notifyDataSetChanged();
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            Log.v("link in picasso",x);
        }


    }
    catch(Exception e) {
        e.printStackTrace();
    }

    }


    class LinksAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return links.size();
        }

        @Override
        public Object getItem(int i) {
            return links.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            EditServiceActivity.LinksHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.card_link, null);//Null for whole xml document
                holder = new EditServiceActivity.LinksHolder();
                holder.ltv = (TextView) convertView.findViewById(R.id.tv_link_display);
                holder.itv = (ImageView)convertView.findViewById(R.id.bt_link_remove);
                convertView.setTag(holder);

            } else {
                holder = (EditServiceActivity.LinksHolder) convertView.getTag();
            }
            String cur_link=links.get(i).toString();
            Log.v("Current",cur_link);
            holder.ltv.setText(cur_link);
            holder.ltv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String a="http://"+links.get(i).toString();
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(a));
                    startActivity(intent);
                }
            });
            holder.itv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    links.remove(i);
                    lvlinks.setAdapter(linksAdapter);
                }
            });
            return convertView;
        }
    }
    class LinksHolder {
        TextView ltv;
        ImageView itv;
    }



    void hidePager()
    {
        imageshown=false;
        Log.v("Clicked","Hidden?");
        viewPager.setVisibility(View.GONE);
        setasthumb.setVisibility(View.GONE);

    }

    void reInstantiatePager()
    {
        viewPager.setAdapter(null);
        viewPager.setAdapter(new ImageFragmentPagerAdapter(getSupportFragmentManager(), images, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePager();
            }
        }));

    }



    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "RnG");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //
        //super.onActivityResult(requestCode,resultCode,data);
        Log.v("Check", "Checking");
        Log.v("Check", "Checking");

        if (data == null || resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (images.size() == 5)
                return;
            Bitmap image = null;
            getContentResolver().notifyChange(fileUri, null);
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                int nh = (int) ( image.getHeight() * (720.0 / image.getWidth()) );
                image=Bitmap.createScaledBitmap(image, 720, nh, true);
                images.add(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.v("Bitmap", image.toString());

            HorizontalAdapter.notifyDataSetChanged();
            reInstantiatePager();        }
        if (requestCode == 1) {
            if (images.size() == 5)
                return;
            Log.v("Trying", "trying");
            ArrayList<Image> imagesfrompicker = (ArrayList<Image>) ImagePicker.getImages(data);
            for (Image x : imagesfrompicker) {
                String picturePath = x.getPath();
                Bitmap orig= BitmapFactory.decodeFile(picturePath);
                float div=orig.getWidth()/orig.getHeight();
                int width=720,hieght=1280;
                if(!(div<1))
                {width=1280;hieght=720;}
                Bitmap b=Config.lessResolution(picturePath,width,hieght);
                Log.v("bytecount", Integer.toString(b.getByteCount()));
                images.add(b);
            }
            HorizontalAdapter.notifyDataSetChanged();
            reInstantiatePager();        }
        else if (requestCode==CITY_SEARCH_REQUEST) {
            Log.v("Trying","trying");
            Log.v("data",data.getStringExtra("data"));
            String value = data.getStringExtra("data");
            city.setText(value);
        }
        if(requestCode == 22)
        {
            links=data.getStringArrayListExtra("links");
            int l = links.size();
            lvlinks.setVisibility(View.VISIBLE);
            setListViewHeightBasedOnChildren(lvlinks);

            lvlinks.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            lvlinks.setAdapter(linksAdapter);
            int h =  50*l;
            lvlinks.setMinimumHeight(h);
        }
    }





    class HorizontalAdapter  extends RecyclerView.Adapter<EditServiceActivity.HorizontalAdapter.MyViewHolder> {
        private Context mContext;
        private ArrayList<Bitmap> images;
        private int thumbnail=0;
        public HorizontalAdapter(Context mContext, final ArrayList<Bitmap> images) {
            this.mContext = mContext;
            this.images=images;
            Log.v("Adapter created","Created");

        }
        void setPosition(int i)
        {
            thumbnail=i;
        }

        @Override
        public EditServiceActivity.HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_pic, parent, false);
            Log.v("oncreateViewholder","currect");
            return new EditServiceActivity.HorizontalAdapter.MyViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(final EditServiceActivity.HorizontalAdapter.MyViewHolder holder, final int position) {
            holder.i.setImageBitmap(images.get(position));
            Log.v("inside","holder setting bitmap");
            /**
             *Set all onclicks here
             *
             */
            holder.i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reInstantiatePager();
                    reInstantiatePager();
                    imageshown=true;
                    viewPager.setVisibility(View.VISIBLE);
                    setasthumb.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(position);

                }
            });
            if(position==thumbnail)
            {
                holder.relativeLayout.setBackgroundResource(R.drawable.background_border);
            }
            else
                holder.relativeLayout.setBackgroundResource(R.drawable.empty);

            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    images.remove(position);
                    if(thumbnail==position) {
                        thumbnail = 0;
                    }
                    notifyDataSetChanged();
                    reInstantiatePager();                }
            });
        }
        @Override
        public int getItemCount() {
            return images.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            Button set;
            ImageView i;
            ImageButton del;
            RelativeLayout relativeLayout;
            public MyViewHolder(View view) {
                super(view);
                del = (ImageButton) view.findViewById(R.id.yes_bt);
                i = (ImageView) view.findViewById(R.id.act_image);
                relativeLayout=(RelativeLayout)view.findViewById(R.id.rel_lay);
                //set=(Button)view.findViewById(R.id.button);
            }
        }
    }

}
