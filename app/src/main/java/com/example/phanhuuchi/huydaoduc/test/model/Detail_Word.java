package com.example.phanhuuchi.huydaoduc.test.model;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phanhuuchi.huydaoduc.test.Data.DBSQL;
import com.example.phanhuuchi.huydaoduc.test.Main.MainActivity;
import com.example.phanhuuchi.huydaoduc.test.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Detail_Word extends AppCompatActivity {
//    @BindView(R.id.image)
//    ImageView image;
//    @BindView(R.id.editDetailTen)
//    EditText editTen;
//    @BindView(R.id.editDetailMota)
//    EditText editMota;

    EditText editTen;
    EditText editMota;
    ImageView imageDetail;
    ImageButton imageButtonSound;
    TextView WordNote;
    EditText txtNote;

    private Word _curWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__word);
        ButterKnife.bind(this);
        init();
        setWord();
    }

    private void setWord() {

        editTen.setText(_curWord.getTen());
        editMota.setText(_curWord.getMota());
        imageDetail.setImageBitmap(_curWord.getImageBitmap());


        imageButtonSound.setVisibility(View.INVISIBLE);
        if(_curWord.getSound() != null)
        {
            imageButtonSound.setVisibility(View.VISIBLE);
            imageButtonSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(Word.mediaPlayer.isPlaying())
                    {
                        StopSound();
                    }
                    else
                    {
                        _curWord.PlaySound(getApplication());
                    }
                }
            });
        }

    }

    //get data
    private void init(){
        int id = getIntent().getIntExtra(DBSQL.WORD_ID_KEY_PUT_EXTRA,0);
        _curWord = WordList.getWordById(id);


        // bind view
        editTen = findViewById(R.id.editDetailTen);
        editMota = findViewById(R.id.editDetailMota);
        imageDetail = findViewById(R.id.imageDetail);
        imageButtonSound = findViewById(R.id.imageButtonSound);

        WordNote = findViewById(R.id.textNote);
        txtNote = findViewById(R.id.txtNote);


        //Toast.makeText(getApplicationContext(),"id= " + WordTen,Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btnEdit)
    public void btnEdit(View view){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Edit");
        dialog.setContentView(R.layout.edit_word);
        dialog.show();

        Button btnUpdate = dialog.findViewById(R.id.btnEdit);
        Button btncancel = dialog.findViewById(R.id.btnCancel);

        final EditText editten = dialog.findViewById(R.id.editTen);
        final EditText editmota = dialog.findViewById(R.id.editMota);

        editten.setText(_curWord.getTen());
        editmota.setText(_curWord.getMota());
        //TODO: add edit image + sound

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTen.getText().toString().equals("")==true||editMota.getText().toString().equals("")==true){
                    Toast.makeText(Detail_Word.this,"Điền đầy đủ thông tin...",Toast.LENGTH_SHORT).show();
                }
                else {
                    //Word word = new Word(editten.getText().toString(),editmota.getText().toString());
                    ContentValues row = new ContentValues();
                    row.put("Ten",editten.getText().toString());
                    row.put("Mota",editmota.getText().toString());
                    MainActivity.database.update("WordDatabase",row,"id=?",new String[]{String.valueOf(_curWord.getId())});
                    MainActivity.database.close();
                    dialog.dismiss();
                    //Toast.makeText(getApplicationContext(),"id= " + WordId,Toast.LENGTH_SHORT).show();
                    Toast.makeText(Detail_Word.this,"Update Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Detail_Word.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @OnClick(R.id.btnDelete)
    public void btnDelete(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông Báo");
        builder.setMessage("Bạn có chắc muốn Xóa ?");

        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WordList.removeWordFromDBById(_curWord.getId());
                Toast.makeText(getApplicationContext(),"Delete Successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Detail_Word.this,MainActivity.class));
            }
        });

        builder.show();
    }


    @OnClick(R.id.btnInfo)
    public void btnInfo(View view){
        final Dialog dialog = new Dialog(Detail_Word.this);
        dialog.setContentView(R.layout.detail_dialog);
        dialog.setTitle("About");
        dialog.show();

        final TextView txtDialogTen = dialog.findViewById(R.id.txtDialogTen);
        final TextView txtDialogMota = dialog.findViewById(R.id.txtDialogMota);
        final TextView txtNote = dialog.findViewById(R.id.txtNote);
        ImageButton btnNote = dialog.findViewById(R.id.btnNote);
        ImageButton btnDialogSound = dialog.findViewById(R.id.btnDialogSound);

        txtDialogTen.setText(_curWord.getTen());
        txtDialogMota.setText(_curWord.getMota());


        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.note_dialog);
                dialog.setTitle("Note");
                dialog.show();

                Button btnAddNote = dialog.findViewById(R.id.btnAddNote);
                Button btnCancelNote = dialog.findViewById(R.id.btnCancelNote);

                btnAddNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WordNote.setText(txtNote.getText());
                        dialog.dismiss();
                    }
                });
            }
        });
        //Toast.makeText(getApplicationContext(),"ss",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // nếu out ra mà nhạc còn phát thì tắt
        StopSound();
        super.onWindowFocusChanged(hasFocus);
    }

    private void StopSound()
    {
        if(Word.mediaPlayer.isPlaying())
        {
            Word.StopPlayingSound();

            //imageButtonSound.setBackgroundResource(R.drawable.ic_play_sound);
        }

    }
}
