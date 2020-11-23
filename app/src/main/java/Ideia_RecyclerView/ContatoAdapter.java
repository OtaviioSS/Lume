package Ideia_RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.imcub.lume.view.Activity_Chat;
import com.imcub.lume.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.imcub.lume.model.Modelo_Usuario;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.MyViewHolder> {
    private String emailUsuario;
    private Modelo_Usuario usuario;
    private List<Modelo_Usuario> usuarios;
    Modelo_Usuario usua;
    private int n = 0;
    Context context;
    private DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public ContatoAdapter(List<Modelo_Usuario> usuarios) {
        this.usuarios = usuarios;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity__contato__item_view, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemLista);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        try {
            usuario = usuarios.get(i);
            holder.nome.setText(usuario.getUsuarioNome());
            holder.imgUser.setImageURI(Uri.parse(usuario.getUsuarioImagemPerfil()));
            holder.mensagem.setText(usuario.getUsuarioEmail());
            holder.emailDoUsuario = usuario.getUsuarioEmail();
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Activity_Chat.class);
                    intent.putExtra("contact",usuarios.get(i));
                    v.getContext().startActivity(intent);

                }
            });
                 } catch (Exception e) {
            Log.i("Erro", "erro", e);
        }

        //Carregar imagem
        if (user != null) {
            emailUsuario = holder.emailDoUsuario;
            holder.inicializarfirebase();
            holder.BaixarImagemPerfil();




        }


    }

    @Override
    public int getItemCount() {
        return usuarios.size();

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView nome;
        TextView mensagem;
        ConstraintLayout layout;
        String emailDoUsuario;



        MyViewHolder(View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgContatoItemView);
            nome = itemView.findViewById(R.id.txtNomeContatoItemView);
            mensagem = itemView.findViewById(R.id.txtUltimaMensagemItemView);
            layout = itemView.findViewById(R.id.layoutcontatos);




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
                            .into(imgUser);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });


        }


    }

}

