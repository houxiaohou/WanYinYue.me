package me.wanyinyue.service;

import javax.annotation.Resource;

import me.wanyinyue.hibernate.basedao.BaseDao;
import me.wanyinyue.model.Record;

import org.springframework.stereotype.Component;

@Component(value = "recordManager")
public class RecordManager {

	private BaseDao<Record> recordDao;

	public void addRecord(Record record) {
		recordDao.save(record);
	}

	public Record getRecordByKeyword(String keyword) {
		return recordDao.findUniqueBy("keyword", keyword);
	}

	public Record getRecordById(int id) {
		return recordDao.findUniqueBy("id", id);
	}

	@Resource(name = "recordDao")
	public void setRecordDao(BaseDao<Record> recordDao) {
		this.recordDao = recordDao;
	}

}
