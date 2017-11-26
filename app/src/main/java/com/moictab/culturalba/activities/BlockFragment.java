package com.moictab.culturalba.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moictab.culturalba.R;
import com.moictab.culturalba.model.Block;
import com.moictab.culturalba.model.Event;

import java.util.List;

public class BlockFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "SECTION_NUMBER";
    private Block block;

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

        View recyclerView = view.findViewById(R.id.events_list);
        setupRecyclerView((RecyclerView) recyclerView);

        return view;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
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
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.event = events.get(position);
            holder.tvTitle.setText(events.get(position).title);
            holder.tvDate.setText(events.get(position).dateFrom);
            holder.tvSchedule.setText(events.get(position).schedule);

            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra("url", holder.event.link);
                    intent.putExtra("date", holder.event.dateFrom);
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
            final TextView tvSchedule;

            Event event;

            ViewHolder(View view) {
                super(view);
                rootView = view;
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
                tvSchedule = (TextView) view.findViewById(R.id.tv_schedule);
            }
        }
    }
}
