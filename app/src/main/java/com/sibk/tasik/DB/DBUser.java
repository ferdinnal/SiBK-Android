package com.sibk.tasik.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.sibk.tasik.Model.User;


public class DBUser extends SQLiteAssetHelper {
    private static final String DATABASE_NAME="sibk.sqlite";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="user";
    private static final String KEY_ID="userid";
    private static final String KEY_NAMA_LENGKAP="fullname";
    private static final String KEY_ALAMAT_EMAIL="email";
    private static final String KEY_TIPE_PENGGUNA="usertypeid";

    public DBUser(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //untuk cek apakah data ada isinya atau tidak
    public boolean isNull()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM "+TABLE_NAME+"";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        db.close();
        return icount <= 0;
    }

    //untuk simpan data
    public void save(User user)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_ID, user.getuserid());
        values.put(KEY_NAMA_LENGKAP, user.getfullname());
        values.put(KEY_ALAMAT_EMAIL,user.getemail());
        values.put(KEY_TIPE_PENGGUNA,user.getusertypeid());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //untuk mendapatkan data
    public User findUser()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,new String[]{KEY_ID,KEY_NAMA_LENGKAP,KEY_ALAMAT_EMAIL,KEY_TIPE_PENGGUNA},null,null,null,null,null);

        User u=new User();
        if (cursor!=null)
        {
            cursor.moveToFirst();
            u.setuserid(cursor.getInt(0));
            u.setfullname(cursor.getString(1));
            u.setemail(cursor.getString(2));
            u.setusertypeid(cursor.getInt(3));

        }else
        {
            u.setuserid(0);
            u.setfullname("");
            u.setemail("");
            u.setusertypeid(0);
        }

        db.close();

        return u;
    }

    //untuk bersihkan atau hapus semua data
    public void delete()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
