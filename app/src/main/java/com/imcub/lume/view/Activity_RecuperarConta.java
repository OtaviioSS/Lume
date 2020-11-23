package com.imcub.lume.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.imcub.lume.R;
import com.muddzdev.styleabletoast.StyleableToast;

public class Activity_RecuperarConta extends AppCompatActivity {
    private EditText baremail;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__recuperar_conta);
        inicializarfirebase();
        baremail = findViewById(R.id.barEmailRecuperarConta);
        Button btnEnviar = findViewById(R.id.btnEnviarRecuperarConta);
        btnEnviar.setOnClickListener(enviarEmailclick);
    }

    View.OnClickListener enviarEmailclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            enviarEmailRecuperacaoDeConta();

        }
    };


    private void inicializarfirebase() {
        FirebaseApp.initializeApp(Activity_RecuperarConta.this);
    }

    private void enviarEmailRecuperacaoDeConta() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = baremail.getText().toString();
        try {
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                new StyleableToast
                                        .Builder(Activity_RecuperarConta.this)
                                        .text("Ideia Adicionada com Sucesso")
                                        .textColor(Color.parseColor("#ffffff"))
                                        .backgroundColor(Color.parseColor("#50d890"))
                                        .textSize(20)
                                        .show();
                                Intent intent = new Intent(Activity_RecuperarConta.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        } catch (Exception e) {
            new StyleableToast
                    .Builder(Activity_RecuperarConta.this)
                    .text("Verifique o email inserido e tente novament.")
                    .textColor(Color.parseColor("#ffffff"))
                    .backgroundColor(Color.parseColor("#ea5455"))
                    .textSize(20)
                    .show();

        }

    }


}
