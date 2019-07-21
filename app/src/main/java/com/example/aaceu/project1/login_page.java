package com.example.aaceu.project1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login_page extends AppCompatActivity {

    public static final String TABLE_NAME = "login";  // the table name we create in MyDBHelper
    // there are two Database helper in this project
    // LoginDBHelper for storing login details and MyDBHelper for storing Contact data
    LoginDBHelper dbHelperLogin;
    String[] allColumns = new String[] {"id", "username", "password"};
    Button loginButton;
    EditText userNameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        userNameInput = (EditText) findViewById(R.id.userNameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        dbHelperLogin = new LoginDBHelper(this);
    }

    // if username and password match with data stored in database then this method will be called (move to MainActivity page)
    public void GoToContactListActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void register(View view)
    {
        ContentValues values = new ContentValues();
        //if username is not input then show error message using toast
        if (userNameInput.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(login_page.this, "You must enter a username to register!",Toast.LENGTH_LONG).show();
            return;
        } else {
            // if user has input something in username field then add the value to values(COntentValues object)
            // this is for storing the value to database for future  login validation
            values.put("username", userNameInput.getText().toString());
        }

        // if nothing is input to the password field, then show error message
        if (passwordInput.getText().toString().equals("")) {
            Toast.makeText(login_page.this, "You must enter a password to register!",Toast.LENGTH_LONG).show();
            return;
        } else {
            // add the password to values which will be stored with username (above) for future login validation
            values.put("password", encryptPassword(passwordInput.getText().toString()));
        }

        // create writable database
        SQLiteDatabase db = dbHelperLogin.getWritableDatabase();
        // insert the above typed values to the database
        // remember this is LoginDBHelper, which is different from MyDBHelper (for storing Contact object)
        db.insert(TABLE_NAME, null, values);

        // after registration is done set the username and password editTextView to empty string
        userNameInput.setText("");
        passwordInput.setText("");
        Toast.makeText(login_page.this, "Registration successful!",Toast.LENGTH_LONG).show();
    }

    public void login(View view)
    {
        // create readable database from LoginDBHelper
        SQLiteDatabase db = dbHelperLogin.getReadableDatabase();
        // select all columns from login table
        Cursor cursor = db.query(TABLE_NAME, allColumns, null, null, null, null, null);

        // if the table is empty there is nothing to compare, thus inform user that there are no login details in database yet
        if (cursor.getCount() == 0) {
            Toast.makeText(login_page.this, "No login details exists in database!",Toast.LENGTH_LONG).show();
            return;
        }

        // compare each record in database with what user have just input
        // if they are the same, then bring user to MainActivity
        // if no, inform the user that the input data is invalid
        while (cursor.moveToNext())
        {
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            if(userNameInput.getText().toString().equalsIgnoreCase(username) && passwordInput.getText().toString().equals(decryptPassword(password)))
            {
                GoToContactListActivity(view);
                Toast.makeText(login_page.this, "WELCOME " + username.toUpperCase() + "!",Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        Toast.makeText(login_page.this, "Invalid Username and Password!",Toast.LENGTH_LONG).show();
    }

    // ======== encryption and decryption of user credentials ==================================
        /* ideas and recommendation of implementing this encryption and decryption method is
            learned from this website -
            https://stackoverflow.com/questions/30148729/how-to-secure-android-shared-preferences

            NOTE: I do not copy the code straight, I rather studied and create a method which acts
            something like the one a found in the resource
        */
    // =========================================================================================

    private String encryptPassword(String data)
    {
        // the input data will be encode as bas64 and returned the encoded string
        // the encrypted data(password only) is stored in database so that no one understands what does it store
        return Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
    }

    private String decryptPassword(String data)
    {
        // the input data will be decrypt using Base64 method and returned the decrypted method
        // when comparing the user input data and data in database for validation, password extracted from
        // database will be decrypted
        return new String(Base64.decode(data, Base64.DEFAULT));
    }
    // -------------------------------------------------------------------------------------------
}
