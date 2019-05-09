package foodget.ihm.foodget.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import foodget.ihm.foodget.DatabaseHelper;
import foodget.ihm.foodget.R;
import foodget.ihm.foodget.activities.LoginActivity;
import foodget.ihm.foodget.activities.NewCartActivity;
import foodget.ihm.foodget.activities.RegisterActivity;
import foodget.ihm.foodget.adapters.FoodListAdapter;
import foodget.ihm.foodget.models.Alert;
import foodget.ihm.foodget.models.Alerts;
import foodget.ihm.foodget.models.Shopping;
import foodget.ihm.foodget.models.User;

public class TabMainMenu extends Fragment {

    String TAG = "MAINMENU";
    DatabaseHelper db;
    EditText add_food;
    EditText add_price;
    TextView welcomeView;
    Button add_data;
    Button btn_cart;
    ArrayList<Shopping> listItem;
    ArrayAdapter adapter;
    ListView shoppingView;
    User currentUser;
    Double total = 0.0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment_tab1
        final View view = inflater.inflate(R.layout.activity_main_menu, container, false);

        db = new DatabaseHelper(getContext());
        welcomeView = view.findViewById(R.id.welcomeView);
        add_data = view.findViewById(R.id.add_data);
        btn_cart = view.findViewById(R.id.btn_cart);
        add_food = view.findViewById(R.id.add_food);
        add_price = view.findViewById(R.id.add_price);
        listItem = new ArrayList<>();
        shoppingView = view.findViewById(R.id.shopping_list);
        currentUser = this.getArguments().getParcelable("user");
        Log.d(TAG, "onCreate: " + currentUser);
        if (currentUser != null) {
            welcomeView.setText("Bonjour " + currentUser.getfName() + "\n" + "Vous avez dépensé " + total + " €");
        }
        viewDataInMenu();
        shoppingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = shoppingView.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "" + text, Toast.LENGTH_SHORT).show();
            }
        });

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String food = add_food.getText().toString().trim();
                String price = add_price.getText().toString().trim();
                Shopping newShopping = new Shopping(food, Double.parseDouble(price));

                if (!food.equals("") && db.addFood(newShopping, currentUser)) {
                    Toast.makeText(getContext(), "data added", Toast.LENGTH_SHORT).show();
                    db.addAlert(new Alert(Alerts.PRODUCT_ADDED.toString().replace("%product%", food)
                            .replace("%price%", price),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/MM/yy HH:mm", Locale.FRANCE))), currentUser);
                    add_food.setText("");
                    add_price.setText("");
                    listItem.clear();
                    viewDataInMenu();

                } else {
                    Toast.makeText(getContext(), "Data not added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(getContext(), NewCartActivity.class);
                cartIntent.putExtra("user",currentUser);
                startActivity(cartIntent);
            }
        });

        return view;
    }

    public void viewDataInMenu() {
        Cursor cursor = db.viewMenuData();
        Log.d(TAG, "onCreate: " + currentUser);
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No data to view", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                System.out.println(currentUser);
                if (cursor.getString(3).equals(currentUser.getUsername())) {
                    Shopping newShopping = new Shopping(cursor.getString(1), cursor.getDouble(2));
                    listItem.add(newShopping);
                    total += newShopping.getPrice();
                    welcomeView.setText("Bonjour " + currentUser.getfName() + "\n" + "Vous avez dépensé " + total + " €");
                    Log.d(TAG, "viewDataInMenu: total " + total + " getPrice() " + newShopping.getPrice());
                } else {
                    Intent ToLoginPageIntent = new Intent(getContext(), LoginActivity.class);
                    Toast.makeText(getContext(), "Erreur de programme. Veuillez vous reconnecter", Toast.LENGTH_SHORT).show();
                    startActivity(ToLoginPageIntent);
                }

            }

            adapter = new ArrayAdapter(getContext(), R.layout.da_food, listItem);
            FoodListAdapter adapterFood = new FoodListAdapter(getContext(), R.layout.da_food, listItem);
            shoppingView.setAdapter(adapterFood);
        }
    }


}
