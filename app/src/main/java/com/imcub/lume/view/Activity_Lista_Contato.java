package com.imcub.lume.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import Ideia_RecyclerView.ContatoAdapter;

import com.imcub.lume.R;
import com.imcub.lume.model.Modelo_Usuario;

public class Activity_Lista_Contato extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ContatoAdapter contatoAdapter;
    List<Modelo_Usuario> listadeusuarios = new ArrayList<>();
    Handler handler = new Handler();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__lista__contato);
        inicializarfirebase();
        inicializarrecyclerView();
        RecuperarContatos();
        atualizar();
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();
        contatoAdapter.notifyDataSetChanged();

    }
    private void atualizar() {

    }

    private void inicializarrecyclerView() {
        recyclerView = findViewById(R.id.recyclerContato);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        contatoAdapter = new ContatoAdapter(listadeusuarios);
        recyclerView.setAdapter(contatoAdapter);
        handler.postDelayed(new Runnable() {
            public void run() {

            }
        }, 7000);

    }

    private void inicializarfirebase() {
        FirebaseApp.initializeApp(Activity_Lista_Contato.this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


    }

    private void RecuperarContatos() {
        DatabaseReference contatosRef = databaseReference.child("ListasContatos").child(user.getUid());
        contatosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listadeusuarios.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    listadeusuarios.add(ds.getValue(Modelo_Usuario.class));
                }
                contatoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}