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
import com.worth.framework.base.view.dialog.SearchDialog
import com.worth.framework.business.enter.VipSdkHelper
import com.worth.framework.business.ext.*
import com.worth.framework.business.global.mQuickList
import com.worth.framework.databinding.SdkActivityDialogLayoutBinding

@Route(path = DIALOG_ACTIVITY, name = "全局的弹窗dialogActivity")
class MainDialogActivity : BaseActivity<SdkActivityDialogLayoutBinding>() {
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
            sdkTvCenterContent.setOnClickListener(View.OnClickListener {
                val dialog = SearchDialog(this@MainDialogActivity)
                dialog.setTags(mQuickList)
                dialog.show()
            })
        }
    }

    override fun initData() {

    }
}