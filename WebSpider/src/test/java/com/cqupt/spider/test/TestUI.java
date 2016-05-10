package com.cqupt.spider.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

public class TestUI extends JFrame{

	public static void main(String[] args) {
		int result = 0;
		String path = null;
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// 得到桌面路径
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		fileChooser.setDialogTitle("请选择要上传的种子文件...");
		fileChooser.setApproveButtonText("确定");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		result = fileChooser.showOpenDialog(new TestUI());
		if (JFileChooser.APPROVE_OPTION == result) {
			path = fileChooser.getSelectedFile().getPath();
			
			System.out.println("path: " + path);
		}
		String fileName=path.substring(path.lastIndexOf("\\")+1);
		
		copyFile(path,"doc/"+ fileName);
		
		
		
	}
	
	/** 
	* 复制单个文件 
	* @param oldPath String 原文件路径 如：c:/adc.txt 
	* @param newPath String 复制后路径 如：f:/adc.txt 
	* @return boolean 
	*/ 
	public static void copyFile(String oldPath, String newPath) {
		try {
			
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			 // 文件存在时
			if (oldfile.exists()) {
				// 读入原文件
				InputStream inStream = new FileInputStream(oldPath); 
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			
			System.out.println("上传文件出错");
			e.printStackTrace();

		}

	} 

}
