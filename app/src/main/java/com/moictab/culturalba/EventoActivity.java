package com.moictab.culturalba;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import hirondelle.date4j.DateTime;
import model.Evento;
import network.CulturalbaRetryPolicy;
import scraper.WebScraper;

public class EventoActivity extends AppCompatActivity {

    private static String URL = "";
    private Evento evento;

    // UI
    private NestedScrollView scrollView;
    private ProgressBar progressBar;
    private View viewLocation;
    private View viewHorario;
    private View viewPrecios;
    private View viewFechas;
    private View viewLink;
    private View viewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        URL = getIntent().getExtras().getString("url");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                toolbarLayout.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        toolbarLayout.setTag(target);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        scrollView = (NestedScrollView) findViewById(R.id.evento_detail_container);
        scrollView.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        viewLocation = findViewById(R.id.location);
        viewHorario = findViewById(R.id.horario);
        viewPrecios = findViewById(R.id.precios);
        viewFechas = findViewById(R.id.fechas);
        viewLink = findViewById(R.id.link);
        viewDescription = findViewById(R.id.description);

        viewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + evento.location + ", Albacete"));
                startActivity(intent);
            }
        });

        viewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(evento.link));
                startActivity(intent);
            }
        });

        ((TextView) viewLocation.findViewById(R.id.textview_titulo)).setText("Localización");
        ((ImageView) viewLocation.findViewById(R.id.imageview_icon)).setImageResource(R.mipmap.ic_place_black_36dp);

        ((TextView) viewHorario.findViewById(R.id.textview_titulo)).setText("Horario");
        ((ImageView) viewHorario.findViewById(R.id.imageview_icon)).setImageResource(R.mipmap.ic_access_time_black_36dp);

        ((TextView) viewPrecios.findViewById(R.id.textview_titulo)).setText("Precios");
        ((ImageView) viewPrecios.findViewById(R.id.imageview_icon)).setImageResource(R.mipmap.ic_euro_symbol_black_36dp);

        ((TextView) viewFechas.findViewById(R.id.textview_titulo)).setText("Fechas");
        ((ImageView) viewFechas.findViewById(R.id.imageview_icon)).setImageResource(R.mipmap.ic_event_black_36dp);

        ((TextView) viewLink.findViewById(R.id.textview_titulo)).setText("Enlace original");
        ((ImageView) viewLink.findViewById(R.id.imageview_icon)).setImageResource(R.mipmap.ic_place_black_36dp);

        ((TextView) viewDescription.findViewById(R.id.textview_titulo)).setText("Descripción");
        ((ImageView) viewDescription.findViewById(R.id.imageview_icon)).setImageResource(R.mipmap.ic_place_black_36dp);

        RequestQueue queue = Volley.newRequestQueue(EventoActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                evento = WebScraper.scrapEvento(response);
                Picasso.with(EventoActivity.this).load(evento.imageLink).resize(toolbarLayout.getWidth(), toolbarLayout.getHeight()).centerCrop().into(target);

                toolbarLayout.setTitle(evento.title);

                if (evento.location != null && !evento.location.isEmpty()) {
                    ((TextView) viewLocation.findViewById(R.id.textview_texto)).setText(evento.location);
                } else {
                    ((TextView) viewLocation.findViewById(R.id.textview_texto)).setText("No disponible");
                }

                if (evento.link != null && !evento.link.isEmpty()) {
                    ((TextView) viewLink.findViewById(R.id.textview_texto)).setText(evento.link);
                    ((TextView) viewLink.findViewById(R.id.textview_texto)).setTextColor(getResources().getColor(R.color.blue_link));
                } else {
                    ((TextView) viewLink.findViewById(R.id.textview_texto)).setText("No disponible");
                }

                if (evento.horario != null && !evento.horario.isEmpty()) {
                    ((TextView) viewHorario.findViewById(R.id.textview_texto)).setText(evento.horario);
                } else {
                    ((TextView) viewHorario.findViewById(R.id.textview_texto)).setText("No disponible");
                }

                if (evento.dateFrom != null && !evento.dateFrom.isEmpty() && evento.dateTo != null && !evento.dateTo.isEmpty()) {

                    int yearFrom = Integer.parseInt(evento.dateFrom.substring(0, 4));
                    int monthFrom = Integer.parseInt(evento.dateFrom.substring(5, 7));
                    int dayFrom = Integer.parseInt(evento.dateFrom.substring(8, 10));
                    int yearTo = Integer.parseInt(evento.dateTo.substring(0, 4));
                    int monthTo = Integer.parseInt(evento.dateTo.substring(5, 7));
                    int dayTo = Integer.parseInt(evento.dateTo.substring(8, 10));

                    DateTime dateFrom = new DateTime(yearFrom, monthFrom, dayFrom, 0, 0, 0, 0);
                    DateTime dateTo = new DateTime(yearTo, monthTo, dayTo, 0, 0, 0, 0);

                    ((TextView) viewFechas.findViewById(R.id.textview_texto)).setText("Desde el " + dateFrom.format("DD/MM/YYYY") + " hasta el " + dateTo.format("DD/MM/YYYY"));
                } else {
                    ((TextView) viewFechas.findViewById(R.id.textview_texto)).setText("No disponible");
                }

                if (evento.precios != null && !evento.precios.isEmpty()) {
                    ((TextView) viewPrecios.findViewById(R.id.textview_texto)).setText(evento.precios);
                } else {
                    ((TextView) viewPrecios.findViewById(R.id.textview_texto)).setText("No disponible");
                }

                if (evento.description != null && !evento.description.isEmpty()) {
                    ((TextView) viewDescription.findViewById(R.id.textview_texto)).setText(evento.description);
                } else {
                    ((TextView) viewDescription.findViewById(R.id.textview_texto)).setText("No disponible");
                }

                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.getMessage());
                Toast.makeText(EventoActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        stringRequest.setRetryPolicy(new CulturalbaRetryPolicy());
        queue.add(stringRequest);

    }
}
