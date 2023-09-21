package com.rgt.service;

import java.util.Date;

public interface ActivityLogsService {

	void markUserLogs(Long id, String domain, String string, Date currentTimeStamp, String emailId,
			String emailId2, Date lastLogin);

}
