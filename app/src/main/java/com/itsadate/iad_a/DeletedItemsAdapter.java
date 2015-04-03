package com.itsadate.iad_a;

import java.util.List;

//import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
//import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DeletedItemsAdapter extends ArrayAdapter<ArchItem> {
    Context mContext;
    private LayoutInflater inflater;
    //public static final String MyPREFERENCES = "MyPreferences_001";

    public DeletedItemsAdapter(Context context, List<ArchItem> architems) {
        super(context, R.layout.arch_item, R.id.rowTextView1, architems);
        mContext = context;
        // Cache the LayoutInflate to avoid asking for a new one each time.
        inflater = LayoutInflater.from(context);
        //SharedPreferences pref = getSharedPreferences (MyPREFERENCES, MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Planet to display
        ArchItem archit = (ArchItem) this.getItem(position);

        // The child views in each row.
        CheckBox checkBox;
        TextView textView;
        TextView textView2;

        // Create a new row view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.arch_item, null);
            RelativeLayout singleRow = (RelativeLayout) convertView.findViewById(R.id.relLayout);

            SharedPreferences pref = mContext.getSharedPreferences("MyPreferences_001", 0);
            int archRowColor = pref.getInt("listRowColor", -16776961); // Get row color from pref file
            int archTextColor = pref.getInt("listTextColor", -1); // Get text color from pref file
            singleRow.setBackgroundColor(archRowColor);


            // Find the child views.
            textView = (TextView) convertView.findViewById(R.id.rowTextView1);
            textView2 = (TextView) convertView.findViewById(R.id.rowTextView2);
            checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);

            textView.setTextColor(archTextColor);
            textView2.setTextColor(archTextColor);

            convertView.setTag(new SelectViewHolder(textView,textView2, checkBox));

        } else {
            // Because we use a ViewHolder, we avoid having to call
            SelectViewHolder viewHolder = (SelectViewHolder) convertView
                    .getTag();
            checkBox = viewHolder.getCheckBox();
            textView = viewHolder.getTextView();
            textView2 = viewHolder.getTextView2();
        }

        // Tag the CheckBox with the event it is displaying, so that we can
        // access the event in onClick() when the CheckBox is toggled.
        //checkBox.setTag(archit);
        // Display planet data
        checkBox.setChecked(archit.isChecked());
        textView.setText(archit.getName());
        textView2.setText(archit.getTime());
        return convertView;
    }
}

/** Holds child views for one row. */
  class SelectViewHolder {
    private CheckBox checkBox;
    private TextView textView;
    private TextView textView2;


    public SelectViewHolder(TextView textView, TextView textView2, CheckBox checkBox) {
        this.checkBox = checkBox;
        this.textView = textView;
        this.textView2 = textView2;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public TextView getTextView() {
        return textView;
    }
    public TextView getTextView2() {
        return textView2;
    }

}