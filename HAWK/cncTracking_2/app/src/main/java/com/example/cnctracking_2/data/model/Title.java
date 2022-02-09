package com.example.cnctracking_2.data.model;

import com.google.gson.annotations.SerializedName;

public class Title{

	@SerializedName("text")
	private String text;

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	@Override
 	public String toString(){
		return 
			"Title{" + 
			"text = '" + text + '\'' + 
			"}";
		}
}