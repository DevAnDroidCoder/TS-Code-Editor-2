package com.ts.code.editor;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.DialogInterface;
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
import android.util.TypedValue;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageInfo;
import androidx.core.content.FileProvider;
import com.google.android.material.textview.MaterialTextView;

public class FilemanagerActivity extends AppCompatActivity {
	
	private FloatingActionButton _fab;
	private String primaryColor = "";
	private boolean isRequestedForPermission = false;
	private boolean isDarkTheme = false;
	private boolean IsForProject = false;
	private boolean isForFolder = false;
	private boolean isForFile = false;
	private String path = "";
	private double pos = 0;
	private String RequestedForResult = "";
	private boolean isInsideProject = false;
	private HashMap<String, Object> isProject = new HashMap<>();
	private boolean isForResult = false;
	private String secondaryColor = "";
	private String colorOnPrimary = "";
	private String colorOnSecondary = "";
	private double color = 0;
	
	private ArrayList<String> fileList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> FileListData = new ArrayList<>();
	
	private LinearLayout Main_LinearLayout;
	private LinearLayout Error_LinearLayout;
	private LinearLayout Main_LinearLayout_Listview1_Backdrop;
	private Button Main_Button_UseThisFolder;
	private LinearLayout linear1;
	private ListView Listview1_Backdrop_LinearLayout_Listview1;
	private ImageView imageview1;
	private TextView Error_TextView_AccessDenied;
	
	private SharedPreferences theme;
	private AlertDialog.Builder AppPermissions;
	private Intent Activities = new Intent();
	private TypedValue TypedValue;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.filemanager);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_fab = findViewById(R.id._fab);
		
		Main_LinearLayout = findViewById(R.id.Main_LinearLayout);
		Error_LinearLayout = findViewById(R.id.Error_LinearLayout);
		Main_LinearLayout_Listview1_Backdrop = findViewById(R.id.Main_LinearLayout_Listview1_Backdrop);
		Main_Button_UseThisFolder = findViewById(R.id.Main_Button_UseThisFolder);
		linear1 = findViewById(R.id.linear1);
		Listview1_Backdrop_LinearLayout_Listview1 = findViewById(R.id.Listview1_Backdrop_LinearLayout_Listview1);
		imageview1 = findViewById(R.id.imageview1);
		Error_TextView_AccessDenied = findViewById(R.id.Error_TextView_AccessDenied);
		theme = getSharedPreferences("theme", Activity.MODE_PRIVATE);
		AppPermissions = new AlertDialog.Builder(this);
		TypedValue TypedValue = new TypedValue();
		
		Main_Button_UseThisFolder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (isForResult) {
					if (isForFolder) {
						Activities.putExtra("path", path);
						setResult(RESULT_OK, Activities);
						finish();
					}
					else {
						_custom_smart_toast("Folder request could not be returned", primaryColor, 10, primaryColor);
					}
				}
				else {
					_custom_smart_toast("Folder request could not be returned", primaryColor, 10, primaryColor);
				}
			}
		});
		
		imageview1.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View _view) {
				
				return true;
			}
		});
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Activities.setClass(getApplicationContext(), SettingActivity.class);
				startActivity(Activities);
			}
		});
	}
	
	private void initializeLogic() {
		_UI();
		Main_LinearLayout.setVisibility(View.GONE);
		Error_LinearLayout.setVisibility(View.VISIBLE);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				Main_LinearLayout.setVisibility(View.GONE);
				Error_LinearLayout.setVisibility(View.VISIBLE);
				ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
		} else {
				startManually();
		}
		
		//Import JSON
		/*

*/
	}
	
	public void startManually(){
		if (getIntent().hasExtra("path")) {
			_FileList(getIntent().getStringExtra("path"));
		}
		else {
			_FileList(FileUtil.getExternalStorageDir());
		}
		isForResult = false;
		IsForProject = false;
		isForFolder = false;
		isForFile = false;
		isInsideProject = false;
		if (getIntent().hasExtra("for")) {
			if (!getIntent().getStringExtra("for").equals("result")) {
				isForResult = true;
			}
			else {
				isForResult = false;
			}
		}
		if (getIntent().hasExtra("type")) {
			if (getIntent().getStringExtra("type").equals("project")) {
				IsForProject = true;
				if (getIntent().hasExtra("isInsideProject")) {
					if (getIntent().getStringExtra("isInsideProject").equals("true")) {
						isInsideProject = true;
					}
					else {
						isInsideProject = false;
					}
				}
			}
			if (getIntent().getStringExtra("type").equals("folder")) {
				isForFolder = true;
			}
			if (getIntent().getStringExtra("type").equals("file")) {
				isForFile = true;
				if (getIntent().hasExtra("fileType")) {
					if (getIntent().getStringExtra("fileType").equals("HTML")) {
						
					}
					if (getIntent().getStringExtra("fileType").equals("CSS")) {
						
					}
					if (getIntent().getStringExtra("fileType").equals("JavaScript")) {
						
					}
					if (getIntent().getStringExtra("fileType").equals("XML")) {
						
					}
					if (getIntent().getStringExtra("fileType").equals("Java")) {
						
					}
				}
			}
		}
		Main_Button_UseThisFolder.setVisibility(View.GONE);
		if (isForResult) {
			if (IsForProject) {
				
			}
			else {
				if (isForFolder) {
					Main_Button_UseThisFolder.setVisibility(View.VISIBLE);
				}
				else {
					if (isForFile) {
						
					}
					else {
						
					}
				}
			}
		}
		else {
			if (IsForProject) {
				
			}
			else {
				if (isForFolder) {
					Main_Button_UseThisFolder.setVisibility(View.VISIBLE);
				}
				else {
					if (isForFile) {
						
					}
					else {
						
					}
				}
			}
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int _requestCode, String[] _permissions, int[] _grantResults) {
		for(int _repeat10 = 0; _repeat10 < (int)(_permissions.length); _repeat10++) {
			String Permission = _permissions[_repeat10];
			if (_grantResults[_repeat10] == PackageManager.PERMISSION_DENIED) {
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
						AppPermissions.setTitle("Storage permission required");
						AppPermissions.setMessage("Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app");
						AppPermissions.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								isRequestedForPermission = true;
								Intent intent = new Intent();
								intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", FilemanagerActivity.this.getPackageName(),null);
								intent.setData(uri);
								FilemanagerActivity.this.startActivity(intent);
							}
						});
						AppPermissions.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								finishAffinity();
							}
						});
						AppPermissions.create().show();
					}
					//setting
					if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(Permission)) {
						AppPermissions.setTitle("Storage permission required");
						AppPermissions.setMessage("Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app");
						AppPermissions.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								isRequestedForPermission = true;
								Intent intent = new Intent();
								intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", FilemanagerActivity.this.getPackageName(),null);
								intent.setData(uri);
								FilemanagerActivity.this.startActivity(intent);
							}
						});
						AppPermissions.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								finishAffinity();
							}
						});
						AppPermissions.create().show();
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
		if (isRequestedForPermission) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
			|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
					Main_LinearLayout.setVisibility(View.GONE);
					Error_LinearLayout.setVisibility(View.VISIBLE);
					ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
			} else {
					startManually();
			}
			
		}
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				Main_LinearLayout.setVisibility(View.GONE);
				Error_LinearLayout.setVisibility(View.VISIBLE);
		}
		
	}
	
	@Override
	public void onBackPressed() {
		if (isForResult) {
			if (isForFolder) {
				Activities.putExtra("path", path);
				setResult(RESULT_CANCELED, Activities);
				finish();
			}
			else {
				finish();
			}
		}
		else {
			finish();
		}
	}
	public void _UI() {
		/*
.initialize(theme,FilemanagerActivity.this);
.getCurrentTSDThemeData();
isDarkTheme = .isDarkTheme;
primaryColor = .PrimaryColor;
secondaryColor = .SecondaryColor;
colorOnPrimary = .ColorOnPrimary;
colorOnSecondary = .ColorOnSecondary;
_setBackground(secondaryColor, Main_LinearLayout);
_rippleRoundStroke(Main_Button_UseThisFolder, primaryColor, colorOnPrimary, 10, 0, primaryColor);
_setTextColor(Main_Button_UseThisFolder, colorOnPrimary);
_setBackground(colorOnSecondary, Error_LinearLayout);
_setTextColor(Error_TextView_AccessDenied, colorOnSecondary);
*/
		_StatusAndNavigation(TSDTheme.getCurrentMaterialThemeColor(R.attr.colorPrimary,FilemanagerActivity.this));
		_RippleEffects(TSDTheme.getCurrentMaterialThemeColor(R.attr.colorPrimary,FilemanagerActivity.this), imageview1);
	}
	
	
	public void _StatusAndNavigation(final String _color) {
		//Navigation
		//modified by ashishtechnozone
		if (Build.VERSION.SDK_INT >= 21) { Window w = this.getWindow(); w.setNavigationBarColor(Color.parseColor(_color)); }
		
		
		//stutus bar
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
			Window w =this.getWindow();
			w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(Color.parseColor(_color));
		}
	}
	
	
	public void _rippleRoundStroke(final View _view, final String _focus, final String _pressed, final double _round, final double _stroke, final String _strokeclr) {
		android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
		GG.setColor(Color.parseColor(_focus));
		GG.setCornerRadius((float)_round);
		GG.setStroke((int) _stroke,
		Color.parseColor("#" + _strokeclr.replace("#", "")));
		android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_pressed)}), GG, null);
		_view.setBackground(RE);
	}
	
	
	public void _setTextColor(final TextView _view, final String _color) {
		_view.setTextColor(Color.parseColor(_color));
	}
	
	
	public void _showRationale(final String _permission, final String _text) {
		if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(_permission)) {
			AppPermissions.setTitle("Storage Permission required");
			AppPermissions.setMessage(_text);
			AppPermissions.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					if (ContextCompat.checkSelfPermission(FilemanagerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
					|| ContextCompat.checkSelfPermission(FilemanagerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
							ActivityCompat.requestPermissions(FilemanagerActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
					} else {
							FilemanagerActivity m = new FilemanagerActivity();
							m.startManually();
					}
					
				}
			});
			AppPermissions.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					finishAffinity();
				}
			});
			AppPermissions.create().show();
		}
	}
	
	
	public void _setBackground(final String _color, final View _view) {
		_view.setBackgroundColor(Color.parseColor(_color));
	}
	
	
	public void _FileList(final String _path) {
		if (new File(_path).canRead()) {
			Main_LinearLayout.setVisibility(View.VISIBLE);
			Error_LinearLayout.setVisibility(View.GONE);
			FileListData.clear();
			FileUtil.listDir(_path, fileList);
			pos = 0;
			for(int _repeat13 = 0; _repeat13 < (int)(fileList.size()); _repeat13++) {
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("path", fileList.get((int)(pos)));
					FileListData.add((int)pos, _item);
				}
				
				pos++;
			}
			Listview1_Backdrop_LinearLayout_Listview1.setAdapter(new Listview1_Backdrop_LinearLayout_Listview1Adapter(FileListData));
			((BaseAdapter)Listview1_Backdrop_LinearLayout_Listview1.getAdapter()).notifyDataSetChanged();
			path = _path;
		}
		else {
			Main_LinearLayout.setVisibility(View.GONE);
			Error_LinearLayout.setVisibility(View.VISIBLE);
		}
	}
	
	
	public void _ImageFocusEffect(final ImageView _img, final String _c1, final String _c2) {
		_img.setImageTintList(new android.content.res.ColorStateList(new int[][] {{-android.R.attr.state_pressed},{android.R.attr.state_pressed}},new int[]{Color.parseColor(_c1), Color.parseColor(_c2)}));
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
	
	
	public void _custom_smart_toast(final String _text_message, final String _colour, final double _round, final String _colours) {
		LayoutInflater inflater = getLayoutInflater(); View toastLayout = inflater.inflate(R.layout.toast, null);
		
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
	
	
	public void _Install(final String _apk) {
		String PATH = _apk;
		    java.io.File file = new java.io.File(PATH);
		    if(file.exists()) {
				        Intent intent = new Intent(Intent.ACTION_VIEW);
				        intent.setDataAndType(uriFromFile(getApplicationContext(), new java.io.File(PATH)), "application/vnd.android.package-archive");
				        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				        try {
						            getApplicationContext().startActivity(intent);
						        } catch (ActivityNotFoundException e) {
						            e.printStackTrace();
						            Log.e("TAG", "Error in opening the file!");
						        }
				    }else{
				        Toast.makeText(getApplicationContext(),"installing",Toast.LENGTH_LONG).show();
				    }
	}
	Uri uriFromFile(Context context, java.io.File file) {
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				        return androidx.core.content.FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName() + ".provider", file); 
				    } else {
				        return Uri.fromFile(file);
				    }
	}
	
	
	public void _MaterialToast(final String _message) {
		
	}
	
	
	public void _RippleEffects(final String _color, final View _view) {
		android.content.res.ColorStateList clr = new android.content.res.ColorStateList(new int[][]{new int[]{}},new int[]{Color.parseColor(_color)});
		android.graphics.drawable.RippleDrawable ripdr = new android.graphics.drawable.RippleDrawable(clr, null, null);
		_view.setBackground(ripdr);
	}
	
	public class Listview1_Backdrop_LinearLayout_Listview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview1_Backdrop_LinearLayout_Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.file_manager, null);
			}
			
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final androidx.cardview.widget.CardView cardview2 = _view.findViewById(R.id.cardview2);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			
			textview1.setText(Uri.parse(_data.get((int)_position).get("path").toString()).getLastPathSegment());
			if (FileUtil.isDirectory(path.concat("/".concat(textview1.getText().toString())))) {
				if (FileUtil.isExistFile(path.concat("/".concat(textview1.getText().toString())).concat("/".concat(".TSDwebsiteCreator.project")))) {
					try{
						isProject = new Gson().fromJson(FileUtil.readFile(path.concat("/".concat(textview1.getText().toString())).concat("/".concat(".TSDwebsiteCreator.project"))), new TypeToken<HashMap<String, Object>>(){}.getType());
						if ("TSDWebsiteCreator".equals(isProject.get("project").toString())) {
							if (isProject.get("path").toString().equals(path.concat("/".concat(textview1.getText().toString())))) {
								cardview2.setRadius((float)360);
								imageview1.setImageResource(R.drawable.logo_black);
								linear1.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View _view) {
										Activities.putExtra("lastPath", getIntent().getStringExtra("lastPath"));
										Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
										Activities.putExtra("isInsideProject", "true");
										Activities.putExtra("type", "project");
										Activities.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
										Activities.setClass(getApplicationContext(), CodeEditorActivity.class);
										startActivity(Activities);
									}
								});
							}
							else {
								cardview2.setRadius((float)0);
								imageview1.setImageResource(R.drawable.ic_folder_black);
								linear2.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View _view) {
										if (getIntent().hasExtra("lastPath")) {
											if (getIntent().hasExtra("type")) {
												Activities.putExtra("lastPath", getIntent().getStringExtra("lastPath"));
												Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
												Activities.putExtra("type", getIntent().getStringExtra("type"));
												if (isInsideProject) {
													Activities.putExtra("isInsideProject", "true");
												}
												Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
												startActivity(Activities);
											}
											else {
												Activities.putExtra("lastPath", getIntent().getStringExtra("lastPath"));
												Activities.putExtra("type", "project");
												Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
												if (isInsideProject) {
													Activities.putExtra("isInsideProject", "true");
												}
												Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
												startActivity(Activities);
											}
										}
										else {
											if (getIntent().hasExtra("type")) {
												Activities.putExtra("lastPath", FileUtil.getExternalStorageDir());
												Activities.putExtra("type", getIntent().getStringExtra("type"));
												Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
												if (isInsideProject) {
													Activities.putExtra("isInsideProject", "true");
												}
												Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
												startActivity(Activities);
											}
											else {
												Activities.putExtra("lastPath", FileUtil.getExternalStorageDir());
												Activities.putExtra("type", "project");
												Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
												if (isInsideProject) {
													Activities.putExtra("isInsideProject", "true");
												}
												Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
												startActivity(Activities);
											}
										}
									}
								});
							}
						}
						else {
							cardview2.setRadius((float)0);
							imageview1.setImageResource(R.drawable.ic_folder_black);
							linear2.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View _view) {
									if (getIntent().hasExtra("lastPath")) {
										if (getIntent().hasExtra("type")) {
											Activities.putExtra("lastPath", getIntent().getStringExtra("lastPath"));
											Activities.putExtra("type", getIntent().getStringExtra("type"));
											Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
											if (isInsideProject) {
												Activities.putExtra("isInsideProject", "true");
											}
											Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
											startActivity(Activities);
										}
										else {
											Activities.putExtra("lastPath", getIntent().getStringExtra("lastPath"));
											Activities.putExtra("type", "project");
											Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
											if (isInsideProject) {
												Activities.putExtra("isInsideProject", "true");
											}
											Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
											startActivity(Activities);
										}
									}
									else {
										if (getIntent().hasExtra("project")) {
											Activities.putExtra("lastPath", FileUtil.getExternalStorageDir());
											Activities.putExtra("type", getIntent().getStringExtra("type"));
											Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
											if (isInsideProject) {
												Activities.putExtra("isInsideProject", "true");
											}
											Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
											startActivity(Activities);
										}
										else {
											Activities.putExtra("lastPath", FileUtil.getExternalStorageDir());
											Activities.putExtra("type", "project");
											Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
											if (isInsideProject) {
												Activities.putExtra("isInsideProject", "true");
											}
											Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
											startActivity(Activities);
										}
									}
								}
							});
						}
					}catch(Exception e){
						cardview2.setRadius((float)0);
						imageview1.setImageResource(R.drawable.ic_folder_black);
						linear2.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View _view) {
								if (getIntent().hasExtra("lastPath")) {
									if (getIntent().hasExtra("type")) {
										Activities.putExtra("lastPath", getIntent().getStringExtra("lastPath"));
										Activities.putExtra("type", getIntent().getStringExtra("type"));
										Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
										if (isInsideProject) {
											Activities.putExtra("isInsideProject", "true");
										}
										Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
										startActivity(Activities);
									}
									else {
										Activities.putExtra("lastPath", getIntent().getStringExtra("lastPath"));
										Activities.putExtra("type", "project");
										Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
										if (isInsideProject) {
											Activities.putExtra("isInsideProject", "true");
										}
										Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
										startActivity(Activities);
									}
								}
								else {
									if (getIntent().hasExtra("project")) {
										Activities.putExtra("lastPath", FileUtil.getExternalStorageDir());
										Activities.putExtra("type", getIntent().getStringExtra("type"));
										Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
										if (isInsideProject) {
											Activities.putExtra("isInsideProject", "true");
										}
										Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
										startActivity(Activities);
									}
									else {
										Activities.putExtra("lastPath", FileUtil.getExternalStorageDir());
										Activities.putExtra("type", "project");
										Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
										if (isInsideProject) {
											Activities.putExtra("isInsideProject", "true");
										}
										Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
										startActivity(Activities);
									}
								}
							}
						});
					}
				}
				else {
					cardview2.setRadius((float)0);
					imageview1.setImageResource(R.drawable.ic_folder_black);
					linear2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
							if (getIntent().hasExtra("lastPath")) {
								if (getIntent().hasExtra("type")) {
									Activities.putExtra("lastPath", getIntent().getStringExtra("lastPath"));
									Activities.putExtra("type", getIntent().getStringExtra("type"));
									Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
									if (isInsideProject) {
										Activities.putExtra("isInsideProject", "true");
									}
									Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
									startActivity(Activities);
								}
								else {
									Activities.putExtra("lastPath", getIntent().getStringExtra("lastPath"));
									Activities.putExtra("type", "project");
									Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
									if (isInsideProject) {
										Activities.putExtra("isInsideProject", "true");
									}
									Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
									startActivity(Activities);
								}
							}
							else {
								if (getIntent().hasExtra("project")) {
									Activities.putExtra("lastPath", FileUtil.getExternalStorageDir());
									Activities.putExtra("type", getIntent().getStringExtra("type"));
									Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
									if (isInsideProject) {
										Activities.putExtra("isInsideProject", "true");
									}
									Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
									startActivity(Activities);
								}
								else {
									Activities.putExtra("lastPath", FileUtil.getExternalStorageDir());
									Activities.putExtra("type", "project");
									Activities.putExtra("path", path.concat("/".concat(textview1.getText().toString())));
									if (isInsideProject) {
										Activities.putExtra("isInsideProject", "true");
									}
									Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
									startActivity(Activities);
								}
							}
						}
					});
				}
			}
			else {
				cardview2.setRadius((float)0);
				imageview1.setImageResource(R.drawable.outline_description_black_36);
				linear2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						if (isInsideProject) {
							try{
								if (Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().substring((int)(Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().length() - ".".concat("html").length()), (int)(Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().length())).equals(".html")) {
									
									startActivity(Activities);
								}
								try{
									if (Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().substring((int)(Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().length() - ".".concat("apk").length()), (int)(Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().length())).equals(".apk")) {
										_Install(path.concat("/".concat(textview1.getText().toString())));
									}
									else {
										_OpenFile(path.concat("/".concat(textview1.getText().toString())), Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment());
									}
								}catch(Exception e){
									_OpenFile(path.concat("/".concat(textview1.getText().toString())), Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment());
								}
							}catch(Exception e){
								_OpenFile(path.concat("/".concat(textview1.getText().toString())), Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment());
							}
						}
						else {
							try{
								if (Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().substring((int)(Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().length() - ".".concat("apk").length()), (int)(Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().length())).equals(".apk")) {
									_Install(path.concat("/".concat(textview1.getText().toString())));
								}
								else if (Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().substring((int)(Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().length() - ".".concat("html").length()), (int)(Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment().length())).equals(".html")){
									_OpenFile(path.concat("/".concat(textview1.getText().toString())), Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment());
								}
								else {
									_OpenFile(path.concat("/".concat(textview1.getText().toString())), Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment());
								}
							}catch(Exception e){
								_OpenFile(path.concat("/".concat(textview1.getText().toString())), Uri.parse(path.concat("/".concat(textview1.getText().toString()))).getLastPathSegment());
							}
						}
					}
				});
			}
			cardview1.setCardElevation((float)0);
			cardview1.setRadius((float)20);
			cardview1.setPreventCornerOverlap(true);
			cardview2.setCardElevation((float)0);
			cardview2.setPreventCornerOverlap(true);
			cardview2.setCardBackgroundColor(Color.TRANSPARENT);
			_rippleRoundStroke(linear1, TSDTheme.getCurrentMaterialThemeColor(R.attr.colorPrimary,FilemanagerActivity.this), TSDTheme.getCurrentMaterialThemeColor(R.attr.colorOnPrimary,FilemanagerActivity.this), 0, 0, TSDTheme.getCurrentMaterialThemeColor(R.attr.colorPrimary,FilemanagerActivity.this));
			
			return _view;
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