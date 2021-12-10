package com.example.ecommerce.seller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivitySellerDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class SellerDashboardActivity extends AppCompatActivity {

    Fragment fragment = null;
    ActivitySellerDashboardBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_seller_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.dash_container,new SellerHomeFragment()).commit();
        binding.bottomBar.setItemSelected(R.id.home,true);

        binding.bottomBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.home:{
                        fragment = new SellerHomeFragment();
                        break;
                    } case R.id.add:{
                        startActivity(new Intent(SellerDashboardActivity.this,SellerProductCategoryActivity.class));
                        break;
                    } case R.id.logout:{
                        firebaseAuth.signOut();
                        startActivity(new Intent(SellerDashboardActivity.this,SellerLoginActivity.class));
                    }
                }

                if (fragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.dash_container,fragment).commit();
                }
            }
        });

    }
}
