package com.imcub.lume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Modelo_Usuario implements Parcelable {
    private String usuarioNome;
    private String usuarioEmail;
    private String usuarioDataNascimento;
    private String usuarioImagemPerfil;
    private String usuarioId;
    private  String usuarioSobrenome;
    private String usuarioFaculdade;

    public Modelo_Usuario(){

    }

    public Modelo_Usuario(String usuarioNome,String usuarioEmail,String usuarioSenha,
                          String usuarioDataNascimento,String usuarioImagemPerfil,
                          String usuarioId, String usuarioInstituicao,String usuarioSobrenome,String usuarioFaculdade){
        this.usuarioNome = usuarioNome;
        this.usuarioEmail = usuarioEmail;
        this.usuarioDataNascimento = usuarioDataNascimento;
        this.usuarioImagemPerfil = usuarioImagemPerfil;
        this.usuarioId = usuarioId;
        this.usuarioSobrenome = usuarioSobrenome;
        this.usuarioFaculdade = usuarioFaculdade;
    }

    protected Modelo_Usuario(Parcel in) {
        usuarioNome = in.readString();
        usuarioEmail = in.readString();
        usuarioDataNascimento = in.readString();
        usuarioImagemPerfil = in.readString();
        usuarioId = in.readString();
        usuarioSobrenome = in.readString();
        usuarioFaculdade = in.readString();
    }

    public static final Creator<Modelo_Usuario> CREATOR = new Creator<Modelo_Usuario>() {
        @Override
        public Modelo_Usuario createFromParcel(Parcel in) {
            return new Modelo_Usuario(in);
        }

        @Override
        public Modelo_Usuario[] newArray(int size) {
            return new Modelo_Usuario[size];
        }
    };

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }



    public String getUsuarioDataNascimento() {
        return usuarioDataNascimento;
    }

    public void setUsuarioDataNascimento(String usuarioDataNascimento) {
        this.usuarioDataNascimento = usuarioDataNascimento;
    }

    public String getUsuarioImagemPerfil() {
        return usuarioImagemPerfil;
    }

    public void setUsuarioImagemPerfil(String usuarioImagemPerfil) {
        this.usuarioImagemPerfil = usuarioImagemPerfil;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioSobrenome() {
        return usuarioSobrenome;
    }

    public void setUsuarioSobrenome(String usuarioSobrenome) {
        this.usuarioSobrenome = usuarioSobrenome;
    }

    public String getUsuarioFaculdade() {
        return usuarioFaculdade;
    }

    public void setUsuarioFaculdade(String usuarioFaculdade) {
        this.usuarioFaculdade = usuarioFaculdade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(usuarioNome);
        dest.writeString(usuarioEmail);
        dest.writeString(usuarioDataNascimento);
        dest.writeString(usuarioImagemPerfil);
        dest.writeString(usuarioId);
        dest.writeString(usuarioSobrenome);
        dest.writeString(usuarioFaculdade);
    }
}
