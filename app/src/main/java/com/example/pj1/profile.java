package com.example.pj1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pj1.databinding.ActivityProfileBinding;
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

public class profile extends AppCompatActivity {
    ActivityProfileBinding binding;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final GoogleSignInAccount user= GoogleSignIn.getLastSignedInAccount(this);
        FirebaseDatabase.getInstance()
                .getReference()
                .child("USERS")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,Object> map= (Map<String, Object>) snapshot.getValue();
                    binding.Name.setText(""+map.get("Name"));
                    binding.EmailAddress.setText(""+map.get("Email"));
                    binding.Address.setText(""+map.get("Address"));
                    binding.Phone.setText(""+map.get("Phone"));
                    Picasso.get().load(map.get("PROFILEPIC")+"").placeholder(R.drawable.ic_user).into(binding.Profile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        binding.EmailAddress.setText(user.getEmail());
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.Name.getText().toString();
                String phone=binding.Phone.getText().toString();
                String address=binding.Address.getText().toString();
                String email=binding.EmailAddress.getText().toString();
                String uid=FirebaseAuth.getInstance().getUid();
                HashMap<String ,Object> obj=new HashMap<>();
                obj.put("Name",name);
                obj.put("Phone",phone);
                obj.put("Address",address);
                obj.put("Email",email);
                obj.put("UID",uid);
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("USERS")
                        .child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(obj)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(profile.this, "PROFILE UPDATED", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        setSupportActionBar(binding.toolbar);
        toggle=new ActionBarDrawerToggle(this,binding.drawer,binding.toolbar,
                R.string.Open,R.string.Close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        binding.navdrw.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent2=new Intent(profile.this,MainActivity.class);
                        startActivity(intent2);
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.myshop:
                        Intent intent1=new Intent(profile.this,myshop.class);
                        startActivity(intent1);
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(profile.this,login.class);
                        startActivity(intent);
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profile:
                        Toast.makeText(getApplicationContext(), "PROFILE OPENED", Toast.LENGTH_LONG).show();
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "DRAWER CLOSE", Toast.LENGTH_SHORT).show();
                        binding.drawer.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
        upload();
    }
    public void upload(){
        binding.Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,13);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            Uri img1=data.getData();
            binding.Profile.setImageURI(img1);
            final StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                    .child("Profile")
                    .child(FirebaseAuth.getInstance().getUid());
            storageReference.putFile(img1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("USERS")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("PROFILEPIC")
                                    .setValue(uri.toString());
                            Toast.makeText(profile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}