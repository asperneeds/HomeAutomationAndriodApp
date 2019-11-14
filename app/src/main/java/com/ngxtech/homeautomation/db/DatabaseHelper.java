package com.ngxtech.homeautomation.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ngxtech.homeautomation.bean.Category;
import com.ngxtech.homeautomation.bean.DeviceItem;
import com.ngxtech.homeautomation.bean.Switch;
import com.ngxtech.homeautomation.utils.DebugLog;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String CategoryTable = "categorytable";
    private static String Category_Name = "name";
    private static String ID = "_id";

    private static String SwitchTable ="switchtable";
    private static String Sid="_id";
    private static String SwitchName="switchname";
    private static String Device_id="deviceid";





    private static String DeviceTable ="devicetable";
    private static String Did="_id";
    private static String Devicename="devicename";
    private static String DeviceMACID="devicemacid";
    private static String Category_id="categoryid";
    private static String Switchcount="switchcount";




    private static final String  br = "Create Table " + CategoryTable + "( " + ID + "  Integer primary key autoincrement , " + Category_Name + " text )";
    private static final String br2= " Create Table " + SwitchTable + "( " + Sid + "  Integer primary key autoincrement , " + SwitchName + " text ,"+ Device_id + " text )";
    private static final String br3= " Create Table " + DeviceTable + "(  " + DeviceMACID + " text  primary key ,"+Category_id + " text ,"+Switchcount+" text ,"+Devicename+ " text )";



   public SQLiteDatabase database;
    private static DatabaseHelper dbHelper;

    public DatabaseHelper(Context context) {
        super(context, "homeautomation.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(br);
        db.execSQL(br2);
        db.execSQL(br3);


    }

    public static DatabaseHelper getInstance(Context context){
        if(dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
            return dbHelper;
        }
        else
        {
            return dbHelper;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table If Exists " + CategoryTable);
        db.execSQL("Drop Table If Exists " + SwitchTable);
        db.execSQL("Drop Table If exists " + DeviceTable);
        onCreate(db);


    }


    public boolean insertData(Category rname) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(Category_Name, rname.getCategory_name());
        long result = database.insert(CategoryTable, null, values);
        if (result == -1) {
            return false;
        } else return true;

    }


    public boolean insertDeviceData(DeviceItem rname) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();


             values.put(Devicename, "Device1");
             values.put(DeviceMACID, rname.getDeviceMACID());
             values.put(Switchcount, rname.getSwitchCount());
             values.put(Category_id, rname.getCategory_id());





        long result = database.insert(DeviceTable, null, values);
        if (result == -1) {
            return false;
        } else return true;

    }

    public Cursor GetDeviceData() {
        database = dbHelper.getWritableDatabase();
        return database.rawQuery("Select * from "+DeviceTable,null);






    }


    public boolean insertSwitchData(Switch switches) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

            values.put(SwitchName, switches.getSwitchName());
            values.put(Device_id, switches.getDevice_id());







        long result = database.insert(SwitchTable, null, values);
        if (result == -1) {
            return false;
        } else return true;

    }


    public boolean updateCategoryname(Category rname) {
        database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Category category =new Category();

        values.put(Category_Name, rname.getCategory_name());

      long result=  database.update(CategoryTable, values, ID + " = ?",
                new String[]{String.valueOf(rname.getId())});
        //  values.put(switchCount,rname.getSwitchCount());
        //   values.put(ssid,rname.getSsid());
        // long result=  database.update(CategoryTable,,null);
     if (result==-1) {
         return false;
     }else
      return true;

    }

    public ArrayList<Category> GetData(){

        ArrayList<Category> names=new ArrayList<>();
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        Cursor cursor= database.rawQuery(" select * from "+ CategoryTable,null);
        DebugLog.logTrace("");
        Category category;
       if (cursor.moveToFirst()) {
           do {
               category = new Category();
               String id = cursor.getString(cursor.getColumnIndex(ID));
               category.setId(id);
               String Name = cursor.getString(cursor.getColumnIndex(Category_Name));
               category.setCategory_name(Name);

               names.add(category);
               DebugLog.logTrace("read data id : "+id);
               //   names.add(new Category(cursor.getInt(cursor.getColumnIndex(ID)),cursor.getString(cursor.getColumnIndex(Room_Name))));


           } while (cursor.moveToNext());
       }

        return names;


    }

    public ArrayList<Switch> GetSwitchdata(){

        ArrayList<Switch> switches=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor= database.rawQuery(" select * from "+ SwitchTable,null);
        DebugLog.logTrace("");
        Switch switchmodel;
        if (cursor.moveToFirst()) {
            do {
                switchmodel = new Switch();
                String id = cursor.getString(cursor.getColumnIndex(Sid));
                switchmodel.set_id(id);
                String Name = cursor.getString(cursor.getColumnIndex(SwitchName));
                switchmodel.setSwitchName(Name);

                switches.add(switchmodel);
                DebugLog.logTrace("read data id : "+id);
                //   names.add(new Category(cursor.getInt(cursor.getColumnIndex(ID)),cursor.getString(cursor.getColumnIndex(Room_Name))));


            } while (cursor.moveToNext());
        }

        return switches;


    }


    public void deleteCategoryname(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CategoryTable, ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        db.close();
    }
    public  void deleteTable(Context context) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {

            db.execSQL("DELETE FROM " + CategoryTable);

        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }


    public  boolean isItemAvailable(Context context) {


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean exists = false;


        String query = "SELECT * FROM " + DeviceTable;


        try {
            Cursor cursor = db.rawQuery(query, null);

            exists = (cursor.getCount() > 0);
            cursor.close();

        } catch (SQLiteException e) {

            e.printStackTrace();
            db.close();

        }

        return exists;
    }
    public void UpdateSwitchName( Switch switches,String id) {
        SQLiteDatabase sdb = this.getWritableDatabase();
        // sdb.execSQL("UPDATE "+TABLE_NAME+"SET name ='"+adapter.getName()+"',age= '"+adapter.getAge()+"',occupation= '"+adapter.getOccupation()+"',image= '"+adapter.getImage()+'" WHERE _id='"+id+"'");
        sdb.execSQL("UPDATE  " + SwitchTable + " SET SwitchName ='" + switches.getSwitchName() +  "'  WHERE _id='" + id + "'");

    }

    public  boolean isItemSwitchAvailable(Context context) {


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean exists = false;


        String query = "SELECT * FROM " + SwitchTable;


        try {
            Cursor cursor = db.rawQuery(query, null);

            exists = (cursor.getCount() > 0);
            cursor.close();

        } catch (SQLiteException e) {

            e.printStackTrace();
            db.close();

        }

        return exists;
    }
               public void Rollback(Context context){
                   try {
                     database = dbHelper.getReadableDatabase();
                       database.beginTransaction();
                   //    GetData();
                       String query = "SELECT * FROM " + CategoryTable;
                       Cursor cursor = database.rawQuery(query, null);
                       while (cursor.moveToNext()){
                           System.out.print(cursor+"Curser");
                           Log.d("Curser","cuserdata"+cursor);


                       }
                       // your sql stuff
                       database.setTransactionSuccessful();
                   } catch(SQLException e) {
                       // do some error handling
                   } finally {
                       database.endTransaction();
                   }
               }


    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(DeviceTable, null, null);
        db.delete(SwitchTable, null, null);
    }
}
