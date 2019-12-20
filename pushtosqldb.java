package test.pushtosqldb_0_1;

import routines.Numeric;
import routines.DataOperation;
import routines.TalendDataGenerator;
import routines.TalendStringUtil;
import routines.TalendString;
import routines.StringHandling;
import routines.Relational;
import routines.TalendDate;
import routines.Mathematical;
import routines.SQLike;
import routines.system.*;
import routines.system.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Comparator;

@SuppressWarnings("unused")
/**
 * Job: pushtosqldb Purpose: <br>
 * Description:  <br>
 * @author user@talend.com
 * @version 6.5.1.20180116_1512
 * @status 
 */
public class pushtosqldb implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "pushtosqldb.log");
	}
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(pushtosqldb.class);

	public final Object obj = new Object();

	// for transmiting parameters purpose
	private Object valueObject = null;

	public Object getValueObject() {
		return this.valueObject;
	}

	public void setValueObject(Object valueObject) {
		this.valueObject = valueObject;
	}

	private final static String defaultCharset = java.nio.charset.Charset
			.defaultCharset().name();

	private final static String utf8Charset = "UTF-8";

	// contains type for every context property
	public class PropertiesWithType extends java.util.Properties {
		private static final long serialVersionUID = 1L;
		private java.util.Map<String, String> propertyTypes = new java.util.HashMap<>();

		public PropertiesWithType(java.util.Properties properties) {
			super(properties);
		}

		public PropertiesWithType() {
			super();
		}

		public void setContextType(String key, String type) {
			propertyTypes.put(key, type);
		}

		public String getContextType(String key) {
			return propertyTypes.get(key);
		}
	}

	// create and load default properties
	private java.util.Properties defaultProps = new java.util.Properties();

	// create application properties with default
	public class ContextProperties extends PropertiesWithType {

		private static final long serialVersionUID = 1L;

		public ContextProperties(java.util.Properties properties) {
			super(properties);
		}

		public ContextProperties() {
			super();
		}

		public void synchronizeContext() {

		}

	}

	private ContextProperties context = new ContextProperties();

	public ContextProperties getContext() {
		return this.context;
	}

	private final String jobVersion = "0.1";
	private final String jobName = "pushtosqldb";
	private final String projectName = "TEST";
	public Integer errorCode = null;
	private String currentComponent = "";

	private final java.util.Map<String, Object> globalMap = new java.util.HashMap<String, Object>();
	private final static java.util.Map<String, Object> junitGlobalMap = new java.util.HashMap<String, Object>();

	private final java.util.Map<String, Long> start_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Long> end_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Boolean> ok_Hash = new java.util.HashMap<String, Boolean>();
	public final java.util.List<String[]> globalBuffer = new java.util.ArrayList<String[]>();

	// OSGi DataSource
	private final static String KEY_DB_DATASOURCES = "KEY_DB_DATASOURCES";

	public void setDataSources(
			java.util.Map<String, javax.sql.DataSource> dataSources) {
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		for (java.util.Map.Entry<String, javax.sql.DataSource> dataSourceEntry : dataSources
				.entrySet()) {
			talendDataSources.put(
					dataSourceEntry.getKey(),
					new routines.system.TalendDataSource(dataSourceEntry
							.getValue()));
		}
		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
	}

	private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	private final java.io.PrintStream errorMessagePS = new java.io.PrintStream(
			new java.io.BufferedOutputStream(baos));

	public String getExceptionStackTrace() {
		if ("failure".equals(this.getStatus())) {
			errorMessagePS.flush();
			return baos.toString();
		}
		return null;
	}

	private Exception exception;

	public Exception getException() {
		if ("failure".equals(this.getStatus())) {
			return this.exception;
		}
		return null;
	}

	private class TalendException extends Exception {

		private static final long serialVersionUID = 1L;

		private java.util.Map<String, Object> globalMap = null;
		private Exception e = null;
		private String currentComponent = null;
		private String virtualComponentName = null;

		public void setVirtualComponentName(String virtualComponentName) {
			this.virtualComponentName = virtualComponentName;
		}

		private TalendException(Exception e, String errorComponent,
				final java.util.Map<String, Object> globalMap) {
			this.currentComponent = errorComponent;
			this.globalMap = globalMap;
			this.e = e;
		}

		public Exception getException() {
			return this.e;
		}

		public String getCurrentComponent() {
			return this.currentComponent;
		}

		public String getExceptionCauseMessage(Exception e) {
			Throwable cause = e;
			String message = null;
			int i = 10;
			while (null != cause && 0 < i--) {
				message = cause.getMessage();
				if (null == message) {
					cause = cause.getCause();
				} else {
					break;
				}
			}
			if (null == message) {
				message = e.getClass().getName();
			}
			return message;
		}

		@Override
		public void printStackTrace() {
			if (!(e instanceof TalendException || e instanceof TDieException)) {
				if (virtualComponentName != null
						&& currentComponent.indexOf(virtualComponentName + "_") == 0) {
					globalMap.put(virtualComponentName + "_ERROR_MESSAGE",
							getExceptionCauseMessage(e));
				}
				globalMap.put(currentComponent + "_ERROR_MESSAGE",
						getExceptionCauseMessage(e));
				System.err.println("Exception in component " + currentComponent
						+ " (" + jobName + ")");
			}
			if (!(e instanceof TDieException)) {
				if (e instanceof TalendException) {
					e.printStackTrace();
				} else {
					e.printStackTrace();
					e.printStackTrace(errorMessagePS);
					pushtosqldb.this.exception = e;
				}
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass()
							.getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(pushtosqldb.this, new Object[] { e,
									currentComponent, globalMap });
							break;
						}
					}

					if (!(e instanceof TDieException)) {
					}
				} catch (Exception e) {
					this.e.printStackTrace();
				}
			}
		}
	}

	public void tAzureStorageGet_1_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tAzureStorageGet_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileList_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileList_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileList_2_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileList_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileInputDelimited_1_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileList_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tMap_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileList_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tMSSqlOutput_1_error(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileList_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tAzureStorageGet_1_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tFileList_1_onSubJobError(Exception exception,
			String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread
				.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(),
				ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tAzureStorageGet_1Process(
			final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tAzureStorageGet_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {

			String currentMethodName = new java.lang.Exception()
					.getStackTrace()[0].getMethodName();
			boolean resumeIt = currentMethodName.equals(resumeEntryMethodName);
			if (resumeEntryMethodName == null || resumeIt || globalResumeTicket) {// start
																					// the
																					// resume
				globalResumeTicket = true;

				/**
				 * [tAzureStorageGet_1 begin ] start
				 */

				ok_Hash.put("tAzureStorageGet_1", false);
				start_Hash
						.put("tAzureStorageGet_1", System.currentTimeMillis());

				currentComponent = "tAzureStorageGet_1";

				int tos_count_tAzureStorageGet_1 = 0;

				class BytesLimit65535_tAzureStorageGet_1 {
					public void limitLog4jByte() throws Exception {

					}
				}

				new BytesLimit65535_tAzureStorageGet_1().limitLog4jByte();

				org.talend.components.api.component.ComponentDefinition def_tAzureStorageGet_1 = new org.talend.components.azurestorage.blob.tazurestorageget.TAzureStorageGetDefinition();

				org.talend.components.azurestorage.blob.tazurestorageget.TAzureStorageGetProperties props_tAzureStorageGet_1 = (org.talend.components.azurestorage.blob.tazurestorageget.TAzureStorageGetProperties) def_tAzureStorageGet_1
						.createRuntimeProperties();
				props_tAzureStorageGet_1.setValue("localFolder",
						"C:/Users/Administrator/Desktop/test");
				props_tAzureStorageGet_1.setValue("dieOnError", false);
				props_tAzureStorageGet_1.setValue("container", "test");
				java.util.List<Object> tAzureStorageGet_1_remoteBlobsGet_create = new java.util.ArrayList<Object>();
				tAzureStorageGet_1_remoteBlobsGet_create.add(true);
				((org.talend.daikon.properties.Properties) props_tAzureStorageGet_1.remoteBlobsGet)
						.setValue("create",
								tAzureStorageGet_1_remoteBlobsGet_create);
				java.util.List<Object> tAzureStorageGet_1_remoteBlobsGet_prefix = new java.util.ArrayList<Object>();
				tAzureStorageGet_1_remoteBlobsGet_prefix.add("raw/");
				((org.talend.daikon.properties.Properties) props_tAzureStorageGet_1.remoteBlobsGet)
						.setValue("prefix",
								tAzureStorageGet_1_remoteBlobsGet_prefix);
				java.util.List<Object> tAzureStorageGet_1_remoteBlobsGet_include = new java.util.ArrayList<Object>();
				tAzureStorageGet_1_remoteBlobsGet_include.add(true);
				((org.talend.daikon.properties.Properties) props_tAzureStorageGet_1.remoteBlobsGet)
						.setValue("include",
								tAzureStorageGet_1_remoteBlobsGet_include);
				props_tAzureStorageGet_1.connection
						.setValue(
								"accountKey",
								routines.system.PasswordEncryptUtil
										.decryptPassword("2b75046ea77c142dd447a19b0e931d2a78b043f570d8eba878466eef3dc9df307cae738a55e315d1fd0b71c6bbc55e3a9cc701b67879e13e84bfb9de9bce9c3ccf0389342883c1951ad97f8e4c1b22d2289c8e31d5b8dde9f4f7aba1746784ea"));
				props_tAzureStorageGet_1.connection.setValue("accountName",
						"adlsgen2check");
				props_tAzureStorageGet_1.connection.setValue(
						"useSharedAccessSignature", false);
				props_tAzureStorageGet_1.connection
						.setValue(
								"protocol",
								org.talend.components.azurestorage.tazurestorageconnection.TAzureStorageConnectionProperties.Protocol.HTTPS);
				props_tAzureStorageGet_1.connection.referencedComponent
						.setValue("referenceDefinitionName",
								"tAzureStorageConnection");
				props_tAzureStorageGet_1.schema
						.setValue(
								"schema",
								new org.apache.avro.Schema.Parser()
										.parse("{\"type\":\"record\",\"name\":\"EmptyRecord\",\"fields\":[]}"));
				if (org.talend.components.api.properties.ComponentReferenceProperties.ReferenceType.COMPONENT_INSTANCE == props_tAzureStorageGet_1.connection.referencedComponent.referenceType
						.getValue()) {
					final String referencedComponentInstanceId_tAzureStorageGet_1 = props_tAzureStorageGet_1.connection.referencedComponent.componentInstanceId
							.getStringValue();
					if (referencedComponentInstanceId_tAzureStorageGet_1 != null) {
						org.talend.daikon.properties.Properties referencedComponentProperties_tAzureStorageGet_1 = (org.talend.daikon.properties.Properties) globalMap
								.get(referencedComponentInstanceId_tAzureStorageGet_1
										+ "_COMPONENT_RUNTIME_PROPERTIES");
						props_tAzureStorageGet_1.connection.referencedComponent
								.setReference(referencedComponentProperties_tAzureStorageGet_1);
					}
				}
				globalMap.put(
						"tAzureStorageGet_1_COMPONENT_RUNTIME_PROPERTIES",
						props_tAzureStorageGet_1);

				org.talend.components.api.container.RuntimeContainer container_tAzureStorageGet_1 = new org.talend.components.api.container.RuntimeContainer() {
					public Object getComponentData(String componentId,
							String key) {
						return globalMap.get(componentId + "_" + key);
					}

					public void setComponentData(String componentId,
							String key, Object data) {
						globalMap.put(componentId + "_" + key, data);
					}

					public String getCurrentComponentId() {
						return "tAzureStorageGet_1";
					}

					public Object getGlobalData(String key) {
						return globalMap.get(key);
					}
				};

				int nb_line_tAzureStorageGet_1 = 0;

				org.talend.components.api.component.ConnectorTopology topology_tAzureStorageGet_1 = null;
				topology_tAzureStorageGet_1 = org.talend.components.api.component.ConnectorTopology.NONE;

				org.talend.daikon.runtime.RuntimeInfo runtime_info_tAzureStorageGet_1 = def_tAzureStorageGet_1
						.getRuntimeInfo(
								org.talend.components.api.component.runtime.ExecutionEngine.DI,
								props_tAzureStorageGet_1,
								topology_tAzureStorageGet_1);
				java.util.Set<org.talend.components.api.component.ConnectorTopology> supported_connector_topologies_tAzureStorageGet_1 = def_tAzureStorageGet_1
						.getSupportedConnectorTopologies();

				org.talend.components.api.component.runtime.RuntimableRuntime componentRuntime_tAzureStorageGet_1 = (org.talend.components.api.component.runtime.RuntimableRuntime) (Class
						.forName(runtime_info_tAzureStorageGet_1
								.getRuntimeClassName()).newInstance());
				org.talend.daikon.properties.ValidationResult initVr_tAzureStorageGet_1 = componentRuntime_tAzureStorageGet_1
						.initialize(container_tAzureStorageGet_1,
								props_tAzureStorageGet_1);

				if (initVr_tAzureStorageGet_1.getStatus() == org.talend.daikon.properties.ValidationResult.Result.ERROR) {
					throw new RuntimeException(
							initVr_tAzureStorageGet_1.getMessage());
				}

				if (componentRuntime_tAzureStorageGet_1 instanceof org.talend.components.api.component.runtime.ComponentDriverInitialization) {
					org.talend.components.api.component.runtime.ComponentDriverInitialization compDriverInitialization_tAzureStorageGet_1 = (org.talend.components.api.component.runtime.ComponentDriverInitialization) componentRuntime_tAzureStorageGet_1;
					compDriverInitialization_tAzureStorageGet_1
							.runAtDriver(container_tAzureStorageGet_1);
				}

				org.talend.components.api.component.runtime.SourceOrSink sourceOrSink_tAzureStorageGet_1 = null;
				if (componentRuntime_tAzureStorageGet_1 instanceof org.talend.components.api.component.runtime.SourceOrSink) {
					sourceOrSink_tAzureStorageGet_1 = (org.talend.components.api.component.runtime.SourceOrSink) componentRuntime_tAzureStorageGet_1;
					org.talend.daikon.properties.ValidationResult vr_tAzureStorageGet_1 = sourceOrSink_tAzureStorageGet_1
							.validate(container_tAzureStorageGet_1);
					if (vr_tAzureStorageGet_1.getStatus() == org.talend.daikon.properties.ValidationResult.Result.ERROR) {
						throw new RuntimeException(
								vr_tAzureStorageGet_1.getMessage());
					}
				}

				/**
				 * [tAzureStorageGet_1 begin ] stop
				 */

				/**
				 * [tAzureStorageGet_1 main ] start
				 */

				currentComponent = "tAzureStorageGet_1";

				tos_count_tAzureStorageGet_1++;

				/**
				 * [tAzureStorageGet_1 main ] stop
				 */

				/**
				 * [tAzureStorageGet_1 end ] start
				 */

				currentComponent = "tAzureStorageGet_1";

				// end of generic

				ok_Hash.put("tAzureStorageGet_1", true);
				end_Hash.put("tAzureStorageGet_1", System.currentTimeMillis());

				/**
				 * [tAzureStorageGet_1 end ] stop
				 */
			}// end the resume

			if (resumeEntryMethodName == null || globalResumeTicket) {
				resumeUtil.addLog("CHECKPOINT",
						"CONNECTION:SUBJOB_OK:tAzureStorageGet_1:OnSubjobOk",
						"", Thread.currentThread().getId() + "", "", "", "",
						"", "");
			}

			tFileList_1Process(globalMap);

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			throw error;
		} finally {

			try {

				/**
				 * [tAzureStorageGet_1 finally ] start
				 */

				currentComponent = "tAzureStorageGet_1";

				// finally of generic

				/**
				 * [tAzureStorageGet_1 finally ] stop
				 */
			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tAzureStorageGet_1_SUBPROCESS_STATE", 1);
	}

	public static class ouputStruct implements
			routines.system.IPersistableRow<ouputStruct> {
		final static byte[] commonByteArrayLock_TEST_pushtosqldb = new byte[0];
		static byte[] commonByteArray_TEST_pushtosqldb = new byte[0];

		public String Serving_Office;

		public String getServing_Office() {
			return this.Serving_Office;
		}

		public String Serving_OfficeCluster;

		public String getServing_OfficeCluster() {
			return this.Serving_OfficeCluster;
		}

		public String Serving_Region;

		public String getServing_Region() {
			return this.Serving_Region;
		}

		public String ClientGroup;

		public String getClientGroup() {
			return this.ClientGroup;
		}

		public String IndustryPractice;

		public String getIndustryPractice() {
			return this.IndustryPractice;
		}

		public String GXCIndustryLevel1;

		public String getGXCIndustryLevel1() {
			return this.GXCIndustryLevel1;
		}

		public String CapabilityPractice;

		public String getCapabilityPractice() {
			return this.CapabilityPractice;
		}

		public String RevenueType;

		public String getRevenueType() {
			return this.RevenueType;
		}

		public String ProspectiveType;

		public String getProspectiveType() {
			return this.ProspectiveType;
		}

		public String Jan_2017;

		public String getJan_2017() {
			return this.Jan_2017;
		}

		public String Feb_2017;

		public String getFeb_2017() {
			return this.Feb_2017;
		}

		public String Mar_2017;

		public String getMar_2017() {
			return this.Mar_2017;
		}

		public String Apr_2017;

		public String getApr_2017() {
			return this.Apr_2017;
		}

		public String May_2017;

		public String getMay_2017() {
			return this.May_2017;
		}

		public String Jun_2017;

		public String getJun_2017() {
			return this.Jun_2017;
		}

		public String Jul_2017;

		public String getJul_2017() {
			return this.Jul_2017;
		}

		public String Aug_2017;

		public String getAug_2017() {
			return this.Aug_2017;
		}

		public String Sep_2017;

		public String getSep_2017() {
			return this.Sep_2017;
		}

		public String Oct_2017;

		public String getOct_2017() {
			return this.Oct_2017;
		}

		public String Nov_2017;

		public String getNov_2017() {
			return this.Nov_2017;
		}

		public String Dec_2017;

		public String getDec_2017() {
			return this.Dec_2017;
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_TEST_pushtosqldb.length) {
					if (length < 1024
							&& commonByteArray_TEST_pushtosqldb.length == 0) {
						commonByteArray_TEST_pushtosqldb = new byte[1024];
					} else {
						commonByteArray_TEST_pushtosqldb = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_TEST_pushtosqldb, 0, length);
				strReturn = new String(commonByteArray_TEST_pushtosqldb, 0,
						length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_TEST_pushtosqldb) {

				try {

					int length = 0;

					this.Serving_Office = readString(dis);

					this.Serving_OfficeCluster = readString(dis);

					this.Serving_Region = readString(dis);

					this.ClientGroup = readString(dis);

					this.IndustryPractice = readString(dis);

					this.GXCIndustryLevel1 = readString(dis);

					this.CapabilityPractice = readString(dis);

					this.RevenueType = readString(dis);

					this.ProspectiveType = readString(dis);

					this.Jan_2017 = readString(dis);

					this.Feb_2017 = readString(dis);

					this.Mar_2017 = readString(dis);

					this.Apr_2017 = readString(dis);

					this.May_2017 = readString(dis);

					this.Jun_2017 = readString(dis);

					this.Jul_2017 = readString(dis);

					this.Aug_2017 = readString(dis);

					this.Sep_2017 = readString(dis);

					this.Oct_2017 = readString(dis);

					this.Nov_2017 = readString(dis);

					this.Dec_2017 = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.Serving_Office, dos);

				// String

				writeString(this.Serving_OfficeCluster, dos);

				// String

				writeString(this.Serving_Region, dos);

				// String

				writeString(this.ClientGroup, dos);

				// String

				writeString(this.IndustryPractice, dos);

				// String

				writeString(this.GXCIndustryLevel1, dos);

				// String

				writeString(this.CapabilityPractice, dos);

				// String

				writeString(this.RevenueType, dos);

				// String

				writeString(this.ProspectiveType, dos);

				// String

				writeString(this.Jan_2017, dos);

				// String

				writeString(this.Feb_2017, dos);

				// String

				writeString(this.Mar_2017, dos);

				// String

				writeString(this.Apr_2017, dos);

				// String

				writeString(this.May_2017, dos);

				// String

				writeString(this.Jun_2017, dos);

				// String

				writeString(this.Jul_2017, dos);

				// String

				writeString(this.Aug_2017, dos);

				// String

				writeString(this.Sep_2017, dos);

				// String

				writeString(this.Oct_2017, dos);

				// String

				writeString(this.Nov_2017, dos);

				// String

				writeString(this.Dec_2017, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("Serving_Office=" + Serving_Office);
			sb.append(",Serving_OfficeCluster=" + Serving_OfficeCluster);
			sb.append(",Serving_Region=" + Serving_Region);
			sb.append(",ClientGroup=" + ClientGroup);
			sb.append(",IndustryPractice=" + IndustryPractice);
			sb.append(",GXCIndustryLevel1=" + GXCIndustryLevel1);
			sb.append(",CapabilityPractice=" + CapabilityPractice);
			sb.append(",RevenueType=" + RevenueType);
			sb.append(",ProspectiveType=" + ProspectiveType);
			sb.append(",Jan_2017=" + Jan_2017);
			sb.append(",Feb_2017=" + Feb_2017);
			sb.append(",Mar_2017=" + Mar_2017);
			sb.append(",Apr_2017=" + Apr_2017);
			sb.append(",May_2017=" + May_2017);
			sb.append(",Jun_2017=" + Jun_2017);
			sb.append(",Jul_2017=" + Jul_2017);
			sb.append(",Aug_2017=" + Aug_2017);
			sb.append(",Sep_2017=" + Sep_2017);
			sb.append(",Oct_2017=" + Oct_2017);
			sb.append(",Nov_2017=" + Nov_2017);
			sb.append(",Dec_2017=" + Dec_2017);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (Serving_Office == null) {
				sb.append("<null>");
			} else {
				sb.append(Serving_Office);
			}

			sb.append("|");

			if (Serving_OfficeCluster == null) {
				sb.append("<null>");
			} else {
				sb.append(Serving_OfficeCluster);
			}

			sb.append("|");

			if (Serving_Region == null) {
				sb.append("<null>");
			} else {
				sb.append(Serving_Region);
			}

			sb.append("|");

			if (ClientGroup == null) {
				sb.append("<null>");
			} else {
				sb.append(ClientGroup);
			}

			sb.append("|");

			if (IndustryPractice == null) {
				sb.append("<null>");
			} else {
				sb.append(IndustryPractice);
			}

			sb.append("|");

			if (GXCIndustryLevel1 == null) {
				sb.append("<null>");
			} else {
				sb.append(GXCIndustryLevel1);
			}

			sb.append("|");

			if (CapabilityPractice == null) {
				sb.append("<null>");
			} else {
				sb.append(CapabilityPractice);
			}

			sb.append("|");

			if (RevenueType == null) {
				sb.append("<null>");
			} else {
				sb.append(RevenueType);
			}

			sb.append("|");

			if (ProspectiveType == null) {
				sb.append("<null>");
			} else {
				sb.append(ProspectiveType);
			}

			sb.append("|");

			if (Jan_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Jan_2017);
			}

			sb.append("|");

			if (Feb_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Feb_2017);
			}

			sb.append("|");

			if (Mar_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Mar_2017);
			}

			sb.append("|");

			if (Apr_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Apr_2017);
			}

			sb.append("|");

			if (May_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(May_2017);
			}

			sb.append("|");

			if (Jun_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Jun_2017);
			}

			sb.append("|");

			if (Jul_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Jul_2017);
			}

			sb.append("|");

			if (Aug_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Aug_2017);
			}

			sb.append("|");

			if (Sep_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Sep_2017);
			}

			sb.append("|");

			if (Oct_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Oct_2017);
			}

			sb.append("|");

			if (Nov_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Nov_2017);
			}

			sb.append("|");

			if (Dec_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Dec_2017);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(ouputStruct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row1Struct implements
			routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_TEST_pushtosqldb = new byte[0];
		static byte[] commonByteArray_TEST_pushtosqldb = new byte[0];

		public String Serving_Office;

		public String getServing_Office() {
			return this.Serving_Office;
		}

		public String Serving_OfficeCluster;

		public String getServing_OfficeCluster() {
			return this.Serving_OfficeCluster;
		}

		public String Serving_Region;

		public String getServing_Region() {
			return this.Serving_Region;
		}

		public String ClientGroup;

		public String getClientGroup() {
			return this.ClientGroup;
		}

		public String IndustryPractice;

		public String getIndustryPractice() {
			return this.IndustryPractice;
		}

		public String GXCIndustryLevel1;

		public String getGXCIndustryLevel1() {
			return this.GXCIndustryLevel1;
		}

		public String CapabilityPractice;

		public String getCapabilityPractice() {
			return this.CapabilityPractice;
		}

		public String RevenueType;

		public String getRevenueType() {
			return this.RevenueType;
		}

		public String ProspectiveType;

		public String getProspectiveType() {
			return this.ProspectiveType;
		}

		public String Jan_2017;

		public String getJan_2017() {
			return this.Jan_2017;
		}

		public String Feb_2017;

		public String getFeb_2017() {
			return this.Feb_2017;
		}

		public String Mar_2017;

		public String getMar_2017() {
			return this.Mar_2017;
		}

		public String Apr_2017;

		public String getApr_2017() {
			return this.Apr_2017;
		}

		public String May_2017;

		public String getMay_2017() {
			return this.May_2017;
		}

		public String Jun_2017;

		public String getJun_2017() {
			return this.Jun_2017;
		}

		public String Jul_2017;

		public String getJul_2017() {
			return this.Jul_2017;
		}

		public String Aug_2017;

		public String getAug_2017() {
			return this.Aug_2017;
		}

		public String Sep_2017;

		public String getSep_2017() {
			return this.Sep_2017;
		}

		public String Oct_2017;

		public String getOct_2017() {
			return this.Oct_2017;
		}

		public String Nov_2017;

		public String getNov_2017() {
			return this.Nov_2017;
		}

		public String Dec_2017;

		public String getDec_2017() {
			return this.Dec_2017;
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_TEST_pushtosqldb.length) {
					if (length < 1024
							&& commonByteArray_TEST_pushtosqldb.length == 0) {
						commonByteArray_TEST_pushtosqldb = new byte[1024];
					} else {
						commonByteArray_TEST_pushtosqldb = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_TEST_pushtosqldb, 0, length);
				strReturn = new String(commonByteArray_TEST_pushtosqldb, 0,
						length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos)
				throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_TEST_pushtosqldb) {

				try {

					int length = 0;

					this.Serving_Office = readString(dis);

					this.Serving_OfficeCluster = readString(dis);

					this.Serving_Region = readString(dis);

					this.ClientGroup = readString(dis);

					this.IndustryPractice = readString(dis);

					this.GXCIndustryLevel1 = readString(dis);

					this.CapabilityPractice = readString(dis);

					this.RevenueType = readString(dis);

					this.ProspectiveType = readString(dis);

					this.Jan_2017 = readString(dis);

					this.Feb_2017 = readString(dis);

					this.Mar_2017 = readString(dis);

					this.Apr_2017 = readString(dis);

					this.May_2017 = readString(dis);

					this.Jun_2017 = readString(dis);

					this.Jul_2017 = readString(dis);

					this.Aug_2017 = readString(dis);

					this.Sep_2017 = readString(dis);

					this.Oct_2017 = readString(dis);

					this.Nov_2017 = readString(dis);

					this.Dec_2017 = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.Serving_Office, dos);

				// String

				writeString(this.Serving_OfficeCluster, dos);

				// String

				writeString(this.Serving_Region, dos);

				// String

				writeString(this.ClientGroup, dos);

				// String

				writeString(this.IndustryPractice, dos);

				// String

				writeString(this.GXCIndustryLevel1, dos);

				// String

				writeString(this.CapabilityPractice, dos);

				// String

				writeString(this.RevenueType, dos);

				// String

				writeString(this.ProspectiveType, dos);

				// String

				writeString(this.Jan_2017, dos);

				// String

				writeString(this.Feb_2017, dos);

				// String

				writeString(this.Mar_2017, dos);

				// String

				writeString(this.Apr_2017, dos);

				// String

				writeString(this.May_2017, dos);

				// String

				writeString(this.Jun_2017, dos);

				// String

				writeString(this.Jul_2017, dos);

				// String

				writeString(this.Aug_2017, dos);

				// String

				writeString(this.Sep_2017, dos);

				// String

				writeString(this.Oct_2017, dos);

				// String

				writeString(this.Nov_2017, dos);

				// String

				writeString(this.Dec_2017, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("Serving_Office=" + Serving_Office);
			sb.append(",Serving_OfficeCluster=" + Serving_OfficeCluster);
			sb.append(",Serving_Region=" + Serving_Region);
			sb.append(",ClientGroup=" + ClientGroup);
			sb.append(",IndustryPractice=" + IndustryPractice);
			sb.append(",GXCIndustryLevel1=" + GXCIndustryLevel1);
			sb.append(",CapabilityPractice=" + CapabilityPractice);
			sb.append(",RevenueType=" + RevenueType);
			sb.append(",ProspectiveType=" + ProspectiveType);
			sb.append(",Jan_2017=" + Jan_2017);
			sb.append(",Feb_2017=" + Feb_2017);
			sb.append(",Mar_2017=" + Mar_2017);
			sb.append(",Apr_2017=" + Apr_2017);
			sb.append(",May_2017=" + May_2017);
			sb.append(",Jun_2017=" + Jun_2017);
			sb.append(",Jul_2017=" + Jul_2017);
			sb.append(",Aug_2017=" + Aug_2017);
			sb.append(",Sep_2017=" + Sep_2017);
			sb.append(",Oct_2017=" + Oct_2017);
			sb.append(",Nov_2017=" + Nov_2017);
			sb.append(",Dec_2017=" + Dec_2017);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (Serving_Office == null) {
				sb.append("<null>");
			} else {
				sb.append(Serving_Office);
			}

			sb.append("|");

			if (Serving_OfficeCluster == null) {
				sb.append("<null>");
			} else {
				sb.append(Serving_OfficeCluster);
			}

			sb.append("|");

			if (Serving_Region == null) {
				sb.append("<null>");
			} else {
				sb.append(Serving_Region);
			}

			sb.append("|");

			if (ClientGroup == null) {
				sb.append("<null>");
			} else {
				sb.append(ClientGroup);
			}

			sb.append("|");

			if (IndustryPractice == null) {
				sb.append("<null>");
			} else {
				sb.append(IndustryPractice);
			}

			sb.append("|");

			if (GXCIndustryLevel1 == null) {
				sb.append("<null>");
			} else {
				sb.append(GXCIndustryLevel1);
			}

			sb.append("|");

			if (CapabilityPractice == null) {
				sb.append("<null>");
			} else {
				sb.append(CapabilityPractice);
			}

			sb.append("|");

			if (RevenueType == null) {
				sb.append("<null>");
			} else {
				sb.append(RevenueType);
			}

			sb.append("|");

			if (ProspectiveType == null) {
				sb.append("<null>");
			} else {
				sb.append(ProspectiveType);
			}

			sb.append("|");

			if (Jan_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Jan_2017);
			}

			sb.append("|");

			if (Feb_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Feb_2017);
			}

			sb.append("|");

			if (Mar_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Mar_2017);
			}

			sb.append("|");

			if (Apr_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Apr_2017);
			}

			sb.append("|");

			if (May_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(May_2017);
			}

			sb.append("|");

			if (Jun_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Jun_2017);
			}

			sb.append("|");

			if (Jul_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Jul_2017);
			}

			sb.append("|");

			if (Aug_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Aug_2017);
			}

			sb.append("|");

			if (Sep_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Sep_2017);
			}

			sb.append("|");

			if (Oct_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Oct_2017);
			}

			sb.append("|");

			if (Nov_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Nov_2017);
			}

			sb.append("|");

			if (Dec_2017 == null) {
				sb.append("<null>");
			} else {
				sb.append(Dec_2017);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row1Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(),
						object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void tFileList_1Process(final java.util.Map<String, Object> globalMap)
			throws TalendException {
		globalMap.put("tFileList_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {

			String currentMethodName = new java.lang.Exception()
					.getStackTrace()[0].getMethodName();
			boolean resumeIt = currentMethodName.equals(resumeEntryMethodName);
			if (resumeEntryMethodName == null || resumeIt || globalResumeTicket) {// start
																					// the
																					// resume
				globalResumeTicket = true;

				row1Struct row1 = new row1Struct();
				ouputStruct ouput = new ouputStruct();

				/**
				 * [tFileList_1 begin ] start
				 */

				int NB_ITERATE_tFileList_2 = 0; // for statistics

				ok_Hash.put("tFileList_1", false);
				start_Hash.put("tFileList_1", System.currentTimeMillis());

				currentComponent = "tFileList_1";

				int tos_count_tFileList_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileList_1 - " + ("Start to work."));
				class BytesLimit65535_tFileList_1 {
					public void limitLog4jByte() throws Exception {

						StringBuilder log4jParamters_tFileList_1 = new StringBuilder();
						log4jParamters_tFileList_1.append("Parameters:");
						log4jParamters_tFileList_1.append("DIRECTORY" + " = "
								+ "\"C:/Users/Administrator/Desktop/test\"");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("LIST_MODE" + " = "
								+ "DIRECTORIES");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("INCLUDSUBDIR"
								+ " = " + "true");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("CASE_SENSITIVE"
								+ " = " + "YES");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("ERROR" + " = "
								+ "false");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("GLOBEXPRESSIONS"
								+ " = " + "true");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("FILES" + " = "
								+ "[]");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("ORDER_BY_NOTHING"
								+ " = " + "true");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("ORDER_BY_FILENAME"
								+ " = " + "false");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("ORDER_BY_FILESIZE"
								+ " = " + "false");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1
								.append("ORDER_BY_MODIFIEDDATE" + " = "
										+ "false");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("ORDER_ACTION_ASC"
								+ " = " + "true");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("ORDER_ACTION_DESC"
								+ " = " + "false");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1.append("IFEXCLUDE" + " = "
								+ "false");
						log4jParamters_tFileList_1.append(" | ");
						log4jParamters_tFileList_1
								.append("FORMAT_FILEPATH_TO_SLASH" + " = "
										+ "false");
						log4jParamters_tFileList_1.append(" | ");
						if (log.isDebugEnabled())
							log.debug("tFileList_1 - "
									+ (log4jParamters_tFileList_1));
					}
				}

				new BytesLimit65535_tFileList_1().limitLog4jByte();

				final StringBuffer log4jSb_tFileList_1 = new StringBuffer();

				String directory_tFileList_1 = "C:/Users/Administrator/Desktop/test";
				final java.util.List<String> maskList_tFileList_1 = new java.util.ArrayList<String>();
				final java.util.List<java.util.regex.Pattern> patternList_tFileList_1 = new java.util.ArrayList<java.util.regex.Pattern>();
				maskList_tFileList_1.add("*");
				for (final String filemask_tFileList_1 : maskList_tFileList_1) {
					String filemask_compile_tFileList_1 = filemask_tFileList_1;

					filemask_compile_tFileList_1 = org.apache.oro.text.GlobCompiler
							.globToPerl5(
									filemask_tFileList_1.toCharArray(),
									org.apache.oro.text.GlobCompiler.DEFAULT_MASK);

					java.util.regex.Pattern fileNamePattern_tFileList_1 = java.util.regex.Pattern
							.compile(filemask_compile_tFileList_1);
					patternList_tFileList_1.add(fileNamePattern_tFileList_1);
				}
				int NB_FILEtFileList_1 = 0;

				final boolean case_sensitive_tFileList_1 = true;
				final java.util.List<java.io.File> list_tFileList_1 = new java.util.ArrayList<java.io.File>();
				final java.util.Set<String> filePath_tFileList_1 = new java.util.HashSet<String>();
				java.io.File file_tFileList_1 = new java.io.File(
						directory_tFileList_1);

				file_tFileList_1.listFiles(new java.io.FilenameFilter() {
					public boolean accept(java.io.File dir, String name) {
						java.io.File file = new java.io.File(dir, name);

						if (!file.isDirectory()) {
							return true;
						} else {

							String fileName_tFileList_1 = file.getName();
							for (final java.util.regex.Pattern fileNamePattern_tFileList_1 : patternList_tFileList_1) {
								if (fileNamePattern_tFileList_1.matcher(
										fileName_tFileList_1).matches()) {
									if (!filePath_tFileList_1.contains(file
											.getAbsolutePath())) {
										list_tFileList_1.add(file);
										filePath_tFileList_1.add(file
												.getAbsolutePath());
									}
								}
							}
							file.listFiles(this);
						}

						return false;
					}
				});
				java.util.Collections.sort(list_tFileList_1);

				log.info("tFileList_1 - Start to list files");

				for (int i_tFileList_1 = 0; i_tFileList_1 < list_tFileList_1
						.size(); i_tFileList_1++) {
					java.io.File files_tFileList_1 = list_tFileList_1
							.get(i_tFileList_1);
					String fileName_tFileList_1 = files_tFileList_1.getName();

					String currentFileName_tFileList_1 = files_tFileList_1
							.getName();
					String currentFilePath_tFileList_1 = files_tFileList_1
							.getAbsolutePath();
					String currentFileDirectory_tFileList_1 = files_tFileList_1
							.getParent();
					String currentFileExtension_tFileList_1 = null;

					if (files_tFileList_1.getName().contains(".")
							&& files_tFileList_1.isFile()) {
						currentFileExtension_tFileList_1 = files_tFileList_1
								.getName().substring(
										files_tFileList_1.getName()
												.lastIndexOf(".") + 1);
					} else {
						currentFileExtension_tFileList_1 = "";
					}

					NB_FILEtFileList_1++;
					globalMap.put("tFileList_1_CURRENT_FILE",
							currentFileName_tFileList_1);
					globalMap.put("tFileList_1_CURRENT_FILEPATH",
							currentFilePath_tFileList_1);
					globalMap.put("tFileList_1_CURRENT_FILEDIRECTORY",
							currentFileDirectory_tFileList_1);
					globalMap.put("tFileList_1_CURRENT_FILEEXTENSION",
							currentFileExtension_tFileList_1);
					globalMap.put("tFileList_1_NB_FILE", NB_FILEtFileList_1);

					log.info("tFileList_1 - Current file or directory path : "
							+ currentFilePath_tFileList_1);

					/**
					 * [tFileList_1 begin ] stop
					 */

					/**
					 * [tFileList_1 main ] start
					 */

					currentComponent = "tFileList_1";

					tos_count_tFileList_1++;

					/**
					 * [tFileList_1 main ] stop
					 */
					NB_ITERATE_tFileList_2++;

					/**
					 * [tFileList_2 begin ] start
					 */

					int NB_ITERATE_tFileInputDelimited_1 = 0; // for statistics

					ok_Hash.put("tFileList_2", false);
					start_Hash.put("tFileList_2", System.currentTimeMillis());

					currentComponent = "tFileList_2";

					int tos_count_tFileList_2 = 0;

					if (log.isDebugEnabled())
						log.debug("tFileList_2 - " + ("Start to work."));
					class BytesLimit65535_tFileList_2 {
						public void limitLog4jByte() throws Exception {

							StringBuilder log4jParamters_tFileList_2 = new StringBuilder();
							log4jParamters_tFileList_2.append("Parameters:");
							log4jParamters_tFileList_2
									.append("DIRECTORY"
											+ " = "
											+ "((String)globalMap.get(\"tFileList_1_CURRENT_FILEPATH\"))");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2.append("LIST_MODE"
									+ " = " + "FILES");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2.append("INCLUDSUBDIR"
									+ " = " + "true");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2.append("CASE_SENSITIVE"
									+ " = " + "YES");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2.append("ERROR" + " = "
									+ "false");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2.append("GLOBEXPRESSIONS"
									+ " = " + "true");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2.append("FILES" + " = "
									+ "[{FILEMASK=" + ("\"*\"") + "}]");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2
									.append("ORDER_BY_NOTHING" + " = " + "true");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2
									.append("ORDER_BY_FILENAME" + " = "
											+ "false");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2
									.append("ORDER_BY_FILESIZE" + " = "
											+ "false");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2
									.append("ORDER_BY_MODIFIEDDATE" + " = "
											+ "false");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2
									.append("ORDER_ACTION_ASC" + " = " + "true");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2
									.append("ORDER_ACTION_DESC" + " = "
											+ "false");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2.append("IFEXCLUDE"
									+ " = " + "false");
							log4jParamters_tFileList_2.append(" | ");
							log4jParamters_tFileList_2
									.append("FORMAT_FILEPATH_TO_SLASH" + " = "
											+ "false");
							log4jParamters_tFileList_2.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileList_2 - "
										+ (log4jParamters_tFileList_2));
						}
					}

					new BytesLimit65535_tFileList_2().limitLog4jByte();

					final StringBuffer log4jSb_tFileList_2 = new StringBuffer();

					String directory_tFileList_2 = ((String) globalMap
							.get("tFileList_1_CURRENT_FILEPATH"));
					final java.util.List<String> maskList_tFileList_2 = new java.util.ArrayList<String>();
					final java.util.List<java.util.regex.Pattern> patternList_tFileList_2 = new java.util.ArrayList<java.util.regex.Pattern>();
					maskList_tFileList_2.add("*");
					for (final String filemask_tFileList_2 : maskList_tFileList_2) {
						String filemask_compile_tFileList_2 = filemask_tFileList_2;

						filemask_compile_tFileList_2 = org.apache.oro.text.GlobCompiler
								.globToPerl5(
										filemask_tFileList_2.toCharArray(),
										org.apache.oro.text.GlobCompiler.DEFAULT_MASK);

						java.util.regex.Pattern fileNamePattern_tFileList_2 = java.util.regex.Pattern
								.compile(filemask_compile_tFileList_2);
						patternList_tFileList_2
								.add(fileNamePattern_tFileList_2);
					}
					int NB_FILEtFileList_2 = 0;

					final boolean case_sensitive_tFileList_2 = true;
					final java.util.List<java.io.File> list_tFileList_2 = new java.util.ArrayList<java.io.File>();
					final java.util.Set<String> filePath_tFileList_2 = new java.util.HashSet<String>();
					java.io.File file_tFileList_2 = new java.io.File(
							directory_tFileList_2);

					file_tFileList_2.listFiles(new java.io.FilenameFilter() {
						public boolean accept(java.io.File dir, String name) {
							java.io.File file = new java.io.File(dir, name);

							if (!file.isDirectory()) {

								String fileName_tFileList_2 = file.getName();
								for (final java.util.regex.Pattern fileNamePattern_tFileList_2 : patternList_tFileList_2) {
									if (fileNamePattern_tFileList_2.matcher(
											fileName_tFileList_2).matches()) {
										if (!filePath_tFileList_2.contains(file
												.getAbsolutePath())) {
											list_tFileList_2.add(file);
											filePath_tFileList_2.add(file
													.getAbsolutePath());
										}
									}
								}
								return true;
							} else {
								file.listFiles(this);
							}

							return false;
						}
					});
					java.util.Collections.sort(list_tFileList_2);

					log.info("tFileList_2 - Start to list files");

					for (int i_tFileList_2 = 0; i_tFileList_2 < list_tFileList_2
							.size(); i_tFileList_2++) {
						java.io.File files_tFileList_2 = list_tFileList_2
								.get(i_tFileList_2);
						String fileName_tFileList_2 = files_tFileList_2
								.getName();

						String currentFileName_tFileList_2 = files_tFileList_2
								.getName();
						String currentFilePath_tFileList_2 = files_tFileList_2
								.getAbsolutePath();
						String currentFileDirectory_tFileList_2 = files_tFileList_2
								.getParent();
						String currentFileExtension_tFileList_2 = null;

						if (files_tFileList_2.getName().contains(".")
								&& files_tFileList_2.isFile()) {
							currentFileExtension_tFileList_2 = files_tFileList_2
									.getName().substring(
											files_tFileList_2.getName()
													.lastIndexOf(".") + 1);
						} else {
							currentFileExtension_tFileList_2 = "";
						}

						NB_FILEtFileList_2++;
						globalMap.put("tFileList_2_CURRENT_FILE",
								currentFileName_tFileList_2);
						globalMap.put("tFileList_2_CURRENT_FILEPATH",
								currentFilePath_tFileList_2);
						globalMap.put("tFileList_2_CURRENT_FILEDIRECTORY",
								currentFileDirectory_tFileList_2);
						globalMap.put("tFileList_2_CURRENT_FILEEXTENSION",
								currentFileExtension_tFileList_2);
						globalMap
								.put("tFileList_2_NB_FILE", NB_FILEtFileList_2);

						log.info("tFileList_2 - Current file or directory path : "
								+ currentFilePath_tFileList_2);

						/**
						 * [tFileList_2 begin ] stop
						 */

						/**
						 * [tFileList_2 main ] start
						 */

						currentComponent = "tFileList_2";

						tos_count_tFileList_2++;

						/**
						 * [tFileList_2 main ] stop
						 */
						NB_ITERATE_tFileInputDelimited_1++;

						/**
						 * [tMSSqlOutput_1 begin ] start
						 */

						ok_Hash.put("tMSSqlOutput_1", false);
						start_Hash.put("tMSSqlOutput_1",
								System.currentTimeMillis());

						currentComponent = "tMSSqlOutput_1";

						int tos_count_tMSSqlOutput_1 = 0;

						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - " + ("Start to work."));
						class BytesLimit65535_tMSSqlOutput_1 {
							public void limitLog4jByte() throws Exception {

								StringBuilder log4jParamters_tMSSqlOutput_1 = new StringBuilder();
								log4jParamters_tMSSqlOutput_1
										.append("Parameters:");
								log4jParamters_tMSSqlOutput_1
										.append("USE_EXISTING_CONNECTION"
												+ " = " + "false");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1.append("DRIVER"
										+ " = " + "MSSQL_PROP");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("HOST"
												+ " = "
												+ "\"gansqlserver.database.windows.net\"");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1.append("PORT"
										+ " = " + "\"1433\"");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("DB_SCHEMA" + " = " + "\"dbo\"");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1.append("DBNAME"
										+ " = " + "\"sample\"");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1.append("USER"
										+ " = " + "\"ganesan\"");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("PASS"
												+ " = "
												+ String.valueOf(
														"ecaa51ff80b877dba70cc6f4b1351f7f")
														.substring(0, 4)
												+ "...");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1.append("TABLE"
										+ " = " + "\"sap\"");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("TABLE_ACTION" + " = "
												+ "CREATE_IF_NOT_EXISTS");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("IDENTITY_INSERT" + " = "
												+ "false");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("DATA_ACTION" + " = "
												+ "INSERT");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("SPECIFY_IDENTITY_FIELD"
												+ " = " + "false");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("SPECIFY_DATASOURCE_ALIAS"
												+ " = " + "false");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("DIE_ON_ERROR" + " = "
												+ "false");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("PROPERTIES" + " = " + "\"\"");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("COMMIT_EVERY" + " = "
												+ "10000");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1.append("ADD_COLS"
										+ " = " + "[]");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("USE_FIELD_OPTIONS" + " = "
												+ "false");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("IGNORE_DATE_OUTOF_RANGE"
												+ " = " + "false");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("ENABLE_DEBUG_MODE" + " = "
												+ "false");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("SUPPORT_NULL_WHERE" + " = "
												+ "false");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("USE_BATCH_SIZE" + " = "
												+ "true");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								log4jParamters_tMSSqlOutput_1
										.append("BATCH_SIZE" + " = " + "10000");
								log4jParamters_tMSSqlOutput_1.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tMSSqlOutput_1 - "
											+ (log4jParamters_tMSSqlOutput_1));
							}
						}

						new BytesLimit65535_tMSSqlOutput_1().limitLog4jByte();

						int nb_line_tMSSqlOutput_1 = 0;
						int nb_line_update_tMSSqlOutput_1 = 0;
						int nb_line_inserted_tMSSqlOutput_1 = 0;
						int nb_line_deleted_tMSSqlOutput_1 = 0;
						int nb_line_rejected_tMSSqlOutput_1 = 0;

						int deletedCount_tMSSqlOutput_1 = 0;
						int updatedCount_tMSSqlOutput_1 = 0;
						int insertedCount_tMSSqlOutput_1 = 0;
						int rejectedCount_tMSSqlOutput_1 = 0;
						String dbschema_tMSSqlOutput_1 = null;
						String tableName_tMSSqlOutput_1 = null;
						boolean whetherReject_tMSSqlOutput_1 = false;

						java.util.Calendar calendar_tMSSqlOutput_1 = java.util.Calendar
								.getInstance();
						long year1_tMSSqlOutput_1 = TalendDate.parseDate(
								"yyyy-MM-dd", "0001-01-01").getTime();
						long year2_tMSSqlOutput_1 = TalendDate.parseDate(
								"yyyy-MM-dd", "1753-01-01").getTime();
						long year10000_tMSSqlOutput_1 = TalendDate.parseDate(
								"yyyy-MM-dd HH:mm:ss", "9999-12-31 24:00:00")
								.getTime();
						long date_tMSSqlOutput_1;

						java.util.Calendar calendar_datetimeoffset_tMSSqlOutput_1 = java.util.Calendar
								.getInstance(java.util.TimeZone
										.getTimeZone("UTC"));

						java.sql.Connection conn_tMSSqlOutput_1 = null;
						String dbUser_tMSSqlOutput_1 = null;
						dbschema_tMSSqlOutput_1 = "dbo";
						String driverClass_tMSSqlOutput_1 = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - "
									+ ("Driver ClassName: ")
									+ (driverClass_tMSSqlOutput_1) + ("."));
						java.lang.Class.forName(driverClass_tMSSqlOutput_1);
						String port_tMSSqlOutput_1 = "1433";
						String dbname_tMSSqlOutput_1 = "sample";
						String url_tMSSqlOutput_1 = "jdbc:sqlserver://"
								+ "gansqlserver.database.windows.net";
						if (!"".equals(port_tMSSqlOutput_1)) {
							url_tMSSqlOutput_1 += ":" + "1433";
						}
						if (!"".equals(dbname_tMSSqlOutput_1)) {
							url_tMSSqlOutput_1 += ";databaseName=" + "sample";

						}
						url_tMSSqlOutput_1 += ";appName=" + projectName + ";"
								+ "";
						dbUser_tMSSqlOutput_1 = "ganesan";

						final String decryptedPassword_tMSSqlOutput_1 = routines.system.PasswordEncryptUtil
								.decryptPassword("ecaa51ff80b877dba70cc6f4b1351f7f");

						String dbPwd_tMSSqlOutput_1 = decryptedPassword_tMSSqlOutput_1;
						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - "
									+ ("Connection attempts to '")
									+ (url_tMSSqlOutput_1)
									+ ("' with the username '")
									+ (dbUser_tMSSqlOutput_1) + ("'."));
						conn_tMSSqlOutput_1 = java.sql.DriverManager
								.getConnection(url_tMSSqlOutput_1,
										dbUser_tMSSqlOutput_1,
										dbPwd_tMSSqlOutput_1);
						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - " + ("Connection to '")
									+ (url_tMSSqlOutput_1)
									+ ("' has succeeded."));

						resourceMap.put("conn_tMSSqlOutput_1",
								conn_tMSSqlOutput_1);

						conn_tMSSqlOutput_1.setAutoCommit(false);
						int commitEvery_tMSSqlOutput_1 = 10000;
						int commitCounter_tMSSqlOutput_1 = 0;

						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - "
									+ ("Connection is set auto commit to '")
									+ (conn_tMSSqlOutput_1.getAutoCommit())
									+ ("'."));
						int batchSize_tMSSqlOutput_1 = 10000;
						int batchSizeCounter_tMSSqlOutput_1 = 0;

						if (dbschema_tMSSqlOutput_1 == null
								|| dbschema_tMSSqlOutput_1.trim().length() == 0) {
							tableName_tMSSqlOutput_1 = "sap";
						} else {
							tableName_tMSSqlOutput_1 = dbschema_tMSSqlOutput_1
									+ "].[" + "sap";
						}
						int count_tMSSqlOutput_1 = 0;

						java.sql.Statement isExistStmt_tMSSqlOutput_1 = conn_tMSSqlOutput_1
								.createStatement();
						boolean whetherExist_tMSSqlOutput_1 = false;
						try {
							isExistStmt_tMSSqlOutput_1
									.execute("SELECT TOP 1 1 FROM ["
											+ tableName_tMSSqlOutput_1 + "]");
							whetherExist_tMSSqlOutput_1 = true;
						} catch (java.lang.Exception e) {
							whetherExist_tMSSqlOutput_1 = false;
						}
						isExistStmt_tMSSqlOutput_1.close();
						if (!whetherExist_tMSSqlOutput_1) {
							java.sql.Statement stmtCreate_tMSSqlOutput_1 = conn_tMSSqlOutput_1
									.createStatement();
							if (log.isDebugEnabled())
								log.debug("tMSSqlOutput_1 - " + ("Creating")
										+ (" table '")
										+ (tableName_tMSSqlOutput_1) + ("'."));
							stmtCreate_tMSSqlOutput_1
									.execute("CREATE TABLE ["
											+ tableName_tMSSqlOutput_1
											+ "]([Serving_Office] VARCHAR(100)  ,[Serving_OfficeCluster] VARCHAR(100)  ,[Serving_Region] VARCHAR(100)  ,[ClientGroup] VARCHAR(100)  ,[IndustryPractice] VARCHAR(100)  ,[GXCIndustryLevel1] VARCHAR(50)  ,[CapabilityPractice] VARCHAR(50)  ,[RevenueType] VARCHAR(50)  ,[ProspectiveType] VARCHAR(50)  ,[Jan_2017] VARCHAR(100)  ,[Feb_2017] VARCHAR(100)  ,[Mar_2017] VARCHAR(100)  ,[Apr_2017] VARCHAR(100)  ,[May_2017] VARCHAR(50)  ,[Jun_2017] VARCHAR(50)  ,[Jul_2017] VARCHAR(50)  ,[Aug_2017] VARCHAR(50)  ,[Sep_2017] VARCHAR(50)  ,[Oct_2017] VARCHAR(50)  ,[Nov_2017] VARCHAR(50)  ,[Dec_2017] VARCHAR(50)  )");
							if (log.isDebugEnabled())
								log.debug("tMSSqlOutput_1 - " + ("Create")
										+ (" table '")
										+ (tableName_tMSSqlOutput_1)
										+ ("' has succeeded."));
							stmtCreate_tMSSqlOutput_1.close();
						}
						String insert_tMSSqlOutput_1 = "INSERT INTO ["
								+ tableName_tMSSqlOutput_1
								+ "] ([Serving_Office],[Serving_OfficeCluster],[Serving_Region],[ClientGroup],[IndustryPractice],[GXCIndustryLevel1],[CapabilityPractice],[RevenueType],[ProspectiveType],[Jan_2017],[Feb_2017],[Mar_2017],[Apr_2017],[May_2017],[Jun_2017],[Jul_2017],[Aug_2017],[Sep_2017],[Oct_2017],[Nov_2017],[Dec_2017]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						java.sql.PreparedStatement pstmt_tMSSqlOutput_1 = conn_tMSSqlOutput_1
								.prepareStatement(insert_tMSSqlOutput_1);

						/**
						 * [tMSSqlOutput_1 begin ] stop
						 */

						/**
						 * [tMap_1 begin ] start
						 */

						ok_Hash.put("tMap_1", false);
						start_Hash.put("tMap_1", System.currentTimeMillis());

						currentComponent = "tMap_1";

						int tos_count_tMap_1 = 0;

						if (log.isDebugEnabled())
							log.debug("tMap_1 - " + ("Start to work."));
						class BytesLimit65535_tMap_1 {
							public void limitLog4jByte() throws Exception {

								StringBuilder log4jParamters_tMap_1 = new StringBuilder();
								log4jParamters_tMap_1.append("Parameters:");
								log4jParamters_tMap_1.append("LINK_STYLE"
										+ " = " + "AUTO");
								log4jParamters_tMap_1.append(" | ");
								log4jParamters_tMap_1
										.append("TEMPORARY_DATA_DIRECTORY"
												+ " = " + "");
								log4jParamters_tMap_1.append(" | ");
								log4jParamters_tMap_1.append("ROWS_BUFFER_SIZE"
										+ " = " + "2000000");
								log4jParamters_tMap_1.append(" | ");
								log4jParamters_tMap_1
										.append("CHANGE_HASH_AND_EQUALS_FOR_BIGDECIMAL"
												+ " = " + "true");
								log4jParamters_tMap_1.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tMap_1 - "
											+ (log4jParamters_tMap_1));
							}
						}

						new BytesLimit65535_tMap_1().limitLog4jByte();

						// ###############################
						// # Lookup's keys initialization
						int count_row1_tMap_1 = 0;

						// ###############################

						// ###############################
						// # Vars initialization
						class Var__tMap_1__Struct {
						}
						Var__tMap_1__Struct Var__tMap_1 = new Var__tMap_1__Struct();
						// ###############################

						// ###############################
						// # Outputs initialization
						int count_ouput_tMap_1 = 0;

						ouputStruct ouput_tmp = new ouputStruct();
						// ###############################

						/**
						 * [tMap_1 begin ] stop
						 */

						/**
						 * [tFileInputDelimited_1 begin ] start
						 */

						ok_Hash.put("tFileInputDelimited_1", false);
						start_Hash.put("tFileInputDelimited_1",
								System.currentTimeMillis());

						currentComponent = "tFileInputDelimited_1";

						int tos_count_tFileInputDelimited_1 = 0;

						if (log.isDebugEnabled())
							log.debug("tFileInputDelimited_1 - "
									+ ("Start to work."));
						class BytesLimit65535_tFileInputDelimited_1 {
							public void limitLog4jByte() throws Exception {

								StringBuilder log4jParamters_tFileInputDelimited_1 = new StringBuilder();
								log4jParamters_tFileInputDelimited_1
										.append("Parameters:");
								log4jParamters_tFileInputDelimited_1
										.append("FILENAME"
												+ " = "
												+ "((String)globalMap.get(\"tFileList_2_CURRENT_FILEPATH\"))");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("CSV_OPTION" + " = " + "false");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("ROWSEPARATOR" + " = "
												+ "\"\\n\"");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("FIELDSEPARATOR" + " = "
												+ "\",\"");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("HEADER" + " = " + "0");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("FOOTER" + " = " + "0");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("LIMIT" + " = " + "");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("REMOVE_EMPTY_ROW" + " = "
												+ "true");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("UNCOMPRESS" + " = " + "false");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("DIE_ON_ERROR" + " = "
												+ "false");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("ADVANCED_SEPARATOR" + " = "
												+ "false");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("RANDOM" + " = " + "false");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("TRIMALL" + " = " + "false");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("TRIMSELECT" + " = "
												+ "[{TRIM=" + ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Serving_Office")
												+ "}, {TRIM=" + ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Serving_OfficeCluster")
												+ "}, {TRIM=" + ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Serving_Region")
												+ "}, {TRIM=" + ("false")
												+ ", SCHEMA_COLUMN="
												+ ("ClientGroup") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("IndustryPractice")
												+ "}, {TRIM=" + ("false")
												+ ", SCHEMA_COLUMN="
												+ ("GXCIndustryLevel1")
												+ "}, {TRIM=" + ("false")
												+ ", SCHEMA_COLUMN="
												+ ("CapabilityPractice")
												+ "}, {TRIM=" + ("false")
												+ ", SCHEMA_COLUMN="
												+ ("RevenueType") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("ProspectiveType")
												+ "}, {TRIM=" + ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Jan_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Feb_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Mar_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Apr_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("May_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Jun_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Jul_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Aug_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Sep_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Oct_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Nov_2017") + "}, {TRIM="
												+ ("false")
												+ ", SCHEMA_COLUMN="
												+ ("Dec_2017") + "}]");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("CHECK_FIELDS_NUM" + " = "
												+ "false");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("CHECK_DATE" + " = " + "false");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("ENCODING" + " = "
												+ "\"ISO-8859-15\"");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("SPLITRECORD" + " = " + "false");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								log4jParamters_tFileInputDelimited_1
										.append("ENABLE_DECODE" + " = "
												+ "false");
								log4jParamters_tFileInputDelimited_1
										.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tFileInputDelimited_1 - "
											+ (log4jParamters_tFileInputDelimited_1));
							}
						}

						new BytesLimit65535_tFileInputDelimited_1()
								.limitLog4jByte();

						final routines.system.RowState rowstate_tFileInputDelimited_1 = new routines.system.RowState();

						int nb_line_tFileInputDelimited_1 = 0;
						org.talend.fileprocess.FileInputDelimited fid_tFileInputDelimited_1 = null;
						try {

							Object filename_tFileInputDelimited_1 = ((String) globalMap
									.get("tFileList_2_CURRENT_FILEPATH"));
							if (filename_tFileInputDelimited_1 instanceof java.io.InputStream) {

								int footer_value_tFileInputDelimited_1 = 0, random_value_tFileInputDelimited_1 = -1;
								if (footer_value_tFileInputDelimited_1 > 0
										|| random_value_tFileInputDelimited_1 > 0) {
									throw new java.lang.Exception(
											"When the input source is a stream,footer and random shouldn't be bigger than 0.");
								}

							}
							try {
								fid_tFileInputDelimited_1 = new org.talend.fileprocess.FileInputDelimited(
										((String) globalMap
												.get("tFileList_2_CURRENT_FILEPATH")),
										"ISO-8859-15", ",", "\n", true, 0, 0,
										-1, -1, false);
							} catch (java.lang.Exception e) {

								log.error("tFileInputDelimited_1 - "
										+ e.getMessage());

								System.err.println(e.getMessage());

							}

							log.info("tFileInputDelimited_1 - Retrieving records from the datasource.");

							while (fid_tFileInputDelimited_1 != null
									&& fid_tFileInputDelimited_1.nextRecord()) {
								rowstate_tFileInputDelimited_1.reset();

								row1 = null;

								boolean whetherReject_tFileInputDelimited_1 = false;
								row1 = new row1Struct();
								try {

									int columnIndexWithD_tFileInputDelimited_1 = 0;

									columnIndexWithD_tFileInputDelimited_1 = 0;

									row1.Serving_Office = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 1;

									row1.Serving_OfficeCluster = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 2;

									row1.Serving_Region = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 3;

									row1.ClientGroup = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 4;

									row1.IndustryPractice = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 5;

									row1.GXCIndustryLevel1 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 6;

									row1.CapabilityPractice = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 7;

									row1.RevenueType = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 8;

									row1.ProspectiveType = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 9;

									row1.Jan_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 10;

									row1.Feb_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 11;

									row1.Mar_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 12;

									row1.Apr_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 13;

									row1.May_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 14;

									row1.Jun_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 15;

									row1.Jul_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 16;

									row1.Aug_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 17;

									row1.Sep_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 18;

									row1.Oct_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 19;

									row1.Nov_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									columnIndexWithD_tFileInputDelimited_1 = 20;

									row1.Dec_2017 = fid_tFileInputDelimited_1
											.get(columnIndexWithD_tFileInputDelimited_1);

									if (rowstate_tFileInputDelimited_1
											.getException() != null) {
										throw rowstate_tFileInputDelimited_1
												.getException();
									}

								} catch (java.lang.Exception e) {
									whetherReject_tFileInputDelimited_1 = true;

									log.error("tFileInputDelimited_1 - "
											+ e.getMessage());

									System.err.println(e.getMessage());
									row1 = null;

								}

								log.debug("tFileInputDelimited_1 - Retrieving the record "
										+ fid_tFileInputDelimited_1
												.getRowNumber() + ".");

								/**
								 * [tFileInputDelimited_1 begin ] stop
								 */

								/**
								 * [tFileInputDelimited_1 main ] start
								 */

								currentComponent = "tFileInputDelimited_1";

								tos_count_tFileInputDelimited_1++;

								/**
								 * [tFileInputDelimited_1 main ] stop
								 */
								// Start of branch "row1"
								if (row1 != null) {

									/**
									 * [tMap_1 main ] start
									 */

									currentComponent = "tMap_1";

									if (log.isTraceEnabled()) {
										log.trace("row1 - "
												+ (row1 == null ? "" : row1
														.toLogString()));
									}

									boolean hasCasePrimitiveKeyWithNull_tMap_1 = false;

									// ###############################
									// # Input tables (lookups)
									boolean rejectedInnerJoin_tMap_1 = false;
									boolean mainRowRejected_tMap_1 = false;

									// ###############################
									{ // start of Var scope

										// ###############################
										// # Vars tables

										Var__tMap_1__Struct Var = Var__tMap_1;// ###############################
										// ###############################
										// # Output tables

										ouput = null;

										// # Output table : 'ouput'
										count_ouput_tMap_1++;

										ouput_tmp.Serving_Office = row1.Serving_Office;
										ouput_tmp.Serving_OfficeCluster = row1.Serving_OfficeCluster;
										ouput_tmp.Serving_Region = row1.Serving_Region;
										ouput_tmp.ClientGroup = row1.ClientGroup;
										ouput_tmp.IndustryPractice = row1.IndustryPractice;
										ouput_tmp.GXCIndustryLevel1 = row1.GXCIndustryLevel1;
										ouput_tmp.CapabilityPractice = row1.CapabilityPractice;
										ouput_tmp.RevenueType = row1.RevenueType;
										ouput_tmp.ProspectiveType = row1.ProspectiveType;
										ouput_tmp.Jan_2017 = row1.Jan_2017;
										ouput_tmp.Feb_2017 = row1.Feb_2017;
										ouput_tmp.Mar_2017 = row1.Mar_2017;
										ouput_tmp.Apr_2017 = row1.Apr_2017;
										ouput_tmp.May_2017 = row1.May_2017;
										ouput_tmp.Jun_2017 = row1.Jun_2017;
										ouput_tmp.Jul_2017 = row1.Jul_2017;
										ouput_tmp.Aug_2017 = row1.Aug_2017;
										ouput_tmp.Sep_2017 = row1.Sep_2017;
										ouput_tmp.Oct_2017 = row1.Oct_2017;
										ouput_tmp.Nov_2017 = row1.Nov_2017;
										ouput_tmp.Dec_2017 = row1.Dec_2017;
										ouput = ouput_tmp;
										log.debug("tMap_1 - Outputting the record "
												+ count_ouput_tMap_1
												+ " of the output table 'ouput'.");

										// ###############################

									} // end of Var scope

									rejectedInnerJoin_tMap_1 = false;

									tos_count_tMap_1++;

									/**
									 * [tMap_1 main ] stop
									 */
									// Start of branch "ouput"
									if (ouput != null) {

										/**
										 * [tMSSqlOutput_1 main ] start
										 */

										currentComponent = "tMSSqlOutput_1";

										if (log.isTraceEnabled()) {
											log.trace("ouput - "
													+ (ouput == null ? ""
															: ouput.toLogString()));
										}

										whetherReject_tMSSqlOutput_1 = false;
										if (ouput.Serving_Office == null) {
											pstmt_tMSSqlOutput_1.setNull(1,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(1,
													ouput.Serving_Office);
										}

										if (ouput.Serving_OfficeCluster == null) {
											pstmt_tMSSqlOutput_1.setNull(2,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1
													.setString(
															2,
															ouput.Serving_OfficeCluster);
										}

										if (ouput.Serving_Region == null) {
											pstmt_tMSSqlOutput_1.setNull(3,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(3,
													ouput.Serving_Region);
										}

										if (ouput.ClientGroup == null) {
											pstmt_tMSSqlOutput_1.setNull(4,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(4,
													ouput.ClientGroup);
										}

										if (ouput.IndustryPractice == null) {
											pstmt_tMSSqlOutput_1.setNull(5,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(5,
													ouput.IndustryPractice);
										}

										if (ouput.GXCIndustryLevel1 == null) {
											pstmt_tMSSqlOutput_1.setNull(6,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(6,
													ouput.GXCIndustryLevel1);
										}

										if (ouput.CapabilityPractice == null) {
											pstmt_tMSSqlOutput_1.setNull(7,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(7,
													ouput.CapabilityPractice);
										}

										if (ouput.RevenueType == null) {
											pstmt_tMSSqlOutput_1.setNull(8,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(8,
													ouput.RevenueType);
										}

										if (ouput.ProspectiveType == null) {
											pstmt_tMSSqlOutput_1.setNull(9,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(9,
													ouput.ProspectiveType);
										}

										if (ouput.Jan_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(10,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(10,
													ouput.Jan_2017);
										}

										if (ouput.Feb_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(11,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(11,
													ouput.Feb_2017);
										}

										if (ouput.Mar_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(12,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(12,
													ouput.Mar_2017);
										}

										if (ouput.Apr_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(13,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(13,
													ouput.Apr_2017);
										}

										if (ouput.May_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(14,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(14,
													ouput.May_2017);
										}

										if (ouput.Jun_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(15,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(15,
													ouput.Jun_2017);
										}

										if (ouput.Jul_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(16,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(16,
													ouput.Jul_2017);
										}

										if (ouput.Aug_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(17,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(17,
													ouput.Aug_2017);
										}

										if (ouput.Sep_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(18,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(18,
													ouput.Sep_2017);
										}

										if (ouput.Oct_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(19,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(19,
													ouput.Oct_2017);
										}

										if (ouput.Nov_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(20,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(20,
													ouput.Nov_2017);
										}

										if (ouput.Dec_2017 == null) {
											pstmt_tMSSqlOutput_1.setNull(21,
													java.sql.Types.VARCHAR);
										} else {
											pstmt_tMSSqlOutput_1.setString(21,
													ouput.Dec_2017);
										}

										pstmt_tMSSqlOutput_1.addBatch();
										nb_line_tMSSqlOutput_1++;

										if (log.isDebugEnabled())
											log.debug("tMSSqlOutput_1 - "
													+ ("Adding the record ")
													+ (nb_line_tMSSqlOutput_1)
													+ (" to the ") + ("INSERT")
													+ (" batch."));
										batchSizeCounter_tMSSqlOutput_1++;

										// ////////batch execute by batch
										// size///////
										class LimitBytesHelper_tMSSqlOutput_1 {
											public int limitBytePart1(
													int counter,
													java.sql.PreparedStatement pstmt_tMSSqlOutput_1)
													throws Exception {
												try {

													if (log.isDebugEnabled())
														log.debug("tMSSqlOutput_1 - "
																+ ("Executing the ")
																+ ("INSERT")
																+ (" batch."));
													for (int countEach_tMSSqlOutput_1 : pstmt_tMSSqlOutput_1
															.executeBatch()) {
														if (countEach_tMSSqlOutput_1 == -2
																|| countEach_tMSSqlOutput_1 == -3) {
															break;
														}
														counter += countEach_tMSSqlOutput_1;
													}

													if (log.isDebugEnabled())
														log.debug("tMSSqlOutput_1 - "
																+ ("The ")
																+ ("INSERT")
																+ (" batch execution has succeeded."));
												} catch (java.sql.BatchUpdateException e) {

													int countSum_tMSSqlOutput_1 = 0;
													for (int countEach_tMSSqlOutput_1 : e
															.getUpdateCounts()) {
														counter += (countEach_tMSSqlOutput_1 < 0 ? 0
																: countEach_tMSSqlOutput_1);
													}

													log.error("tMSSqlOutput_1 - "
															+ (e.getMessage()));
													System.err.println(e
															.getMessage());

												}
												return counter;
											}

											public int limitBytePart2(
													int counter,
													java.sql.PreparedStatement pstmt_tMSSqlOutput_1)
													throws Exception {
												try {

													if (log.isDebugEnabled())
														log.debug("tMSSqlOutput_1 - "
																+ ("Executing the ")
																+ ("INSERT")
																+ (" batch."));
													for (int countEach_tMSSqlOutput_1 : pstmt_tMSSqlOutput_1
															.executeBatch()) {
														if (countEach_tMSSqlOutput_1 == -2
																|| countEach_tMSSqlOutput_1 == -3) {
															break;
														}
														counter += countEach_tMSSqlOutput_1;
													}

													if (log.isDebugEnabled())
														log.debug("tMSSqlOutput_1 - "
																+ ("The ")
																+ ("INSERT")
																+ (" batch execution has succeeded."));
												} catch (java.sql.BatchUpdateException e) {

													for (int countEach_tMSSqlOutput_1 : e
															.getUpdateCounts()) {
														counter += (countEach_tMSSqlOutput_1 < 0 ? 0
																: countEach_tMSSqlOutput_1);
													}

													log.error("tMSSqlOutput_1 - "
															+ (e.getMessage()));
													System.err.println(e
															.getMessage());

												}
												return counter;
											}
										}
										if ((batchSize_tMSSqlOutput_1 > 0)
												&& (batchSize_tMSSqlOutput_1 <= batchSizeCounter_tMSSqlOutput_1)) {

											insertedCount_tMSSqlOutput_1 = new LimitBytesHelper_tMSSqlOutput_1()
													.limitBytePart1(
															insertedCount_tMSSqlOutput_1,
															pstmt_tMSSqlOutput_1);

											batchSizeCounter_tMSSqlOutput_1 = 0;
										}

										// //////////commit every////////////

										commitCounter_tMSSqlOutput_1++;
										if (commitEvery_tMSSqlOutput_1 <= commitCounter_tMSSqlOutput_1) {
											if ((batchSize_tMSSqlOutput_1 > 0)
													&& (batchSizeCounter_tMSSqlOutput_1 > 0)) {

												insertedCount_tMSSqlOutput_1 = new LimitBytesHelper_tMSSqlOutput_1()
														.limitBytePart1(
																insertedCount_tMSSqlOutput_1,
																pstmt_tMSSqlOutput_1);

												batchSizeCounter_tMSSqlOutput_1 = 0;
											}

											if (log.isDebugEnabled())
												log.debug("tMSSqlOutput_1 - "
														+ ("Connection starting to commit ")
														+ (commitCounter_tMSSqlOutput_1)
														+ (" record(s)."));
											conn_tMSSqlOutput_1.commit();

											if (log.isDebugEnabled())
												log.debug("tMSSqlOutput_1 - "
														+ ("Connection commit has succeeded."));
											commitCounter_tMSSqlOutput_1 = 0;
										}

										tos_count_tMSSqlOutput_1++;

										/**
										 * [tMSSqlOutput_1 main ] stop
										 */

									} // End of branch "ouput"

								} // End of branch "row1"

								/**
								 * [tFileInputDelimited_1 end ] start
								 */

								currentComponent = "tFileInputDelimited_1";

							}
						} finally {
							if (!((Object) (((String) globalMap
									.get("tFileList_2_CURRENT_FILEPATH"))) instanceof java.io.InputStream)) {
								if (fid_tFileInputDelimited_1 != null) {
									fid_tFileInputDelimited_1.close();
								}
							}
							if (fid_tFileInputDelimited_1 != null) {
								globalMap.put("tFileInputDelimited_1_NB_LINE",
										fid_tFileInputDelimited_1
												.getRowNumber());

								log.info("tFileInputDelimited_1 - Retrieved records count: "
										+ fid_tFileInputDelimited_1
												.getRowNumber() + ".");

							}
						}

						if (log.isDebugEnabled())
							log.debug("tFileInputDelimited_1 - " + ("Done."));

						ok_Hash.put("tFileInputDelimited_1", true);
						end_Hash.put("tFileInputDelimited_1",
								System.currentTimeMillis());

						/**
						 * [tFileInputDelimited_1 end ] stop
						 */

						/**
						 * [tMap_1 end ] start
						 */

						currentComponent = "tMap_1";

						// ###############################
						// # Lookup hashes releasing
						// ###############################
						log.debug("tMap_1 - Written records count in the table 'ouput': "
								+ count_ouput_tMap_1 + ".");

						if (log.isDebugEnabled())
							log.debug("tMap_1 - " + ("Done."));

						ok_Hash.put("tMap_1", true);
						end_Hash.put("tMap_1", System.currentTimeMillis());

						/**
						 * [tMap_1 end ] stop
						 */

						/**
						 * [tMSSqlOutput_1 end ] start
						 */

						currentComponent = "tMSSqlOutput_1";

						try {
							int countSum_tMSSqlOutput_1 = 0;
							if (pstmt_tMSSqlOutput_1 != null
									&& batchSizeCounter_tMSSqlOutput_1 > 0) {

								if (log.isDebugEnabled())
									log.debug("tMSSqlOutput_1 - "
											+ ("Executing the ") + ("INSERT")
											+ (" batch."));
								for (int countEach_tMSSqlOutput_1 : pstmt_tMSSqlOutput_1
										.executeBatch()) {
									if (countEach_tMSSqlOutput_1 == -2
											|| countEach_tMSSqlOutput_1 == -3) {
										break;
									}
									countSum_tMSSqlOutput_1 += countEach_tMSSqlOutput_1;
								}

								if (log.isDebugEnabled())
									log.debug("tMSSqlOutput_1 - "
											+ ("The ")
											+ ("INSERT")
											+ (" batch execution has succeeded."));
							}

							insertedCount_tMSSqlOutput_1 += countSum_tMSSqlOutput_1;

						} catch (java.sql.BatchUpdateException e) {

							int countSum_tMSSqlOutput_1 = 0;
							for (int countEach_tMSSqlOutput_1 : e
									.getUpdateCounts()) {
								countSum_tMSSqlOutput_1 += (countEach_tMSSqlOutput_1 < 0 ? 0
										: countEach_tMSSqlOutput_1);
							}

							insertedCount_tMSSqlOutput_1 += countSum_tMSSqlOutput_1;

							log.error("tMSSqlOutput_1 - " + (e.getMessage()));
							System.err.println(e.getMessage());

						}
						if (pstmt_tMSSqlOutput_1 != null) {

							pstmt_tMSSqlOutput_1.close();

						}

						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - "
									+ ("Connection starting to commit ")
									+ (commitCounter_tMSSqlOutput_1)
									+ (" record(s)."));
						conn_tMSSqlOutput_1.commit();

						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - "
									+ ("Connection commit has succeeded."));
						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - "
									+ ("Closing the connection to the database."));
						conn_tMSSqlOutput_1.close();
						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - "
									+ ("Connection to the database has closed."));
						resourceMap.put("finish_tMSSqlOutput_1", true);

						nb_line_deleted_tMSSqlOutput_1 = nb_line_deleted_tMSSqlOutput_1
								+ deletedCount_tMSSqlOutput_1;
						nb_line_update_tMSSqlOutput_1 = nb_line_update_tMSSqlOutput_1
								+ updatedCount_tMSSqlOutput_1;
						nb_line_inserted_tMSSqlOutput_1 = nb_line_inserted_tMSSqlOutput_1
								+ insertedCount_tMSSqlOutput_1;
						nb_line_rejected_tMSSqlOutput_1 = nb_line_rejected_tMSSqlOutput_1
								+ rejectedCount_tMSSqlOutput_1;

						globalMap.put("tMSSqlOutput_1_NB_LINE",
								nb_line_tMSSqlOutput_1);
						globalMap.put("tMSSqlOutput_1_NB_LINE_UPDATED",
								nb_line_update_tMSSqlOutput_1);
						globalMap.put("tMSSqlOutput_1_NB_LINE_INSERTED",
								nb_line_inserted_tMSSqlOutput_1);
						globalMap.put("tMSSqlOutput_1_NB_LINE_DELETED",
								nb_line_deleted_tMSSqlOutput_1);
						globalMap.put("tMSSqlOutput_1_NB_LINE_REJECTED",
								nb_line_rejected_tMSSqlOutput_1);

						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - " + ("Has ")
									+ ("inserted") + (" ")
									+ (nb_line_inserted_tMSSqlOutput_1)
									+ (" record(s)."));

						if (log.isDebugEnabled())
							log.debug("tMSSqlOutput_1 - " + ("Done."));

						ok_Hash.put("tMSSqlOutput_1", true);
						end_Hash.put("tMSSqlOutput_1",
								System.currentTimeMillis());

						/**
						 * [tMSSqlOutput_1 end ] stop
						 */

						/**
						 * [tFileList_2 end ] start
						 */

						currentComponent = "tFileList_2";

					}
					globalMap.put("tFileList_2_NB_FILE", NB_FILEtFileList_2);

					log.info("tFileList_2 - File or directory count : "
							+ NB_FILEtFileList_2);

					if (log.isDebugEnabled())
						log.debug("tFileList_2 - " + ("Done."));

					ok_Hash.put("tFileList_2", true);
					end_Hash.put("tFileList_2", System.currentTimeMillis());

					/**
					 * [tFileList_2 end ] stop
					 */

					/**
					 * [tFileList_1 end ] start
					 */

					currentComponent = "tFileList_1";

				}
				globalMap.put("tFileList_1_NB_FILE", NB_FILEtFileList_1);

				log.info("tFileList_1 - File or directory count : "
						+ NB_FILEtFileList_1);

				if (log.isDebugEnabled())
					log.debug("tFileList_1 - " + ("Done."));

				ok_Hash.put("tFileList_1", true);
				end_Hash.put("tFileList_1", System.currentTimeMillis());

				/**
				 * [tFileList_1 end ] stop
				 */
			}// end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent,
					globalMap);

			throw te;
		} catch (java.lang.Error error) {

			throw error;
		} finally {

			try {

				/**
				 * [tFileList_1 finally ] start
				 */

				currentComponent = "tFileList_1";

				/**
				 * [tFileList_1 finally ] stop
				 */

				/**
				 * [tFileList_2 finally ] start
				 */

				currentComponent = "tFileList_2";

				/**
				 * [tFileList_2 finally ] stop
				 */

				/**
				 * [tFileInputDelimited_1 finally ] start
				 */

				currentComponent = "tFileInputDelimited_1";

				/**
				 * [tFileInputDelimited_1 finally ] stop
				 */

				/**
				 * [tMap_1 finally ] start
				 */

				currentComponent = "tMap_1";

				/**
				 * [tMap_1 finally ] stop
				 */

				/**
				 * [tMSSqlOutput_1 finally ] start
				 */

				currentComponent = "tMSSqlOutput_1";

				if (resourceMap.get("finish_tMSSqlOutput_1") == null) {
					if (resourceMap.get("conn_tMSSqlOutput_1") != null) {
						try {

							if (log.isDebugEnabled())
								log.debug("tMSSqlOutput_1 - "
										+ ("Closing the connection to the database."));

							java.sql.Connection ctn_tMSSqlOutput_1 = (java.sql.Connection) resourceMap
									.get("conn_tMSSqlOutput_1");

							ctn_tMSSqlOutput_1.close();

							if (log.isDebugEnabled())
								log.debug("tMSSqlOutput_1 - "
										+ ("Connection to the database has closed."));
						} catch (java.sql.SQLException sqlEx_tMSSqlOutput_1) {
							String errorMessage_tMSSqlOutput_1 = "failed to close the connection in tMSSqlOutput_1 :"
									+ sqlEx_tMSSqlOutput_1.getMessage();

							log.error("tMSSqlOutput_1 - "
									+ (errorMessage_tMSSqlOutput_1));
							System.err.println(errorMessage_tMSSqlOutput_1);
						}
					}
				}

				/**
				 * [tMSSqlOutput_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tFileList_1_SUBPROCESS_STATE", 1);
	}

	public String resuming_logs_dir_path = null;
	public String resuming_checkpoint_path = null;
	public String parent_part_launcher = null;
	private String resumeEntryMethodName = null;
	private boolean globalResumeTicket = false;

	public boolean watch = false;
	// portStats is null, it means don't execute the statistics
	public Integer portStats = null;
	public int portTraces = 4334;
	public String clientHost;
	public String defaultClientHost = "localhost";
	public String contextStr = "Default";
	public boolean isDefaultContext = true;
	public String pid = "0";
	public String rootPid = null;
	public String fatherPid = null;
	public String fatherNode = null;
	public long startTime = 0;
	public boolean isChildJob = false;
	public String log4jLevel = "";

	private boolean execStat = true;

	private ThreadLocal<java.util.Map<String, String>> threadLocal = new ThreadLocal<java.util.Map<String, String>>() {
		protected java.util.Map<String, String> initialValue() {
			java.util.Map<String, String> threadRunResultMap = new java.util.HashMap<String, String>();
			threadRunResultMap.put("errorCode", null);
			threadRunResultMap.put("status", "");
			return threadRunResultMap;
		};
	};

	private PropertiesWithType context_param = new PropertiesWithType();
	public java.util.Map<String, Object> parentContextMap = new java.util.HashMap<String, Object>();

	public String status = "";

	public static void main(String[] args) {
		final pushtosqldb pushtosqldbClass = new pushtosqldb();

		int exitCode = pushtosqldbClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'pushtosqldb' - Done.");
		}

		System.exit(exitCode);
	}

	public String[][] runJob(String[] args) {

		int exitCode = runJobInTOS(args);
		String[][] bufferValue = new String[][] { { Integer.toString(exitCode) } };

		return bufferValue;
	}

	public boolean hastBufferOutputComponent() {
		boolean hastBufferOutput = false;

		return hastBufferOutput;
	}

	public int runJobInTOS(String[] args) {
		// reset status
		status = "";

		String lastStr = "";
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--context_param")) {
				lastStr = arg;
			} else if (lastStr.equals("")) {
				evalParam(arg);
			} else {
				evalParam(lastStr + " " + arg);
				lastStr = "";
			}
		}

		if (!"".equals(log4jLevel)) {
			if ("trace".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.TRACE);
			} else if ("debug".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.DEBUG);
			} else if ("info".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.INFO);
			} else if ("warn".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.WARN);
			} else if ("error".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.ERROR);
			} else if ("fatal".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.FATAL);
			} else if ("off".equalsIgnoreCase(log4jLevel)) {
				log.setLevel(org.apache.log4j.Level.OFF);
			}
			org.apache.log4j.Logger.getRootLogger().setLevel(log.getLevel());
		}
		log.info("TalendJob: 'pushtosqldb' - Start.");

		if (clientHost == null) {
			clientHost = defaultClientHost;
		}

		if (pid == null || "0".equals(pid)) {
			pid = TalendString.getAsciiRandomString(6);
		}

		if (rootPid == null) {
			rootPid = pid;
		}
		if (fatherPid == null) {
			fatherPid = pid;
		} else {
			isChildJob = true;
		}

		try {
			// call job/subjob with an existing context, like:
			// --context=production. if without this parameter, there will use
			// the default context instead.
			java.io.InputStream inContext = pushtosqldb.class.getClassLoader()
					.getResourceAsStream(
							"test/pushtosqldb_0_1/contexts/" + contextStr
									+ ".properties");
			if (isDefaultContext && inContext == null) {

			} else {
				if (inContext != null) {
					// defaultProps is in order to keep the original context
					// value
					defaultProps.load(inContext);
					inContext.close();
					context = new ContextProperties(defaultProps);
				} else {
					// print info and job continue to run, for case:
					// context_param is not empty.
					System.err.println("Could not find the context "
							+ contextStr);
				}
			}

			if (!context_param.isEmpty()) {
				context.putAll(context_param);
				// set types for params from parentJobs
				for (Object key : context_param.keySet()) {
					String context_key = key.toString();
					String context_type = context_param
							.getContextType(context_key);
					context.setContextType(context_key, context_type);

				}
			}
		} catch (java.io.IOException ie) {
			System.err.println("Could not load context " + contextStr);
			ie.printStackTrace();
		}

		// get context value from parent directly
		if (parentContextMap != null && !parentContextMap.isEmpty()) {
		}

		// Resume: init the resumeUtil
		resumeEntryMethodName = ResumeUtil
				.getResumeEntryMethodName(resuming_checkpoint_path);
		resumeUtil = new ResumeUtil(resuming_logs_dir_path, isChildJob, rootPid);
		resumeUtil.initCommonInfo(pid, rootPid, fatherPid, projectName,
				jobName, contextStr, jobVersion);

		List<String> parametersToEncrypt = new java.util.ArrayList<String>();
		// Resume: jobStart
		resumeUtil.addLog("JOB_STARTED", "JOB:" + jobName,
				parent_part_launcher, Thread.currentThread().getId() + "", "",
				"", "", "",
				resumeUtil.convertToJsonText(context, parametersToEncrypt));

		java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap = new java.util.concurrent.ConcurrentHashMap<Object, Object>();
		globalMap.put("concurrentHashMap", concurrentHashMap);

		long startUsedMemory = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		long endUsedMemory = 0;
		long end = 0;

		startTime = System.currentTimeMillis();

		this.globalResumeTicket = true;// to run tPreJob

		this.globalResumeTicket = false;// to run others jobs

		try {
			errorCode = null;
			tAzureStorageGet_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tAzureStorageGet_1) {
			globalMap.put("tAzureStorageGet_1_SUBPROCESS_STATE", -1);

			e_tAzureStorageGet_1.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println((endUsedMemory - startUsedMemory)
					+ " bytes memory increase when running : pushtosqldb");
		}

		int returnCode = 0;
		if (errorCode == null) {
			returnCode = status != null && status.equals("failure") ? 1 : 0;
		} else {
			returnCode = errorCode.intValue();
		}
		resumeUtil.addLog("JOB_ENDED", "JOB:" + jobName, parent_part_launcher,
				Thread.currentThread().getId() + "", "", "" + returnCode, "",
				"", "");

		return returnCode;

	}

	// only for OSGi env
	public void destroy() {

	}

	private java.util.Map<String, Object> getSharedConnections4REST() {
		java.util.Map<String, Object> connections = new java.util.HashMap<String, Object>();

		return connections;
	}

	private void evalParam(String arg) {
		if (arg.startsWith("--resuming_logs_dir_path")) {
			resuming_logs_dir_path = arg.substring(25);
		} else if (arg.startsWith("--resuming_checkpoint_path")) {
			resuming_checkpoint_path = arg.substring(27);
		} else if (arg.startsWith("--parent_part_launcher")) {
			parent_part_launcher = arg.substring(23);
		} else if (arg.startsWith("--watch")) {
			watch = true;
		} else if (arg.startsWith("--stat_port=")) {
			String portStatsStr = arg.substring(12);
			if (portStatsStr != null && !portStatsStr.equals("null")) {
				portStats = Integer.parseInt(portStatsStr);
			}
		} else if (arg.startsWith("--trace_port=")) {
			portTraces = Integer.parseInt(arg.substring(13));
		} else if (arg.startsWith("--client_host=")) {
			clientHost = arg.substring(14);
		} else if (arg.startsWith("--context=")) {
			contextStr = arg.substring(10);
			isDefaultContext = false;
		} else if (arg.startsWith("--father_pid=")) {
			fatherPid = arg.substring(13);
		} else if (arg.startsWith("--root_pid=")) {
			rootPid = arg.substring(11);
		} else if (arg.startsWith("--father_node=")) {
			fatherNode = arg.substring(14);
		} else if (arg.startsWith("--pid=")) {
			pid = arg.substring(6);
		} else if (arg.startsWith("--context_type")) {
			String keyValue = arg.substring(15);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.setContextType(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.setContextType(keyValue.substring(0, index),
							keyValue.substring(index + 1));
				}

			}

		} else if (arg.startsWith("--context_param")) {
			String keyValue = arg.substring(16);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.put(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.put(keyValue.substring(0, index),
							keyValue.substring(index + 1));
				}
			}
		} else if (arg.startsWith("--log4jLevel=")) {
			log4jLevel = arg.substring(13);
		}

	}

	private static final String NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY = "<TALEND_NULL>";

	private final String[][] escapeChars = { { "\\\\", "\\" }, { "\\n", "\n" },
			{ "\\'", "\'" }, { "\\r", "\r" }, { "\\f", "\f" }, { "\\b", "\b" },
			{ "\\t", "\t" } };

	private String replaceEscapeChars(String keyValue) {

		if (keyValue == null || ("").equals(keyValue.trim())) {
			return keyValue;
		}

		StringBuilder result = new StringBuilder();
		int currIndex = 0;
		while (currIndex < keyValue.length()) {
			int index = -1;
			// judege if the left string includes escape chars
			for (String[] strArray : escapeChars) {
				index = keyValue.indexOf(strArray[0], currIndex);
				if (index >= 0) {

					result.append(keyValue.substring(currIndex,
							index + strArray[0].length()).replace(strArray[0],
							strArray[1]));
					currIndex = index + strArray[0].length();
					break;
				}
			}
			// if the left string doesn't include escape chars, append the left
			// into the result
			if (index < 0) {
				result.append(keyValue.substring(currIndex));
				currIndex = currIndex + keyValue.length();
			}
		}

		return result.toString();
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getStatus() {
		return status;
	}

	ResumeUtil resumeUtil = null;
}
/************************************************************************************************
 * 117118 characters generated by Talend Data Integration on the December 20,
 * 2019 5:45:14 AM UTC
 ************************************************************************************************/
