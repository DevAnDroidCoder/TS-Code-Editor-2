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
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import io.github.rosemoe.sora.*;
import io.github.rosemoe.sora.langs.java.*;
import io.github.rosemoe.sora.langs.textmate.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageInfo;

public class MainActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private boolean isRequested = false;
	private String TextOnSecondary = "";
	private String TextOnPrimary = "";
	private String primaryColor = "";
	private String secondaryColor = "";
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private TextView textview1;
	private ImageView imageview1;
	
	private TimerTask DelayApp;
	private Intent Activities = new Intent();
	private SharedPreferences theme;
	private MaterialAlertDialogBuilder MaterialDialog;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		textview1 = findViewById(R.id.textview1);
		imageview1 = findViewById(R.id.imageview1);
		theme = getSharedPreferences("theme", Activity.MODE_PRIVATE);
		MaterialDialog = new MaterialAlertDialogBuilder(this);
	}
	
	private void initializeLogic() {
		_UI();
		try{
			PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			String version = pInfo.versionName;
			textview1.setText("Version : ".concat(version));
		}catch(Exception e){
			textview1.setText("");
		}
		/*
Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/App/Asset/logo.gif")).into(imageview1);
*/
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
								Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(),null);
								intent.setData(uri);
								MainActivity.this.startActivity(intent);
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
	
	@Override
	public void onRequestPermissionsResult(int _requestCode, String[] _permissions, int[] _grantResults) {
		MaterialAlertDialogBuilder PermissionAsk2 = new MaterialAlertDialogBuilder(MainActivity.this);
		for(int _repeat20 = 0; _repeat20 < (int)(_permissions.length); _repeat20++) {
			String Permission = _permissions[_repeat20];
			if (_grantResults[_repeat20] == PackageManager.PERMISSION_DENIED) {
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
						MaterialDialog.setTitle("Storage permission required");
						MaterialDialog.setMessage("Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app.");
						MaterialDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								isRequested = true;
								Intent intent = new Intent();
								intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(),null);
								intent.setData(uri);
								MainActivity.this.startActivity(intent);
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
					//setting
					if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(Permission)) {
						MaterialDialog.setTitle("Storage permission required");
						MaterialDialog.setMessage("Storage permissions is highly recommend for storing and reading files in device.Without this permission you can't use this app.");
						MaterialDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								isRequested = true;
								Intent intent = new Intent();
								intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(),null);
								intent.setData(uri);
								MainActivity.this.startActivity(intent);
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
	
	public void startManually(){
		DelayApp = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Activities.putExtra("lastPath", FileUtil.getExternalStorageDir());
						Activities.putExtra("path", FileUtil.getExternalStorageDir());
						Activities.putExtra("type", "project");
						Activities.putExtra("isInsideProject", "false");
						Activities.setClass(getApplicationContext(), FilemanagerActivity.class);
						startActivity(Activities);
					}
				});
			}
		};
		_timer.schedule(DelayApp, (int)(4000));
	}
	public void _UI() {
		/*

.initialize(theme,MainActivity.this);
.getCurrentTSDThemeData("themedarkblue");
_StatusAndNavigation(.PrimaryColor);
_setTextColor(textview1, .ColorOnPrimary);
*/
		_StatusAndNavigation("#1b0b54");
		_setTextColor(textview1, "#FFFFFF");
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
	
	
	public void _showRationale(final String _permission, final String _text) {
		if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(_permission)) {
			MaterialDialog.setTitle("Storage Permission required");
			MaterialDialog.setMessage(_text);
			MaterialDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
					|| ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
							ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
					} else {
							MainActivity m = new MainActivity();
							m.startManually();
					}
					
				}
			});
			MaterialDialog.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					finishAffinity();
				}
			});
			MaterialDialog.create().show();
		}
	}
	
	
	public void _setTextColor(final TextView _view, final String _color) {
		_view.setTextColor(Color.parseColor(_color));
	}
	
	
	public void _requestStoragePermission() {
		ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
	}
	
	
	public void _AppConfig() {
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