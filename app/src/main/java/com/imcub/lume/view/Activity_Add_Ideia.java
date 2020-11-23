package com.imcub.lume.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.imcub.lume.R;
import com.imcub.lume.model.Modelo_Ideia;
import com.imcub.lume.viewmodel.AddIdeiaViewModel;
import com.muddzdev.styleabletoast.StyleableToast;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class Activity_Add_Ideia extends AppCompatActivity {
    private EditText txtTituloIdeia;
    private EditText txtDescricaoIdeia;
    private EditText txtWhastAppIdeia;
    private EditText txtValor;
    private TextView txtNomeUsuario;
    private TextView txtFaculdade;
    private ImageView imgPerfilUsuario;
    private Button btnPublicar;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Uri imagemSelecionada;
    private Spinner categSpin;
    CarregamentoDialog carregamentoDialog = new CarregamentoDialog(Activity_Add_Ideia.this);
    private ImageView addImg;
    private ImageView cliqueIMG;
    Modelo_Ideia ideia = new Modelo_Ideia();
    AddIdeiaViewModel ideiaViewModel = new AddIdeiaViewModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add__ideia);
        inicializarcompnetente();
        cliquesdebotoes();
        ideiaViewModel.inicializarfirebase(Activity_Add_Ideia.this);
        BaixarImagemPerfil();
        ideiaViewModel.RecuperarUsuarioLogado(txtNomeUsuario, txtFaculdade, Activity_Add_Ideia.this);
        txtValor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    txtValor.removeTextChangedListener(this);
                    Locale myLocale = new Locale("pt", "BR");
                    String cleanString = s.toString().replaceAll("[R$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));

                    current = formatted;
                    txtValor.setText(formatted);
                    txtValor.setSelection(formatted.length());

                    txtValor.addTextChangedListener(this);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        BaixarImagemPerfil();
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
                storageRef.child("images/user/" + Objects.requireNonNull(user).getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .into(imgPerfilUsuario);
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(Activity_Add_Ideia.this, "Erro", Toast.LENGTH_SHORT).show();
        }


    }


    private void cliquesdebotoes() {
        btnPublicar.setOnClickListener(adicionarIdeia);
        cliqueIMG.setOnClickListener(selecionarimagem);
    }

    private void inicializarcompnetente() {
        txtTituloIdeia = findViewById(R.id.txtTituloAddIdeia);
        txtDescricaoIdeia = findViewById(R.id.txtDescricaoAddIdeia);
        txtWhastAppIdeia = findViewById(R.id.txtWhastAppAddIdeia);
        txtValor = findViewById(R.id.txtEmailAddIdeia);
        txtNomeUsuario = findViewById(R.id.txtNomeUsuarioAddIdeia);
        imgPerfilUsuario = findViewById(R.id.imgUserAddNovaIdeia);
        btnPublicar = findViewById(R.id.btnConcluirAddIdeia);
        categSpin = findViewById(R.id.spinnerCategoriaAddIdeia);
        txtFaculdade = findViewById(R.id.txtFaculdadeAddIdeia);
        addImg = findViewById(R.id.imgPublicacaoAddImagem);
        cliqueIMG = findViewById(R.id.imgPublicacaoClique);
    }

    protected void onActivityResult(int request, int resultCode, Intent data) {
        super.onActivityResult(request, resultCode, data);
        if (request == 0) {
            try {
                imagemSelecionada = data.getData();
                addImg.setImageURI(imagemSelecionada);
            } catch (NullPointerException nu) {
                new StyleableToast
                        .Builder(Activity_Add_Ideia.this)
                        .text("Nenhuma imagem selecionada.")
                        .textColor(Color.parseColor("#ffffff"))
                        .backgroundColor(Color.parseColor("#ea5455"))
                        .textSize(20)
                        .show();
            }
        } else {
            imagemSelecionada = Uri.parse("android.resource://com.imcub.imcubApp/drawable/ic_person_black_24dp.xml");

        }

    }


    private void limparcampos() {
        txtTituloIdeia.setText("");
        txtDescricaoIdeia.setText("");
        txtWhastAppIdeia.setText("");
        txtValor.setText("0");
        txtNomeUsuario.setText("");


    }


    View.OnClickListener selecionarimagem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            } catch (NullPointerException ne) {
                new StyleableToast
                        .Builder(Activity_Add_Ideia.this)
                        .text("Nenhuma imagem selecionada.")
                        .textColor(Color.parseColor("#ffffff"))
                        .backgroundColor(Color.parseColor("#ea5455"))
                        .textSize(20)
                        .show();
            }

        }
    };
    View.OnClickListener adicionarIdeia = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                carregamentoDialog.inicioCarregamentoDialog();
                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date data = new Date();
                String dataFormatada = formataData.format(data);
                String dataPub = "Publicado " + dataFormatada;  
                ideia.setIdeiaValorInvestimento(txtValor.getText().toString());
                ideia.setIdeiaTitulo(txtTituloIdeia.getText().toString());
                ideia.setIdeiaDescricao(txtDescricaoIdeia.getText().toString());
                ideia.setIdeiaWhatsApp(txtWhastAppIdeia.getText().toString().trim());
                ideia.setIdeiaValorInvestimento(txtValor.getText().toString());
                ideia.setIdeianomeUser(txtNomeUsuario.getText().toString());
                ideia.setIdeiaImagemPerfil(imgPerfilUsuario.toString());
                ideia.setIdeiaImagemIdeia(addImg.toString());
                ideia.setUserFaculdade(txtFaculdade.getText().toString());
                ideia.setIdeiaId(UUID.randomUUID().toString());
                ideia.setIdeiaCurtidas("0");
                ideia.setIdeiaDataDaPub(dataPub);
                ideia.setIdeiaIdUsuario(user.getUid());
                ideia.setEmailDoUsuario(user.getEmail());
                ideia.setIdeiaCategoria(categSpin.getSelectedItem().toString());
                ideiaViewModel.uploadimg(imagemSelecionada, ideia.getIdeiaId());
                ideiaViewModel.SalvarIdeia(ideia,Activity_Add_Ideia.this);
                limparcampos();
            } catch (Exception e) {
                new StyleableToast
                        .Builder(Activity_Add_Ideia.this)
                        .text("Houve problemas ao publicar a ideia!")
                        .textColor(Color.parseColor("#ffffff"))
                        .backgroundColor(Color.parseColor("#ea5455"))
                        .textSize(20)
                        .show();
            }
        }
    };


}
