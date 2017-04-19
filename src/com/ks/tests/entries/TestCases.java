package com.ks.tests.entries;

import java.util.Comparator;

public class TestCases  {
       private   int    caseId;
       private  String caseName;
       private  String caseAction;
       private String caseExpect;
       private String caseTester;
       private String caseActual;
       private String testResult;
       
       
    public TestCases() {

	}
       
	public TestCases(int caseId, String caseName, String caseAction, String caseExpect, String caseTester,
			String caseActual, String testResult) {
		super();
		this.caseId = caseId;
		this.caseName = caseName;
		this.caseAction = caseAction;
		this.caseExpect = caseExpect;
		this.caseTester = caseTester;
		this.caseActual = caseActual;
		this.testResult = testResult;
	}
	public int getCaseId() {
		return caseId;
	}
	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	public String getCaseAction() {
		return caseAction;
	}
	public void setCaseAction(String caseAction) {
		this.caseAction = caseAction;
	}
	public String getCaseExpect() {
		return caseExpect;
	}
	public void setCaseExpect(String caseExpect) {
		this.caseExpect = caseExpect;
	}
	public String getCaseTester() {
		return caseTester;
	}
	public void setCaseTester(String caseTester) {
		this.caseTester = caseTester;
	}
	public String getCaseActual() {
		return caseActual;
	}
	public void setCaseActual(String caseActual) {
		this.caseActual = caseActual;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
       
	
	public String toString(){
	
		return "caseId="+caseId+";caseName="+caseName+
				";caseAction="+caseAction+";caseExpect="+caseExpect+
				";caseActual="+caseActual+
				";caseTester="+caseTester;
	}





	
       
}
