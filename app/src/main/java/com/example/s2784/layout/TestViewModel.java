package com.example.s2784.layout;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TestViewModel extends ViewModel {
    private final ArrayList<RoomInfo> group = new ArrayList<>();
    private final ArrayList<RoomInfo> friend = new ArrayList<>();
    private final ArrayList<RoomInfo> course = new ArrayList<>();
    private final ArrayList<RoomInfo> roomlist = new ArrayList<>();
    private final HashMap<String,ArrayList<RoomInfo>> listHash = new HashMap<>();
    private String userID;
    private String userName = "USER";
    private Bitmap userIcon;
//    private boolean dataChange = false;


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; FCM_MessageService.setCurUser(userName);}

    public Bitmap getUserIcon() { return userIcon; }

    public void setUserIcon(Bitmap userIcon) { this.userIcon = userIcon; }

    public void removeFromGroup(int pos){
        group.remove(pos);
    }

    public void removeFromFriend(int pos){
        friend.remove(pos);
    }

    public void addInGroup(RoomInfo roomInfo){
        group.add(roomInfo);
    }

    public void addInFriend(RoomInfo roomInfo){
        friend.add(roomInfo);
    }

    public void addInCourse(RoomInfo roomInfo) { course.add(roomInfo); }

    public ArrayList<RoomInfo> getGroup() { uncheck(); return group; }

    public ArrayList<RoomInfo> getFriend() { uncheck(); return friend; }

    public ArrayList<RoomInfo> getCourse() { uncheck(); return course; }

    public ArrayList<RoomInfo> getRoomList() {  uncheck();
                                                roomlist.clear();
                                                roomlist.addAll(friend);
                                                roomlist.addAll(group);
                                                roomlist.addAll(course);
                                                Collections.sort(roomlist);
                                                return roomlist; }

    public HashMap<String, ArrayList<RoomInfo>> getListHash() {
        return listHash;
    }

    public void putListHash(String string,ArrayList<RoomInfo> list){
        listHash.put(string,list);
    }

    public RoomInfo getRoomInfo(String code){   //only call when into chatroom activity, also set unread
        RoomInfo roomInfo = new RoomInfo();
        for(int i=0;i<friend.size();i++){
            roomInfo = friend.get(i);
            if(code.equals(roomInfo.getCode())){
                friend.get(i).setUnReadNum(0);
                return roomInfo;
            }
        }
        for (int i=0;i<group.size();i++) {
            roomInfo = group.get(i);
            if(code.equals(roomInfo.getCode())){
                group.get(i).setUnReadNum(0);
                return roomInfo;
            }
        }
        for (int i=0;i<course.size();i++) {
            roomInfo = course.get(i);
            if(code.equals(roomInfo.getCode())){
                course.get(i).setUnReadNum(0);
                return roomInfo;
            }
        }
        return roomInfo;
    }

    public void clearAll(){
        group.clear();
        friend.clear();
        course.clear();
        roomlist.clear();
        listHash.clear();
    }

    private void uncheck(){
        for(int i=0;i<friend.size();i++){
            RoomInfo roomInfo = friend.get(i);
            roomInfo.setChecked(false);
        }
        for(int i=0;i<group.size();i++){
            RoomInfo roomInfo = group.get(i);
            roomInfo.setChecked(false);
        }
        for(int i=0;i<course.size();i++){
            RoomInfo roomInfo = course.get(i);
            roomInfo.setChecked(false);
        }
    }
}
