package com.imcub.lume.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.imcub.lume.R;
import com.imcub.lume.view.MainActivity;

public class Activity_introApp extends AppIntro {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance("Bem-Vindo",
                "O Lume é um aplicativo para universitarios, aqui você podera achar parceiro pra execuatr seus mais ousados projetos, compartilhe conhecimento e faça o futuro.",
                R.drawable.readingdoodle, Color.parseColor("#00a8cc")));
        addSlide(AppIntroFragment.newInstance("Precisa de ajuda para executar um projeto?",
                "Você precisa de um PARCEIRO para poder executar um projeto com você?, um programador, administrador, agricultor etc, AQUI È O LUGAR .",
                R.drawable.minaecachorro, Color.parseColor("#32407B")));

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences != null) {
            boolean checar = sharedPreferences.getBoolean("checarAcesso", false);
            if (checar) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        editor.putBoolean("checarAcesso", false).commit();

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        editor.putBoolean("checarAcesso", true).commit();
        finish();
    }
}
