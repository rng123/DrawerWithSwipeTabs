package com.androidbelieve.drawerwithswipetabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceActiveFragment extends Fragment {

    private ViewGroup rootView;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private ArrayList<MyAds> list_service;
    MyActiveServiceAdapter serviceAdapter;
    public ServiceActiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   //FacebookSdk.sdkInitialize(getContext());
        rootView = (ViewGroup) inflater.inflate(R.layout.pendingads_layout,null);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_pending_ads);
        recyclerView.setHasFixedSize(true);
        list_service = new ArrayList<>();
        String abc=AccessToken.getCurrentAccessToken().getUserId();
        serviceAdapter=new MyActiveServiceAdapter(getContext(),list_service);
        new ServiceAsyncTask(Config.link+"myservices.php?pid="+abc+"&status=ACTIVE",serviceAdapter,list_service).execute();
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(serviceAdapter);
        //setupListItems();

        recyclerView.setOnScrollListener(new onListenerScroll(serviceAdapter,list_service));
        recyclerView.setLayoutManager(llm);
        return rootView;

    }

}
