package com.ts.code.editor;

import java.lang.String;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.content.Context;
import android.Manifest;

public class TSDTheme {
	public SharedPreferences theme;
	public String themeName;
	public String PrimaryColor;
	public String SecondaryColor;
	public String ColorOnPrimary;
	public String ColorOnSecondary;
	public boolean isDarkTheme;
	public boolean isThemeColorsExists = false;
	public String[] themeList;
	public String[] themeColors;
	public Context c;
	
	//initialize 
	public void initialize(SharedPreferences sp,Context _c){
		theme = sp;
        c = _c;
	}
	
	//get theme list data
	public String[] getTSDThemeColorList(){
		return c.getResources().getStringArray(R.array.themelist);
	}
	
	//update theme data
	public void getCurrentTSDThemeData(){
		//get theme name
		if (theme.contains("EditorTheme")) {
			themeList = c.getResources().getStringArray(R.array.themelist);
			for(int position = 0;position < themeList.length -1;position++) {
				if (themeList[position].equals(theme.getString("EditorTheme", ""))) {
					themeName = themeList[position];
				}
			}
		}else {
			themeList = c.getResources().getStringArray(R.array.themelist);
			themeName = themeList[0];
		}
		
		//Get theme data in variables
		if(themeName.equals("light")){
			themeColors = c.getResources().getStringArray(R.array.light);
            isThemeColorsExists = true;
		}
		if(themeName.equals("dark")){
			themeColors = c.getResources().getStringArray(R.array.dark);
            isThemeColorsExists = true;
		}
		if(isThemeColorsExists){
			PrimaryColor = themeColors[0];
			SecondaryColor = themeColors[1];
			isDarkTheme = Boolean.parseBoolean(themeColors[3]);
		}
	}
	
	public void getCurrentTSDThemeData(String name){
		//get theme name
		themeName = name;
		//Get theme data in variables
		if(themeName.equals("light")){
			themeColors = c.getResources().getStringArray(R.array.light);
            isThemeColorsExists = true;
		}
		if(themeName.equals("dark")){
			themeColors = c.getResources().getStringArray(R.array.dark);
            isThemeColorsExists = true;
		}
		
		if(isThemeColorsExists){
			PrimaryColor = themeColors[0];
			SecondaryColor = themeColors[1];
			isDarkTheme = Boolean.parseBoolean(themeColors[3]);
		}
	}
}
