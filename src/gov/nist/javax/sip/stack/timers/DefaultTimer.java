/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package gov.nist.javax.sip.stack.timers;

import gov.nist.javax.sip.stack.SIPStackTimerTask;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author jean.deruelle@gmail.com
 *
 */
public class DefaultTimer extends Timer implements SipTimer {

	private class DefaultTimerTask extends TimerTask {
		private SIPStackTimerTask task;

		public DefaultTimerTask(SIPStackTimerTask task) {
			this.task= task;
			task.setSipTimerTask((Runnable)this);
		}
		
		public void run() {
			 try {
				 task.runTask();
	        } catch (Throwable e) {
	            System.out.println("SIP stack timer task failed due to exception:");
	            e.printStackTrace();
	        }
		}
		
		@Override
		public boolean cancel() {
			if(task != null) {
				task.cleanUpBeforeCancel();
				task = null;
			}
			return super.cancel();
		}
	}
	
	@Override
	public boolean schedule(SIPStackTimerTask task, long delay) {		
		super.schedule(new DefaultTimerTask(task), delay);
		return true;
	}

	@Override
	public boolean schedule(SIPStackTimerTask task, long delay, long period) {
		super.schedule(new DefaultTimerTask(task), delay, period);
		return true;
	}
	
	@Override
	public boolean cancel(SIPStackTimerTask task) {
		return ((TimerTask)task.getSipTimerTask()).cancel();
	}

	@Override
	public void setConfigurationProperties(Properties configurationProperties) {
		// don't need the properties so nothing to see here
	}

	@Override
	public void stop() {
		cancel();		
	}



}
