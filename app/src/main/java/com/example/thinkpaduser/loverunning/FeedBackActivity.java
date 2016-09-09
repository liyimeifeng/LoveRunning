package com.example.thinkpaduser.loverunning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBackActivity extends AppCompatActivity {
    private int numText = 300;//最多要求的字数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        final TextView textView = (TextView) findViewById(R.id.activity_feed_back_text_view);
        final EditText editText = (EditText)findViewById(R.id.activity_feed_back_edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = numText - s.length();
                //textView显示剩余的字数
                textView.setText("" + number + "/300");
                selectionStart = editText.getSelectionStart();
                selectionEnd = editText.getSelectionEnd();
                if (wordNum.length() > numText){
                    //删除多余输入的字，不显示出来
                    s.delete(selectionStart-1,selectionEnd);
                    int temSelection = selectionEnd;
                    editText.setText(s);
                    editText.setSelection(temSelection);//设置光标在最后
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.advice_commit){
            Toast.makeText(FeedBackActivity.this,"点击提交",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.advice_commit, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
