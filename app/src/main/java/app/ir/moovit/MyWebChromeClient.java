package app.ir.moovit;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class MyWebChromeClient  extends WebChromeClient {
    // reference to activity instance. May be unnecessary if your web chrome client is member class.
    private MainActivity myActivity;

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        // make sure there is no existing message
        if (myActivity.uploadMessage != null) {
            myActivity.uploadMessage.onReceiveValue(null);
            myActivity.uploadMessage = null;
        }

        myActivity.uploadMessage = filePathCallback;

        Intent intent = fileChooserParams.createIntent();
        try {
            myActivity.startActivityForResult(intent, MainActivity.REQUEST_SELECT_FILE);
        } catch (ActivityNotFoundException e) {
            myActivity.uploadMessage = null;
            Toast.makeText(myActivity, "Cannot open file chooser", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
