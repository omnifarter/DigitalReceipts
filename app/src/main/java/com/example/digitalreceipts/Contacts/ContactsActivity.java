package com.example.digitalreceipts.Contacts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.digitalreceipts.Billsplit.BIllSplitActivity;
import com.example.digitalreceipts.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsActivity extends AppCompatActivity {
    final String[] from = {ContactsContract.Contacts.DISPLAY_NAME};
    HashMap<String,String> name_to_number = new HashMap<>();
    SimpleCursorAdapter simpleCursorAdapter;
    TextView textView;
    ListView l1;
    Button next;
    SearchView search_name;
    ArrayList<String> names;
    ArrayList<String> numbers = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        names = new ArrayList<>();
        names.add("Myself");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        l1 = findViewById(R.id.list_of_contacts);
        textView = findViewById(R.id.freq_contacted);
        search_name = findViewById(R.id.searchView);
        next = findViewById(R.id.next);
        final int[] to = {R.id.contact_name};
        l1.setTextFilterEnabled(true);
        l1.setDivider(null);
        final Cursor cursor_freq = getContentResolver().query(ContactsContract.Contacts.CONTENT_STREQUENT_URI,null,null,null);
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.contact_item_list, cursor_freq, from, to);
        l1.setAdapter(simpleCursorAdapter);
        l1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting contact numbers
                Cursor cursors= getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
                if (cursors != null) {
                    while(cursors.moveToNext()){
                        String name_temp = cursors.getString(cursors.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number_temp = cursors.getString(cursors.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        if(names.contains(name_temp)){
                            numbers.add(number_temp);
                        }
                    }
                    cursors.close();
                }
                //putting them in a hashmap
                for (int i = 0; i < names.size() ; i++) {
                    name_to_number.put(names.get(i),numbers.get(i));
                }
                System.out.println("THIS IS THE NAME TO NUMBER: " + name_to_number.toString());
                Intent intent = new Intent(getApplicationContext(), BIllSplitActivity.class);
                Intent current = getIntent();
                intent.putParcelableArrayListExtra("BILL_SPLIT",current.getParcelableArrayListExtra("BILL_SPLIT"));
                intent.putExtra("RECEIPT_NUMBER",current.getStringExtra("RECEIPT_NUMBER"));
                intent.putStringArrayListExtra("NAMES",names);
                intent.putExtra("NAME_TO_NUMBER", name_to_number);
                startActivity(intent);
            }
        });
        simpleCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                String searchQuery = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
                String[] args = {"%"+charSequence.toString()+"%"};
                Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, searchQuery, args, null);

                return cur;
            }
        });
        search_name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(search_name.getQuery().length()==0){
                    textView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    simpleCursorAdapter.swapCursor(cursor_freq);
                }
                else{
                    if(simpleCursorAdapter.getCursor() == cursor_freq){
                        textView.setHeight(0);
                        final Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                        simpleCursorAdapter.swapCursor(cursor);}
                    simpleCursorAdapter.getFilter().filter(s);

                    simpleCursorAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = view.findViewById(R.id.contact_name);
                String text = textView.getText().toString();
                if(names.contains(text)){
                    names.remove(text);
                    Toast.makeText(getApplicationContext(), text + " has been removed", Toast.LENGTH_SHORT).show();

                }
                else {
                    names.add(text);
                    Toast.makeText(getApplicationContext(), text + " has been added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}