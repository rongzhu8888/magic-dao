# magic-dao #

## 1. 解决问题 ##

- 简化数据库访问（通过对JdbcTemplate的封装使得单表增删改查无需写任何SQL）

- 多数据源读写分离（默认写主库读从库，支持指定从主库或者任一从库读取）

- 分表（同时支持单一字段和多字段分表；支持用户自定义分表处理逻辑）



**使用过程中如有任何疑问请加QQ（1609924522）沟通** 。

## 2. 设计说明  ##

...

## 3. 使用说明 ##

**magic-dao** 已经上传到Maven中央仓库中（[http://search.maven.org/](http://search.maven.org/)），项目中添加如下依赖即可导入。

	<dependency>
		<groupId>com.github.rongzhu8888</groupId>
    	<artifactId>magic-dao</artifactId>
    	<version>1.0.1</version>
	</dependency>

内部依赖spring-jdbc:4.2.3.RELEASE、 spring-aspects:4.2.3.RELEASE，如果spring版本冲突，可以将内部依赖排除：

	<dependency>
		<groupId>com.github.rongzhu8888</groupId>
    	<artifactId>magic-dao</artifactId>
    	<version>1.0.1</version>
		<exclusions>
	        <exclusion>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-jdbc</artifactId>
	        </exclusion>
	        <exclusion>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-aspects</artifactId>
	        </exclusion>
	    </exclusions>
	</dependency>



### 3.1 数据源配置 ###


- 单数据源

	    <!-- 可以被替换为JNDI、DBCP等任何数据源 -->
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

		<bean id="singleDataSource" class="MagicSingleDataSource">
			<property name="dataSource" ref="myDataSource" />
		</bean>



- 多数据源（读写分离)

	默认写master库，读slave库（如果开发者需要定制哪些service或者业务需要读master库，请见**3.5 读写分离**模块）


		<bean id="master" class="com.mchange.v2.c3p0.ComboPooledDataSource">
			...
		</bean>

		<bean id="slave1" class="com.mchange.v2.c3p0.ComboPooledDataSource">
			...
		</bean>

		<bean id="slave2" class="com.mchange.v2.c3p0.ComboPooledDataSource">
			...
		</bean>

		<bean id="multiDataSource" class="MagicMultipleDataSource">
			<property name="master" ref="master" />
			<property name="slaves">
				<map>
					<entry key="slave1" value-ref="slave1" /> <!-- key表示该数据源的alias -->
					<entry key="slave2" value-ref="slave2" />
			</map>
			</property>
		</bean>


### 3.2 PO与Column映射配置 ###

**magic-dao** 提供了三类注解用以配置实体对象与表字段的映射关系。

**@Table** ：PO类注解，表示当前实体对应表名

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Table {

    	String name();

	}


**@Key** ：PO类属性注解，表示当前属性对应的字段为主键

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Key {

	    String column();

	    boolean autoIncrement() default false;

	}

**@Column** ：PO类属性注解，表示当前属性对应表中的字段名

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Column {

	    String value();

	    boolean readOnly() default false; //not need to insert and update

	}



我们将从以下三种情况说明如何配置PO对象与Table的映射关系：

- **普通唯一主键**

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

- **自增唯一主键**

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


- **联合主键**

		//主键对象
		public class UserRoleKey implements Serializable {

		    @Key(column = "user_id")
		    private Long userId;

		    @Key(column = "role_id")
			private String roleId;

			... <省略getXxx和setXxx方法>

		}

		...

		//PO对象
		@Table(name = "mc_user_role")
		public class UserRolePo extends UserRoleKey {

			@Column(value = "create_time")
		    private Date createTime;

			... <省略getXxx和setXxx方法>

		}



###3.3 Dao接口与实现类 ###

**magic-dao** 对单表访问（增删改查）非常方便，只需简单定义该表的接口与实现类（接口继承**MagicDao** ,
实现类继承**MagicGenericDao** ）即可。

MagicDao接口的泛型为<KEY, ENTITY>，具体用法如下：

- 唯一主键

		//接口
		public interface MagicAppDao extends MagicDao<Long, AppPo> {

		}

		...

		//实现类
		public class MagicAppDaoImpl extends MagicGenericDao<Long, AppPo> implements MagicAppDao {

		}


- 联合主键

		//接口
		public interface UserRoleDao extends MagicDao<UserRoleKey, UserRolePo> {

		}

		...

		//实现类
		public class UserRoleDaoImpl extends MagicGenericDao<UserRoleKey, UserRolePo> implements MagicAppDao {

		}



###3.4 事务 ###
直接使用Spring的DataSourceTransactionManager即可，注意多数据源场景下DataSourceTransactionManager的dataSource属性应该配置为指向master库的数据源。

	<!-- 事务管理器 -->
	<bean id="transactionManager"
		  class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="master" />
	</bean>
	<!-- 事务注解驱动，标注@Transactional的类和方法将具有事务性 -->
	<tx:annotation-driven transaction-manager="transactionManager" />


###3.5 读写分离 ###
我们知道，多数据源读写分离场景中，一般是写master库，读slave库。**magic-dao** 默认情况下也是如此，开发者只需提供多个数据源，并配置到**MagicMultipleDataSource** 实例中即可，无需做任何额外配置。

然而，某些场景对数据的实时性要求非常高，需要从master库读取数据，更极端的情况下，某些数据需要从指定的slave库中读取，对于这些需求，magic-dao同样支持。

**magic-dao** 提供了**@QueryDataSource** 注解和**RuntimeQueryDataSourceAop** 来满足该需求。

	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface QueryDataSource {

	    String alias() default ""; //指定完成数据读取的从库别名

	    DataSourceType type() default DataSourceType.SLAVE; //指定从主库OR从库读取数据，默认从库

	}



**使用方法：**

在Service实现类或者其具体某个方法上添加 **@QueryDataSource** 注解，并且在spring容器（applicationContext.xml)中添加**RuntimeQueryDataSourceAop** 配置。



- QueryDataSource注解

	（1）类级别注解

		//对该类中所有方法均有效
		@QueryDataSource(alias = "slave1")
		public class AppServiceImpl {

			...

		}


	（2）方法级别注解


		public class UserServiceImpl {

			//仅对该方法有效
			@QueryDataSource(type = DataSourceType.MASTER)
			public void getAttentionList() {

				...

			}

		...

		}



- AOP配置

		<bean id="dataSourceAop" class="pers.zr.opensource.magic.dao.runtime.RuntimeQueryDataSourceAop"></bean>
		<aop:config>
			<aop:aspect ref="dataSourceAop">
				<aop:around
					method="determine"
					pointcut="execution(* demo.xxx..*ServiceImpl*.*(..))" /> //这里视情况而定，无需拦截所有的ServiceImpl
			</aop:aspect>
		</aop:config>

**注意：**

(1) @QueryDataSource注解在类上，表示该类所有的方法都应用此注解；

(2) 方法级别的注解较类级别优先级高，如果方法和类同时具有@QueryDataSource注解，则取方法级别注解；

(3) @QueryDataSource的alias属性比type优先级高，如果指定了alias，则忽略type属性。


###3.6 分表 ###

**magic-dao** 提供**@TableShard** 注解用以配置分表策略。

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface TableShard {

	    String shardTable();

	    String[] shardColumns(); //支持多字段分表

	    String separator() default "_";

	    int shardCount();
	}


**magic-dao** 使用提供了默认的分表处理器DefaultTableShardHandler用以根据分表策略计算实际表名。

	public class DefaultTableShardHandler implements TableShardHandler {

    @Override
    public String getRealTableName(TableShardStrategy shardStrategy, Object... columnValues) {
        if(shardStrategy == null) {
            throw new RuntimeException("Failed to get actual table name, caused by tableShardStrategy is null!");
        }
        if(columnValues == null || columnValues.length < 1) {
            throw new RuntimeException("Failed to get actual table name, caused by columnValue is empty or null!");
        }

        StringBuilder sb = new StringBuilder();
        for(Object o : columnValues) {
            sb.append(String.valueOf(o));
        }
        int tableIndex = HashSlotUtil.getSlot(sb.toString(), shardStrategy.getShardCount());
        return shardStrategy.getShardTable() + shardStrategy.getSeparator() + ((tableIndex < 10) ? "0" + tableIndex : String.valueOf(tableIndex));
    }
}

**使用方法：**

- **单表分表访问**

	实现单表分表访问非常简单，只需通过**@TableShard** 在PO对象上配置分表策略即可。

		@TableShard(shardTable = "mc_orders", shardCount = 32, shardColumn = "user_id", separator = "_")
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


-	**联合查询分表访问**

	尽量避免在代码中对子表（分表）进行联合查询，可以通过多次访问数据库并在应用层中进行整合。


-	**自定义分表处理器**

	**magic-dao** 默认使用**DefaultTableShardHandler** 读取分表策略并根据shard字段的值利用**JedisHashSlot**  算法（redis cluster中key定位算法）计算实际表名，所以默认情况下不支持auto-increment字段（自增长字段在insert前无法知晓具体值），不过开发人员可以对该种情况实现自己的分表逻辑（如随机）。

	**magic-dao**为开发人员预留了自定义分表逻辑的空间，只需实现**TableShardHandler** 接口，并在Spring容器中将该handler实例注入到对应的dao实例中即可。


		public class MyTableShardHandler implements TableShardHandler {
		    @Override
		    public String getShardTableName(TableShardStrategy shardStrategy, Object... columnValue) {

				//实现自己的分表逻辑

				return xxx;
			}
	    }


	...

		<bean id="MyShardHandler" class="test.pers.zr.magic.dao.core.action.MyTableShardHandler" />

		<bean id="appDao" class="demo.pers.zr.magic.dao.app.MagicAppDaoImpl" >
			<property name="magicDataSource" ref="multiDataSource" />
			<property name="tableShardHandler" ref="MyShardHandler" />
		</bean>




###3.7 单表动态查询 ###
在介绍单表动态查询具体方法前，请先看**MagicDao** 提供的针对单表查询接口：

	//根据动态条件查询
	public List<ENTITY> query(Matcher...conditions);

	//根据动态条件查询并对结果排序
    public List<ENTITY> query(List<Order> orders, Matcher...conditions);

	//根据动态条件查询并分页
    public List<ENTITY> query(Page page, Matcher...conditions);

	//根据动态条件查询并对结果排序和分页
    public List<ENTITY> query(Page page, List<Order> orders, Matcher...conditions);

	//根据动态条件查询总数
    public Long getCount(Matcher...conditions);


**magic-dao** 通过**Matcher** 接口来抽象各类型的查询条件，具体如下：

-	**EqualsMatcher**

	>表示 **column = xxx**

-	**NotEqualsMatcher**

	>表示 **column != xxx**

-	**GreaterMatcher**

	>表示 **column > xxx**

-	**GreaterOrEqualsMatcher**

	>表示 **column >= xxx**

-	**LessMatcher**

	>表示 **column < xxx**

-	**LessOrEqualsMatcher**

	>表示 **column <= xxx**

-	**InMatcher**

	>表示 **column in (xxx, yyy, ... zzz)**

-	**LikeMatcher**

	>表示 **column like %xxx%**

-	**LeftLikeMatcher**

	>表示 **column like xxx%**

-	**RightLikeMatcher**

	>表示 **column like %xxx**

-	**BetweenAndMatcher**

	>表示 **column between XXX and yyyy**


熟悉了上述的动态查询和Matcher接口后，动态查询将会异常简单：

比如，查询指定用户的所有订单列表

	public List<UserOrder> getUserOrderList(Long userId) {

		Matcher userMatcher = new EqualsMatcher("user_id", userId);

		return userOrderDao.query(userMatcher);

	}

查询指定用户的所有订单列表并按时间倒叙排序

	public List<UserOrder> getUserOrderList(Long userId) {

		Matcher userMatcher = new EqualsMatcher("user_id", userId);

		List<Order> orders = new ArrayList<Order>();
		Order order = new Order("create_time", OrderType.DESC);
		orders.add(order);

		return userOrderDao.query(orders, userMatcher);

	}


分页查询用户的订单列表

	public List<UserOrder> getUserOrderList(Long userId, int pageNo, int pageSize) {

		Matcher userMatcher = new EqualsMatcher("user_id", userId);

		Page page = new Page(pageNo, pageSize);

		return userOrderDao.query(page, userMatcher);

	}



###3.8 多表联合查询 ###
复杂查询需要开发人员自己写SQL。只需要注意以下几点即可：

1、JdbcTemplate获取方式：

	JdbcTemplate jdbcTemplate = magicDataSource.getJdbcTemplate(ActionMode.QUERY);

2、RowMapper获取方式：

	//如果目标Mapper为当前Dao类的默认mapper，直接通过this.rowMapper即可访问

	//如果需要获取别的Dao类中的mapper,可以通过MapperContextHolder获取
	RowMapper rowMapper = MapperContextHolder.getRowMapper(xxxx.class);


###3.9 效率###
由于Spring单例模式的lazy-init属性默认值为false，即容器启动时，所有的Dao实例即被创建，在创建过程中，父类MagicGenericDao的默认构造器将被调用，用以扫描并初始化当前Dao所依赖的Po对象与表的映射关系以及Po字段与setXxx()和getXxx()的映射关系。所以，Spring容器在启动时便已经将所有的注解和映射关系都已经解析完毕，不用太担心效率问题。



## TODO ##

分库

