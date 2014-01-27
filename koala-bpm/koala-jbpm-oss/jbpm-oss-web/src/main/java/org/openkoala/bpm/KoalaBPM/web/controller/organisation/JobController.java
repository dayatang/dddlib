package org.openkoala.bpm.KoalaBPM.web.controller.organisation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.openkoala.organisation.SnIsExistException;
import org.openkoala.organisation.application.JobApplication;
import org.openkoala.organisation.domain.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dayatang.querychannel.support.Page;

/**
 * 职务管理Controller
 * @author xmfang
 *
 */
@Controller
@RequestMapping("/job")
public class JobController extends BaseController {

	@Autowired
	private JobApplication jobApplication;
	
	/**
	 * 分页查询职务
	 * @param page
	 * @param pagesize
	 * @param job
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/pagingquery")
	public Map<String, Object> pagingQuery(int page, int pagesize, Job job) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Page<Job> jobs = jobApplication.pagingQueryJobs(job, page, pagesize);
		
		dataMap.put("Rows", jobs.getResult());
		dataMap.put("start", page * pagesize - pagesize);
		dataMap.put("limit", pagesize);
		dataMap.put("Total", jobs.getTotalCount());
		return dataMap;
	}

	/**
	 * 查询所有职务
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/query-all")
	public Map<String, Object> queryAllJobs() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", getBaseApplication().findAll(Job.class));
		return dataMap;
	}

	/**
	 * 创建一个职务
	 * @param job
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/create")
	public Map<String, Object> createJob(Job job) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			getBaseApplication().saveParty(job);
			dataMap.put("result", "success");
		} catch (SnIsExistException exception) {
			dataMap.put("result", "职务编码: " + job.getSn() + " 已被使用！");
		} catch (Exception e) {
			dataMap.put("result", "保存失败！");
		}
		return dataMap;
	}

	/**
	 * 更新职务信息
	 * @param job
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/update")
	public Map<String, Object> updateJob(Job job) {
		Map<String, Object> dataMap = null;
		try {
			dataMap = new HashMap<String, Object>();
			getBaseApplication().updateParty(job);
			dataMap.put("result", "success");
		} catch (SnIsExistException exception) {
			dataMap.put("result", "职务编码: " + job.getSn() + " 已被使用！");
		} catch (Exception e) {
			if(dataMap != null){
				dataMap.put("result", "修改失败！");
			}
		}
		return dataMap;
	}
	
	/**
	 * 根据ID获得职务
	 * @param id
	 * @return
	 */
    @ResponseBody
    @RequestMapping("/get/{id}")
	public Map<String,Object> get(@PathVariable("id") Long id) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data", getBaseApplication().getEntity(Job.class, id));
		return dataMap;
	}
	
    /**
     * 撤销某个职务
     * @param job
     * @return
     */
	@ResponseBody
    @RequestMapping("/terminate")
	public Map<String, Object> terminateJob(Job job) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		getBaseApplication().terminateParty(job);
		dataMap.put("result", "success");
		return dataMap;
	}
	
	/**
	 * 同时撤销多个职务
	 * @param jobs
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/terminateJobs", method = RequestMethod.POST, consumes = "application/json")
	public Map<String, Object> terminateJobs(@RequestBody Job[] jobs) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		getBaseApplication().terminateParties(new HashSet<Job>(Arrays.asList(jobs)));
		dataMap.put("result", "success");
		return dataMap;
	}
	
}
