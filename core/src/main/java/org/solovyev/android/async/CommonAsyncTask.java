package org.solovyev.android.async;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.collections.JCollections;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * User: serso
 * Date: 5/29/12
 * Time: 11:39 PM
 */
public abstract class CommonAsyncTask<Param, Progress, R> extends AsyncTask<Param, Progress, CommonAsyncTask.Result<R>> {

    @NotNull
    private static final String TAG = "CommonAsyncTask";

    @NotNull
    private final WeakReference<Context> contextRef;

    private boolean mask;

    @Nullable
    private AlertDialog dialog;

    protected CommonAsyncTask(@NotNull Context context) {
        this(context, false);
    }

    protected CommonAsyncTask(@NotNull Context context, boolean mask) {
        this.mask = mask;
        this.contextRef = new WeakReference<Context>(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        final Context context = getContext();
        if (context != null && mask) {
            dialog = ProgressDialog.show(context, "Loading...", "Loading...", true, false);
        }
    }

    @Override
    protected final Result<R> doInBackground(Param... params) {
        try {
            return new CommonResult<R>(doWork(JCollections.asList(params)));
        } catch (CommonAsyncTaskRuntimeException e) {
            return new CommonResult<R>(e.getException());
        } catch (Exception e) {
            return new CommonResult<R>(e);
        }
    }

    protected abstract R doWork(@NotNull List<Param> params);

    @Override
    protected final void onPostExecute(@NotNull Result<R> r) {
        super.onPostExecute(r);

        if (dialog != null) {
            dialog.dismiss();
        }

        if (r.isFailure()) {
            onFailurePostExecute(r.getFailureResult());
        } else {
            onSuccessPostExecute(r.getSuccessResult());
        }

    }

    @Nullable
    protected Context getContext() {
        return contextRef.get();
    }

    protected abstract void onSuccessPostExecute(@Nullable R result);

    protected abstract void onFailurePostExecute(@NotNull Exception e);

    private static class CommonResult<SR> implements Result<SR> {

        @Nullable
        private SR successResult;

        @Nullable
        private Exception failureResult;

        public CommonResult() {
        }

        public CommonResult(@Nullable SR result) {
            this.successResult = result;
        }

        public CommonResult(@NotNull Exception e) {
            this.failureResult = e;
        }

        @Override
        @Nullable
        public SR getSuccessResult() {
            return successResult;
        }

        @Nullable
        @Override
        public Exception getFailureResult() {
            return this.failureResult;
        }

        @Override
        public boolean isFailure() {
            return this.failureResult != null;
        }
    }

    protected static interface Result<SR> {

        boolean isFailure();

        @Nullable
        Exception getFailureResult();

        @Nullable
        SR getSuccessResult();
    }

    protected void throwException(@NotNull Exception e) {
        throw new CommonAsyncTaskRuntimeException(e);
    }

    protected void defaultOnFailurePostExecute(@NotNull Exception e) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private static class CommonAsyncTaskRuntimeException extends RuntimeException {

        @NotNull
        private Exception exception;

        public CommonAsyncTaskRuntimeException(@NotNull Exception exception) {
            this.exception = exception;
        }

        @NotNull
        public Exception getException() {
            return exception;
        }
    }
}
