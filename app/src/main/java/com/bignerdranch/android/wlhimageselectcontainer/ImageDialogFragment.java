package com.bignerdranch.android.wlhimageselectcontainer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017/7/17/017.
 */

public class ImageDialogFragment extends DialogFragment {
    private static final String MSG_KEY = "msg";
    public static ImageDialogFragment newInstance(String msg) {
        ImageDialogFragment f = new ImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(MSG_KEY, msg);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_image, container, false);
        final ImageView imageView = (ImageView) v.findViewById(R.id.dialog_image);
        final String uri = getArguments().getString(MSG_KEY);
        Glide.with(getActivity()).load(uri).into(imageView);
        return v;
    }
}
