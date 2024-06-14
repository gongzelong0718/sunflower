package com.example.codescanner

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alipay.android.phone.scancode.export.ScanRequest
import com.alipay.android.phone.scancode.export.adapter.MPScan
import com.alipay.android.phone.scancode.export.adapter.MPScanCallbackAdapter
import com.alipay.android.phone.scancode.export.adapter.MPScanError
import com.alipay.android.phone.scancode.export.adapter.MPScanResult
import com.alipay.android.phone.scancode.export.adapter.MPScanStarter


/**
 * Created by xingcheng on 2018/8/8.
 */
class CodeScannerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.scan_title)

        findViewById<View>(R.id.standard_ui_btn).setOnClickListener {
//            startActivity(
//                Intent(
//                    this@ScanActivity,
//                    ScanRequestActivity::class.java
//                )
//            )

            val scanRequest = ScanRequest()
            // 设置提示文字
            scanRequest.setViewText("提示文字");

            // 设置打开手电筒提示文字
            scanRequest.setOpenTorchText("打开手电筒");

            // 设置关闭手电筒提示文字
            scanRequest.setCloseTorchText("关闭手电筒");

            // 设置扫码识别类型
            // 该设置仅对直接扫码生效，对识别相册图片无效
            scanRequest.setRecognizeType(
                ScanRequest.RecognizeType.QR_CODE,    // 二维码
                ScanRequest.RecognizeType.BAR_CODE,   // 条形码
                ScanRequest.RecognizeType.DM_CODE,    // DM 码
                ScanRequest.RecognizeType.PDF417_Code // PDF417 码
            ); // 不设置，则默认识别前三种

            // 设置隐藏相册按钮
            scanRequest.setNotSupportAlbum(true);

            // 设置多码标记图片
//            scanRequest.setMultiMaMarker(R.drawable.green_arrow);
            scanRequest.setMultiMaMarker(com.alipay.android.phone.scancode.export.R.drawable.scan_ray);

            // 设置多码提示文字
            scanRequest.setMultiMaTipText("点击绿色箭头选择码");

            // 设置选中单个码后的圆点颜色
            scanRequest.setMaTargetColor("#32CD32");

            // 开启 AI 识别小码并自动放大，仅 10.2.3 及以上基线支持，需接入扫一扫 AI 组件
            scanRequest.setEnableAI(true);

            // 设置延时提示文案，仅 10.2.3 及以上基线支持
            scanRequest.setDelayTipText("延时x秒弹出toast");

            // 设置延时提示时间，单位毫秒，仅 10.2.3 及以上基线支持
            scanRequest.setDelayTipTime(5000);
            MPScan.startMPaasScanFullScreenActivity(
                this,
                scanRequest,
                object : MPScanCallbackAdapter() {
                    override fun onScanFinish(
                        context: Context, mpScanResult: MPScanResult, mpScanStarter: MPScanStarter
                    ): Boolean {
                        AlertDialog.Builder(context)
                            .setMessage(if (mpScanResult != null) mpScanResult.text else "没有识别到码")
                            .setPositiveButton(R.string.confirm,
                                DialogInterface.OnClickListener { dialog, which -> mpScanStarter.restart() })
                            .create().show()
                        // 返回 false 表示该回调未消费，下次识别继续回调
                        return false
                    }
                })
        }

        findViewById<View>(R.id.window_scanner_ui_btn).setOnClickListener(View.OnClickListener {
            val scanRequest = ScanRequest()
            // 设置扫码页 UI 风格
            scanRequest.setScanType(ScanRequest.ScanType.QRCODE) // 二维码风格
            scanRequest.setScanType(ScanRequest.ScanType.BARCODE) // 条形码风格，默认


            // 设置扫码界面 title
            scanRequest.setTitleText("标准扫码")


            // 设置扫码窗口下提示文字
            scanRequest.setViewText("提示文字")


            // 设置打开手电筒提示文字，仅 10.1.60 及以上基线支持
            scanRequest.setOpenTorchText("打开手电筒")


            // 设置关闭手电筒提示文字，仅 10.1.60 及以上基线支持
            scanRequest.setCloseTorchText("关闭手电筒")


            // 设置扫码识别类型，仅 10.1.60.6+ 和 10.1.68.2+ 基线支持
            // 该设置仅对直接扫码生效，对识别相册图片无效
            scanRequest.setRecognizeType(
                ScanRequest.RecognizeType.QR_CODE,  // 二维码
                ScanRequest.RecognizeType.BAR_CODE,  // 条形码
                ScanRequest.RecognizeType.DM_CODE,  // DM 码
                ScanRequest.RecognizeType.PDF417_Code // PDF417 码
            ) // 不设置，则默认识别前三种


            // 设置透明状态栏（在 Android 4.4+ 系统上生效），仅 10.1.68.15+ 基线支持
            scanRequest.isTranslucentStatusBar = true


            // 设置隐藏相册按钮，仅 10.1.68.22+ 基线支持
            scanRequest.setNotSupportAlbum(true)

            MPScan.startMPaasScanFullScreenActivity(this,
                scanRequest,
                object : MPScanCallbackAdapter() {
                    override fun onScanFinish(
                        context: Context, mpScanResult: MPScanResult, mpScanStarter: MPScanStarter
                    ): Boolean {
                        return true
                    }

                    override fun onScanError(context: Context, error: MPScanError): Boolean {
                        // 识别错误
                        return super.onScanError(context, error)
                    }

                    override fun onScanCancel(context: Context): Boolean {
                        // 识别取消
                        return super.onScanCancel(context)
                    }
                })
        })

        findViewById<View>(R.id.custom_ui_btn).setOnClickListener { scanWithCustomUI() }
        findViewById<View>(R.id.morecode_ui_btn).setOnClickListener { morecode() }
        findViewById<View>(R.id.btn_close_open_torch).setOnClickListener {
            isOpen = !isOpen
            if (isOpen) {
                Utils.toast(this@CodeScannerActivity, "光线暗，自动打开手电筒")
            } else {
                Utils.toast(this@CodeScannerActivity, "光线暗，提示自动打开手电筒")
            }
        }
    }

    private fun scanWithCustomUI() {
        ScanHelper.getInstance().scan(this, object : ScanHelper.ScanCallback {
            override fun onScanResult(isProcessed: Boolean, result: Intent?) {
                if (!isProcessed) {
                    // 扫码界面点击物理返回键或左上角返回键
                    return
                }

                if (result == null || result.data == null) {
                    Toast.makeText(
                        this@CodeScannerActivity, R.string.scan_failure, Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                DialogUtil.alert(this@CodeScannerActivity, result.data.toString())
            }
        })
    }

    private fun morecode() {
        val scanRequest: ScanRequest = ScanRequest()
        MPScan.startMPaasScanFullScreenActivity(
            this,
            scanRequest,
            object : MPScanCallbackAdapter() {
                override fun onScanFinish(
                    context: Context, mpScanResult: MPScanResult, mpScanStarter: MPScanStarter
                ): Boolean {
                    Toast.makeText(
                        applicationContext,
                        if (mpScanResult != null) mpScanResult.getText() else "没有识别到码",
                        Toast.LENGTH_SHORT
                    ).show()
                    (context as Activity).finish()
                    // 返回 true 表示该回调已消费，不需要再次回调
                    return true
                }
            })
    }

    companion object {
        var isOpen: Boolean = false
    }
}