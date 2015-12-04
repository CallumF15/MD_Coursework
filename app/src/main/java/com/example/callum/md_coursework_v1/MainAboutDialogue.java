package com.example.callum.md_coursework_v1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Callum on 02/12/2015.
 */
public class MainAboutDialogue extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(getActivity());
        aboutDialog.setMessage("This App will display news your interested in")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        aboutDialog.setTitle("About");
        aboutDialog.setIcon(R.drawable.ic_action_about);
        //create the alert dialog  object and return it
        return aboutDialog.create();
    }
}
