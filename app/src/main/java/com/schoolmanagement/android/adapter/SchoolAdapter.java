package com.schoolmanagement.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.schoolmanagement.android.R;
import com.schoolmanagement.android.models.School;

import java.util.List;

public class SchoolAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_SCHOOL_ITEM = 1;
    private View.OnClickListener onOverflowClickListener;

    public SchoolAdapter(List<Object> listItems) {
        super(listItems);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_SCHOOL_ITEM) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_school, parent, false);
            return new SchoolViewHolder(itemView);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_SCHOOL_ITEM) {
            bindSchoolViewHolder((SchoolViewHolder) holder, position);
        }
        super.onBindViewHolder(holder, position);
    }

    private void bindSchoolViewHolder(SchoolViewHolder holder, int position) {
        School school = (School) listItems.get(position);
        if (school == null) {
            return;
        }
        holder.nameTextView.setText(school.getName());
        holder.addressTextView.setText(school.getLocation());

        holder.overflowView.setTag(school);
        holder.overflowView.setOnClickListener(onOverflowClickListener);
    }

    @Override
    public int getItemViewType(int position) {
        Object object = listItems.get(position);
        if (object instanceof School) {
            return VIEW_TYPE_SCHOOL_ITEM;
        }
        return super.getItemViewType(position);
    }

    public void setOnOverflowClickListener(View.OnClickListener onOverflowClickListener) {
        this.onOverflowClickListener = onOverflowClickListener;
    }

    private class SchoolViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressTextView;
        ImageView schoolImageView;
        View overflowView;

        public SchoolViewHolder(View itemView) {
            super(itemView);
            schoolImageView = itemView.findViewById(R.id.image_view);
            nameTextView = itemView.findViewById(R.id.text_view_title);
            addressTextView = itemView.findViewById(R.id.text_view_sub_title);
            overflowView = itemView.findViewById(R.id.layout_overflow);
        }
    }
}
