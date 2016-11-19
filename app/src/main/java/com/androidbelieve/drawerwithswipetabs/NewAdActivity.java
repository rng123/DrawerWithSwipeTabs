package com.androidbelieve.drawerwithswipetabs;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.widget.SpinnerAdapter;

public class NewAdActivity extends AppCompatActivity {

    private Toolbar toolbar=null;
    private Fragment fragment;
    @Override
    public void onBackPressed() {
        if(fragment.isVisible()) {
         if(fragment instanceof AdFragment)
         {
             AdFragment a=(AdFragment)fragment;
             if(a.imageshown)
             {
                a.hidePager();
             }
         }
         if(fragment instanceof ServiceFragment)
         {
             ServiceFragment sf=(ServiceFragment)fragment;

         }
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_ad);
        String selection=getIntent().getStringExtra("fragment");
        String[] category = getResources().getStringArray(R.array.type);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);}
        //toolbar.setLogo(R.drawable.ic_dots);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(NewAdActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.type, R.layout.spinner_dropdown_item);
        Spinner navigationSpinner = new Spinner(getSupportActionBar().getThemedContext());
        navigationSpinner.setAdapter(spinnerAdapter);
        toolbar.addView(navigationSpinner, 0);
        if(selection.equals("newad"))
        {
            navigationSpinner.setSelection(1);
        }
        else
        {
            navigationSpinner.setSelection(2);
        }

        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragment=new AdFragment();
                    fragmentTransaction.replace(R.id.fl_category,fragment).commit();
                }
                if(position==2){
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragment=new ServiceFragment();
                    fragmentTransaction.replace(R.id.fl_category,fragment).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_our,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, AdActivity.class));
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*private FragmentManager.OnBackStackChangedListener backStackListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            setNavIcon();
        }
    };

    protected void setNavIcon() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        drawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
    }*/
}