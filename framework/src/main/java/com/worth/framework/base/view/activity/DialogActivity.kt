package com.worth.framework.base.view.activity

import android.graphics.drawable.AnimationDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.worth.framework.R
import com.worth.framework.base.core.base.activity.BaseActivity
import com.worth.framework.base.core.base.constants.ARouterPath.DIALOG_ACTIVITY
import com.worth.framework.base.core.utils.LDBus
import com.worth.framework.base.view.dialog.SearchDialog
import com.worth.framework.business.ext.EVENT_WITH_INPUT_ASR_RESULT
import com.worth.framework.business.ext.EVENT_WITH_USER_INPUT_RESULT
import com.worth.framework.databinding.SdkActivityDialogLayoutBinding

@Route(path = DIALOG_ACTIVITY, name = "全局的弹窗dialogActivity")
class DialogActivity : BaseActivity<SdkActivityDialogLayoutBinding>() {
    override fun initBefore() {

    }

    override fun injectARouter() {
        ARouter.getInstance().inject(this)
    }

    override fun initArgs() {

    }

    override fun initLayoutId(): Int {
        return R.layout.sdk_activity_dialog_layout
    }

    override fun initObserve() {
        LDBus.observer(EVENT_WITH_USER_INPUT_RESULT) {
            if (!isFinishing && !isDestroyed) {
                it?.run {                                   //  网络请求返回后发送LDBus进行事件的回调

                }
            }
        }

        LDBus.observer(EVENT_WITH_INPUT_ASR_RESULT) {
            if (!isFinishing && !isDestroyed) {
                it?.run {                                   //  网络请求ASR的返回结果

                }
            }
        }
    }

    override fun initViewAndListener() {

        binding?.run {
            (sdkIvLoading?.background as AnimationDrawable).start()
            setContent("我曾经跨过山和大海，也曾经穿过人山人,我曾经跨过山和大海，也曾经穿过人山人我曾经跨过山和大海，也曾经穿过人山人")
        }
    }

    private fun setContent(text: String) {
        binding?.run {
            sdkTvCenterContent.text = "\"$text\" >"
            val len = sdkTvCenterContent.length()
            when {
                len < 28 -> {
                    sdkTvCenterContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
                }
                len in 28..54 -> {
                    sdkTvCenterContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
                }
                else -> {
                    sdkTvCenterContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                }
            }
            sdkTvCenterContent.setOnClickListener(View.OnClickListener {
                val list = mutableListOf<String>()
                list.add("我谁")
                list.add("我是谁1")
                list.add("我我是谁1是谁2")
                list.add("我谁3")
                list.add("我是谁我是谁14")
                list.add("我是我是谁1我是谁1谁5")
                list.add("我是我是谁谁6")
                list.add("我是谁7")
                list.add("我是我是谁1我是谁1我是谁1谁8")
                val dialog = SearchDialog(this@DialogActivity)
                dialog.setTags(list)
                dialog.show()
            })
        }
    }

    override fun initData() {

    }
}