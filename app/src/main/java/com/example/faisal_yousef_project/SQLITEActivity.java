package com.example.faisal_yousef_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SQLITEActivity extends AppCompatActivity {


    EditText input_sql_fname;
    EditText input_sql_lname;
    EditText input_sql_phone;
    EditText input_sql_email;
    EditText input_sql_id;


    Button btn_select_options;
    Button btn_delete;
    Button btn_insert;
    Button btn_insert_from_firebase;
    Button btn_update;

    FirebaseDatabase database;
    DatabaseReference ref;

    private void setup(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_q_l_i_t_e);
        setup();
        DBHelper dbHelper=new DBHelper(this);

        input_sql_fname =findViewById(R.id.inp_sql_fname);
        input_sql_lname =findViewById(R.id.inp_sql_lname);
        input_sql_phone =findViewById(R.id.inp_sql_phone);
        input_sql_email =findViewById(R.id.inp_email);
        input_sql_id =findViewById(R.id.inp_uid);

        btn_insert=findViewById(R.id.btn_sql_insert);
        btn_insert_from_firebase=findViewById(R.id.button_sl_insert_from_fb);
        btn_update=findViewById(R.id.btn_sql_update);
        btn_select_options=findViewById(R.id.btn_sql_select_options);
        btn_delete=findViewById(R.id.btn_sql_delete);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String phone= input_sql_phone.getText()+"";
                String email= input_sql_email.getText()+"";
                String id= input_sql_id.getText()+"";
                String fname= input_sql_fname.getText()+"";
                String lname= input_sql_lname.getText()+"";

                if (
                        fname.isEmpty() ||
                                lname.isEmpty() ||
                                phone.isEmpty() ||
                                email.isEmpty() ||
                                id.isEmpty()){
                    Toast.makeText(SQLITEActivity.this,"Please fill in all fields to start this operation.",Toast.LENGTH_SHORT).show();
                }

                int i=dbHelper.insert(fname,lname,phone,email,Integer.valueOf(id));
                if (i!=-1){
                    Toast.makeText(SQLITEActivity.this,"Record is inserted",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SQLITEActivity.this,"Record was not inserted.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_insert_from_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id= input_sql_id.getText()+"";
                if (id.isEmpty() ){
                    Toast.makeText(SQLITEActivity.this,"Please fill in ID field for this operation",Toast.LENGTH_SHORT).show();
                    return;
                }
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childData:snapshot.getChildren()){
                            if (childData.child("userId").getValue(Integer.class)==Integer.valueOf(id)){
                                User u=snapshot.child(childData.getKey()).getValue(User.class);
                                int i=dbHelper.insert(u);

                                if (i!=-1){
                                    Toast.makeText(SQLITEActivity.this,"Record is inserted",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(SQLITEActivity.this,"Record was not inserted.",Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                        }
                        Toast.makeText(SQLITEActivity.this,"No user with this ID was found",Toast.LENGTH_SHORT).show();
                        return;

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone= input_sql_phone.getText()+"";
                String email= input_sql_email.getText()+"";
                String id= input_sql_id.getText()+"";
                String fname= input_sql_fname.getText()+"";
                String lname= input_sql_lname.getText()+"";

                if (
                        fname.isEmpty() ||
                                lname.isEmpty() ||
                                phone.isEmpty() ||
                                email.isEmpty() ||
                                id.isEmpty()){
                    Toast.makeText(SQLITEActivity.this,"Please fill in all fields for this operation",Toast.LENGTH_SHORT).show();
                    return;
                }
                int i=dbHelper.updateUser(fname,lname,phone,email,Integer.valueOf(id));
                if (i>0){
                    Toast.makeText(SQLITEActivity.this,"Record updated",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SQLITEActivity.this,"No records were updated",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_select_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SQLITEActivity.this,SQLSelect.class));
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid= input_sql_id.getText()+"";
                if (uid.isEmpty() ){
                    Toast.makeText(SQLITEActivity.this,"Please fill in uid field for this operation",Toast.LENGTH_SHORT).show();
                    return;
                }
                int i=dbHelper.deleteByUID(Integer.valueOf(uid));
                if (i>0){
                    Toast.makeText(SQLITEActivity.this,"Record deleted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SQLITEActivity.this,"No record was deleted",Toast.LENGTH_SHORT).show();

                }
            }
        });




    }
}