package com.ts.code.editor.editor;

import java.lang.String;
import java.lang.Boolean;

import android.content.Context;
import android.content.pm.PackageManager;
import android.Manifest;
import android.net.Uri;
import android.widget.*;


import androidx.core.content.ContextCompat;

import com.ts.code.editor.CodeEditorActivity;

public class Editor {
	public static int DEFAULT_EDITOR = 0;
	public static int ACE_EDITOR = 1;
	public static int SORA_EDITOR = 2;
	public Context c;
	public boolean CanOpenInEditor(String _path){
		Boolean canOpen = new Boolean(false);
		try{
			if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("html").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("html"))) {
				canOpen = true;
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("css").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("css"))){
				canOpen = true;
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("js").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("js"))){
				canOpen = true;
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("java").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("java"))){
				canOpen = true;
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("xml").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("xml"))){
				canOpen = true;
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("json").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("json"))){
				canOpen = true;
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("txt").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("txt"))){
				canOpen = true;
			}
		}catch(Exception e){
			canOpen = false;
		}
		return (canOpen);
	}
	
}
