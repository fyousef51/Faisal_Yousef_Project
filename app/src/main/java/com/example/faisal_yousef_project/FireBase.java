package com.example.faisal_yousef_project;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.HashMap;
import java.util.Map;

public class FireBase extends AppCompatActivity {



    Button button_fb_update;
    Button button_fb_delete;
    Button button_fb_insert;
    Button button_fb_select;

    EditText input_fb_phone;
    EditText input_fb_fname;
    EditText input_fb_lname;
    EditText input_fb_email;
    EditText input_fb_id;
    RequestQueue rq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base);
        setup();
        rq= Volley.newRequestQueue(this);
        rq.add(Helper.weather(this));

        input_fb_fname =findViewById(R.id.input_fb_fname);
        input_fb_lname =findViewById(R.id.input_fb_lname);
        input_fb_phone=findViewById(R.id.input_fb_phone);
        input_fb_email =findViewById(R.id.input_fb_email);
        input_fb_id =findViewById(R.id.input_fb_uid);

        button_fb_delete =findViewById(R.id.btn_delete);
        button_fb_update =findViewById(R.id.button_firebase_update);
        button_fb_insert =findViewById(R.id.button_firebase_insert);
        button_fb_select =findViewById(R.id.btn_go_select);

        button_fb_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id= input_fb_id.getText()+"";
                if (id.isEmpty()){
                    Toast.makeText(FireBase.this,"Please fill in the uid field for this operation.",Toast.LENGTH_SHORT).show();
                    return;
                }
                delete(Integer.valueOf(id));
            }
        });
        button_fb_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FireBase.this,FireBaseList.class));
            }
        });



        button_fb_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone=input_fb_phone.getText()+"";
                String email= input_fb_email.getText()+"";
                String id= input_fb_id.getText()+"";
                String fname= input_fb_fname.getText()+"";
                String lname= input_fb_lname.getText()+"";

                if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || email.isEmpty() || id.isEmpty()){
                    Toast.makeText(FireBase.this,"Please fill all fields to start the operation.",Toast.LENGTH_SHORT).show();
                    return;
                }
                insertUser(email,fname,lname,phone,Integer.valueOf(id));
            }
        });

        button_fb_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone=input_fb_phone.getText()+"";
                String email= input_fb_email.getText()+"";
                String fname= input_fb_fname.getText()+"";
                String lname= input_fb_lname.getText()+"";
                String id= input_fb_id.getText()+"";
                HashMap<String,Object> map=new HashMap<>();

                if (id.isEmpty()){
                    Toast.makeText(FireBase.this,"Please the ID for this operation(update)",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!phone.isEmpty()){
                    map.put("phoneNumber",phone);
                }
                if (!email.isEmpty()){
                    map.put("emailAddress",email);
                }
                if (!id.isEmpty()){
                    map.put("userId",Integer.valueOf(id));
                }
                if (!fname.isEmpty()){
                    map.put("firstName",fname);
                }
                if (!lname.isEmpty()){
                    map.put("lastName",lname);
                }
                update(Integer.valueOf(id),map);
            }
        });


    }

    FirebaseDatabase database;
    DatabaseReference ref;

    private void setup(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

    }

    private void insertUser(String email,String fName,String lName,String phone,int userId){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==userId){
                        Toast.makeText(FireBase.this,"The record with ID "+userId+" was found.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                int initialCount=(int)snapshot.getChildrenCount();
                while(snapshot.hasChild(initialCount+"")){
                    initialCount++;
                }
                DatabaseReference insertRef=ref.child(initialCount+"");


                insertRef.child("emailAddress").setValue(email);
                insertRef.child("firstName").setValue(fName);
                insertRef.child("lastName").setValue(lName);
                insertRef.child("phoneNumber").setValue(phone);
                insertRef.child("userId").setValue(userId);
                Toast.makeText(FireBase.this,"Inserted successfully.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateUserAllDetails(String email,String fName,String lName,String phone,int userId){
        Map<String,Object> stringObjectMap=new HashMap<>();
        stringObjectMap.put("emailAddress",email);
        stringObjectMap.put("firstName",fName);
        stringObjectMap.put("lastName",lName);
        stringObjectMap.put("phoneNumber",phone);
        stringObjectMap.put("userId",userId);

        update(userId,stringObjectMap);
    }


    private void update(int uid,Map<String,Object> keyValMap){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==uid){
                        ref.child(child.getKey()).updateChildren(keyValMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FireBase.this,"Record is now Updated",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FireBase.this,"Error: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }
                Toast.makeText(FireBase.this,"No user with this ID was found.",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void delete(int uid){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==uid){
                        ref.child(child.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FireBase.this,"Record was deleted successfully.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FireBase.this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                        return;
                    }
                }
                Toast.makeText(FireBase.this,"No user with this ID was found",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}

@IgnoreExtraProperties
class User{


    int userId;

    String firstName;
    String lastName;
    String emailAddress;
    String phoneNumber;

    public User(){

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}