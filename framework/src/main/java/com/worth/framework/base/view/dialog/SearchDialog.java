package com.worth.framework.base.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.worth.framework.R;

import java.util.List;

import me.next.tagview.TagCloudView;

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    6/6/21 --> 8:30 PM
 * Description: This is SearchDialog
 */
public class SearchDialog extends Dialog implements TextView.OnEditorActionListener {
    private Context mContext;

    public SearchDialog(@NonNull Context context) {
        super(context, R.style.dialog_bottom_full);
        this.mContext = context;
        init();
    }

    public SearchDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    protected SearchDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
        init();
    }

    public void init() {
        setCanceledOnTouchOutside(true);    //  手指触碰到外界取消
        setCancelable(true);                //  可取消 为true
        Window window = getWindow();        //  得到dialog的窗体
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.share_animation);
        View view = View.inflate(mContext, R.layout.sdk_dialog_search_layout, null); //获取布局视图
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
        initView();
    }

    EditText sdk_et_input;
    ImageView sdk_iv_close;
    TextView sdk_tv_guess_say;
    TagCloudView tagCloudView;

    private void initView() {
        sdk_iv_close = findViewById(R.id.sdk_iv_close);
        sdk_et_input = findViewById(R.id.sdk_et_input);
        sdk_tv_guess_say = findViewById(R.id.sdk_tv_guess_say);
        tagCloudView = findViewById(R.id.sdk_rl_search_view);

        //设置软键盘回车键事件监听
        sdk_et_input.setOnEditorActionListener(this);
        sdk_iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setTags(List<String> tags) {
        TagCloudView tagView = findViewById(R.id.sdk_rl_search_view);
        tagView.setTags(tags);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // actionId 当前事件组件的资源ID，用来区分多个EditText用同一个监听器
        // event 事件源，封装了当前操作动作
        // EditorInfo.IME_ACTION_GO 判断当前回车键 在xml中设置的类型
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEND:
                Log.e("onEditorAction:", "IME_ACTION_SEND");
                dismiss();
                break;
        }
        return false;
    }
}
