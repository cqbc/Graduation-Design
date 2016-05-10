package com.cqupt.spider.start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import com.cqupt.common.enums.CrawlTypeEnum;
import com.cqupt.common.enums.RetCodeEnum;
import com.cqupt.common.enums.RetDescEnum;
import com.cqupt.common.enums.TaskTypeEnum;
import com.cqupt.common.statics.StaticValue4RelationMap;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.FileOperatorUtil;
import com.cqupt.spider.pojos.CrawlTaskPojo;
import com.cqupt.spider.pojos.RetStatus;

public class SpiderUI extends JFrame implements ActionListener{

	JButton button1_1 ;
	JButton button1_2 ;
	JButton button2_1 ;
	JButton button2_2 ;
	
	JTextField text1_1;
	JTextField text1_2;
	JTextField text1_3;
	JTextField text1_4;
	JTextField text1_5;
	JTextField text1_6;
	
	JTextField text2_1;
	JTextField text2_2;
	JTextField text2_3;
	JTextField text2_4;
	JTextField text2_5;
	JTextField text2_6;
	
	
	
	public static void main(String[] args) {
		new SpiderUI();
	}
	
	SpiderUI(){
		
		setBounds(100,100,500,300);
		setVisible(true);
		setTitle("WebSpider任务管理器");
		
		
		//创建元搜索的UI
		JPanel layer1 = new JPanel();
		
		JLabel lable1_1 = new JLabel("抓取原始标题：");
		text1_1 = new JTextField(10);
		text1_1.setText("元搜索");
		text1_1.setEnabled(false);
		text1_1.setDisabledTextColor(Color.RED);
		
		JLabel lable1_2 = new JLabel("抓取方式：");
		text1_2 = new JTextField(10);
		text1_2.setText("MetaSearch_NEWSPage");
		text1_2.setEnabled(false);
		text1_2.setDisabledTextColor(Color.RED);
		
		JLabel lable1_3 = new JLabel("搜索关键词：");
		text1_3 = new JTextField(20);
		text1_3.setText("例如：英雄联盟");
		
		JLabel lable1_4 = new JLabel("抓取页面数：");
		text1_4 = new JTextField(3);
		text1_4.setText(SystemParasSpider.crawl_max_page_number+"");
		
		JLabel lable1_5 = new JLabel("媒体类型：");
		text1_5 = new JTextField(3);
		text1_5.setText("1");
		
		JLabel lable1_6 = new JLabel("任务级别：");
		text1_6= new JTextField(3);
		text1_6.setText("B");
		
		Box	boxV1_1 = Box.createVerticalBox();
	
		boxV1_1.add(lable1_1);
		boxV1_1.add(Box.createVerticalStrut(10));
		boxV1_1.add(lable1_2);
		boxV1_1.add(Box.createVerticalStrut(10));
		boxV1_1.add(lable1_3);
		boxV1_1.add(Box.createVerticalStrut(10));
		boxV1_1.add(lable1_4);
		boxV1_1.add(Box.createVerticalStrut(10));
		boxV1_1.add(lable1_5);
		boxV1_1.add(Box.createVerticalStrut(10));
		boxV1_1.add(lable1_6);
		
		Box boxV1_2 = Box.createVerticalBox();
		boxV1_2.add(text1_1);
		boxV1_2.add(text1_2);
		boxV1_2.add(text1_3);
		boxV1_2.add(text1_4);
		boxV1_2.add(text1_5);
		boxV1_2.add(text1_6);
		
		Box baseBox1 = Box.createHorizontalBox();
		baseBox1.add(boxV1_1);
		baseBox1.add(Box.createHorizontalStrut(10));
		baseBox1.add(boxV1_2);
		
		button1_1 = new JButton("增加任务");
		button1_2 = new JButton("删除任务");
		button1_1.addActionListener(this);
		button1_2.addActionListener(this);
		
		
		layer1.add(baseBox1);
		layer1.add(button1_1);
		layer1.add(button1_2);
		
		
		//创建WebPage_URL的UI
		
		JPanel layer2 = new JPanel();
		
		JLabel lable2_1 = new JLabel("抓取原始标题：");
		text2_1 = new JTextField(10);
		text2_1.setText("XXX新闻");
		
		JLabel lable2_2 = new JLabel("抓取方式：");
		text2_2 = new JTextField(10);
		text2_2.setText("WebPage_URL");
		text2_2.setEnabled(false);
		text2_2.setDisabledTextColor(Color.RED);
		
		JLabel lable2_3 = new JLabel("网站首页URL：");
		text2_3 = new JTextField(20);
		text2_3.setText("例如：http://news.sina.com.cn/");
		
		JLabel lable2_4 = new JLabel("抓取深度：");
		text2_4 = new JTextField(3);
		text2_4.setText(SystemParasSpider.depth+"");
		
		JLabel lable2_5 = new JLabel("媒体类型：");
		text2_5 = new JTextField(3);
		text2_5.setText("1");
		
		JLabel lable2_6 = new JLabel("任务级别：");
		text2_6= new JTextField(3);
		text2_6.setText("B");
		
		
		Box	boxV2_1 = Box.createVerticalBox();
		boxV2_1.add(lable2_1);
		boxV2_1.add(Box.createVerticalStrut(10));
		boxV2_1.add(lable2_2);
		boxV2_1.add(Box.createVerticalStrut(10));
		boxV2_1.add(lable2_3);
		boxV2_1.add(Box.createVerticalStrut(10));
		boxV2_1.add(lable2_4);
		boxV2_1.add(Box.createVerticalStrut(10));
		boxV2_1.add(lable2_5);
		boxV2_1.add(Box.createVerticalStrut(10));
		boxV2_1.add(lable2_6);
		
		Box boxV2_2 = Box.createVerticalBox();
		boxV2_2.add(text2_1);
		boxV2_2.add(text2_2);
		boxV2_2.add(text2_3);
		boxV2_2.add(text2_4);
		boxV2_2.add(text2_5);
		boxV2_2.add(text2_6);
		
		Box baseBox2 = Box.createHorizontalBox();
		baseBox2.add(boxV2_1);
		baseBox2.add(Box.createHorizontalStrut(10));
		baseBox2.add(boxV2_2);
		
		button2_1 = new JButton("增加任务");
		button2_2 = new JButton("删除任务");
		button2_1.addActionListener(this);
		button2_2.addActionListener(this);
		
		layer2.add(baseBox2);
		layer2.add(button2_1);
		layer2.add(button2_2);
		
		
		//创建种子文件上传UI
		JPanel layer3 = new JPanel();
		
		final JTextField text3 = new JTextField(30);
		text3.setEnabled(false);
		text3.setDisabledTextColor(Color.RED);
		
		JButton button3 = new JButton("上传种子文件");
		button3.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   int result = 0;
				   String path = null;
				   JFileChooser fileChooser = new JFileChooser();
					FileSystemView fsv = FileSystemView.getFileSystemView();
					// 得到桌面路径
					fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
					fileChooser.setDialogTitle("请选择要上传的种子文件...");
					fileChooser.setApproveButtonText("确定");
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					result = fileChooser.showOpenDialog(SpiderUI.this);
					if (JFileChooser.APPROVE_OPTION == result) {
						path = fileChooser.getSelectedFile().getPath();
						text3.setText(path);
						String fileName=path.substring(path.lastIndexOf("\\")+1);
						FileOperatorUtil.copyFile(path,"seeds/"+ fileName);
					}
			    }
			  });
		
		
		layer3.add(text3);
		layer3.add(button3);
		
		
		
		//创建系统服务UI
		JPanel layer4 = new JPanel();
		
		final JButton button4_1 = new JButton("开启WebService服务");
		final JButton button4_2 = new JButton("关闭WebService服务");
		final JButton button4_3 = new JButton("开启Spider主节点服务");
		final JButton button4_4 = new JButton("开启Spider从节点服务");
		final JButton button4_5 = new JButton("关闭所有服务");
		
		button4_1.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   button4_1.setEnabled(false);
				   button4_1.setBackground(Color.GREEN);
				   button4_2.setEnabled(true);
				   button4_2.setBackground(Color.RED);
				   SpiderUIService.startWebService();
			   }
			  });
		
		
		
		button4_2.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   button4_2.setEnabled(false);
				   button4_2.setBackground(Color.GREEN);
				   button4_1.setEnabled(true);
				   button4_1.setBackground(Color.RED);
				   SpiderUIService.stopWebService();
			   }
			  });
		
		
	
		button4_3.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   button4_3.setEnabled(false);
				   button4_3.setBackground(Color.GREEN);
				   SpiderUIService.SpiderUITempThread t1 = new SpiderUIService.SpiderUITempThread("start");
				   t1.start();
			   }
			  });
		
		button4_4.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   button4_4.setEnabled(false);
				   button4_4.setBackground(Color.GREEN);
				   SpiderUIService.SpiderUITempThread t2 = new SpiderUIService.SpiderUITempThread("stop");
				   t2.start();
			   }
			  });
		
		button4_5.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   SpiderUIService.stopWorld();
			
			   }
			  });
		
		layer4.add(button4_1);
		layer4.add(button4_2);
		layer4.add(button4_3);
		layer4.add(button4_4);
		layer4.add(button4_5);
		
		
		
		//主界面UI布局
		JTabbedPane p = new JTabbedPane(JTabbedPane.LEFT);
		p.add("系统服务",layer4);
		p.add("元搜索任务",layer1);
		p.add("WebPage_Url任务",layer2);
		p.add("种子任务",layer3);
		p.validate();
		add(p,BorderLayout.CENTER);
		
		
		validate();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button1_1){
			int result = JOptionPane.showConfirmDialog(this, "是否执行任务？","确认对话框",JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
            	CrawlTaskPojo taskPojo = new CrawlTaskPojo();
    			
    			taskPojo.setTitle(text1_1.getText().trim());
    			taskPojo.setValue(text1_3.getText().trim());
    			taskPojo.setCrawlPageNumber(Integer.parseInt(text1_4.getText().trim()));
    			taskPojo.setType(TaskTypeEnum.Keyword);
    			taskPojo.setCrawlEngine(CrawlTypeEnum.MetaSearch_NEWSPage);
    			taskPojo.setMedia_type(Integer.parseInt(text1_5.getText().trim()));
    			// task level枚举转换
    			taskPojo.setLevel(StaticValue4RelationMap
    					.getTaskLevelEnumByString(text1_6.getText().trim()));

    			taskPojo.setSource_title(taskPojo.getTitle()+"_"+taskPojo.getValue());
    			RetStatus rs = SpiderUIService.CallTaskService(taskPojo, 1);
    			
    			if(rs.getRetCode() == RetCodeEnum.Ok && rs.getRetDesc() == RetDescEnum.Success){
  	                	JOptionPane.showMessageDialog(this, "任务添加成功！");
    			}else{
    				JOptionPane.showMessageDialog(this, "任务添加失败！");
    			}
    			
            }
			
		
		}else if(e.getSource() == button1_2){
			
			int result = JOptionPane.showConfirmDialog(this, "是否执行任务？","确认对话框",JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
            	CrawlTaskPojo taskPojo = new CrawlTaskPojo();
    			
    			taskPojo.setTitle(text1_1.getText().trim());
    			taskPojo.setValue(text1_3.getText().trim());
    			taskPojo.setCrawlPageNumber(Integer.parseInt(text1_4.getText().trim()));
    			taskPojo.setType(TaskTypeEnum.Keyword);
    			taskPojo.setCrawlEngine(CrawlTypeEnum.MetaSearch_NEWSPage);
    			taskPojo.setMedia_type(Integer.parseInt(text1_5.getText().trim()));
    			// task level枚举转换
    			taskPojo.setLevel(StaticValue4RelationMap
    					.getTaskLevelEnumByString(text1_6.getText().trim()));

    			taskPojo.setSource_title(taskPojo.getTitle()+"_"+taskPojo.getValue());
    			RetStatus rs = SpiderUIService.CallTaskService(taskPojo, 2);
    			if(rs.getRetCode() == RetCodeEnum.Ok && rs.getRetDesc() == RetDescEnum.Success){
	                	JOptionPane.showMessageDialog(this, "任务添加成功！");
    			}else{
    				JOptionPane.showMessageDialog(this, "任务添加失败！");
    			}
    			
            }
			
		}else if(e.getSource() == button2_1){
			int result = JOptionPane.showConfirmDialog(this, "是否执行任务？","确认对话框",JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
            	CrawlTaskPojo taskPojo = new CrawlTaskPojo();
    			
    			taskPojo.setTitle(text2_1.getText().trim());
    			taskPojo.setValue(text2_3.getText().trim());
    			taskPojo.setDepth(Integer.parseInt(text2_4.getText().trim()));
    			taskPojo.setCurrent_depth(0);
    			taskPojo.setTopN(SystemParasSpider.topN);
    			taskPojo.setType(TaskTypeEnum.Url);
    			taskPojo.setCrawlEngine(CrawlTypeEnum.WebPage_Url);
    			taskPojo.setMedia_type(Integer.parseInt(text1_5.getText().trim()));
    			// task level枚举转换
    			taskPojo.setLevel(StaticValue4RelationMap
    					.getTaskLevelEnumByString(text1_6.getText().trim()));

    			taskPojo.setSource_title(taskPojo.getTitle()+"_"+taskPojo.getValue());
    			RetStatus rs = SpiderUIService.CallTaskService(taskPojo, 1);
    			if(rs.getRetCode() == RetCodeEnum.Ok && rs.getRetDesc() == RetDescEnum.Success){
	                	JOptionPane.showMessageDialog(this, "任务添加成功！");
    			}else{
    				JOptionPane.showMessageDialog(this, "任务添加失败！");
    			}
    			
            }
			
		}else if(e.getSource() == button2_2){
			int result = JOptionPane.showConfirmDialog(this, "是否执行任务？","确认对话框",JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
            	CrawlTaskPojo taskPojo = new CrawlTaskPojo();
    			
    			taskPojo.setTitle(text2_1.getText().trim());
    			taskPojo.setValue(text2_3.getText().trim());
    			taskPojo.setDepth(Integer.parseInt(text2_4.getText().trim()));
    			taskPojo.setCurrent_depth(0);
    			taskPojo.setTopN(SystemParasSpider.topN);
    			taskPojo.setType(TaskTypeEnum.Url);
    			taskPojo.setCrawlEngine(CrawlTypeEnum.WebPage_Url);
    			taskPojo.setMedia_type(Integer.parseInt(text1_5.getText().trim()));
    			// task level枚举转换
    			taskPojo.setLevel(StaticValue4RelationMap
    					.getTaskLevelEnumByString(text1_6.getText().trim()));

    			taskPojo.setSource_title(taskPojo.getTitle()+"_"+taskPojo.getValue());
    			RetStatus rs = SpiderUIService.CallTaskService(taskPojo, 2);
    			if(rs.getRetCode() == RetCodeEnum.Ok && rs.getRetDesc() == RetDescEnum.Success){
	                	JOptionPane.showMessageDialog(this, "任务删除成功！");
    			}else{
    				JOptionPane.showMessageDialog(this, "任务删除失败！");
    			}
    			
            }
			
		}
	}
	
	
}
