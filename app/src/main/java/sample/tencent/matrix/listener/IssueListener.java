package sample.tencent.matrix.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iget.datareporter.DataReporter;
import com.tencent.matrix.plugin.DefaultPluginListener;
import com.tencent.matrix.report.Issue;
import com.tencent.matrix.util.MatrixLog;

import sample.tencent.matrix.report.IssueWrap;

public class IssueListener extends DefaultPluginListener {

    public static final String TAG = "IssueListener";

    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    private long mNativeReporter;

    public IssueListener(Context context, long nativeReporter) {
        super(context);
        mNativeReporter = nativeReporter;
    }

    @Override
    public void onReportIssue(final Issue issue) {
        super.onReportIssue(issue);
        MatrixLog.e(TAG, issue.toString());
        final Gson gson = new GsonBuilder().create();
        MatrixLog.e(TAG, gson.toJson(new IssueWrap(issue)));

        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DataReporter.push(mNativeReporter, gson.toJson(new IssueWrap(issue)).getBytes());
            }
        }, 200);
    }
}