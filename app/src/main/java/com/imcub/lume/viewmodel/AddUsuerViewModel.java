package com.imcub.lume.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imcub.lume.model.Modelo_Usuario;
import com.imcub.lume.view.Activity_AddUser;
import com.imcub.lume.view.MainActivity;
import com.muddzdev.styleabletoast.StyleableToast;

public class AddUsuerViewModel extends AndroidViewModel {


    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    public AddUsuerViewModel(@NonNull Application application) {
        super(application);
    }
    private void inicializarfirebase() {
        FirebaseApp.initializeApp(getApplication().getApplicationContext());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    public void insert(String email, final String password) {
        try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) getApplication().getApplicationContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && password.length() >= 8) {


                            Intent i = new Intent(getApplication().getApplicationContext(), MainActivity.class);
                            getApplication().getApplicationContext().startActivity(i);

                        } else if (password.length() < 8) {
                            new StyleableToast
                                    .Builder(getApplication().getApplicationContext())
                                    .text("A senha deve ter no minimo 8 digitos.")
                                    .textColor(Color.parseColor("#ffffff"))
                                    .backgroundColor(Color.parseColor("#ea5455"))
                                    .textSize(20)
                                    .show();
                        }
                    }
                });

                new StyleableToast
                        .Builder(getApplication().getApplicationContext())
                        .text("Os campos de senha e email devem ser preenchidos.")
                        .textColor(Color.parseColor("#ffffff"))
                        .backgroundColor(Color.parseColor("#ea5455"))
                        .textSize(20)
                        .show();

        } catch (IllegalArgumentException argumentErro) {
            new StyleableToast
                    .Builder(getApplication().getApplicationContext())
                    .text("Os campos de senha e email devem se preenchidos corretamente.")
                    .textColor(Color.parseColor("#ffffff"))
                    .backgroundColor(Color.parseColor("#ea5455"))
                    .textSize(20)
                    .show();

        }
    }

    private void adicionarUsuarioBanco(String email,String name,String
            surname, String academy) {
        Modelo_Usuario usuario = new Modelo_Usuario();
        usuario.setUsuarioId(email);
        usuario.setUsuarioNome(name);
        usuario.setUsuarioFaculdade(academy);
        usuario.setUsuarioImagemPerfil("");
        usuario.setUsuarioDataNascimento("");
        usuario.setUsuarioSobrenome(surname);
        databaseReference.child("Usuario").child(usuario.getUsuarioId()).setValue(usuario);


    }





}
