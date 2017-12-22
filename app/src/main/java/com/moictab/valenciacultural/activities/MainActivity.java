package com.moictab.valenciacultural.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.moictab.valenciacultural.R;
import com.moictab.valenciacultural.controller.BlocksController;
import com.moictab.valenciacultural.controller.NetworkController;
import com.moictab.valenciacultural.dialogs.StartDialog;
import com.moictab.valenciacultural.fragments.BlockFragment;
import com.moictab.valenciacultural.model.Block;
import com.moictab.valenciacultural.model.Event;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String URL = "http://www.valencia.es/ayuntamiento/agenda_accesible.nsf/agenda.xml";

    private List<Block> blocks = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    private NetworkController networkBusiness;
    private RequestQueue queue;

    private ViewPager viewPager;
    private ProgressBar progressBar;
    private View emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.container);

        progressBar = findViewById(R.id.progressbar);
        emptyLayout = findViewById(R.id.empty_layout);
        Button reloadButton = emptyLayout.findViewById(R.id.button_reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });

        queue = Volley.newRequestQueue(this);
        makeRequest();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (preferences.getBoolean("first_time", true)) {
            StartDialog dialog = new StartDialog();
            dialog.show(getFragmentManager(), "inicio_dialog");
            preferences.edit().putBoolean("first_time", false).apply();
        }
    }

    public void makeRequest() {

        networkBusiness = new NetworkController(events, queue);

        progressBar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        emptyLayout.setVisibility(View.GONE);
        queue.add(networkBusiness.getEventsRequest(URL));

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                networkBusiness.requestFinished();
                if (!networkBusiness.isRequestsPending()) {
                    setAdapter();
                }
            }
        });
    }

    private void setAdapter() {
        BlocksController controller = new BlocksController();
        events = networkBusiness.getEvents();
        blocks = controller.getBlocks(events);

        BlocksAdapter adapter = new BlocksAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        progressBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_favs) {
            Intent intent = new Intent(MainActivity.this, FavsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_actualizar) {
            makeRequest();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class BlocksAdapter extends FragmentStatePagerAdapter {

        BlocksAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = BlockFragment.newInstance(position + 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable("block", blocks.get(position));
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return blocks.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return blocks.get(position).title;
        }
    }
}
