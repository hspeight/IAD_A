package com.itsadate.iad_a;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
//import android.graphics.Color;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    MyDBHandler dbHandler;
    public static final String MyPREFERENCES = "MyPreferences_001";

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private List<String> _listDataSubHeader; // Sub header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    SharedPreferences pref;
    int rowColor, textColor;
    View linLayout;
    View relLayout;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, List<String> listDataSubHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataSubHeader = listDataSubHeader;
        this._listDataChild = listChildData;

        //this.stars = new boolean[listChildData.size()];

        //ImageView delimage;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        //System.out.println("!!-  " + childText);
        final String gp = String.valueOf(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_view, null);
            //System.out.println("!!-  " + " returning convertview");
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

        txtListChild.setText(childText);

        ImageView imgChildDelete = (ImageView) convertView.findViewById(R.id.imageViewDelete);
        imgChildDelete.setTag(groupPosition);
        ImageView imgChildEdit = (ImageView) convertView.findViewById(R.id.imageViewEdit);
        imgChildEdit.setTag(groupPosition);
        ImageView imgChildPlay = (ImageView) convertView.findViewById(R.id.imageViewPlay);
        imgChildPlay.setTag(groupPosition);

        relLayout = convertView.findViewById(R.id.relLayoutChildBG); // Background of row's child view
        relLayout.setBackgroundColor(Color.WHITE); // hard code to always be this color

        //System.out.println("!!-  " + " here1");
   /*     imgListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                System.out.println("!!-  " + gp + "/" + childPosition);
                //delEvent(v, "4");
            }
        });
*/
        //System.out.println("!!-  " + groupPosition + "/" + childPosition);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
    public Object getGroupSub(int groupPosition) {
        return this._listDataSubHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        String headerSubTitle = (String) getGroupSub(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parent_view, null);
            //SharedPreferences pref = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            pref = _context.getSharedPreferences(MyPREFERENCES, 0);
            rowColor = pref.getInt("listRowColor", -16776961); // Get background color from pref file
            textColor = pref.getInt("listTextColor", -1); // Get text color from pref file
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTextColor(textColor);

        TextView lblListSubHeader = (TextView) convertView.findViewById(R.id.lblListSubHeader);
        lblListSubHeader.setTextColor(textColor);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        linLayout = convertView.findViewById(R.id.linLayoutParentBG);
        linLayout.setBackgroundColor(rowColor);

        lblListSubHeader.setText(headerSubTitle);
        //lblListSubHeader.setBackgroundColor(Color.DKGRAY);

        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean delEvent(final View v, final String rowID){
        dbHandler = new MyDBHandler(v.getContext(), null, null, 1);
        System.out.println("!!- " + rowID);
        //Toast.makeText(getApplicationContext(), "Delete clicked "  + rowID, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        //System.out.println("!!- here");
        builder.setTitle("Delete Event?")
                .setIcon(R.drawable.ic_launcher)
                .setMessage("Click OK to delete the event")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean result = dbHandler.deleteEvent(Integer.parseInt(rowID));
                        if (result) {
                            Toast.makeText(v.getContext(), "Event Deleted", Toast.LENGTH_SHORT).show();
                            //finish();
                            //onPause();; // call onpause so that on onresume can be called to refresh list
                            //onResume();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }
}