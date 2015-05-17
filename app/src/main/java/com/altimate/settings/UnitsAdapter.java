package com.altimate.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.altimate.R;
import com.altimate.models.DistanceUnit;

/**
 * Created by jeanweatherwax on 4/25/15.
 */
public class UnitsAdapter extends BaseAdapter {

  private List<DistanceUnit> mDistanceUnits;
  private Context mContext;

  public UnitsAdapter(Context context) {
    mContext = context;
  }

  public void setItems(List<DistanceUnit> items) {
    mDistanceUnits = items;
  }

  @Override
  public int getCount() {
    return mDistanceUnits.size();
  }

  @Override
  public Object getItem(int position) {
    return mDistanceUnits.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View rowView = convertView;
    ViewHolder viewHolder;
    if (rowView == null) {
      viewHolder = new ViewHolder();
      rowView = LayoutInflater.from(mContext).inflate(R.layout.settings_spinner_item, parent, false);
      viewHolder.itemTextView = (TextView) rowView.findViewById(R.id.item_text);
      rowView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) rowView.getTag();
    }

    DistanceUnit item = mDistanceUnits.get(position);
    viewHolder.itemTextView.setText(item.name());

    return rowView;
  }

  public static class ViewHolder {
    TextView itemTextView;
  }

}
