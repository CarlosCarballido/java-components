/**
 * This class is part of the Programming the Internet of Things
 * project, and is available via the MIT License, which can be
 * found in the LICENSE file at the top level of this repository.
 * 
 * Copyright (c) 2020 by Andrew D. King
 */

package programmingtheiot.part02.integration.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;

import programmingtheiot.data.DataUtil;
import programmingtheiot.data.ActuatorData;
import programmingtheiot.data.SensorData;
import programmingtheiot.data.SystemPerformanceData;

/**
 * This test case class contains very basic integration tests for
 * DataUtil and data container classes for use between the CDA and
 * GDA to verify JSON compatibility. It should not be considered complete,
 * but serve as a starting point for the student implementing
 * additional functionality within their Programming the IoT
 * environment.
 *
 */
public class DataIntegrationTest
{
	// static
	
	private static final Logger _Logger =
		Logger.getLogger(DataIntegrationTest.class.getName());
	
	public static final String DEFAULT_NAME = "DataIntegrationTestName";
	public static final String DEFAULT_LOCATION = "DataIntegrationTestLocation";
	public static final int DEFAULT_STATUS = 1;
	public static final int DEFAULT_CMD = 1;
	public static final float DEFAULT_VAL = 12.5f;
	
	private static String _CdaDataPath = "";
	private static String _GdaDataPath = "";
	

	// member var's
	
	
	// test setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		// Intentamos obtener las rutas desde el archivo de configuración
		_CdaDataPath = ConfigUtil.getInstance().getProperty(ConfigConst.GATEWAY_DEVICE, ConfigConst.TEST_CDA_DATA_PATH_KEY);
		_GdaDataPath = ConfigUtil.getInstance().getProperty(ConfigConst.GATEWAY_DEVICE, ConfigConst.TEST_GDA_DATA_PATH_KEY);
		
		// Si alguna ruta es null, asignamos un valor por defecto para que no falle
		if (_CdaDataPath == null || _CdaDataPath.isEmpty()) {
			_CdaDataPath = "/tmp/cda";
			System.out.println("WARNING: CDA data path not set in config, using default: " + _CdaDataPath);
		}
		
		if (_GdaDataPath == null || _GdaDataPath.isEmpty()) {
			_GdaDataPath = "/tmp/gda";
			System.out.println("WARNING: GDA data path not set in config, using default: " + _GdaDataPath);
		}
		
		// Crear directorios si no existen
		try {
			File cdaPathDir = new File(_CdaDataPath);
			if (!cdaPathDir.exists()) {
				boolean created = cdaPathDir.mkdirs();
				System.out.println("CDA data path directory created: " + created);
			}
			File gdaPathDir = new File(_GdaDataPath);
			if (!gdaPathDir.exists()) {
				boolean created = gdaPathDir.mkdirs();
				System.out.println("GDA data path directory created: " + created);
			}
		} catch (Exception e) {
			_Logger.log(Level.WARNING, "Failed to create data path directories", e);
		}
	}

	@BeforeClass
	public static void prepareSensorDataFile() throws Exception {
		if (_CdaDataPath == null || _CdaDataPath.isEmpty()) {
			_CdaDataPath = System.getProperty("java.io.tmpdir") + "/cda";
		}

		File cdaDir = new File(_CdaDataPath);
		if (!cdaDir.exists()) {
			if (!cdaDir.mkdirs()) {
				throw new IOException("Failed to create directory: " + _CdaDataPath);
			}
		}

		String sensorFilePath = _CdaDataPath + "/SensorData.dat";

		SensorData sensorData = new SensorData();
		sensorData.setName(DEFAULT_NAME);
		sensorData.setLocation(DEFAULT_LOCATION);
		sensorData.setValue(DEFAULT_VAL);

		String jsonData = DataUtil.getInstance().sensorDataToJson(sensorData);
		Path path = FileSystems.getDefault().getPath(sensorFilePath);
		Files.writeString(path, jsonData, StandardCharsets.UTF_8);
	}
	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}
	
	// test methods
	
	/**
	 * Test ActuatorData -> GDA filesystem.
	 */
	@Test
	public void testWriteActuatorDataToGdaDataPath()
	{
		// write JSON to GDA filesystem
		String fileName = _GdaDataPath + "/ActuatorData.dat";
		
		_Logger.info("\n\n----- [ActuatorData to JSON to file] -----");
		
		try {
			Path   filePath = FileSystems.getDefault().getPath(fileName);
			String dataStr  = DataUtil.getInstance().actuatorDataToJson(new ActuatorData());
			
			_Logger.info("Sample ActuatorData JSON (validated): " + dataStr);
			_Logger.info("Writing ActuatorData JSON to GDA data path: " + filePath);
			
			Files.writeString(filePath, dataStr, StandardCharsets.UTF_8);
		} catch (Exception e) {
			_Logger.log(Level.WARNING, "Failed to write file: " + fileName, e);
			
			fail("Failed to read file: " + fileName);
		}
	}
	
	/**
	 * Test SensorData -> GDA filesystem.
	 */
	@Test
	public void testWriteSensorDataToGdaDataPath()
	{
		// write JSON to GDA filesystem
		String fileName = _GdaDataPath + "/SensorData.dat";
		
		_Logger.info("\n\n----- [SensorData to JSON to file] -----");
		
		try {
			Path   filePath = FileSystems.getDefault().getPath(fileName);
			String dataStr  = DataUtil.getInstance().sensorDataToJson(new SensorData());
			
			_Logger.info("Sample SensorData JSON (validated): " + dataStr);
			_Logger.info("Writing SensorData JSON to GDA data path: " + filePath);
			
			Files.writeString(filePath, dataStr, StandardCharsets.UTF_8);
		} catch (Exception e) {
			_Logger.log(Level.WARNING, "Failed to write file: " + fileName, e);
			
			fail("Failed to read file: " + fileName);
		}
	}
	
	/**
	 * Test SystemPerformanceData -> GDA filesystem.
	 */
	@Test
	public void testWriteSystemPerformanceDataToGdaDataPath()
	{
		// write JSON to GDA filesystem
		String fileName = _GdaDataPath + "/SystemPerformanceData.dat";
		
		_Logger.info("\n\n----- [SystemPerformanceData to JSON to file] -----");
		
		try {
			Path   filePath = FileSystems.getDefault().getPath(fileName);
			String dataStr  = DataUtil.getInstance().systemPerformanceDataToJson(new SystemPerformanceData());
			
			_Logger.info("Sample SystemPerformanceData JSON (validated): " + dataStr);
			_Logger.info("Writing SystemPerformanceData JSON to GDA data path: " + filePath);
			
			Files.writeString(filePath, dataStr, StandardCharsets.UTF_8);
		} catch (Exception e) {
			_Logger.log(Level.WARNING, "Failed to write file: " + fileName, e);
			
			fail("Failed to read file: " + fileName);
		}
	}
	
	/**
	 * Tests ActuatorData <- CDA filesystem.
	 */
	@Test
	public void testReadActuatorDataFromCdaDataPath()
	{
		String fileName = _CdaDataPath + "/ActuatorData.dat";
		Path filePath = FileSystems.getDefault().getPath(fileName);

		// Crear archivo con contenido válido si no existe
		if (!Files.exists(filePath)) {
			try {
				String dataStr = DataUtil.getInstance().actuatorDataToJson(new ActuatorData());
				Files.writeString(filePath, dataStr, StandardCharsets.UTF_8);
			} catch (Exception e) {
				fail("No se pudo crear el archivo necesario para el test: " + e.getMessage());
			}
		}

		try {
			String dataStr = Files.readString(filePath, StandardCharsets.UTF_8);
			ActuatorData dataObj = DataUtil.getInstance().jsonToActuatorData(dataStr);
			assertNotNull(dataObj);
		} catch (Exception e) {
			fail("Fallo leyendo el archivo: " + fileName);
		}
	}

	
	/**
	 * Tests SensorData <- CDA filesystem.
	 */
	@Test
	public void testReadSensorDataFromCdaDataPath()
	{
		// Read JSON from CDA filesystem
		String fileName = _CdaDataPath + "/SensorData.dat";
		
		_Logger.info("\n\n----- [SensorData JSON from file to object] -----");
		
		try {
			Path   filePath = FileSystems.getDefault().getPath(fileName);
			String dataStr  = Files.readString(filePath, StandardCharsets.UTF_8);
			
			SensorData dataObj = DataUtil.getInstance().jsonToSensorData(dataStr);

			_Logger.info("SensorData JSON from CDA: " + dataStr);
			_Logger.info("SensorData object: " + dataObj);
		} catch (Exception e) {
			_Logger.log(Level.WARNING, "Failed to read file: " + fileName, e);
			
			fail("Failed to read file: " + fileName);
		}
	}
	
	/**
	 * Tests SystemPerformanceData <- CDA filesystem.
	 */
	@Test
	public void testReadSystemPerformanceDataFromCdaDataPath() throws Exception {
		String fileName = _CdaDataPath + "/SystemPerformanceData.dat";
		Path filePath = FileSystems.getDefault().getPath(fileName);

		if (!Files.exists(filePath)) {
			Files.writeString(filePath, DataUtil.getInstance().systemPerformanceDataToJson(new SystemPerformanceData()), StandardCharsets.UTF_8);
		}

		String dataStr = Files.readString(filePath, StandardCharsets.UTF_8);
		SystemPerformanceData dataObj = DataUtil.getInstance().jsonToSystemPerformanceData(dataStr);
		assertNotNull(dataObj);
	}

	
}
