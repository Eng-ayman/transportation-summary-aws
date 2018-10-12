package com.transport.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.util.json.Jackson;
import com.transport.model.SummaryData;
import com.transport.model.TransportData;
import com.transport.model.TransportObject;

/**
 * The Class S3LampdaHandler.
 * this handler responsible of receiving an upload notification on this path <br>
 * anybucketName/records/anyPartnerId/file.json from s3 <br>
 * then deserialize that file and make some calculation on it and upload it back on s3 in /summary/partnerId/summary-2018-10-08_23-59-59.txt
 * 
 * @author Ayman Ghaly
 */
public class S3LampdaHandler implements RequestHandler<S3EventNotification, String> {

	/* (non-Javadoc)
	 * @see com.amazonaws.services.lambda.runtime.RequestHandler#handleRequest(java.lang.Object, com.amazonaws.services.lambda.runtime.Context)
	 */
	public String handleRequest(S3EventNotification input, Context context) {

		try {
			context.getLogger().log("Notification Received  ");
			context.getLogger().log("---------------------------------------------------");
			context.getLogger().log(input.toJson());

			AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard().build();

			List<S3EventNotificationRecord> records = input.getRecords();

			if (!records.isEmpty()) {
				String bucketName = records.get(0).getS3().getBucket().getName();
				String fileKey = records.get(0).getS3().getObject().getKey();

				context.getLogger().log("bucketNme:" + bucketName + "   FileKey: " + fileKey);

				if (validateUploadNotification(fileKey)) {

					// get the uploaded file from s3 
					String objectAsString = amazonS3Client.getObjectAsString(bucketName, fileKey);

					//logs
					context.getLogger().log("---------------- file content -----------------");
					context.getLogger().log(objectAsString);

					// bind json file on java model   
					List<TransportObject> allTransportObjects = Jackson
							.fromJsonString(objectAsString, TransportData.class).getTransportRecords();
					
					// calculate summary data
					SummaryData summaryData = calculateSummaryData(allTransportObjects);

					// upload summary object to s3
					context.getLogger().log("summary key:" +getSummaryFileKey(fileKey, bucketName));
					amazonS3Client.putObject(bucketName, getSummaryFileKey(fileKey,bucketName), Jackson.toJsonString(summaryData));

				} else {

					context.getLogger().log(
							"upload operation is not valid , you must upload the file in the right folder ex:- records/partnerId/file.json");
				}

			} else {
				context.getLogger().log("Notification records are empty");

			}

			context.getLogger().log("Notification handling finished ");

			return "success";
		} catch (Exception ex) {
			context.getLogger().log("An exception happned");
			context.getLogger().log(ex.toString());
			return "fail";
		}
	}

	/**
	 * get summary file key it should like like this
	 * summary/partnerId/summary-2018-10-08_23-59-59.
	 *
	 * @param uploadedFileKey the uploaded file key
	 * @return the summary file key
	 * @throws Exception 
	 */
	private String getSummaryFileKey(String uploadedFileKey, String bucketName) throws Exception {
		String partnerId = getPartnerId(uploadedFileKey);
		String currentDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
		
		// the summary file is in json format but i upload it as .txt to avoid receive an upload notification on it again from s3 
		// as the only way i found was straight forward prefix ,suffix 
		return "summary/".concat(partnerId.concat("/summary-").concat(currentDateString).concat(".txt"));
	}

	/**
	 * validate that the file Uploaded in this path records/partnerID/
	 *
	 * @param fileKey the file key
	 * @return true, if successful
	 */
	private boolean validateUploadNotification(String fileKey) {

		boolean isUploadNotificationValid = true;

		String[] filePath = fileKey.split("/");

		if (filePath.length != 3) {
			isUploadNotificationValid = false;
		} else if (!filePath[0].equalsIgnoreCase("records")) {
			isUploadNotificationValid = false;
		}

		return isUploadNotificationValid;
	}

	/**
	 * get partner Id the uploaded file must follow this pattern records/partnerId/file.json 
	 *
	 * @param fileKey the file key
	 * @return the uploaded file path
	 * @throws Exception 
	 */
	private String getPartnerId(String fileKey) throws Exception {

		String[] splitedValues = fileKey.split("/");
		if (splitedValues.length!=3) {
			throw new Exception("wrong file key pattren:-"+fileKey);
		}
		return splitedValues[1];
	}

	/**
	 * Calculate summary data.
	 * @param transportObjects the transport objects
	 * @return the summary data
	 */
	private SummaryData calculateSummaryData(List<TransportObject> transportObjects) {

		SummaryData summaryData = new SummaryData();

		for (TransportObject transObj : transportObjects) {

			switch (transObj.getTransportType()) {
			case CAR: {
				summaryData.accumulateToCarsTotal(transObj.getPassengerCapacity());
			}
				break;
			case TRAIN: {
				summaryData.accumulateToTrainsTotal(transObj.getWagonsNumber() * transObj.getWagonPassengerCapacity());
			}
				break;
			case PLANE: {
				summaryData.accumulateToPlaneTotal(
						transObj.getBussinessPassengerCapacity() + transObj.getEconomyPassengerCapacity());
			}
				break;
			default: {
			}
			}
		}

		return summaryData;
	}
}
