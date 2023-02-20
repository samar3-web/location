package com.samar.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    AsiaFoodAdapter asiaFoodAdapter;
    private RecyclerView productsRv, asia_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Home> asiaFoodList = new ArrayList<>();
        asiaFoodList.add(new Home("Appart", "7600 TND", R.drawable.home1, "The iPhone 14 Pro Max display has rounded corners that follow a beautiful curved design, and these corners are within a standard rectangle.", "Loisir ","55432325","4.5","istore"));
        asiaFoodList.add(new Home(" villa ", "120 TND", R.drawable.home2, "Montre Raymond Daniel", "Loisir","55832098","4.2","City Watch"));
        asiaFoodList.add(new Home(" Duplex", "1500 TND", R.drawable.home3, "", "Maison et jardin","12345678","3.2","Tunisia MAll"));

        setAsiaRecycler(asiaFoodList);
    }
    private void setAsiaRecycler(List<Home> asiaFoodList) {

        asia_recycler = findViewById(R.id.productsRv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        asia_recycler.setLayoutManager(layoutManager);
        asiaFoodAdapter = new AsiaFoodAdapter(this, asiaFoodList);
        asia_recycler.setAdapter(asiaFoodAdapter);

    }
}