package com.csumb.cst363;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.EmptyStackException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * Controller class for patient interactions.
 *   register as a new patient.
 *   update patient profile.
 */
@Controller
public class ControllerPatient {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*
	 * Request patient_register form.
	 */
	@GetMapping("/patient/new")
	public String newPatient(Model model) {
		// return blank form for new patient registration
		model.addAttribute("patient", new Patient());
		return "patient_register";
	}
	
	/*
	 * Request form to search for patient.
	 */
	@GetMapping("/patient/edit")
	public String getPatientForm(Model model) {
		// prompt for patient id and name
		return "patient_get";
	}
	
	/*
	 * Process a form to create new patient.
	 */
	@PostMapping("/patient/new")
	public String newPatient(Patient p, Model model) {
		
		try (Connection con = getConnection();) {
			
			PreparedStatement ps;
			String doctorID="";
			String doctorSpecialty="";
	        int ssnLength;
	        int[] ssnArray;
	        int birthYear;
			Character[] birthDate;
			int zipLength;
			ResultSet rs;
			
			//XSS PREVENTION SANITATION --------
			// minus "-" for birthdate
			String [] sanitizeArray;
			sanitizeArray = new String[] {"<",">","&","”","’","(",")","#","&",";","+"}; //minus "-" because of birthdate
			
			for (int i=0; i < sanitizeArray.length; i++) {
				if (p.getName().contains(sanitizeArray[i])
					|| p.getBirthdate().contains(sanitizeArray[i])
					|| p.getStreet().contains(sanitizeArray[i])
					|| p.getCity().contains(sanitizeArray[i])
					|| p.getState().contains(sanitizeArray[i])
					|| p.getPrimaryName().contains(sanitizeArray[i])) {
					throw new Exception("Invalid characters included.");
				}
			}
			
			// and sanitizing for "-" except for birthdate
			if (p.getName().contains("-")
					|| p.getStreet().contains("-")
					|| p.getCity().contains("-")
					|| p.getState().contains("-")
					|| p.getPrimaryName().contains("-")) {
					throw new Exception("Invalid characters included.");
			}
			
			ps = con.prepareStatement("select doctor_id, specialty from doctor where (name = ?)");
			ps.setString(1, p.getPrimaryName());
			rs = ps.executeQuery();
			try {
				rs.next();
				doctorID = rs.getString(1);
				doctorSpecialty = rs.getString(2);
			} catch (Exception e) {
				model.addAttribute("message", "Error: Doctor not found.");
				model.addAttribute("patient", p);
				return "patient_register";				
			}
			
	        // SSN VALIDATION --------
	        // character by character into int array, checking for non-integers
	        ssnLength = p.getSsn().length();
	        ssnArray = new int[ssnLength];
	  
	        for (int i = 0; i < ssnLength; i++) {
	        	
	        	if (Character.isDigit(p.getSsn().charAt(i))) {
	        		ssnArray[i] = Character.getNumericValue(p.getSsn().charAt(i));
	        	} else {
	        		throw new Exception("Invalid social security number.");
	        	}
	        }
	        
	        // if ssn doesn't match requirements, throw error
			if ((ssnLength < 9)
				|| (ssnLength > 9)
				|| (ssnArray[0] == 0)
				|| (ssnArray[0] == 9)
				|| (ssnArray[4] == 0)
				|| (ssnArray[8] == 0)) {
				throw new Exception("Invalid social security number.");
			}
			
			// NAME VALIDATION  --------
			Boolean containsLetter = false;
			
			// throw error if any character is not a letter, period, or space 
	        for (int i = 0; i < p.getName().length(); i++) {
	        	if (!(Character.isLetter(p.getName().charAt(i)))
	        		&& !(p.getName().charAt(i) == '.')
	        		&& !(p.getName().charAt(i) == ' ')) {
	        		throw new Exception("Invalid name.");
	        	} else if (Character.isLetter(p.getName().charAt(i))) {
	        		containsLetter = true; //verifying if there are any letters at all
	        	}
	        }
			
	        // if no letters present, throw error
			if (!(containsLetter)) {
				throw new Exception("Invalid name.");
			}
			
			// BIRTHDATE VALIDATION --------
			
			System.out.println("p.getBirthdate(): " + p.getBirthdate());
			
	        // if birthdate doesn't match exact 0000-00-00 format, throw error
			if ((p.getBirthdate().length() != 10)
				|| (!Character.isDigit(p.getBirthdate().charAt(0)))
				|| (!Character.isDigit(p.getBirthdate().charAt(1)))
				|| (!Character.isDigit(p.getBirthdate().charAt(2)))
				|| (!Character.isDigit(p.getBirthdate().charAt(3)))
				|| (!(p.getBirthdate().charAt(4) == '-'))
				|| (!Character.isDigit(p.getBirthdate().charAt(5)))
				|| (!Character.isDigit(p.getBirthdate().charAt(6)))
				|| (!(p.getBirthdate().charAt(7) == '-'))
				|| (!Character.isDigit(p.getBirthdate().charAt(8)))
				|| (!Character.isDigit(p.getBirthdate().charAt(9)))) {
				throw new Exception("Invalid birth date.");
			}
			
			birthYear = Integer.parseInt(p.getBirthdate().substring(0, 4));
			
			if ((birthYear < 1900) || (birthYear > 2022)) {
				throw new Exception("Invalid birth year.");
			}
			
			// STREET VALIDATION --------
			containsLetter = false;
			
			// throw error if any character is not a letter, period, or space 
	        for (int i = 0; i < p.getStreet().length(); i++) {
	        	if (!(Character.isLetter(p.getStreet().charAt(i)))
	        		&& !(Character.isDigit(p.getStreet().charAt(i)))
	        		&& !(p.getStreet().charAt(i) == '.')
	        		&& !(p.getStreet().charAt(i) == ' ')) {
	        		throw new Exception("Invalid street address.");
	        	} else if (Character.isLetter(p.getStreet().charAt(i))) {
	        		containsLetter = true; //verifying if there are any letters at all
	        	}
	        }
			
	        // if no letters present, throw error
			if (!(containsLetter)) {
				throw new Exception("Invalid street address.");
			}
			
			// CITY VALIDATION --------
			containsLetter = false;
			
			// throw error if any character is not a letter, period, or space 
	        for (int i = 0; i < p.getCity().length(); i++) {
	        	if (!(Character.isLetter(p.getCity().charAt(i)))
	        		&& !(p.getCity().charAt(i) == '.')
	        		&& !(p.getCity().charAt(i) == ' ')) {
	        		throw new Exception("Invalid city.");
	        	} else if (Character.isLetter(p.getCity().charAt(i))) {
	        		containsLetter = true; //verifying if there are any letters at all
	        	}
	        }
			
	        // if no letters present, throw error
			if (!(containsLetter)) {
				throw new Exception("Invalid city.");
			}
			
			// STATE VALIDATION --------
			containsLetter = false;
			
			// throw error if input is not 2 letters
			if ((p.getState().length() != 2)
				|| !(Character.isLetter(p.getState().charAt(0)))
				|| !(Character.isLetter(p.getState().charAt(1)))) {
				throw new Exception("Invalid state.");
			}
			
			// ZIPCODE VALIDATION --------
			zipLength = p.getZipcode().length();
			
	        for (int i = 0; i < zipLength; i++) {
	        	// throw error if any non-digits
	        	if (!(Character.isDigit(p.getZipcode().charAt(i)))) {
	        		throw new Exception("Invalid zip code.");
	        	}
	        }
			
	        // throw error if not exactly 5 or 9 digits
			if ((zipLength != 5) && (zipLength != 9)) {
				throw new Exception("Invalid zip code.");
			}
			
			// PRIMARY NAME VALIDATION --------
			containsLetter = false;
			
			// throw error if any character is not a letter, period, or space 
	        for (int i = 0; i < p.getPrimaryName().length(); i++) {
	        	if (!(Character.isLetter(p.getPrimaryName().charAt(i)))
	        		&& !(p.getPrimaryName().charAt(i) == '.')
	        		&& !(p.getPrimaryName().charAt(i) == ' ')) {
	        		throw new Exception("Invalid primary doctor name.");
	        	} else if (Character.isLetter(p.getPrimaryName().charAt(i))) {
	        		containsLetter = true; //verifying if there are any letters at all
	        	}
	        }
			
	        // if no letters present, throw error
			if (!(containsLetter)) {
				throw new Exception("Invalid primary doctor name.");
			}
			
			// Validate that primary doctor must be internal medicine, family medicine,
				// or pediatrics, and an adult cannot have a pediatrics doctor as a PCP
			if (((birthYear < 2010) && (doctorSpecialty.matches("Pediatrics"))) ||
					((!doctorSpecialty.matches("Family Medicine"))
					&& (!doctorSpecialty.matches("Internal Medicine"))
					&& (!doctorSpecialty.matches("Pediatrics")))) {
				throw new Exception("Invalid doctor selection.");
			}
			
			ps = con.prepareStatement("insert into patient(ssn, name, birthdate, street, city, state, zipcode, primary_id) values(?, ?, ?, ?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, p.getSsn());
			ps.setString(2, p.getName());
			ps.setString(3, p.getBirthdate());
			ps.setString(4, p.getStreet());
			ps.setString(5, p.getCity());
			ps.setString(6, p.getState());
			ps.setString(7, p.getZipcode());
			ps.setString(8, doctorID);
			
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();

			// set the returned patient id
			if (rs.next()) {
				p.setPatientId(String.valueOf((int)rs.getLong(1)));
			}
		
			// display message and patient information
			model.addAttribute("message", "Registration successful.");
			model.addAttribute("patient", p);
			return "patient_show";
			
		} catch (Exception e) {
			
			model.addAttribute("message", "Error: "+e.getMessage());
			model.addAttribute("patient", p);
			return "patient_register";	
		}

	}
	
	/*
	 * Search for patient by patient id and name.
	 */
	@PostMapping("/patient/show")
	public String getPatientForm(@RequestParam("patientId") String patientId, @RequestParam("name") String name,
			Model model) {

		// TODO

		/*
		 * code to search for patient by id and name retrieve patient data and primary
		 * doctor data create Patient object
		 */
		
		// return fake data for now.
		Patient p = new Patient();
		p.setPatientId(patientId);
		p.setName(name);
		p.setBirthdate("2001-01-01");
		p.setStreet("123 Main");
		p.setCity("SunCity");
		p.setState("CA");
		p.setZipcode("99999");
		p.setPrimaryID(11111);
		p.setPrimaryName("Dr. Watson");
		p.setSpecialty("Family Medicine");
		p.setYears("1992");

		model.addAttribute("patient", p);
		return "patient_show";
	}

	/*
	 * Search for patient by patient id.
	 */
	@GetMapping("/patient/edit/{patientId}")
	public String updatePatient(@PathVariable String patientId, Model model) {

		// TODO Complete database logic search for patient by id.

		// return fake data for now.
		Patient p = new Patient();
		p.setPatientId(patientId);
		p.setName("Alex Patient");
		p.setBirthdate("2001-01-01");
		p.setStreet("123 Main");
		p.setCity("SunCity");
		p.setState("CA");
		p.setZipcode("99999");
		p.setPrimaryID(11111);
		p.setPrimaryName("Dr. Watson");
		p.setSpecialty("Family Medicine");
		p.setYears("1992");

		model.addAttribute("patient", p);
		return "patient_edit";
	}
	
	
	/*
	 * Process changes to patient address and primary doctor
	 */
	@PostMapping("/patient/edit")
	public String updatePatient(Patient p, Model model) {

		// TODO

		/*
		 * validate primary doctor name and other data update databaser
		 */

		model.addAttribute("patient", p);
		return "patient_show";
	}

	/*
	 * return JDBC Connection using jdbcTemplate in Spring Server
	 */

	private Connection getConnection() throws SQLException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		return conn;
	}

}
