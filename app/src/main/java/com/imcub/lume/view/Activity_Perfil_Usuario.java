package com.imcub.lume.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.imcub.lume.R;
import com.muddzdev.styleabletoast.StyleableToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Ideia_RecyclerView.IdeiaAdapter2;
import com.imcub.lume.model.Modelo_Ideia;
import com.imcub.lume.model.Modelo_Usuario;

public class Activity_Perfil_Usuario extends AppCompatActivity {


    private TextView nomuser;
    private ImageButton addImg;
    private Uri imagemSelecionada;
    private ImageView viewImagePerfil;
    private Button btnSaveImg;
    private Button btnCancelAltImg;
    private LinearLayout linearLayout;
    private IdeiaAdapter2 ideiaAdapter;
    private DatabaseReference databaseReference;
    List<Modelo_Ideia> listadeideias = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TextView btnEditPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__perfil__usuario);
        inicializarcomponentes();
        inicializarfirebase();
        RecyclerView recyclerView = findViewById(R.id.recyclerIdeiasPerfil);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ideiaAdapter = new IdeiaAdapter2(listadeideias);
        recyclerView.setAdapter(ideiaAdapter);
        RecuperarIdeias();
        RecuperarUsuarioLogado();
        BaixarImagemPerfil();
        cliques();



    }

    private void inicializarcomponentes() {
        nomuser = findViewById(R.id.txtNomeUsuarioPerfil);
        addImg = findViewById(R.id.addImg);
        linearLayout = findViewById(R.id.btnaddFotos);
        btnSaveImg = findViewById(R.id.btnSaveImg);
        viewImagePerfil = findViewById(R.id.imgUsuarioPerfil);
        btnCancelAltImg = findViewById(R.id.btncancel);
        btnEditPerfil = findViewById(R.id.iconEditPerfil);


    }

    private void inicializarfirebase() {
        FirebaseApp.initializeApp(Activity_Perfil_Usuario.this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void cliques() {
        addImg.setOnClickListener(selecionarimagem);
        btnSaveImg.setOnClickListener(salvarImagem);
        btnCancelAltImg.setOnClickListener(cancelarAltImg);
        btnEditPerfil.setOnClickListener(abrirTelaEditarPerfil);
    }



    private void RecuperarUsuarioLogado() {
        Query query = databaseReference.child("Usuario").orderByChild("usuarioEmail").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Modelo_Usuario usuario = new Modelo_Usuario();
                    usuario.setUsuarioNome((String) childDataSnapshot.child("usuarioNome").getValue());
                    nomuser.setText(usuario.getUsuarioNome());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            nomuser.setText(user.getDisplayName());


        } else {
            Log.d("NomeErro","Erro ao recuperar nome do banco de dados.");
        }

    }

    private void RecuperarIdeias() {
        DatabaseReference ideiaRef = databaseReference.child("Ideias/").child("Todas Categorias");
        ideiaRef.orderByChild("ideiaIdUsuario").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listadeideias.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    listadeideias.add(ds.getValue(Modelo_Ideia.class));
                }
                ideiaAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    View.OnClickListener selecionarimagem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            linearLayout.setVisibility(View.VISIBLE);
            try {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            } catch (NullPointerException ne) {
                new StyleableToast
                        .Builder(Activity_Perfil_Usuario.this)
                        .text("Nenhuma imagem selecionada.")
                        .textColor(Color.parseColor("#ffffff"))
                        .backgroundColor(Color.parseColor("#ea5455"))
                        .textSize(20)
                        .show();            }

        }
    };
    View.OnClickListener salvarImagem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            uploadimg();
            linearLayout.setVisibility(View.INVISIBLE);
        }
    };

    View.OnClickListener cancelarAltImg = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            linearLayout.setVisibility(View.INVISIBLE);
            BaixarImagemPerfil();
        }
    };


    View.OnClickListener abrirTelaEditarPerfil = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Intent intent = new Intent(Activity_Perfil_Usuario.this, Activity_EditarPerfil.class);
            final Bundle bundle = new Bundle();
            bundle.putString("nomeDoUsuario", nomuser.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    };


    //[Inicio de Função Carregar Imagem Para Firebase Storage]
    private void uploadimg() {
        try {
            String filename = user.getEmail();
            final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/user/" + filename);
            ref.putFile(imagemSelecionada)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.i("Sucesso", uri.toString());
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
            new StyleableToast
                    .Builder(Activity_Perfil_Usuario.this)
                    .text("Nenhuma imagem selecionada.")
                    .textColor(Color.parseColor("#ffffff"))
                    .backgroundColor(Color.parseColor("#ea5455"))
                    .textSize(20)
                    .show();
        }
    }
    //[Fim de Função Carregar Imagm Para Firebase Storage]

    private void BaixarImagemPerfil() {
        try {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            storageRef.child("images/user/" + Objects.requireNonNull(user).getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get()
                            .load(uri)
                            .into(viewImagePerfil);
                }


            });

        } catch (Exception e) {

            new StyleableToast
                    .Builder(Activity_Perfil_Usuario.this)
                    .text("Erro ao buscar imagem de perfil.")
                    .textColor(Color.parseColor("#ffffff"))
                    .backgroundColor(Color.parseColor("#ea5455"))
                    .textSize(20)
                    .show();        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            try {
                imagemSelecionada = data.getData();
                viewImagePerfil.setImageURI(imagemSelecionada);
            } catch (NullPointerException nu) {
                new StyleableToast
                        .Builder(Activity_Perfil_Usuario.this)
                        .text("Nenhuma imagem selecionada.")
                        .textColor(Color.parseColor("#ffffff"))
                        .backgroundColor(Color.parseColor("#ea5455"))
                        .textSize(20)
                        .show();            }
        } else {
            imagemSelecionada = Uri.parse("android.resource://com.imcub.imcubApp/drawable/ic_person_black_24dp.xml");

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
