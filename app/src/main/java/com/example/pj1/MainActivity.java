package com.example.pj1;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pj1.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity implements shopadapter.uid {
    ActivityMainBinding binding;ActionBarDrawerToggle toggle;
    ArrayList<shopmodel> list=new ArrayList<>(); DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        shopadapter shopadapter=new shopadapter(list,getApplicationContext(),this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),
                2,GridLayoutManager.VERTICAL,false);
        binding.shopdisplay.setLayoutManager(gridLayoutManager);
        ref=FirebaseDatabase.getInstance().getReference().child("SHOPS");
        nav();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        list.add(dataSnapshot.getValue(shopmodel.class));
                    }
                    binding.shopdisplay.setAdapter(shopadapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        if(binding.search !=null){
            binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String s) {
                    ref.orderByChild("Landmark").equalTo("Sector-13")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        list.clear();
                                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                            list.add(dataSnapshot.getValue(shopmodel.class));
                                        }
                                        binding.shopdisplay.setAdapter(shopadapter);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                    return true;
                }
            });
        }else{
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        list.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            list.add(dataSnapshot.getValue(shopmodel.class));
                        }
                        binding.shopdisplay.setAdapter(shopadapter);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    public void nav(){
        toggle=new ActionBarDrawerToggle(this,binding.drawer,binding.toolbar,
                R.string.Open,R.string.Close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        binding.navdrw.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Toast.makeText(getApplicationContext(), "HOME OPENED", Toast.LENGTH_LONG).show();
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.myshop:
                        Intent intent2=new Intent(MainActivity.this,myshop.class);
                        startActivity(intent2);
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(MainActivity.this,login.class);
                        startActivity(intent);
                        binding.drawer.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profile:
                        Intent intent1=new Intent(MainActivity.this,profile.class);
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
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void uid(shopmodel shopmodel) {
        startActivity(new Intent(MainActivity.this,details.class).putExtra("UID",shopmodel));
    }
}