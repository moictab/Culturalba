package com.moictab.valenciacultural.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.moictab.valenciacultural.R;
import com.moictab.valenciacultural.activities.EventActivity;
import com.moictab.valenciacultural.controller.FavsController;
import com.moictab.valenciacultural.dao.FavDao;
import com.moictab.valenciacultural.model.Block;
import com.moictab.valenciacultural.model.Event;
import com.moictab.valenciacultural.model.Fav;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockFragment extends Fragment {

    public static final String TAG = "BlockFragment";

    private static final String ARG_SECTION_NUMBER = "SECTION_NUMBER";
    private FavsController favsController;
    private Block block;
    private List<Fav> favedEvents = new ArrayList<>();

    public BlockFragment() {
        // Default empty constructor
    }

    public static BlockFragment newInstance(int sectionNumber) {
        BlockFragment fragment = new BlockFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        this.block = (Block) getArguments().getSerializable("block");

        RecyclerView recyclerView = view.findViewById(R.id.events_list);
        setupRecyclerView(recyclerView);

        return view;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        favedEvents = new FavsController().getAllFavs(getActivity());

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(block.events);
        recyclerView.setAdapter(adapter);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private final List<Event> events;

        RecyclerViewAdapter(List<Event> items) {
            events = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.event = events.get(position);
            holder.tvTitle.setText(events.get(position).title);
            holder.tvDate.setText(events.get(position).date);
            holder.tvLocation.setText(events.get(position).location);

            if (favedEvents.contains(events.get(position))) {
                holder.btnFav.setTextColor(getResources().getColor(R.color.gray));
            } else {
                holder.btnFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        favsController.saveEventOnFavs(getActivity(), events.get(position));
                        holder.btnFav.setTextColor(getResources().getColor(R.color.gray));
                    }
                });
            }

            holder.btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, getShareText(events.get(position)));
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });

            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra("url", holder.event.link);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final View rootView;
            final TextView tvTitle;
            final TextView tvDate;
            final TextView tvLocation;

            final Button btnFav;
            final Button btnShare;

            Event event;

            ViewHolder(View view) {
                super(view);
                rootView = view;
                tvTitle = view.findViewById(R.id.tv_title);
                tvDate = view.findViewById(R.id.tv_date);
                tvLocation = view.findViewById(R.id.tv_location);
                btnFav = view.findViewById(R.id.btn_favs);
                btnShare = view.findViewById(R.id.btn_share);
            }
        }
    }

    private String getShareText(Event event) {
        StringBuilder builder = new StringBuilder();
        builder.append("Mira el evento ");
        builder.append(event.title);
        builder.append(" en este enlace: ");
        builder.append(event.link);

        return builder.toString();
    }
}
