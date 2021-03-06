package foodget.ihm.foodget.fragments;

import android.app.Dialog;
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
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import foodget.ihm.foodget.BluetoothActivity;
import foodget.ihm.foodget.DatabaseHelper;
import foodget.ihm.foodget.OnClickInMyAdapterListener;
import foodget.ihm.foodget.R;
import foodget.ihm.foodget.activities.LoginActivity;
import foodget.ihm.foodget.activities.MyCartActivity;
import foodget.ihm.foodget.activities.StatsDisplayActivity;
import foodget.ihm.foodget.adapters.FoodListAdapter;
import foodget.ihm.foodget.models.Alert;
import foodget.ihm.foodget.models.Alerts;
import foodget.ihm.foodget.models.Shopping;
import foodget.ihm.foodget.models.ShoppingList;
import foodget.ihm.foodget.models.User;

public class TabMainMenu extends Fragment implements OnClickInMyAdapterListener {

    String TAG = "MAINMENU";
    DatabaseHelper db;
    EditText add_food;
    EditText add_price;
    TextView welcomeView;
    Button add_data;
    Button btn_cart;
    Button set_threshold;
    Button btn_stats;
    List<Shopping> listItem;
    ArrayAdapter adapter;
    ListView shoppingView;
    User currentUser;
    Button deleteFood;
    Double total = 0.0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment_tab1
        final View view = inflater.inflate(R.layout.activity_main_menu, container, false);

        db = new DatabaseHelper(getContext());
        welcomeView = view.findViewById(R.id.welcomeView);
        add_data = view.findViewById(R.id.add_data);
        btn_cart = view.findViewById(R.id.btn_cart);
        btn_stats = view.findViewById(R.id.btn_stats);
        deleteFood = view.findViewById(R.id.deleteFood);
        listItem = new ArrayList<>();
        shoppingView = view.findViewById(R.id.food_list);
        set_threshold = view.findViewById(R.id.set_threshold);
        currentUser = this.getArguments().getParcelable("user");
        //this.bluetoothConnectionService = new BluetoothConnectionService(getContext(), currentUser, db);
        Log.d(TAG, "onCreate: " + currentUser);
        viewDataInMenu();


        shoppingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = shoppingView.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "" + text, Toast.LENGTH_SHORT).show();

                Intent bluetoothIntent = new Intent(getContext(), BluetoothActivity.class);
                bluetoothIntent.putExtra("user", currentUser);
                startActivity(bluetoothIntent);
            }
        });


        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(getContext(), MyCartActivity.class);
                cartIntent.putExtra("user", currentUser);
                startActivity(cartIntent);
            }
        });

        btn_stats.setOnClickListener(v -> {
            Intent statsIntent = new Intent(getContext(), StatsDisplayActivity.class);
            statsIntent.putExtra("user", currentUser);
            startActivity(statsIntent);
        });

        Dialog popupThreshold = new Dialog(getContext());
        set_threshold.setOnClickListener((View v) -> {
            popupThreshold.setContentView(R.layout.new_threshold);

            Button add = popupThreshold.findViewById(R.id.add_dataButton_threshold);
            EditText threshold = popupThreshold.findViewById(R.id.ThreshInput);
            Button cancel = popupThreshold.findViewById(R.id.cancelButton_threshold);

            add.setOnClickListener((View v1) -> {
                String thresh = threshold.getText().toString().trim();
                if (!thresh.equals("")) {
                    currentUser.setThreshold(Integer.parseInt(thresh));
                    popupThreshold.dismiss();
                    welcomeView.setText(getString(R.string.welcome)
                            .replace("%username%", currentUser.getfName())
                            .replace("%money%", String.format(Locale.FRANCE, "%.2f", total))
                            .concat(getString(R.string.threshold)
                                    .replace("%threshold%", "" + currentUser.getThreshold())));
                } else {
                    Toast.makeText(getContext(), "ERREUR ! Veuillez rentrer les informations demandées.", Toast.LENGTH_SHORT).show();
                }

            });

            cancel.setOnClickListener((View v1) -> {
                popupThreshold.dismiss();
            });

            popupThreshold.show();


        });

        Dialog popupAddSpentMoney = new Dialog(getContext());
        add_data.setOnClickListener((View v) -> {
            popupAddSpentMoney.setContentView(R.layout.adding_data_popup);

            Button add_dataButton = popupAddSpentMoney.findViewById(R.id.add_dataButton);
            Button cancelButton = popupAddSpentMoney.findViewById(R.id.cancelButton);
            EditText priceInput = popupAddSpentMoney.findViewById(R.id.priceInput);
            EditText productInput = popupAddSpentMoney.findViewById(R.id.productInput);

            cancelButton.setOnClickListener((View v1) -> popupAddSpentMoney.dismiss());

            add_dataButton.setOnClickListener((View v2) -> {
                Log.d(TAG, "TENTATIVE AJOUT");
                String food = productInput.getText().toString().trim();
                String price = priceInput.getText().toString().trim();
                Shopping newShopping = null;
                if (!food.equals("") && !price.equals("")) {
                    newShopping = new Shopping(food, Double.parseDouble(price), LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/MM/yy HH:mm", Locale.FRANCE)));
                } else {
                    Toast.makeText(getContext(), "ERREUR ! Veuillez rentrer les informations demandées.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "on avance");

                if (db.addFood(newShopping, currentUser)) {
                    Toast.makeText(getContext(), "Produit ajouté !", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, " AJOUT");
                    db.addAlert(new Alert(Alerts.PRODUCT_ADDED.toString().replace("%product%", food)
                            .replace("%price%", price),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/MM/yy HH:mm", Locale.FRANCE))), currentUser);
                    if ((total + newShopping.getPrice()) > currentUser.getThreshold()) {
                        db.addAlert(new Alert(Alerts.THRESHOLD_OVER.toString().replace("%over%",
                                String.valueOf((total + newShopping.getPrice()) - currentUser.getThreshold())),
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/MM/yy HH:mm", Locale.FRANCE))), currentUser);
                    }
                    listItem.clear();
                    viewDataInMenu();

                } else {
                    Toast.makeText(getContext(), "Erreur, produit non ajouté...", Toast.LENGTH_SHORT).show();
                }
                popupAddSpentMoney.dismiss();
            });
            popupAddSpentMoney.show();
        });

        return view;
    }

    public void viewDataInMenu() {
        Cursor cursor = db.viewMenuData(currentUser);
        total = 0.0;
        listItem.clear();
        Log.d(TAG, "onCreate: " + currentUser);
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "Votre liste est vide !", Toast.LENGTH_SHORT).show();
            listItem.clear();
            total = 0.0;
            welcomeView.setText(getString(R.string.welcome)
                    .replace("%username%", currentUser.getfName())
                    .replace("%money%", String.format(Locale.FRANCE, "%.2f", total))
                    .concat(getString(R.string.threshold)
                            .replace("%threshold%", "" + currentUser.getThreshold())));
        } else {
            while (cursor.moveToNext()) {
                System.out.println(currentUser);
                if (cursor.getString(3).equals(currentUser.getUsername())) {
                    Shopping newShopping = new Shopping(cursor.getString(1), cursor.getDouble(2), cursor.getString(4));
                    System.out.println(cursor.getString(4));
                    System.out.println(newShopping.getDateAsDate().toString());
                    listItem.add(newShopping);
                    if (newShopping.getDateAsDate().isAfter(LocalDateTime.now().minusMonths(1))) {
                        total += newShopping.getPrice();
                    }
                    if (currentUser.getThreshold() > 0) {
                        welcomeView.setText(getString(R.string.welcome)
                                .replace("%username%", currentUser.getfName())
                                .replace("%money%", String.format(Locale.FRANCE, "%.2f", total))
                                .concat(getString(R.string.threshold)
                                        .replace("%threshold%", "" + currentUser.getThreshold())));
                    } else {
                        welcomeView.setText(getString(R.string.welcome)
                                .replace("%username%", currentUser.getfName())
                                .replace("%money%", String.format(Locale.FRANCE, "%.2f", total)));
                    }
                    Log.d(TAG, "viewDataInMenu: total " + total + " getPrice() " + newShopping.getPrice());
                } else {
                    Intent ToLoginPageIntent = new Intent(getContext(), LoginActivity.class);
                    Toast.makeText(getContext(), "Erreur de programme. Veuillez vous reconnecter", Toast.LENGTH_SHORT).show();
                    startActivity(ToLoginPageIntent);
                }

            }


            listItem.sort(Comparator.comparing(Shopping::getDateAsDate).reversed());
            listItem = listItem.stream().filter(shopping -> shopping.getDateAsDate().isAfter(LocalDateTime.now().minusMonths(1)))
                    .collect(Collectors.toList());

            //adapter = new ArrayAdapter(getContext(), R.layout.da_food, listItem);
            FoodListAdapter adapterFood = new FoodListAdapter(getContext(), R.layout.da_food, listItem, this);
            shoppingView.setAdapter(adapterFood);
        }
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public void onItemclicked() {
        Log.d(TAG, "Testing Interface....");
        viewDataInMenu();
    }

    @Override
    public ShoppingList getShoppingList() {
        return null;
    }
}
