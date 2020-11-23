package com.imcub.lume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Modelo_Ideia implements Parcelable {
    private String ideiaId;
    private String ideiaTitulo;
    private String ideiaDescricao;
    private String ideiaImagemIdeia;
    private String ideiaWhatsApp;
    private String ideiaEmail;
    private String ideianomeUser;
    private String ideiaSobrenome;
    private String ideiaCurtidas;
    private String ideiaImagemPerfil;
    private String ideiaIdUsuario;
    private String ideiaDataDaPub;
    private String emailDoUsuario;
    private String ideiaCategoria;
    private String userFaculdade;
    private String ideiaValorInvestimento;
    private String pessoasCurtiram;


    public Modelo_Ideia() {

    }


    public Modelo_Ideia(String ideiaId, String ideiaTitulo, String ideiaDescricao,
                        String ideiaImagemIdeia, String ideiaWhatsApp, String ideiaEmail,
                        String ideianomeUser, String ideiaCurtidas, String ideiaImagemPerfil,
                        String ideiaIdUsuario, String ideiaDataDaPub, String emailDoUsuario, String ideiaSobrenome,
                        String ideiaCategoria, String userFaculdade, String ideiaValorInvestimento,String pessoasCurtiram) {
        this.ideiaId = ideiaId;
        this.ideiaTitulo = ideiaTitulo;
        this.ideiaDescricao = ideiaDescricao;
        this.ideiaImagemIdeia = ideiaImagemIdeia;
        this.ideiaWhatsApp = ideiaWhatsApp;
        this.ideiaEmail = ideiaEmail;
        this.ideianomeUser = ideianomeUser;
        this.ideiaCurtidas = ideiaCurtidas;
        this.ideiaImagemPerfil = ideiaImagemPerfil;
        this.ideiaIdUsuario = ideiaIdUsuario;
        this.ideiaDataDaPub = ideiaDataDaPub;
        this.emailDoUsuario = emailDoUsuario;
        this.ideiaSobrenome = ideiaSobrenome;
        this.ideiaCategoria = ideiaCategoria;
        this.userFaculdade = userFaculdade;
        this.ideiaValorInvestimento = ideiaValorInvestimento;
        this.pessoasCurtiram = pessoasCurtiram;
    }





    protected Modelo_Ideia(Parcel in) {
        ideiaId = in.readString();
        ideiaTitulo = in.readString();
        ideiaDescricao = in.readString();
        ideiaImagemIdeia = in.readString();
        ideiaWhatsApp = in.readString();
        ideiaEmail = in.readString();
        ideianomeUser = in.readString();
        ideiaCurtidas = in.readString();
        ideiaImagemPerfil = in.readString();
        ideiaIdUsuario = in.readString();
        ideiaDataDaPub = in.readString();
        emailDoUsuario = in.readString();
        pessoasCurtiram = in.readString();

    }

    public static final Creator<Modelo_Ideia> CREATOR = new Creator<Modelo_Ideia>() {
        @Override
        public Modelo_Ideia createFromParcel(Parcel in) {
            return new Modelo_Ideia(in);
        }

        @Override
        public Modelo_Ideia[] newArray(int size) {
            return new Modelo_Ideia[size];
        }
    };

    public String getEmailDoUsuario() {
        return emailDoUsuario;
    }

    public void setEmailDoUsuario(String emailDoUsuario) {
        this.emailDoUsuario = emailDoUsuario;
    }

    // métodos getters e setters
    public String getIdeiaId() {
        return ideiaId;
    }

    public void setIdeiaId(String ideiaId) {
        this.ideiaId = ideiaId;
    }

    public String getIdeiaTitulo() {
        return ideiaTitulo;
    }

    public void setIdeiaTitulo(String ideiaTitulo) {
        this.ideiaTitulo = ideiaTitulo;
    }

    public String getIdeiaDescricao() {
        return ideiaDescricao;
    }

    public void setIdeiaDescricao(String ideiaDescricao) {
        this.ideiaDescricao = ideiaDescricao;
    }

    public String getIdeiaImagemIdeia() {
        return ideiaImagemIdeia;
    }

    public void setIdeiaImagemIdeia(String ideiaImagemIdeia) {
        this.ideiaImagemIdeia = ideiaImagemIdeia;
    }

    public String getIdeiaWhatsApp() {
        return ideiaWhatsApp;
    }

    public void setIdeiaWhatsApp(String ideiaWhatsApp) {
        this.ideiaWhatsApp = ideiaWhatsApp;
    }

    public String getIdeiaEmail() {
        return ideiaEmail;
    }

    public void setIdeiaEmail(String ideiaEmail) {
        this.ideiaEmail = ideiaEmail;
    }

    public String getIdeianomeUser() {
        return ideianomeUser;
    }

    public void setIdeianomeUser(String ideianomeUser) {
        this.ideianomeUser = ideianomeUser;
    }

    public String getIdeiaImagemPerfil() {
        return ideiaImagemPerfil;
    }

    public void setIdeiaImagemPerfil(String ideiaImagemPerfil) {
        this.ideiaImagemPerfil = ideiaImagemPerfil;
    }

    public String getIdeiaIdUsuario() {
        return ideiaIdUsuario;
    }

    public void setIdeiaIdUsuario(String ideiaIdUsuario) {
        this.ideiaIdUsuario = ideiaIdUsuario;
    }

    public String getIdeiaDataDaPub() {
        return ideiaDataDaPub;
    }

    public void setIdeiaDataDaPub(String ideiaDataDaPub) {
        this.ideiaDataDaPub = ideiaDataDaPub;
    }

    public String getIdeiaSobrenome() {
        return ideiaSobrenome;
    }

    public void setIdeiaSobrenome(String ideiaSobrenome) {
        this.ideiaSobrenome = ideiaSobrenome;
    }

    public String getIdeiaCategoria() {
        return ideiaCategoria;
    }

    public void setIdeiaCategoria(String ideiaCategoria) {
        this.ideiaCategoria = ideiaCategoria;
    }

    public String getUserFaculdade() {
        return userFaculdade;
    }

    public void setUserFaculdade(String userFaculdade) {
        this.userFaculdade = userFaculdade;
    }

    public String getIdeiaValorInvestimento() {
        return ideiaValorInvestimento;
    }

    public void setIdeiaValorInvestimento(String ideiaValorInvestimento) {
        this.ideiaValorInvestimento = ideiaValorInvestimento;
    }

    public String getIdeiaCurtidas() {
        return ideiaCurtidas;
    }

    public void setIdeiaCurtidas(String ideiaCurtidas) {
        this.ideiaCurtidas = ideiaCurtidas;
    }

    public String getPessoasCurtiram() {
        return pessoasCurtiram;
    }

    public void setPessoasCurtiram(String pessoasCurtiram) {
        this.pessoasCurtiram = pessoasCurtiram;
    }

    public static Creator<Modelo_Ideia> getCREATOR() {
        return CREATOR;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ideiaId);
        dest.writeString(ideiaTitulo);
        dest.writeString(ideiaDescricao);
        dest.writeString(ideiaImagemIdeia);
        dest.writeString(ideiaWhatsApp);
        dest.writeString(ideiaEmail);
        dest.writeString(ideianomeUser);
        dest.writeString(ideiaCurtidas);
        dest.writeString(ideiaImagemPerfil);
        dest.writeString(ideiaIdUsuario);
        dest.writeString(ideiaDataDaPub);
        dest.writeString(pessoasCurtiram);
    }
}
