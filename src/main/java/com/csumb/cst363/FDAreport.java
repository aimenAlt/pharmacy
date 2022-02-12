package com.csumb.cst363;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Scanner;

public class FDAreport {

    public static String databaseUrl = "jdbc:mysql://localhost:3306/project1";
    public static String databaseUser = "root";
    public static String databasePassword = "testing123";


    public static void main(String[] args) throws SQLException {
        //Accept drug name, start date string and end date string
        //create method that connects to the database, use pased in method and return the result.
        //create method that will connect to the database (via the method created) get the data requested then close it.
        String drugName = "";
        String startDate = "";
        String endDate = "";
        boolean valid = false;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input the drug name");
        while(true) {
            drugName = getStringInput(scan);
            if(validateName(drugName)) break;
            else {
                System.out.println("Invalid name. Please input proper drug name");
            }
        }
        System.out.println("Please input the start date for your search: (Format: YYYY-MM-DD)");
        while(true) {
            startDate = getStringInput(scan);
            if(validateDate(startDate)) break;
            else {
                System.out.println("Invalid Date. Please input proper start date");
            }
        }
        System.out.println("Please input the end date for your search: (Format: YYYY-MM-DD)");
        while(true) {
            endDate = getStringInput(scan);
            if(validateDate(endDate)) break;
            else {
                System.out.println("Invalid Date. Please input proper end date");
            }
        }
        getFromDatabase(drugName, startDate, endDate);
    }

//    public interface Query {
//        HashMap<>
//    }

//    public static <T, K> HashMap<T, K> getFromDatabase() {
//
//    }

    public static String getStringInput(Scanner scan) {
        return scan.nextLine();
    }

    public static void getFromDatabase(String drugName, String startDate, String endDate) throws SQLException {

        HashMap<String, Integer> drugQty = null;
        PreparedStatement stmt;
        String query = "SELECT doctor.name, SUM(prescription.quantity) AS drug_quantity FROM doctor INNER JOIN prescription ON doctor.ssn = prescription.doctor_ssn WHERE prescription.drug_name LIKE ? AND (prescription.request_date between ? AND ?) GROUP BY doctor.doctor_id";
        try (Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            stmt = con.prepareStatement(query);
            stmt.setString(1, "%" + drugName + "%");
            stmt.setString(2, startDate);
            stmt.setString(3, endDate);
            ResultSet results = stmt.executeQuery();
            drugQty = new HashMap<String, Integer>();
            System.out.println("Doctor's name" + "\t|\t" + "Drug Qty");
            System.out.println("_____________" + "\t|\t" + "________");
            while(results.next()) {
//                System.out.println(results.getString(1));
//                drugQty.put(results.getString(1), Integer.parseInt(results.getString(2)));
//                System.out.println(results.getString(2));
                System.out.println(results.getString(1) + "\t|\t" + results.getString(2));
//                String tempLot = new String(results.getInt(1),
//                        results.getString(2), results.getDouble(3),
//                        new Date(results.getString(4)), results.getString(5));;
//                lots.put(tempLot.getLotID(), tempLot);
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public static boolean validateDate(String date) {
        try {
            DateFormat df = new SimpleDateFormat(date);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateName(String name) {
        char[] chars = name.toCharArray();
        for(char c : chars){
            if(Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

}


