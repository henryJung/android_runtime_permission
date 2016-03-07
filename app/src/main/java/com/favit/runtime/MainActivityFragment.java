package com.favit.runtime;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.favit.runtime.util.OnPermssionCallBackListener;
import com.favit.runtime.util.RuntimeUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final int RESULT_CAMERA = 0;
    private static final int RESULT_ALBUM = 1;

    private Button btnCamera;
    private Button btnAlbum;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        btnCamera = (Button) view.findViewById(R.id.camera);
        btnAlbum = (Button) view.findViewById(R.id.album);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RuntimeUtil.PERMISSION_CAMERA) {
            if (RuntimeUtil.verifyPermissions(getActivity(), grantResults)) {
                openCamera();
            }
        } else if (requestCode == RuntimeUtil.PERMISSION_ALBUM) {
            if (RuntimeUtil.verifyPermissions(getActivity(), getView(), grantResults)) {
                openAlbum();
            }
        }
    }


    private void openCamera() {
        RuntimeUtil.checkPermission(getActivity(), getView(), Manifest.permission.CAMERA, RuntimeUtil.PERMISSION_CAMERA, null, new OnPermssionCallBackListener() {
            @Override
            public void OnGrantPermission() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, RESULT_CAMERA);
                }
            }
        });
    }

    private void openAlbum() {
        RuntimeUtil.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, RuntimeUtil.PERMISSION_ALBUM, new OnPermssionCallBackListener() {
            @Override
            public void OnGrantPermission() {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_ALBUM);
            }
        });
    }
}
