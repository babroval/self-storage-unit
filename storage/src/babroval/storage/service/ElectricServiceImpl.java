package babroval.storage.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;

import babroval.storage.dao.Dao;
import babroval.storage.dao.ElectricDaoImpl;
import babroval.storage.util.TableStorage;

public class ElectricServiceImpl<T> implements Service<T> {

	@SuppressWarnings("unchecked")
	private Dao<T> dao = (Dao<T>) new ElectricDaoImpl();

	@Override
	public void insert(T ob) {
		dao.insert(ob);
	}

	@Override
	public void update(T ob) {
		dao.update(ob);
	}

	@Override
	public void assignTo(T ob) {
		dao.assignTo(ob);
	}

	@Override
	public List<String> getAllNames() {
		return dao.loadAllNames();
	}

	@Override
	public List<String> getAllNumbers() {
		return dao.loadAllNumbers();
	}

	@Override
	public T getByName(String name) {
		return dao.loadByName(name);
	}

	@Override
	public T getByStorageNumber(String number) {
		return dao.loadByStorageNumber(number);
	}

	@Override
	public T getLastPaidByStorageNumber(String number) {
		return dao.loadLastPaidByStorageNumber(number);
	}

	@Override
	public TableStorage getReadOnlyTable() {
		return dao.loadReadOnlyTable();
	}

	@Override
	public TableStorage getEditTable() {
		return dao.loadEditTable();
	}

	@Override
	public TableStorage getSortTable() {
		return dao.loadSortTable();
	}

	@Override
	public TableStorage getTableByStorageNumber(String number) {
		return dao.loadTableByStorageNumber(number);
	}

	@Override
	public TableStorage getDebtorsByYearQuarter(String year, String quarter) {
		return dao.loadDebtorsByYearQuarter(year, quarter);
	}

	@Override
	public Integer getIdByName(String name) {
		return dao.loadIdByName(name);
	}

	@Override
	public Integer getIdByStorageNumber(String number) {
		return dao.loadIdByStorageNumber(number);
	}

	@Override
	public String getNameByStorageNumber(String number) {
		return dao.loadNameByStorageNumber(number);
	}

	@Override
	public Date getLastQuarterPaidByStorageNumber(String number) {
		return dao.loadLastQuarterPaidByStorageNumber(number);
	}

	@Override
	public BigDecimal getSum(Integer indicationLastPaid, Integer indication, BigDecimal tariff) {

		BigDecimal kWh = new BigDecimal(String.valueOf(indication - indicationLastPaid));

		if (tariff.compareTo(new BigDecimal("0")) <= 0 || kWh.compareTo(new BigDecimal("0")) <= 0) {
			throw new NumberFormatException("e");
		}

		BigDecimal sum = kWh.multiply(tariff);
		sum = sum.setScale(2, RoundingMode.HALF_UP);

		return sum;
	}

}
