package com.neutron.musicbox.database.data;

public class Video {
	
	private int id;
	private String name;
	
	
	
	public Video(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Video() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", name=" + name + "]";
	}
	
}
