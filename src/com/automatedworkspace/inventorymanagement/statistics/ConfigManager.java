package com.automatedworkspace.inventorymanagement.statistics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * The type Config manager.
 */
public class ConfigManager {

	/**
	 * The constant JSON_FILE_PATH.
	 */
	private static final String JSON_FILE_PATH = "src/com/automatedworkspace/files/config.json";
	/**
	 * The constant JSON_IN_OUT_FILE_PATH.
	 */
	private static final String JSON_IN_OUT_FILE_PATH = "src/com/automatedworkspace/files/InAndOut.json";


	/**
	 * Write config.
	 *
	 * @param config the config
	 * @throws IOException the io exception
	 */
	public static void writeConfig(Config config) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (Writer writer = new FileWriter(JSON_FILE_PATH)) {
			gson.toJson(config, writer);
		}

	}

	/**
	 * Read config config.
	 *
	 * @return the config
	 * @throws IOException the io exception
	 */
	public static Config readConfig() throws IOException {
		Gson gson = new Gson();
		try (Reader reader = new FileReader(JSON_FILE_PATH)) {
			return gson.fromJson(reader, Config.class);
		}
	}

	/**
	 * Write in out.
	 *
	 * @param config the config
	 * @throws IOException the io exception
	 */
	public static void writeInOut(DeliveryConfig config) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (Writer writer = new FileWriter(JSON_IN_OUT_FILE_PATH)) {
			gson.toJson(config, writer);
		}
	}

	/**
	 * Read in out delivery config.
	 *
	 * @return the delivery config
	 * @throws IOException the io exception
	 */
	public static DeliveryConfig readInOut() throws IOException {
		Gson gson = new Gson();
		try (Reader reader = new FileReader(JSON_IN_OUT_FILE_PATH)) {
			return gson.fromJson(reader, DeliveryConfig.class);
		}
	}


}