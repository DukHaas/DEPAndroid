package com.example.depeat.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.depeat.R;
import com.example.depeat.datamodels.Food;
import com.example.depeat.datamodels.Restaurant;
import com.example.depeat.ui.activities.adapters.FoodAdapter;
import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity implements FoodAdapter.OnQuantityChangedListener {

    //RecyclerView component
    private RecyclerView foodRV;
    private RecyclerView.LayoutManager foodLayoutManager;
    private FoodAdapter foodAdapter;

    //UI component
    private ImageView imageResturant;
    private TextView nameRestaurant, addressRestaurant, totalPrice;
    private Button buttonCheckout;
    private ProgressBar progressBar;
    private TextView minOrder;


    //data model
    private Restaurant restaurant;

    private float total = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Modifica grafica
        ShopActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        ShopActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setTitle("Menu");

        setContentView(R.layout.activity_shop);
        nameRestaurant = findViewById(R.id.name_restaurant);
        addressRestaurant = findViewById(R.id.address_restaurant);
        totalPrice = findViewById(R.id.id_total_number);
        minOrder = findViewById(R.id.minOrderNumber);

        buttonCheckout = findViewById(R.id.id_buttonCheckout);
        progressBar = findViewById(R.id.determinateBar);
        foodRV = findViewById(R.id.list_food);
        restaurant = getRestaurant();
        restaurant.setFoods(getFood());

        nameRestaurant.setText(restaurant.getNome());
        addressRestaurant.setText(restaurant.getIndirizzo());
        progressBar.setMax((int)restaurant.getMinimoOrdine() * 100);
        minOrder.setText(String.valueOf(restaurant.getMinimoOrdine()));

        binData();
        foodLayoutManager = new LinearLayoutManager(this);
        foodAdapter = new FoodAdapter(this, restaurant.getFoods());
        foodAdapter.setOnQuantityChangedListener(this);

        foodRV.setLayoutManager(foodLayoutManager);
        ((SimpleItemAnimator) foodRV.getItemAnimator()).setSupportsChangeAnimations(false);
        foodRV.setAdapter(foodAdapter);


        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopActivity.this, CheckoutActivity.class));
            }
        });


    }

    //TODO hardcore
    private void binData(){
        restaurant = getRestaurant();
        restaurant.setImageId("https://rovato5stelle.files.wordpress.com/2013/11/mcdonald.jpg");
        restaurant.setFoods(getFood());
        nameRestaurant.setText(restaurant.getNome());
        addressRestaurant.setText(restaurant.getIndirizzo());
        Glide.with(this).load(restaurant.getImageId()).into(imageResturant);
        progressBar.setMax((int)restaurant.getMinimoOrdine() * 100);


    }

    //TODO hardcore
    private Restaurant getRestaurant(){
        return new Restaurant("NomeRistorante", "Via Sandro Sandri", 50.00f);
    }

    //TODO hardcore
    private ArrayList<Food> getFood(){
        ArrayList<Food> food = new ArrayList<>();
        food.add(new Food("nome", 6.00f));
        food.add(new Food("nome", 6.00f));
        food.add(new Food("nome", 6.00f));
        food.add(new Food("nome", 6.00f));
        food.add(new Food("nome", 6.00f));
        food.add(new Food("nome", 6.00f));
        food.add(new Food("nome", 6.00f));
        return food;
    }

    private void updateTotal(float item){
        total = total+item;
        totalPrice.setText(String.valueOf(total));
    }

    private void updateProgressBar(int progress){
        progressBar.setProgress(progress);
    }

    private void enableButton(){
        buttonCheckout.setEnabled(total >= restaurant.getMinimoOrdine());
    }


    @Override
    public void onChange(float priceFood) {
        updateTotal(priceFood);
        updateProgressBar((int)total*100);
        enableButton();
    }
}
