package com.devseven.gympack.materialsetlogger.mainmenu;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import com.devseven.gympack.materialsetlogger.R;
import com.devseven.gympack.materialsetlogger.datacontroller.ApplicationFileManager;


public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentInteractionListener {
    // might wrap these in a holder like BuilderViewHolder
    ViewPager viewPager;
    FloatingActionButton floatingActionButton;
    BottomNavigationView navigationView;
    int PERMISSION_REQUEST_CODE = 100; // I don't actually remember what this was for...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!areIOPermissionsGranted())
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE}, PERMISSION_REQUEST_CODE);
        }
        ApplicationFileManager.buildDirectorires(this);
        setContentView(R.layout.main_menu_v2);

        // region VIEWPAGER SETUP
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setCurrentItem(MainPagerAdapter.INDEX_HOME);
        viewPager.addOnPageChangeListener(new MainPagerAdapterChangeListener(mainPagerAdapter));
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.action_homeMenu:
                        viewPager.setCurrentItem(MainPagerAdapter.INDEX_HOME);
                    break;
                    case R.id.action_logsMenu:
                        viewPager.setCurrentItem(MainPagerAdapter.INDEX_LOG);
                    break;
                    case R.id.action_routinesMenu:
                        viewPager.setCurrentItem(MainPagerAdapter.INDEX_ROUTINES);
                    break;
                }

                return true;
            }
        });
        // endregion
    }

    public boolean areIOPermissionsGranted() {
        /*
         * The function areIOPermissionsGranted() returns true if the user has already granted all the permissions necessary for
         *  the proper operation of the application, i.e. read/write files to external and internal storage. The application stores
         *  routine files, logs and exercise data.
         */
        int permission_write_external = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_read_external = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission_storage = ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE);
        if (permission_read_external == PackageManager.PERMISSION_DENIED) {
            Log.e(this.toString(), "PERMISSION ERROR. READ EXTERNAL NOT GRANTED.");
            return false;
        } else if (permission_write_external == PackageManager.PERMISSION_DENIED) {
            Log.e(this.toString(), "PERMISSION ERROR. WRITE EXTERNAL NOT GRANTED.");
            return false;

        } else if(permission_storage == PackageManager.PERMISSION_DENIED)
        {
            Log.e(this.toString(),"PERMISSION ERROR. STORAGE NOT GRANTED");
            return false;
        }
        else {
            Log.d(this.toString(), "Permissions granted.");
            return true;
        }

    }



    @Override
    public void onNextDayStarted() {

    }

    @Override
    public void onNextDayChanged(int dayIndex) {

    }


    @Override
    public FloatingActionButton getFloatingActionButton() {
        if(floatingActionButton == null)
            floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        return floatingActionButton;
    }

}
