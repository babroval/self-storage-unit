package babroval.storage.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import babroval.storage.entity.Rent;
import babroval.storage.frames.TableStorage;
import babroval.storage.mysql.ConnectionPool;

public class RentDao implements Dao<Rent> {

	public RentDao() {
	}

	@Override
	public void insert(Rent ob) {
		try (Connection cn = ConnectionPool.getPool().getConnection();
				PreparedStatement ps = (PreparedStatement) cn
						.prepareStatement("insert into " + ob.getClass().getSimpleName()
								+ " (storage_id, date, quarter_paid, sum, info) values (?,?,?,?,?)")) {

			ps.setInt(1, ob.getStorage_id());
			ps.setDate(2, ob.getDate());
			ps.setDate(3, ob.getQuarter_paid());
			ps.setBigDecimal(4, ob.getSum());
			ps.setString(5, ob.getInfo());

			ps.execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Rent ob) {
		try (Connection cn = ConnectionPool.getPool().getConnection();
				PreparedStatement ps = (PreparedStatement) cn.prepareStatement("update " + ob.getClass().getSimpleName()
						+ " set storage_id=?, date=?, quarter_paid=?, sum=?, info=? " + " where rent_id = "
						+ ob.getRent_id())) {

			ps.setInt(1, ob.getStorage_id());
			ps.setDate(2, ob.getDate());
			ps.setDate(3, ob.getQuarter_paid());
			ps.setBigDecimal(4, ob.getSum());
			ps.setString(5, ob.getInfo());

			ps.execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Date loadLastQuarterPaidByStorageNumber(String number) {

		Date date = new Date(0);

		try (Connection cn = ConnectionPool.getPool().getConnection();
				Statement st = cn.createStatement();
				ResultSet rs = st.executeQuery(
						"SELECT MAX(rent.quarter_paid)" + " FROM rent, storage" + " WHERE storage.storage_number='"
								+ number + "'" + " AND rent.storage_id=storage.storage_id")) {

			while (rs.next()) {
				date = Date.valueOf(rs.getString(1));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return date;
	}

	public TableStorage loadReadOnlyTable() {

		TableStorage table = new TableStorage();

		try (Connection cn = ConnectionPool.getPool().getConnection();
				Statement st = cn.createStatement();
				ResultSet rs = st
						.executeQuery("SELECT rent.date, storage.storage_number, rent.quarter_paid, rent.sum, rent.info"
								+ " FROM storage, rent" + " WHERE storage.storage_id=rent.storage_id "
								+ " AND rent.date!=0 ORDER BY rent.rent_id DESC")) {

			table = new TableStorage(rs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return table;
	}

	public TableStorage loadTableByStorageNumber(String number) {

		TableStorage table = new TableStorage();

		try (Connection cn = ConnectionPool.getPool().getConnection();
				Statement st = cn.createStatement();
				ResultSet rs = st
						.executeQuery("SELECT rent.date, rent.quarter_paid, rent.sum, rent.info" + " FROM storage, rent"
								+ " WHERE storage.storage_id=rent.storage_id" + " AND storage.storage_number='" + number
								+ "'" + " AND rent.date!=0" + " ORDER BY rent.quarter_paid DESC")) {

			table = new TableStorage(rs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return table;
	}

	public TableStorage loadDebtorsByYearQuarter(String year, String quarter) {

		TableStorage table = new TableStorage();

		try (Connection cn = ConnectionPool.getPool().getConnection();
				Statement st = cn.createStatement();
				ResultSet rs = st
						.executeQuery("SELECT MAX(rent.quarter_paid), storage.storage_number, user.name, user.info"
								+ " FROM storage, user, rent" + " WHERE rent.storage_id=storage.storage_id"
								+ " AND storage.user_id=user.user_id" + " GROUP BY storage.storage_id"
								+ " HAVING MAX(rent.quarter_paid)<'" + year + "-" + quarter + "-01" + "'"
								+ " ORDER BY rent.quarter_paid ASC")) {

			table = new TableStorage(rs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return table;
	}

	public TableStorage loadEditTable() {

		TableStorage table = new TableStorage();

		try (Connection cn = ConnectionPool.getPool().getConnection();
				Statement st = cn.createStatement();
				ResultSet rs = st.executeQuery("SELECT rent.rent_id, storage.storage_number, rent.date,"
						+ " rent.quarter_paid, rent.sum, rent.info" + " FROM storage, rent"
						+ " WHERE rent.storage_id=storage.storage_id" + " ORDER BY rent.rent_id DESC")) {

			table = new TableStorage(rs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return table;
	}

	@Override
	public void assignTo(Rent ob) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> loadAllNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> loadAllNumbers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rent loadByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rent loadByStorageNumber(String number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rent loadLastPaidByStorageNumber(String number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer loadIdByStorageNumber(String number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String loadNameByStorageNumber(String number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableStorage loadSortTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer loadIdByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
