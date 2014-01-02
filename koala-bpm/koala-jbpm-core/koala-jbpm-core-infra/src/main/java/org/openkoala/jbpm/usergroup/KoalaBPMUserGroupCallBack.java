package org.openkoala.jbpm.usergroup;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.task.identity.UserGroupCallback;

public class KoalaBPMUserGroupCallBack implements UserGroupCallback {
	
	private static final String NO_GROUP="no_groups";

	public boolean existsUser(String userId) {

		// accept all by default

		return true;

	}

	public boolean existsGroup(String groupId) {

		// accept all by default

		return true;

	}

	public List<String> getGroupsForUser(String userId, List<String> groupIds,

	List<String> allExistingGroupIds) {

		if (groupIds != null) {

			List<String> retList = new ArrayList<String>(groupIds);

			// merge all groups

			if (allExistingGroupIds != null) {

				for (String grp : allExistingGroupIds) {

					if (!retList.contains(grp)) {

						retList.add(grp);

					}

				}

			}

			return retList;

		} else {

			// return empty list by default
			List<String> groups = new ArrayList<String>();
			groups.add(NO_GROUP);
			return groups;

		}

	}

}
