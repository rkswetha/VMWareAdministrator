package com.cmpe283;


import com.cmpe283.ApplicationConfig;
import com.cmpe283.StatsCollector;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.VirtualMachine;

public class LogWriter extends Thread{
	
	private VirtualMachine virtualMachine;
	private HostSystem vHost;
	
	@Override
	public void run() {
		StringBuffer buffer = new StringBuffer();
		while(true){
			
			try {
				buffer.append(StatsCollector.generate(virtualMachine));
				buffer.append(StatsCollector.generate(vHost));
				ApplicationConfig.writeLog(buffer.toString());
				buffer.setLength(0);    //clearing buffer
				Thread.sleep(21000);     //21 sec waiting period as 
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}
	}
	
	public VirtualMachine getVirtualMachine() {
		return virtualMachine;
	}
	public void setVirtualMachine(VirtualMachine virtualMachine) {
		this.virtualMachine = virtualMachine;
	}
	public HostSystem getvHost() {
		return vHost;
	}
	public void setvHost(HostSystem vHost) {
		this.vHost = vHost;
	}
	
	

}
