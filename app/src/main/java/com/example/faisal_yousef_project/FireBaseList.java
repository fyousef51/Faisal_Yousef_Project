package com.example.faisal_yousef_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseList extends AppCompatActivity {


    EditText inp_select_uid;
    ListView lv;

    ArrayList<User> users=new ArrayList<>();
    ArrayList<User> fetchedUsers=new ArrayList<>();

    int uidToDisplay=-1; //-1 means all
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_list);

        setup();


        inp_select_uid=findViewById(R.id.input_select_uid);

        TextWatcher tw =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    uidToDisplay=-1;
                }else{
                    uidToDisplay=Integer.valueOf(s.toString());
                }
                update();
            }
        };
        inp_select_uid.addTextChangedListener(tw);


        lv=findViewById(R.id.lv_firebase);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return users.size();
            }

            @Override
            public Object getItem(int position) {
                return users.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                User u=users.get(position);
                System.out.println(u);
                TableLayout table=(TableLayout) LayoutInflater.from(FireBaseList.this).inflate(R.layout.list_item,parent,false);

                TextView out_uid=table.findViewById(R.id.output_firebase_uid);
                TextView out_name=table.findViewById(R.id.out_fb_name);
                TextView out_phone=table.findViewById(R.id.output_firebase_phone);
                TextView out_email=table.findViewById(R.id.output_firebase_email);
                out_uid.setText(""+u.userId);
                out_name.setText(u.firstName+" "+u.lastName);
                out_phone.setText(u.phoneNumber);
                out_email.setText(u.emailAddress);

                return table;
            }


        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get all users
                fetchedUsers=snapshot.getValue(new GenericTypeIndicator<ArrayList<User>>() {});
                //remove null objects
                ArrayList<User> newUsersList=new ArrayList<>();
                for (User u:fetchedUsers){
                    if (u!=null){
                        newUsersList.add(u);
                    }
                }
                fetchedUsers=newUsersList;
                update();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FETCH",error.getMessage());
                Toast.makeText(FireBaseList.this,"Error: "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }
    FirebaseDatabase database;
    DatabaseReference ref;

    private void setup(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

    }

    private void update(){
        if (uidToDisplay!=-1){
            for (User u:fetchedUsers) {
                if (uidToDisplay==u.getUserId()){
                    users.clear();
                    users.add(u);
                }
            }
        }else{
            users.clear();
            users.addAll(fetchedUsers);
        }
        ((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
    }


}