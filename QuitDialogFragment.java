package com.prosus.androidgames.viral;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class QuitDialogFragment extends DialogFragment {

  // The activity that creates an instance of this dialog fragment must
  // implement this interface in order to receive event callbacks. Each 
  // method passes the DialogFragment in case the host needs to query it.
  public interface QuitDialogListener {
    public void onDialogPositiveClick(DialogFragment dialog);

    public void onDialogNegativeClick(DialogFragment dialog);
  }

  // Use this instance of the interface to deliver action events
  QuitDialogListener mListener;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    builder.setTitle("Quit?");
    builder.setMessage("Are you sure you want to quit?");

    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        mListener.onDialogPositiveClick(QuitDialogFragment.this);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
      }
    });

    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        mListener.onDialogNegativeClick(QuitDialogFragment.this);
      }
    });

    AlertDialog dialog = builder.create();
    return dialog;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    // Verify that the host activity implements the callback interface.
    try {
      // Instantiate the QuitDialogListener so we can send events to the host.
      mListener = (QuitDialogListener) activity;
    } catch (ClassCastException e) {
      // The activity doesn't implement the interface, throw exception.
      throw new ClassCastException(activity.toString() + " must implement QuitDialogListener");
    }
  }
}