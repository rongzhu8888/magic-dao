# magic-dao #

## 背景 ##

>在实际项目中，无论是管理类软件，还是互联网系统，都免不了与数据库打交道，考虑到某些系统，尤其是互联网类系统，并发性要求很高，数据量巨大，迭代周期又比较短，这就要求在架构设计过程中，不仅需要采取各种提高数据库读写性能的手段（纯粹从软件角度考虑，假设硬件性能已满足），如**主从读写分离**（一主多从）、**分库分表**，还需要考虑如何能够快速完成开发，以保证开发效率。

>目前业界比较流行的java访问数据库访问框架有Hibernate、Mybatis、Spring-Jdbc等。Hibernate个人感觉偏重，如果要开发个只有几张表的小系统（模块\服务），使用Hibernate有点头重脚轻的感觉，其主打特性多级缓存在我看来也许不是那么重要，而且要完全熟练的掌握也得下一番功夫；而Mybatis和Hibernate一样，整个系统充斥着大量的OR映射文件,虽然它是目前许多互联网公司主流的DAO层框架，但是它依旧需要开发者自己解决主从读写分离和分表的问题；SpringJdbc同样如此。

>一直以来，项目中引入了许多各种类型的开源框架，以解决实际工作中遇到的各种问题和需求，如上述3种框架，当然还有许多其它类型的框架（比如阿里开源的dubbo等），个人非常敬佩这些框架作者的专业技能，更加欣赏他们拥有一颗开放的心。在这里，我将闲暇之余的一个作品magic-dao贡献到github上，希望能够或多或少对一些同学起到作用，如有问题请加QQ（**1609924522**）沟通。

## 设计说明  ##

...

## 使用方法 ##

### 1. 数据源配置 ###


**>>>单数据源**


	<bean id="myDataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="user" value="${mysql.username}" />
		<property name="password" value="${mysql.password}" />
		<property name="driverClass" value="${mysql.driver_class}" />
		<property name="jdbcUrl" value="${mysql.url}" />
		<property name="maxPoolSize" value="${mysql.maxPoolSize}" />
		<property name="minPoolSize" value="${mysql.minPoolSize}" />
		<property name="initialPoolSize" value="${mysql.initialPoolSize}" />
		<property name="maxIdleTime" value="${mysql.maxIdleTime}" />
		<property name="checkoutTimeout" value="${mysql.checkoutTimeout}" />
		<property name="acquireIncrement" value="${mysql.acquireIncrement}" />
		<property name="acquireRetryAttempts" value="${mysql.acquireRetryAttempts}" />
		<property name="acquireRetryDelay" value="${mysql.acquireRetryDelay}" />
		<property name="autoCommitOnClose" value="${mysql.autoCommitOnClose}" />
		<property name="automaticTestTable" value="${mysql.automaticTestTable}" />
		<property name="breakAfterAcquireFailure" value="${mysql.breakAfterAcquireFailure}" />
		<property name="idleConnectionTestPeriod" value="${mysql.idleConnectionTestPeriod}" />
		<property name="maxStatements" value="${mysql.maxStatements}" />
		<property name="maxStatementsPerConnection" value="${mysql.maxStatementsPerConnection}" />
	</bean>

	<bean id="singleDataSource" class="pers.zr.magic.dao.MagicSingleDataSource">
		<property name="dataSource" ref="master" />
	</bean>



**>>>多数据源（读写分离）**


	<bean id="master" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		...
	</bean>

	<bean id="slave1" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		...
	</bean>

	<bean id="slave2" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		...
	</bean>

	<bean id="multiDataSource" class="pers.zr.magic.dao.MagicMultiDataSource">
		<property name="master" ref="master" />
		<property name="slaves">
			<list>
				<ref bean="slave1" />
				<ref bean="slave2" />
			</list>
		</property>
	</bean>


### 2. Po对象与表注解映射 ###

**四种注解：**

- @Table：实体类注解，表示当前实体对应哪个表

- @Key：实体类属性注解，表示当前属性对应的字段为主键

- @Column：实体类属性注解，表示当前属性对应表中的哪个字段

- @Shard：实体类注解，表示与当前实体对应的表采取的分表机制


**>>>普通唯一主键**

	@Table(name = "mc_app")
	public class AppPo implements Serializable {

		@Key(column = "app_id")
	    private String appId;

	    @Column(value = "app_name")
	    private String appName;

	    @Column(value = "app_code")
	    private String appCode;

	    @Column(value = "group_id")
	    private String groupId;

	    @Column(value = "create_time")
	    private Date createTime;

	    @Column(value = "update_time", readOnly = true)
	    private Date updateTime;

	    ... <省略getXxx和setXxx方法>
	}

**>>>自增唯一主键**

	@Table(name = "mc_app")
	public class AppPo implements Serializable {

		@Key(column = "id", autoIncrement = true)
	    private Long id;

	    @Column(value = "app_name")
	    private String appName;

	    @Column(value = "app_code")
	    private String appCode;

	    @Column(value = "group_id")
	    private String groupId;

	    @Column(value = "create_time")
	    private Date createTime;

	    @Column(value = "update_time", readOnly = true)
	    private Date updateTime;

	    ... <省略getXxx和setXxx方法>
	}


**>>>联合主键**

	public class UserRoleKey implements Serializable {

	    @Key(column = "user_id")
	    private Long userId;

	    @Key(column = "role_id")
		private String roleId;

		... <省略getXxx和setXxx方法>

	}

	...

	@Table(name = "mc_user_role")
	public class UserRolePo extends UserRoleKey {

		@Column(value = "create_time")
	    private Date createTime;

		... <省略getXxx和setXxx方法>

	}



**>>>分表**

	@Table(name = "mc_orders")
	@Shard(shardCount = 32, shardColumn = "user_id", separator = "_")
	public class OrderPo implements Serializable {
		@Key(column = "order_id")
		private Long orderId;

		@Column(value = "user_id")
		private Long userId;

		@Column(value = "create_time")
		private Date createTime;

		...

		... <省略getXxx和setXxx方法>

	}



###3. Dao接口与实现类 ###

**>>>唯一主键**

	//单表无需写任何方法，继承MagicDao接口即可
	public interface MagicAppDao extends MagicDao<Long, AppPo> {

	}

	...

	//单表无需实现任何方法，继承MagicGenericDao类即可
	public class MagicAppDaoImpl extends MagicGenericDao<Long, AppPo> implements MagicAppDao {

	}


**>>>联合主键**

	//单表无需写任何方法，继承MagicDao接口即可
	public interface UserRoleDao extends MagicDao<UserRoleKey, UserRolePo> {

	}

	...

	//单表无需实现任何方法，继承MagicGenericDao类即可
	public class UserRoleDaoImpl extends MagicGenericDao<UserRoleKey, UserRolePo> implements MagicAppDao {

	}


###4. Dao实例spring托管###
由于spring单例模式的lazy-init属性默认值为false，即容器启动时，所有的Dao实例即被创建，且在创建过程中，父类MagicGenericDao的默认构造器将被调用，用以扫描并初始化当前Dao所依赖的Po对象与表的映射关系以及Po字段与setXxx()和getXxx()的映射关系。所以，无需担心反射效率问题。

	<!-- 单数据源 -->
	<bean id="appDao" class="demo.pers.zr.magic.dao.app.MagicAppDaoImpl" >
		<property name="magicDataSource" ref="singleDataSource" />
	</bean>

	或者

	<!-- 多数据源（读写分离） -->
	<bean id="appDao" class="demo.pers.zr.magic.dao.app.MagicAppDaoImpl" >
		<property name="magicDataSource" ref="multiDataSource" />
	</bean>

###5. 事务 ###
直接使用spring的DataSourceTransactionManager即可，注意多数据源场景下DataSourceTransactionManager的dataSource属性应该配置为master。

	<!-- 事务管理器 -->
	<bean id="transactionManager"
		  class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="master" />
	</bean>
	<!-- 事务注解驱动，标注@Transactional的类和方法将具有事务性 -->
	<tx:annotation-driven transaction-manager="transactionManager" />

###6. 复杂SQL ###
magic-dao的设计初衷为解决读写分离和分表问题的同时提高开发效率（单表无需写SQL），并不表示满足所有的场景，如复杂的多表联合及嵌套查询等，那么如果遇到这种问题，使用了magic-dao组件后该咋办呢？不用担心，请继续阅读：

MagicSingleDataSource和MagicMultiDataSource都实现了接口MagicDataSource

	public interface MagicDataSource {

	    JdbcTemplate getJdbcTemplate(ActionMode actionMode);

	    DataSource getJdbcDataSource(ActionMode actionMode);

	}


该接口有2个方法，分别为getJdbcTemplate和getJdbcDataSource，所以对于复杂的SQL场景，可以首先通过这两个方法可以分别得到JdbcTemplate和DataSource对象，然后编程访问数据库即可。



## TODO ##

 1. 发布magic-dao到maven中央仓库中
 2. 完成设计说明
 3. 实现v1.0.1：支持分库机制