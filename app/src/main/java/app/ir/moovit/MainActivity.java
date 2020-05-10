package app.ir.moovit;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    Button again;
    LinearLayout parent_no_internet;
    boolean doubleBackToExitPressedOnce = false;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());

        AndroidNetworking.get("http://37.120.146.7:3003/dehban")
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("value")){
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progress);
        parent_no_internet = findViewById(R.id.parent_no_internet);
        again = findViewById(R.id.again);

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        if (isOnline() && isConnectingToInternet()) {

            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("https://moovit.ir/");
            webView.setWebViewClient(new WebViewClient());

            webView.setWebChromeClient(new WebChromeClient() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                    if (uploadMessage != null) {
                        uploadMessage.onReceiveValue(null);
                        uploadMessage = null;
                    }

                    uploadMessage = filePathCallback;

                    Intent intent = fileChooserParams.createIntent();
                    try
                    {
                        startActivityForResult(intent, 100);
                    } catch (ActivityNotFoundException e)
                    {
                        uploadMessage = null;
                        Toast.makeText(getApplicationContext(), "خطا در فایل", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    return true;
                }

                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        webView.setVisibility(View.GONE);
                    }

                    progressBar.setProgress(progress);
                    if (progress == 100) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        webView.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            parent_no_internet.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setInitialScale(100);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.loadUrl("http://137.takestancity.ir/user/login");
        webView.setWebViewClient(new WebViewClient());

//        webView.setWebChromeClient(new WebChromeClient() {
//
//
//        }
//        webView.setWebChromeClient(new MyWebChromeClient(){
//            public void onProgressChanged(WebView view, int progress) {
//                if(progress < 100 && pbar.getVisibility() == ProgressBar.GONE){
//                    pbar.setVisibility(ProgressBar.VISIBLE);
//                    txtview.setVisibility(View.VISIBLE);
//                }
//
//                pbar.setProgress(progress);
//                if(progress == 100) {
//                    pbar.setVisibility(ProgressBar.GONE);
//                    txtview.setVisibility(View.GONE);
//                }
//            }
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                if (uploadMessage != null) {
//                    uploadMessage.onReceiveValue(null);
//                    uploadMessage = null;
//                }
//
//                uploadMessage = filePathCallback;
//
//                Intent intent = fileChooserParams.createIntent();
//                try
//                {
//                    startActivityForResult(intent, 100);
//                } catch (ActivityNotFoundException e)
//                {
//                    uploadMessage = null;
//                    Toast.makeText(getApplicationContext(), "خطا در فایل", Toast.LENGTH_LONG).show();
//                    return false;
//                }
//                return true;
//            }
//        });

//        webView.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
//                    progressBar.setVisibility(ProgressBar.VISIBLE);
//                }
//
//                progressBar.setProgress(progress);
//                if (progress == 100) {
//                    progressBar.setVisibility(ProgressBar.GONE);
//                }
//            }
//        });


//        final String js = "javascript:document.getElementById('txtpwd').value='12345678';" + "javascript:document.getElementById('txtun').value='admin';" +
//                "document.getElementById('btnLogin').click()";
//
//        // Set a web view client for web view
//        webView.setWebViewClient(new WebViewClient() {
//            /*
//                void onPageFinished (WebView view, String url)
//                    Notify the host application that a page has finished loading. This method is
//                    called only for main frame. When onPageFinished() is called, the rendering
//                    picture may not be updated yet. To get the notification for the new Picture,
//                    use onNewPicture(WebView, Picture).
//
//                Parameters
//                    view WebView : The WebView that is initiating the callback.
//                    url String : The url of the page.
//            */
//            public void onPageFinished(WebView view, String url) {
//                if (Build.VERSION.SDK_INT >= 19) {
//                    view.evaluateJavascript(js, new ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String s) {
//                            Log.i("ldkjfa", "onReceiveValue: "+ s);
//                        }
//                    });
//                }
//            }
//        });
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }

        return false;
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI
                    ||
                    activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) ? true : false;
        }
        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == 100)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                uploadMessage = null;
            }
        }
        else if (requestCode == FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage)
                return;

            Uri result = data == null || resultCode != MainActivity.RESULT_OK ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
        else
            Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.toast_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


//        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
//            if (requestCode == REQUEST_SELECT_FILE) {
//                if (uploadMessage == null) return;
//                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
//                uploadMessage = null;
//            }
//        }
//    }


    }
}