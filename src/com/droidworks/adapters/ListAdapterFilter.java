package com.droidworks.adapters;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class ListAdapterFilter implements ListAdapter {

	@SuppressWarnings("unused")
	private final static String LOG_LABEL = "ListAdapterFilter";
	
	private final ListAdapter adapter;
	private final ItemFilter filter;
	
	public ListAdapterFilter(ListAdapter adapter, ItemFilter filter) {
		this.adapter = adapter;
		this.filter = filter;
	}

	public interface ItemFilter {
		public boolean filter(Object item);
	}
	
	@Override
	public int getCount() {
		int count = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			Object item = adapter.getItem(i);
			if (!filter.filter(item))
				count++;
		}
		
		return count;
	}

	@Override
	public Object getItem(int position) {
		return adapter.getItem(offsetPosition(position));
	}

	public int offsetPosition(int position) {
		
		int count = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			Object item = adapter.getItem(i);
			
			if (!filter.filter(item)) {
				if (position == count) 
					return i;
				count++;
			}
		}
		return -1;
	}
	
	@Override
	public long getItemId(int position) {
		return adapter.getItem(offsetPosition(position)).hashCode();
	}

	@Override
	public int getItemViewType(int position) {
		return adapter.getItemViewType(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return adapter.getView(offsetPosition(position), convertView, parent);
	}

	@Override
	public int getViewTypeCount() {
		return adapter.getViewTypeCount();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		if (offsetPosition(0) == -1) {
			return true;
		}
		
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		adapter.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		adapter.unregisterDataSetObserver(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return adapter.areAllItemsEnabled();
	}

	@Override
	public boolean isEnabled(int position) {
		return adapter.isEnabled(offsetPosition(position));
	}

}
