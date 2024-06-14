package com.google.samples.apps.sunflower

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.alipay.mobile.common.rpc.RpcException
import com.alipay.mobile.framework.service.common.TaskScheduleService
import com.mpaas.mgs.adapter.api.MPRpc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RpcMainActivity : BaseActivity(), View.OnClickListener {
    private var client: HTTP_API_Group1Client? = null
    private val taskService: TaskScheduleService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rpc_main)

        // 获取 client 实例
        client = MPRpc.getRpcProxy(HTTP_API_Group1Client::class.java)

        //        taskService = MPFramework.getExternalService(TaskScheduleService.class.getName());
        initView()
        initRpcConfig()
    }

    private fun initView() {
        findViewById<View>(R.id.btn_get).setOnClickListener(this)
    }

    private fun initRpcConfig() {
        val rpcInvokeContext = MPRpc.getRpcInvokeContext(client)

        // 设置超时时间，单位ms
        rpcInvokeContext.setTimeout(60000)

        // 设置网关地址，如果不设置将以 portal 工程中的 AndroidManifest.xml 为准
//        String gwUrl = "https://cn-hangzhou-mgs-gw.cloud.alipay.com/mgw.htm";
//        rpcInvokeContext.setGwUrl(gwUrl);

        //设置请求头
        val headerMap: MutableMap<String, String> = HashMap()
        headerMap["key1"] = "val1"
        headerMap["key2"] = "val2"
        rpcInvokeContext.requestHeaders = headerMap

        // 设置rpc拦截器
        // 全局设置，可在 MockLauncherApplicationAgent.postInit() 中设置
//        MPRpc.addRpcInterceptor(OperationType.class, new CommonInterceptor());
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_get -> testGet()
            else -> {}
        }
    }

    private fun testGet() {
        // rpc请求是同步的，这里使用了 mpaas 框架提供的线程池
        // 开发者也可使用自己的线程池
//        taskService.parallelExecute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // 设置请求
////                    ArticleList0JsonGetReq req = new ArticleList0JsonGetReq();
////                    req.name = "123";
////                    req.pass = "123";
//                    String response = client.flowerGetnameGet();
//                    if (response != null) {
//                        showToast(20210715, response);
//                    }
//                } catch (RpcException e) {
//                    e.printStackTrace();// 处理 RPC 请求异常
//                    showToast(e.getCode(), e.getMsg());
//                    Log.i("RpcException", " GET code: " + e.getCode() + " msg: " + e.getMsg());
//                }
//            }
//        }, "rpc-get");

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                try {
                    // 设置请求
                    val response = client?.flowerGetnameGet();
                    if (response != null) {
                        showToastUsingCoroutine(20210715, response);
                    }
                } catch (e : RpcException) {
                    e.printStackTrace();// 处理 RPC 请求异常
                    showToastUsingCoroutine(e.getCode(), e.getMsg());
                    Log.i("RpcException", " GET code: " + e.getCode() + " msg: " + e.getMsg());
                }
            }
        }

    }

    private fun showToast(code: Int, msg: String) {
        runOnUiThread {
            if (code == 20210715) {
                Toast.makeText(this@RpcMainActivity, msg, Toast.LENGTH_SHORT).show()
            } else {
                val error = "CODE : $code\nMSG : $msg"
                Toast.makeText(this@RpcMainActivity, error, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private suspend fun showToastUsingCoroutine(code: Int, msg: String) {
        withContext(Dispatchers.Main) {
            if (code == 20210715) {
                Toast.makeText(this@RpcMainActivity, msg, Toast.LENGTH_SHORT).show()
            } else {
                val error = "CODE : $code\nMSG : $msg"
                Toast.makeText(this@RpcMainActivity, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}