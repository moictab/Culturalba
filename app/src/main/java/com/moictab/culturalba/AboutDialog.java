package com.moictab.culturalba;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Diálogo que se muestra cuando se pulsa la opción de "Acerca de"
 */
public class AboutDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_about, null);

        TextView tvVersion = (TextView) rootView.findViewById(R.id.textview_version);

        String version = "0";
        try {
            version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvVersion.setText(getString(R.string.version, String.valueOf(version)));
        builder.setView(rootView)
                .setPositiveButton("Aceptar", null);

        return builder.create();
    }
}
