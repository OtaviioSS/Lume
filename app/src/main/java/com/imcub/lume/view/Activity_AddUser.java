package com.imcub.lume.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import firebase.Conexao;

import com.imcub.lume.R;
import com.imcub.lume.model.Modelo_Usuario;
import com.imcub.lume.viewmodel.AddUsuerViewModel;
import com.muddzdev.styleabletoast.StyleableToast;

public class Activity_AddUser extends AppCompatActivity {
    private EditText txtnome;
    private EditText txtemail;
    private static final String TAG = "lo";
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private EditText txtsenha;
    private Button concluir;
    private EditText txtsobrenome;
    private FirebaseAuth auth;
    private EditText txtfaculdade;
    private DatabaseReference databaseReference;
    private TextView btnTelaLogin;
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnGoogle;
    private GoogleSignInAccount account;
    AddUsuerViewModel addUsuerViewModel;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Uri imagemSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__cadastro__usuario);
        inicializarcomponentes();
        inicializarfirebase();
        mAuth = FirebaseAuth.getInstance();
        cliques();
        logarcomGoogle();


    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();


    }

    private void inicializarfirebase() {
        FirebaseApp.initializeApp(Activity_AddUser.this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void inicializarcomponentes() {
        txtnome = findViewById(R.id.barNomeCadUser);
        txtemail = findViewById(R.id.barEmailCadUser);
        txtsenha = findViewById(R.id.barSenhaCadUser);
        concluir = findViewById(R.id.btnConcluirCadUser);
        txtsobrenome = findViewById(R.id.barSobreNomeCadUser);
        btnTelaLogin = findViewById(R.id.txtEntrar);
        btnGoogle = findViewById(R.id.btnGoogleCadUser);
        txtfaculdade = findViewById(R.id.barFaculdadeCadUser);
    }

    private void logarcomGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void cliques() {
        concluir.setOnClickListener(registro);
        btnTelaLogin.setOnClickListener(abrirTelaLogin);
        btnGoogle.setOnClickListener(logarGoogle);
    }

    private void criarUser(String email, String senha) {

        /*
        try {
            if (txtemail != null && txtsenha.length() >= 8) {
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(Activity_AddUser.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && txtsenha.length() >= 8) {
                            adicionarUsuarioBanco();
                            limparcampos();
                            Intent i = new Intent(Activity_AddUser.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else if (txtsenha.length() < 8) {
                            new StyleableToast
                                    .Builder(Activity_AddUser.this)
                                    .text("A senha deve ter no minimo 8 digitos.")
                                    .textColor(Color.parseColor("#ffffff"))
                                    .backgroundColor(Color.parseColor("#ea5455"))
                                    .textSize(20)
                                    .show();
                        }
                    }
                });
            } else {
                new StyleableToast
                        .Builder(Activity_AddUser.this)
                        .text("Os campos de senha e email devem ser preenchidos.")
                        .textColor(Color.parseColor("#ffffff"))
                        .backgroundColor(Color.parseColor("#ea5455"))
                        .textSize(20)
                        .show();
            }
        } catch (IllegalArgumentException argumentErro) {
            new StyleableToast
                    .Builder(Activity_AddUser.this)
                    .text("Os campos de senha e email devem se preenchidos corretamente.")
                    .textColor(Color.parseColor("#ffffff"))
                    .backgroundColor(Color.parseColor("#ea5455"))
                    .textSize(20)
                    .show();

        }*/
    }

    private void adicionarUsuarioBanco() {
        Modelo_Usuario usuario = new Modelo_Usuario();
        usuario.setUsuarioId(auth.getUid());
        usuario.setUsuarioEmail(txtemail.getText().toString());
        usuario.setUsuarioNome(txtnome.getText().toString());
        usuario.setUsuarioFaculdade(txtfaculdade.getText().toString());
        usuario.setUsuarioImagemPerfil("");
        usuario.setUsuarioDataNascimento("");
        usuario.setUsuarioSobrenome(txtsobrenome.getText().toString());
        uploadimg();
        databaseReference.child("Usuario").child(usuario.getUsuarioId()).setValue(usuario);


    }

    private void uploadimg() {
        try {
            String filename = txtemail.getText().toString().trim();
            final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
            ref.putFile(imagemSelecionada)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Erro", e.getMessage(), e);
                }
            });
        } catch (IllegalArgumentException iEx) {


        }
    }

    private void limparcampos() {
        try {
            txtemail.setText("");
            txtnome.setText("");
            txtfaculdade.setText("");
            txtsenha.setText("");
        }catch (Exception e) {
            Log.d("Erro","Erro ao limpar campo");
        }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =  GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                handleSignInResult(task);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (Exception e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Activity_AddUser.this,"Cadastro realizado com sucesso!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Activity_AddUser.this, Activity_Inicio.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }


    View.OnClickListener registro = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = txtemail.getText().toString().trim();
            String senha = txtsenha.getText().toString().trim();
            addUsuerViewModel.insert(email,senha);
        }
    };


    View.OnClickListener abrirTelaLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Activity_AddUser.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener logarGoogle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(),"Desculpe, o login com Google esta temporariamente indisponível .",Toast.LENGTH_SHORT).show();
        }
    };


}
