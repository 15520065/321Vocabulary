package com.example.phanhuuchi.huydaoduc.test.Main;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phanhuuchi.huydaoduc.test.R;
import com.example.phanhuuchi.huydaoduc.test.model.Word;
import com.example.phanhuuchi.huydaoduc.test.model.WordList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by PhanHuuChi on 1/12/2017.
 */


public class Exam_Activity extends AppCompatActivity {

    // answer button
    List<Button> _buttonList;
    List<Button> _curButtonList;
    boolean _listFlag;          // cho biết đã lấy list button thứ nhất hay thứ hai, do có 2 bộ

    // question view
    TextView _questionText;
    ImageButton _questionSound;
    ImageView _questionImage;

    List<Word> _wordList;
    List<Integer> _wordIndexSelectedList;           // danh sach những từ đã được hỏi ở lần trước

    boolean _isShowingReasult;

    public Exam_Activity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_);

        init();
        check();

    }

    private void init() {
        _listFlag = true;
        _isShowingReasult = false;

        _wordList = WordList.getWordList();
        _curButtonList = new ArrayList<>();

        // dùng để tránh trường hợp 1 từ xuất hiện quá nhiều lần
        _wordIndexSelectedList = new ArrayList<>();
        addButtonAnswerToList();


        // bind view
        _questionText = findViewById(R.id.txtQuestion);
        _questionImage = findViewById(R.id.imaQuestion);
        _questionSound = findViewById(R.id.imaBtnQuestionSound);
    }

    private void OnRestart()
    {
        // an toan bo ds
        for (Button btn : _buttonList)
        {
            btn.setVisibility(View.INVISIBLE);
        }
        // lay list btn hien tai
        _curButtonList.clear();
        if(_listFlag)
        {
            for (int i = 0; i < 4; i++) {
                _curButtonList.add(_buttonList.get(i));
            }
            _listFlag = false;
        }
        else
        {
            for (int i = 4; i < 8; i++) {
                _curButtonList.add(_buttonList.get(i));
            }
            _listFlag = true;
        }


        // button default
        for (Button btn : _curButtonList) {
            btn.setText("");
            // set default background color
            btn.setBackgroundResource(R.drawable.background_btn);
            btn.setVisibility(View.VISIBLE);
        }

        // lấy danh sách từ
        _wordList = WordList.getWordList();
    }

    /*
    Vì 1 Word có thể có Image, sound, translate text --> nên khi làm ktr
    ta có thể lấy bất kì Image hay sound hay text ra kiểm tra
    hàm này sẽ lấy ngẫu nhiên
    */
    private void getQuestion(final Word word)
    {
        _questionText.setVisibility(View.INVISIBLE);
        _questionImage.setVisibility(View.INVISIBLE);
        _questionSound.setVisibility(View.INVISIBLE);

        Random r = new Random();
        final int rI = r.nextInt(3);
        if(word.getImage() != null && rI == 0)
        {
            // use image
            _questionImage.setImageBitmap(word.getImageBitmap());
            _questionImage.setVisibility(View.VISIBLE);

        }
        else if (word.getSound() != null && rI == 1)
        {
            // use sound
            _questionSound.setVisibility(View.VISIBLE);
            _questionSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Word.mediaPlayer.isPlaying())
                        StopSound();
                    else
                    {
                        word.PlaySound(getApplication());
                        //_questionSound.setBackgroundResource(R.drawable.ic_stop_sound);
                    }
                }
            });
        }
        else
        {
            // use text
            _questionText.setText(word.getMota());
            _questionText.setVisibility(View.VISIBLE);
        }
    }

    private void check() {
        OnRestart();

        int btnCount = 4;          // số lượng button cần dùng
        if (_wordList.size() < 4)
        {
            // nếu số lượng ít hơn 4 thì đặt lại n
            btnCount = _wordList.size();

            // ẩn và loại những button k cần thiết - do số lượng từ đếm từ 1, còn button đếm từ 0 nên làm cẩn thận
            for (int i = btnCount; i < _wordList.size(); i++) {
                _curButtonList.get(i).setVisibility(View.INVISIBLE);
                _curButtonList.remove(_curButtonList.get(i));

            }
        }

        // animation : sau khi xác định đc ds btn hiện tại ta mới chạy animation
        BtnAnimationIn();

        // lấy 1 btn
        Random r = new Random();
        final int rBt = r.nextInt(btnCount);

        //// Lấy 1 Word bất kì
        int curentIndex =  -1;
        if (_wordIndexSelectedList.size() == _wordList.size())
        {
            // nếu ds đủ số lượng từ hiện tại thì ds sẽ chạy lại 1 vòng mới
            _wordIndexSelectedList.clear();
        }
        // xem từ lấy ra đã có chưa nếu có rồi thì chạy lại tìm từ mới
        boolean flag = true;
        while (flag) {
            flag = false;
            curentIndex = r.nextInt(_wordList.size());
            for (int selectedIndex : _wordIndexSelectedList) {
                if (selectedIndex == curentIndex) {
                    flag = true;
                }
            }
        }
        _wordIndexSelectedList.add(curentIndex);


        int temp[] = {-1, -1, -1, -1};          // biến lưu giá trị từ hiện tại của btn
        temp[rBt] = curentIndex;

        final long endTime = 1400;      // thời gian thể hiện kết quả trước khi chạy cái mới

        for (int i = 0; i < btnCount; i++)
        {
            if (i == rBt) {
                getQuestion(_wordList.get(curentIndex));
                _curButtonList.get(rBt).setText(_wordList.get(curentIndex).getMota());

                // set action
                _curButtonList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!_isShowingReasult)
                        {
                            Toast.makeText(getApplicationContext(), "ss", Toast.LENGTH_LONG).show();
                            view.setBackgroundColor(Color.GREEN);

                            // timer -- millis: thời gian timer chạy; Interval : cứ bao nhiêu s thì ontick đc gọi
                            final CountDownTimer start = new CountDownTimer(endTime, endTime) {
                                @Override
                                public void onTick(long l) {
                                }

                                public void onFinish()
                                {
                                    BtnAnimationOut();
                                    check();
                                }
                            }.start();
                            _isShowingReasult = true;
                        }

                    }
                });

            } else {
                flag = true;
                int rWord = -1;
                while (flag) {
                    flag = false;
                    // lấy 1 từ bất kì
                    rWord = r.nextInt(_wordList.size());
                    // xem từ lấy ra đã có chưa nếu có rồi thì chạy lại tìm từ mới
                    for (int btnIndex = 0; btnIndex < btnCount; btnIndex++) {

                        if (rWord == temp[btnIndex]) {
                            flag = true;
                        }
                    }
                }

                _curButtonList.get(i).setText(_wordList.get(rWord).getMota());

                // set action
                _curButtonList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!_isShowingReasult)
                        {
                            Toast.makeText(getApplicationContext(), "wrong", Toast.LENGTH_SHORT).show();
                            view.setBackgroundColor(Color.RED);
                            _curButtonList.get(rBt).setBackgroundColor(Color.GREEN);

                            final CountDownTimer start = new CountDownTimer(endTime, endTime) {
                                @Override
                                public void onTick(long l) {
                                }

                                public void onFinish() {
                                    BtnAnimationOut();
                                    check();
                                }
                            }.start();
                            _isShowingReasult = true;
                        }
                    }
                });

                temp[i] = rWord;
            }
        }

    }


    private void addButtonAnswerToList() {
        _buttonList = new ArrayList<>();
        _buttonList.add((Button) findViewById(R.id.btnAnswer1));
        _buttonList.add((Button) findViewById(R.id.btnAnswer2));
        _buttonList.add((Button) findViewById(R.id.btnAnswer3));
        _buttonList.add((Button) findViewById(R.id.btnAnswer4));

        // dãy 2
        _buttonList.add((Button) findViewById(R.id.btnAnswer5));
        _buttonList.add((Button) findViewById(R.id.btnAnswer6));
        _buttonList.add((Button) findViewById(R.id.btnAnswer7));
        _buttonList.add((Button) findViewById(R.id.btnAnswer8));

        // an toan bo ds
        for (Button btn : _buttonList)
        {
            btn.setVisibility(View.INVISIBLE);
        }

    }


    private void StopSound()
    {
        if(Word.mediaPlayer.isPlaying())
        {
            Word.StopPlayingSound();
            //_questionSound.setBackgroundResource(R.drawable.ic_play_sound);
        }
    }


    //// ANIMATION
    private void BtnAnimationOut()
    {
        StopSound();

        Animation trans = AnimationUtils.loadAnimation(this,R.anim.transition_button_out);
        for (Button btn : _curButtonList) {
            btn.setVisibility(View.GONE);

            btn.setAnimation(trans);

        }
        trans.start();
        trans.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                _isShowingReasult = false;
            }
        });
    }

    private void BtnAnimationIn()
    {
        Animation trans = AnimationUtils.loadAnimation(this,R.anim.transition_button_in);
        for (Button btn : _curButtonList) {
            btn.setAnimation(trans);
        }
        trans.start();
    }

}


