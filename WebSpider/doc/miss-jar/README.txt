在WebSpider项目的pom文件中,由于这俩个开源jar包是一位爬虫大神写的，不一定下得到，
并且jar包中还存在着一些其他的依赖jar包。所以特此声明一下，
在该文件夹下有上传的jar包和相应的源码。

需要获取更多的相关的jar包和源码，从该大神github下获取，
地址：https://github.com/erliang20088


WebSpider的pom文件获取大神其中俩个jar的方式
<!--大神开源包  -->
		<dependency>
			<groupId>com.zel.parser</groupId>
			<artifactId>jsoupparser_zel</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
		<dependency>
			<groupId>com.vaolan</groupId>
			<artifactId>url-analysis</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
