/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.async;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.solovyev.android.core.R;
import org.solovyev.common.collections.Collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * User: serso
 * Date: 5/29/12
 * Time: 11:39 PM
 */
public abstract class CommonAsyncTask<PARAM, PROGRESS, RESULT> extends AsyncTask<PARAM, PROGRESS, CommonAsyncTask.Result<RESULT>> {

    @Nonnull
    private static final String TAG = "CommonAsyncTask";

    @Nonnull
    private final WeakReference<Context> contextRef;

    @Nullable
    private final MaskParams maskParams;

    @Nullable
    private AlertDialog dialog;

    protected CommonAsyncTask() {
        this.maskParams = null;
        this.contextRef = new WeakReference<Context>(null);
    }

    protected CommonAsyncTask(@Nonnull Context context) {
        this.maskParams = null;
        this.contextRef = new WeakReference<Context>(context);
    }

    protected CommonAsyncTask(@Nonnull Context context, @Nonnull MaskParams maskParams) {
        this.maskParams = maskParams;
        this.contextRef = new WeakReference<Context>(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        final Context context = getContext();
        if (context != null && maskParams != null) {
            dialog = maskParams.show(context);
        }
    }

    @Override
    protected final Result<RESULT> doInBackground(PARAM... params) {
        try {
            return new CommonResult<RESULT>(doWork(Collections.asList(params)));
        } catch (CommonAsyncTaskRuntimeException e) {
            return new CommonResult<RESULT>(e.getException());
        } catch (Exception e) {
            return new CommonResult<RESULT>(e);
        }
    }

    protected abstract RESULT doWork(@Nonnull List<PARAM> params);

    @Override
    protected final void onPostExecute(@Nonnull Result<RESULT> r) {
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

    protected abstract void onSuccessPostExecute(@Nullable RESULT result);

    protected abstract void onFailurePostExecute(@Nonnull Exception e);

    private static class CommonResult<SR> implements Result<SR> {

        @Nullable
        private SR successResult;

        @Nullable
        private Exception failureResult;

        public CommonResult(@Nullable SR result) {
            this.successResult = result;
        }

        public CommonResult(@Nonnull Exception e) {
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

    protected void throwException(@Nonnull Exception e) {
        throw new CommonAsyncTaskRuntimeException(e);
    }

    protected void defaultOnFailurePostExecute(@Nonnull Exception e) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private static class CommonAsyncTaskRuntimeException extends RuntimeException {

        @Nonnull
        private Exception exception;

        public CommonAsyncTaskRuntimeException(@Nonnull Exception exception) {
            this.exception = exception;
        }

        @Nonnull
        public Exception getException() {
            return exception;
        }
    }

    public static final class MaskParams {

        private final int titleResId;

        private final int messageResId;

        private boolean indeterminate = true;

        private boolean cancelable = false;

        private MaskParams(int titleResId, int messageResId) {
            this.titleResId = titleResId;
            this.messageResId = messageResId;
        }

        @Nonnull
        public static MaskParams newInstance(int titleResId, int messageResId) {
            return new MaskParams(titleResId, messageResId);
        }

        @Nonnull
        public static MaskParams newDefault() {
            return new MaskParams(R.string.acl_loading, R.string.acl_loading);
        }

        public void setIndeterminate(boolean indeterminate) {
            this.indeterminate = indeterminate;
        }

        public void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }

        @Nonnull
        public AlertDialog show(@Nonnull Context context) {
            final String title = context.getString(titleResId);
            final String message = context.getString(messageResId);
            return ProgressDialog.show(context, title, message, indeterminate, cancelable);
        }
    }
}
