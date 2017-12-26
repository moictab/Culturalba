package com.moictab.valenciacultural.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moictab.valenciacultural.R;
import com.moictab.valenciacultural.controller.FavsController;
import com.moictab.valenciacultural.model.Fav;

import java.util.List;

public class FavsActivity extends AppCompatActivity {

    private List<Fav> favs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs);

        favs = new FavsController().getAllFavs(FavsActivity.this);

        RecyclerView recyclerView = findViewById(R.id.favs_list);
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(favs);
        recyclerView.setAdapter(adapter);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<FavsActivity.RecyclerViewAdapter.ViewHolder> {

        private final List<Fav> favs;

        RecyclerViewAdapter(List<Fav> favs) {
            this.favs = favs;
        }

        @Override
        public FavsActivity.RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favs_list_content, parent, false);
            return new FavsActivity.RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FavsActivity.RecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.tvTitle.setText(favs.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return favs.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final View rootView;
            final TextView tvTitle;

            ViewHolder(View view) {
                super(view);
                rootView = view;
                tvTitle = view.findViewById(R.id.tv_title);
            }
        }
    }
}
