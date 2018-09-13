package com.schoolmanagement.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter {

    protected List<Object> listItems;

    public BaseAdapter(List<Object> listItems) {
        this.listItems = listItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }


    public void addItemAtPosition(int position, Object object) {
        if (listItems != null && listItems.size() >= position) {
            listItems.add(position, object);
            notifyDataSetChanged();
        }
    }

    public void addItem(Object object) {
        if (listItems != null) {
            listItems.add(object);
            notifyDataSetChanged();
        }
    }

    public void addAll(List objects) {
        if (listItems != null) {
            listItems.addAll(objects);
            notifyDataSetChanged();
        }
    }

    public void clearList() {
        if (listItems != null) {
            listItems.clear();
        }
    }

    public Object getItem(int position) {
        if (listItems != null && listItems.size() > position) {
            return listItems.get(position);
        }
        return null;
    }

    public int getItemPosition(Object object) {
        if (listItems != null) {
            return listItems.indexOf(object);
        }
        return -1;
    }

    public void removeItemAtPosition(int position) {
        if (listItems != null && listItems.size() > position) {
            listItems.remove(position);
        }
        notifyDataSetChanged();
    }

    public boolean removeItem(Object object) {
        boolean isItemDeleted = false;
        if (listItems != null) {
            isItemDeleted = listItems.remove(object);
        }
        notifyDataSetChanged();
        return isItemDeleted;
    }
}
