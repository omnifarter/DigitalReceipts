package com.example.digitalreceipts.Contacts;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
<<<<<<< Updated upstream
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
=======
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
>>>>>>> Stashed changes
import android.widget.Toast;

import com.example.digitalreceipts.R;

import java.util.ArrayList;
<<<<<<< Updated upstream
=======

public class ContactsActivity extends AppCompatActivity {
>>>>>>> Stashed changes

public class ContactsActivity extends AppCompatActivity {
    final String[] from = {ContactsContract.Contacts.DISPLAY_NAME};
    SimpleCursorAdapter simpleCursorAdapter;
    ListView l1;
<<<<<<< Updated upstream
    ListView l2;
    Button next;
    SearchView search_name;
    ArrayList<String> names;
    @RequiresApi(api = Build.VERSION_CODES.O)
=======
    ArrayList<String> items;

>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        names = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
<<<<<<< Updated upstream
        l1 = findViewById(R.id.list_of_contacts);
        l1.setTextFilterEnabled(true);
        search_name = findViewById(R.id.searchView);
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        final int[] to = {R.id.contact_name};
        l1.setAlpha(0);
        l1.setDivider(null);
        next = findViewById(R.id.next);
=======
        l1 = findViewById(R.id.listView);

        items = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        startManagingCursor(cursor);

        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone._ID};

        int[] to = {android.R.id.text1, android.R.id.text2};
>>>>>>> Stashed changes

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.contact_item_list, cursor, from, to);
        l1.setAdapter(simpleCursorAdapter);
        l1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
<<<<<<< Updated upstream
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"next screen",Toast.LENGTH_LONG).show();
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
=======

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String text = l1.getItemAtPosition(l1.getItemAtPosition(position).toString());
                //Toast.makeText(ContactsActivity.this,text,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);


        MenuItem searchItem =  menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
>>>>>>> Stashed changes
                return false;
            }

            @Override
<<<<<<< Updated upstream
            public boolean onQueryTextChange(String s) {
                if(search_name.getQuery().length()==0){
                    l1.setAlpha(0);
                }
                else{
                    l1.setAlpha(1);
                    simpleCursorAdapter.getFilter().filter(s);

                    simpleCursorAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.contact_name);
                String text = textView.getText().toString();
                if(names.contains(text)){
                    names.remove(text);
                    Log.i("look","text removed");
                    Toast.makeText(getApplicationContext(), new String(text + " has been removed"), Toast.LENGTH_LONG).show();

                }
                else {
                    names.add(text);
                    textView.setPressed(true);
                    Log.i("look","text added");
                    Toast.makeText(getApplicationContext(), new String(text + " has been added"), Toast.LENGTH_LONG).show();
                }
            }
        });
=======
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> templist = new ArrayList<>();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
>>>>>>> Stashed changes
    }

}