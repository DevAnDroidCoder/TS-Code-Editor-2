package com.ts.code.editor.editor;

import com.ts.code.editor.R;
import com.ts.code.editor.FileUtil;

import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme;
import io.github.rosemoe.sora.langs.java.JavaLanguage;
import io.github.rosemoe.sora.lang.Language;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.*;

import java.lang.Boolean;
import java.lang.String;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import android.graphics.Typeface;
import org.eclipse.tm4e.core.theme.IRawTheme;
import org.eclipse.tm4e.core.internal.theme.reader.ThemeReader;


public class SoraEditor {
	public CodeEditor sora_editor;
	
	
	public void SetUpSoraCodeEditor(LinearLayout _linear,String _path,Context _c){
        // clear editor
        _linear.removeAllViews();
		// New Code editor
		sora_editor = (CodeEditor)((LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sora_code_editor,null).findViewById(R.id.CodeEditor);
		// Theme name from assets
		String theme = "MaterialLight.tmTheme";
		// Set code font
		sora_editor.setTypefaceText(Typeface.createFromAsset(_c.getAssets(), "Editor/Sora-editor/fonts/jetbrains.ttf"));
		// Set line font
		sora_editor.setTypefaceLineNumber(Typeface.createFromAsset(_c.getAssets(), "Editor/Sora-editor/fonts/jetbrains.ttf"));
		// Enable editing
		sora_editor.setEditable(true);
		// Set editor cursor width
		sora_editor.setCursorWidth(4);
		// Enable line number
		sora_editor.setPinLineNumber(true);
		//sora_editor.setHighlightBracketPair(true);
		sora_editor.setLigatureEnabled(true);
		
		sora_editor.showSoftInput();
		
		// Configuration of editor for java 
		// Java Configration start
        /*
		if (TSUtils.fileType(_path).equals("Java")) {
			try {
				EditorColorScheme colorScheme = sora_editor.getColorScheme();
				if (!(colorScheme instanceof TextMateColorScheme)) {
					IRawTheme iRawTheme = ThemeReader.readThemeSync(theme, _c.getAssets().open("Editor/Sora-editor/textmate/" + theme));
					colorScheme = TextMateColorScheme.create(iRawTheme);
					sora_editor.setColorScheme(colorScheme);
				}
				Language language = TextMateLanguage.create("java.tmLanguage.json",_c.getAssets().open("Editor/Sora-editor/textmate/java/syntaxes/java.tmLanguage.json"),new InputStreamReader(_c.getAssets().open("Editor/Sora-editor/textmate/java/language-configuration.json")),((TextMateColorScheme) colorScheme).getRawTheme());
				
				sora_editor.setEditorLanguage(language);
				sora_editor.rerunAnalysis();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
        */
		// Java Configration end
		
		// Check if file path is readable
		if (new File(_path).canRead()) {
			// Read file path and set as editor code
			sora_editor.setText(FileUtil.readFile(_path));
		}
		else {
			// File path is not readable just through an error
			sora_editor.setText("Error while opening file.");
		}
		
		// remove sora editor from it's parent to add in _linear
		if (((LinearLayout)sora_editor.getParent()) != null) {
			((LinearLayout)sora_editor.getParent()).removeView(sora_editor);
		}
		// add sora editor to _linear
		_linear.addView(sora_editor);
		/*
		LastUsedSoraCodeEditor = sora_editor;
		EditorSessionAddMap = new HashMap<>();
		EditorSessionAddMap.put("editor", LastUsedSoraCodeEditor);
		EditorSessionAddMap.put("path", _path);
		EditorSessionAddMap.put("editor_type", Editor);
		EditorSession.add(EditorSessionAddMap);
		EditorSessionNumber++;
		*/
	}
	public CodeEditor GetSoraEditor (){
		if(sora_editor == null){
			return null;
		}else{
			return sora_editor;
		}
	}
}
