package com.example.ecommerce.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ecommerce.Dialog;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivitySellerLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    ActivitySellerLoginBinding binding;
    FirebaseAuth firebaseAuth;
    String mail, pass;
    Dialog dialog = new Dialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seller_login);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellerLogin();
            }
        });


    }

    private void sellerLogin() {
        dialog.loadDialog(SellerLoginActivity.this);
        mail = binding.sellerMail.getText().toString();
        pass = binding.sellerPass.getText().toString();



        firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(SellerLoginActivity.this,"Logedin successfully",Toast.LENGTH_LONG).show();
                dialog.dismisDialog();
                startActivity(new Intent(SellerLoginActivity.this,SellerDashboardActivity.class));
                finish();
            }
        });
    }
}
