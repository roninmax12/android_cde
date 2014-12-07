package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.webkit.WebViewClient;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;

/**
 * Created by Максим on 16.09.2014.
 */
public class FragmentRecordCDE extends Fragment {
    WebView webView;
    ImageView imageView;
    Animation animation;
    FrameLayout fLayout;
    Activity rootActivity;

    int checked_count = 0;

    public FragmentRecordCDE(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_cde, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        rootActivity = getActivity();
        rootActivity.getActionBar().setTitle(R.string.title_fragmentRecord);
        rootActivity.getActionBar().setSubtitle(null);

        imageView = (ImageView)getView().findViewById(R.id.fragmentRecord_imageView);
        webView = (WebView)getView().findViewById(R.id.fragmentRecord_webView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);
        fLayout = (FrameLayout)getView().findViewById(R.id.fragmentRecord_frameLayout);

        Global.Application.currentFragmentId = Global.Configuration.NAV_RECORD_CDE;

        loadUrl();
    }

    public void loadUrl(){
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(animation);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearHistory();
        webView.loadUrl("https://de.ifmo.ru/--schedule/index.php?login=" + Global.Application.connection.getLogin() + "&passwd=" + Global.Application.connection.getPassword() + "&role=%D1%F2%F3%E4%E5%ED%F2");
        webView.setWebViewClient(new MyWebClient() {
            public void onPageFinished(WebView view, String url) {
                if (++checked_count == 2) {
                    // 2 - т.к. при ПЕРВОМ запуске этого WevView страница загружается только на ВТОРОМ срабатывании данного метода.
                    imageView.clearAnimation();
                    imageView.setVisibility(View.INVISIBLE);
                    if (Global.Application.currentFragmentId == Global.Configuration.NAV_RECORD_CDE){
                        Animation a = AnimationUtils.loadAnimation(rootActivity, R.anim.up_top);
                        fLayout.startAnimation(a);
                    }
                    webView.setVisibility(View.VISIBLE);
                    checked_count = 0; // was be changed to zero, because this is working on android 4.4.2
                    // 1 - т.к. при ПОВТОРНОМ запуске этого WevView страница загружается на ПЕРВОМ (одна операция инкрементации) срабатывании данного метода.
                }
            }
        });
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }
}
