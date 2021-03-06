package babroval.storage.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import babroval.storage.dao.ElectricDaoImpl;
import babroval.storage.dao.RentDaoImpl;
import babroval.storage.dao.StorageDaoImpl;
import babroval.storage.dao.UserDaoImpl;
import babroval.storage.model.Electric;
import babroval.storage.model.Rent;
import babroval.storage.model.Storage;
import babroval.storage.model.User;

public final class InitDB {

	public static void createDB(String url, String user, String password) {

		try (Connection cn = ConnectionPool.getPool().getConnection(url, user, password);
				Statement st = cn.createStatement()) {

			st.executeUpdate("CREATE DATABASE " + ConnectionPool.NAME_DB);

			st.executeUpdate("USE " + ConnectionPool.NAME_DB);

			st.executeUpdate("CREATE TABLE user (" 
							+ "user_id INT PRIMARY KEY AUTO_INCREMENT,"
							+ " name VARCHAR(50)," 
							+ " info VARCHAR(100))");
			
			st.executeUpdate("CREATE TABLE storage (" 
							+ "storage_id INT PRIMARY KEY AUTO_INCREMENT,"
							+ " user_id INT," 
							+ " storage_number VARCHAR(50),"
							+ " info VARCHAR(100),"
							+ " FOREIGN KEY(user_id) REFERENCES user(user_id))");

			st.executeUpdate("CREATE TABLE rent (" 
							+ "rent_id INT PRIMARY KEY AUTO_INCREMENT," 
							+ " storage_id INT,"
							+ " date DATE," 
							+ " quarter_paid DATE," 
							+ " sum DECIMAL(10,2)," 
							+ " info VARCHAR(100),"
							+ " FOREIGN KEY(storage_id) REFERENCES storage(storage_id))");

			st.executeUpdate("CREATE TABLE electric (" 
							+ "electric_id INT PRIMARY KEY AUTO_INCREMENT," 
							+ " storage_id INT,"
							+ " date DATE," 
							+ " tariff DECIMAL(10,3),"
							+ " meter_paid INT,"
							+ " sum DECIMAL(10,2),"
							+ " info VARCHAR(100)," 
							+ " FOREIGN KEY(storage_id) REFERENCES storage(storage_id))");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		UserDaoImpl daoUser = new UserDaoImpl();
		daoUser.insert(new User("init", "init"));
		daoUser.insert(new User("JANE ROE", "tel. 29-78-56-546, 200 E MAIN ST PHOENIX AZ 85123"));
		daoUser.insert(new User("JOHN SMITH", "tel. 44-164-76-389, 795 E DRAGRAM TUCSON AZ 85705"));
		daoUser.insert(new User("CHRIS NISWANDEE", "tel. 25-797-35-91, 300 BOYLSTON AVE E SEATTLE WA 98102"));

		StorageDaoImpl daoStorage = new StorageDaoImpl();
		daoStorage.insert(new Storage(1, "0", "init"));
		daoStorage.insert(new Storage(4, "2", "double"));
		daoStorage.insert(new Storage(3, "1a", "standard"));
		daoStorage.insert(new Storage(2, "1b", "small"));
		
		RentDaoImpl daoRent = new RentDaoImpl();
		daoRent.insert(new Rent(2, DateUtil.stringToDate("18-11-2017", "dd-MM-yyyy"), DateUtil.stringToDate("01-01-2018", "dd-MM-yyyy"), BigDecimal.valueOf(20.00), "45325"));
		daoRent.insert(new Rent(4, DateUtil.stringToDate("19-11-2017", "dd-MM-yyyy"), DateUtil.stringToDate("01-04-2018", "dd-MM-yyyy"), BigDecimal.valueOf(40.00), "67567"));
		daoRent.insert(new Rent(2, DateUtil.stringToDate("20-11-2017", "dd-MM-yyyy"), DateUtil.stringToDate("01-04-2018", "dd-MM-yyyy"), BigDecimal.valueOf(20.00), "34556"));
		daoRent.insert(new Rent(4, DateUtil.stringToDate("29-11-2017", "dd-MM-yyyy"), DateUtil.stringToDate("01-07-2018", "dd-MM-yyyy"), BigDecimal.valueOf(20.00), "78574"));
		daoRent.insert(new Rent(3, DateUtil.stringToDate("30-11-2017", "dd-MM-yyyy"), DateUtil.stringToDate("01-10-2018", "dd-MM-yyyy"), BigDecimal.valueOf(60.00), "353465"));

		ElectricDaoImpl daoElectric = new ElectricDaoImpl();
		daoElectric.insert(new Electric(2, DateUtil.stringToDate("18-11-2017", "dd-MM-yyyy"), BigDecimal.valueOf(0.178), 45700, BigDecimal.valueOf(17.80), "56456"));
		daoElectric.insert(new Electric(4, DateUtil.stringToDate("19-11-2017", "dd-MM-yyyy"), BigDecimal.valueOf(0.178), 34500, BigDecimal.valueOf(26.70), "45763"));
		daoElectric.insert(new Electric(2, DateUtil.stringToDate("20-11-2017", "dd-MM-yyyy"), BigDecimal.valueOf(0.178), 45800, BigDecimal.valueOf(17.80), "74535"));
		daoElectric.insert(new Electric(4, DateUtil.stringToDate("01-12-2017", "dd-MM-yyyy"), BigDecimal.valueOf(0.170), 34650, BigDecimal.valueOf(25.50), "34567"));
		daoElectric.insert(new Electric(3, DateUtil.stringToDate("03-12-2017", "dd-MM-yyyy"), BigDecimal.valueOf(0.170), 44600, BigDecimal.valueOf(8.50), "73457"));
	}

	public static void deleteDB(String url, String user, String password) {

		try (Connection con = ConnectionPool.getPool().getConnection(url, user, password);
				Statement st = con.createStatement()) {

			st.executeUpdate("DROP DATABASE " + ConnectionPool.NAME_DB);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void showResultSet(ResultSet rs) {
		ResultSetMetaData rsmd;
		try {
			rsmd = (ResultSetMetaData) rs.getMetaData();

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				System.out.print(rsmd.getColumnName(i) + "\t");
			}
			while (rs.next()) {
				System.out.println("\t");
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					System.out.print(rs.getString(i) + "\t");
				}
			}
			System.out.println();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}