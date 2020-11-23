package Ideia_RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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
import com.imcub.lume.model.Modelo_Denuncia;
import com.imcub.lume.model.Modelo_Ideia;
import com.imcub.lume.model.Modelo_Usuario;
import com.imcub.lume.view.Activity_Inicio;
import com.imcub.lume.view.Activity_Lista_Contato;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class IdeiaAdapter extends RecyclerView.Adapter<IdeiaAdapter.MyViewHolder> {
    private String Idideia;
    private String emailUsuario;
    private Modelo_Ideia ideia;
    private List<Modelo_Ideia> ideias;
    private int n = 0;
    private String qtd;
    Context context;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Modelo_Usuario usuarioLogado = new Modelo_Usuario();
    long v = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorLike;
    Object s;

    //ADAPTER DA IDEIA ADAPTA OS DADOS AO LAYOUT
    public IdeiaAdapter(List<Modelo_Ideia> ideias) {
        this.ideias = ideias;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_view_1, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemLista);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        try {
            ideia = ideias.get(i);

            holder.idIdeia = ideia.getIdeiaId();
            holder.idUser = ideia.getIdeiaIdUsuario();
            holder.emailDoUsuario = ideia.getEmailDoUsuario();
            holder.tituloIdeia.setText(ideia.getIdeiaTitulo());
            holder.descricao.setText(ideia.getIdeiaDescricao());
            if (ideia.getIdeiaSobrenome() == null) {
                holder.nome.setText(ideia.getIdeianomeUser());
            } else {
                holder.nome.setText(ideia.getIdeianomeUser() + " " + ideia.getIdeiaSobrenome());
            }
            holder.imgUser.setImageURI(Uri.parse(ideia.getIdeiaImagemPerfil()));
            holder.time.setText("Publicado dia " + ideia.getIdeiaDataDaPub());
            holder.valor.setText(ideia.getIdeiaValorInvestimento());
            holder.telefone.setText(ideia.getIdeiaWhatsApp());
            holder.faculdade.setText(ideia.getUserFaculdade());
            holder.txtQuantidadeLike.setText(ideia.getIdeiaCurtidas());

            holder.adicionarContato.setOnClickListener(holder.addNewContact);
            holder.btnMore.setOnClickListener(holder.moreclick);

            holder.btnLike.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(final LikeButton likeButton) {
                    //Pegar a quantidade de cutidas do banco
                    v = Long.parseLong(String.valueOf(holder.txtQuantidadeLike.getText())) ;


                    //sómar mais um
                    v = v+1;
                    //enviar para o banco
                    qtd = Long.toString(v);
                    if (holder.txtQuantidadeLike.getText() != qtd) {
                        holder.txtQuantidadeLike.setText(qtd.toString());
                        //atualizar no banco
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("ideiaCurtidas",qtd);
                        databaseReference.child("Ideias").child("Todas Categorias")
                                .child(holder.tituloIdeia.getText() + holder.idIdeia.toString())
                                .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Modelo_Usuario usuario = new Modelo_Usuario();
                                usuario.setUsuarioId(user.getUid());
                                usuario.setUsuarioNome(ideia.getIdeianomeUser());
                                databaseReference.child("Ideias").child("Todas Categorias")
                                        .child(holder.tituloIdeia.getText() + holder.idIdeia.toString())
                                        .child("UsuariosCurtiram").child(user.getUid()).setValue(usuario);
                                if (v < 0){
                                    holder.txtQuantidadeLike.setText("0");
                                }

                            }
                        });
                        v =0;

                    }


                }

                @Override
                public void unLiked(final LikeButton likeButton) {
                    v = Long.parseLong(String.valueOf(holder.txtQuantidadeLike.getText())) ;
                    v = v-1;
                    qtd = Long.toString(v);
                    if (v > 0) {
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("ideiaCurtidas",qtd);
                        databaseReference.child("Ideias").child("Todas Categorias")
                                .child(holder.tituloIdeia.getText() + holder.idIdeia.toString())
                                .updateChildren(hashMap)
                                .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                holder.btnLike.setLiked(false);
                                databaseReference.child("Ideias").child("Todas Categorias")
                                        .child(holder.tituloIdeia.getText() + holder.idIdeia.toString())
                                        .child("UsuariosCurtiram").child(user.getUid()).removeValue();
                                holder.btnLike.setLiked(false);
                            }
                        });

                        v =0;


                    }

                }
            });
            ;
        } catch (Exception e) {
            Log.i("Erro ao recuperar ideia", "erro", e);
        }

        //Carregar imagem
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        if (user != null) {
            emailUsuario = holder.emailDoUsuario;
            Idideia = holder.idIdeia;
            holder.BaixarImagemPublicacao();
            holder.BaixarImagemPerfil();
            holder.inicializarfirebase();
            holder.RecuperarUsuarioLogado();
            holder.VerificarCurtidas();
            StorageReference imagesPublicRef = storageRef
                    .child("images/publications/" + user.getUid());


        }


    }

    @Override
    public int getItemCount() {
        return ideias.size();

    }


    //VIEWHOLDER DA IDEIA METODOS PARA CADA LINHA
    class MyViewHolder extends RecyclerView.ViewHolder {
        String idIdeia;
        String idUser;
        String emailDoUsuario;

        ImageView imgUser;
        TextView nome;
        TextView valor;
        TextView time;
        TextView descricao;
        TextView tituloIdeia;
        TextView telefone;
        ImageView btnMore;
        TextView btnDenunciar;
        TextView faculdade;
        ImageView imgPublic;
        ConstraintLayout layout;
        ImageView adicionarContato;
        LikeButton btnLike;
        TextView txtQuantidadeLike;


        MyViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.usernameItemView1);
            time = itemView.findViewById(R.id.timeItemView1);
            imgUser = itemView.findViewById(R.id.imgPerfilItemView1);
            tituloIdeia = itemView.findViewById(R.id.txtTitulo);
            valor = itemView.findViewById(R.id.txtvalorItemView);
            telefone = itemView.findViewById(R.id.telefoneItemView);
            descricao = itemView.findViewById(R.id.txtDescricaoItemView);
            btnMore = itemView.findViewById(R.id.iconMais);
            btnDenunciar = itemView.findViewById(R.id.denunciarIdeia);
            faculdade = itemView.findViewById(R.id.faculdadeItemView1);
            imgPublic = itemView.findViewById(R.id.imgPublicacaoInicio);
            layout = itemView.findViewById(R.id.layoutadapter);
            adicionarContato = itemView.findViewById(R.id.btnChatItemView);
            btnLike = itemView.findViewById(R.id.btnApoiarItemView);
            txtQuantidadeLike = itemView.findViewById(R.id.txtQtdLikesItemView);


        }

        public void inicializarfirebase() {
            FirebaseApp.initializeApp(context);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }

        private void BaixarImagemPerfil() {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            storageRef.child("images/user/" + emailUsuario).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get()
                            .load(uri)
                            .resize(100, 100)
                            .centerCrop()
                            .into(imgUser);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });


        }

        private void BaixarImagemPublicacao() {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            storageRef.child("images/publications/" + Idideia).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get()
                            .load(uri)
                            .into(imgPublic);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });


        }

        private void RecuperarUsuarioLogado() {
            databaseReference.child("Usuario").orderByChild("usuarioEmail").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        usuarioLogado.setUsuarioId((String) dataSnapshot.child("usuarioId").getValue());
                        usuarioLogado.setUsuarioNome((String) dataSnapshot.child("usuarioNome").getValue());
                        usuarioLogado.setUsuarioImagemPerfil((String) dataSnapshot.child("usuarioImagemPerfil").getValue());
                        usuarioLogado.setUsuarioEmail((String) dataSnapshot.child("usuarioEmail").getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        private void VerificarCurtidas(){
         databaseReference.child("Ideias").child("Todas Categorias")
                    .child(tituloIdeia.getText() + idIdeia.toString())
                    .child("UsuariosCurtiram").child(user.getUid()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 int qtdLiikes = Integer.parseInt(String.valueOf(txtQuantidadeLike.getText())) ;
                 if (snapshot.child("usuarioId").getValue() != null && qtdLiikes > 0){
                     btnLike.setLiked(true);

                 }else {
                     btnLike.setLiked(false);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });





        }




        View.OnClickListener moreclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.ideiamenu);
                final Dialog dialog = new Dialog(v.getContext());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.denunciarIdeia:
                                dialog.setContentView(R.layout.dialogdenunciar);
                                Button btnConfirmar = dialog.findViewById(R.id.btnConfirmarDenuncia);
                                Button btnCancelar = dialog.findViewById(R.id.btnCancelarDenuncia);
                                final EditText txtDenuncia = dialog.findViewById(R.id.txtDenuncia);
                                dialog.show();
                                btnConfirmar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Modelo_Denuncia denuncia = new Modelo_Denuncia();
                                            denuncia.setDenuncia(txtDenuncia.getText().toString());
                                            denuncia.setIdDenuncia(UUID.randomUUID().toString());
                                            denuncia.setIdIdeia(idIdeia);
                                            Toast.makeText(v.getContext(), "Denuncia realizada",
                                                    Toast.LENGTH_LONG).show();
                                            databaseReference.child("Denuncia")
                                                    .child(denuncia.getIdDenuncia()).setValue(ideia)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(context,
                                                            "Erro no servidor, por favor tente mais tarde",
                                                            Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(context, Activity_Inicio.class);
                                                    context.startActivity(intent);
                                                }
                                            });


                                        } catch (Exception e) {
                                        }
                                    }
                                });
                                btnCancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(v.getContext(),
                                                Activity_Inicio.class);
                                        v.getContext().startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                                return true;
                            case R.id.sobreideia:
                                imgPublic.setVisibility(View.VISIBLE);
                                return true;
                        }

                        return false;
                    }
                });
                popupMenu.show();

            }
        };
        View.OnClickListener addNewContact = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modelo_Usuario usuario = new Modelo_Usuario();
                usuario.setUsuarioNome(nome.getText().toString());
                usuario.setUsuarioImagemPerfil(ideia.getIdeiaImagemPerfil());
                usuario.setUsuarioEmail(emailDoUsuario.toString());
                usuario.setUsuarioId(idUser);
                databaseReference.child("ListasContatos/" + user.getUid())
                        .child(nome.getText().toString()).setValue(usuario)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Adicionado ao chat",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
                databaseReference.child("ListasContatos/" + usuario.getUsuarioId())
                        .child(usuarioLogado.getUsuarioNome().toString()).setValue(usuarioLogado)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Adicionado ao chat",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, Activity_Lista_Contato.class);
                                context.startActivity(intent);
                            }
                        });


            }
        };


    }

}