package com.moictab.culturalba;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioButton;

import listener.OnSettingsAccepted;

/**
 * Diálogo de inicio que muestra información. Sólo se muestra una vez
 */
public class SettingsDialog extends DialogFragment {

    private OnSettingsAccepted listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_settings, null);

        final RadioButton rbHoy = (RadioButton) rootView.findViewById(R.id.radiobutton_hoy);
        final RadioButton rbTodo = (RadioButton) rootView.findViewById(R.id.radiobutton_todo);

        boolean today = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("today", true);

        if (today) {
            rbHoy.setChecked(true);
        } else {
            rbTodo.setChecked(true);
        }

        builder.setView(rootView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.OnBarcodeProcessed(rbHoy.isChecked());
                    }
                });

        return builder.create();
    }

    public void setListener(OnSettingsAccepted listener) {
        this.listener = listener;
    }
}
