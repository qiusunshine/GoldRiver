package com.hdy.goldhe;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.hdy.goldhe.Base.BaseActivity;
import com.hdy.goldhe.Util.DownPicUtil;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.umeng.analytics.MobclickAgent;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import cn.refactor.lib.colordialog.ColorDialog;
import static android.view.View.VISIBLE;

public class webActivity extends BaseActivity {
	private WebView cc;
	public ValueCallback<Uri[]> uploadMessage;
	public static final int REQUEST_SELECT_FILE = 100;
	private WebSettings webSettings;
 	private String url33;
	private ProgressBar bar;
	private boolean isjiexi=false;
	/** 视频全屏参数 */
	protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	private View customView;
	private FrameLayout fullscreenContainer;
	private WebChromeClient.CustomViewCallback customViewCallback;
	private Toolbar toolbar;
	private boolean loadpicture=true;
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_detail2);
		inittoolbar();
		//调用菜单始终显示
		//初始化状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
		}
		cc = (WebView) findViewById(R.id.mainWebView3);
		bar = (ProgressBar)findViewById(R.id.myProgressBar3);
		webSettings = cc.getSettings();
		webSettings.setJavaScriptEnabled(true);
// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setDisplayZoomControls(false);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setDefaultTextEncodingName("UTF-8");
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setDomStorageEnabled(true);
		cc.getSettings().setAppCacheMaxSize(1024*1024*8);
		String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
		cc.getSettings().setAppCachePath(appCachePath);
		cc.getSettings().setAllowFileAccess(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		if (mDensity == 240) {
			webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		} else if (mDensity == 160) {
			webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		} else if(mDensity == 120) {
			webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
		}else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
			webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		}else if (mDensity == DisplayMetrics.DENSITY_TV){
			webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		}else{
			webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		}
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		//下一个界面接收值：
		Intent intent = this.getIntent();
		String url = intent.getStringExtra("url");
		loadpicture=intent.getBooleanExtra("loadpicture",true);
		cc.setWebViewClient(new MyWebViewClient());
		cc.loadUrl(url);
		cc.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					bar.setVisibility(View.INVISIBLE);
				}
				else {
					if (bar.getVisibility()==View.INVISIBLE ) {
						bar.setVisibility(View.VISIBLE);
					}
					bar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
			/*** 视频播放相关的方法 **/
			@Override
			public View getVideoLoadingProgressView() {
				FrameLayout frameLayout = new FrameLayout(webActivity.this);
				frameLayout.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
				return frameLayout;
			}
			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				showCustomView(view, callback);
			}
			@Override
			public void onHideCustomView() {
				hideCustomView();
			}
//支持图片上传
			@TargetApi(Build.VERSION_CODES.LOLLIPOP)
			public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
				if (uploadMessage != null) {
					uploadMessage.onReceiveValue(null);
					uploadMessage = null;
				}
				uploadMessage = filePathCallback;
				Intent intent = fileChooserParams.createIntent();
				try {
					startActivityForResult(intent, REQUEST_SELECT_FILE);
				} catch (ActivityNotFoundException e) {
					uploadMessage = null;
					Toast.makeText(getBaseContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
					return false;
				}
				return true;
			}
		});
//长安保存网络图片
		cc.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				final WebView.HitTestResult hitTestResult = cc.getHitTestResult();
				// 如果是图片类型或者是带有图片链接的类型
				if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
						hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
					// 弹出保存图片的对话框
					ColorDialog dialog = new ColorDialog(webActivity.this);
					dialog.setColor("#009688");
					dialog.setAnimationEnable(true);
					dialog.setTitle("保存图片");
					dialog.setContentText("是否保存图片到本地?");
					dialog.setPositiveListener("保存", new ColorDialog.OnPositiveListener() {
						@Override
						public void onClick(ColorDialog dialog) {
							dialog.dismiss();
							String url = hitTestResult.getExtra();
							// 下载图片到本地
							DownPicUtil.downPic(url, new DownPicUtil.DownFinishListener() {
								public void getDownPath(String s) {
									AppToastMgr.ToastShortBottomCenter(webActivity.this,"下载完成");
									Message msg = Message.obtain();
									msg.obj = s;
									handler.sendMessage(msg);
								}
							});
							}
							});
							dialog.setNegativeListener("查看",new ColorDialog.OnNegativeListener(){
								@Override
								public void onClick(ColorDialog dialog) {
									cc.loadUrl(hitTestResult.getExtra());
									dialog.dismiss();
								}
							});
							dialog.show();
					return true;
				} else {
					return false;
				}
			}
		});
//下载文件事件
		cc.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String arg1, String arg2,
										String arg3, long arg4) {
				final String urlpass=url;
				ColorDialog dialog = new ColorDialog(webActivity.this);
				dialog.setColor("#009688");
				dialog.setAnimationEnable(true);
				dialog.setTitle("下载提示");
				dialog.setContentText("确定下载此文件？");
				dialog.setPositiveListener("下载", new ColorDialog.OnPositiveListener() {
					@Override
					public void onClick(ColorDialog dialog) {
						Uri uri = Uri.parse(urlpass);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
						dialog.dismiss();
					}
				});
				dialog.setNegativeListener("复制链接",new ColorDialog.OnNegativeListener(){
					@Override
					public void onClick(ColorDialog dialog) {
                        ClipboardManager clipManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData mClipData = ClipData.newPlainText("Label", urlpass);
// 将ClipData内容放到系统剪贴板里。
                        clipManager.setPrimaryClip(mClipData);
						AppToastMgr.ToastShortBottomCenter(webActivity.this,"已复制链接");
                        dialog.dismiss();
					}
				});
				dialog.show();


			}
		});
//oncreat()方法末尾
	}
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String picFile = (String) msg.obj;
				String[] split = picFile.split("/");
				String fileName = split[split.length - 1];
				try {
					MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), picFile, fileName, null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// 最后通知图库更新
				getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + picFile)));
				AppToastMgr.ToastShortBottomCenter(webActivity.this,"图片保存图库成功");
			}
		};
public void inittoolbar(){
	toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.web3_toolbar);
	toolbar.setNavigationIcon(R.drawable.ic_arrow_back_red);
	toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_toolbar_overflow));
	setSupportActionBar(toolbar);
	toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
	toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			switch (item.getItemId()){
				case R.id.toolbar_share:
					sendtext(cc.getTitle()+"\n\n链接："+cc.getUrl()+"\n\n--GoldRiver-HDY");
					break;
				case R.id.toolbar_collection:
					ClipboardManager clipManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData mClipData = ClipData.newPlainText("Label", cc.getUrl());
					// 将ClipData内容放到系统剪贴板里。
					clipManager.setPrimaryClip(mClipData);
					AppToastMgr.ToastShortBottomCenter(webActivity.this,"已复制链接");
					break;
				case R.id.toolbar_sourceurl:
					if(cc.getUrl().contains("ms.csdn.net")){
						try {
							Intent intenturl=getIntent();
							cc.loadUrl(intenturl.getStringExtra("source_url"));
						} catch (Exception e) {
							AppToastMgr.ToastShortBottomCenter(webActivity.this,e.toString());
							e.printStackTrace();
						}
					}
					else AppToastMgr.ToastShortBottomCenter(webActivity.this,"当前页面没有链接来源");
					break;
				default:break;
			}
			return true;
		}
	});
	getSupportActionBar().setDisplayShowTitleEnabled(false);
	makeActionOverflowMenuShown();
}
	private void makeActionOverflowMenuShown() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.web3_menu, menu);//加载menu文件到布局
		return true;
	}

	//创建方法将输入的内容发出去
	public void sendtext(String str){
		Intent intent = new Intent();
        /*设置action为发送分享，
        *并判断要发送分享的内容是否为空
         */
		intent.setAction(Intent.ACTION_SEND);
		if(str!=null){
			intent.putExtra(Intent.EXTRA_TEXT,str);
		}else{
			intent.putExtra(Intent.EXTRA_TEXT,"");
		}
		intent.setType("text/plain");
		startActivity(Intent.createChooser(intent,"分享"));
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				/** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
				if (customView != null) {
					hideCustomView();
				} else if (cc.canGoBack()) {
					cc.goBack();

				} else {
					finish();
				}
				return true;
			default:
				return super.onKeyUp(keyCode, event);
		}
	}
	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onStop() {
		super.onStop();
	}
	protected void onResume() {
		super.onResume();
		//恢复播放
		cc.resumeTimers();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		//暂停播放
		cc.pauseTimers();
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (cc != null) {
			ViewParent parent = cc.getParent();
			if (parent != null) {
				((ViewGroup) parent).removeView(cc);
			}
			cc.stopLoading();
			cc.getSettings().setJavaScriptEnabled(false);
			cc.clearHistory();
			cc.clearView();
			cc.removeAllViews();
			try {
				cc.destroy();
			} catch (Throwable ex) {

			}
		}

	}
	/** 视频播放全屏 函数集合**/
	private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {

		// if a view already exists then immediately terminate the new one
		if (customView != null) {
			callback.onCustomViewHidden();
			return;
		}
		webActivity.this.getWindow().getDecorView();
		FrameLayout decor = (FrameLayout) getWindow().getDecorView();
		fullscreenContainer = new webActivity.FullscreenHolder(webActivity.this);
		fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
		decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
		customView = view;
		customViewCallback = callback;
		cc.setVisibility(View.INVISIBLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setStatusBarVisibility(false);
	}
	/** 隐藏视频全屏 */
	private void hideCustomView() {
		if (customView == null) {
			return;
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setStatusBarVisibility(true);
		FrameLayout decor = (FrameLayout) getWindow().getDecorView();
		decor.removeView(fullscreenContainer);
		fullscreenContainer = null;
		customView = null;
		customViewCallback.onCustomViewHidden();
		cc.setVisibility(VISIBLE);
	}
	/** 全屏容器界面 */
	static class FullscreenHolder extends FrameLayout {

		public FullscreenHolder(Context ctx) {
			super(ctx);
			setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
		}
		@Override
		public boolean onTouchEvent(MotionEvent evt) {
			return true;
		}
	}
	private void setStatusBarVisibility(boolean visible) {
		int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private final class MyWebViewClient extends WebViewClient{
		public WebResourceResponse shouldInterceptRequest(WebView view, String url){
			if(url.contains("www.doubiekan.org/w/")||url.contains("http://shen.duanat.com/"))
				return new WebResourceResponse(null,null,null);
			if((!loadpicture)&&(url.endsWith(".jpg")||url.endsWith(".jpeg")||url.endsWith(".png")))
				return new WebResourceResponse(null,null,null);
			if(!isjiexi) {
				if(url.contains("douban")&&url.contains("do/_init_.js"))
					return new WebResourceResponse(null,null,null);
				else return super.shouldInterceptRequest(view, url);
			}
			else 	return super.shouldInterceptRequest(view, url);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			try {
				toolbar.setTitle(view.getTitle());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(!isjiexi) {
                cc.loadUrl("javascript:function setTop(){document.getElementById('down-footer').style.display=\"none\";}setTop();");
				cc.loadUrl("javascript:function setTop(){document.getElementById('xnjKT431212_2860').style.display=\"none\";}setTop();");//磁力蚂蚁
				cc.loadUrl("javascript:function setTop(){document.getElementById('Chy59y1dlwe4').style.display=\"none\";}setTop();");//都别看
				cc.loadUrl("javascript:function setTop(){document.getElementById('fixedAdWrap').style.display=\"none\";}setTop();");//果壳
				cc.loadUrl("javascript:function setTop(){document.querySelector('.kyt-download-area').style.display=\"none\";}setTop();");
				cc.loadUrl("javascript:function setTop(){document.querySelector('.kyt-footer').style.display=\"none\";}setTop();");
				cc.loadUrl("javascript:function setTop(){document.querySelector('.kyt-promotion-bar').style.display=\"none\";}setTop();");
				cc.loadUrl("javascript:function setTop(){document.querySelector('.kyt-promotion-bar-positioner').style.display=\"none\";}setTop();");
				cc.loadUrl("javascript:function setTop(){document.querySelector('.kyt-download-area-launch').style.display=\"none\";}setTop();");
				cc.loadUrl("javascript:function setTop(){document.querySelector('.global-header').style.display=\"none\";}setTop();");
				cc.loadUrl("javascript:function setTop(){document.querySelector('.qr').style.display=\"none\";}setTop();");
				cc.loadUrl("javascript:function setTop(){document.querySelector('.bs-header').style.display=\"none\";}setTop();");
                cc.loadUrl("javascript:function setTop(){document.querySelector('.top_box').style.display=\"none\";}setTop();");
                cc.loadUrl("javascript:function setTop(){document.querySelector('.ad_box').style.display=\"none\";}setTop();");
				cc.loadUrl("javascript:function setTop(){document.querySelector('.tmall_1111_left').style.display=\"none\";}setTop();");//磁力蚂蚁
				cc.loadUrl("javascript:function setTop(){document.querySelector('.bottom_ad_download show').style.display=\"none\";}setTop();");//豆瓣搜索页
            }
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			toolbar.setTitle(R.string.loading);
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
			}
		}
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			AppToastMgr.ToastShortBottomCenter(webActivity.this,"加载失败！");
		}
		//加载https错误让它继续加载而不是显示空白
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//      一定要注释掉！
//      super.onReceivedSslError(view, handler, error);
			handler.proceed();
		}
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if( url.startsWith("http:") || url.startsWith("https:") ) {
				return false;
			}
			else {
				url33=url;
                Snackbar.make(cc, "允许网页打开相应软件？", Snackbar.LENGTH_LONG)
                        .setAction("打开", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url33));
                                    startActivity(intent);
                                } catch (Exception e) {
									AppToastMgr.ToastShortBottomCenter(webActivity.this,"您的手机没有安装该软件！");
                                }
                            }
                        })
                        .show();
				return true;
			}
		}
	}


}
