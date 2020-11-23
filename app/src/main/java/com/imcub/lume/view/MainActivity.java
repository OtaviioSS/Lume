package com.imcub.lume.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.imcub.lume.R;
import com.muddzdev.styleabletoast.StyleableToast;


import java.util.Objects;

import firebase.Conexao;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lo" ;
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private EditText barsenha;
    private EditText baremail;
    private  FirebaseAuth auth;
    private TextView esqueci;
    private Button btnlogar;
    private TextView btnCad;
    private Button btnGoogleLogin;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarComponentes();
        inicializarfirebase();
        criarRequest();
        mAuth = FirebaseAuth.getInstance();
        cliquesdebotao();

    }

    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(MainActivity.this, Activity_Inicio.class);
            startActivity(intent);
            finish();

        }

    }

    private void inicializarComponentes() {
        esqueci = findViewById(R.id.btnEsqueci);
        baremail = findViewById(R.id.barEmailLogin);
        barsenha = findViewById(R.id.barSenhaLogin);
        btnlogar = findViewById(R.id.btnLogarLogin);
        btnCad = findViewById(R.id.btnCadEmail);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogar);


    }

    private void cliquesdebotao() {
        btnlogar.setOnClickListener(logar);
        btnCad.setOnClickListener(telacadastro);
        esqueci.setOnClickListener(resetarsenha);
        btnGoogleLogin.setOnClickListener(logarGoogle);
    }

    private void inicializarfirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
    }

    private void criarRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    private void login(String email,String senha) {
        try {
            auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(MainActivity.this, Activity_Inicio.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        new StyleableToast
                                .Builder(MainActivity.this)
                                .text("E-mail ou senha inválidos.")
                                .textColor(Color.parseColor("#ffffff"))
                                .backgroundColor(Color.parseColor("#ea5455"))
                                .textSize(20)
                                .show();
                        FirebaseAuth.getInstance().signOut();

                    }
                }
            });
        }catch (NullPointerException nullEx){
            nullEx.printStackTrace();

        }catch (IllegalArgumentException argEx){
            new StyleableToast
                    .Builder(MainActivity.this)
                    .text("O campo de email e senha deve ser preenchida.")
                    .textColor(Color.parseColor("#ffffff"))
                    .backgroundColor(Color.parseColor("#ea5455"))
                    .textSize(20)
                    .show();

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(Objects.requireNonNull(account));
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            Intent intent = new Intent(MainActivity.this, Activity_Inicio.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    View.OnClickListener logarGoogle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signIn();
        }
    };

    View.OnClickListener logar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String email = baremail.getText().toString().trim();
                String senha = barsenha.getText().toString().trim();
                login(email, senha);
            }catch (NullPointerException nullEx){
                Toast.makeText(MainActivity.this,"Por favor preencha todos os campos",Toast.LENGTH_LONG).show();
            }catch (IllegalArgumentException illegalEx){
                new StyleableToast
                        .Builder(MainActivity.this)
                        .text("E-mail ou senha inválidos.")
                        .textColor(Color.parseColor("#ffffff"))
                        .backgroundColor(Color.parseColor("#ea5455"))
                        .textSize(20)
                        .show();            }
        }
    };
    
    View.OnClickListener telacadastro = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, Activity_AddUser.class);
            startActivity(intent);

        }};

    View.OnClickListener resetarsenha = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Activity_RecuperarConta.class);
            startActivity(intent);

        }
    };
}
