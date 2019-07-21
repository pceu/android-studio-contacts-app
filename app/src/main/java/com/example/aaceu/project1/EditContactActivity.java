package com.example.aaceu.project1;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

public class EditContactActivity extends AppCompatActivity {

    // TABLE_NAME is the database table name where we store Contacts objects
    public static final String TABLE_NAME = "contacts";
    //SET_WHERE_CLAUSE is for comparing database id with the userID stored in here (they are actually the same)
    public static final String SET_WHERE_CLAUSE = "id = ";
    // MyDBHelper instance is instantiated as we want to save the edited data to the database
    MyDBHelper dbHelper;

    EditText editContactNameInput;
    EditText editPhoneNumberInput;
    EditText editEmailInput;
    EditText editUrCompanyInput;
    int userID;
    private static String errorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // instantiate MyDBHelper object here with this context
        dbHelper = new MyDBHelper(this);

        // assign each edit text to related id in XML file
        editContactNameInput = (EditText) findViewById(R.id.editContactNameInput);
        editPhoneNumberInput = (EditText) findViewById(R.id.editPhoneNumberInput);
        editEmailInput = (EditText) findViewById(R.id.editEmailInput);
        editUrCompanyInput = (EditText) findViewById(R.id.editUrCompanyInput);

        // extract intent passed from ContactDetails page and assign them to appropriate EditText in this page
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            userID = bundle.getInt("userID");
            editContactNameInput.setText(bundle.getString("name"));
            editPhoneNumberInput.setText(bundle.getString("phoneNumber"));
            editEmailInput.setText(bundle.getString("email"));
            editUrCompanyInput.setText(bundle.getString("company"));
        }
    }

    public void saveToDatabase(View view)
    {
        if (fieldsValidation()) {
            // firstly store the data to ContentValues object first
            ContentValues values = new ContentValues();
            // store each field with their related key to values (ContentValues object)
            values.put("name", editContactNameInput.getText().toString());
            values.put("phoneNum", editPhoneNumberInput.getText().toString());
            values.put("email", editEmailInput.getText().toString());
            values.put("company", editUrCompanyInput.getText().toString());

            // create writable SQLiteDatabase of our dbHelper
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // execute update command to database
            // update the edited values of Contact data (name, email ...) to database
            // userID is used to search where this contact information is stored in the database
            db.update(TABLE_NAME, values, SET_WHERE_CLAUSE + userID, null);

            // go to MainActivity and reloads the data from database
            Intent intent = new Intent(EditContactActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // if the validation returns false value, show alert dialog with appropriate message to the user
            // in this steps we create AlertDialog.Builder first, then show the message to user
            // set the cancel/negative button to close the alert dialog
            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(EditContactActivity.this);
            alertBuilder.setMessage(errorMessage);
            alertBuilder.setCancelable(true);
            // if cancel button is pressed, then call the cancellation of the dialog interface
            alertBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog errorAlertDialog = alertBuilder.create();
            errorAlertDialog.show();
        }

    }

    public void deleteFromDB (View view)
    {
        // get a dbHelper writableDatabase
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // run the delete sql commands where the userID equal to the id in database
        db.delete(TABLE_NAME, SET_WHERE_CLAUSE + userID, null);

        // go to the main activity and reloads the data using intent
        Intent intent = new Intent(EditContactActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // using patterns are learned from Android Studio official Developer website
    // https://developer.android.com/reference/android/util/Patterns
    public boolean fieldsValidation()
    {
        boolean result = true;
        // assign false to result if the input name is less than 2 characters or is empty
        if (editContactNameInput.getText().toString().isEmpty() || editContactNameInput.getText().toString().length() < 2) {
            result = false;
            errorMessage = "Name cannot be empty or must be greater than two characters!";
            return result;
        }

        // assign result to false if phone number is empty or matches the builtin patterns of phone in android studio
        if (editPhoneNumberInput.getText().toString().isEmpty() || !Patterns.PHONE.matcher(editPhoneNumberInput.getText().toString()).matches()) {
            result = false;
            errorMessage = "Phone cannot be empty or must be a phone number format!";
            return result;
        }

        // checks with email address patterns and assign false to result if not match
        if (editEmailInput.getText().toString().isEmpty())
        {
            // we do not have to do anything as result is set to true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(editEmailInput.getText().toString()).matches()) {
            result = false;
            errorMessage = "Email address is not email format!";
            return result;
        }

        //if company is less than two characters assign result as false
        if (editUrCompanyInput.getText().toString().isEmpty())
        {

        } else if (editUrCompanyInput.getText().toString().length() < 2) {
            result = false;
            errorMessage = "If company is insert, then must be longer than two characters length!";
            return result;
        }

        return result;
    }
}
