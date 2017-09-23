package ru.sfedu.nanotechnology.controller;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexFunc {
    public HashMap<Double, Double> checkXinSomeDegr(String function) {

        // We get some function
        //String function = "-x^-3.5+x^2-3.5x+5.5";


        // Hashmap<Coefficient, Degree>
        HashMap<Double, Double> mapOfXinDegree = new HashMap<>();


        //      First Part
        // For scanning x in some degree
        // Example: -64x^31 -25.5x^-2.5
        Pattern pattern = Pattern.compile("(\\+|\\-)?(\\d*(\\.\\d*)?)x\\^(\\-)?(\\d*(\\.\\d*)?)");
        Matcher matcher = pattern.matcher(function);
        // I write here all matchers that will be found
        // And count them from it
        ArrayList<String> listingMatchers = new ArrayList<>();

        int j = 0;
        while (matcher.find()) {
            listingMatchers.add(function.substring(matcher.start(), matcher.end()));
            j++;
        }
        double coef;
        double power;
        for (int i = 0; i < listingMatchers.size(); i++) {
            String arg = listingMatchers.get(i);
            String[] numbers = arg.split("x\\^");

            // Here some conditions to prevent some unusual coef
            if (numbers[0].isEmpty() || numbers[0].equals("+")) {
                coef = 1;
            } else if (numbers[0].equals("-")) {
                coef = -1;
            } else {
                coef = Double.parseDouble(numbers[0]);
            }
            // power doesn't need any corrections
            power = Double.parseDouble(numbers[1]);
            // And finally right what we have in our map
            mapOfXinDegree.put(coef, power);
        }
        return mapOfXinDegree;
    }

    public double checkXinFirstDegr(String function) {
        // I write here all matchers that will be found
        // And count them from it
        ArrayList<String> listingMatchers = new ArrayList<>();

        //      Second Part
        // For scanning x in First degree only
        // It is much easier
        // Example: +64x -25.5x
        Pattern pattern = Pattern.compile("(\\+|\\-)?(\\d*(\\.\\d*)?)x([^\\^]|$)");
        Matcher matcher = pattern.matcher(function);
        int j = 0;
        while (matcher.find()) {
            listingMatchers.add(function.substring(matcher.start(), matcher.end()));
            j++;
        }

        double coef = 0;
        for (int i = 0; i < listingMatchers.size(); i++) {
            String arg = listingMatchers.get(i);
            String[] numbers = arg.split("x");
            // The same conditions
            if (numbers[0].isEmpty() || numbers[0].equals("+")) {
                coef = 1;
            } else if (numbers[0].equals("-")) {
                coef = -1;
            } else {
                coef = Double.parseDouble(numbers[0]);
            }
        }
        return coef;
    }

    public double checkFreeCoef(String function) {
        // I write here all matchers that will be found
        // And count them from it
        ArrayList<String> listingMatchers = new ArrayList<>();

        // Free coef
        double freeCoef = 0;

        //      Third(Last) Part
        // Scan free coef
        //Example: -55.5' +55.5'
        listingMatchers.clear();

        // Check if free coef is in the begining
        Pattern pattern = Pattern.compile("((\\+|\\-)?)(\\d*(\\.\\d*)?)'");
        Matcher matcher = pattern.matcher(function);

        String strFreeCoef = null;
        while (matcher.find())
        {
            strFreeCoef = function.substring(matcher.start(), matcher.end());

        }

            try {
                    String[] strFrCoef= strFreeCoef.split("'");
                freeCoef = Double.parseDouble(strFrCoef[0]);
            } catch (Exception e) {
                // No Free Coef Found
            }
        return freeCoef;
    }
}

