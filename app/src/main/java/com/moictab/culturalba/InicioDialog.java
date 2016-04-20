package com.moictab.culturalba;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

/**
 * Diálogo de inicio que muestra información. Sólo se muestra una vez
 */
public class InicioDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_inicio, null);
        builder.setView(rootView)
                .setPositiveButton("Aceptar", null);

        return builder.create();
    }
}
