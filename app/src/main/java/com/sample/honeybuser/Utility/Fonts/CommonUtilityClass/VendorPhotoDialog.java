package com.sample.honeybuser.Utility.Fonts.CommonUtilityClass;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.sample.honeybuser.Activity.VendorDetailActivity;
import com.sample.honeybuser.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Im033 on 11/4/2016.
 */

public class VendorPhotoDialog extends DialogFragment {
    private View view;
    private ImageView vendorImage, closeImageView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        view = LayoutInflater.from(getActivity()).inflate(R.layout.vendor_photo_dialog, null, false);
        vendorImage = (ImageView) view.findViewById(R.id.vendorDialogImageView);

        view.findViewById(R.id.vendorDetailsDialogClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        String image = getArguments().getString("image");
        Log.d("VendorDialog",image);
        if (!image.equalsIgnoreCase("")) {
            Picasso.with(getActivity()).load(image).into(vendorImage);
        } else {
            vendorImage.setImageResource(R.drawable.nouser);
        }
        dialog.setContentView(view);

        return dialog;
    }
}
