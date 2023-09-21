package com.rgt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rgt.entity.UserActivityLogsEntity;
import com.rgt.repository.UserActivityLogsRepository;

@Service
public class ActivityLogsServiceImpl implements ActivityLogsService{
	
	@Autowired
	private UserActivityLogsRepository userActivityLogsRepository;
	

	@Override
	public void markUserLogs(Long id, String domain, String message, Date date, String createdBy,
			String name, Date lastLogin) {
		UserActivityLogsEntity activityModel = new UserActivityLogsEntity();
		activityModel.setDomainName(domain);
		activityModel.setTagId(id);
		activityModel.setCreatedBy(createdBy);
		activityModel.setCreatedOn(date);
		activityModel.setName(name);
		activityModel.setMessage(message);
		activityModel.setLastLogin(lastLogin);
		
		userActivityLogsRepository.save(activityModel);
		
	}

}
