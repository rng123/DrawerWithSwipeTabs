package com.androidbelieve.drawerwithswipetabs;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ViewGroup rootView;
    String adap[],service[];
    HomeAdapter ha;
    HomeAdapter homeAdapter;
    private ArrayList<String> cats;
    private boolean x=true;
    private ArrayList<String> servicecats;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewservice;
    private GenericAsyncTask genericAsyncTask;
    private GenericAsyncTask genericAsyncTaskService;
    private FloatingActionButton fabservice;
    private FloatingActionButton fabad;
    private boolean shown=false;
    private SharedPreferences sharedPreferences;
    private RelativeLayout rl_main;

    FloatingActionButton fab;
    TextView v,vs;


    private void Blurring()
    {
        if(!shown) {
            final View content = rootView.findViewById(R.id.rl_main);
            Blur.blur(rootView,getContext(),x);
            x=false;
            Animation cross=AnimationUtils.loadAnimation(getContext(),R.anim.cross);
            fab.startAnimation(cross);
            v.animate().alpha(1f).setDuration(500).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    v.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            vs.animate().alpha(1f).setDuration(500).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    vs.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            Animation fabad_show = AnimationUtils.loadAnimation(getContext(), R.anim.fabad_show);
            fabad.startAnimation(fabad_show);
            fabad.setVisibility(View.VISIBLE);
            fabservice.startAnimation(fabad_show);
            fabservice.setVisibility(View.VISIBLE);
            shown=true;
            fabad.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), NewAdActivity.class);
                    i.putExtra("fragment", "newad");
                    startActivity(i);
                }
            });
            fabservice.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), NewAdActivity.class);
                    i.putExtra("fragment", "service");
                    startActivity(i);
                }
            });

        }
        else
        {

            final View content = rootView;
            rootView.findViewById(R.id.rl_main).setVisibility(View.VISIBLE);
            Blur.unBlur(content);
            Animation anticross=AnimationUtils.loadAnimation(getContext(),R.anim.anticross);
            fab.startAnimation(anticross);
            v.animate().alpha(0f).setDuration(500).setInterpolator(new LinearInterpolator()).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    v.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            vs.animate().alpha(0f).setDuration(500).setInterpolator(new LinearInterpolator()).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    vs.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();


            Animation fabad_hide = AnimationUtils.loadAnimation(getContext(), R.anim.fabad_hide);
            fabad.startAnimation(fabad_hide);
            fabad.setVisibility(View.INVISIBLE);
            fabservice.startAnimation(fabad_hide);
            fabservice.setVisibility(View.INVISIBLE);
            shown=false;

        }
    }

    public void onBackPressed()
    {
        if(shown)
        {
            Blurring();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = (ViewGroup) inflater.inflate(R.layout.home_layout, container, false);
        cats=new ArrayList<String>();
        servicecats=new ArrayList<String>();
        recyclerView= (RecyclerView) rootView.findViewById(R.id.rr);
        recyclerViewservice=(RecyclerView)rootView.findViewById(R.id.serv_rel_lay);
        ha= new HomeAdapter(getContext(),cats,Category_List.class);
        homeAdapter=new HomeAdapter(getContext(),servicecats,Service_Category.class);
        recyclerView.setAdapter(ha);
        recyclerViewservice.setAdapter(homeAdapter);
        fabservice=(FloatingActionButton)rootView.findViewById(R.id.fabnewservice);
        fabad=(FloatingActionButton)rootView.findViewById(R.id.fabnewad);
        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        v=(TextView)rootView.findViewById(R.id.Tv_ad);
        vs=(TextView)rootView.findViewById(R.id.Tv_serv);
        rl_main= (RelativeLayout) rootView.findViewById(R.id.rl_main);
        Picasso.with(getContext()).load(Config.link+"dod.php").into((ImageView)rootView.findViewById(R.id.dod));
        rootView.findViewById(R.id.blurimage).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){
                Blurring();
            }
        });
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Blurring();
            }
        });

        genericAsyncTask=new GenericAsyncTask(getContext(),Config.link+"category.php","message",new AsyncResponse()
        {
            @Override
            public void processFinish(Object output)
            {
                String out=(String)output;
                if(!out.contains(";;"))
                    return;
                adap=out.split(";;");

                for(int i=0;i<adap.length;i++ )
                {
                    try {
                        cats.add(adap[i]);
                        ha.notifyDataSetChanged();
                    }
                        catch(NullPointerException e)
                        {
                            Log.v("Null pointer caught","Maybe activity was closed?");
                        }
                }
                Log.v("Cats",cats.toString());

            }
        });
        genericAsyncTaskService=new GenericAsyncTask(getContext(), Config.link+"categoryservice.php", "", new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String out=(String)output;
                if(!out.contains(";;"))
                    return;
                service=out.split(";;");
                for(int i=0;i<service.length;i++ )
                {
                    try {
                        //Log.v("HELLo",adap[i]);
                        servicecats.add(service[i]);
                        homeAdapter.notifyDataSetChanged();
                    }
                    catch(NullPointerException e)
                    {
                        Log.v("Null pointer caught","Maybe activity was closed?");
                    }
                }

            }
        });
        genericAsyncTask.execute();
        genericAsyncTaskService.execute();
        return rootView;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(genericAsyncTask!=null)
            if(genericAsyncTask.getStatus()!= AsyncTask.Status.FINISHED)
                genericAsyncTask.cancel(true);
    }

}

