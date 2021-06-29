package com.worth.framework.base.view.activity

import android.graphics.drawable.AnimationDrawable
import android.util.TypedValue
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.worth.framework.R
import com.worth.framework.base.core.base.activity.BaseActivity
import com.worth.framework.base.core.base.constants.ARouterPath.DIALOG_ACTIVITY
import com.worth.framework.base.core.utils.LDBus
import com.worth.framework.base.core.utils.LDBus.sendSpecial
import com.worth.framework.base.view.dialog.SearchDialog
import com.worth.framework.business.ext.*
import com.worth.framework.business.global.mQuickList
import com.worth.framework.business.global.speakFinishWhenWakeUpCall
import com.worth.framework.business.utils.RecordUtils
import com.worth.framework.business.utils.SpeakUtils
import com.worth.framework.business.utils.WakeUpUtils
import com.worth.framework.databinding.SdkActivityDialogLayoutBinding

@Route(path = DIALOG_ACTIVITY, name = "全局的弹窗dialogActivity")
class MainDialogActivity : BaseActivity<SdkActivityDialogLayoutBinding>() {
    private var dialog: SearchDialog? = null

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

        LDBus.observer(MAIN_DIALOG_RE_WAKE_UP) {                    //  再次唤起
            if (!isFinishing && !isDestroyed) {
                binding?.sdkTvCenterDef?.visibility = View.VISIBLE
                binding?.sdkTvCenterDef1?.visibility = View.VISIBLE
                binding?.ivBottom?.visibility = View.VISIBLE

                binding?.sdkTvCenterRef?.visibility = View.GONE
                binding?.sdkTvCenterRef?.text = ""
                binding?.sdkTvCenterContent?.visibility = View.GONE
                binding?.sdkTvCenterContent?.text = ""
            }
        }
        LDBus.observer(ASR_EXIT) {                                  //  语音对话结束
            if (!isFinishing && !isDestroyed) {
                dialog?.dismiss()
                finish()
            }
        }
        LDBus.observer(NET_WORK_REQUEST_START) {                    //  网络请求开始
            if (!isFinishing && !isDestroyed) {
                binding?.sdkIvLoading?.visibility = View.VISIBLE
            }
        }

        LDBus.observer(NET_WORK_REQUEST_FINISH) {                   //  网络请求返回
            runOnUiThread {
                if (!isFinishing && !isDestroyed) {
                    binding?.sdkIvLoading?.visibility = View.GONE
                }
            }
        }

        LDBus.observer(EVENT_WITH_USER_INPUT_RESULT) {
            if (!isFinishing && !isDestroyed) {
                binding?.sdkTvCenterDef?.visibility = View.GONE
                binding?.sdkTvCenterDef1?.visibility = View.GONE
                binding?.ivBottom?.visibility = View.GONE

                it?.run {                       //  网络请求返回后发送LDBus进行事件的回调
                    binding?.sdkTvCenterRef?.visibility = View.VISIBLE
                    binding?.sdkTvCenterRef?.text = it.toString()
                }
            }
        }

        LDBus.observer(EVENT_WITH_INPUT_ASR_RESULT) {
            if (!isFinishing && !isDestroyed) {
                binding?.sdkTvCenterDef?.visibility = View.GONE
                binding?.sdkTvCenterDef1?.visibility = View.GONE
                binding?.ivBottom?.visibility = View.GONE
                it?.run {                                           //  网络请求ASR的返回结果
                    binding?.sdkTvCenterContent?.visibility = View.VISIBLE
                    setContent(it.toString())
                }
            }
        }
    }

    override fun initViewAndListener() {
        binding?.run {
            (sdkIvLoading?.background as AnimationDrawable).start()
            sdkIvTopRight.setOnClickListener {
                finish()
            }
        }
    }

    private fun setContent(text: String) {
        binding?.run {
            sdkTvCenterContent.text = "\"$text\" >"
            val len = sdkTvCenterContent.length()
            when {
                len < 28 -> {
                    sdkTvCenterContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                }
                len in 28..54 -> {
                    sdkTvCenterContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
                }
                else -> {
                    sdkTvCenterContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                }
            }
            sdkTvCenterContent.setOnClickListener {
                dialog = SearchDialog(this@MainDialogActivity)
                dialog?.run {
                    setTags(mQuickList)
                    show()
                }
            }
        }
    }

    override fun initData() {
        val msg = intent.getStringExtra("input_msg")
        msg?.let { sendSpecial(EVENT_WITH_INPUT_ASR_RESULT, it) } //  发送ars识别的结果给页面进行展示
    }

    override fun onDestroy() {
        if (speakFinishWhenWakeUpCall) {                                                //  是被wakeUp唤醒
            WakeUpUtils.ins().startListener()
            SpeakUtils.ins().stopSpeak()
            RecordUtils.ins().stopRecord()
            RecordUtils.ins().cancel()
        } else {                                                                        //  网络返回的发声
            SpeakUtils.ins().stopSpeak()
            RecordUtils.ins().stopRecord()
            RecordUtils.ins().cancel()
//            WakeUpUtils.ins().stopListener()
            WakeUpUtils.ins().startListener()
        }
        super.onDestroy()
    }
}