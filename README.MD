## 分支描述
### 1.  p_kali 分支说明
    1).  本分支主要是针对"卡励空气净化器"项目进行的定制开发;

    2).  分支主要是针对移动端的特殊功能需求定制开发,与商用租赁产品不同,不能合并到产品的分支;


#### 特性功能说明
    1). 卡励采用微信硬件接口生成的二维码,因此只支持微信端使用,只有卡励厂商独立运营;

    2). 定时任务,卡励的移动端原型中定义了用户可以设置定时任务的操作,其他产品中未包含该功能;
        设计思路: 新增表order_timer,记录每个订单用户设置的定时任务,同时增加定时任务操作

    3). 充值优惠的配置项,不同运营商可以配置不同的充值优惠信息; (后台业务系统暂无相应界面及接口)
        设计思路: 在运营商扩展表operator_ext表中增加recharge_promotion字段,用户存储运营商设置的充值规则
   
   ##### 针对充值优惠配置信息(recharge_promotion字段描述)
   ```
   {
   	'percentage':23,
   	'promotions':[
   		{
   			'money':100.00,
   			'promotion':5.00
   		},
   		{
   			'money':200.00,
   			'promotion':10.00
   		}
   	]
   }
   ```   
 ##### 配置项说明
     1、percentage:  用户手动输入金额的优惠比例，此处存储的是百分数分子项
     
     2、promotions:  运营商配置的充值优惠项集合
     
     3、money:  用户实际的支付金额
     
     4、promotion: 商家返给用户的赠送金额
    
注:卡励的项目要求是,充值赠送的金额不能参与分润,因此要将订单支付金额的组成记录下来,分润生成时要注意做区分

### 2.  f_xzy_order_person分支说明
    1). 主要针对"享智云空气净化器"项目进行的定制化开发

    2). 主要针对的是对移动端特殊功能定制开发,与商用租赁产品不同,不能合并到产品的分支

#### 功能特性说明
    1). 享智云移动端页面要同时兼容微信、支付宝和普通浏览器的访问,二维码的生成方式采用的是网页形式

    2). 押金规则,享智云要求所有用户必须先交指定押金金额,才能使用设备;
        设计思路: 在运营商扩展表operator_ext表中增加cash_pledge的字段,用于不同运营商设置不同的押金金额,用户在使用指定运营商设备的时候加载不同的押金金额。

### 3.  p_06_qek分支说明
        1). 主要针对"沁尔康"项目进行的定制化开发
    
        2). 主要针对的是对移动端特殊功能定制开发,与商用租赁产品不同,不能合并到产品的分支
    
#### 功能特性说明
        1). 沁尔康二维码的生成方式采用的是网页形式
    
        2). 沁尔康一个设备拥有六个出水口，需要分别对其进行监控，分别记录状态进行数据点上报和下发指令
        设计：添加一个device_ext表，存储设备的出水口及状态
             添加一个 order_base_ext表，将用户下单的出水口号，出水量记录
        3).收费模式需要配置最小单位：毫升
        4）.后台需要修改创建订单，设备数据点上报和设备出水口状态记录