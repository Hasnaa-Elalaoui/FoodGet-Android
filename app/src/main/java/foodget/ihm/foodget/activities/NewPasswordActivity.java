package foodget.ihm.foodget.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import foodget.ihm.foodget.DatabaseHelper;
import foodget.ihm.foodget.R;

public class NewPasswordActivity extends AppCompatActivity {
    EditText mTextNewPass;
    EditText mTextConfirmPass;
    EditText mTextOldPass;
    Button mSubmit;
    DatabaseHelper db;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_acceuil:
                    Intent MainMenuIntent = new Intent(NewPasswordActivity.this,MainMenu.class);
                    startActivity(MainMenuIntent);
                    break;

                case R.id.navigation_compte:
                    Intent MyAccountIntent = new Intent(NewPasswordActivity.this,MyAccountActivity.class);
                    startActivity(MyAccountIntent);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        mSubmit=(Button) findViewById(R.id.ConfirmButton) ;
        mTextNewPass=(EditText) findViewById(R.id.textnewpass);
        mTextConfirmPass=(EditText) findViewById(R.id.textnewpassconfirm);
        mTextOldPass=(EditText) findViewById(R.id.textoldpass);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String oldpass=mTextOldPass.getText().toString().trim();
                String newpass=mTextNewPass.getText().toString().trim();
                String newpassconfirm=mTextConfirmPass.getText().toString().trim();
                if (newpass.equals(newpassconfirm)){
                    db.UpdatePassword(oldpass,newpass);
                }

            }
        });

    }


}