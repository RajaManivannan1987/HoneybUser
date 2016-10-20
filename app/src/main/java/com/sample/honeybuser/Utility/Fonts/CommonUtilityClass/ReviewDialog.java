package com.sample.honeybuser.Utility.Fonts.CommonUtilityClass;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.sample.honeybuser.InterFaceClass.VolleyResponseListerner;
import com.sample.honeybuser.R;
import com.sample.honeybuser.Singleton.Complete;
import com.sample.honeybuser.Utility.Fonts.WebServices.ConstandValue;
import com.sample.honeybuser.Utility.Fonts.WebServices.GetResponseFromServer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Im033 on 10/20/2016.
 */

public class ReviewDialog extends DialogFragment {
    private String TAG = "ReviewDialog";
    private View view;
    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button cancelButton, submitButton;
    private String vendorId = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        vendorId = getArguments().getString(ConstandValue.vendorId, "");
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_product_review, null, false);
        ratingBar = (RatingBar) view.findViewById(R.id.vendorDetailsRatingBar);
        reviewEditText = (EditText) view.findViewById(R.id.vendorDetailsEditText);
        cancelButton = (Button) view.findViewById(R.id.vendorDetailsCancelButton);
        submitButton = (Button) view.findViewById(R.id.vendorDetailsSubmitButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingBar.getRating() > 0) {
                    submit();
                } else {
                    Toast.makeText(getActivity(), "Select star rating", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setContentView(view);
        return dialog;

    }

    private void submit() {
        submitButton.setEnabled(false);
        GetResponseFromServer.getWebService(getActivity().getApplicationContext(), TAG).addRating(getActivity().getApplicationContext(), vendorId, ratingBar.getRating() + "", reviewEditText.getText().toString().trim(), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    CommonMethods.toast(getActivity(), response.getString("message"));
                    dismiss();
                    Complete.ratingReloadDialogInstance().orderCompleted();
                } else {
                    CommonMethods.toast(getActivity(), response.getString("message"));
                }
                submitButton.setEnabled(true);
            }

            @Override
            public void onError(String message, String title) {
                submitButton.setEnabled(true);
                CommonMethods.toast(getActivity(), message);
            }
        });
    }
}
