package com.neutron.musicbox.database.data;

public class Song {
	
	private Long id;
	private String title;
	private String artiste;
	private Long albumId;
	private String path;
	
	public Song(Long id, String title, String artiste, Long albumId, String path) {
		super();
		this.id = id;
		this.title = title;
		this.artiste = artiste;
		this.albumId = albumId;
		this.path = path;
	}
	public Song() {
		this.id = (long) 0;
		this.title = "";
		this.artiste = "";
		this.albumId = (long) 0;
		this.path = "";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtiste() {
		return artiste;
	}
	public void setArtiste(String artiste) {
		this.artiste = artiste;
	}
	public Long getAlbumId() {
		return albumId;
	}
	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public String toString() {
		return "Song [id=" + id + ", title=" + title + ", artiste=" + artiste
				+ ", albumId=" + albumId + ", path=" + path + "]";
	}
	
}
