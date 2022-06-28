package com.example.food;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Menu_Page extends AppCompatActivity {
    RecyclerView rcView;
    Button cart,order;
    Menu_Adapter adapter;

    FirebaseRecyclerOptions<MenuModal> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        rcView=findViewById(R.id.recycler_menu);
        cart=findViewById(R.id.cart);
        order=findViewById(R.id.order);
        Intent intent=getIntent();
        String ID=intent.getStringExtra("ID").toString();

        options=new FirebaseRecyclerOptions.Builder<MenuModal>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("menu").orderByChild("restro_id").equalTo(ID), MenuModal.class)
                .build();


        rcView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Menu_Adapter(options);
        rcView.setAdapter(adapter);

        cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int cartTotal=0;
                ArrayList<OrderModal> arr=new ArrayList();
                for(String keys:Menu_Adapter.map.keySet()){
                    arr.add(Menu_Adapter.map.get(keys));
                    cartTotal+=Menu_Adapter.map.get(keys).getTotalPrice();
                }
//                for(int i=0;i<arr.size();i++){
//                    Log.i("data",""+arr.get(i));
//                }
                Intent intent=new Intent(Menu_Page.this,Cart_Details.class);
                Bundle args = new Bundle();
                args.putSerializable("array",(Serializable)arr);
                intent.putExtra("BUNDLE",args);
                intent.putExtra("cartTotal",cartTotal);
                startActivity(intent);
            }

        });

        order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int cartTotal=0;
                ArrayList<OrderModal> arr=new ArrayList();
                for(String keys:Menu_Adapter.map.keySet()){
                    arr.add(Menu_Adapter.map.get(keys));
                    cartTotal+=Menu_Adapter.map.get(keys).getTotalPrice();
                }
                Intent intent=new Intent(Menu_Page.this,HomeActivity.class);
                intent.putExtra("total",cartTotal);
                startActivity(intent);
                Toast.makeText(Menu_Page.this, "go to login", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
     }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}