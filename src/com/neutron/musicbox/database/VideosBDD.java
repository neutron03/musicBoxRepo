package com.neutron.musicbox.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.neutron.musicbox.database.data.Video;

public class VideosBDD {
	
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "musicbox.db";
 
	private static final String Tvideos = "Tvideos";
	private static final String COL_ID = "ID";
	private static final int NUM_COL_ID = 0;
	private static final String COL_NAME = "NAME";
	private static final int NUM_COL_NAME = 1;
	
	private android.database.sqlite.SQLiteDatabase bdd;
	private SQLiteDatabase sqliteDatabase;
	
	public VideosBDD(Context context){
		//On cr�e la BDD et sa table
		sqliteDatabase = new SQLiteDatabase(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en �criture
		bdd = sqliteDatabase.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'acc�s � la BDD
		bdd.close();
	}
	public android.database.sqlite.SQLiteDatabase getBDD(){
		return bdd;
	}
	
	public long insertVideo(Video video){
		//Cr�ation d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associ�e � une cl� (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_NAME, video.getName());
		//on ins�re l'objet dans la BDD via le ContentValues
		return bdd.insert(Tvideos, null, values);
	}
	
	public int updateVideo(int id, Video video){
		//La mise � jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simplement pr�ciser quel livre on doit mettre � jour gr�ce � l'ID
		ContentValues values = new ContentValues();
		values.put(COL_NAME, video.getName());
		return bdd.update(Tvideos, values, COL_ID + " = " +id, null);
	}
	
	public int removeVideoWithID(int id){
		//Suppression d'un livre de la BDD gr�ce � l'ID
		return bdd.delete(Tvideos, COL_ID + " = " +id, null);
	}
 
	public Video getVideoWithName(String name){
		//R�cup�re dans un Cursor les valeurs correspondant � un livre contenu dans la BDD (ici on s�lectionne le livre gr�ce � son titre)
		Cursor c = bdd.query(Tvideos, new String[] {COL_ID, COL_NAME}, COL_NAME + " LIKE \"" + name +"\"", null, null, null, null);
		return cursorToVideo(c);
	}
 
	//Cette m�thode permet de convertir un cursor en un livre
	private Video cursorToVideo(Cursor c){
		//si aucun �l�ment n'a �t� retourn� dans la requ�te, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier �l�ment
		c.moveToFirst();
		//On cr�� un livre
		Video video = new Video();
		//on lui affecte toutes les infos gr�ce aux infos contenues dans le Cursor
		video.setId(c.getInt(NUM_COL_ID));
		video.setName(c.getString(NUM_COL_NAME));
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return video;
	}
}
