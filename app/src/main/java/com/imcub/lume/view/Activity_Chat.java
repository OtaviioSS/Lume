package com.imcub.lume.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.imcub.lume.R;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.*;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.imcub.lume.model.Modelo_Message;
import com.imcub.lume.model.Modelo_Usuario;

public class Activity_Chat extends AppCompatActivity {

    private GroupAdapter adapter;
    private ImageView imgPerfilContato;
    private String emailUsuario;
    private EditText editChat;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Modelo_Usuario contato;
    String me;
    List<Modelo_Message> listadeideias = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__chat);
        RecyclerView rview = findViewById(R.id.recyclerChat);
        TextView nomeContato = findViewById(R.id.nomeContatoChat);
        imgPerfilContato = findViewById(R.id.imgContactChat);
        contato = Objects.requireNonNull(getIntent().getExtras()).getParcelable("contact");
        nomeContato.setText(Objects.requireNonNull(contato).getUsuarioNome());
        emailUsuario = contato.getUsuarioEmail();
        adapter = new GroupAdapter();
        rview.setLayoutManager(new LinearLayoutManager(Activity_Chat.this));
        rview.setAdapter(adapter);
        FirebaseApp.initializeApp(Activity_Chat.this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        BaixarImagemPerfil();
        ImageView btnChat = findViewById(R.id.btnEnviarChat);
        editChat  = findViewById(R.id.barCaixaTextoChat);
        updateToken();
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
       databaseReference.child("Usuario").orderByChild("usuarioEmail").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               me = user.getUid();
               fetchMessages();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void updateToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        String uid = FirebaseAuth.getInstance().getUid();
        if(uid != null){
                FirebaseFirestore.getInstance().collection("users")
                        .document(uid)
                        .update("token",token);
        }
    }

    private void fetchMessages() {
        if(me!=null){
            String fromId = me;
            String toId = contato.getUsuarioId();
            FirebaseFirestore.getInstance().collection("conversations/")
                    .document(fromId)
                    .collection(toId)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            List<DocumentChange> documentChanges = value != null ? value.getDocumentChanges() : null;
                            if(documentChanges!=null){
                                for(DocumentChange doc: documentChanges){
                                    if (doc.getType() == DocumentChange.Type.ADDED){
                                        Modelo_Message message = doc.getDocument().toObject(Modelo_Message.class);
                                        adapter.add(new MessageItem(message));

                                    }
                                }
                            }

                        }
                    });

        }
    }

    private void sendMessage() {
        String text = editChat.getText().toString();
        editChat.setText(null);
        String fromId = FirebaseAuth.getInstance().getUid();
        String toId = contato.getUsuarioId();
        long timestamp = System.currentTimeMillis();

        Modelo_Message modelo_message = new Modelo_Message();
        modelo_message.setFromId(fromId);
        modelo_message.setToId(toId);
        modelo_message.setTimestamp(timestamp);
        modelo_message.setText(text);
        if(!modelo_message.getText().isEmpty()){
            FirebaseFirestore.getInstance().collection("/conversations")
                    .document(fromId)
                    .collection(toId)
                    .add(modelo_message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste",documentReference.getId());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Teste",e.getMessage(),e);


                }
            });

            FirebaseFirestore.getInstance().collection("/conversations")
                    .document(toId)
                    .collection(fromId)
                    .add(modelo_message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste",documentReference.getId());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Teste",e.getMessage(),e);
                }
            });


        }



    }

    private static class MessageItem extends Item<ViewHolder>{
        private final Modelo_Message message;

        private MessageItem(Modelo_Message message) {
            this.message = message;
        }


        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
        TextView txtMsg = viewHolder.itemView.findViewById(R.id.txtMsg);
        txtMsg.setText(message.getText());

        }

        @Override
        public int getLayout() {

            return message.getFromId().equals(FirebaseAuth.getInstance().getUid())
                    ? R.layout.item_from_msg
                    : R.layout.item_to_msg ;
        }
    }

    private void BaixarImagemPerfil() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/user/" + emailUsuario).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .into(imgPerfilContato);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });


    }

        }