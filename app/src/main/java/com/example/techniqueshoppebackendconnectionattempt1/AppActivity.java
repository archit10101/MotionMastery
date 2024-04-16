package com.example.techniqueshoppebackendconnectionattempt1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.techniqueshoppebackendconnectionattempt1.Fragments.CreateFragment;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.HomeFragment;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchFragment;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);

        loadFragment(R.id.navHome, new HomeFragment(), false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                item.setChecked(true);

                if (itemId == R.id.navHome){
                    loadFragment(R.id.navHome,new HomeFragment(), true);
                }else if(itemId == R.id.navSearch){
                    loadFragment(R.id.navSearch, new SearchFragment(), true);
                }else if(itemId == R.id.navCreate){
                    loadFragment(R.id.navCreate, new CreateFragment(), true);
                }else if(itemId == R.id.navSettings){
                    loadFragment(R.id.navSettings, new SettingsFragment(), true);
                }
                return false;
            }
        });
    }

    private void loadFragment(int nav, Fragment fragment, boolean replace){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (replace){
            fragmentTransaction.replace(R.id.frameLayout,fragment);
        }else {
            fragmentTransaction.add(R.id.frameLayout,fragment);

        }
        fragmentTransaction.commit();

    }
}