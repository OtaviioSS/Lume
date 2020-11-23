package com.imcub.lume.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.Objects;

import com.imcub.lume.R;
import com.imcub.lume.model.Modelo_Ideia;
import com.muddzdev.styleabletoast.StyleableToast;

import static com.imcub.lume.R.drawable.btnbg;

public class Activity_EditarIdeia extends AppCompatActivity {
    private EditText txtTitulo;
    private EditText txtDescricao;
    private EditText txtEmail;
    private EditText txtWhatsApp;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button btnConcluir;
    private TextView btnEditar;
    private Button btnCancelar;
    private String nomeUsuario;
    private String imgPerfil;
    private  String dataPublic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__editar_ideia);
        inicializarcomponentes();
        inicializarfirebase();
        cliques();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (intent.getExtras() != null){
            txtTitulo.setText(Objects.requireNonNull(bundle).getString("titulo"));
        }else {
            Toast.makeText(Activity_EditarIdeia.this, "Erro ao recuperar titulo", Toast.LENGTH_LONG).show();
        }
        RecuperarIdeia();
    }



    private void inicializarcomponentes() {
        txtTitulo = findViewById(R.id.barTituloEditIdeia);
        txtDescricao = findViewById(R.id.barDescricaoIdeiaEditIdeia);
        txtEmail = findViewById(R.id.barEmailEditIdeia);
        txtWhatsApp = findViewById(R.id.barWhastAppEditIdeia);
        btnConcluir = findViewById(R.id.btnConcluirEditIdeia);
        btnEditar = findViewById(R.id.btnEditIdeia);
        btnCancelar = findViewById(R.id.btnCancelarEditarIdeia);
    }

    public void inicializarfirebase() {
        FirebaseApp.initializeApp(Activity_EditarIdeia.this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void cliques() {
        btnConcluir.setOnClickListener(concluirAlteracao);
        btnEditar.setOnClickListener(editarIdeia);
        btnCancelar.setOnClickListener(cancelarEditar);
    }

    private void RecuperarIdeia() {
        Query query = databaseReference.child("Ideias/").child("Todas Categorias").orderByChild("ideiaTitulo").equalTo(txtTitulo.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Modelo_Ideia ideia = new Modelo_Ideia();
                        ideia.setIdeiaDescricao((String) childDataSnapshot.child("ideiaDescricao").getValue());
                        txtDescricao.setText(ideia.getIdeiaDescricao());
                        ideia.setIdeiaEmail((String) childDataSnapshot.child("ideiaEmail").getValue());
                        txtEmail.setText(ideia.getIdeiaEmail());
                        ideia.setIdeiaWhatsApp((String) childDataSnapshot.child("ideiaWhatsApp").getValue());
                        txtWhatsApp.setText(ideia.getIdeiaWhatsApp());
                        ideia.setIdeianomeUser((String) childDataSnapshot.child("ideianomeUser").getValue());
                        nomeUsuario = ideia.getIdeianomeUser();
                        ideia.setIdeiaImagemPerfil((String) childDataSnapshot.child("ideiaImagemPerfil").getValue());
                        imgPerfil = ideia.getIdeiaImagemPerfil();
                        ideia.setIdeiaDataDaPub((String) childDataSnapshot.child("ideiaDataDaPub").getValue());
                        dataPublic = ideia.getIdeiaDataDaPub();

                    }catch (Exception e){
                        Toast.makeText(Activity_EditarIdeia.this,"Erro ao recuperar dados do banco de dados",Toast.LENGTH_LONG).show();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private  void SalvarAlteracao(){
        try {
            Modelo_Ideia ideia = new Modelo_Ideia();
            ideia.setIdeiaTitulo(txtTitulo.getText().toString());
            ideia.setIdeiaDescricao(txtDescricao.getText().toString());
            ideia.setIdeiaWhatsApp(txtWhatsApp.getText().toString().trim());
            ideia.setIdeiaEmail(txtEmail.getText().toString().trim());
            ideia.setIdeianomeUser(nomeUsuario);
            ideia.setIdeiaImagemPerfil(imgPerfil);
            ideia.setIdeiaCurtidas("0");
            ideia.setIdeiaDataDaPub(dataPublic);
            ideia.setIdeiaIdUsuario(user.getUid());
            ideia.setEmailDoUsuario(user.getEmail());
            databaseReference.child("Ideias").child(txtTitulo.getText().toString().trim()).setValue(ideia).addOnCompleteListener(Activity_EditarIdeia.this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                new StyleableToast
                                        .Builder(Activity_EditarIdeia.this)
                                        .text("Ideia alterada.")
                                        .textColor(Color.parseColor("#ffffff"))
                                        .backgroundColor(Color.parseColor("#50d890"))
                                        .textSize(20)
                                        .show();                            }
                        }
                    });
        } catch (Exception e) {
            new StyleableToast
                    .Builder(Activity_EditarIdeia.this)
                    .text("Erro ao salvar alteração, tente novamente")
                    .textColor(Color.parseColor("#ffffff"))
                    .backgroundColor(Color.parseColor("#ea5455"))
                    .textSize(20)
                    .show();
        }
    }

    View.OnClickListener concluirAlteracao = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SalvarAlteracao();
            Intent intent = getIntent();
            finish();
            startActivity(intent);

        }
    };

    View.OnClickListener editarIdeia = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnEditar.setVisibility(View.INVISIBLE);
            btnEditar.setEnabled(false);
            btnConcluir.setBackgroundResource(btnbg);
            btnConcluir.setTextColor(Color.parseColor("#ffffff"));
            btnConcluir.setEnabled(true);
            btnCancelar.setVisibility(View.VISIBLE);
            btnCancelar.setEnabled(true);
            txtDescricao.setEnabled(true);
            txtEmail.setEnabled(true);
            txtWhatsApp.setEnabled(true);
        }
    };


    View.OnClickListener cancelarEditar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            startActivity(intent);
            finish();

        }
    };

}
