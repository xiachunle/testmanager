package com.ks.tests.casers;

import static org.testng.Assert.assertEquals;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.ks.tests.dao.CasesDao;
import com.ks.tests.entries.TestCases;
import com.ks.tests.utils.SpringBeanFactoryUtils;

@Listeners({ com.ks.tests.manager.GenerateReporter.class })
public class SampleTest {
	

	private CasesDao caseDao=(CasesDao) SpringBeanFactoryUtils.getBean("testcase");
	@DataProvider(name = "data")
	public Iterator<Object[]> createDataResource() {
		Set<Object[]> set = new TreeSet<Object[]>(new Comparator<Object[]>() {

			@Override
			public int compare(Object[] o1, Object[] o2) {
				TestCases[] t1 = (TestCases[]) o1;
				TestCases[] t2 = (TestCases[]) o2;
				return t1[0].getCaseId() - t2[0].getCaseId();
			}
		});
		for (TestCases caser : caseDao.queryAll()) {
			System.out.println(caser.toString());
			set.add(new TestCases[] { caser });
		}
		Iterator<Object[]> iterator = set.iterator();
		return iterator;
	}

	@Test(dataProvider = "data")
	public void f(TestCases casers) {
		String actual = "2";

		assertEquals(actual, casers.getCaseExpect());
	}

}
