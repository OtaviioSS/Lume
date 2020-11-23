package com.imcub.lume.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.imcub.lume.R;
import com.muddzdev.styleabletoast.StyleableToast;
import com.squareup.picasso.Picasso;
import com.imcub.lume.model.Modelo_Usuario;
public class Activity_EditarPerfil extends AppCompatActivity {


    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText barnome;
    private EditText bardata;
    private EditText barsobrenome;
    private EditText barinstituicao;
    private String email;
    private String imgPerfil;
    private Button btnconcluir;
    private TextView btncancelar;
    private String idUser;
    private ImageView btnMais;
    private TextView deletConta;
    private ImageView imgPerfilUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__editar_perfil);
        inicializarfirebase();
        RecuperarUsuarioLogado();
        inicializarcomponentes();
        cliquedebotao();
        BaixarImagemPerfil();
    }

    private void inicializarcomponentes() {
        barnome = findViewById(R.id.barNomeEditPerfil);
        bardata = findViewById(R.id.barDataEditPerfil);
        btnconcluir = findViewById(R.id.btnConcluir);
        btncancelar = findViewById(R.id.btnCancelar);
        barsobrenome = findViewById(R.id.barSobreNomeEditPerfil);
        barinstituicao = findViewById(R.id.barInstituicaoEditPerfil);
        btnMais = findViewById(R.id.mais);
        deletConta = findViewById(R.id.deletarConta);
        imgPerfilUsuario = findViewById(R.id.btnselcfotoEditPerfil);

    }

    private void cliquedebotao() {
        btnconcluir.setOnClickListener(salvarDadoS);
        btncancelar.setOnClickListener(cancelarEdit);
        btnMais.setOnClickListener(clicarMore);
        deletConta.setOnClickListener(deleta);
    }

    private void inicializarfirebase() {
        FirebaseApp.initializeApp(Activity_EditarPerfil.this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    private void RecuperarUsuarioLogado() {
        Query query = databaseReference.child("Usuario").orderByChild("usuarioEmail").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Modelo_Usuario usuario = new Modelo_Usuario();
                        usuario.setUsuarioNome((String) childDataSnapshot.child("usuarioNome").getValue());
                        usuario.setUsuarioDataNascimento((String) childDataSnapshot.child("usuarioDataNascimento").getValue());
                        usuario.setUsuarioSobrenome((String) childDataSnapshot.child("usuarioSobrenome").getValue());
                        usuario.setUsuarioFaculdade((String) childDataSnapshot.child("usuarioFaculdade").getValue());
                        usuario.setUsuarioId((String) childDataSnapshot.child("usuarioId").getValue());
                        usuario.setUsuarioEmail((String) childDataSnapshot.child("usuarioEmail").getValue());
                        usuario.setUsuarioImagemPerfil((String) childDataSnapshot.child("usuarioImagemPerfil").getValue());
                        barnome.setText(usuario.getUsuarioNome());
                        bardata.setText(usuario.getUsuarioDataNascimento());
                        idUser = usuario.getUsuarioId();
                        barsobrenome.setText(usuario.getUsuarioSobrenome());
                        barinstituicao.setText(usuario.getUsuarioFaculdade());
                        email = usuario.getUsuarioEmail();
                        imgPerfil = usuario.getUsuarioImagemPerfil();
                    }catch (Exception e){
                        new StyleableToast
                                .Builder(Activity_EditarPerfil.this)
                                .text("Problemas ao consultar dados.")
                                .textColor(Color.parseColor("#ffffff"))
                                .backgroundColor(Color.parseColor("#ea5455"))
                                .textSize(20)
                                .show();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void SalvarAlteracao() {
        try {
            Modelo_Usuario usuario = new Modelo_Usuario();
            usuario.setUsuarioNome(barnome.getText().toString());
            usuario.setUsuarioSobrenome(barsobrenome.getText().toString());
            usuario.setUsuarioFaculdade(barinstituicao.getText().toString());
            usuario.setUsuarioDataNascimento(bardata.getText().toString());
            usuario.setUsuarioEmail(email);
            usuario.setUsuarioId(idUser);
            usuario.setUsuarioImagemPerfil(imgPerfil);
            databaseReference.child("Usuario").child(idUser).setValue(usuario).addOnCompleteListener(Activity_EditarPerfil.this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                new StyleableToast
                                        .Builder(Activity_EditarPerfil.this)
                                        .text("Perfil alterado!")
                                        .textColor(Color.parseColor("#ffffff"))
                                        .backgroundColor(Color.parseColor("#50d890"))
                                        .textSize(20)
                                        .show();
                            }
                        }
                    });


        }catch(Exception e) {
            new StyleableToast
                    .Builder(Activity_EditarPerfil.this)
                    .text("Verifique os campos e tente novamente.")
                    .textColor(Color.parseColor("#ffffff"))
                    .backgroundColor(Color.parseColor("#ea5455"))
                    .textSize(20)
                    .show();
        }
    }

    private void BaixarImagemPerfil() {
        try {
            if (user.getPhotoUrl() != null) {
                Picasso.get()
                        .load(user.getPhotoUrl())
                        .into(imgPerfilUsuario);
                imgPerfilUsuario.setImageURI(user.getPhotoUrl());

            } else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                assert user != null;
                storageRef.child("images/user/" + user.getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .into(imgPerfilUsuario);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(Activity_EditarPerfil.this,"Erro",Toast.LENGTH_SHORT).show();
        }


    }
    View.OnClickListener salvarDadoS = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SalvarAlteracao();
        }
    };

    View.OnClickListener cancelarEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Activity_EditarPerfil.this, Activity_Perfil_Usuario.class);
            startActivity(intent);

        }
    };

    View.OnClickListener clicarMore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deletConta.setVisibility(View.VISIBLE);

        }
    };

    View.OnClickListener deleta = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dilogs();

        }
    };
    private void dilogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_EditarPerfil.this);
        builder.setTitle("Excluir conta?");
        builder.setMessage("Todas as ideias criadas continuarão ativas até que você as apague,  " +
                "após excluir sua conta você não poderamais alteralas ou removelas, tem certeza que deseja excluir sua conta agora? ");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child("Usuario").child(idUser).removeValue();
                user.delete();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Activity_EditarPerfil.this, MainActivity.class);
                startActivity(intent);
                finish();





            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
