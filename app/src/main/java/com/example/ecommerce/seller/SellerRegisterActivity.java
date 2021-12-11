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
import com.example.ecommerce.databinding.ActivitySellerRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySellerRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    String mail, pass, phone, name, address;
    Dialog dialog = new Dialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seller_register);

        firebaseAuth = FirebaseAuth.getInstance();
        binding.sellerLoginBtn.setOnClickListener(this);
        binding.sellerRegBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.seller_login_btn: {
                startActivity(new Intent(SellerRegisterActivity.this, SellerLoginActivity.class));
                break;
            }
            case R.id.seller_reg_btn: {
                createAccount();
                dialog.loadDialog(SellerRegisterActivity.this);
                break;
            }
        }
    }

    private void createAccount() {
        mail = binding.sellerMail.getText().toString();
        pass = binding.sellerPass.getText().toString();
        phone = binding.sellerPhone.getText().toString();
        name = binding.sellerName.getText().toString();
        address = binding.sellerAddress.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String uId = firebaseAuth.getCurrentUser().getUid();
                        final DatabaseReference databaseReference;
                        databaseReference = FirebaseDatabase.getInstance().getReference();

                        HashMap<String, Object> sellerAccount = new HashMap<>();
                        sellerAccount.put("sId", uId);
                        sellerAccount.put("name", name);
                        sellerAccount.put("email", mail);
                        sellerAccount.put("address", address);
                        sellerAccount.put("phone", phone);

                        databaseReference.child("Sellers").child(uId)
                                .updateChildren(sellerAccount).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SellerRegisterActivity.this, "account created", Toast.LENGTH_LONG).show();
                                dialog.dismisDialog();
                                startActivity(new Intent(SellerRegisterActivity.this,SellerLoginActivity.class));
                            }
                        });


                    }
                });
    }
}
