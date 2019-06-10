package com.yuanwang.common.utils;

import com.huawei.api.SMEntry;
import com.huawei.api.SMException;
import com.yuanwang.common.startup.StartUpRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class TestSms {
    private static final Logger LOGGER= LoggerFactory.getLogger(TestSms.class);
    /**
     *@author： Fangqun
     *@description: 向短信机插入命令发送短信
     *@param:  [dbConn, info, phone]
     *@return: int
     *@exception:
     *@date:  上午 9:38 2019/6/10 0010
     **/
    public static int testSendSms(Connection dbConn,String info,String phone){

        try {
            Statement stmt = dbConn.createStatement();
            String sql = "INSERT INTO tbl_SMSendTask " +
                    "      ( CreatorID, TaskName,SmSendedNum,  OperationType, SuboperationType, SendType, OrgAddr, DestAddr, SM_Content, SendTime, NeedStateReport, ServiceID, FeeType, FeeCode, MsgID, SMType, MessageID, DestAddrType, SubTime, TaskStatus, SendLevel, SendState, TryTimes, [Count], SuccessID) " +
                    "VALUES ('0000','','0','WAS','66','1','106573075060','"+phone+"','"+info+"',GETDATE(),'0','TZJ0010101','01','0','','0','0','0',GETDATE(),'0','0','0','3','1','0')";
            LOGGER.info(sql);
            stmt.executeUpdate(sql);
            LOGGER.info("插入成功！");

            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }

    }

    /**
     *@author： Fangqun
     *@description: 加载驱动，连接数据库
     *@param:  [info, phone]
     *@return: void
     *@exception:
     *@date:  上午 9:38 2019/6/10 0010
     **/
   public static void sendphoneMain(String info,String phone){

       String driverName="com.microsoft.jdbc.sqlserver.SQLServerDriver";
       String dbURL="jdbc:sqlserver://172.17.0.234:1433;DatabaseName=DB_CustomSMS";
       String userName="CustomSMS";
       String userPwd="SqlMsde@InfoxEie2000";
       try
       {
           Class.forName(driverName);
           LOGGER.info("加载驱动成功！");
       }catch(Exception e){
           e.printStackTrace();
           LOGGER.info("加载驱动失败！");
       }

       try{
           Connection dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
           LOGGER.info("连接数据库成功！");
           testSendSms(dbConn,info,phone);
       }catch(Exception e)
       {
           e.printStackTrace();
           LOGGER.info("SQL Server连接失败！");
       }
   }
    /**
     *@author： Fangqun
     *@description: 测试函数
     *@param:  [args]
     *@return: void
     *@exception:
     *@date:  上午 9:40 2019/6/10 0010
     **/
    public static void main(String[] args) throws SMException {
        sendphoneMain("你有待审查的报销流程（财务报销系统）","15988864336");

    }

}
