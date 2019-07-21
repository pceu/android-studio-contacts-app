package com.example.aaceu.project1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TABLE_NAME = "contacts";  // the table name we create in MyDBHelper
    MyDBHelper dbHelper;
    // string variable for selecting all columns in the database table fields
    String[] allColumns = new String[] {"id", "name", "phoneNum", "email", "company", "image"};

    // a list of Contact object - will use the list to populate the listView later
    private List<Contact> contactData = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate new MyDBHelper in this context
        dbHelper = new MyDBHelper(this);
        // create Toolbar and link with the one in XML using its id
        Toolbar myToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        myToolBar.setTitle("@string/app_name");

        // on onCreate of this activity save the new created contact from creating new contact page to database
        // only save the data to database only if the the bundle of intent passed from create new contact page is not null, thus won't cause error
        serializeData();

        /*
            any data saved in database are deserialize in this method below
            onCreate(Bundle savedInstanceState)
                call super.onCreate(SavedInstanceState)
                call setContentView() method
                deserialize/load UserData
                serialize/ save UserData
            UserData (int levelNumber, double coinAmount)
                assign levelNumber to this levelNumber(class property)
                assign coinAmount to this coinAmount (class property)

            getCoinAmount()
                return coinAmount;

            Level(int id, List<String> qWords, List<String> hints)
                id = class property ID
                qWords = class property qWords
                hints = class property hints

            increaseCoinAmount(double amount)
                coinAmount = coinAmount - amount;
                end

        */
        deserializeData();

        // creates a listView in main activity
        generateListView();
    }

    //========== Serializing and Deserialization data from database =============================================================================

    public void serializeData()
    {
        // the new created contact from CreateNewContact are passed to this activity using Intent object
        //in here we extract the intent using bundle
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            // create temporary ContentValues variable and store the respective details of contact
            // the intent is passed from the CreateNewContact activity Java class
            ContentValues values = new ContentValues();
            values.put("name", bundle.getString("contactName"));
            values.put("phoneNum", bundle.getString("phoneNumber"));
            values.put("email", bundle.getString("email"));
            values.put("company", bundle.getString("company"));
            values.put("image", bundle.getString("image"));

            // get a writable database from dbHelper object and insert the above values to the database
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(TABLE_NAME, null, values);
        }
    }

    public void deserializeData()
    {
        // create readable database of MyDBHelper object and select all the columns in the table
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, allColumns, null, null, null, null, null);

        // if the count in database is null (no data record in database) then return
        if (cursor.getCount() == 0) {
            return;
        }

        while (cursor.moveToNext())
        {
            // store all the columns (except id)to a temp variable respectively, then pass them to a addNewContact as a parameter
            int userID = cursor.getInt(cursor.getColumnIndex("id")); // id from database is stored as we can search database and edit or modify the data
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNum"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String company = cursor.getString(cursor.getColumnIndex("company"));
            String image = cursor.getString(cursor.getColumnIndex("image"));

            // pass the above retrieved data as parameters to create Contact instance/object for displaying in the listView
            addNewContact(userID, name, phoneNumber, email, company, image);
        }

    }

    //-------------------------------------------------------------------------------------------------------

    // accepts Contact type as a parameter and add the Contact object to a contactData List<Contact>
    public void addContact(Contact person)
    {
        contactData.add(person);
    }

    // unlike the above one, we can pass each Contact class attributes as parameters and then call the above
    // method to add the newly created Contact object to contactData list.
    public void addNewContact (int userID, String name, String phoneNum, String email, String company, String image)
    {
        addContact(new Contact(userID, name, phoneNum, email, company, image));
    }

    private class ContactAdapter extends ArrayAdapter<Contact> {
        // create adapter class as we need this to display listView on the screen
        public ContactAdapter() {
            super(MainActivity.this, R.layout.contact_list, contactData);
        }

        /*getView method
        takes three parameters position for the location in contactData (instance from Contact class)
        convertView is the returned View
        parent which is the the parent of this View; ViewGroup
        The if statement checks if the View(contactView) is null, and if so it gets the inflater and inflate the contact_list layout
        I learn this method from Online Video, however I do not copy but rather figured out/modified by my own to suits my codes.
        */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View contactView = convertView;
            if (contactView == null) {
                contactView = getLayoutInflater().inflate(R.layout.contact_list, parent,false);
            }

            // gets the position of the variable contactData (contactData is a Contact class type List<>).
            Contact contact = contactData.get(position);

            //set image to the contact person imageView
            ImageView imageView = (ImageView)contactView.findViewById(R.id.person_icon);
            if (contact.getImage().equalsIgnoreCase("person1")) {
                imageView.setImageResource(R.drawable.person1);
            }
            else {
                imageView.setImageResource(R.drawable.person2);
            }

            // set the personNameText with the name from Contact class
            TextView nameText = (TextView)contactView.findViewById(R.id.personNameText);
            nameText.setText(contact.getName());
            // set the personCompanyText with the company name from Contact class
            TextView companyText = (TextView)contactView.findViewById(R.id.personCompanyText);
            companyText.setText(contact.getCompany());

            return contactView;
        }
    }

    private void generateListView()
    {
        // create new array adapter for ContactAdapter
        ArrayAdapter myAdapter = new ContactAdapter();
        // use the list view as contactListView which is defined by custom (includes image, full name and company name)
        ListView list = (ListView) findViewById(R.id.contactsListView);
        //set the adapter to the listView we created
        list.setAdapter(myAdapter);

        /*
            when user clicks we want to pass all the related data of the Contact object to details screen
            in this case we pass all the data using Intent object when user clicks a particular list in the listView
            userID is also passed as we will be needing later to use this when we want to update the data related to this object
            in the database - we will use this to identify the current object we are updating in the database
         */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ContactDetailsPage.class);
                i.putExtra("userID", contactData.get(position).getUserID());
                i.putExtra("name", contactData.get(position).getName());
                i.putExtra("image", contactData.get(position).getImage());
                i.putExtra("phoneNumber", contactData.get(position).getPhoneNum());
                i.putExtra("email", contactData.get(position).getEmail());
                i.putExtra("company", contactData.get(position).getCompany());
                startActivity(i);
            }
        });

    }

    // ========== To Login Page ========================================================================
    // this change the view to LoginPage layout
    public void toCreateNewContact(View view) {
        Intent intent = new Intent(this, CreateNewContact.class);
        startActivity(intent);
    }

    // ========== Menu =================================================================================
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get the item in the menu item execute appropriate switch case according to
        // which is selected in the menu list
        switch (item.getItemId()) {
            case R.id.logout:
                Intent intent = new Intent(this, login_page.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}
