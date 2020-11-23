package com.imcub.lume.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
import com.google.firebase.storage.UploadTask;
import com.imcub.lume.model.Modelo_Ideia;
import com.imcub.lume.model.Modelo_Usuario;
import com.imcub.lume.view.Activity_Add_Ideia;
import com.imcub.lume.view.Activity_Inicio;
import com.muddzdev.styleabletoast.StyleableToast;

public class AddIdeiaViewModel {
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Activity_Add_Ideia activity_add_ideia = new Activity_Add_Ideia();


    public void inicializarfirebase(Context context) {
        FirebaseApp.initializeApp(context);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void uploadimg(Uri img, String filename) {
        final StorageReference ref = FirebaseStorage.getInstance()
                .getReference("/images/publications/" + filename);
        ref.putFile(img)
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


    }

    public void RecuperarUsuarioLogado(final TextView nome, final TextView instituicao, Context context) {
        Query query = databaseReference.child("Usuario").orderByChild("usuarioEmail").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Modelo_Usuario usuario = new Modelo_Usuario();
                    usuario.setUsuarioNome((String) childDataSnapshot.child("usuarioNome").getValue());
                    nome.setText(usuario.getUsuarioNome());
                    usuario.setUsuarioFaculdade((String) childDataSnapshot.child("usuarioFaculdade").getValue());
                    instituicao.setText(usuario.getUsuarioFaculdade());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            nome.setText(user.getDisplayName());

        } else {

            new StyleableToast
                    .Builder(context)
                    .text("Houve problemas ao recuperar nome.")
                    .textColor(Color.parseColor("#ffffff"))
                    .backgroundColor(Color.parseColor("#ea5455"))
                    .textSize(20)
                    .show();
        }
    }


    public void SalvarIdeia(Modelo_Ideia ideia, final Context context) {
        databaseReference.child("Ideias").child(ideia.getIdeiaCategoria())
                .child(ideia.getIdeiaTitulo() + ideia.getIdeiaId()).setValue(ideia)
                .addOnCompleteListener((Activity) context,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(context, Activity_Inicio.class);
                                    context.startActivity(intent);
                                    new StyleableToast
                                            .Builder(context)
                                            .text("Ideia Adicionada com Sucesso")
                                            .textColor(Color.parseColor("#ffffff"))
                                            .backgroundColor(Color.parseColor("#50d890"))
                                            .textSize(20)
                                            .show();
                                }
                            }
                        });
        databaseReference.child("Ideias").child("Todas Categorias")
                .child(ideia.getIdeiaTitulo() + ideia.getIdeiaId())
                .setValue(ideia).addOnCompleteListener((Activity) context,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context,
                                    Activity_Inicio.class);
                            context.startActivity(intent);

                        }
                    }
                });
    }
}
