1.尽量避免在同一个方法中操作不同的数据源,如果有，则建议将其拆分成两个方法，并分开调用，比如A调用B，则将B抽离出来与A同级
2.采用AOP代理目标方法，在目标方法结束后清除当前线程的数据源


CREATE TABLE `data_source_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alias` varchar(100) NOT NULL,
  `db_type` varchar(100) NOT NULL,
  `db_url` varchar(240) NOT NULL,
  `db_username` varchar(100) NOT NULL,
  `db_password` varchar(100) NOT NULL,
  `app_code` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `datasource_config_UN` (`alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8


CREATE TABLE `channels` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `level_type` varchar(100) NOT NULL,
  `app_code` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8


概念：
多数据源：就是同一个项目中可以有多个数据库的方法实例，通过创建不同的数据源datasource对象来访问相对应的数据库
多数据源切换：就是在操作时，可能需要操作多个数据库，则需要进行不同datasource的切换

实现方式：
方式1.直接在要切换的地方调用切换数据源的方法,获取新的数据源，通过datasource.getConnection获取新库的连接

方式2.统一管理数据源,将数据源交给当前线程去管理
原理就是将当前数据源设置到当前线程中，当其他地方要使用时，就从当前线程中取出当前线程持有的数据


Spring的支持：
    Spring提供了多数据源动态切换的支持
        AbstractRoutingDataSource类，该类实际还是一个DataSource类，只是该类重写了getConnection()方法
        在getConnection方法中通过调用determineTargetDataSource()来动态返回目标数据源datasource对象，再通过目标数据源datasource.getConnection()获取目标数据库的连接
        在determineTargetDataSource()方法中调用了模版方法determineCurrentLookupKey(),这个方法由用户自定义返回想要的数据源配置name
        
Mybatis的支持：
    Mybatis真正操作数据是通过每一个SqlSession会话，而一般创建SqlSession会话的都是通过SqlSessionFactory来创建,
    SqlSessionFactory一般在一个项目中是全局单实例的,要实现多数据源的支持我们有以下两种方式：
        方式1：手动切换方式：
                根据多个数据源创建多个SqlSessionFactory实例，每个SqlSessionFactory注入对应的数据源实例,在需要操作不同数据库时获取对应实例话的SqlSessionFactory，然后通过获取sqlSession进行操作,
                在Spring中使用SqlSessionTemplate来代理mybatis的操作，所以需要创建多个SqlSessionTemplate
        
        方式2：自动切换方式：
                创建一个单实例SqlSessionFactory，并注入Spring提供的多数据源切换的支持数据源类AbstractRoutingDataSource的实例,
                当通过SqlsessionFactory创建的SqlSession实例,再通过defaultSqlSession.getConnection()方法时,会调用AbstractRoutingDataSource类中的getConnection方法,并通过determineTargetDataSource()方法选择当前数据源
                而我们只需要重写determineCurrentLookupKey()方法，返回我们想要的数据即可
                

Transaction事物的支持：


在没有使用@Transaction事物的情况下：
org.apache.ibatis.session.defaults.DefaultSqlSessionFactory.openSession(org.apache.ibatis.session.ExecutorType)
在没有使用@Transaction的情况下，每次执行sql都会调用这个方法，所以会每次都创建一个Transaction
org.apache.ibatis.session.defaults.DefaultSqlSessionFactory.openSessionFromDataSource
    
    private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
        Transaction tx = null;
        try {
          final Environment environment = configuration.getEnvironment();
          final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
          //在这里的environment.getDataSource()实际返回的是注入到SqlSessionFactory中的数据DynamicDataSource的实例
          //这里创建的事物对象为SpringManagedTransaction
          tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
          final Executor executor = configuration.newExecutor(tx, execType);
          return new DefaultSqlSession(configuration, executor, autoCommit);
        } catch (Exception e) {
          closeTransaction(tx); // may have fetched a connection so lets call close()
          throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
        } finally {
          ErrorContext.instance().reset();
        }
      }

org.apache.ibatis.executor.SimpleExecutor.prepareStatement
    在这里开始调用去获取connection连接

org.apache.ibatis.executor.BaseExecutor.getConnection
    
    protected Connection getConnection(Log statementLog) throws SQLException {
        //这里通过前面创建的SpringManagedTransaction对象调用getConnection()方法
        Connection connection = transaction.getConnection();
        if (statementLog.isDebugEnabled()) {
          return ConnectionLogger.newInstance(connection, statementLog, queryStack);
        } else {
          return connection;
        }
      }

org.mybatis.spring.transaction.SpringManagedTransaction.getConnection
    
    public Connection getConnection() throws SQLException {
        //切不切数据源，关键点就在这块，当没有使用@Transaction时,this.connection每次进来都为null,而使用了@Transaction的话，会从本地线程中获取,所以this.connection不为null
        if (this.connection == null) {
          openConnection();
        }
        return this.connection;
      }

org.mybatis.spring.transaction.SpringManagedTransaction.openConnection

    private void openConnection() throws SQLException {
        //在这里去获取连接，这里的dataSource还是DynamicDataSource对象
        this.connection = DataSourceUtils.getConnection(this.dataSource);
        this.autoCommit = this.connection.getAutoCommit();
        this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);
    
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug(
              "JDBC Connection ["
                  + this.connection
                  + "] will"
                  + (this.isConnectionTransactional ? " " : " not ")
                  + "be managed by Spring");
        }
      }



org.springframework.jdbc.datasource.DataSourceUtils.doGetConnection

    public static Connection doGetConnection(DataSource dataSource) throws SQLException {
    		Assert.notNull(dataSource, "No DataSource specified");
    
    		ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
    		if (conHolder != null && (conHolder.hasConnection() || conHolder.isSynchronizedWithTransaction())) {
    			conHolder.requested();
    			if (!conHolder.hasConnection()) {
    				logger.debug("Fetching resumed JDBC Connection from DataSource");
    				conHolder.setConnection(dataSource.getConnection());
    			}
    			return conHolder.getConnection();
    		}
    		// Else we either got no holder or an empty thread-bound holder here.
    
    		logger.debug("Fetching JDBC Connection from DataSource");
    		//在这个类中的Connection con = dataSource.getConnection(); 调用注入到SqlSessionFactory中的DataSource的getConnection()方法
            //而这个DataSource就是DynamicDataSource
    		Connection con = dataSource.getConnection();
    
    		if (TransactionSynchronizationManager.isSynchronizationActive()) {
    			logger.debug("Registering transaction synchronization for JDBC Connection");
    			// Use same Connection for further JDBC actions within the transaction.
    			// Thread-bound object will get removed by synchronization at transaction completion.
    			ConnectionHolder holderToUse = conHolder;
    			if (holderToUse == null) {
    				holderToUse = new ConnectionHolder(con);
    			}
    			else {
    				holderToUse.setConnection(con);
    			}
    			holderToUse.requested();
    			TransactionSynchronizationManager.registerSynchronization(
    					new ConnectionSynchronization(holderToUse, dataSource));
    			holderToUse.setSynchronizedWithTransaction(true);
    			if (holderToUse != conHolder) {
    				TransactionSynchronizationManager.bindResource(dataSource, holderToUse);
    			}
    		}
    
    		return con;
    	}

    
在添加了@Transaction的情况下：

添加了@Transaction后，事物的创建就会交给事物管理器去创建，而如果没有添加@Transaction则事物对象只在获取Connection时创建,所以没有添加@Transaction时在同一个
方法中调用不同数据源会创建多个SpringManagedTransaction事物对象，而在这个类中判断this.connection==null时自然为true,所以会新建connection
如果是加上了@Transaction，则表示某个方法的SpringManagedTransaction事物的生命周期交给了事物管理器,在同一个方法中整个事物默认是单利的
所以在这个类中判断this.connection=null时自然为false,所以会重用connection,这样就会导致在@Transaction标注的同一个方法中无法进行多个数据源的切换


org.springframework.transaction.interceptor.TransactionInterceptor.invoke
    
    public Object invoke(final MethodInvocation invocation) throws Throwable {
		// Work out the target class: may be {@code null}.
		// The TransactionAttributeSource should be passed the target class
		// as well as the method, which may be from an interface.
		Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);

		// Adapt to TransactionAspectSupport's invokeWithinTransaction...
		return invokeWithinTransaction(invocation.getMethod(), targetClass, new InvocationCallback() {
			@Override
			public Object proceedWithInvocation() throws Throwable {
			    
				return invocation.proceed();
			}
		});
	}
	
org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction
在这个类中创建事物管理器和事物
    
    protected Object invokeWithinTransaction(Method method, Class<?> targetClass, final InvocationCallback invocation)
			throws Throwable {

		// If the transaction attribute is null, the method is non-transactional.
		//根据调用@Transation拦截的目标方法,创建与之对应的事物信息,如果返回attribute为null,则这个方法表示没有事物
		final TransactionAttribute txAttr = getTransactionAttributeSource().getTransactionAttribute(method, targetClass);
		//将事物交给事物管理器,并返回事物管理器
		final PlatformTransactionManager tm = determineTransactionManager(txAttr);
		final String joinpointIdentification = methodIdentification(method, targetClass, txAttr);

		if (txAttr == null || !(tm instanceof CallbackPreferringPlatformTransactionManager)) {
			// Standard transaction demarcation with getTransaction and commit/rollback calls.
			TransactionInfo txInfo = createTransactionIfNecessary(tm, txAttr, joinpointIdentification);
			Object retVal = null;
			try {
				// This is an around advice: Invoke the next interceptor in the chain.
				// This will normally result in a target object being invoked.
				//执行目标方法，比如ChannelsService.save方法,这个save方法中有多个数据源的操作,不管多少个数据源的操作,此时只有一个
				//事物管理器,这个方法中只有一个SpringManagedTransaction事物对象,
				//所以在执行org.mybatis.spring.transaction.SpringManagedTransaction.getConnection 判断this.connection==null时返回false  (除了第一次 )
				retVal = invocation.proceedWithInvocation();
			}
			catch (Throwable ex) {
				// target invocation exception
				completeTransactionAfterThrowing(txInfo, ex);
				throw ex;
			}
			finally {
				cleanupTransactionInfo(txInfo);
			}
			commitTransactionAfterReturning(txInfo);
			return retVal;
		}

		else {
			// It's a CallbackPreferringPlatformTransactionManager: pass a TransactionCallback in.
			try {
				Object result = ((CallbackPreferringPlatformTransactionManager) tm).execute(txAttr,
						new TransactionCallback<Object>() {
							@Override
							public Object doInTransaction(TransactionStatus status) {
								TransactionInfo txInfo = prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
								try {
									return invocation.proceedWithInvocation();
								}
								catch (Throwable ex) {
									if (txAttr.rollbackOn(ex)) {
										// A RuntimeException: will lead to a rollback.
										if (ex instanceof RuntimeException) {
											throw (RuntimeException) ex;
										}
										else {
											throw new ThrowableHolderException(ex);
										}
									}
									else {
										// A normal return value: will lead to a commit.
										return new ThrowableHolder(ex);
									}
								}
								finally {
									cleanupTransactionInfo(txInfo);
								}
							}
						});

				// Check result: It might indicate a Throwable to rethrow.
				if (result instanceof ThrowableHolder) {
					throw ((ThrowableHolder) result).getThrowable();
				}
				else {
					return result;
				}
			}
			catch (ThrowableHolderException ex) {
				throw ex.getCause();
			}
		}
	}
    
org.apache.ibatis.executor.SimpleExecutor.prepareStatement





从上面我们可以知道，@Transaction开启后，同一个方法中只会有第一个数据源的connection
既然在同一个@Transaction方法中无法进行多数据源的切换,那么如何解决这个问题呢？解决方法就是将多个操作分开，并在开启事物前就设置各自的数据源

方法一：
    分布式事物  （暂不考虑）

方法二：
    使用JTA  (暂不考虑)
    
方法三：
    将多个数据源的操作分开到不同方法中
    比如：
        通过在事物开启前
            
            这个方法在controller中
            @GetMapping("/save")
                public void save(){
                    try {
                        DataSourceContextHolder.clear();
                        DataSourceContextHolder.setDBAlais("db1");
                        channelsService.saveA();
            
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        DataSourceContextHolder.clear();
                        DataSourceContextHolder.setDBAlais("db2");
                        channelsService.saveB();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
            
                }
        
            下面两个方法在service中
             @Transactional
                public void saveA(){
                    //使用默认的数据源
                    Channels channels = new Channels();
                    channels.setName("aa");
                    channels.setAppCode("1");
                    channels.setLevelType("a");
                    channelsMapper.insert(channels);
                    int i=10/0;
                }
            
                @Transactional
                public void saveB(){
                    Channels channels = new Channels();
            
                    channels.setName("bb");
                    channels.setAppCode("2");
                    channels.setLevelType("b");
                    channelsMapper.insert(channels);
                    int i=10/0;
                }
            
     
注意：同一个service的两个方法调用,是不会开启事物的,比如上面的save调用saveA 是不会有事物的，因为spring中采用的是切面开启事物,save方法并没有开启,所以会被认定为没有事物