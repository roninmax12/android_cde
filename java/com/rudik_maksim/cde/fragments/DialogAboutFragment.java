package com.rudik_maksim.cde.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rudik_maksim.cde.R;

/**
 * Created by Максим on 17.09.2014.
 */
public class DialogAboutFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Dialog dialog = super.getDialog();
        dialog.setTitle("Версия " + getString(R.string.app_version));

        // Set title divider color
        int titleId = getResources().getIdentifier("title", "id", "android");
        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");

        View titleDivider = dialog.findViewById(titleDividerId);
        View titleView = dialog.findViewById(titleId);

        if (titleDivider != null)
            titleDivider.setBackgroundColor(getResources().getColor(R.color.main_blue_color));

        if (titleView != null)
            titleView.setBackgroundColor(getResources().getColor(R.color.main_blue_color));

        View v = inflater.inflate(R.layout.dialog_about, container, false);
        LinearLayout linLayoutDevPage = (LinearLayout) v.findViewById(R.id.dialog_about_linLayout_developerPage);
        LinearLayout linLayoutAppPage = (LinearLayout) v.findViewById(R.id.dialog_about_linLayout_applicationPage);
        LinearLayout linLayoutAppGooglePage = (LinearLayout) v.findViewById(R.id.dialog_about_linLayout_applicationGooglePage);

        linLayoutAppGooglePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dismiss();
            }
        });

        linLayoutAppPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.app_site)));
                startActivity(i);
                dismiss();
            }
        });

        linLayoutDevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.dev_site)));
                startActivity(i);
                dismiss();
            }
        });

        return v;
    }
}
