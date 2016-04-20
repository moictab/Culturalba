package com.moictab.culturalba;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import model.Block;
import model.Evento;

public class BlockFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private Block block;

    // UI
    private View recyclerView;
    private RecyclerViewAdapter adapter;

    public BlockFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        block = (Block) getArguments().getSerializable("block");

        recyclerView = rootView.findViewById(R.id.evento_list);
        setupRecyclerView((RecyclerView) recyclerView);
        setupItemDecorator((RecyclerView) recyclerView);

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new RecyclerViewAdapter(block.eventos);
        recyclerView.setAdapter(adapter);
    }

    private void setupItemDecorator(@NonNull RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private final List<Evento> mValues;

        public RecyclerViewAdapter(List<Evento> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.evento_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.tvTitulo.setText(mValues.get(position).title);
            holder.tvFecha.setText(mValues.get(position).dateFrom);
            holder.tvHorario.setText(mValues.get(position).horario);

            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EventoActivity.class);
                    intent.putExtra("url", holder.mItem.link);
                    intent.putExtra("fecha", holder.mItem.dateFrom);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final View rootView;
            public final TextView tvTitulo;
            public final TextView tvFecha;
            public final TextView tvHorario;
            // public final TextView mDateView;
            public Evento mItem;

            public ViewHolder(View view) {
                super(view);
                rootView = view;
                tvTitulo = (TextView) view.findViewById(R.id.textview_title);
                tvFecha = (TextView) view.findViewById(R.id.textview_fecha);
                tvHorario = (TextView) view.findViewById(R.id.textview_horario);
            }
        }
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public DividerItemDecoration(Context context) {
            mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
