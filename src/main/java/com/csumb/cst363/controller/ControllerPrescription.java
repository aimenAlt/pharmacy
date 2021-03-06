package com.csumb.cst363.controller;

import com.csumb.cst363.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


@Controller    
public class ControllerPrescription {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*
	 * Doctor requests form to create new prescription.
	 */
	@GetMapping("/prescription/new")
	public String newPrescripton(Model model) {
		model.addAttribute("prescription", new Prescription());
		return "prescription_create";
	}
	
	/* 
	 * Patient requests form to search for prescription.
	 */
	@GetMapping("/prescription/fill")
	public String getfillForm(Model model) {
		model.addAttribute("prescription", new Prescription());
		return "prescription_fill";
	}
	
	/* 
	 * Process the new prescription form.
	 * 1.  Validate that Doctor SSN exists and matches Doctor Name.
	 * 2.  Validate that Patient SSN exists and matches Patient Name.
	 * 3.  Validate that Drug name exists.
	 * 4.  Insert new prescription.
	 * 5.  If error, return error message and the prescription form
	 * 6.  Otherwise, return the prescription with the rxid number that was generated by the database.
	 */
	@PostMapping("/prescription")
	public String newPrescription( Prescription p, Model model) {
		
		
		// TODO 
		
		/*
		 * replace following with code to validate the prescription 
		 * and insert a new prescription.  get the prescription number generated by database.
		 */
		// set fake data for auto-generated prescription id.
		p.setRxid("RX1980031234");
		
		model.addAttribute("message", "Prescription created.");
		model.addAttribute("prescription", p);
		return "prescription_show";
	}
	

	
	/*
	 * Process the prescription fill request from a patient.
	 * 1.  Validate that Prescription p contains rxid, pharmacy name and pharmacy address
	 *     and uniquely identify a prescription and a pharmacy.
	 * 2.  update prescription with pharmacyid, name and address.
	 * 3.  update prescription with today's date.
	 * 4.  Display updated prescription 
	 * 5.  or if there is an error show the form with an error message.
	 */
	@PostMapping("/prescription/fill")
	public String processFillForm(Prescription p, Model model) throws SQLException {


		String selectRxidQuery = 	"SELECT rxid FROM prescription WHERE rxid = ?";
		ArrayList<Object> selectRxidVars = new ArrayList<>();
		selectRxidVars.add(p.getRxid());
		ArrayList<Object> rxidResult = getFromDatabase(selectRxidQuery, selectRxidVars);
		try {
			CachedRowSet finalResult = (CachedRowSet) rxidResult.get(0);
			System.out.print("finalResult: ");
			System.out.println(finalResult.getInt(1));
		} catch(Exception e) {
			model.addAttribute("message", "Error: RXID does not exist");
			model.addAttribute("prescription", p);
			return "prescription_fill";
		}

		ArrayList<Object> validationResult = validatePrescriptionVals(p);
		boolean validPrescription = (boolean) validationResult.get(0);
		String invalidMessage = (String) validationResult.get(1);
		if(!validPrescription) {
			model.addAttribute("message", invalidMessage);
			model.addAttribute("prescription", p);
			return "prescription_fill";
		}

		String insertPharmacyQuery = "INSERT INTO pharmacy (pharmacy_name, street) VALUES (?,?)";
		ArrayList<Object> insertPharmacyVars = new ArrayList<>();
		insertPharmacyVars.add(p.getPharmacyName());
		insertPharmacyVars.add(p.getPharmacyAddress());
		Long newPharmacyId = (Long) insertIntoDatabase(insertPharmacyQuery, insertPharmacyVars).get(0);

		String updatePrescriptionQuery = "UPDATE prescription SET fill_date = CURRENT_DATE, pharmacy_id = ? WHERE rxid = ?";
		ArrayList<Object> updatePrescriptionVars = new ArrayList<>();
		updatePrescriptionVars.add(newPharmacyId.intValue());
		updatePrescriptionVars.add(p.getRxid());
		insertIntoDatabase(updatePrescriptionQuery, updatePrescriptionVars);

		String selectPrescriptionQuery = 	"SELECT prescription.rxid, doctor.ssn, doctor.name, " +
											"patient.ssn, patient.name, drug.trade_name, prescription.quantity, " +
											"prescription.fill_date, drug.price FROM prescription " +
											"INNER JOIN doctor ON prescription.doctor_ssn = doctor.ssn " +
											"INNER JOIN patient ON prescription.patient_ssn = patient.ssn " +
											"INNER JOIN drug ON prescription.drug_name = drug.trade_name " +
											"WHERE prescription.rxid = ?";
		ArrayList<Object> selectPrescriptionVars = new ArrayList<>();
		selectPrescriptionVars.add(p.getRxid());
		ArrayList<Object> queryResult = getFromDatabase(selectPrescriptionQuery, selectPrescriptionVars);
		CachedRowSet result = (CachedRowSet) queryResult.get(0);
		p.setPharmacyID(newPharmacyId.toString());
		p.setCost(String.format("%.2f", result.getDouble(9)));
		p.setDateFilled( result.getString(8));
		p.setDoctor_ssn(result.getString(2));
		p.setDoctorName(result.getString(3));
		p.setPatient_ssn(result.getString(4));
		p.setPatientName(result.getString(5));
		p.setDrugName(result.getString(6));
		System.out.println(result.getString(7));
		p.setQuantity(Integer.parseInt(result.getString(7)));
		// display the updated prescription
		model.addAttribute("message", "Prescription has been filled.");
		model.addAttribute("prescription", p);
		return "prescription_show";
	}
	
	/*
	 * return JDBC Connection using jdbcTemplate in Spring Server
	 */

	private Connection getConnection() throws SQLException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		return conn;
	}


	public ArrayList<Object> getFromDatabase(String query, ArrayList<Object> queryVars) throws SQLException {

		ArrayList<Object> queryResults = null;
		PreparedStatement stmt;
		try (Connection con = this.getConnection()){
			stmt = con.prepareStatement(query);
			insertQueryVars(queryVars, stmt);
			ResultSet results = stmt.executeQuery();
			queryResults = new ArrayList<>();
			RowSetFactory factory = RowSetProvider.newFactory();
			CachedRowSet rowset = factory.createCachedRowSet();
			rowset.populate(results);
			rowset.next();
			queryResults.add(rowset);
//			while(rowset.next()) {
//				queryResults.add(rowset);
//			}
		} catch(Exception e) {
			System.out.println(e);
		}
		return queryResults;
	}

	public ArrayList<Object> insertIntoDatabase(String query, ArrayList<Object> queryVars) throws SQLException {

		ArrayList<Object> queryResults = null;
		PreparedStatement stmt;
		try (Connection con = this.getConnection()){
			stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			insertQueryVars(queryVars, stmt);
			queryResults = new ArrayList<>();
			int rowsAffected = stmt.executeUpdate();
			ResultSet generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				System.out.println(generatedKeys.getLong(1));
				Long tempLong = generatedKeys.getLong(1);
				queryResults.add(tempLong);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
		return queryResults;
	}

	private void insertQueryVars(ArrayList<Object> queryVars, PreparedStatement stmt) {
		int i = 1;
		for (Object e : queryVars) {
			if (e instanceof String) {
				try {
					stmt.setString(i, (String) e);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			} else if (e instanceof Integer) {
				try {
					stmt.setInt(i, (Integer) e);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			i++;
		}
	}

	public ArrayList<Object> validatePrescriptionVals(Prescription prescription) {
		ArrayList<Object> returningData = new ArrayList<Object>();
		returningData.add(true);
		returningData.add("");
		if(!validateRxid(prescription.getRxid())) {
			returningData.set(0, false);
			returningData.set(1, "Error: Invalid RXid");
		} else if(!validateName(prescription.getPatientName())) {
			returningData.set(0, false);
			returningData.set(1, "Error: Invalid patient name");
		} else if(!validateName(prescription.getPharmacyName())) {
			returningData.set(0, false);
			returningData.set(1, "Error: Invalid pharmacy name");
		} else if(!validateAddress(prescription.getPharmacyAddress())) {
			returningData.set(0, false);
			returningData.set(1, "Error: Invalid pharmacy address");
		}
		return returningData;
	}

	public boolean validateRxid(String rxid) {
		try {
			Integer.parseInt(rxid);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean validateName(String name) {
		char[] chars = name.toCharArray();
		for(char c : chars){
			if(Character.isDigit(c)){
				return false;
			}
		}
		return true;
	}

	public static boolean validateAddress(String address) {
		return address.matches(
				"\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" );
	}

}
