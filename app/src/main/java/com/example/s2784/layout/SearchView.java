package com.example.s2784.layout;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
//import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.s2784.layout.databinding.ActivityMainBinding;
import com.example.s2784.layout.databinding.ActivityTabsBinding;
import com.example.s2784.layout.databinding.SearchViewBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchView extends Activity {

    private SearchViewBinding searchViewBinding;
    private ListAdapter adapter_ForSearch;
    private ListView listView_search;

    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<RoomInfo> friend_and_group_list;
        friend_and_group_list = getIntent().getParcelableArrayListExtra("friend_and_group_list");
        super.onCreate(savedInstanceState);
        searchViewBinding = DataBindingUtil.setContentView(this, R.layout.search_view);

        adapter_ForSearch = new ListAdapter(friend_and_group_list,this,Tab1.listDataHeader,Tabs.testViewModel.getListHash());
        //adapter= new ListAdapter(arrayList);
        searchViewBinding.listView.setAdapter(adapter_ForSearch);

        searchViewBinding.search.setActivated(true);
        searchViewBinding.search.setQueryHint("Type your keyword here");
        searchViewBinding.search.onActionViewExpanded();
        searchViewBinding.search.setIconified(false);
        searchViewBinding.search.clearFocus();

        //click to chatroom
        listView_search = (ListView)findViewById(R.id.list_view);
        //listView_search.setAdapter(adapter);
        listView_search.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RoomInfo roomInfo = (RoomInfo) adapter_ForSearch.getItem(position);
                String code = roomInfo.getCode();
                Intent chat = new Intent(SearchView.this,Chatroom.class);
                chat.putExtra("code", code);
                chat.putExtra("id",Tabs.userID);
                startActivity(chat);
            }
        });
        //click to chatroom

        searchViewBinding.search.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter_ForSearch.getFilter().filter(newText);

                return false;
            }
        });

        listView_search.setOnScrollListener(new ListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        //scroll was stopped, let's show search bar again
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        //user is scrolling, let's hide search bar
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                    //user scrolled down, first element is hidden
                }
            }

        });


        /*for search view*/
    }

}
