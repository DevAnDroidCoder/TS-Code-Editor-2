package com.ts.code.editor;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
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
import android.content.pm.PackageInfo;
import androidx.core.content.FileProvider;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import org.eclipse.tm4e.core.theme.IRawTheme;
import org.eclipse.tm4e.core.internal.theme.reader.ThemeReader;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme;
import io.github.rosemoe.sora.langs.java.JavaLanguage;
import java.io.InputStreamReader;
import com.ts.code.editor.editor.Editor;
import com.ts.code.editor.editor.SoraEditor;

public class CodeEditorActivity extends AppCompatActivity {
	
	private String path = "";
	private boolean isRequested = false;
	public CodeEditor LastUsedSoraCodeEditor;
	private HashMap<String, Object> EditorSessionAddMap = new HashMap<>();
	
	private ArrayList<String> FileList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> EditorSession = new ArrayList<>();
	
	private LinearLayout LinearLayout_Main;
	private LinearLayout LinearLayout_Main_EditorSection;
	private LinearLayout LinearLayout_Main_FileSection;
	private HorizontalScrollView ScrollViewH_EditorSection_Toolbar;
	private HorizontalScrollView ScrollViewH_EditorSection_FileTab;
	private LinearLayout editor;
	private LinearLayout LinearLayout_Toolbar_Toolbar;
	private TextView textview2;
	private TextView textview3;
	private LinearLayout Linear_FileTab_FileTab;
	private ScrollView ScrollViewH_FileSection_FileTree;
	private HorizontalScrollView ScrollViewV_FileSection_FileTreeError;
	private HorizontalScrollView ScrollViewV_FileTree_FileTree;
	private LinearLayout LinearLayout_FileTree_FileTree;
	private LinearLayout LinearLayout_FileTreeError_FileTreeError;
	private TextView TextView_FileTreeError_FileTreeError;
	
	private MaterialAlertDialogBuilder MaterialDialog;
	private View FileTreeInflater;
	private PopupWindow EditorChooserPopupWindow;
	private View EditorChooser;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.code_editor);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		LinearLayout_Main = findViewById(R.id.LinearLayout_Main);
		LinearLayout_Main_EditorSection = findViewById(R.id.LinearLayout_Main_EditorSection);
		LinearLayout_Main_FileSection = findViewById(R.id.LinearLayout_Main_FileSection);
		ScrollViewH_EditorSection_Toolbar = findViewById(R.id.ScrollViewH_EditorSection_Toolbar);
		ScrollViewH_EditorSection_FileTab = findViewById(R.id.ScrollViewH_EditorSection_FileTab);
		editor = findViewById(R.id.editor);
		LinearLayout_Toolbar_Toolbar = findViewById(R.id.LinearLayout_Toolbar_Toolbar);
		textview2 = findViewById(R.id.textview2);
		textview3 = findViewById(R.id.textview3);
		Linear_FileTab_FileTab = findViewById(R.id.Linear_FileTab_FileTab);
		ScrollViewH_FileSection_FileTree = findViewById(R.id.ScrollViewH_FileSection_FileTree);
		ScrollViewV_FileSection_FileTreeError = findViewById(R.id.ScrollViewV_FileSection_FileTreeError);
		ScrollViewV_FileTree_FileTree = findViewById(R.id.ScrollViewV_FileTree_FileTree);
		LinearLayout_FileTree_FileTree = findViewById(R.id.LinearLayout_FileTree_FileTree);
		LinearLayout_FileTreeError_FileTreeError = findViewById(R.id.LinearLayout_FileTreeError_FileTreeError);
		TextView_FileTreeError_FileTreeError = findViewById(R.id.TextView_FileTreeError_FileTreeError);
		MaterialDialog = new MaterialAlertDialogBuilder(this);
	}
	
	private void initializeLogic() {
		_UI();
		if (getIntent().hasExtra("path")) {
			setTitle(Uri.parse(getIntent().getStringExtra("path")).getLastPathSegment());
			path = getIntent().getStringExtra("path");
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			
			MaterialDialog.setTitle("Storage Permission required");
			MaterialDialog.setMessage("Storage permission is required please allow app to use storage in next page.");
			MaterialDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					if (shouldShowRequestPermissionRationale("Manifest.permission.READ_EXTERNAL_STORAGE")) {
						MaterialDialog.setTitle("Storage permission required");
						MaterialDialog.setMessage("Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app.");
						MaterialDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								isRequested = true;
								Intent intent = new Intent();
								intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", CodeEditorActivity.this.getPackageName(),null);
								intent.setData(uri);
								CodeEditorActivity.this.startActivity(intent);
							}
						});
						MaterialDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								finishAffinity();
							}
						});
						MaterialDialog.create().show();
					}
					else {
						_requestStoragePermission();
					}
				}
			});
			MaterialDialog.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					_showRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app");
				}
			});
			MaterialDialog.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					finishAffinity();
				}
			});
			MaterialDialog.create().show();
		} else {
				startManually();
		}
	}
	
	public void startManually(){
		if (new File(path).canRead()) {
			ScrollViewH_FileSection_FileTree.setVisibility(View.VISIBLE);
			ScrollViewV_FileSection_FileTreeError.setVisibility(View.GONE);
			_FileTree(path, LinearLayout_FileTree_FileTree);
		}
		else {
			ScrollViewH_FileSection_FileTree.setVisibility(View.GONE);
			ScrollViewV_FileSection_FileTreeError.setVisibility(View.VISIBLE);
			TextView_FileTreeError_FileTreeError.setText("Read access denied.");
		}
	}
	
	public void _UI() {
		// getSupportActionBar().hide();
	}
	
	
	public void _FileTree(final String _path, final View _view) {
		((LinearLayout)_view).removeAllViews();
		FileUtil.listDir(_path, FileList);
		try{
			JSONArray FileTree = new JSONArray(new Gson().toJson(FileList));
			for(int _repeat19 = 0; _repeat19 < (int)(FileTree.length()); _repeat19++) {
				final int position = _repeat19;
				FileTreeInflater = getLayoutInflater().inflate(R.layout.file_tree, null);
				final LinearLayout Layout = ((LinearLayout)FileTreeInflater.findViewById(R.id.linear5));
				final LinearLayout Layout2 = ((LinearLayout)FileTreeInflater.findViewById(R.id.linear2));
				final LinearLayout LayoutToInflateChild = ((LinearLayout)FileTreeInflater.findViewById(R.id.linear3));
				final ImageView ShowMoreOrLess = ((ImageView)FileTreeInflater.findViewById(R.id.imageview1));
				final ImageView FileType = ((ImageView)FileTreeInflater.findViewById(R.id.imageview2));
				final TextView FileName = ((TextView)FileTreeInflater.findViewById(R.id.textview1));
				LayoutToInflateChild.setVisibility(View.GONE);
				if (FileUtil.isDirectory(FileTree.getString(position))) {
					FileType.setImageResource(R.drawable.ic_folder_black);
					ShowMoreOrLess.setImageResource(R.drawable.ic_arrow_drop_down_black);
					final String childPath = FileTree.getString(position);
					ShowMoreOrLess.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View _view) {
							if (LayoutToInflateChild.getVisibility() == View.GONE) {
								LayoutToInflateChild.removeAllViews();
								_FileTree(childPath, LayoutToInflateChild);
								LayoutToInflateChild.setVisibility(View.VISIBLE);
								ShowMoreOrLess.setImageResource(R.drawable.ic_arrow_drop_up_black);
							}
							else {
								LayoutToInflateChild.removeAllViews();
								LayoutToInflateChild.setVisibility(View.GONE);
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
							final Editor MEditor = new Editor();
							if (MEditor.CanOpenInEditor(FileTree.getString(position))) {
								final String childPath = FileTree.getString(position);
								EditorChooser = getLayoutInflater().inflate(R.layout.menu_codeeditor, null);
								EditorChooserPopupWindow = new PopupWindow(EditorChooser, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
								((LinearLayout)EditorChooser.findViewById(R.id.Option_Ace_Editor)).setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View _view) {
										SketchwareUtil.showMessage(getApplicationContext(), "Working on it.");
										}
								});
								((LinearLayout)EditorChooser.findViewById(R.id.Option_Sora_Editor)).setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View _view) {
										final SoraEditor soraCodeEditor = new SoraEditor();
										soraCodeEditor.SetUpSoraCodeEditor(editor,childPath,CodeEditorActivity.this);
										LastUsedSoraCodeEditor =  soraCodeEditor.GetSoraEditor();
										EditorSessionAddMap = new HashMap<>();
										EditorSessionAddMap.put("editor", LastUsedSoraCodeEditor);
										EditorSessionAddMap.put("path", _path);
										EditorSessionAddMap.put("editor_type", Editor.SORA_EDITOR);
										EditorSession.add(EditorSessionAddMap);
										EditorSessionAddMap.clear();
										}
								});
								EditorChooserPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
								EditorChooserPopupWindow.showAsDropDown(Layout2, 0, 0);
							}
							else {
								_OpenFile(FileTree.getString(position));
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
	
	
	public void _showRationale(final String _permission, final String _text) {
		if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(_permission)) {
			MaterialDialog.setTitle("Storage Permission required");
			MaterialDialog.setMessage(_text);
			MaterialDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					if (ContextCompat.checkSelfPermission(CodeEditorActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
					|| ContextCompat.checkSelfPermission(CodeEditorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
							ActivityCompat.requestPermissions(CodeEditorActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
					} else {
							MainActivity m = new MainActivity();
							m.startManually();
					}
					
				}
			});
			MaterialDialog.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					_showRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app");
				}
			});
			MaterialDialog.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					finishAffinity();
				}
			});
			MaterialDialog.create().show();
		}
	}
	
	
	public void _OpenFile(final String _path) {
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
	
	
	public void _requestStoragePermission() {
		ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
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