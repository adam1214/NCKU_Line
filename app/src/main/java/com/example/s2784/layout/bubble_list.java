package com.example.s2784.layout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by uscclab on 2018/5/21.
 * 聊天室內的list view的Adapter
 * msgList 中儲存一則則的聊天訊息
 * notifyDataSetChanged Method 實作一次只更新一行的方法(不用一次更新整個listView)
 * ... 2018/6/30
 */

public class bubble_list extends BaseAdapter {

    private Context context;
    private ArrayList<bubble> msgList;
    private static LayoutInflater inflater = null;
    private  String copyText;
    protected LinearLayout bubble_left;
    protected LinearLayout bubble_left_nodate;
    protected LinearLayout bubble_right;
    protected LinearLayout bubble_right_nodate;


    public bubble_list(Context context, ArrayList<bubble> msgList) {
        this.context = context;
        this.msgList = msgList;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View rowView, ViewGroup parent) {
        final bubble Bubble = (bubble) getItem(position);
        final RoomInfo roomInfo;
        TextView name = null;
        TextView time = null;
        TextView date = null;
        ImageView pic = null;
        int type = Bubble.getType();
        int data_t = Bubble.getData_t();
        if (type == 0) {
            if (data_t == 0) {
                if (position > 0 && msgList.get(position).getDate().equals(msgList.get(position - 1).getDate())) {
                    rowView = inflater.inflate(R.layout.bubble_chat_left_nodate, null);
                    bubble_left_nodate = rowView.findViewById(R.id.bubble_chat_left_nodate);
//                    bubble_right = rowView.findViewById(R.id.bubble_chat_right);
                    bubble_left_nodate.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
//                            Toast.makeText(v.getContext(), "bubble_chat_left_nodate", Toast.LENGTH_SHORT).show();
                            initPopWindow(v,position);
                            return true;
                        }
                    });
                } else {
                    rowView = inflater.inflate(R.layout.bubble_chat_left, null);
                    bubble_left = rowView.findViewById(R.id.bubble_chat_left);
                    bubble_left.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
//                            Toast.makeText(v.getContext(), "bubble_chat_left", Toast.LENGTH_SHORT).show();
                            initPopWindow(v,position);
                            return true;
                        }
                    });
                    date = (TextView) rowView.findViewById(R.id.date_left);
                    date.setText(Bubble.getDate());
                }
            } else if (data_t == 1) {
                if (position > 0 && msgList.get(position).getDate().equals(msgList.get(position - 1).getDate())) {
                    rowView = inflater.inflate(R.layout.bubble_chat_img_left_nodate, null);
                } else {
                    rowView = inflater.inflate(R.layout.bubble_chat_img_left, null);
                    date = (TextView) rowView.findViewById(R.id.date_left);
                    date.setText(Bubble.getDate());
//                    bubble_left = lv.findViewById(R.id.bubble_chat_left);
//                    bubble_left_nodate = lv.findViewById(R.id.bubble_chat_left_nodate);
//                    bubble_right = lv.findViewById(R.id.bubble_chat_right);
//                    bubble_right_nodate = lv.findViewById(R.id.bubble_chat_right_nodate);
                }
            }
            name = (TextView) rowView.findViewById(R.id.userName);
            if (Tabs.mqtt.MapAlias(Bubble.getName()) != null) {
                name.setText(Tabs.mqtt.MapAlias(Bubble.getName()));
            } else {
                name.setText(Bubble.getName());
            }
            pic = (ImageView) rowView.findViewById(R.id.bubblePic);
            if (Bubble.getPic() == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.friend);
                pic.setImageBitmap(bitmap);
            } else {
                pic.setImageBitmap(Bubble.getPic());
            }
        } else {
            if (data_t == 0) {
                if (position > 0 && msgList.get(position).getDate().equals(msgList.get(position - 1).getDate())) {
                    rowView = inflater.inflate(R.layout.bubble_chat_right_nodate, null);
                    bubble_right_nodate = rowView.findViewById(R.id.bubble_chat_right_nodate);
                    bubble_right_nodate.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
//                            Toast.makeText(v.getContext(), "bubble_chat_right_nodate", Toast.LENGTH_SHORT).show();
                            initPopWindow(v,position);
                            return true;
                        }
                    });
                } else {
                    rowView = inflater.inflate(R.layout.bubble_chat_right, null);
                    bubble_right = rowView.findViewById(R.id.bubble_chat_right);
                    bubble_right.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
//                            Toast.makeText(v.getContext(), "bubble_chat_right", Toast.LENGTH_SHORT).show();
                            initPopWindow(v,position);
                            return true;
                        }
                    });
                    date = (TextView) rowView.findViewById(R.id.date_right);
                    date.setText(Bubble.getDate());
                }
            } else if (data_t == 1) {
                if (position > 0 && msgList.get(position).getDate().equals(msgList.get(position - 1).getDate())) {
                    rowView = inflater.inflate(R.layout.bubble_chat_img_right_nodate, null);

                } else {
                    rowView = inflater.inflate(R.layout.bubble_chat_img_right, null);
                    date = (TextView) rowView.findViewById(R.id.date_right);
                    date.setText(Bubble.getDate());
                }
            }
        }

        time = (TextView) rowView.findViewById(R.id.msg_time);
        time.setText(Bubble.getTime());
        if (data_t == 0) {
            TextView txt_msg = rowView.findViewById(R.id.txt_msg);
            txt_msg.setText(Bubble.getTxtMsg());
        } else if (data_t == 1) {
            ImageView imageView = rowView.findViewById(R.id.img_msg);
            imageView.setImageBitmap(Bubble.getImage());
        }

        return rowView;
    }

    public void notifyDataSetChanged(ListView lv, int position) {
        //listView 可能很長(超過手機畫面),這裡拿到處於手機畫面中,第一以及最後一個的item的index
        int firstVisiblePosition = lv.getFirstVisiblePosition();
        int lastVisiblePosition = lv.getLastVisiblePosition();

        //要更新的行數處在手機畫面中才更新(不再畫面中的item,隨著listView捲動會自動更新)
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View v = lv.getChildAt(position - firstVisiblePosition);

            if (v == null) {
                return;
            }

            //更新
            this.getView(position, v, lv);
        }
    }

    private void initPopWindow(View v, final int position) {
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.simple_popup, null, false);
        Button btn_copy = (Button) view.findViewById(R.id.copy);
        Button btn_foward = (Button) view.findViewById(R.id.foward);
        Button btn_delete = (Button) view.findViewById(R.id.delete);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.style.pop_anim);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 50, 0);

        //设置popupWindow里的按钮的事件
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "複製~", Toast.LENGTH_SHORT).show();
                ClipboardManager cbm =(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                copyText = msgList.get(position).getTxtMsg();
                ClipData mClipData = ClipData.newPlainText("Label",copyText);
                cbm.setPrimaryClip(mClipData);
                popWindow.dismiss();
            }
        });
        btn_foward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "轉寄", Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "刪除", Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });
    }
//    @Override
//    public boolean onLongClick(View v) {
//        switch (v.getId()) {
//            case R.id.bubble_chat_left:
//                Toast.makeText(v.getContext(), "bubble_chat_left", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.bubble_chat_left_nodate:
//                Toast.makeText(v.getContext(), "bubble_chat_left_nodate", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.bubble_chat_right:
//                Toast.makeText(v.getContext(), "bubble_chat_left_nodate", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.bubble_chat_right_nodate:
//                Toast.makeText(v.getContext(), "bubble_chat_left_nodate", Toast.LENGTH_SHORT).show();
//                return true;
//        }
//        return true;
//    }
}
