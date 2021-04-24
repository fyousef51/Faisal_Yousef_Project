package com.example.faisal_yousef_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SQLSelect extends AppCompatActivity {

    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_q_l_select);
        DBHelper dbHelper=new DBHelper(this);
        ListView lv=findViewById(R.id.lv_sql);
        EditText inp_uid=findViewById(R.id.inp_sql_select_uid);
        Button select=findViewById(R.id.button_sqlite_select);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=inp_uid.getText()+"";
                Cursor c;
                if (uid.isEmpty()){
                    c=dbHelper.selectAll();
                }else{
                    c=dbHelper.selectByUID(Integer.valueOf(uid));
                }
                if (c==null){
                    Toast.makeText(SQLSelect.this,"No records were found.",Toast.LENGTH_SHORT).show();
                    return;
                }
                users.clear();
                do {
                    User u=new User();
                    u.firstName=c.getString(0);
                    u.lastName=c.getString(1);
                    u.phoneNumber=c.getString(2);
                    u.emailAddress=c.getString(3);
                    u.userId=c.getInt(4);
                    users.add(u);
                }while (c.moveToNext());

                ((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
            }
        });

        users=new ArrayList<>();

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

                TableLayout table=(TableLayout) LayoutInflater.from(SQLSelect.this).inflate(R.layout.list_item,parent,false);

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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u= users.get(position);

                Toast.makeText(SQLSelect.this,u.getFirstName()+" "+ u.getLastName(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}