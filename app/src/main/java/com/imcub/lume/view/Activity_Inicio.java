package com.imcub.lume.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imcub.lume.R;
import com.imcub.lume.model.Modelo_Ideia;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;

import Ideia_RecyclerView.IdeiaAdapter;

public class Activity_Inicio extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private IdeiaAdapter ideiaAdapter;
    List<Modelo_Ideia> listadeideias = new ArrayList<>();
    private Spinner spinCategInicio;
    int iCurrentSelection;
    private SwipeRefreshLayout refreshLayout;
    Handler handler = new Handler();
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__inicio);
        inicializarcomponentes();
        inicializarfirebase();
        inicializarrecyclerView();
        atualizar();
        RecuperarIdeias();
        verificarAutenticacao();
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        spinCategInicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (iCurrentSelection != position) {
                    RecuperarIdeias();
                }
                iCurrentSelection = position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void atualizar() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inicializarrecyclerView();
            }
        });
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_red_light,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ideiaAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
    }

    private void inicializarrecyclerView() {
        recyclerView = findViewById(R.id.recyclerInicio);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ideiaAdapter = new IdeiaAdapter(listadeideias);
        recyclerView.setAdapter(ideiaAdapter);
        handler.postDelayed(new Runnable() {
            public void run() {
                if (ideiaAdapter != null)
                    refreshLayout.setRefreshing(false);
            }
        }, 7000);

    }

    private void inicializarcomponentes() {
        BottomNavigationView navigationView = findViewById(R.id.navegtionInicio);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        spinCategInicio = findViewById(R.id.spinnerCategInicio);
        refreshLayout = findViewById(R.id.swip);

    }

    private void inicializarfirebase() {
        FirebaseApp.initializeApp(Activity_Inicio.this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


    }

    private void verificarAutenticacao() {
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (FirebaseAuth.getInstance().getUid() == null) {
                Intent intent = new Intent(Activity_Inicio.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.d("Verific", "Erro ao verificar altenticação");
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        verificarAutenticacao();
    }

    private void RecuperarIdeias() {
        DatabaseReference ideiaRef = databaseReference.child("Ideias/" + spinCategInicio.getSelectedItem().toString());
        ideiaRef.addValueEventListener(new ValueEventListener() {
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
                new StyleableToast
                        .Builder(Activity_Inicio.this)
                        .text("Não foi possivel buscar ideias, por favor abra o aplicativo novamente.")
                        .textColor(Color.parseColor("#ffffff"))
                        .backgroundColor(Color.parseColor("#ea5455"))
                        .textSize(18)
                        .show();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.addiadeiamenu:
                    Intent intent1 = new Intent(Activity_Inicio.this, Activity_Add_Ideia.class);
                    startActivity(intent1);
                    return true;
                case R.id.sair:
                    FirebaseAuth.getInstance().signOut();
                    verificarAutenticacao();
                    return true;
                case R.id.chat:
                    Intent intent2 = new Intent(Activity_Inicio.this, Activity_Lista_Contato.class);
                    startActivity(intent2);
                    break;
                case R.id.perfil:
                    Intent intent3 = new Intent(Activity_Inicio.this, Activity_Perfil_Usuario.class);
                    startActivity(intent3);
                    break;
            }
            return false;
        }
    };


}