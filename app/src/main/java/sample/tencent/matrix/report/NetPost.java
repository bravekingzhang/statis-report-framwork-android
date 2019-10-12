package sample.tencent.matrix.report;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.iget.datareporter.DataReporter;
import com.iget.datareporter.IReport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NetPost implements IReport {

    private long mNativeReporter;
    private Object lock[] = new Object[0];
    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    public NetPost() {
    }

    public void setNativeReporter(long nativeReporter) {
        synchronized (lock) {
            mNativeReporter = nativeReporter;
        }
    }

    @Override
    public void upload(final long key, final byte[][] data) {
        //模拟网络上报
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    if (data != null) {
                        for (int i = 0; i < data.length; i++) {
                            if (data[i] != null) {
                                out.write(data[i]);
                            }
                        }
                        IssueWrap issue = new GsonBuilder().create().fromJson(out.toString(), IssueWrap.class);
                        if (issue.getTag().equals(com.tencent.matrix.resource.config.SharePluginInfo.TAG_PLUGIN)) {
                            // TODO: 2019/10/11 更具类型调用不同的接口上报数据
                        } else if (issue.getTag().equals(com.tencent.matrix.trace.config.SharePluginInfo.TAG_PLUGIN)) {

                        } else if (issue.getTag().equals(com.tencent.matrix.trace.config.SharePluginInfo.TAG_PLUGIN_EVIL_METHOD)) {

                        } else if (issue.getTag().equals(com.tencent.matrix.trace.config.SharePluginInfo.TAG_PLUGIN_FPS)) {

                        } else if (issue.getTag().equals(com.tencent.matrix.trace.config.SharePluginInfo.TAG_PLUGIN_STARTUP)) {

                        } else if (issue.getTag().equals(com.tencent.matrix.iocanary.config.SharePluginInfo.TAG_PLUGIN)) {

                        }
                        Log.d("DataReporter:data_", issue.toString());
                        out.close();
                    }
                } catch (IOException e) {

                }
                synchronized (lock) {
                    if (mNativeReporter == 0) {
                        return;
                    }
                    DataReporter.uploadSucess(mNativeReporter, key);
                }
            }
        }, 200);

    }
}
