package com.example.e_dziennik;


import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

/** Pomocnicza klasa okienek dialogowych z możliwością wyboru opcji.
 * Po wyborze opcji, jej wynik jest zapisywany w textView przekazanym
 * w konstruktorze.
 */
public class OptionsDialog extends AppCompatDialogFragment {

    private String mainText;
    private String[] options;
    private TextView toModify;

    private boolean useFilter = false;
    private String separatorType = " ";

    /**
     * @param mainText - tekst do ustawienia w nagłówku okna dialogowego
     * @param options - dostępne opcje
     * @param textView - podlegający modyfikacji przy wyborze odpowiedniej opcji
     */
    public OptionsDialog(String mainText, String[] options, TextView textView)
    {
        this.mainText = mainText;
        this.options = options;
        this.toModify = textView;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mainText)
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        toModify.setText(filterOption(options[which]));
                    }
                });
        return builder.create();
    }

    /** Filter wykorzystywany tylko gdy flaga <code>useFilter</code> jest na true.
     * Jeśli opcja jest w postaci pary klucz-wartość oddzielonych separatorem
     * <code>separatorType</code> to zwracamy tylko wartość.
     * Jeśli jest jednoczłonowa zwracamy naturalnie całość opcji.*/
    public String filterOption(String option)
    {
        if(!useFilter) return option;
        option = option.trim();
        if(option.contains(" ")){
            String[] elements = option.split(separatorType);
            for(int i=1; i<elements.length; i++) //zabezpieczenie gdyby było więcej spacji
                if(!elements[i].equals("")) return elements[i];
        }
        return option;
    }

    public void enableFilter()
    {
        useFilter = true;
    }

    public void setSeparatorType(String separator)
    {
        this.separatorType = separator;
    }
}
