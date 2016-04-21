package com.moictab.culturalba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import listener.OnSettingsAccepted;
import model.Block;
import scraper.WebScraper;

/**
 * Contiene todas las pestañas, que se corresponden a categorías de eventos,
 * y dentro de cada una de las pestañas muestra una lista de los eventos correspondientes.
 */
public class MainActivity extends AppCompatActivity implements OnSettingsAccepted {

    // URL de la que se obtiene la lista de eventos y sus categorías
    private final static String URL_TODAY = "http://www.albacete.es/es/agenda";
    private final static String URL_EVERYTHING = "http://www.albacete.es/es/agenda/agenda-completa";
    private static String URL;

    private List<Block> blocks = new ArrayList<>();
    private RequestQueue queue;

    private PagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ProgressBar progressBar;
    private View emptyLayout;
    private Button reloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        emptyLayout = findViewById(R.id.empty_layout);
        reloadButton = (Button) emptyLayout.findViewById(R.id.button_reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });

        Boolean today = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean("today", true);
        if (today) {
            URL = URL_TODAY;
        } else {
            URL = URL_EVERYTHING;
        }

        queue = Volley.newRequestQueue(MainActivity.this);
        makeRequest();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (preferences.getBoolean("first_time", true)) {
            InicioDialog dialog = new InicioDialog();
            dialog.show(getFragmentManager(), "inicio_dialog");
            preferences.edit().putBoolean("first_time", false).apply();
        }
    }

    /**
     * Hace la petición web, la parsea y pone los resultados en sus listas correspondientes.
     */
    public void makeRequest() {

        progressBar.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.INVISIBLE);
        emptyLayout.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                blocks.clear();
                blocks = WebScraper.scrapList(response);

                setAdapter();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Error obteniendo los datos", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.INVISIBLE);
            }
        });

        queue.add(stringRequest);

    }

    private void setAdapter() {
        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSectionsPagerAdapter.notifyDataSetChanged();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        progressBar.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
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

        if (id == R.id.action_settings) {
            SettingsDialog dialog = new SettingsDialog();
            dialog.setListener(MainActivity.this);
            dialog.show(getFragmentManager(), "SettingsDialog");
        }

        if (id == R.id.action_actualizar) {
            makeRequest();
            return true;
        }

        if (id == R.id.action_acerca) {
            AboutDialog dialog = new AboutDialog();
            dialog.show(getFragmentManager(), "AboutDialog");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnBarcodeProcessed(boolean today) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
        editor.putBoolean("today", today).apply();

        if (today) {
            URL = URL_TODAY;
        } else {
            URL = URL_EVERYTHING;
        }

        makeRequest();
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
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
