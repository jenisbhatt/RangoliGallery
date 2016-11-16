package utils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.Html;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Jenis on 3/7/2016
 */
public class MatrialAlertDialogHelper {
    // states using confirm dialog box
    public static final int CONFIRM_REDIRECT = 1;
    public static final int CONFIRM_FINISH = 2;
    public static final int CONFIRM_DISMISS = 3;

    public static MaterialDialog mMaterialDialog = null;


    private MatrialAlertDialogHelper() {
    }


    /**
     * Confirm dialog box which can be used for calling another activity, finish
     * current activity and return to last activity and dismiss the confirm
     * dialog box. (confirm dialog box - Redirect, Finish, Dismiss)
     *
     * @param title           - dialog box title
     * @param msg             - dialog box message (HTML formatted text)
     * @param source          - calling activity
     * @param destination     - activity to be called using Intent
     * @param action          - to check states of dialog box (REDIRECT, FINISH, DISMISS)
     * @param positiveBtnText - text to set for positive button
     */
    public static void confirmDialog(String title, String msg,
                                     final Activity source, final Class<?> destination,
                                     final int action, final String positiveBtnText) {

//        final AlertDialog.Builder dialog =
//                new AlertDialog.Builder(source);

        final MaterialDialog.Builder dialog = new MaterialDialog.Builder(source);


//        final AlertDialog alertDialog = dialog.show();
//        dialog.setCancelable(false);
//        dialog.setTitle(title);
//        dialog.setMessage(Html
//                .fromHtml(msg));

        final MaterialDialog alertDialog = dialog.show();
        dialog.cancelable(false);
        dialog.title(title);
        dialog.content(Html
                .fromHtml(msg));


//        if (positiveBtnText != null) {
//            dialog.setPositiveButton(positiveBtnText, null);
//        } else {
//            dialog.setPositiveButton("Ok", null);
//        }

        if (positiveBtnText != null) {
            dialog.positiveText(positiveBtnText);
        } else {
            dialog.positiveText("Ok");
        }


//        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (action) {
//                    case CONFIRM_REDIRECT:
//                        alertDialog.dismiss();
//                        if (destination != null) {
//                            Intent intent = new Intent(source, destination);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            source.startActivity(intent);
//                        }
//                        break;
//
//                    case CONFIRM_FINISH:
//                        alertDialog.dismiss();
//                        source.finish();
//                        break;
//
//                    case CONFIRM_DISMISS:
//                        alertDialog.dismiss();
//                        break;
//
//                    default:
//                        alertDialog.dismiss();
//                        break;
//                }
//            }
//        });
//
//        dialog.show();

        dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                switch (action) {
                    case CONFIRM_REDIRECT:
                        alertDialog.dismiss();
                        if (destination != null) {
                            Intent intent = new Intent(source, destination);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            source.startActivity(intent);
                        }
                        break;

                    case CONFIRM_FINISH:
                        alertDialog.dismiss();
                        source.finish();
                        break;

                    case CONFIRM_DISMISS:
                        alertDialog.dismiss();
                        break;

                    default:
                        alertDialog.dismiss();
                        break;
                }
            }
        });

        dialog.show();

    }

    /**
     * ALERT BOX WITH TWO BUTTON 'RETRY' - executing AsyncTask class AND
     * 'CANCEL' - to finish/dismiss current activity
     *
     * @param title           - dialog box title
     * @param msg             - dialog box message
     * @param source          - calling activity
     * @param asyncClass      - AsyncTask to be executed
     * @param action          - to check states of dialog box (FINISH, DISMISS)
     * @param positiveBtnText - text to set for positive button
     */
    public static void alertRetryAndCancelFinish(String title, String msg,
                                                 final Activity source,
                                                 final AsyncTask<String, Void, String> asyncClass, final int action,
                                                 final String positiveBtnText) {

        try {
//            final AlertDialog.Builder dialog =
//                    new AlertDialog.Builder(source);

            final MaterialDialog.Builder dialog = new MaterialDialog.Builder(source);

//            final AlertDialog alertDialog = dialog.show();
//            dialog.setCancelable(false);
//            dialog.setTitle(title);
//            dialog.setMessage(Html
//                    .fromHtml(msg));

            final MaterialDialog alertDialog = dialog.show();
            dialog.cancelable(false);
            dialog.title(title);
            dialog.content(Html
                    .fromHtml(msg));

//            if (positiveBtnText != null) {
//                dialog.setPositiveButton(positiveBtnText, null);
//            } else {
//                dialog.setPositiveButton("RETRY", null);
//            }

            if (positiveBtnText != null) {
                dialog.positiveText(positiveBtnText);
            } else {
                dialog.positiveText("RETRY");
            }
            dialog.negativeText("CANCEL");


//            dialog.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    asyncClass.execute();
//                }
//            });

            dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    alertDialog.dismiss();
                    asyncClass.execute();
                }
            });

//            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    switch (action) {
//                        case CONFIRM_FINISH:
//                            alertDialog.dismiss();
//                            source.finish();
//                            break;
//
//                        case CONFIRM_DISMISS:
//                            alertDialog.dismiss();
//                            break;
//
//                        default:
//                            alertDialog.dismiss();
//                            break;
//                    }
//                }
//            });
//            dialog.show();


            dialog.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    switch (action) {
                        case CONFIRM_FINISH:
                            alertDialog.dismiss();
                            source.finish();
                            break;

                        case CONFIRM_DISMISS:
                            alertDialog.dismiss();
                            break;

                        default:
                            alertDialog.dismiss();
                            break;
                    }
                }
            });
            dialog.show();

        } catch (Exception e) {

        }
    }
}
