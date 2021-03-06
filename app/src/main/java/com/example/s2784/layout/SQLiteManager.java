package com.example.s2784.layout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Locale;

import me.leolin.shortcutbadger.ShortcutBadger;

public class SQLiteManager {
    static private SQLiteDatabase DB;
    static private String DBNAME = "CHENG_LINE";
    static private String Badge_table_name = "BADGE";
    static private String Login_table_name = "LOGIN";
    static private String Intro_table_name = "INTRO";
    static private String Password_table_name = "PASSWORD";
    static private String vote_table_name = "VOTE";
    static private Context ctx;

    static public void setContext(Context context){ ctx = context; }

    static public void DBinit(){
        DB = ctx.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
    }

    static public void createTableForBadge(){
        String instruction = "CREATE TABLE IF NOT EXISTS " +  Badge_table_name + "(" + "Room VARCHAR(40)" +
                "," + "Count int(10)" + ")";
        DB.execSQL(instruction);
    }

    static public void createTableForLogin(){
        String instruction = "CREATE TABLE IF NOT EXISTS " + Login_table_name + "(id VARCHAR(9))";
        DB.execSQL(instruction);
    }

    static public void createTableForIntro(){
        String sql = "CREATE TABLE IF NOT EXISTS " + Intro_table_name + "(intro VARCHAR(36))";
        DB.execSQL(sql);
    }

    static public void createTableForPassword(){
        String sql = "CREATE TABLE IF NOT EXISTS " + Password_table_name + "(password VARCHAR(36))";
        DB.execSQL(sql);
    }

    static public void createTableForVote() {
        String sql = "CREATE TABLE IF NOT EXISTS " + vote_table_name + "(annoc_pk int(10))";
        DB.execSQL(sql);
    }

    static public void queryForBadge(String code){
        Cursor c = DB.rawQuery("SELECT " + "*" + " FROM " + Badge_table_name + " WHERE " + "Room = " + "'" + code + "'", null);
        if(c.getCount() == 0){
            insertFirst(code);
        }
        badgePlus(code);
        c.close();
    }

    static public int querySingleRoomBadge(String code){
        int num = 0;
        Cursor c = DB.rawQuery("SELECT " + "Count" + " FROM " + Badge_table_name + " WHERE " + "Room = " + "'" + code + "'", null);
        if(c.moveToFirst()){
            num = c.getInt(0);
        }
        c.close();
        return num;
    }

    static public boolean queryForLogin(){
        boolean result;
        Cursor c = DB.rawQuery("SELECT id FROM " + Login_table_name,null);
        if(c.getCount() == 0){
            result =  false;
        }else {
            result =  true;
        }
        c.close();
        return result;
    }

    static public String getUserID(){
        String userID;
        Cursor c = DB.rawQuery("SELECT id FROM " + Login_table_name + " LIMIT 1",null);
        c.moveToFirst();
        userID  = c.getString(0);
        c.close();
        return userID;
    }

    static public void addUser(String id){
        ContentValues cv = new ContentValues(1);
        cv.put("id",id);
        DB.insert(Login_table_name,null,cv);
    }

    static public void insertFirst(String code){
        ContentValues cv = new ContentValues(2);
        cv.put("Room",code);
        cv.put("count",0);
        DB.insert(Badge_table_name,null,cv);
    }

    static public void badgePlus(String code){
        String instruction = "UPDATE " + Badge_table_name + " SET " + "Count = Count + 1" + " WHERE " +
                "Room = " + "'" + code + "'";
        DB.execSQL(instruction);
        upDateIcon();
    }

    static public void badgeClear(String code){
        String instruction = "UPDATE " + Badge_table_name + " SET " + "Count = 0" + " WHERE " +
                "Room = " + "'" + code + "'";
        DB.execSQL(instruction);
        upDateIcon();
    }

    static public void upDateIcon(){
        int num = 0;
        Cursor c = DB.rawQuery("SELECT " + "Count" + " FROM " + Badge_table_name,null);
        if(c.moveToFirst()){
            for(int i=0;i<c.getCount();i++){
                int cnt = c.getInt(0);
                num += cnt;
                c.moveToNext();
            }
        }
        ShortcutBadger.applyCount(ctx,num);
        c.close();
    }

    static public int getTotalUnread(){
        int num = 0;
        Cursor c = DB.rawQuery("SELECT " + "Count" + " FROM " + Badge_table_name,null);
        if(c.moveToFirst()){
            for(int i=0;i<c.getCount();i++){
                int cnt = c.getInt(0);
                num += cnt;
                c.moveToNext();
            }
        }
        c.close();
        return num;
    }

    static public void deleteAllBadge(){
        String instruction = "DELETE FROM " + Badge_table_name;
        DB.execSQL(instruction);
        upDateIcon();
    }

    static public void deleteAllUser(){
        String instruction = "DELETE FROM " + Login_table_name;
        DB.execSQL(instruction);
    }

    static public void deleteAllIntro() {
        String sql = "DELETE FROM " + Intro_table_name;
        DB.execSQL(sql);
    }

    static public void deleteAllPassword() {
        String sql = "DELETE FROM " + Password_table_name;
        DB.execSQL(sql);
    }

    static public void deleteAllVote() {
        String sql = "DELETE FROM " + vote_table_name;
        DB.execSQL(sql);
    }

    static public String getIntro() {
        String intro;
        Cursor c = DB.rawQuery("SELECT intro FROM " + Intro_table_name + " LIMIT 1", null);
        if(c.getCount() == 0) {
            intro = ctx.getString(R.string.default_intro);
            ContentValues cv = new ContentValues(1);
            cv.put("intro", intro);
            DB.insert(Intro_table_name, null, cv);
        } else {
            c.moveToFirst();
            intro = c.getString(0);
        }
        c.close();
        return intro;
    }

    static public boolean setIntro(String newIntro) {
        ContentValues cv = new ContentValues(1);
        cv.put("intro", newIntro);
        int result = DB.update(Intro_table_name, cv, null, null);
        if(result > 0) {
            return true;
        } else {
            return false;
        }
    }

    static public String getPassword() {
        String password;
        Cursor c = DB.rawQuery("SELECT password FROM " + Password_table_name + " LIMIT 1", null);
        if(c.getCount() == 0) {
            password = ctx.getString(R.string.default_password);
            ContentValues cv = new ContentValues(1);
            cv.put("password", password);
            DB.insert(Password_table_name, null, cv);
        } else {
            c.moveToFirst();
            password = c.getString(0);
        }
        c.close();
        return password;
    }

    static public boolean setPassword(String newPassword) {
        ContentValues cv = new ContentValues(1);
        cv.put("password", newPassword);
        int result = DB.update(Password_table_name, cv, null, null);
        if(result > 0) {
            return true;
        } else {
            return false;
        }
    }

    static public void insertVoteRes(int annoc_pk)
    {
        ContentValues cv = new ContentValues(1);
        cv.put("annoc_pk", annoc_pk);
        DB.insert(vote_table_name, null, cv);
    }

    static public boolean queryForVote(int pk)
    {
        String sql = String.format(Locale.getDefault(),
                "SELECT null FROM %s WHERE annoc_pk = '%d'",
                vote_table_name, pk);
        Cursor c = DB.rawQuery(sql, null);
        Log.d("SQL", String.format("pk = %d, count = %d\n", pk, c.getCount()));

        if(c.getCount() > 0) {
            c.close();
            return true;
        }
        else {
            c.close();
            return false;
        }
    }
}
