package com.itsadate.iad_a;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;


public class ListPreferenceMultiSelect extends ListPreference {
    // Need to make sure the SEPARATOR is unique and weird enough that it
    // doesn't match one of the entries.
    // Not using any fancy symbols because this is interpreted as a regex for
    // splitting strings.
    /** The Constant SEPARATOR. */
    private static final String SEPARATOR = ",";

    /**
     * Parses the stored value.
     *
     * @param val
     *            the val
     *
     * @return the string[]
     */
    public static String[] parseStoredValue(final CharSequence val) {
        if (val == null) {
            return null;
        } else if ("".equals(val)) {
            return null;
        } else {
            return ((String) val).split(ListPreferenceMultiSelect.SEPARATOR);
        }
    }

    /** The clicked dialog entry indices. */
    private boolean[] mClickedDialogEntryIndices;

    /**
     * Instantiates a new list preference multi select.
     *
     * @param context
     *            the context
     */
    public ListPreferenceMultiSelect(final Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new list preference multi select.
     *
     * @param context
     *            the context
     * @param attrs
     *            the attrs
     */
    public ListPreferenceMultiSelect(final Context context,
                                     final AttributeSet attrs) {
        super(context, attrs);

        mClickedDialogEntryIndices = new boolean[getEntries().length];
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult) {
        // super.onDialogClosed(positiveResult);
        // Update the Calendar list according to the user selections
        final CharSequence[] entryValues = getEntryValues();
        if (positiveResult && (entryValues != null)) {
            final StringBuffer value = new StringBuffer();
            for (int i = 0; i < entryValues.length; i++) {
                if (mClickedDialogEntryIndices[i]) {
                    value.append(entryValues[i]).append(
                            ListPreferenceMultiSelect.SEPARATOR);
                }
            }

            if (callChangeListener(value)) {
                String val = value.toString();
                if (val.length() > 0) {
                    val = val.substring(0, val.length()
                            - ListPreferenceMultiSelect.SEPARATOR.length());
                }
                setValue(val);
            }
        }
        restoreCheckedEntries();
    }

    @Override
    protected void onPrepareDialogBuilder(final Builder builder) {
        final CharSequence[] entries = getEntries();
        final CharSequence[] entryValues = getEntryValues();

        if ((entries == null) || (entryValues == null)
                || (entries.length != entryValues.length)) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array which are both the same length");
        }

        restoreCheckedEntries();
        builder.setMultiChoiceItems(entries, mClickedDialogEntryIndices,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int which, final boolean val) {
                        mClickedDialogEntryIndices[which] = val;
                    }
                });
    }

    private void restoreCheckedEntries() {
        final CharSequence[] entryValues = getEntryValues();

        final String[] vals = ListPreferenceMultiSelect
                .parseStoredValue(getValue());
        if (vals != null) {
            for (final String val2 : vals) {
                final String val = val2.trim();
                for (int i = 0; i < entryValues.length; i++) {
                    final CharSequence entry = entryValues[i];
                    if (entry.equals(val)) {
                        mClickedDialogEntryIndices[i] = true;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void setEntries(final CharSequence[] entries) {
        super.setEntries(entries);
        mClickedDialogEntryIndices = new boolean[entries.length];
    }

}
