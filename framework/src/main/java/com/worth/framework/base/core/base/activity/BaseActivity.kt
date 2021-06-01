package com.worth.framework.base.core.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * BaseActivity
 */
abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    var binding: T? = null

    abstract fun initBefore()

    abstract fun injectARouter()

    abstract fun initArgs()

    @LayoutRes
    abstract fun initLayoutId(): Int

    /**
     * 初始化liveData中的数据、eventBus的数据
     */
    abstract fun initObserve()

    /**
     * 初始化页面的view信息和监听事件
     */
    abstract fun initViewAndListener()

    /**
     * 从网络获取数据或数据库的入口
     */
    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        initBefore()
        super.onCreate(savedInstanceState)
        injectARouter()
        initArgs()
        binding = DataBindingUtil.setContentView(this, initLayoutId())
        initObserve()
        lifecycleScope.launchWhenCreated {
            initViewAndListener()
            initData()
        }
    }

    fun exec(function: () -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            function()
        }
    }
}