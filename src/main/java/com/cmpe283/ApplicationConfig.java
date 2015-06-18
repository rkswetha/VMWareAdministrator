package com.cmpe283;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class ApplicationConfig {

	private static List<HostSystem> hostSystems;

	/*public static final String LOG_STORAGE_PATH = System
			.getProperty("user.home") + "/Desktop/";*/
	public static final String LOG_STORAGE_PATH = "/tmp/";

	public static  String VM_NAME = "VM-Logstash-Test";  //can take this from command prompt

	public synchronized List<HostSystem> getHostSystems() {
		return hostSystems;
	}

	//private static ConfigurationUtility instance;
	private static ServiceInstance serviceInstance;
	private static ServiceInstance adminServiceInstance;
	private static InventoryNavigator inventoryNavigator;
	
	private static final String[] performanceCounters = { "cpu.usage.average", "mem.usage.average",
														 "net.usage.average", "disk.read.average","disk.write.average" };
	
	private static PerformanceManager performanceManager;
	private static PrintStream outStream;

	/*
	 * private ConfigurationUtility() throws MalformedURLException,
	 * RemoteException{
	 * 
	 * }
	 */

	public static PerformanceManager getPerformanceManager() {
		return performanceManager;
	}


	public static void init() {
		hostSystems = new ArrayList<HostSystem>();
		URL url;
		try {
			url = new URL("https://130.65.132.114/sdk");

			URL urlAdmin = new URL("https://130.65.132.19/sdk");
			serviceInstance = new ServiceInstance(url, "administrator",
					"12!@qwQW", true);
			adminServiceInstance = new ServiceInstance(urlAdmin,
					"student@vsphere.local", "12!@qwQW", true);

			inventoryNavigator = new InventoryNavigator(
					serviceInstance.getRootFolder());
			
			performanceManager = serviceInstance.getPerformanceManager();
			outStream = new PrintStream(new FileOutputStream(
					LOG_STORAGE_PATH + "VM"
					+ "-log.txt", true));
			String vmname = /*"TM14-VM07-Ubu";*/ setVirtualMachineName();
			if(vmname!=null)
				VM_NAME = vmname;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ServiceInstance getServiceInstance() {
		return serviceInstance;
	}

	public static ServiceInstance getAdminServiceInstance() {
		return adminServiceInstance;
	}

	public static VirtualMachine getVM() {

		VirtualMachine vm = null;
		try {
			vm = (VirtualMachine) inventoryNavigator
					.searchManagedEntity("VirtualMachine", VM_NAME);
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vm;

	}
	
	public static HostSystem getvHost() {

		HostSystem vmHost = null;
		try {
			ManagedEntity[] hosts = inventoryNavigator.searchManagedEntities("HostSystem");
			for (int i = 0; i < hosts.length; i++) {
				HostSystem host = (HostSystem) hosts[i];
				VirtualMachine vms[] = host.getVms();
				for (int p = 0; p < vms.length; p++) {
					VirtualMachine v = (VirtualMachine) vms[p];
					if ((v.getName().toLowerCase())
							.equals(VM_NAME.toLowerCase())) {
						vmHost = host;
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error in get Statistics:" + e.getMessage());
		}
		return vmHost;

	}
	
	public static void writeLog(String inputLogs){
		if(outStream==null){
			try {
				outStream= new PrintStream(new FileOutputStream(
					LOG_STORAGE_PATH + "VM"
							+ "-log.txt", true));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("Given file path is not valid !");
				e.printStackTrace();
			}
		}
	
		outStream.append(inputLogs);
		
	}


	public static String[] getPerformanceCounters() {
		// TODO Auto-generated method stub
		return performanceCounters;
	}

	/*
	 * public static ConfigurationUtility getInstance(){ if(instance==null){ try {
	 * instance = new ConfigurationUtility(); } catch (MalformedURLException e) {
	 * // TODO Auto-generated catch block //e.printStackTrace();
	 * System.out.println("Malformed URL Exception"); } catch (RemoteException
	 * e) { // TODO Auto-generated catch block //e.printStackTrace();
	 * System.out.println("Remote Exception while connecting"); } } return
	 * instance; }
	 */
	private static String setVirtualMachineName(){
		
		InetAddress iAddress = null;
		try {
			iAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String currentIp = iAddress.getHostAddress();
		System.out.println("ip address "+currentIp);
		
		ManagedEntity[] hosts = null;
		try {
			hosts = inventoryNavigator.searchManagedEntities("VirtualMachine");
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < hosts.length; i++) {
			VirtualMachine vm = (VirtualMachine) hosts[i];
			if(!vm.getConfig().template && null != vm.getGuest().getIpAddress()){
			if(vm.getGuest().getIpAddress().equals(currentIp)){
				String vmName = vm.getName();
				return vmName;
			}
			}
		}
		
		return null;
	}
}
