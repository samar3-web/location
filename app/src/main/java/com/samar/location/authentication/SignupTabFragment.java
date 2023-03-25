package com.samar.location.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.samar.location.BottomNavigationBar.BootomNavBarMain;
import com.samar.location.databasecontoller.FirebaseDB;
import com.samar.location.models.Customer_Model;
import com.samar.location.models.Owner_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samar.location.R;


public class SignupTabFragment extends Fragment {

    EditText email;
    EditText name;
    EditText lastName;
    EditText pass;
    EditText mobile_no;
    EditText con_pass;
    Button signup;
    RadioButton customer, owner;
    RadioGroup radioGroup;
    String account_type="";
    float v = 0;

    FirebaseDB firebaseDB;
    Customer_Model customerModel;
    Owner_Model ownerModel;
    FirebaseFirestore firebaseFirestore;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment, container, false);

        email = root.findViewById(R.id.email);
        name = root.findViewById(R.id.name);
        lastName = root.findViewById(R.id.lastName);
        pass = root.findViewById(R.id.pass);
        mobile_no = root.findViewById(R.id.mobile_no);
        signup = root.findViewById(R.id.signup);
        con_pass = root.findViewById(R.id.con_pass);
        customer = root.findViewById(R.id.customer_button);
        owner = root.findViewById(R.id.owner_button);
        radioGroup=root.findViewById(R.id.radioGroup);

  //----------------------------------------------------------------------------------------------------------------------------------//
        //creating instance of firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDB = new FirebaseDB();
        customerModel=new Customer_Model();
        ownerModel = new Owner_Model();
//------------------------------------------------------------------------------------------------------------------------//
        email.setTranslationY(300);
        name.setTranslationY(300);
        lastName.setTranslationY(300);
        pass.setTranslationY(300);
        mobile_no.setTranslationY(300);
        con_pass.setTranslationY(300);
        signup.setTranslationY(300);

        email.setAlpha(v);
        name.setAlpha(v);
        lastName.setAlpha(v);
        pass.setAlpha(v);
        mobile_no.setAlpha(v);
        con_pass.setAlpha(v);
        signup.setAlpha(v);

        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        name.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        lastName.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        mobile_no.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        con_pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        signup.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1200).start();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remail = email.getText().toString();
                String rname = name.getText().toString();
                String rlastname = lastName.getText().toString();
                String rpassword = pass.getText().toString();
                String confirmpassword=con_pass.getText().toString();
                String number = mobile_no.getText().toString();

                   RadioButton selectedRadioButton  = (RadioButton)root.findViewById(radioGroup.getCheckedRadioButtonId());
                   try {
                       account_type=selectedRadioButton.getText().toString();
                   }catch (Exception r){}

                if( emailValidator(remail) && nameValidator(rname) && lastNameValidator(rlastname) && passwordValidator(rpassword ,confirmpassword) &&  phoneValidator(number) && accountValidator(customer,owner))
                {


                    firebaseAuth.createUserWithEmailAndPassword(remail,rpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete( Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                if(account_type=="CUSTOMER")
                                {
                                    customerModel.setEmail(remail);
                                    customerModel.setName(rname);
                                    customerModel.setLastName(rlastname);
                                    customerModel.setPassword(confirmpassword);
                                    customerModel.setPhone(number);
                                    customerModel.setAccountType(account_type);
                                    firebaseDB.setData(firebaseFirestore,customerModel, firebaseAuth.getCurrentUser().getUid());

                                }
                                else
                                {
                                    ownerModel.setEmail(remail);
                                    ownerModel.setName(rname);
                                    ownerModel.setLastName(rlastname);
                                    ownerModel.setPassword(confirmpassword);
                                    ownerModel.setPhone(number);
                                    ownerModel.setAccountType(account_type);
                                    firebaseDB.setData(firebaseFirestore,ownerModel ,firebaseAuth.getCurrentUser().getUid());
                                }

                                Toast.makeText(getActivity(), "Signup completed.", Toast.LENGTH_SHORT).show();

                                //Login the New USER
                                loginUser(remail, rpassword,account_type);

                                /*

                                //Request Login

                                TabLayout tabs = (TabLayout)((LoginActivity)getActivity()).findViewById(R.id.tab_layout);

                                tabs.getTabAt(0).select();

                                 */

                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Signup Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }


            }
        });


        return root;


    }

    private boolean emailValidator(String remail) {
            if(remail.isEmpty())
            {email.setError("Cannot be empty"); return false;}
        return true;
    }
    private boolean nameValidator(String rname) {
        if(rname.isEmpty())
        {name.setError("Cannot be empty"); return false;}
        return true;
    }
    private boolean lastNameValidator(String rlastname) {
        if(rlastname.isEmpty())
        {lastName.setError("Cannot be empty"); return false;}
        return true;
    }
    private boolean accountValidator(RadioButton customer, RadioButton owner) {
        if(customer.isChecked()==false && owner.isChecked()==false)
           {
               Toast.makeText(getActivity(), "Choose Owner or Customer", Toast.LENGTH_LONG).show();
               return false;
           }

        return true;
    }

    private boolean phoneValidator(String number) {
        if (number.isEmpty()) {
            mobile_no.setError("Cannot be Empty");
            return false;
        }
        if(number.length()<8)
        {mobile_no.setError("Must be 8 Characters or higher"); return false;}

        return true;
    }

    private boolean passwordValidator(String password, String confirmpassword) {
        if (password.isEmpty()) {
            pass.setError("Cannot be Empty");
            return false;
        }

        if (confirmpassword.isEmpty()) {
            con_pass.setError("Cannot be Empty");
            return false;
        }
        if(password.length() < 8){
            pass.setError("Must be 8 Characters");
            return false;
        }
        if(!password.equals(confirmpassword))
        {    pass.setError("Password must be same");
            con_pass.setError("Password must be same");

            return false;
        }

       return true;

    }

    private void loginUser(String userEmail, String userPassword, String accountType) {

        firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Intent intent = new Intent(getActivity(), BootomNavBarMain.class);
                        intent.putExtra("accountType",accountType);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getActivity(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}




  /*auth.createUserWithEmailAndPassword(remail,rpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
@Override
public void onComplete( Task<AuthResult> task) {
        if(task.isSuccessful())
        {
        Toast.makeText(getActivity(), "Signup completed.", Toast.LENGTH_SHORT).show();
        TabLayout tabs = (TabLayout)((LoginActivity)getActivity()).findViewById(R.id.tab_layout);
        tabs.getTabAt(0).select();
        }
        else
        {
        Toast.makeText(getActivity(), "Failed Failed", Toast.LENGTH_SHORT).show();

        }
        }
        });*/