package com.huebelancer.ccscreener.Helpers;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;

import com.huebelancer.ccscreener.ModelLayer.Database.Models.Stock;
import com.huebelancer.ccscreener.ModelLayer.Enums.Frame;
import com.huebelancer.ccscreener.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mahuebel on 8/15/17.
 */

public class Helpers {

    //region Activity-Fragment helpers

    public static void addFragmentToActivity(FragmentManager manager, Fragment fragment, int frameId) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void replaceFragmentInActivity(FragmentManager manager, Fragment fragment, int frameId) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //endregion

    //region Frame helpers

    public static String frameToLabel(Frame frame) {
        String f;
        switch (frame) {
            case CURRENT_ITM:
                f = Constants.CURRENT_ITM;
                break;
            case CURRENT_OTM:
                f = Constants.CURRENT_OTM;
                break;
            case NEXT_ITM:
                f = Constants.NEXT_ITM;
                break;
            case NEXT_OTM:
                f = Constants.NEXT_OTM;
                break;
            default:
                f = Constants.CURRENT_ITM;
                break;
        }

        return f;
    }

    public static Frame labelToFrame(String frame) {
        Frame f;
        switch (frame) {
            case Constants.CURRENT_ITM:
                f = Frame.CURRENT_ITM;
                break;
            case Constants.CURRENT_OTM:
                f = Frame.CURRENT_OTM;
                break;
            case Constants.NEXT_ITM:
                f = Frame.NEXT_ITM;
                break;
            case Constants.NEXT_OTM:
                f = Frame.NEXT_OTM;
                break;
            default:
                f = Frame.CURRENT_ITM;
                break;
        }

        return f;
    }

    public static String frameLabelToDisplay(String frame) {
        switch (frame) {
            case Constants.CURRENT_ITM:
            case Constants.NEXT_ITM:
                return "In the Money";
            case Constants.CURRENT_OTM:
            case Constants.NEXT_OTM:
                return "Outside the Money";
            default:
                return "In the Money";
        }
    }

    public static boolean frameIsNext(Frame frame) {
        switch (frame) {
            case CURRENT_ITM:
            case CURRENT_OTM:
                return false;
            case NEXT_ITM:
            case NEXT_OTM:
                return true;
            default:
                return false;
        }
    }

    //endregion

    //region Resource helpers

    public static int iconResIdFor(Stock stock, Frame frame) {
        if (stock.hasImpendingEarningsReport(frame)) {
            return R.drawable.ic_alarm_black_24dp;
        } else if (!stock.isAllUpToDate(frame)) {
            return R.drawable.ic_update_black_24dp;
        } else {
            double profitability = stock.profitability(frame);
            if (profitability > 10) {
                return R.drawable.ic_sentiment_very_satisfied_black_24dp;
            } else if (profitability > 2) {
                return R.drawable.ic_sentiment_satisfied_black_24dp;
            } else if (profitability > 1) {
                return R.drawable.ic_sentiment_neutral_black_24dp;
            } else if (profitability > 0) {
                return R.drawable.ic_sentiment_dissatisfied_black_24dp;
            } else {
                return R.drawable.ic_sentiment_very_dissatisfied_black_24dp;
            }
        }
    }

    public static int profitEvalutationIcon(boolean isProfitable) {
        return isProfitable ? R.drawable.ic_check_black_24dp : R.drawable.ic_close_black_24dp;
    }

    public static int probabilityEvaluationIcon(boolean isProbable) {
        return isProbable ? R.drawable.ic_check_black_24dp : R.drawable.ic_close_black_24dp;
    }

    //endregion

    //region Date helpers

    public static String dateDisplay(Date date, SimpleDateFormat formatter) {
        return formatter.format(date);
    }

    public static Date acceptableUpdateDate() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (day == 6 || day == 0 || day == 1 && hour < 15) {
            cal.add(Calendar.DAY_OF_YEAR, -6);
            cal.set(Calendar.HOUR_OF_DAY, 14);
            cal.set(Calendar.MINUTE, 55);
            cal.set(Calendar.SECOND, 0);
            return cal.getTime();

        } else {
            if (hour > 14) {
                cal.set(Calendar.HOUR_OF_DAY, 14);
                cal.set(Calendar.MINUTE, 55);
                cal.set(Calendar.SECOND, 0);
                return cal.getTime();

            } else {
                cal.add(Calendar.DAY_OF_YEAR, -1);
                cal.set(Calendar.HOUR_OF_DAY, 14);
                cal.set(Calendar.MINUTE, 55);
                cal.set(Calendar.SECOND, 0);
                return cal.getTime();

            }
        }
    }

    public static boolean isUpToDate(Date date) {
        return date.after(acceptableUpdateDate()) || date == acceptableUpdateDate();
    }

    //endregion

}
