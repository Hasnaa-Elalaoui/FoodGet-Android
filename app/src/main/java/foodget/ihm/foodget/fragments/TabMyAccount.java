package foodget.ihm.foodget.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import foodget.ihm.foodget.R;
import foodget.ihm.foodget.activities.CameraActivity;
import foodget.ihm.foodget.activities.LoginActivity;
import foodget.ihm.foodget.activities.NewMailActivity;
import foodget.ihm.foodget.activities.NewNameActivity;
import foodget.ihm.foodget.activities.NewPasswordActivity;
import foodget.ihm.foodget.models.User;

public class TabMyAccount extends Fragment {

    Button UpdatePhoto;
    Button UpdateMail;
    Button UpdateName;
    Button UpdatePassWord;
    Button Logout;
    User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment_tab1
        final View view = inflater.inflate(R.layout.activity_myaccount, container, false);

        UpdatePhoto = view.findViewById(R.id.ModifierPhotoButton);
        UpdateMail = view.findViewById(R.id.ModifierMailButton);
        UpdateName = view.findViewById(R.id.ModifierPrenomButton);
        UpdatePassWord = view.findViewById(R.id.ModifierMDPButton);
        Logout = view.findViewById(R.id.LogoutButton);
        currentUser = this.getArguments().getParcelable("user");

        UpdatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPhotoIntent = new Intent(getContext(), CameraActivity.class);
                newPhotoIntent.putExtra("USER", currentUser);
                startActivity(newPhotoIntent);
            }
        });

        UpdateMail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent NewMailIntent= new Intent(getContext(), NewMailActivity.class);
                NewMailIntent.putExtra("USER", currentUser);
                startActivity(NewMailIntent);
            }
        });

        UpdateName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent NewNameIntent= new Intent(getContext(), NewNameActivity.class);
                NewNameIntent.putExtra("USER", currentUser);
                startActivity(NewNameIntent);
            }
        });

        UpdatePassWord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent NewPasswordIntent= new Intent(getContext(), NewPasswordActivity.class);
                NewPasswordIntent.putExtra("USER", currentUser);
                startActivity(NewPasswordIntent);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent LogOutIntent= new Intent(getContext(), LoginActivity.class);
                startActivity(LogOutIntent);
            }
        });

        return view;
    }
}
