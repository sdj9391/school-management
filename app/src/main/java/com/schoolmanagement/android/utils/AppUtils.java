package com.schoolmanagement.android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.schoolmanagement.R;
import com.schoolmanagement.android.models.ApiError;
import com.schoolmanagement.android.restapis.AppApiInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;

public class AppUtils {
    static ProgressDialog mProgressDialog;

    /**
     * Gets the version name of the application. For e.g. 1.9.3
     **/
    public static String getApplicationVersionNumber(Context context) {

        String versionName = null;

        if (context == null) {
            return versionName;
        }

        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    /**
     * Gets the version code of the application. For e.g. Maverick Meerkat or 2013050301
     **/
    public static int getApplicationVersionCode(Context ctx) {

        int versionCode = 0;

        try {
            versionCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * Shows a long time duration toast message.
     *
     * @param msg Message to be show in the toast.
     * @return Toast object just shown
     **/
    public static Toast showToast(Context ctx, CharSequence msg) {
        return showToast(ctx, msg, Toast.LENGTH_LONG);
    }

    /**
     * Shows the message passed in the parameter in the Toast.
     *
     * @param msg      Message to be show in the toast.
     * @param duration Duration in milliseconds for which the toast should be shown
     * @return Toast object just shown
     **/
    public static Toast showToast(Context ctx, CharSequence msg, int duration) {
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        toast.setDuration(duration);
        toast.show();
        return toast;
    }

    /**
     * Checks if the Internet connection is available.
     *
     * @return Returns true if the Internet connection is available. False otherwise.
     **/
    public static boolean isInternetAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // if network is NOT available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Shows a progress dialog with a spinning animation in it. This method must preferably called
     * from a UI thread.
     *
     * @param ctx           Activity context
     * @param title         Title of the progress dialog
     * @param body          Body/Message to be shown in the progress dialog
     * @param isCancellable True if the dialog can be cancelled on back button press, false otherwise
     **/
    public static void showProgressDialog(Context ctx, String title, String body, boolean isCancellable) {
        showProgressDialog(ctx, title, body, null, isCancellable);
    }

    /**
     * Shows a progress dialog with a spinning animation in it. This method must preferably called
     * from a UI thread.
     *
     * @param ctx           Activity context
     * @param title         Title of the progress dialog
     * @param body          Body/Message to be shown in the progress dialog
     * @param icon          Icon to show in the progress dialog. It can be null.
     * @param isCancellable True if the dialog can be cancelled on back button press, false otherwise
     **/
    public static void showProgressDialog(Context ctx, String title, String body, Drawable icon, boolean isCancellable) {
        if (ctx instanceof Activity) {
            if (!((Activity) ctx).isFinishing()) {
                mProgressDialog = ProgressDialog.show(ctx, title, body, true);
                mProgressDialog.setIcon(icon);
                mProgressDialog.setCancelable(isCancellable);
            }
        }
    }

    /**
     * Check if the {@link ProgressDialog} is visible in the UI.
     **/
    public static boolean isProgressDialogVisible() {
        return (mProgressDialog != null);
    }

    /**
     * Dismiss the progress dialog if it is visible.
     **/
    public static void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    /**
     * Checks if the parameter string is null or empty.
     * Space is considered as empty.
     *
     * @param string
     * @return true if null or zero-length.
     */
    public static boolean isEmpty(String string) {
        return TextUtils.isEmpty(string)
                || string.length() == 0
                || string.trim().length() == 0;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null && activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    /**
     * TODO: 11/28/16 move to internet utils
     *
     * @param response
     * @return
     */
    private static ApiError parseApiMessage(Response response) {

        ApiError error = new ApiError();
        if (response == null) {
            return error;
        }

        Converter<ResponseBody, ApiError> converter =
                AppApiInstance.getRetrofit()
                        .responseBodyConverter(ApiError.class, new Annotation[0]);

        try {
            ResponseBody errorBody = response.errorBody();
            if (errorBody != null) {
                error = converter.convert(errorBody);
            }
        } catch (IOException e) {
            DebugLog.e("Failed to parse error response.");
        }

        return error;
    }

    @NonNull
    /**
     * TODO: 11/28/16 move to internet utils
     * Parses the error response to extract meaningful error message from it.
     * In case a message extracted is empty returns a generic message.
     *
     * @param context
     * @param response if null, message returned will be "No response"
     * @return
     */
    public static String parse(@NonNull Context context, @Nullable Response response) {
        String message = parseApiMessage(response).getMessage();

        if (isEmpty(message)) {
            message = context.getString(parse(response));
        }

        return message;
    }

    /**
     * Parses the error response from json object.
     * In case a message extracted is empty returns a generic message from using status code.
     *
     * @param context
     * @param jsonObject
     * @param statusCode
     * @return
     */
    public static String parseFromJson(@NonNull Context context, @NonNull JSONObject jsonObject, int statusCode) {
        String message = null;
        try {
            if (jsonObject != null) {
                message = jsonObject.getString("message");
            } else {
                DebugLog.v("JSON object is null, hence further status code will use to show message.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isEmpty(message)) {
            message = context.getString(parse(statusCode));
        }

        return message;
    }

    @NonNull
    /**
     * Parses the error response to extract meaningful error message from it.
     * Usually called from {@link retrofit2.Callback#onFailure(Call, Throwable)}
     * In case a message extracted is empty returns a generic message.
     * @param throwable parameter usually received from Retrofit.
     * @return
     */
    public static String parse(Context context, @Nullable Throwable throwable) {

        // could not determine what the error was, show generic message.
        String message = context.getString(R.string.error_general);

        if (throwable == null) {
            return message;
        }

        message = throwable.getMessage();

        DebugLog.e("message: " + message);

        if (!isEmpty(message)) {

            // Throwable error message for Android OS version below 5.0 is "Failed to connect"
            // And for 5.0 & above contains "Unable to resolve host"
            if (message.contains("Unable to resolve host")
                    || message.contains("Failed to connect")) {
                message = context.getString(R.string.msg_no_internet);
            }
        }

        return message;
    }

    @NonNull
    /**
     *
     */
    private static int parse(Response response) {

        int statusCode = -1;
        if (response != null) {
            statusCode = response.code();
        }

        return parse(statusCode);
    }

    @StringRes
    /**
     *
     * @param statusCode If -1, "No response" message will be returned.
     * @return String resource ID
     */
    public static int parse(int statusCode) {

        if (statusCode == -1) {
            return R.string.error_server_no_response;
        }

        if (statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
            return R.string.error_server_400bad_request;
        } else if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            return R.string.error_server_401unaunthorized;
        } else if (statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
            return R.string.error_server_403forbidden;
        } else if (statusCode == HttpURLConnection.HTTP_BAD_METHOD) {
            return R.string.error_server_405forbidden;
        } else if (statusCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            return R.string.error_server_500internal_error;
        } else if (statusCode == HttpURLConnection.HTTP_UNAVAILABLE) {
            return R.string.error_server_503unavailable;
        } else {
            return R.string.error_general;
        }
    }

    /**
     * To convert date into particular date format.
     *
     * @param date
     * @param format
     * @return date as string
     */
    public static String formatDateForDisplay(long date, String format) {
        if (isEmpty(format)) {
            return null;
        }

        SimpleDateFormat DATE_DISPLAY_FORMATTER = new SimpleDateFormat(format);
        return DATE_DISPLAY_FORMATTER.format(new Date(date));
    }

    /**
     * Creates a confirmation dialog with Yes-No Button. By default the buttons just dismiss the
     * dialog.
     *
     * @param ctx
     * @param message     Message to be shown in the dialog.
     * @param yesListener Yes click handler
     * @param noListener
     **/
    public static void showConfirmDialog(Context ctx, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        showConfirmDialog(ctx, message, yesListener, noListener, "Yes", "No");
    }

    /**
     * Creates a confirmation dialog with Yes-No Button. By default the buttons just dismiss the
     * dialog.
     *
     * @param ctx
     * @param message     Message to be shown in the dialog.
     * @param yesListener Yes click handler
     * @param noListener
     * @param yesLabel    Label for yes button
     * @param noLabel     Label for no button
     **/
    public static void showConfirmDialog(Context ctx, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener, String yesLabel, String noLabel) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        if (yesListener == null) {
            yesListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }

        if (noListener == null) {
            noListener = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }

        builder.setMessage(message).setPositiveButton(yesLabel, yesListener).setNegativeButton(noLabel, noListener).show();
    }

    /**
     * Creates a confirmation dialog that show a pop-up with button labeled as parameters labels.
     *
     * @param ctx                 {@link Activity} {@link Context}
     * @param message             Message to be shown in the dialog.
     * @param dialogClickListener
     * @param positiveBtnLabel    For e.g. "Yes"
     * @param negativeBtnLabel    For e.g. "No"
     **/
    public static void showDialog(Context ctx, String message, String positiveBtnLabel, String negativeBtnLabel, DialogInterface.OnClickListener dialogClickListener) {

        if (dialogClickListener == null) {
            throw new NullPointerException("Action listener cannot be null");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(message).setPositiveButton(positiveBtnLabel, dialogClickListener).setNegativeButton(negativeBtnLabel, dialogClickListener).show();
    }

    /**
     * Formats given size in bytes to KB, MB, GB or whatever. This will work up to 1000 TB
     */
    public static String formatSize(long size) {

        if (size <= 0) return "0";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * Formats given size in bytes to KB, MB, GB or whatever. Preferably use this method for
     * performance efficiency.
     *
     * @param si Controls byte value precision. If true, formatting is done using approx. 1000 Uses
     *           1024 if false.
     **/
    public static String formatSize(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;

        if (bytes < unit) {
            return bytes + " B";
        }

        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableResId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
