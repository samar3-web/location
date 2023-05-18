package com.samar.location.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.samar.location.BottomNavigationBar.BootomNavBarMain;
import com.samar.location.R;


public class LoginTabFragment extends Fragment {
    EditText email;
    EditText pass;
    TextView forgotPass, loginas;
    Button login;
    RadioGroup radioGroup;
    float v = 0;
    String accountType;
    RadioButton lg_customer, lg_owner;
    FirebaseAuth firebaseAuth;
    Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_franment, container, false);

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.pass);
        forgotPass = root.findViewById(R.id.forgotPass);
        login = root.findViewById(R.id.button_login);


        Log.d("xxxx", "onCreateView: login account type " + accountType);

        //Creating instancce of firebase auth
        firebaseAuth = FirebaseAuth.getInstance();


        email.setTranslationY(300);
        pass.setTranslationY(300);
        forgotPass.setTranslationY(300);
        login.setTranslationY(300);
        email.setAlpha(v);
        pass.setAlpha(v);
        forgotPass.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        forgotPass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        login.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v,"This is snakebar",Snackbar.LENGTH_LONG).show();

                String remail = email.getText().toString();
                String rpass = pass.getText().toString();

                if (validateEmailAndPassword(remail, rpass))
                    loginUser(remail, rpass);


            }
        });

        return root;


    }

    private boolean validateEmailAndPassword(String remail, String rpass) {

        if (remail.isEmpty() || rpass.isEmpty()) {
            email.setError("Cannot be empty");
            pass.setError("Cannot be empty");
            return false;
        }

        return true;
    }


    private void loginUser(String remail, String rpass) {
        firebaseAuth.signInWithEmailAndPassword(remail, rpass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getActivity(), "Logging in", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getActivity(), BootomNavBarMain.class);
                        intent.putExtra("accountType", accountType);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
