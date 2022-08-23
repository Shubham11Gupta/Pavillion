package com.example.pj1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import com.example.pj1.databinding.ActivityDetailsBinding;
import com.squareup.picasso.Picasso;

public class details extends AppCompatActivity {
    ActivityDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent= getIntent();
        if(intent.getExtras()!=null){
            shopmodel shopmodel=(com.example.pj1.shopmodel)intent.getSerializableExtra("UID");
            Picasso.get().load(shopmodel.getImage1()).into(binding.imageView);
            Picasso.get().load(shopmodel.getImage2()).into(binding.imageView2);
            String data=shopmodel.getName()+"\n"
                    +shopmodel.getAddress()+"\n"+shopmodel.getLandmark()+"\n"+shopmodel.getEmail()+"\n"+
                    shopmodel.getPhone()+"\n"+shopmodel.getCategory();
            binding.detail.setText(data);
            binding.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu=new PopupMenu(details.this,binding.menu);
                    popupMenu.getMenuInflater().inflate(R.menu.menudetils,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.call:
                                    Intent intent1=new Intent(Intent.ACTION_CALL);
                                    intent1.setData(Uri.parse("tel:"+shopmodel.getPhone()));
                                    startActivity(intent1);
                                    Toast.makeText(details.this, "calling "+shopmodel.getPhone(), Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });
        }
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(details.this,MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}