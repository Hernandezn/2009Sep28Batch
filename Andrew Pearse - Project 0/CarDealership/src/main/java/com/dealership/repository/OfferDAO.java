package com.dealership.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dealership.config.EnvironmentConnectionUtil;
import com.dealership.models.Car;
import com.dealership.models.Offer;

public class OfferDAO implements DAOBase<Offer, Integer> {
	
	final static Logger logger = Logger.getLogger(CarDAO.class);

	
	public int getNextOfferID() {
		int nextOfferID = 0;
		
		try(Connection conn = EnvironmentConnectionUtil.getInstance().getConnection()){
			String sql = "select * from dealership.next_offer()";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				nextOfferID = rs.getInt(1);
			}
			
			logger.info("getNextOfferID() successfully called");
			ps.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("Database access denied.", e);
			e.printStackTrace();
			return nextOfferID;
		}
		
		return nextOfferID;
	}
	
	public List<Offer> viewAllPaymentsOnCars(String username){
		List<Offer> offers = new LinkedList<>();
		
		try(Connection conn = EnvironmentConnectionUtil.getInstance().getConnection()){
			String sql = "select off_id, down_payment, months_left, accepted, rejected, user_name, cid, make, model, price, on_lot "
					   + "from \"Offer\" o inner join offers_made om using (off_id) "
					   					+ "inner join \"Cars\" c using (cid) "
					   + "where car_owner = ? and accepted = true";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				offers.add(new Offer(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBoolean(4), rs.getBoolean(5), rs.getString(6), 
						new Car(rs.getInt(7), rs.getString(8), rs.getString(9), rs.getInt(10), null, rs.getBoolean(11))));
			}
			
			logger.info("viewAllPaymentsOnCars() successfully called");
			ps.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("Database access denied.", e);
			e.printStackTrace();
			return null;
		}
		
		return offers;
	}
	
	public List<Offer> viewAllOffersOnACar(int cid){
		List<Offer> offers = new LinkedList<>();
		
		try(Connection conn = EnvironmentConnectionUtil.getInstance().getConnection()){
			String sql = "select off_id, down_payment, months_left, user_name, cid, make, model, price "
					   + "from \"Offer\" inner join offers_made using (off_id) "
					   				 + "inner join \"Cars\" using (cid) "
					   + "where accepted = false and rejected = false and cid = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, cid);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				offers.add(new Offer(rs.getInt(1), rs.getInt(2), rs.getInt(3), false, false, rs.getString(4), 
						             new Car(rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8), null, true)));
			}
			
			logger.info("viewAllOffersOnACar() successfully called");
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			logger.error("Database access denied.", e);
			e.printStackTrace();
			return null;
		}
		
		return offers;
	}
	
	public List<Offer> getAllPayments(){
		List<Offer> offers = new LinkedList<>();
		
		try (Connection conn = EnvironmentConnectionUtil.getInstance().getConnection()){
			String sql = "select down_payment, months_left, user_name, make, model, price "
					   + "from \"Offer\" inner join offers_made using (off_id) "
					   				  + "inner join \"Cars\" using (cid) "
					   + "where accepted = true;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				offers.add(new Offer(rs.getInt(1), rs.getInt(2), rs.getString(3), new Car(0, rs.getString(4), rs.getString(5), rs.getInt(6), null, false)));
			}
			
			logger.info("getAllPayments() successfully called");
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			logger.error("Database access denied.", e);
			e.printStackTrace();
			return null;
		}
		
		
		
		return offers;
	}
	
	@Override
	public List<Offer> findAll() {
		List<Offer> offers = new LinkedList<>();
		
		try(Connection conn = EnvironmentConnectionUtil.getInstance().getConnection()){
			String sql = "select * from \"Offer\"";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				offers.add(new Offer(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBoolean(4), rs.getBoolean(5), rs.getString(6)));
			}
			
			logger.info("findAll() succesfully called");
			rs.close();
			ps.close();
			
		} catch (SQLException e) {
			logger.error("Database access denied.", e);
			e.printStackTrace();
			return null;
		}
		
		return offers;
	}

	@Override
	public Offer findById(Integer i) {
		Offer offer = null;
		
		try (Connection conn = EnvironmentConnectionUtil.getInstance().getConnection()){
			String sql = "select * from \"Offer\" where off_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				offer = new Offer(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBoolean(4), rs.getBoolean(5), rs.getString(6));
			}
			
			logger.info("findById() successfully called");
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			logger.error("Database access denied.", e);
			e.printStackTrace();
			return null;
		}

		return offer;
	}

	@Override
	/**
	 * if an offer is accepted, take the car off the lot and reject all other offers
	 * else if an offer is rejected, set rejected to true
	 */
	public Offer update(Offer t) {
		
		if (t.isAccepted()) {
			try (Connection conn = EnvironmentConnectionUtil.getInstance().getConnection()){
				
				String sql = "update \"Offer\" set accepted = true "
										  	    + "where off_id = ?";
				
				PreparedStatement ps = conn.prepareStatement(sql);
				
				ps.setInt(1, t.getOfferID());
				ps.executeUpdate();
				
				sql = "select cid "
				    + "from \"Offer\" inner join offers_made using(off_id) "
				    + "where off_id = ?";
				
				ps = conn.prepareStatement(sql);
				ps.setInt(1, t.getOfferID());
				ResultSet rs = ps.executeQuery();
				int cid = 0;
				
				while (rs.next()) {
					cid = rs.getInt(1);
				}
				
				sql = "select off_id "
					+ "from \"Offer\" inner join offers_made using(off_id) "
					+ "where accepted != true and cid = ?";

				ps = conn.prepareStatement(sql);
				ps.setInt(1, cid);
				rs = ps.executeQuery();
				
				List<Integer> offersToReject = new LinkedList<>();
				
				while(rs.next()) {
					offersToReject.add(rs.getInt(1));
				}
				
				sql = "update \"Offer\" set rejected = true where off_id = ?";
				ps = conn.prepareStatement(sql);
				
				while(!offersToReject.isEmpty()){
					int nextOffer = offersToReject.remove(0);
					ps.setInt(1, nextOffer);
					ps.executeUpdate();
				}
				
				sql = "update \"Cars\" set on_lot = false where cid = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, cid);
				ps.executeUpdate();
				
				sql = "update \"Cars\" set car_owner = ? where cid = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, t.getOfferer());
				ps.setInt(2, cid);
				ps.executeUpdate();
				
				logger.info("update(accepted) successfully called");
				rs.close();
				ps.close();
				
			} catch (SQLException e) {
				logger.error("Database access denied.", e);
				e.printStackTrace();
				return null;
			}
			
			return t;
		}
		else if (t.isRejected()) {
			try (Connection conn = EnvironmentConnectionUtil.getInstance().getConnection()) {
				String sql = "update \"Offer\" set rejected = true where off_id = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				
				ps.setInt(1, t.getOfferID());
				ps.executeUpdate();
				
				logger.info("update(rejected) successfully called");
				ps.close();
			} catch (SQLException e) {
				logger.error("Database access denied.", e);
				e.printStackTrace();
				return null;
			}
			
			return t;
		}
		
		logger.warn("Invalid Offer format used");
		return null;
	}

	@Override
	public Offer create(Offer t) {
		try (Connection conn = EnvironmentConnectionUtil.getInstance().getConnection()){	
			String sql = "call dealership.make_offer(?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getOfferedFor().getCarID());
			ps.setInt(2, t.getDownPayment());
			ps.setInt(3, t.getMonthsLeft());
			ps.setString(4, t.getOfferer());
			
			ps.execute();
			
			logger.info("create() successfully called");
			ps.close();
		} catch (SQLException e) {
			logger.error("Database access denied.", e);
			e.printStackTrace();
			return null;
		}
		
		return t;
	}

	@Override
	public int delete(Integer i) {
		int updated = 0;
		try (Connection conn = EnvironmentConnectionUtil.getInstance().getConnection()){
			String sql = "delete from offers_made where off_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, i);
			
			updated = ps.executeUpdate();
			
			sql = "delete from \"Offer\" where off_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, i);
			updated = ps.executeUpdate();
			
			logger.info("delete() successfully called");
			ps.close();
		} catch (SQLException e) {
			logger.error("Database access denied.", e);
			e.printStackTrace();
			updated = 0;
		}
		
		return updated;
	}

	
}