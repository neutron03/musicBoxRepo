package com.neutron.musicbox.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabase extends SQLiteOpenHelper {
	 
		

		private static final String Tvideos = "Tvideos";
		private static final String COL_ID = "ID";
		private static final String COL_NAME = "NAME";
	 
		private static final String CREATE_BDD = "CREATE TABLE " + Tvideos + " ("
		+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT NOT NULL);";
	 
		public SQLiteDatabase(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onCreate(android.database.sqlite.SQLiteDatabase db) {
			// TODO Auto-generated method stub
			//on crée la table à partir de la requête écrite dans la variable CREATE_BDD
			db.execSQL(CREATE_BDD);
		}

		@Override
		public void onUpgrade(android.database.sqlite.SQLiteDatabase db,
				int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE " + Tvideos + ";");
			onCreate(db);
			
		}
	}