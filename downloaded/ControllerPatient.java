package com.csumb.cst363;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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
		
		System.out.println(p.toString());
		
		try (Connection con = getConnection();) {
			
			PreparedStatement ps;
			String doctorID="";
			String doctorSpecialty="";
			int birthYear = Integer.parseInt(p.getBirthdate().substring(0, 4));
			int ssnLength = (int) (Math.log10(Integer.parseInt(p.getSsn())) + 1);
			int zipLength = (int) (Math.log10(Integer.parseInt(p.getZipcode())) + 1);
			ResultSet rs;
			
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
			
			System.out.println("doctorID: " + doctorID);
			System.out.println("doctorSpecialty: " + doctorSpecialty);
			System.out.println("birthYear: " + birthYear);
			System.out.println("ssnLength: " + ssnLength);
			
			
			// validation that primary doctor must be internal medicine, family medicine, or pediatrics, and an adult cannot have a pediatrics doctor as a PCP
			if (((birthYear < 2010) && (doctorSpecialty.matches("Pediatrics"))) ||
					((!doctorSpecialty.matches("Family Medicine"))
					&& (!doctorSpecialty.matches("Internal Medicine"))
					&& (!doctorSpecialty.matches("Pediatrics")))) {
				throw new Exception("Invalid doctor selection.");
			}
			
			// ssn length validation
			if ((ssnLength < 9) || (ssnLength > 9)) {
				throw new Exception("Invalid social security number.");
			}
			
			// zip length validation
			if ((zipLength < 5) || (zipLength > 5)) {
				throw new Exception("Invalid zip code.");
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

			//set the returned patient id
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
