package com.example.s2784.layout;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private static List<String> listDataHeader;
        private static HashMap<String,ArrayList<RoomInfo>> listHashMap;
//        private Handler handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                notifyDataSetChanged();//更新数据
//                super.handleMessage(msg);
//            }
//        };


    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, ArrayList<RoomInfo>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int j) {
        return listHashMap.get(listDataHeader.get(i)).get(j); // i =Group Item  j = Child Item
    }


    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int j) {
        return j;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_section, null);
        }
        TextView listHeader = (TextView) view.findViewById(R.id.listHeader);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, int j, boolean b, View view, ViewGroup viewGroup) {
        final RoomInfo roomInfo = (RoomInfo) getChild(i, j);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }
        TextView txtlistChild = (TextView) view.findViewById(R.id.listItem);
        txtlistChild.setText(roomInfo.getRoomName());
        ImageView imageChild = (ImageView) view.findViewById(R.id.listImage);
        imageChild.setImageBitmap(BitmapFactory.decodeByteArray(roomInfo.getIcon_data(),0,roomInfo.getIcon_data().length));
        return view;

    }

    @Override
    public boolean isChildSelectable(int i, int j) {
        return true;
    }

    //-----------------use for Main.java
    public static int getGroupCount_ForMain() {
        return listDataHeader.size();
    }

    public static int getChildrenCount_ForMain(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    public static Object getChild_ForMain(int i, int j) {
        return listHashMap.get(listDataHeader.get(i)).get(j); // i =Group Item  j = Child Item
    }
//    public void refresh(ExpandableListView expandableListView, int groupPosition){
//        handler.sendMessage(new Message());
//        //重新伸縮 更新數據
//        if(expandableListView.isGroupExpanded(groupPosition)){
//            expandableListView.collapseGroup(groupPosition);
//            expandableListView.expandGroup(groupPosition);
//        }
//        else
//        {
//            expandableListView.expandGroup(groupPosition);
//            expandableListView.collapseGroup(groupPosition);
//        }

//
//    }
    //-----------------use for Main.java
}
