package Ideia_RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.imcub.lume.view.Activity_EditarIdeia;
import com.imcub.lume.R;

import java.util.List;

import com.imcub.lume.model.Modelo_Ideia;

public class IdeiaAdapter2 extends RecyclerView.Adapter<IdeiaAdapter2.MyViewHolder> {

    List<Modelo_Ideia> ideias;
    int cor = 0;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;


    public IdeiaAdapter2(List<Modelo_Ideia> ideias) {
        this.ideias = ideias;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity__perfil__item__view, parent, false);
        return new MyViewHolder(itemLista);


    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {


        try {
            Modelo_Ideia ideia = ideias.get(i);
            holder.tituloIdeia.setText(ideia.getIdeiaTitulo());
            holder.deletar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Excluir conta?");
                    builder.setMessage("Tem certeza que deseja excluir a ideia? ");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Apagando ideia
                            Query query = databaseReference.child("Ideias/").child("Todas Categorias").orderByChild("ideiaTitulo").equalTo(holder.tituloIdeia.getText().toString());
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                        childDataSnapshot.getRef().removeValue();
                                        Toast.makeText(builder.getContext(), "Ideia apagada", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

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
            });

            holder.editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(v.getContext(), Activity_EditarIdeia.class);
                    final Bundle bundle = new Bundle();
                    bundle.putString("titulo", holder.tituloIdeia.getText().toString());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);

                }
            });


        } catch (Exception e) {
            Log.i("Erro", "erro", e);
        }


    }


    @Override
    public int getItemCount() {
        return ideias.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tituloIdeia;
        ImageView deletar;
        ImageView editar;


        MyViewHolder(View itemView) {
            super(itemView);
            tituloIdeia = itemView.findViewById(R.id.textView);
            deletar = itemView.findViewById(R.id.deletItem);
            editar = itemView.findViewById(R.id.editItem);


        }

    }


}