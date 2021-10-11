package petstone.project.animalisland.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import petstone.project.animalisland.R;

public class WebviewActivity extends AppCompatActivity {

    WebView webView;

    class MyScript{

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data){

            Log.d("data",data);

            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            i.putExtra("data",data);
            setResult(RESULT_OK,i);
            finish();
/*
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("data",data);
            intent.putExtras(extra);
            setResult(RESULT_OK,intent);
            finish();
*/

        }

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.webView);
        //자스 실행 허용
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyScript(), "Android");
        //화면 고정
        webView.setWebViewClient(new WebViewClient(){


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        //webView.loadUrl("file:///android_asset/html/daum2.html");
        webView.loadUrl("http://sd1926.dothome.co.kr/");
        //webView.loadUrl("http://naver.com");

    }
}