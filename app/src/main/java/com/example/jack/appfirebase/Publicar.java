package com.example.jack.appfirebase;

import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class Publicar extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference personaRef;

    private EditText txtproducname;
    private EditText txtmumcontac;
    private EditText txtpreciop;
    private EditText txtnumberFijo;
    private Button btnpublicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);

        txtproducname = (EditText) findViewById(R.id.txtProductoname);
        txtmumcontac = (EditText) findViewById(R.id.txtNumbercontac);
        txtnumberFijo = (EditText) findViewById(R.id.txtNumberFijo);
        txtpreciop = (EditText) findViewById(R.id.txtPreciop);

        btnpublicar = (Button) findViewById(R.id.btnPublicar);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("home", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("home", "onAuthStateChanged:signed_out");
                }
            }
        };

        database= FirebaseDatabase.getInstance();
        personaRef = database.getReference("movilesfirebase");

        btnpublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserProfileChangeRequest usuario = new UserProfileChangeRequest.Builder()
                        .setDisplayName(txtproducname.getText().toString().trim()).build();

                mAuth.getCurrentUser().updateProfile(usuario);
                personaRef = database.getReference("persona").child(mAuth.getCurrentUser().getUid());
                personaRef.child("Nombre del Producto").setValue(txtproducname.getText().toString());
                personaRef.child("Precio").setValue(txtpreciop.getText().toString());
                personaRef.child("telefonos").child("celular").setValue(txtmumcontac.getText().toString());
                personaRef.child("telefonos").child("fijo").setValue(txtnumberFijo.getText().toString());
                personaRef.child("dispositivoDondeGuardo").setValue(FirebaseInstanceId.getInstance().getToken());

                personaRef.getParent().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.

                        System.out.println(dataSnapshot.getChildrenCount());

                        //Log.d("Home Activity", "Value is: " + valor);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Home Activity", "Failed to read value.", error.toException());
                    }
                });

            }
        });

    }
}
