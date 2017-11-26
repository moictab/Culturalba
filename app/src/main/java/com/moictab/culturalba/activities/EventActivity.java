package com.moictab.culturalba.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.moictab.culturalba.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import hirondelle.date4j.DateTime;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

import com.moictab.culturalba.model.Event;
import com.moictab.culturalba.network.ApplicationRetryPolicy;
import com.moictab.culturalba.scraper.WebScraper;

public class EventActivity extends AppCompatActivity {

    private String dateFromBlock = "";
    private Event event;

    private NestedScrollView scrollView;
    private ProgressBar progressBar;
    private View viewLocation;
    private View viewSchedule;
    private View viewPrices;
    private View viewDates;
    private View viewLink;
    private View viewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        String url = getIntent().getExtras().getString("url");
        dateFromBlock = getIntent().getExtras().getString("date");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                toolbarLayout.setBackground(new BitmapDrawable(EventActivity.this.getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        toolbarLayout.setTag(target);

        scrollView = (NestedScrollView) findViewById(R.id.evento_detail_container);
        scrollView.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        viewLocation = findViewById(R.id.location);
        viewSchedule = findViewById(R.id.horario);
        viewPrices = findViewById(R.id.precios);
        viewDates = findViewById(R.id.fechas);
        viewLink = findViewById(R.id.link);
        viewDescription = findViewById(R.id.description);

        viewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + event.location + ", Albacete"));  // Abrimos Google Maps o similar
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EventActivity.this, "Error abriendo el mapa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.link));  // Abrimos el navegador
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EventActivity.this, "Error abriendo el enlace", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((TextView) viewLocation.findViewById(R.id.tv_event_title)).setText(getString(R.string.localizacion));
        ((ImageView) viewLocation.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.ic_place_black_36dp);

        ((TextView) viewSchedule.findViewById(R.id.tv_event_title)).setText(getString(R.string.horario));
        ((ImageView) viewSchedule.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.ic_access_time_black_36dp);

        ((TextView) viewPrices.findViewById(R.id.tv_event_title)).setText(getString(R.string.precios));
        ((ImageView) viewPrices.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.ic_euro_symbol_black_36dp);

        ((TextView) viewDates.findViewById(R.id.tv_event_title)).setText(getString(R.string.fechas));
        ((ImageView) viewDates.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.ic_event_black_36dp);

        ((TextView) viewLink.findViewById(R.id.tv_event_title)).setText(getString(R.string.enlace_original));
        ((ImageView) viewLink.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.ic_link_black_24dp);

        ((TextView) viewDescription.findViewById(R.id.tv_event_title)).setText(getString(R.string.descripcion));
        ((ImageView) viewDescription.findViewById(R.id.iv_icon)).setImageResource(R.mipmap.ic_description_black_24dp);

        RequestQueue queue = Volley.newRequestQueue(EventActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                event = WebScraper.scrapEvent(response);

                Transformation transformation = new ColorFilterTransformation(Color.argb(100, 0, 0, 0));

                Picasso.with(EventActivity.this).load(event.imageLink).resize(toolbarLayout.getWidth(), toolbarLayout.getHeight()).centerCrop().transform(transformation).into(target);
                toolbarLayout.setTitle(event.title);

                if (event.location != null && !event.location.isEmpty()) {
                    ((TextView) viewLocation.findViewById(R.id.tv_text)).setText(event.location);
                } else {
                    ((TextView) viewLocation.findViewById(R.id.tv_text)).setText(getString(R.string.no_disponible));
                }

                if (event.link != null && !event.link.isEmpty()) {
                    ((TextView) viewLink.findViewById(R.id.tv_text)).setText(event.link);
                    ((TextView) viewLink.findViewById(R.id.tv_text)).setTextColor(getResources().getColor(R.color.blue_link));
                } else {
                    ((TextView) viewLink.findViewById(R.id.tv_text)).setText(getString(R.string.no_disponible));
                }

                if (event.schedule != null && !event.schedule.isEmpty()) {
                    ((TextView) viewSchedule.findViewById(R.id.tv_text)).setText(event.schedule);
                } else {
                    ((TextView) viewSchedule.findViewById(R.id.tv_text)).setText(getString(R.string.no_disponible));
                }

                if (event.dateFrom != null && !event.dateFrom.isEmpty() && event.dateTo != null && !event.dateTo.isEmpty()) {

                    int yearFrom = Integer.parseInt(event.dateFrom.substring(0, 4));
                    int monthFrom = Integer.parseInt(event.dateFrom.substring(5, 7));
                    int dayFrom = Integer.parseInt(event.dateFrom.substring(8, 10));
                    int yearTo = Integer.parseInt(event.dateTo.substring(0, 4));
                    int monthTo = Integer.parseInt(event.dateTo.substring(5, 7));
                    int dayTo = Integer.parseInt(event.dateTo.substring(8, 10));

                    DateTime dateFrom = new DateTime(yearFrom, monthFrom, dayFrom, 0, 0, 0, 0);
                    DateTime dateTo = new DateTime(yearTo, monthTo, dayTo, 0, 0, 0, 0);

                    ((TextView) viewDates.findViewById(R.id.tv_text)).setText("Desde el " + dateFrom.format("DD/MM/YYYY") + " hasta el " + dateTo.format("DD/MM/YYYY"));
                } else {
                    ((TextView) viewDates.findViewById(R.id.tv_text)).setText(dateFromBlock);
                }

                if (event.prices != null && !event.prices.isEmpty()) {
                    ((TextView) viewPrices.findViewById(R.id.tv_text)).setText(event.prices);
                } else {
                    ((TextView) viewPrices.findViewById(R.id.tv_text)).setText(getString(R.string.no_disponible));
                }

                if (event.description != null && !event.description.isEmpty()) {
                    ((TextView) viewDescription.findViewById(R.id.tv_text)).setText(event.description);
                } else {
                    ((TextView) viewDescription.findViewById(R.id.tv_text)).setText(getString(R.string.no_disponible));
                }

                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, event.link);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EventActivity.this, R.string.error_getting_data, Toast.LENGTH_SHORT).show();
            }
        });

        stringRequest.setRetryPolicy(new ApplicationRetryPolicy());
        queue.add(stringRequest);

    }
}
