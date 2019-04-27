package foodget.ihm.foodget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyAccountActivity extends AppCompatActivity {
    Button UpdateMail;
    Button UpdateName;
    Button UpdatePassWord;
    Button Logout;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_acceuil:
                    Intent MainMenuIntent = new Intent(MyAccountActivity.this,MainMenu.class);
                    startActivity(MainMenuIntent);
                    break;

                case R.id.navigation_compte:
                    Intent MyAccountIntent = new Intent(MyAccountActivity.this,MyAccountActivity.class);
                    startActivity(MyAccountIntent);
                    break;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

        UpdateMail = (Button) findViewById(R.id.ModifierMailButton);
        UpdateName = (Button) findViewById(R.id.ModifierPrenomButton);
        UpdatePassWord = (Button) findViewById(R.id.ModifierMDPButton);
        Logout = (Button) findViewById(R.id.LogoutButton);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        UpdateMail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent NewMailIntent= new Intent(MyAccountActivity.this,NewMailActivity.class);
                startActivity(NewMailIntent);
            }
        });
        UpdateName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent NewNameIntent= new Intent(MyAccountActivity.this,NewNameActivity.class);
                startActivity(NewNameIntent);
            }
        });
        UpdatePassWord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent NewPasswordIntent= new Intent(MyAccountActivity.this,NewPasswordActivity.class);
                startActivity(NewPasswordIntent);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent LogOutIntent= new Intent(MyAccountActivity.this,LoginActivity.class);
                startActivity(LogOutIntent);
            }
        });

    }

}
