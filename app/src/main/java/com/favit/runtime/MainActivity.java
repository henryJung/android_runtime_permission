package com.favit.runtime;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.favit.runtime.util.OnPermssionCallBackListener;
import com.favit.runtime.util.RuntimeUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String curFragmentName = MainActivityFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //저장 권한
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStoragePermission();
            }
        });
    }

    private void getStoragePermission() {
        RuntimeUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, RuntimeUtil.PERMISSION_STORAGE, new OnPermssionCallBackListener() {
            @Override
            public void OnGrantPermission() {
                Toast.makeText(MainActivity.this, "Grant Permission", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getCurrentFragment().onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RuntimeUtil.PERMISSION_STORAGE) {
            if (RuntimeUtil.verifyPermissions(this, grantResults)) {
                getStoragePermission();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Fragment getCurrentFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        Fragment fragment = fragments.get(fragments.size() - 1);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getCurrentFragment().onActivityResult(requestCode, resultCode, data);
    }
}
