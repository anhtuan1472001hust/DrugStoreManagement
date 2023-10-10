package bk.pttkhdt.drugstoremanager.feature.main.ui.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bk.pttkhdt.drugstoremanager.R;

public class AutoCompleteTvAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<String> resultList = new ArrayList<>();
    private String keyword;


    public AutoCompleteTvAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<String> data) {
        if (data != null) {
            resultList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void clearList() {
        resultList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        Log.e("size",String.valueOf(resultList.size()));
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.e("itemList",String.valueOf(resultList.get(position)));
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("item", resultList.get(position));
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_auto_complete_tv, parent, false);
        }
        String item = resultList.get(position);
        TextView textView = convertView.findViewById(R.id.tv_auto_complete);

        if (keyword.isEmpty()) {
            textView.setText(item);
        } else {
            SpannableString spannableString = new SpannableString(item);
            int start = item.toLowerCase().indexOf(keyword);
            int end = start + keyword.length();
            if (start >= 0) {
                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.setText(spannableString);
        }
        return convertView;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                keyword = constraint != null ? constraint.toString().toLowerCase() : "";



                if (keyword.isEmpty()) {
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                } else {
                    List<String> filteredList = new ArrayList<>();
                    for (String item : resultList) {
                        if (item.toLowerCase().contains(keyword)) {
                            filteredList.add(item);
                        }
                    }
                    filterResults.values = filteredList;
                    filterResults.count = filteredList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                    resultList = (List<String>) results.values;
                    notifyDataSetChanged();

            }
        };
        return filter;
    }


}
