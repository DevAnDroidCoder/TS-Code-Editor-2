package com.ts.code.editor;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.rosemoe.sora.*;
import io.github.rosemoe.sora.langs.java.*;
import io.github.rosemoe.sora.langs.textmate.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import android.Manifest;
import android.graphics.Typeface;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageInfo;
import androidx.core.content.FileProvider;
import org.eclipse.tm4e.core.theme.IRawTheme;
import org.eclipse.tm4e.core.internal.theme.reader.ThemeReader;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme;
import io.github.rosemoe.sora.lang.Language;
import java.io.InputStreamReader;
import io.github.rosemoe.sora.langs.java.JavaLanguage;

public class CodeviewActivity extends AppCompatActivity {
	
	public LinearLayout ll2;
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private DrawerLayout _drawer;
	private boolean isRequested = false;
	private double pos = 0;
	private String path = "";
	private String CurrentPath = "";
	private String ListViewPath = "";
	private boolean checkIfAlreadyHave = false;
	private double pos2 = 0;
	private boolean canOpen = false;
	private String Editor = "";
	private boolean AceEditor = false;
	private boolean SoraEditor = false;
	private double EditorSessionNumber = 0;
	public CodeEditor LastUsedSoraCodeEditor;
	public WebView LastUsedAceEditor;
	private HashMap<String, Object> d = new HashMap<>();
	private String EditorPath = "";
	
	private ArrayList<String> fileList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> FileListData = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> TabsDataOfFile = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> EditorSession = new ArrayList<>();
	
	private LinearLayout Main_LinearLayout;
	private LinearLayout Main_LinearLayout_CodeEditorSection;
	private LinearLayout Main_LinearLayout_SideBar;
	private LinearLayout editor;
	private LinearLayout CodeEditorSection_LinearLayout_Toolbar;
	private HorizontalScrollView CodeEditorSection_ScrollView_FileTab;
	private HorizontalScrollView Toolbar_ScrollView_Toolbar;
	private LinearLayout Toolbar_LinearLayout_Toolbar;
	private TextView textview2;
	private TextView textview3;
	private LinearLayout CodeEditorSection_LinearLayout_FileTab;
	private HorizontalScrollView SideBar_ScrollView_Menu;
	private HorizontalScrollView SideBar_ScrollView_FileList;
	private LinearLayout SideBar_LinearLayout_Menu;
	private LinearLayout SideBar_LinearLayout_FileList;
	private ScrollView vscroll1;
	private TextView FileList_TextView_FileListError;
	private LinearLayout FileList_LinearLayout_FileListContainer;
	private LinearLayout linear_container_holder;
	
	private MaterialAlertDialogBuilder MaterialDialog;
	private SharedPreferences Tabs;
	private MaterialAlertDialogBuilder StoragePermission;
	private MaterialAlertDialogBuilder StoragePermission2;
	private MaterialAlertDialogBuilder Exit;
	private SharedPreferences setting;
	private Intent Setting = new Intent();
	private View EditorChooser;
	private PopupWindow EditorChooserPopupWindow;
	private View FileTreeInflater;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.codeview);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		_drawer = findViewById(R.id._drawer);
		ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(CodeviewActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
		_drawer.addDrawerListener(_toggle);
		_toggle.syncState();
		
		LinearLayout _nav_view = findViewById(R.id._nav_view);
		
		Main_LinearLayout = findViewById(R.id.Main_LinearLayout);
		Main_LinearLayout_CodeEditorSection = findViewById(R.id.Main_LinearLayout_CodeEditorSection);
		Main_LinearLayout_SideBar = findViewById(R.id.Main_LinearLayout_SideBar);
		editor = findViewById(R.id.editor);
		CodeEditorSection_LinearLayout_Toolbar = findViewById(R.id.CodeEditorSection_LinearLayout_Toolbar);
		CodeEditorSection_ScrollView_FileTab = findViewById(R.id.CodeEditorSection_ScrollView_FileTab);
		Toolbar_ScrollView_Toolbar = findViewById(R.id.Toolbar_ScrollView_Toolbar);
		Toolbar_LinearLayout_Toolbar = findViewById(R.id.Toolbar_LinearLayout_Toolbar);
		textview2 = findViewById(R.id.textview2);
		textview3 = findViewById(R.id.textview3);
		CodeEditorSection_LinearLayout_FileTab = findViewById(R.id.CodeEditorSection_LinearLayout_FileTab);
		SideBar_ScrollView_Menu = findViewById(R.id.SideBar_ScrollView_Menu);
		SideBar_ScrollView_FileList = findViewById(R.id.SideBar_ScrollView_FileList);
		SideBar_LinearLayout_Menu = findViewById(R.id.SideBar_LinearLayout_Menu);
		SideBar_LinearLayout_FileList = findViewById(R.id.SideBar_LinearLayout_FileList);
		vscroll1 = findViewById(R.id.vscroll1);
		FileList_TextView_FileListError = findViewById(R.id.FileList_TextView_FileListError);
		FileList_LinearLayout_FileListContainer = findViewById(R.id.FileList_LinearLayout_FileListContainer);
		linear_container_holder = findViewById(R.id.linear_container_holder);
		MaterialDialog = new MaterialAlertDialogBuilder(this);
		Tabs = getSharedPreferences("Tabs", Activity.MODE_PRIVATE);
		StoragePermission = new MaterialAlertDialogBuilder(this);
		StoragePermission2 = new MaterialAlertDialogBuilder(this);
		Exit = new MaterialAlertDialogBuilder(this);
		setting = getSharedPreferences("setting", Activity.MODE_PRIVATE);
		
		textview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				EditorChooser = getLayoutInflater().inflate(R.layout.menu_codeeditor, null);
				EditorChooserPopupWindow = new PopupWindow(EditorChooser, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
				((LinearLayout)EditorChooser.findViewById(R.id.Option_Ace_Editor)).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
						if (Editor.equals("Ace Editor")) {
							if (LastUsedAceEditor != null) {
								((LinearLayout)LastUsedAceEditor.getParent()).removeView(LastUsedAceEditor);
							}
						}
						if (Editor.equals("Sora Editor")) {
							if (LastUsedSoraCodeEditor != null) {
								((LinearLayout)LastUsedSoraCodeEditor.getParent()).removeView(LastUsedSoraCodeEditor);
							}
						}
						if (LastUsedAceEditor != null) {
							LastUsedAceEditor.setVisibility(View.GONE);
						}
						if (LastUsedSoraCodeEditor != null) {
							LastUsedSoraCodeEditor.setVisibility(View.GONE);
						}
						Editor = "Ace Editor";
						EditorPath = EditorPath;
						System.out.println("Using Ace Editor");
						LayoutInflater inflater = getLayoutInflater();
						View layout;
						WebView ace_editor;
						layout = inflater.inflate(R.layout.ace_code_editor, null);
						ace_editor = (WebView)layout.findViewById(R.id.CodeEditor);
						if(ace_editor.getParent() != null){
							    ((LinearLayout)ace_editor.getParent()).removeView(ace_editor);
						}
						editor.addView(ace_editor);
						ace_editor.getSettings().setJavaScriptEnabled(true);
						ace_editor.getSettings().setSupportZoom(true);
						ace_editor.loadUrl("https://www.google.com/search?q=test ".concat(EditorPath));
						_closeInEditor(EditorPath);
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("editor", ace_editor);
							EditorSession.add((int)EditorSessionNumber, _item);
						}
						
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("path", EditorPath);
							EditorSession.add((int)EditorSessionNumber, _item);
						}
						
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("editor_type", Editor);
							EditorSession.add((int)EditorSessionNumber, _item);
						}
						
						EditorSessionNumber++;
						LastUsedAceEditor = ace_editor;
						}
				});
				((LinearLayout)EditorChooser.findViewById(R.id.Option_Sora_Editor)).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
						if (Editor.equals("Ace Editor")) {
							if (LastUsedAceEditor != null) {
								((LinearLayout)LastUsedAceEditor.getParent()).removeView(LastUsedAceEditor);
							}
						}
						if (Editor.equals("Sora Editor")) {
							if (LastUsedSoraCodeEditor != null) {
								((LinearLayout)LastUsedSoraCodeEditor.getParent()).removeView(LastUsedSoraCodeEditor);
							}
						}
						if (LastUsedAceEditor != null) {
							LastUsedAceEditor.setVisibility(View.GONE);
						}
						if (LastUsedSoraCodeEditor != null) {
							LastUsedSoraCodeEditor.setVisibility(View.GONE);
						}
						Editor = "Sora Editor";
						EditorPath = EditorPath;
						boolean isEditorExists = false;
						pos2 = 0;
						isEditorExists = false;
						final CodeEditor sora_editor = (CodeEditor)getLayoutInflater().inflate(R.layout.sora_code_editor,null).findViewById(R.id.CodeEditor);
						String theme = "MaterialLight.tmTheme";
						
						sora_editor.setTypefaceText(Typeface.createFromAsset(getAssets(), "Editor/Sora-editor/fonts/jetbrains.ttf"));
						sora_editor.setTypefaceLineNumber(Typeface.createFromAsset(getAssets(), "Editor/Sora-editor/fonts/jetbrains.ttf"));
						//sora_editor.setTextSize(14);
						sora_editor.setEditable(true);
						//sora_editor.setColorScheme();
						sora_editor.setCursorWidth(4);
						sora_editor.setPinLineNumber(true);
						//sora_editor.setHighlightBracketPair(true);
						sora_editor.setLigatureEnabled(true);
						sora_editor.showSoftInput();
						if (_fileType(EditorPath).equals("Java")) {
							try {
									EditorColorScheme colorScheme = sora_editor.getColorScheme();
									if (!(colorScheme instanceof TextMateColorScheme)) {
											IRawTheme iRawTheme = ThemeReader.readThemeSync(theme, getAssets().open("Editor/Sora-editor/textmate/" + theme));
											colorScheme = TextMateColorScheme.create(iRawTheme);
											sora_editor.setColorScheme(colorScheme);
									}
									Language language = TextMateLanguage.create("java.tmLanguage.json",
									getAssets().open("Editor/Sora-editor/textmate/java/syntaxes/java.tmLanguage.json"),
									new InputStreamReader(getAssets().open("Editor/Sora-editor/textmate/java/language-configuration.json")),
									((TextMateColorScheme) colorScheme).getRawTheme());
									
									sora_editor.setEditorLanguage(language);
									sora_editor.rerunAnalysis();
									
							} catch (Exception e) {
									e.printStackTrace();
							}
							
						}
						if (FileUtil.isExistFile(EditorPath)) {
							if (new File(EditorPath).canRead()) {
								sora_editor.setText(FileUtil.readFile(EditorPath));
							}
							else {
								sora_editor.setText("Read Permission Denied.");
							}
						}
						else {
							sora_editor.setText("Path doesn't exists.");
						}
						_closeInEditor(EditorPath);
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("editor", sora_editor);
							EditorSession.add((int)EditorSessionNumber, _item);
						}
						
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("path", EditorPath);
							EditorSession.add((int)EditorSessionNumber, _item);
						}
						
						{
							HashMap<String, Object> _item = new HashMap<>();
							_item.put("editor_type", "Sora Editor");
							EditorSession.add((int)EditorSessionNumber, _item);
						}
						
						EditorSessionNumber++;
						if (((LinearLayout)sora_editor.getParent()) != null) {
							((LinearLayout)sora_editor.getParent()).removeView(sora_editor);
						}
						editor.addView(sora_editor);
						LastUsedSoraCodeEditor = sora_editor;
						}
				});
				EditorChooserPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
				EditorChooserPopupWindow.showAsDropDown(textview2, 0, 0);
			}
		});
		
		textview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "Save");
			}
		});
		
		CodeEditorSection_LinearLayout_FileTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
	}
	
	private void initializeLogic() {
		getSupportActionBar().hide();
		if (getIntent().hasExtra("path")) {
			setTitle(Uri.parse(getIntent().getStringExtra("path")).getLastPathSegment());
			path = getIntent().getStringExtra("path");
			_UpdateWithPath();
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			
			StoragePermission.setTitle("Storage Permission required");
			StoragePermission.setMessage("Storage permission is required please allow app to use storage in next page.");
			StoragePermission.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					_requestStoragePermission();
				}
			});
			StoragePermission.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					_showRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app");
				}
			});
			StoragePermission.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					finishAffinity();
				}
			});
			StoragePermission.create().show();
		} else {
				startManually();
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		if (_requestCode == 144) {
				
		}
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	public void startManually(){
		/*
_FileListOfPath(path, FileList_ListView_FileList, false);
*/
		if (new File(path).canRead()) {
			_ShowList(path, linear_container_holder);
			ListViewPath = path;
			vscroll1.setVisibility(View.VISIBLE);
			FileList_TextView_FileListError.setVisibility(View.GONE);
		}
		else {
			vscroll1.setVisibility(View.GONE);
			FileList_TextView_FileListError.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int _requestCode, String[] _permissions, int[] _grantResults) {
		for(int _repeat11 = 0; _repeat11 < (int)(_permissions.length); _repeat11++) {
			String Permission = _permissions[_repeat11];
			if (_grantResults[_repeat11] == PackageManager.PERMISSION_DENIED) {
				boolean showRationale = shouldShowRequestPermissionRationale(Permission);
				if (showRationale) {
					if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(Permission)) {
						_showRationale(Permission, "Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app");
					}
					if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(Permission)) {
						_showRationale(Permission, "Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app");
					}
				}
				else {
					if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(Permission)) {
						StoragePermission2.setTitle("Storage permission required");
						StoragePermission2.setMessage("Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app.");
						StoragePermission2.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								isRequested = true;
								Intent intent = new Intent();
								intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", CodeviewActivity.this.getPackageName(),null);
								intent.setData(uri);
								CodeviewActivity.this.startActivity(intent);
							}
						});
						StoragePermission2.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								finishAffinity();
							}
						});
						StoragePermission2.create().show();
					}
					//setting
					if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(Permission)) {
						StoragePermission2.setTitle("Storage permission required");
						StoragePermission2.setMessage("Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app.");
						StoragePermission2.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								isRequested = true;
								Intent intent = new Intent();
								intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", CodeviewActivity.this.getPackageName(),null);
								intent.setData(uri);
								CodeviewActivity.this.startActivity(intent);
							}
						});
						StoragePermission2.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								finishAffinity();
							}
						});
						StoragePermission2.create().show();
					}
				}
			}
			else {
				if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(Permission)) {
					startManually();
				}
			}
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		if (isRequested) {
			isRequested = false;
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
			|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
					ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
			} else {
					startManually();
			}
			
		}
	}
	
	@Override
	public void onBackPressed() {
		if (path.equals(ListViewPath)) {
			Exit.setTitle("Close?");
			Exit.setMessage("Do you want to close project?");
			Exit.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					 
				}
			});
			Exit.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					finish();
				}
			});
			Exit.create().show();
		}
		else {
			/*
_FileListOfPath(ListViewPath.replace("/".concat(Uri.parse(ListViewPath).getLastPathSegment()), ""), FileList_ListView_FileList, false);
*/
			_ShowList(path, linear_container_holder);
		}
	}
	public void _UI() {
		
	}
	
	
	public void _showRationale(final String _permission, final String _text) {
		if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(_permission)) {
			StoragePermission2.setTitle("Storage Permission required");
			StoragePermission2.setMessage(_text);
			StoragePermission2.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					if (ContextCompat.checkSelfPermission(CodeviewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
					|| ContextCompat.checkSelfPermission(CodeviewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
							ActivityCompat.requestPermissions(CodeviewActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
					} else {
							CodeviewActivity m = new CodeviewActivity();
							m.startManually();
					}
					
				}
			});
			StoragePermission2.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					finishAffinity();
				}
			});
			StoragePermission2.create().show();
		}
	}
	
	
	public void _requestStoragePermission() {
		ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
	}
	
	
	public void _FileListOfPath(final String _path, final ListView _listview, final boolean _isInside) {
		/*
if (FileUtil.isExistFile(_path)) {
ListViewPath = _path;
if (new File(_path).canRead()) {
if (!_isInside) {
FileList_LinearLayout_FileListContainer.setVisibility(View.VISIBLE);
FileList_TextView_FileListError.setVisibility(View.GONE);
}
FileListData.clear();
FileUtil.listDir(_path, fileList);
pos = 0;
for(int _repeat19 = 0; _repeat19 < (int)(fileList.size()); _repeat19++) {
{
HashMap<String, Object> _item = new HashMap<>();
_item.put("path", fileList.get((int)(pos)));
FileListData.add((int)pos, _item);
}

pos++;
}
if (_isInside) {
FileList_ListView_FileList.setAdapter(new FileList_ListView_FileListAdapter(FileListData));
}
else {
FileList_ListView_FileList.setAdapter(new FileList_ListView_FileListAdapter(FileListData));
}
}
else {
if (!_isInside) {

FileList_TextView_FileListError.setVisibility(View.VISIBLE);
FileList_TextView_FileListError.setText("Read Access denied.");
}
else {
FileList_LinearLayout_FileListContainer.setVisibility(View.VISIBLE);
FileList_TextView_FileListError.setVisibility(View.GONE);
FileListData.clear();
fileList.clear();
{
HashMap<String, Object> _item = new HashMap<>();
_item.put("error", "Read Access denied : ".concat(ListViewPath));
FileListData.add((int)0, _item);
}

if (_isInside) {
FileList_ListView_FileList.setAdapter(new FileList_ListView_FileListAdapter(FileListData));
}
else {
FileList_ListView_FileList.setAdapter(new FileList_ListView_FileListAdapter(FileListData));
}
}
}
}
else {
ListViewPath = _path;
if (!_isInside) {
FileList_LinearLayout_FileListContainer.setVisibility(View.GONE);
FileList_TextView_FileListError.setVisibility(View.VISIBLE);
FileList_TextView_FileListError.setText("Path doesn't exist.");
}
else {
FileList_LinearLayout_FileListContainer.setVisibility(View.VISIBLE);
FileList_TextView_FileListError.setVisibility(View.GONE);
FileListData.clear();
fileList.clear();
{
HashMap<String, Object> _item = new HashMap<>();
_item.put("error", "Path doesn't exist :".concat(ListViewPath));
FileListData.add((int)0, _item);
}

if (_isInside) {
FileList_ListView_FileList.setAdapter(new FileList_ListView_FileListAdapter(FileListData));
}
else {
FileList_ListView_FileList.setAdapter(new FileList_ListView_FileListAdapter(FileListData));
}
}
}
*/
	}
	
	
	public void _custom_smart_toast(final String _text_message, final String _colour, final double _round, final String _colours) {
		LayoutInflater inflater = getLayoutInflater();
		View toastLayout = inflater.inflate(R.layout.toast, null);
		
		TextView textview1 = (TextView) toastLayout.findViewById(R.id.textview1);
		textview1.setText(_text_message);
		LinearLayout linear1 = (LinearLayout) toastLayout.findViewById(R.id.linear1);
		
		Toast toast = new Toast(getApplicationContext()); toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastLayout);
		toast.show();
		
		 android.graphics.drawable.GradientDrawable HEIFBEG = new android.graphics.drawable.GradientDrawable();
		HEIFBEG.setColor(Color.parseColor(_colour));
		HEIFBEG.setCornerRadius((int)_round);
		HEIFBEG.setStroke(5, Color.parseColor(_colours));
		linear1.setBackground(HEIFBEG);
	}
	
	
	public void _UpdateWithPath() {
		
		TabsDataOfFile.clear();
		if (Tabs.contains(path)) {
			if (!Tabs.getString(path, "").equals("")) {
				TabsDataOfFile = new Gson().fromJson(Tabs.getString(path, ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			}
		}
		//
		pos2 = 0;
		for(int _repeat36 = 0; _repeat36 < (int)(TabsDataOfFile.size()); _repeat36++) {
			if (TabsDataOfFile.get((int)pos2).containsKey("path")) {
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.tab, null);
				
				final LinearLayout ll = (LinearLayout)layout.findViewById(R.id.linear1);
				TextView clv2 = (TextView)layout.findViewById(R.id.textview1);
				ImageView clear = (ImageView)layout.findViewById(R.id.clear);
				if(ll.getParent() != null){
						((LinearLayout)ll.getParent()).removeView(ll);
				}
				final String pathForClear = TabsDataOfFile.get((int)pos2).get("path").toString();
				CodeEditorSection_LinearLayout_FileTab.addView(ll);
				clear.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
								ll2 = (LinearLayout)ll;
								CodeEditorSection_LinearLayout_FileTab.removeView(ll2);
								_removePathFromTab(pathForClear);
						        _closeInEditor(pathForClear);
						}
				});
				ll.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
								_OpenInEditor(pathForClear);
						}
				});
				
				clv2.setText(Uri.parse(TabsDataOfFile.get((int)pos2).get("path").toString()).getLastPathSegment());
			}
			pos2++;
		}
	}
	
	
	public boolean _canOpenInEditor(final String _path) {
		canOpen = false;
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
	
	
	public void _OpenFile(final String _path, final String _filename) {
		File path = new File(_path);
		Uri uri = FileProvider.getUriForFile(this, "com.tsd.html.website.creator.FilemanagerActivity", path);
		String mime = getContentResolver().getType(uri);
		
		// Open file with user selected app
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, mime);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		startActivity(intent);
	}
	
	
	public void _AddPathToTab(final String _path) {
		TabsDataOfFile.clear();
		if (Tabs.contains(path)) {
			if (!Tabs.getString(path, "").equals("")) {
				TabsDataOfFile = new Gson().fromJson(Tabs.getString(path, ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			}
		}
		checkIfAlreadyHave = false;
		pos2 = 0;
		for(int _repeat46 = 0; _repeat46 < (int)(TabsDataOfFile.size()); _repeat46++) {
			if (TabsDataOfFile.get((int)pos2).containsKey("path")) {
				if (CurrentPath.equals(TabsDataOfFile.get((int)pos2).get("path").toString())) {
					checkIfAlreadyHave = true;
				}
			}
			pos2++;
		}
		if (!checkIfAlreadyHave) {
			{
				HashMap<String, Object> _item = new HashMap<>();
				_item.put("path", _path);
				TabsDataOfFile.add((int)0, _item);
			}
			
		}
		Tabs.edit().putString(path, new Gson().toJson(TabsDataOfFile)).commit();
		//
		if (!checkIfAlreadyHave) {
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.tab, null);
			
			final LinearLayout ll = (LinearLayout)layout.findViewById(R.id.linear1);
			TextView clv2 = (TextView)layout.findViewById(R.id.textview1);
			ImageView clear = (ImageView)layout.findViewById(R.id.clear);
			if(ll.getParent() != null){
					((LinearLayout)ll.getParent()).removeView(ll);
			}
			CodeEditorSection_LinearLayout_FileTab.addView(ll,0);
			clear.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
					        ll2 = (LinearLayout)ll;
							CodeEditorSection_LinearLayout_FileTab.removeView(ll2);
					        _removePathFromTab(_path);
					        _closeInEditor(_path);
					}
			});
			ll.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
							_OpenInEditor(_path);
					}
			});
			
			clv2.setText(Uri.parse(_path).getLastPathSegment());
			_OpenInEditor(_path);
		}
	}
	
	
	public void _removePathFromTab(final String _path) {
		TabsDataOfFile.clear();
		if (Tabs.contains(path)) {
			if (!Tabs.getString(path, "").equals("")) {
				TabsDataOfFile = new Gson().fromJson(Tabs.getString(path, ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			}
		}
		pos2 = 0;
		for(int _repeat118 = 0; _repeat118 < (int)(TabsDataOfFile.size()); _repeat118++) {
			if (TabsDataOfFile.get((int)pos2).containsKey("path")) {
				if (_path.equals(TabsDataOfFile.get((int)pos2).get("path").toString())) {
					TabsDataOfFile.remove((int)(pos2));
				}
			}
			pos2++;
		}
		Tabs.edit().putString(path, new Gson().toJson(TabsDataOfFile)).commit();
		//
		_closeInEditor(_path);
	}
	
	
	public String _ChooseEditor() {
		String s = "";
		if (setting.contains("editor")) {
			if (setting.getString("editor", "").equals("Ace Editor")) {
				s = setting.getString("editor", "");
			}
			if (setting.getString("editor", "").equals("Sora Editor")) {
				s = setting.getString("editor", "");
			}
		}
		else {
			setting.edit().putString("editor", "Sora Editor").commit();
			s = setting.getString("editor", "");
		}
		return (s);
	}
	
	
	public void _OpenInEditor(final String _path) {
		/*
// apply font
Typeface font = Typeface.createFromAsset(getAssets(), "fonts/jetbrains.ttf");

// apply theme
// theme used: MaterialLight, extracted from CodeStudio
String theme = "MaterialLight.tmTheme";

// apply template preview text
editor.setText(JavaTemplate.getJavaTemplate());

// apply editor settings
editor.setTypefaceText(font);
editor.setTypefaceLineNumber(font);
editor.setTabWidth(4);
editor.setScrollBarSize(1);
editor.setLigatureEnabled(true);
editor.showSoftInput();
editor.setCursorWidth(4);
editor.setTextSize(18);
editor.setPinLineNumber(true);
editor.setHighlightBracketPair(true);
editor.setNonPrintablePaintingFlags(
CodeEditor.FLAG_DRAW_LINE_SEPARATOR |
CodeEditor.FLAG_DRAW_WHITESPACE_LEADING |
CodeEditor.FLAG_DRAW_WHITESPACE_IN_SELECTION
);

// apply textmate theme and language
// sample language: HTML
try {
	EditorColorScheme colorScheme = editor.getColorScheme();
	if (!(colorScheme instanceof TextMateColorScheme)) {
		IRawTheme iRawTheme = ThemeReader.readThemeSync(theme, getAssets().open("textmate/" + theme));
		colorScheme = TextMateColorScheme.create(iRawTheme);
		editor.setColorScheme(colorScheme);
	}
	Language language = TextMateLanguage.create("java.tmLanguage.json",
	getAssets().open("textmate/java/syntaxes/java.tmLanguage.json"),
	new InputStreamReader(getAssets().open("textmate/java/language-configuration.json")),
	((TextMateColorScheme) colorScheme).getRawTheme());
	
	editor.setEditorLanguage(language);
	editor.rerunAnalysis();
	
} catch (Exception e) {
	e.printStackTrace();
}

*/
		try{
			if (_ChooseEditor().equals("Ace Editor")) {
				if (LastUsedAceEditor != null) {
					LastUsedAceEditor.setVisibility(View.GONE);
				}
				if (LastUsedSoraCodeEditor != null) {
					LastUsedSoraCodeEditor.setVisibility(View.GONE);
				}
				Editor = "Ace Editor";
				EditorPath = _path;
				System.out.println("Using Ace Editor");
				LayoutInflater inflater = getLayoutInflater();
				View layout;
				WebView ace_editor;
				layout = inflater.inflate(R.layout.ace_code_editor, null);
				ace_editor = (WebView)layout.findViewById(R.id.CodeEditor);
				if(ace_editor.getParent() != null){
					    ((LinearLayout)ace_editor.getParent()).removeView(ace_editor);
				}
				editor.addView(ace_editor);
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("editor", LastUsedAceEditor);
					EditorSession.add((int)EditorSessionNumber, _item);
				}
				
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("path", _path);
					EditorSession.add((int)EditorSessionNumber, _item);
				}
				
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("editor_type", Editor);
					EditorSession.add((int)EditorSessionNumber, _item);
				}
				
				EditorSessionNumber++;
				LastUsedAceEditor = ace_editor;
			}
			else if (_ChooseEditor().equals("Sora Editor")){
				if (LastUsedAceEditor != null) {
					LastUsedAceEditor.setVisibility(View.GONE);
					System.out.println("Gone Last editor");
				}
				if (LastUsedSoraCodeEditor != null) {
					LastUsedSoraCodeEditor.setVisibility(View.GONE);
					System.out.println("Gone Last editor");
				}
				Editor = "Sora Editor";
				EditorPath = _path;
				System.out.println("Using Sora editor");
				boolean isEditorExists = false;
				pos2 = 0;
				isEditorExists = false;
				for(int _repeat127 = 0; _repeat127 < (int)(EditorSession.size()); _repeat127++) {
					if (EditorSession.get((int)pos2).containsKey("editor") && EditorSession.get((int)pos2).containsKey("path")) {
						if (_path.equals(EditorSession.get((int)pos2).get("path").toString())) {
							isEditorExists = true;
						}
					}
					pos2++;
				}
				if (isEditorExists) {
					System.out.println("Editor Exists");
					pos2 = 0;
					for(int _repeat143 = 0; _repeat143 < (int)(EditorSession.size()); _repeat143++) {
						if (EditorSession.get((int)pos2).containsKey("editor") && EditorSession.get((int)pos2).containsKey("path")) {
							if (_path.equals(EditorSession.get((int)pos2).get("path").toString())) {
								((CodeEditor)EditorSession.get((int)pos2).get("editor")).setVisibility(View.VISIBLE);
								System.out.println("Old Visible editor");
							}
						}
						pos2++;
					}
				}
				else {
					System.out.println("Editor does not exists");
					final CodeEditor sora_editor = (CodeEditor)getLayoutInflater().inflate(R.layout.sora_code_editor,null).findViewById(R.id.CodeEditor);
					String theme = "MaterialLight.tmTheme";
					
					sora_editor.setTypefaceText(Typeface.createFromAsset(getAssets(), "Editor/Sora-editor/fonts/jetbrains.ttf"));
					sora_editor.setTypefaceLineNumber(Typeface.createFromAsset(getAssets(), "Editor/Sora-editor/fonts/jetbrains.ttf"));
					//sora_editor.setTextSize(14);
					sora_editor.setEditable(true);
					//sora_editor.setColorScheme();
					sora_editor.setCursorWidth(4);
					sora_editor.setPinLineNumber(true);
					//sora_editor.setHighlightBracketPair(true);
					sora_editor.setLigatureEnabled(true);
					sora_editor.showSoftInput();
					if (_fileType(_path).equals("Java")) {
						try {
								EditorColorScheme colorScheme = sora_editor.getColorScheme();
								if (!(colorScheme instanceof TextMateColorScheme)) {
										IRawTheme iRawTheme = ThemeReader.readThemeSync(theme, getAssets().open("Editor/Sora-editor/textmate/" + theme));
										colorScheme = TextMateColorScheme.create(iRawTheme);
										sora_editor.setColorScheme(colorScheme);
								}
								Language language = TextMateLanguage.create("java.tmLanguage.json",
								getAssets().open("Editor/Sora-editor/textmate/java/syntaxes/java.tmLanguage.json"),
								new InputStreamReader(getAssets().open("Editor/Sora-editor/textmate/java/language-configuration.json")),
								((TextMateColorScheme) colorScheme).getRawTheme());
								
								sora_editor.setEditorLanguage(language);
								sora_editor.rerunAnalysis();
								
						} catch (Exception e) {
								e.printStackTrace();
						}
						
					}
					if (new File(_path).canRead()) {
						sora_editor.setText(FileUtil.readFile(_path));
					}
					else {
						sora_editor.setText("Error while opening file.");
					}
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("editor", sora_editor);
						EditorSession.add((int)EditorSessionNumber, _item);
					}
					
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("path", _path);
						EditorSession.add((int)EditorSessionNumber, _item);
					}
					
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("editor_type", "Sora Editor");
						EditorSession.add((int)EditorSessionNumber, _item);
					}
					
					EditorSessionNumber++;
					if (((LinearLayout)sora_editor.getParent()) != null) {
						((LinearLayout)sora_editor.getParent()).removeView(sora_editor);
					}
					editor.addView(sora_editor);
					LastUsedSoraCodeEditor = sora_editor;
				}
			}
		}catch(Exception e){
			SketchwareUtil.showMessage(getApplicationContext(), e.toString());
		}
	}
	
	
	public void _closeInEditor(final String _path) {
		pos2 = 0;
		for(int _repeat11 = 0; _repeat11 < (int)(EditorSession.size()); _repeat11++) {
			if (EditorSession.get((int)pos2).containsKey("editor") && EditorSession.get((int)pos2).containsKey("path")) {
				if (EditorSession.get((int)pos2).get("editor_type").toString().equals("Sora Editor")) {
					((LinearLayout)((CodeEditor)EditorSession.get((int)pos2).get("editor")).getParent()).removeView(((CodeEditor)EditorSession.get((int)pos2).get("editor")));
					EditorSession.remove((int)(pos2));
					EditorSessionNumber--;
				}
				else if (EditorSession.get((int)pos2).get("editor_type").toString().equals("Ace Editor")){
					((LinearLayout)((WebView)EditorSession.get((int)pos2).get("editor")).getParent()).removeView(((WebView)EditorSession.get((int)pos2).get("editor")));
					EditorSession.remove((int)(pos2));
					EditorSessionNumber--;
				}
			}
			pos2++;
		}
	}
	
	
	public String _fileType(final String _path) {
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
	
	
	public void _ShowList(final String _path, final View _view) {
		((LinearLayout)_view).removeAllViews();
		FileUtil.listDir(_path, fileList);
		try{
			JSONArray FileTree = new JSONArray(new Gson().toJson(fileList));
			for(int _repeat18 = 0; _repeat18 < (int)(FileTree.length()); _repeat18++) {
				final int position = _repeat18;
				FileTreeInflater = getLayoutInflater().inflate(R.layout.file_tree, null);
				LinearLayout Layout = ((LinearLayout)FileTreeInflater.findViewById(R.id.linear5));
				LinearLayout Layout2 = ((LinearLayout)FileTreeInflater.findViewById(R.id.linear2));
				LinearLayout LayoutToInflateChild = ((LinearLayout)FileTreeInflater.findViewById(R.id.linear3));
				ImageView ShowMoreOrLess = ((ImageView)FileTreeInflater.findViewById(R.id.imageview1));
				ImageView FileType = ((ImageView)FileTreeInflater.findViewById(R.id.imageview2));
				TextView FileName = ((TextView)FileTreeInflater.findViewById(R.id.textview1));
				LayoutToInflateChild.setVisibility(View.GONE);
				if (FileUtil.isDirectory(FileTree.getString(position))) {
					FileType.setImageResource(R.drawable.ic_folder_black);
					ShowMoreOrLess.setImageResource(R.drawable.ic_arrow_drop_down_black);
					final String childPath = FileTree.getString(_repeat18);
					ShowMoreOrLess.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View _view) {
							if (LayoutToInflateChild.getVisibility() == View.GONE) {
								LayoutToInflateChild.removeAllViews();
								_ShowList(childPath, LayoutToInflateChild);
								LayoutToInflateChild.setVisibility(View.VISIBLE);
								ShowMoreOrLess.setImageResource(R.drawable.ic_arrow_drop_up_black);
							}
							else {
								LayoutToInflateChild.setVisibility(View.GONE);
								LayoutToInflateChild.removeAllViews();
								ShowMoreOrLess.setImageResource(R.drawable.ic_arrow_drop_down_black);
							}
							}
					});
				}
				else {
					FileType.setImageResource(R.drawable.outline_description_black_36);
					ShowMoreOrLess.setVisibility(View.GONE);
				}
				FileName.setText(Uri.parse(FileTree.getString(position)).getLastPathSegment());
				Layout2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
						try{
							if (_canOpenInEditor(FileTree.getString(position))) {
								CurrentPath = FileTree.getString(position);
								_AddPathToTab(CurrentPath);
							}
							else {
								_OpenFile(FileTree.getString(position), FileTree.getString(position));
							}
						}
						catch(JSONException e){
							SketchwareUtil.showMessage(getApplicationContext(), e.getMessage());
						}
						}
				});
				if (Layout != null) {
					((LinearLayout)Layout.getParent()).removeView(Layout);
				}
				((LinearLayout)_view).addView(Layout);
			}
		}
		catch(JSONException e){
			SketchwareUtil.showMessage(getApplicationContext(), e.getMessage());
		}
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}