package com.csumb.cst363;

import java.util.Random;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataGenerate {
	
	public static void main(String[] args) {
		
		int numDoctors = 10;
		int numPatients = 1000;
		int numPrescriptions = 5000;
		int maxYear = 2022;
		int minYear = 1900;
		int maxSSN = 999999999;
		int minSSN = 100000000;
		int maxZip = 99999;
		int minZip = 10000;
		int maxStreet = 9999;
		int minStreet = 1;
		int maxQuantity = 999;
		int minQuantity = 5;
		int counter = 0;
		int failure = 0;
		Random gen;
		int tempInt;
		String tempString;
		String tempString2;
		String tempString3;
		String randomSpecialty;
		String randomFirstName;
		String randomLastName;
		String randomStreet;
		String randomCity;
		String randomState;
	  	SimpleDateFormat simpleDateFormat;
	  	Calendar tempCal;
	  	Date tempDate;
	  	String random_date;
	  	PreparedStatement ps;
	  	ResultSet rs;
	  	
	  	String[] specialties = { "Internal Medicine", "Family Medicine", "Pediatrics", "Orthopedics",
	  			"Dermatology",  "Cardiology", "Gynecology", "Gastroenterology", "Psychiatry", "Oncology" };
	  	String[] firstNames = {"Anthony", "Boston", "Valentino", "Brett", "Darwin", "Charles", 
	  			"Rocco", "Jaxson", "Royce", "Broderick", "Jamar", "Marlon", "Tiara", "Matilda", 
	  			"Madalynn", "Jenny", "Valery", "Breanna", "Elle", "Angela", "Lillianna", "Ashley", 
	  			"Danna", "Dana"};
	  	String[] lastNames = {"Huang", "Turner", "Ware", "Bray", "Hull", "Strickland", 
	  			"Pena", "Kirby", "Mooney", "Mcknight", "Peterson", "Vargas", "Stevenson", 
	  			"Jensen", "Wolf", "Ewing", "Lin", "Arroyo", "Whitaker", "Cantrell"};
	  	String[] streets = {"Haywood Street", "Nightingale Rise", "Dunton Road", "Hill View Gardens", 
	  			"Alloway Drive", "Old Vicarage Lane", "Holly Court", "Jubilee Avenue", "Exeter Crescent", 
	  			"Shortlands", "Brent", "City View", "Hawkesbury Close", "Edensor Road", "College Grove", 
	  			"Burwell Road", "Mace Road", "Dunstall Close", "Haileybury Road", "Sandstone Lane"};
	  	String[] cities = {"South Fulton", "Green Bay", "Burbank", "Renton", "Hillsboro",
	  			"El Cajon", "Tyler", "Davie", "San Mateo", "Brockton", "Concord", "Jurupa Valley",
	  			"Daly City", "Allen", "Rio Rancho", "Rialto", "Woodbridge", "South Bend", "Spokane Valley",
	  			"Norwalk", "Menifee", "Vacaville", "Wichita Falls", "Davenport", "Quincy", "Chico", "Lynn",
	  			"Lee's Summit", "New Bedford", "Federal Way", "Clinton", "Edinburg", "Nampa", "Roanoke"};
	  	String[] states = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", 
	  			"ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", 
	  			"MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", 
	  			"PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};
	  	
	  	
	  	//loop to create the doctors
	  	while (counter < numDoctors) {
	  		
	  		gen = new Random(System.currentTimeMillis());
			
			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project1", "root", "testing123");) {
					
				counter++;
				
				ps = con.prepareStatement("insert into doctor(name, specialty, practice_since_year,  ssn ) values(?, ?, ?, ?)", 
						Statement.RETURN_GENERATED_KEYS);
				
				//name generator
				randomFirstName = firstNames[gen.nextInt(firstNames.length)];
				randomLastName = lastNames[gen.nextInt(lastNames.length)];
				ps.setString(1, randomFirstName+" "+randomLastName);
				
				//specialty generator
				randomSpecialty = specialties[gen.nextInt(specialties.length)];
				ps.setString(2, randomSpecialty);
				
				//practice_since_year in practice generator
				tempInt = gen.nextInt((maxYear - minYear) + 1) + minYear;
				tempString = String.valueOf(tempInt);
				ps.setString(3, tempString);
				
				//ssn generator
					tempInt = gen.nextInt((maxSSN - minSSN) + 1) + minSSN;
					tempString = String.valueOf(tempInt);
					ps.setString(4, tempString);
				  	
					ps.executeUpdate();
					
				}
			catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				counter--;
			}
		}
	  	
	  	counter = 0;
	  	
	  	
	  	//then loop to create the patients using random doctor as primary
	  	while (counter < numPatients) {
			
	  		gen = new Random(System.currentTimeMillis());
	  		
			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project1", "root", "testing123");) {

				counter++;

				//primary id generator
				ps = con.prepareStatement("select doctor_id from doctor order by rand() limit 1");
				rs = ps.executeQuery();
				rs.next();
				tempString = rs.getString(1);

				ps = con.prepareStatement("insert into patient(ssn, name, birthdate, street, city, state, zipcode, primary_id) values(?, ?, ?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(8, tempString);

				//ssn generator
				tempInt = gen.nextInt((maxSSN - minSSN) + 1) + minSSN;
				tempString = String.valueOf(tempInt);
				ps.setString(1, tempString);

				//name generator
				randomFirstName = firstNames[gen.nextInt(firstNames.length)];
				randomLastName = lastNames[gen.nextInt(lastNames.length)];
				ps.setString(2, randomFirstName+" "+randomLastName);

		  		//birthdate  generator
			  	simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
				tempCal = Calendar.getInstance(); //had to change from "instance"
				tempInt = gen.nextInt((maxYear - minYear) + 1) + minYear;
				tempCal.set(Calendar.YEAR,  tempInt);
				tempCal.set(Calendar.DAY_OF_YEAR, 1);
				tempCal.add(Calendar.DAY_OF_YEAR, gen.nextInt(365));
				tempDate = new Date(tempCal.getTimeInMillis());
				random_date = simpleDateFormat.format(tempDate);
				ps.setString(3, random_date);

				//street generator
				tempInt = gen.nextInt((maxStreet - minStreet) + 1) + minStreet;
				tempString = String.valueOf(tempInt);
				randomStreet = streets[gen.nextInt(streets.length)];
				ps.setString(4, tempString+" "+randomStreet);

				//city generator
				randomCity = cities[gen.nextInt(cities.length)];
				ps.setString(5, randomCity);

				//state generator
				randomState = states[gen.nextInt(states.length)];
				ps.setString(6, randomState);

				//zipcode generator
				tempInt = gen.nextInt((maxZip - minZip) + 1) + minZip;
				tempString = String.valueOf(tempInt);
				ps.setString(7, tempString);

				ps.executeUpdate();


			} catch (Exception e) {
				System.out.println(e.getMessage());
				counter--;

				//failsafe in case randomizer is failing to make unique SSNs
				failure++;
				if (failure > 1000) {
					System.exit(0);
				}
			}
			
	  	}
	  	
	  	counter = 0;
		
		//then loop to create the prescriptions
	  	while (counter < numPrescriptions) {
			
	  		gen = new Random(System.currentTimeMillis());
	  		
			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project1", "root", "testing123");) {
				
				counter++;
				
				//patient_ssn prep
				ps = con.prepareStatement("select ssn from patient order by rand() limit 1");
				rs = ps.executeQuery();
				rs.next();
				tempString = rs.getString(1);
				
				//doctor_ssn prep
				ps = con.prepareStatement("select ssn from doctor order by rand() limit 1");
				rs = ps.executeQuery();
				rs.next();
				tempString2 = rs.getString(1);
				
				//drug_name prep
				ps = con.prepareStatement("select trade_name from drug order by rand() limit 1");
				rs = ps.executeQuery();
				rs.next();
				tempString3 = rs.getString(1);

				//prepare insert statement
				ps = con.prepareStatement("insert into prescription(quantity, patient_ssn, doctor_ssn, drug_name, request_date) values(?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				
				ps.setString(2, tempString);
				ps.setString(3, tempString2);
				ps.setString(4, tempString3);
				ps.setString(5, generateRandomDate());

				//quantity generator
				tempInt = gen.nextInt((maxQuantity - minQuantity) + 1) + minQuantity;
				tempString = String.valueOf(tempInt);
				ps.setString(1, tempString);
				
				ps.executeUpdate();

				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				counter--;
			}
			
	  	}
	  	
	  	System.exit(0);
	  	
	}

	static String generateRandomDate() {
		int year = randomNumberFromRange(2000, 2022);
		int month = randomNumberFromRange(1, 12);
		int day = randomNumberFromRange(1, 28);
		return year +"-" + month + "-" + day;
	}

	static int randomNumberFromRange(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

}