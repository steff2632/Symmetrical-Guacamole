package com.example.symmetrical_guacamole.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.symmetrical_guacamole.InternetService;
import com.example.symmetrical_guacamole.MyItem;
import com.example.symmetrical_guacamole.R;
import com.example.symmetrical_guacamole.adapters.MyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class PlaceholderFragment extends Fragment {

    private static final String TAG = PlaceholderFragment.class.getSimpleName();

    private RecyclerView mListView;
    private MyAdapter mAdapter;
    private String URL;
    private JsonBroadcastReceiver jsonBroadcastReceiver;

    private LocalBroadcastManager localBroadcastManager;

    public static PlaceholderFragment getInstance(String url) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        fragment.URL = url;
        return fragment;
    }

    public PlaceholderFragment() {
        jsonBroadcastReceiver = new JsonBroadcastReceiver();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        mListView.setLayoutManager(linearLayoutManager);

        mListView.setAdapter(mAdapter);
        localBroadcastManager = LocalBroadcastManager.getInstance(container.getContext());
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_refresh:
                try {
                    loadData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Create and set the list adapter.
        mAdapter = new MyAdapter(context, new MyItem[0]);

        // Load data from net.
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(isAdded()) {
            localBroadcastManager.unregisterReceiver(jsonBroadcastReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isAdded()) {
            localBroadcastManager.registerReceiver(jsonBroadcastReceiver, new IntentFilter(InternetService.Results.GET_STRING));
        }
    }

    private void loadData() throws IOException {

        if(!TextUtils.isEmpty(URL)) {
            Intent intent = new Intent(getActivity(), InternetService.class);
            intent.setData(Uri.parse(InternetService.Actions.GET_STRING));
            intent.putExtra(InternetService.Extras.URL_EXTRA, URL);

            getActivity().startService(intent);
        } else {
            throw new IOException("no URL specified");
        }
    }


    public class JsonBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra(InternetService.Extras.STRING_EXTRA)) {
                String json = intent.getStringExtra(InternetService.Extras.STRING_EXTRA);

                try {
                    JSONArray jsonArray = new JSONArray(json);
                    MyItem[] myItems = new MyItem[jsonArray.length()];

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        MyItem myItem = new MyItem();
                        myItem.desc = jsonObject.getString(MyItem.DESC);
                        myItem.title = jsonObject.getString(MyItem.TITLE);
                        myItem.image = jsonObject.getString(MyItem.IMAGE);

                        Intent dlImages = new Intent(getActivity(), InternetService.class);
                        dlImages.setData(Uri.parse(InternetService.Actions.GET_BYTES));
                        dlImages.putExtra(InternetService.Extras.URL_EXTRA, jsonObject.getString(MyItem.IMAGE));
                        dlImages.putExtra(InternetService.Extras.ITEM_EXTRA, myItem);

//                        getActivity().startService(dlImages);

                        myItems[i] = myItem;
                    }

                    mAdapter.setItems(myItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
