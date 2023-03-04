package com.ts.code.editor.editor;

import android.net.Uri;

import java.lang.String;
import java.lang.Boolean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class TSUtils {
	public static String fileType(String _path){
		String fileTypeExtention = new String();
		try{
			if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("html").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("html"))) {
				fileTypeExtention = "HTML";
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("css").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("css"))){
				fileTypeExtention = "CSS";
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("js").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("js"))){
				fileTypeExtention = "JavaScript";
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("java").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("java"))){
				fileTypeExtention = "Java";
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("xml").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("xml"))){
				fileTypeExtention = "XML";
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("json").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("json"))){
				fileTypeExtention = "JSON";
			}
			else if (Uri.parse(_path).getLastPathSegment().substring((int)(Uri.parse(_path).getLastPathSegment().length() - ".".concat("txt").length()), (int)(Uri.parse(_path).getLastPathSegment().length())).equals(".".concat("txt"))){
				fileTypeExtention = "TEXT";
			}
		}catch(Exception e){
			fileTypeExtention = "null";
		}
		return (fileTypeExtention);
	}
}
