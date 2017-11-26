package com.moictab.culturalba.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moictab.culturalba.R;

public class AboutDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_about, null);

        String version = "0";
        try {
            version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView tvVersion = (TextView) rootView.findViewById(R.id.textview_version);
        tvVersion.setText(String.valueOf(R.string.version) + String.valueOf(version));

        builder.setView(rootView).setPositiveButton(R.string.Accept, null);

        return builder.create();
    }
}
