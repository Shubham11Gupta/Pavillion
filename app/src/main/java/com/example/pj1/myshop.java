package com.example.pj1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pj1.databinding.ActivityMyshopBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class myshop extends AppCompatActivity {
    ActivityMyshopBinding binding;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyshopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        drawer();
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.name.getText().toString();
                String phone=binding.phone.getText().toString();
                String address=binding.address.getText().toString();
                String email=binding.email.getText().toString();
                String landmark=binding.landmark.getText().toString();
                String category=binding.category.getText().toString();
                String uid=FirebaseAuth.getInstance().getUid();
                HashMap<String ,Object> obj=new HashMap<>();
                obj.put("Name",name);
                obj.put("Phone",phone);
                obj.put("Address",address);
                obj.put("Email",email);
                obj.put("Landmark",landmark);
                obj.put("Category",category);
                obj.put("UID",uid);
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("SHOPS")
                        .child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(obj)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "SHOP UPDATED", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        retrive();
        uploadimage();
    }
    public void uploadimage(){
        binding.addimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,13);
            }
        });
        binding.addimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent();
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                startActivityForResult(intent2,14);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==13){
            Uri img1=data.getData();
            binding.addimg1.setImageURI(img1);
            final StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                    .child("Shop_Img")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("1");
            storageReference.putFile(img1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("SHOPS")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("Image1")
                                    .setValue(uri.toString());
                            Toast.makeText(myshop.this, "First Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else{
            Uri img2=data.getData();
            binding.addimg2.setImageURI(img2);
            final StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                    .child("Shop_Img")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("2");
            storageReference.putFile(img2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("SHOPS")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("Image2")
                                    .setValue(uri.toString());
                            Toast.makeText(myshop.this, "Second Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });;
        }
    }
    public void drawer(){
        toggle=new ActionBarDrawerToggle(this,binding.drawer,binding.toolbar,
                R.string.Open,R.string.Close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        binding.navdrw.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent2=new Intent(myshop.this,MainActivity.class);
                        startActivity(intent2);
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.myshop:
                        Toast.makeText(getApplicationContext(), "MY SHOP OPENED", Toast.LENGTH_LONG).show();
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(myshop.this,login.class);
                        startActivity(intent);
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profile:
                        Intent intent1=new Intent(myshop.this,profile.class);
                        startActivity(intent1);
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "DRAWER CLOSE", Toast.LENGTH_SHORT).show();
                        binding.drawer.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
    }
    public void retrive(){
        GoogleSignInAccount user= GoogleSignIn.getLastSignedInAccount(this);
        binding.email.setText(user.getEmail());
        FirebaseDatabase.getInstance()
                .getReference()
                .child("SHOPS")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Map<String,Object> map= (Map<String, Object>) snapshot.getValue();
                            binding.name.setText(""+map.get("Name"));
                            binding.email.setText(""+map.get("Email"));
                            binding.address.setText(""+map.get("Address"));
                            binding.phone.setText(""+map.get("Phone"));
                            binding.landmark.setText(""+map.get("Landmark"));
                            binding.category.setText(""+map.get("Category"));
                            Picasso.get().load(map.get("Image1")+"").placeholder(R.drawable.ic_add_image).into(binding.addimg1);
                            Picasso.get().load(map.get("Image2")+"").placeholder(R.drawable.ic_add_image).into(binding.addimg2);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}
