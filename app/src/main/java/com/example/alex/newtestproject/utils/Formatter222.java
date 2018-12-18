///*
// * Copyright (C) 2006 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.example.alex.newtestproject.utils;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.Resources;
//import android.icu.text.DecimalFormat;
//import android.icu.text.MeasureFormat;
//import android.icu.text.NumberFormat;
//import android.icu.text.UnicodeSet;
//import android.icu.text.UnicodeSetSpanner;
//import android.icu.util.Measure;
//import android.icu.util.MeasureUnit;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.text.BidiFormatter;
//import android.text.TextUtils;
//import android.view.View;
//
//import java.lang.reflect.Constructor;
//import java.math.BigDecimal;
//import java.util.Locale;
//
///**
// * Utility class to aid in formatting common values that are not covered
// * by the {@link java.util.Formatter} class in {@link java.util}
// */
//@SuppressLint("NewApi")
//public final class Formatter222 {
//
//    /** {@hide} */
//    public static final int FLAG_DEFAULT = 0;
//    /** {@hide} */
//    public static final int FLAG_SHORTER = 1 << 0;
//    /** {@hide} */
//    public static final int FLAG_CALCULATE_ROUNDED = 1 << 1;
//
//    /** {@hide} */
//    public static class BytesResult {
//        public final String value;
//        public final String units;
//        public final long roundedBytes;
//
//        public BytesResult(String value, String units, long roundedBytes) {
//            this.value = value;
//            this.units = units;
//            this.roundedBytes = roundedBytes;
//        }
//    }
//
//    private static MeasureUnit createPetaByte() {
//        try {
//            Constructor<MeasureUnit> constructor = MeasureUnit.class
//                    .getDeclaredConstructor(String.class, String.class);
//            constructor.setAccessible(true);
//            return constructor.newInstance("digital", "petabyte");
//        } catch (ReflectiveOperationException e) {
//            throw new RuntimeException("Failed to create petabyte MeasureUnit", e);
//        }
//    }
//
//    /**
//     * ICU doesn't support PETABYTE yet. Fake it so that we can treat all units the same way.
//     */
//    private static final MeasureUnit PETABYTE = createPetaByte();
//
//
//    public static String formatFileSize(@Nullable Context context, long sizeBytes) {
//        return formatFileSize(context, sizeBytes, FLAG_DEFAULT);
//    }
//
//    private static String formatFileSize(@Nullable Context context, long sizeBytes, int flags) {
//        if (context == null) {
//            return "";
//        }
//        final RoundedBytesResult res = RoundedBytesResult.roundBytes(sizeBytes, flags);
//        return bidiWrap(context, formatRoundedBytesResult(context, res));
//    }
//
//    private static NumberFormat getNumberFormatter(Locale locale, int fractionDigits) {
//        final NumberFormat numberFormatter = NumberFormat.getInstance(locale);
//        numberFormatter.setMinimumFractionDigits(fractionDigits);
//        numberFormatter.setMaximumFractionDigits(fractionDigits);
//        numberFormatter.setGroupingUsed(false);
//        if (numberFormatter instanceof DecimalFormat) {
//            // We do this only for DecimalFormat, since in the general NumberFormat case, calling
//            // setRoundingMode may throw an exception.
//            numberFormatter.setRoundingMode(BigDecimal.ROUND_HALF_UP);
//        }
//        return numberFormatter;
//    }
//
//    private static Locale localeFromContext(@NonNull Context context) {
//        return context.getResources().getConfiguration().getLocales().get(0);
//    }
//
//    private static String formatRoundedBytesResult(
//            @NonNull Context context, @NonNull RoundedBytesResult input) {
//        final Locale locale = localeFromContext(context);
//        final NumberFormat numberFormatter = getNumberFormatter(locale, input.fractionDigits);
//        if (input.units == MeasureUnit.BYTE || input.units == PETABYTE) {
//            // ICU spells out "byte" instead of "B", and can't format petabytes yet.
//            final String formattedNumber = numberFormatter.format(input.value);
//            return context.getString(com.android.internal.R.string.fileSizeSuffix,
//                    formattedNumber, getSuffixOverride(context.getResources(), input.units));
//        } else {
//            return formatMeasureShort(locale, numberFormatter, input.value, input.units);
//        }
//    }
//
//    private static String getSuffixOverride(@NonNull Resources res, MeasureUnit unit) {
//        if (unit == MeasureUnit.BYTE) {
//            return res.getString(com.android.internal.R.string.byteShort);
//        } else { // unit == PETABYTE
//            return res.getString(com.android.internal.R.string.petabyteShort);
//        }
//    }
//
//    private static String formatMeasureShort(Locale locale, NumberFormat numberFormatter,
//                                             float value, MeasureUnit units) {
//        final MeasureFormat measureFormatter = MeasureFormat.getInstance(
//                locale, MeasureFormat.FormatWidth.SHORT, numberFormatter);
//        return measureFormatter.format(new Measure(value, units));
//    }
//
//    /**
//     * Wraps the source string in bidi formatting characters in RTL locales.
//     */
//    private static String bidiWrap(@NonNull Context context, String source) {
//        final Locale locale = localeFromContext(context);
//        if (TextUtils.getLayoutDirectionFromLocale(locale) == View.LAYOUT_DIRECTION_RTL) {
//            return BidiFormatter.getInstance(true /* RTL*/).unicodeWrap(source);
//        } else {
//            return source;
//        }
//    }
//
//    private static class RoundedBytesResult {
//        public final float value;
//        public final MeasureUnit units;
//        public final int fractionDigits;
//        public final long roundedBytes;
//
//        private RoundedBytesResult(
//                float value, MeasureUnit units, int fractionDigits, long roundedBytes) {
//            this.value = value;
//            this.units = units;
//            this.fractionDigits = fractionDigits;
//            this.roundedBytes = roundedBytes;
//        }
//
//        /**
//         * Returns a RoundedBytesResult object based on the input size in bytes and the rounding
//         * flags. The result can be used for formatting.
//         */
//        static RoundedBytesResult roundBytes(long sizeBytes, int flags) {
//            final boolean isNegative = (sizeBytes < 0);
//            float result = isNegative ? -sizeBytes : sizeBytes;
//            MeasureUnit units = MeasureUnit.BYTE;
//            long mult = 1;
//            if (result > 900) {
//                units = MeasureUnit.KILOBYTE;
//                mult = 1024;
//                result = result / 1024;
//            }
//            if (result > 900) {
//                units = MeasureUnit.MEGABYTE;
//                mult *= 1024;
//                result = result / 1024;
//            }
//            if (result > 900) {
//                units = MeasureUnit.GIGABYTE;
//                mult *= 1024;
//                result = result / 1024;
//            }
//            if (result > 900) {
//                units = MeasureUnit.TERABYTE;
//                mult *= 1024;
//                result = result / 1024;
//            }
//            if (result > 900) {
//                units = PETABYTE;
//                mult *= 1024;
//                result = result / 1024;
//            }
//            // Note we calculate the rounded long by ourselves, but still let NumberFormat compute
//            // the rounded value. NumberFormat.format(0.1) might not return "0.1" due to floating
//            // point errors.
//            final int roundFactor;
//            final int roundDigits;
//            if (mult == 1 || result >= 100) {
//                roundFactor = 1;
//                roundDigits = 0;
//            } else if (result < 1) {
//                roundFactor = 100;
//                roundDigits = 2;
//            } else if (result < 10) {
//                if ((flags & FLAG_SHORTER) != 0) {
//                    roundFactor = 10;
//                    roundDigits = 1;
//                } else {
//                    roundFactor = 100;
//                    roundDigits = 2;
//                }
//            } else { // 10 <= result < 100
//                if ((flags & FLAG_SHORTER) != 0) {
//                    roundFactor = 1;
//                    roundDigits = 0;
//                } else {
//                    roundFactor = 100;
//                    roundDigits = 2;
//                }
//            }
//
//            if (isNegative) {
//                result = -result;
//            }
//
//            // Note this might overflow if abs(result) >= Long.MAX_VALUE / 100, but that's like
//            // 80PB so it's okay (for now)...
//            final long roundedBytes =
//                    (flags & FLAG_CALCULATE_ROUNDED) == 0 ? 0
//                            : (((long) Math.round(result * roundFactor)) * mult / roundFactor);
//
//            return new RoundedBytesResult(result, units, roundDigits, roundedBytes);
//        }
//    }
//
//}
