package com.example.projectosa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.projectosa.state.EstadoApp;
import com.example.projectosa.utils.Permissions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.example.projectosa.pages.ViewPagerAdapter;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private TabLayout tabLayout; // aquela barra com as páginas que o user quer seleccionar
    private ViewPager2 viewPager; // para permitir a "deslocação" entre páginas
    ViewPagerAdapter viewPagerAdapter; // para permitir a "deslocação" entre páginas

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // O que veio de origem com o projecto
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // A partir daqui é relativo à parte de deslizar com as tabs nas diversas páginas.
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.registerOnPageChangeCallback(new OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
            super.onPageSelected(position);
            TabLayout.Tab tab = tabLayout.getTabAt(position);
            if (tab != null)
                tab.select();// o IF evita o warning: "Method invocation 'select' may produce 'NullPointerException'"
            }
        });
        // Vai buscar as geofences ao Firestore database
        EstadoApp.fetchGeofences();
        // Permissões
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Permissions.requestPermissions(getApplicationContext(), this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        Log.e("DEBUG", "REQUESTCODE: " + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            finish(); // Termina a aplicação, esta só funciona com as permissões activadas
        }
    }
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public ViewPager2 getViewPager(){
        return viewPager;
    }

    public Button getSwitchPedonalPage(){
        return findViewById(R.id.switchLigado);
    }

    public Button getLogoutButtonPedonalPage(){
        return findViewById(R.id.buttonLogout);
    }

    public Button getSwitchViagemPage(){
        return findViewById(R.id.buttonLogout_Viagem);
    }

    public Button getLogoutButtonViagemPage(){
        return findViewById(R.id.buttonLogout_Viagem);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}