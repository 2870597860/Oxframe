package org.n52.oxf.ui.swing.ses;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.applet.*;
import javax.swing.*;

//ֱ�ӵ���SoundHelper.PlaySound();����
//��Sound�ļ��п�����ǰ��Ŀ�¼���
public class SoundHelper
{
 static AudioClip m;

 
 public static void PlaySound(){
	 File   directory = new  File(".");  
	 String root=null;
	try {
		root = directory.getCanonicalPath();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	String path=root+"\\Sound\\alarm.wav";
	String filepath="file:/"+path.replace('\\', '/');
	
 try
  {
   m=Applet.newAudioClip(new URL(filepath));//"file:/e:/ringout.wav"
  }
  catch(Exception e)
  {
   e.printStackTrace();
  }
  JFrame f = new JFrame("nidaye");
  //m.loop();
	m.play();
	m.play();
//	try {
//		Thread.sleep(2000);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
}
 public static void main(String[] args){
	  PlaySound();
	 }
}