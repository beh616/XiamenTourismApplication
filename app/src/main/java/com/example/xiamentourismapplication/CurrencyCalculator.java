package com.example.xiamentourismapplication;

import java.text.DecimalFormat;

public class CurrencyCalculator {
    private static final String AUD = "Australian Dollar (AUD)";
    private static final String EUR = "Euro (EUR)";
    private static final String KRW = "South Korean Won (KRW)";
    private static final String MYR = "Malaysian Ringgit (MYR)";
    private static final String USD = "United States Dollar (USD)";
    private static final String SGD = "Singapore Dollar (SGD)";
    private static final String TWD = "New Taiwan Dollar (TWD)";

//    currency exchange rate on 29 Dec 2021, 1 foreign money to x chinese yuan
    public static final double AUDToCNY = 4.60;
    public static final double EURToCNY = 7.20;
    public static final double KRWToCNY = 0.0054;
    public static final double MYRToCNY = 1.52;
    public static final double USDToCNY = 6.37;
    public static final double SGDToCNY = 4.71;
    public static final double TWDToCNY = 0.23;


    public String convertCurrency(double amount, String currency_type)
    {
        final DecimalFormat df = new DecimalFormat("0.00");
        double convertedCurrency = 0;
        switch(currency_type)
        {
            case AUD:
                convertedCurrency = amount * AUDToCNY;
                break;
            case EUR:
                convertedCurrency = amount * EURToCNY;
                break;
            case KRW:
                convertedCurrency = amount * KRWToCNY;
                break;
            case MYR:
                convertedCurrency = amount * MYRToCNY;
                break;
            case USD:
                convertedCurrency = amount * USDToCNY;
                break;
            case SGD:
                convertedCurrency = amount * SGDToCNY;
                break;
            case TWD:
                convertedCurrency = amount * TWDToCNY;
                break;
            default:
                convertedCurrency = 0;
        }
        return df.format(convertedCurrency);
    }


}
