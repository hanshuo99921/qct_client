package com.example.qct_client.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	public DatabaseOpenHelper(Context context) {
		super(context, "qct_client", null, 2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE dzxx (id integer primary key autoincrement,"
				+ "xm varchar(20) not null,lxdh varchar(30),xzqh varchar(20),dz varchar(200),bz varchar(1))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(oldVersion==1 && newVersion==2){
			db.execSQL("ALTER TABLE dzxx ADD COLUMN xzqh varchar(20)");
			db.execSQL("ALTER TABLE dzxx ADD COLUMN bz varchar(1)");
		}
		
	}

}
